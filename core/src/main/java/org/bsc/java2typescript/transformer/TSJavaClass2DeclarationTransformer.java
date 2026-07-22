package org.bsc.java2typescript.transformer;

import org.bsc.java2typescript.TSConverterContext;
import org.bsc.java2typescript.TSConverterStatic;
import org.bsc.java2typescript.TSTransformer;
import org.bsc.java2typescript.TSType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

        if (tstype.getValue().isEnum())
            return applyEnum(ctx, tstype);

        final Set<Method> methods = getMethodsAsStream(ctx).collect(Collectors.toSet());;

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
                ctx.append('\t').append("static class:java.lang.Class<any>;").append(ENDL);
            }

            // Public fields (class values). Enum constants are emitted by processEnumDecl / the
            // static definition, so exclude them here to avoid duplicate declarations.
            tstype.getFields().stream()
                .filter( f -> !f.isEnumConstant() )
                .map( ctx::getFieldDecl )
                .sorted()
                .forEach( decl -> ctx.append('\t').append(decl).append(ENDL));

            // Public constructors. Interfaces have none (getConstructors() is empty), so no invalid
            // 'constructor' is emitted into a TS interface.
            Stream.of(tstype.getValue().getConstructors())
                .filter( c -> Modifier.isPublic(c.getModifiers()) )
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
