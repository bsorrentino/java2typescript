package org.bsc.java2typescript.transformer;

import org.bsc.java2typescript.TSConverterContext;
import org.bsc.java2typescript.TSConverterStatic;
import org.bsc.java2typescript.TSTransformer;
import org.bsc.java2typescript.TSType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.bsc.java2typescript.Java2TSConverter.Compatibility.GRAALJS;

/**
 *
 */
public class TSJavaClass2DeclarationTransformer extends TSConverterStatic implements TSTransformer {

    /**
     *
     * @param md
     * @return
     */
    /** Erased name+parameter-types signature, used to match a method against an inherited one. */
    private static String methodSig(Method m) {
        return m.getName() + Stream.of(m.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.joining(",", "(", ")"));
    }

    /**
     * The return types the parent chain declares for each signature.
     * <p>
     * A signature can map to several: a parent that itself narrows a grandparent's return type
     * leaves both declarations visible through {@link Class#getMethods()}. Bridges are excluded so
     * only real declarations are compared.
     */
    private static Map<String, Set<Class<?>>> returnTypesBySig(Class<?> type) {
        return Stream.of(type.getMethods())
                .filter(m -> !m.isBridge() && !m.isSynthetic())
                .collect(Collectors.groupingBy(
                        TSJavaClass2DeclarationTransformer::methodSig,
                        Collectors.mapping(Method::getReturnType, Collectors.toSet())));
    }

    /**
     * Whether a method is inherited from the parent unchanged, and so needs no declaration of its
     * own — {@code extends} already provides it.
     * <p>
     * The signature alone is not enough: a covariant override has the same name and parameters as
     * the method it overrides and differs only in its return type, so matching on signature would
     * discard exactly the narrowing the override exists to express.
     */
    private static boolean isInheritedUnchanged(Method m, Map<String, Set<Class<?>>> parentReturnTypes) {
        return parentReturnTypes.getOrDefault(methodSig(m), Collections.emptySet())
                .contains(m.getReturnType());
    }

    protected boolean testMethodNotAllowed(Method md ) {
        final String name = md.getName();
        return !(name.contains("$")     || // remove unnamed
                name.equals("getClass") ||
                name.equals("hashCode") ||
                name.equals("wait")     ||
                name.equals("notify")   ||
                name.equals("notifyAll"))
                ;
    }

    /**
     *
     * @param md
     * @return
     */
    private boolean testMethodsNotAllowedInForeignObjectPrototypeOnList( Method md ) {
        final String name = md.getName();

        return !(name.equals("forEach")      ||
                name.equals("indexOf")      ||
                name.equals("lastIndexOf")  ||
                name.equals("sort"))
                ;
    }

    /**
     *
     * @param ctx
     * @return
     */
    private boolean isForeignObjectPrototypeOptionEnabled(TSConverterContext ctx) {
        return ctx.options.compatibility == GRAALJS &&
                ctx.options.foreignObjectPrototype;
    }

    /**
     *
     * @param ctx
     * @return
     */
    protected Stream<Method> getMethodsAsStream(TSConverterContext ctx) {

        if(  isForeignObjectPrototypeOptionEnabled(ctx) &&
                ctx.type.getValue().equals(java.util.List.class)) {

            return ctx.type.getMethodsAsStream()
                    .filter( this::testMethodsNotAllowedInForeignObjectPrototypeOnList );
        }

        return ctx.type.getMethodsAsStream();
    }

