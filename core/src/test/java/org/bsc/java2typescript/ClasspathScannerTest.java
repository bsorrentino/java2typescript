package org.bsc.java2typescript;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * {@link ClasspathScanner} selects the public API surface out of compiled class files.
 */
public class ClasspathScannerTest {

    // --- fixtures: the scanner runs against this test class' own compiled output ---------------

    public static class PublicNested {
        public static class DeepPublic {
        }
    }

    static class PackagePrivateNested {
        /** Public, but unreachable from outside: the enclosing type is not. */
        public static class PublicInsidePackagePrivate {
        }
    }

    public interface PublicIface {
    }

    /** Compiles to an anonymous class (ClasspathScannerTest$1). */
    public static final Runnable ANONYMOUS = new Runnable() {
        @Override
        public void run() {
        }
    };

    private static final String PREFIX = ClasspathScannerTest.class.getName();

    private static Path testClassesDir() throws Exception {
        return Paths.get(ClasspathScannerTest.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI());
    }

    private static Map<String, String> options(String... keyValues) throws Exception {
        final Map<String, String> options = new HashMap<>();
        options.put(ClasspathScanner.OPTION_SCAN, testClassesDir().toString());
        for (int i = 0; i < keyValues.length; i += 2) {
            options.put(keyValues[i], keyValues[i + 1]);
        }
        return options;
    }

    private static List<String> scanNames(Map<String, String> options) {
        return ClasspathScanner.from(options)
                .orElseThrow(() -> new AssertionError("no scanner"))
                .scan()
                .types()
                .stream()
                .map(t -> t.getValue().getName())
                .collect(Collectors.toList());
    }

    private static List<String> fixtureNames() throws Exception {
        return scanNames(options(ClasspathScanner.OPTION_INCLUDE, PREFIX));
    }

    @Test
    public void noScannerWithoutTheScanOption() {
        assertFalse(ClasspathScanner.from(Collections.emptyMap()).isPresent());
        assertFalse(ClasspathScanner.from(
                Collections.singletonMap(ClasspathScanner.OPTION_SCAN, "  ")).isPresent());
    }

    @Test
    public void publicTypesAreIncluded() throws Exception {
        final List<String> names = fixtureNames();

        assertTrue(names.contains(PREFIX));
        assertTrue(names.contains(PREFIX + "$PublicNested"));
        assertTrue(names.contains(PREFIX + "$PublicNested$DeepPublic"));
        assertTrue(names.contains(PREFIX + "$PublicIface"));
    }

    @Test
    public void nonPublicAndUnreachableTypesAreExcluded() throws Exception {
        final List<String> names = fixtureNames();

        assertFalse(names.contains(PREFIX + "$PackagePrivateNested"));
        // Public, but nested in a package-private type: not referenceable from outside.
        assertFalse(names.contains(PREFIX + "$PackagePrivateNested$PublicInsidePackagePrivate"));
        assertFalse(names.contains(PREFIX + "$1"));
    }

    @Test
    public void excludeIsAppliedAfterInclude() throws Exception {
        final List<String> names = scanNames(options(
                ClasspathScanner.OPTION_INCLUDE, PREFIX,
                ClasspathScanner.OPTION_EXCLUDE, PREFIX + "$PublicNested"));

        assertTrue(names.contains(PREFIX));
        assertFalse(names.contains(PREFIX + "$PublicNested"));
        assertFalse(names.contains(PREFIX + "$PublicNested$DeepPublic"));
    }

    @Test
    public void includeNarrowsToMatchingPrefixesOnly() throws Exception {
        final List<String> names = scanNames(options(
                ClasspathScanner.OPTION_INCLUDE, "org.bsc.does.not.exist"));

        assertTrue(names.isEmpty());
    }

    @Test
    public void resultIsOrderedByClassName() throws Exception {
        final List<String> names = fixtureNames();
        final List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);

        assertEquals(sorted, names);
    }

    @Test
    public void missingRootsAreIgnored() throws Exception {
        final Map<String, String> options = new HashMap<>();
        options.put(ClasspathScanner.OPTION_SCAN,
                testClassesDir() + File.pathSeparator + "/no/such/place");
        options.put(ClasspathScanner.OPTION_INCLUDE, PREFIX);

        assertTrue(scanNames(options).contains(PREFIX));
    }

    @Test
    public void jarRootsAreScanned() throws Exception {
        // junit itself is on the test classpath as a jar: scanning it must yield its public API.
        final Path jar = Paths.get(Test.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI());
        final Map<String, String> options = new HashMap<>();
        options.put(ClasspathScanner.OPTION_SCAN, jar.toString());
        options.put(ClasspathScanner.OPTION_INCLUDE, "org.junit.Assert");

        assertTrue(scanNames(options).contains("org.junit.Assert"));
    }

    @Test
    public void skippedClassesAreReportedNotThrown() throws Exception {
        final Optional<ClasspathScanner> scanner = ClasspathScanner.from(
                options(ClasspathScanner.OPTION_INCLUDE, PREFIX));

        // Nothing here is unloadable, so the run is clean; the point is that scan() completes.
        assertTrue(scanner.get().scan().skipped().isEmpty());
    }
}
