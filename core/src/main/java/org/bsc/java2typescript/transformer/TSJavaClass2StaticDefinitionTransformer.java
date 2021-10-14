package org.bsc.java2typescript.transformer;

import org.bsc.java2typescript.TSConverter;
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

        ctx.append("interface ").append(ctx.type.getSimpleTypeName()).append("Static {\n\n");

        if (ctx.type.getValue().isEnum()) {
            ctx.processEnumType();
        }

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

            final java.util.Set<Method> methodSet = ctx.type.getMethods().stream().filter(TSConverter::isStatic)
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
                .append(ctx.compatibility.javaType(ctx.type.getValue().getName()))
                .append(ENDL)
                .append("\n\n");

        return ctx;
    }
}
