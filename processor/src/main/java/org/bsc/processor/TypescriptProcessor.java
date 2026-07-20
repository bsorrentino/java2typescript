package org.bsc.processor;

import org.bsc.java2typescript.TSNamespace;
import org.bsc.java2typescript.TSType;
import org.bsc.java2typescript.Java2TSConverter;
import org.bsc.processor.annotation.Java2TS;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.tools.FileObject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bsc.java2typescript.Java2TSConverter.PREDEFINED_TYPES;

/**
 * <p>
 * Processor for annotations in {@link org.bsc.processor.annotation}.
 * </p>
 * <p>
 * Supported options:
 * </p>
 * <ul>
 *     <li>{@code ts.outfile}: target file for typescript declarations</li>
 *     <li>{@code compatibility}: specify compatibility with a given script engine
 *     (NASHORN, RHINO, V8)</li>
 * </ul>
 */
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("org.bsc.processor.annotation.*")
@SupportedOptions({"ts.outfile", "compatibility", "ts.registry"})
@org.kohsuke.MetaInfServices(javax.annotation.processing.Processor.class)
public class TypescriptProcessor extends AbstractProcessorEx {

  final static String ENDL = ";\n";

  static final List<TSType> REQUIRED_TYPES = Arrays.asList(
      TSType.of(java.lang.String.class).setExport(true),
      TSType.of(java.lang.Iterable.class).setExport(true).setFunctional(true),
      TSType.of(java.util.Iterator.class),
      TSType.of(java.util.Collection.class),
      TSType.of(java.util.List.class),
      TSType.of(java.util.Set.class),
      TSType.of(java.util.Map.class),
      TSType.of(java.util.Optional.class).setExport(true),
      TSType.of(java.util.stream.Stream.class).setExport(true),

      // Utility class(s)
      TSType.of(java.util.stream.Collectors.class).setExport(true),
      TSType.of(java.util.Collections.class).setExport(true),

      // Native functional interface(s)
      TSType.of(java.util.function.Function.class).setAlias("Func"),
      TSType.of(java.util.function.BiFunction.class).setAlias("BiFunction"),
      TSType.of(java.util.function.Consumer.class).setAlias("Consumer"),
      TSType.of(java.util.function.BiConsumer.class).setAlias("BiConsumer"),
      TSType.of(java.util.function.UnaryOperator.class).setAlias("UnaryOperator"),
      TSType.of(java.util.function.BinaryOperator.class).setAlias("BinaryOperator"),
      TSType.of(java.util.function.Supplier.class).setAlias("Supplier"),
      TSType.of(java.util.function.Predicate.class).setAlias("Predicate"),
      TSType.of(java.util.function.BiPredicate.class).setAlias("BiPredicate"),
      TSType.of(java.lang.Runnable.class),
      TSType.of(java.lang.Comparable.class)

      // Declare Functional Interface(s)
  );

  /**
   * Create a writer for an output file under the {@code j2ts} source-output folder.
   *
   * @param file target file
   * @return     a writer for the output file
   * @throws IOException if an I/O error occurs
   */
  private java.io.Writer createWriter(Path file) throws IOException {

    final FileObject out = super.getSourceOutputFile(Paths.get("j2ts"), file);

    info("output file [%s]", out.getName());

    return out.openWriter();
  }

  /**
   * Read a classpath resource (a header template) as a UTF-8 string.
   *
   * @param header header resource name
   * @return       the resource content
   * @throws IOException if an I/O error occurs
   */
  private String readResource(String header) throws IOException {
    try (final java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(header)) {
      return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
    }
  }