    /**
     *
     * @param ctx
     * @return
     */
    protected TSConverterContext getClassDecl(TSConverterContext ctx) {

        if(  isForeignObjectPrototypeOptionEnabled(ctx) &&
                ctx.type.getValue().equals(java.util.List.class)) {
            return ctx.append("interface List<E> extends Array<E>/* extends Collection<E> */ {");
        }

        return ctx.getClassDecl();
    }
    /**
     *
     * @param ctx
     * @return
     */
    public TSConverterContext apply(TSConverterContext ctx) {

        final TSType tstype = ctx.type;

        
        if (ctx.options.ignoreDeprecated && tstype.getValue().getAnnotation(Deprecated.class) != null)
            return ctx;
        if (tstype.getValue().isEnum())
            return applyEnum(ctx, tstype);

        // When the class really `extends` an emitted parent (see getClassDecl), the parent's
        // members are inherited in TS and must not be re-listed. Drop every method whose name is
        // fully covered by the parent; keep ALL overloads of any name the class also declares a new
        // overload of (a subclass method declaration shadows the parent's overloads of that name in
        // TS, so a partial list would lose the inherited ones).
        final Class<?> emittedSuper = emittedNonGenericSuperclass(tstype.getValue(), ctx.declaredTypeMap);

        Set<Method> methods = getMethodsAsStream(ctx)
                                .filter( md -> !ctx.options.ignoreDeprecated || md.getAnnotation(Deprecated.class) == null)
                                .collect(Collectors.toSet());
        if (emittedSuper != null) {
            final Map<String, Set<Class<?>>> parentReturnTypes = returnTypesBySig(emittedSuper);
            final Set<String> newNames = methods.stream()
                    .filter(m -> !isInheritedUnchanged(m, parentReturnTypes))
                    .map(Method::getName)
                    .collect(Collectors.toSet());
            methods = methods.stream()
                    .filter(m -> newNames.contains(m.getName()))
                    .collect(Collectors.toSet());
        }

        if (tstype.supportNamespace())
            ctx.append("declare namespace ")
                .append(tstype.getNamespace()).append(" {\n\n");

        getClassDecl(ctx).append("\n\n");

        if (tstype.isFunctional()) {

            methods.stream()
                    .filter( m -> Modifier.isAbstract(m.getModifiers()))
                    .findFirst()
                    .ifPresent( m -> ctx.append('\t')
                                    .append(ctx.getMethodParametersAndReturnDecl(m, false))
                                    // Rhino compatibility ???
                                    //.append("\n\t")
                                    //.append(getMethodDecl(ctx, m, false /* non optional */))
                                    .append(ENDL));

            methods.stream()
                    .filter( m -> !Modifier.isAbstract(m.getModifiers()))
                    .map( m -> ctx.getMethodDecl(m, true /* optional */))
                    .sorted()
                    .forEach( decl -> ctx.append('\t')
                            .append(decl)
                            .append(ENDL));

        } else {

            ctx.processEnumDecl();

            // Every Java class value exposes `.class` (its java.lang.Class object). Emit it as a
            // static member so `SomeClass.class` type-checks. Interfaces reach this branch too but
            // have no runtime value / static side (and `static` is illegal on a TS interface), so
            // skip them.
            if (!tstype.getValue().isInterface()) {
                ctx.append('\t').append("static class:java.lang.Class<any>").append(ENDL);
            }

            // Public fields (class values). Enum constants are emitted by processEnumDecl / the
            // static definition, so exclude them here to avoid duplicate declarations. When
            // extending an emitted parent, drop fields inherited from it (they come via `extends`).
            final Set<String> parentFieldNames = (emittedSuper == null)
                    ? java.util.Collections.emptySet()
                    : Stream.of(emittedSuper.getFields()).map(Field::getName).collect(Collectors.toSet());
            tstype.getFields().stream()
                .filter( f -> !f.isEnumConstant() )
                .filter( f -> !parentFieldNames.contains(f.getName()) )
                .filter( f -> !ctx.options.ignoreDeprecated || f.getAnnotation(Deprecated.class) == null)
                .map( ctx::getFieldDecl )
                .sorted()
                .forEach( decl -> ctx.append('\t').append(decl).append(ENDL));

            // Public constructors. Interfaces have none (getConstructors() is empty), so no invalid
            // 'constructor' is emitted into a TS interface.
            Stream.of(tstype.getValue().getConstructors())
                .filter( c -> Modifier.isPublic(c.getModifiers()) )
                .filter( c -> !ctx.options.ignoreDeprecated || c.getAnnotation(Deprecated.class) == null)
                .map( ctx::getConstructorDecl )
                .sorted()
                .forEach( decl -> ctx.append('\t').append(decl).append(ENDL));

            // Emit every method, static included. Static methods carry the `static` keyword (see
            // getMethodDecl), so `typeof <class>` exposes the full static side in the .d.ts and the
            // runtime binding in <out>-types.ts collapses to `typeof <class>` (no duplicated list).
            methods.stream()
                .filter( this::testMethodNotAllowed)
                .map( md -> ctx.getMethodDecl(md, false /* optional */) )
                .sorted()
                .forEach( decl -> ctx.append('\t').append(decl).append(ENDL));
        }

        ctx.append("\n} // end ").append(tstype.getSimpleTypeName()).append('\n');

        appendNestedTypeAliases(ctx, tstype);

        if (tstype.supportNamespace())
            ctx.append("\n} // end namespace ").append(tstype.getNamespace()).append('\n');

        return ctx;
    }

