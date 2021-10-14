package org.bsc.java2typescript.transformer;

import org.bsc.java2typescript.TSConverterContext;
import org.bsc.java2typescript.TSConverterStatic;
import org.bsc.java2typescript.TSTransformer;
import org.bsc.java2typescript.TSType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

/**
 *
 */
public class TSJavaClass2DeclarationTransformer extends TSConverterStatic implements TSTransformer {
    @Override
    public TSConverterContext apply(TSConverterContext ctx) {

        final TSType tstype = ctx.type;

        final java.util.Set<Method> methods = tstype.getMethods();

        if (tstype.supportNamespace())
            ctx.append("declare namespace ").append(tstype.getNamespace()).append(" {\n\n");

        ctx.getClassDecl().append("\n\n");

        if (tstype.isFunctional()) {

            methods.stream()
                    .filter(m -> Modifier.isAbstract(m.getModifiers()))
                    .findFirst()
                    .ifPresent(
                            m -> ctx.append('\t')
                                    .append(ctx.getMethodParametersAndReturnDecl(m, false))
                                    // Rhino compatibility ???
                                    //.append("\n\t")
                                    //.append(getMethodDecl(ctx, m, false /* non optional */))
                                    .append(ENDL));

            methods.stream()
                    .filter(m -> !Modifier.isAbstract(m.getModifiers()))
                    .map(m -> ctx.getMethodDecl(m, true /* optional */))
                    .sorted()
                    .forEach(decl -> ctx.append('\t')
                            .append(decl)
                            .append(ENDL));

        } else {

            ctx.processEnumDecl();

            final java.util.Set<Method> methodSet = methods.stream()
                    .filter(md -> (tstype.isExport() && isStatic(md)) == false).filter((md) -> {
                        final String name = md.getName();
                        return !(name.contains("$") || // remove unnamed
                                name.equals("getClass") || name.equals("hashCode") || name.equals("wait")
                                || name.equals("notify") || name.equals("notifyAll"));
                    }).collect(Collectors.toCollection(() -> new java.util.LinkedHashSet<Method>()));

            methodSet.stream().map(md -> ctx.getMethodDecl(md, false /* optional */)).sorted()
                    .forEach((decl) -> ctx.append('\t').append(decl).append(ENDL));
        }

        return ctx;
    }
}
