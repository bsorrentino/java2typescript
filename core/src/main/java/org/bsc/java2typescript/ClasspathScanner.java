package org.bsc.java2typescript;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Enumerates the types to declare by scanning compiled class files, instead of listing them one by
 * one in {@code @Java2TS(declare = {@Type(...)})}.
 * <p>
 * This exists because listing an entire API surface by hand does not scale: a project with hundreds
 * of public types would need a generated source file just to carry the annotation. Scanning moves
 * that enumeration into the processor, where the classes are already reachable.
 * <p>
 * <b>Classpath requirement.</b> Scanning only decides <em>which</em> names to declare; the classes
 * themselves are loaded through {@link TSType}'s class loader, i.e. the annotation processor
 * classpath. Every scanned root (and the transitive dependencies of the classes in it) must
 * therefore also be on {@code -processorpath}, otherwise the class is unloadable and skipped.
 * <p>
 * <b>Selection rule.</b> A type is included when it is public, non-synthetic, in a named package,
 * and — for nested types — every enclosing type is public too. A public class nested inside a
 * package-private one is not referenceable from outside, so it is not part of the API surface.
 * Anonymous and local classes are excluded, as are classes in the default package (they have no
 * namespace to be declared in).
 *
 * @see TSType#loadClass(String)
 */
public final class ClasspathScanner {

    /** Roots to scan: directories and/or jars, separated by the platform path separator. */
    public static final String OPTION_SCAN = "ts.scan";
    /** Comma-separated binary-name prefixes; when set, only matching classes are scanned. */
    public static final String OPTION_INCLUDE = "ts.scan.include";
    /** Comma-separated binary-name prefixes to skip; applied after {@link #OPTION_INCLUDE}. */
    public static final String OPTION_EXCLUDE = "ts.scan.exclude";

    /**
     * Outcome of a scan: the types to declare, plus the names that were rejected because they could
     * not be loaded or introspected. The skipped names are kept rather than swallowed — a class
     * missing from the processor classpath is usually a build misconfiguration worth seeing.
     */
    public static final class Result {

        private final Set<TSType> types;
        private final List<String> skipped;

        private Result(Set<TSType> types, List<String> skipped) {
            this.types = types;
            this.skipped = skipped;
        }

        static Result empty() {
            return new Result(Collections.emptySet(), Collections.emptyList());
        }

        /** The types to declare, ordered by class name. */
        public Set<TSType> types() {
            return types;
        }

        /** Binary names that matched the filters but could not be loaded, with the failure cause. */
        public List<String> skipped() {
            return skipped;
        }
    }

    private final List<Path> roots;
    private final List<String> includes;
    private final List<String> excludes;

    private ClasspathScanner(List<Path> roots, List<String> includes, List<String> excludes) {
        this.roots = roots;
        this.includes = includes;
        this.excludes = excludes;
    }

    /**
     * Build a scanner from annotation processor options.
     *
     * @param options the processor option map
     * @return        a scanner, or empty when {@link #OPTION_SCAN} is not set
     */
    public static Optional<ClasspathScanner> from(Map<String, String> options) {

        final String scan = options.get(OPTION_SCAN);

        if (scan == null || scan.trim().isEmpty()) {
            return Optional.empty();
        }

        final List<Path> roots = Arrays.stream(scan.split(java.io.File.pathSeparator))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Paths::get)
                .collect(Collectors.toList());