    /**
     * GraalJS accesses a nested Java type as {@code Outer.Inner}, but nested types are emitted
     * flattened as {@code Outer$Inner} siblings in the package namespace. Emit a
     * {@code namespace Outer { ... }} that merges with the {@code class Outer} and re-exposes each
     * nested type under its simple name, so both {@code Outer.Inner} (GraalJS form) and
     * {@code Outer$Inner} (flattened form) resolve. Only nested types that were themselves emitted
     * (present in the declared-type map) are aliased.
     */
    private void appendNestedTypeAliases(TSConverterContext ctx, TSType tstype) {
        final StringBuilder body = new StringBuilder();
        for (Class<?> nested : tstype.getValue().getDeclaredClasses()) {
            if (!Modifier.isPublic(nested.getModifiers()) || nested.isSynthetic()) continue;
            if (!ctx.declaredTypeMap.containsKey(nested.getName())) continue;

            final String simple = nested.getSimpleName();
            final String flattened = TSType.of(nested).getSimpleTypeName(); // Outer$Inner
            final java.lang.reflect.TypeVariable<?>[] tps = nested.getTypeParameters();
            final String tp = (tps.length == 0) ? "" : Stream.of(tps)
                    .map(java.lang.reflect.TypeVariable::getName)
                    .collect(Collectors.joining(",", "<", ">"));

            body.append('\t').append("export type ").append(simple).append(tp)
                    .append(" = ").append(flattened).append(tp).append(ENDL);
            // A nested interface has no runtime value; classes and enums do (constructor / enum
            // object), so only they get a value alias for `Outer.Inner` value access.
            if (!nested.isInterface()) {
                body.append('\t').append("export const ").append(simple)
                        .append(": typeof ").append(flattened).append(ENDL);
            }
        }
        if (body.length() > 0) {
            ctx.append("namespace ").append(tstype.getSimpleTypeName()).append(" {\n")
                    .append(body.toString())
                    .append("} // end nested aliases of ").append(tstype.getSimpleTypeName()).append('\n');
        }
    }

    /**
     * Emit a Java enum as a native, string-valued TypeScript enum, e.g.
     * <pre>
     * declare namespace pkg {
     * enum Mode {
     * 	SAFE = "SAFE",
     * 	PROTECTED = "PROTECTED"
     * }
     * } // end namespace pkg
     * </pre>
     * Each constant maps to its own name as a string so that GraalJS' read-as-string coercion
     * (e.g. {@code plugin.getMode() == "SAFE"}) type-checks, while the nominal enum type still
     * rejects passing a bare string where a Java enum is required — which GraalJS rejects at
     * runtime too. The instance methods and Java {@code Enum} machinery are intentionally dropped.
     *
     * @param ctx    the conversion context
     * @param tstype the enum type
     * @return the context
     */
    private TSConverterContext applyEnum(TSConverterContext ctx, TSType tstype) {

        if (tstype.supportNamespace())
            ctx.append("declare namespace ").append(tstype.getNamespace()).append(" {\n\n");

        ctx.append("enum ").append(tstype.getSimpleTypeName()).append(" {\n");

        final String members = tstype.getFields().stream()
            .filter(java.lang.reflect.Field::isEnumConstant)
            .filter(f -> !ctx.options.ignoreDeprecated || f.getAnnotation(Deprecated.class) == null)
            .map(java.lang.reflect.Field::getName)
            .sorted()
            .map(name -> String.format("\t%s = \"%s\"", name, name))
            .collect(Collectors.joining(",\n"));

        ctx.append(members).append("\n}\n");

        if (tstype.supportNamespace())
            ctx.append("\n} // end namespace ").append(tstype.getNamespace()).append('\n');

        return ctx;
    }
}