  /**
   * Open a file for output, prepending a header template whose placeholders have been substituted.
   *
   * @param file          target file
   * @param header        header template resource to prepend to the output
   * @param substitutions placeholder -&gt; replacement map applied to the header
   * @return              a writer for the output file, positioned after the header
   * @throws IOException if an I/O error occurs
   */
  private java.io.Writer openFile(Path file, String header, java.util.Map<String, String> substitutions) throws IOException {

    final java.io.Writer w = createWriter(file);

    String content = readResource(header);
    for (final java.util.Map.Entry<String, String> e : substitutions.entrySet()) {
      content = content.replace(e.getKey(), e.getValue());
    }
    w.write(content);

    return w;
  }
  @Override
  public boolean process(Context processingContext) throws Exception {

    final String targetDefinitionFile = processingContext.getOptionMap().getOrDefault("ts.outfile", "out");

    final String definitionsFile = targetDefinitionFile.concat(".d.ts");
    final String typesFile = targetDefinitionFile.concat("-types.ts");
    final String registryFile = targetDefinitionFile.concat("-registry.d.ts");

    // Name of the generated type-registry interface. Configurable so distinct projects can each
    // produce their own registry (e.g. ZapApiTypeRegistry, ZapAddonApiTypeRegistry).
    final String registryName =
        processingContext.getOptionMap().getOrDefault("ts.registry", "JavaTypeRegistry");

    // Substituted into the header templates (re-enables the typed Java.type overload).
    final java.util.Map<String, String> headerSubstitutions =
        Collections.singletonMap("{{REGISTRY_TYPE}}", registryName);

    final String foreignObjectPrototype =
            processingContext.getOptionMap()
                    .getOrDefault( "foreignobjectprototype", "false");

    final String compatibilityOption =
        processingContext.getOptionMap()
            .getOrDefault("compatibility", "NASHORN") ;
    info("COMPATIBILITY WITH [%s]", compatibilityOption);

    final Java2TSConverter converter = Java2TSConverter.builder()
                                                    .compatibility( compatibilityOption  )
                                                    .foreignObjectPrototype( foreignObjectPrototype )
                                                    .build();

    try (
        final java.io.Writer wD = openFile(Paths.get(definitionsFile), converter.isRhino() ? "headerD-rhino.ts" : "headerD.ts", headerSubstitutions);
        final java.io.Writer wT = openFile(Paths.get(typesFile), "headerT.ts", headerSubstitutions);
        final java.io.Writer wR = createWriter(Paths.get(registryFile));
    ) {

      final Consumer<String> wD_append = s -> {
        try {
          wD.append(s);
        } catch (IOException e) {
          error("error adding [%s]", s);
        }
      };
      final Consumer<String> wT_append = s -> {
        try {
          wT.append(s);
        } catch (IOException e) {
          error("error adding [%s]", s);
        }
      };
      final Consumer<String> wR_append = s -> {
        try {
          wR.append(s);
        } catch (IOException e) {
          error("error adding [%s]", s);
        }
      };

      final List<TSNamespace> namespaces = enumerateDeclaredPackageAndClass(processingContext);
			info( "==> detected namespaces");
			namespaces.forEach(ns -> info( String.valueOf(ns) ));
			info( "<== detected namespaces");


      final Set<TSType> types = new HashSet<>(PREDEFINED_TYPES);
      types.addAll(REQUIRED_TYPES);

      namespaces.forEach(ns -> types.addAll(ns.types()));

      final java.util.Map<String, TSType> declaredTypes =
          types.stream()
              .collect(Collectors.toMap(tt -> tt.getValue().getName(), tt -> tt));

      types.stream()
          .filter(tt -> !PREDEFINED_TYPES.contains(tt))
          .map(tt -> converter.javaClass2DeclarationTransformer(0, tt, declaredTypes))
          .sorted()
          .forEach(wD_append);

      wT_append.accept(String.format("/// <reference path=\"%s\"/>\n\n", definitionsFile));

      types.stream()
          .filter(TSType::isExport)
          .map(t -> converter.javaClass2StaticDefinitionTransformer(t, declaredTypes))
          .sorted()
          .forEach(wT_append);

      // Type registry: maps each Java.type(...) string key to the concrete class value so that a
      // call with a known key is strongly typed. Only classes/enums are eligible: a TypeScript
      // interface has no value form and cannot be referenced with `typeof`, so Java interfaces are
      // excluded and fall through to the generic `type(k:string):T` overload.
      wR_append.accept(String.format("/// <reference path=\"%s\"/>\n\n", definitionsFile));
      wR_append.accept(String.format("interface %s {\n", registryName));

      types.stream()
          .filter(tt -> !PREDEFINED_TYPES.contains(tt))
          .filter(TSType::supportNamespace)                 // skip aliased types (no namespace path)
          .filter(tt -> !tt.getValue().isInterface())       // `typeof` needs a value
          .filter(tt -> tt.getValue().getPackage() != null) // skip the default (unnamed) package
          .map(tt -> String.format("  \"%s\": typeof %s;\n", tt.getValue().getName(), tt.getTypeName()))
          .distinct()
          .sorted()
          .forEach(wR_append);

      wR_append.accept("}\n");

    } // end try-with-resources

    return true;
  }

  /**
   * Test if the given annotation is a {@link Java2TS}.
   *
   * @param am
   *            the annotation to test
   * @return true if the annotation is a {@link Java2TS}
   */
  private boolean isJava2TS(AnnotationMirror am) {

    info("'%s'='%s'", am.getAnnotationType().toString(), Java2TS.class.getName());
    return am.getAnnotationType().toString().equals(Java2TS.class.getName());

  }
  /**
   * Processes the given annotation to generate a typescript declaration
   *
   * @param am the annotation to process
   *
   * @return the typescript declaration
   */
  private TSNamespace toNamespace(AnnotationMirror am) {

    final Function<AnnotationValue, Set<TSType>> mapTypes = (value) ->
        ((List<? extends AnnotationValue>) value.getValue())
            .stream()
            .map(AnnotationValue::getValue)
            .filter(v -> v instanceof AnnotationMirror)
            .map(v -> toMapObject((AnnotationMirror) v, TSType::of))
            .collect(Collectors.toSet());

    final java.util.Map<? extends ExecutableElement, ? extends AnnotationValue> elementsValues = am.getElementValues();

    // elementsValues.entrySet().forEach( e -> System.out.printf( "===> elementValues.get('%s')=%s\n", e.getKey().getClass(), e.getValue()));

    final Set<TSType> types =
        elementsValues.entrySet().stream()
        .filter( e -> String.valueOf(e.getKey()).startsWith("declare"))
        .map(v -> mapTypes.apply(v.getValue()))
        .findFirst()
        .orElse(Collections.emptySet());

    // System.out.printf( "===> types size=%s\n", types.size());

    final String name = elementsValues.entrySet().stream()
        .filter( e -> String.valueOf(e.getKey()).equals("name"))
        .map(v -> String.valueOf(v.getValue()))
        .findFirst()
        .orElse("unnamed");

    return TSNamespace.of(name, types);

  }

  /**
   *
   * @param processingContext
   * @return
   */
  private List<TSNamespace> enumerateDeclaredPackageAndClass(final Context processingContext) {

    return
        processingContext.elementFromAnnotations().stream()
            .peek(e -> info("Annotation [%s]", e.getKind().name()))
            .filter(e -> ElementKind.PACKAGE == e.getKind() || ElementKind.CLASS == e.getKind())
            .flatMap(e -> e.getAnnotationMirrors().stream().filter(this::isJava2TS))
            .map(this::toNamespace)
            .collect(Collectors.toList())
        ;
  }
}
