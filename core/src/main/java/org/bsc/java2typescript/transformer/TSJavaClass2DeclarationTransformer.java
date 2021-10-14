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

    private boolean testMethodsNotAllowedInForeignObjectPrototypeOnList( Method md ) {
        final String name = md.getName();

        System.out.printf( "testMethodsNotAllowedInForeignObjectPrototypeOnList('%s')\n",  name );

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
    protected Stream<Method> getMethodsAsStream(TSConverterContext ctx) {

        if(  ctx.options.compatibility == GRAALJS &&
                ctx.options.foreignObjectPrototype &&
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
    public TSConverterContext apply(TSConverterContext ctx) {

        final TSType tstype = ctx.type;

        final Set<Method> methods = getMethodsAsStream(ctx).collect(Collectors.toSet());;

        if (tstype.supportNamespace())
            ctx.append("declare namespace ").append(tstype.getNamespace()).append(" {\n\n");

        ctx.getClassDecl().append("\n\n");

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

            methods.stream()
                .filter( md -> (tstype.isExport() && isStatic(md)) == false)
                .filter( this::testMethodNotAllowed)
                .map( md -> ctx.getMethodDecl(md, false /* optional */) )
                .sorted()
                .forEach( decl -> ctx.append('\t').append(decl).append(ENDL));
        }

        return ctx;
    }
}