        return Optional.of(new ClasspathScanner(roots,
                prefixes(options.get(OPTION_INCLUDE)),
                prefixes(options.get(OPTION_EXCLUDE))));
    }

    private static List<String> prefixes(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Scan the configured roots.
     *
     * @return the types to declare and the names that could not be loaded
     */
    public Result scan() {

        // TreeMap: the emitted declarations must not reorder between builds.
        final Map<String, TSType> found = new TreeMap<>();
        final List<String> skipped = new ArrayList<>();

        for (final Path root : roots) {
            for (final String binaryName : binaryNames(root)) {
                if (!matchesFilters(binaryName)) {
                    continue;
                }
                try {
                    final Class<?> type = TSType.loadClass(binaryName);
                    if (isPublicApiType(type) && type.getPackage() != null) {
                        // Loading succeeds lazily: a class whose method signatures reference absent
                        // types only fails when its members are read, which would otherwise happen
                        // deep inside a transformer and abort the whole run.
                        checkIntrospectable(type);
                        found.put(binaryName, TSType.of(type));
                    }
                } catch (Throwable t) {
                    // Not declarable: reported with a name attached rather than swallowed, since a
                    // root missing from the processor classpath shows up here first.
                    skipped.add(String.format("%s (%s)", binaryName, t));
                }
            }
        }

        return new Result(new LinkedHashSet<>(found.values()), skipped);
    }

    /**
     * The binary names of every class file under a root, which may be a directory or a jar. A root
     * that does not exist yields nothing: build layouts routinely list output directories that a
     * given module never produces.
     */
    private List<String> binaryNames(Path root) {

        if (Files.isDirectory(root)) {
            try (Stream<Path> files = Files.walk(root)) {
                return files.filter(Files::isRegularFile)
                        .map(root::relativize)
                        .map(Path::toString)
                        .filter(name -> name.endsWith(".class"))
                        .map(name -> toBinaryName(name.replace(java.io.File.separatorChar, '/')))
                        .filter(name -> !isInfoClass(name))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new UncheckedIOException("cannot scan directory " + root, e);
            }
        }

        if (Files.isRegularFile(root)) {
            try (ZipFile zip = new ZipFile(root.toFile())) {
                final List<String> names = new ArrayList<>();
                final Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    // META-INF/versions/N/... duplicates classes under a release-specific path, and
                    // module-info/package-info carry no declarable type.
                    if (name.endsWith(".class") && !name.startsWith("META-INF/")) {
                        final String binaryName = toBinaryName(name);
                        if (!isInfoClass(binaryName)) {
                            names.add(binaryName);
                        }
                    }
                }
                return names;
            } catch (IOException e) {
                throw new UncheckedIOException("cannot scan archive " + root, e);
            }
        }

        return Collections.emptyList();
    }

    private static String toBinaryName(String resourceName) {
        return resourceName.substring(0, resourceName.length() - ".class".length()).replace('/', '.');
    }

    private static boolean isInfoClass(String binaryName) {
        return binaryName.endsWith("package-info") || binaryName.endsWith("module-info");
    }

    private boolean matchesFilters(String binaryName) {

        if (!includes.isEmpty() && includes.stream().noneMatch(binaryName::startsWith)) {
            return false;
        }
        return excludes.stream().noneMatch(binaryName::startsWith);
    }

    /**
     * Force resolution of everything a declaration needs to read, so that a type referencing an
     * absent class is rejected up front instead of throwing from inside a transformer.
     * <p>
     * Class loading resolves references lazily: {@link Class#forName(String, boolean, ClassLoader)}
     * happily returns a class whose method signatures name types that are nowhere on the classpath,
     * and only reading those members raises {@link NoClassDefFoundError}. Optional dependencies make
     * this routine — a UI class referencing JavaFX, a driver referencing a browser library.
     *
     * @param type the class to probe
     */
    public static void checkIntrospectable(Class<?> type) {
        type.getMethods();
        type.getDeclaredMethods();
        type.getFields();
        type.getConstructors();
        type.getInterfaces();
        type.getSuperclass();
    }

    /**
     * Whether a type is part of the public API surface: public itself, and — when nested — enclosed
     * only by public types. Applied recursively, since {@code Outer.Middle.Inner} is reachable only
     * if every level is.
     */
    private static boolean isPublicApiType(Class<?> type) {

        if (!Modifier.isPublic(type.getModifiers()) || type.isSynthetic()) {
            return false;
        }

        final Class<?> enclosing = type.getEnclosingClass();

        if (enclosing == null) {
            return true; // top level
        }
        if (!type.isMemberClass()) {
            return false; // anonymous or local
        }
        return isPublicApiType(enclosing);
    }
}
