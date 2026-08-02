package org.bsc.java2typescript.transformer;

import org.bsc.java2typescript.Java2TSConverter;
import org.bsc.java2typescript.TSConverterContext;
import org.bsc.java2typescript.TSConverterStatic;
import org.bsc.java2typescript.TSTransformer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * transform java class to Typescript Static definition
 *
 */
public class TSJavaClass2StaticDefinitionTransformer extends TSConverterStatic implements TSTransformer {
    @Override
    public TSConverterContext apply(TSConverterContext ctx) {

        // Classes and enums expose their full static side in the .d.ts declaration, so the runtime
        // binding is simply `typeof <the declared type>` — no duplicated signatures. Interfaces have
        // no value form (`typeof <interface>` is illegal), so they keep an explicit "…Static" type
        // carrying the construct signature (functional SAM) and any static methods.
        if (!ctx.type.getValue().isInterface()) {

            return ctx.append("export const ")
                    .append(ctx.type.getSimpleTypeName())
                    .append(": typeof ")
                    .append(ctx.type.getTypeName())
                    .append(" = ")
                    .append(ctx.getOptions().compatibility.javaType(ctx.type.getValue().getName()))
                    .append(ENDL)
                    .append("\n");
        }

        return applyInterfaceStatic(ctx);
    }

    /**
     * Emit the {@code …Static} construct/static type for an interface, plus its {@code Java.type()}
     * binding. Interfaces (notably functional ones) have no {@code typeof} value form, so their
     * static side cannot be derived from the {@code .d.ts} declaration.
     *
     * @param ctx the conversion context
     * @return the context
     */
    private TSConverterContext applyInterfaceStatic(TSConverterContext ctx) {

        ctx.append("interface ").append(ctx.type.getSimpleTypeName()).append("Static {\n\n");

        // Append class property
        ctx.append("\treadonly class:any;\n");

        if (ctx.type.isFunctional()) {

            final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);
            final String tstype = convertJavaToTS(ctx.type.getValue(), ctx.type, ctx.declaredTypeMap, false,
                    Optional.of((tv) -> TypeVarSet.add(tv.getName())));

            ctx.append("\tnew");
            if (!TypeVarSet.isEmpty()) {
                ctx.append('<').append(TypeVarSet.stream().collect(Collectors.joining(","))).append('>');
            }
            ctx.append("( arg0:").append(tstype).append(" ):").append(tstype).append(ENDL);

        } else {

            Stream.of(ctx.type.getValue().getConstructors()).filter(c -> Modifier.isPublic(c.getModifiers()))
                    .forEach(c -> {
                        ctx.append("\tnew").append(ctx.getMethodParametersAndReturnDecl(c, false)).append(ENDL);
                    });

            final java.util.Set<Method> methodSet = ctx.type.getMethods().stream().filter(Java2TSConverter::isStatic)
                    .collect(Collectors.toCollection(() -> new java.util.LinkedHashSet<>()));

            if (!methodSet.isEmpty()) {

                methodSet.stream().sorted(Comparator.comparing(Method::toGenericString)).forEach(md -> ctx.append('\t')
                        .append(md.getName()).append(ctx.getMethodParametersAndReturnDecl(md, false)).append(ENDL));
            }

        }

        ctx.append("}\n\n").append("export const ")
                .append(ctx.type.getSimpleTypeName())
                .append(": ")
                .append(ctx.type.getSimpleTypeName())
                .append("Static = ")
                .append(ctx.getOptions().compatibility.javaType(ctx.type.getValue().getName()))
                .append(ENDL)
                .append("\n\n");

        return ctx;
    }
}
