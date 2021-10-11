package org.bsc.java2typescript;

import static java.lang.String.format;

import java.lang.reflect.Executable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author bsorrentino
 *
 */
public class TypescriptConverter extends TypescriptConverterStatic {
    public enum Compatibility {
        NASHORN, RHINO, GRAALJS;

        public String javaType(String fqn) {
            switch (this.ordinal()) {
            case 1:
                return format("Packages.%s", fqn );
            default:
                return format("Java.type(\"%s\")", fqn );
            }
        }
    }

    final Compatibility compatibility;

    public TypescriptConverter(Compatibility compatibility) {
        super();
        this.compatibility = compatibility;
    }

    /**
     * 
     * @return
     */
    public final boolean isRhino() {
        return compatibility == Compatibility.RHINO;
    }

    /**
     *
     * @param type
     * @param declaredTypeMap
     * @return
     */
    public String processStatic(TSType type, java.util.Map<String, TSType> declaredTypeMap) {

        final Context ctx = new Context(type, declaredTypeMap);

        ctx.append("interface ").append(ctx.type.getSimpleTypeName()).append("Static {\n\n");

        if (ctx.type.getValue().isEnum()) {
            ctx.processEnumType();
        }

        // Append class property
        ctx.append("\treadonly class:any;\n");

        if (type.isFunctional()) {

            final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);
            final String tstype = convertJavaToTS(type.getValue(), ctx.type, declaredTypeMap, false,
                    Optional.of((tv) -> TypeVarSet.add(tv.getName())));

            ctx.append("\tnew");
            if (!TypeVarSet.isEmpty()) {
                ctx.append('<').append(TypeVarSet.stream().collect(Collectors.joining(","))).append('>');
            }
            ctx.append("( arg0:").append(tstype).append(" ):").append(tstype).append(ENDL);

        }

        else {

            Stream.of(ctx.type.getValue().getConstructors()).filter(c -> Modifier.isPublic(c.getModifiers()))
                    .forEach(c -> {
                        ctx.append("\tnew").append(getMethodParametersAndReturnDecl(ctx, c, false)).append(ENDL);
                    });

            final java.util.Set<Method> methodSet = ctx.type.getMethods().stream().filter(TypescriptConverter::isStatic)
                    .collect(Collectors.toCollection(() -> new java.util.LinkedHashSet<>()));

            if (!methodSet.isEmpty()) {

                methodSet.stream().sorted(Comparator.comparing(Method::toGenericString)).forEach(md -> ctx.append('\t')
                        .append(md.getName()).append(getMethodParametersAndReturnDecl(ctx, md, false)).append(ENDL));
            }

        }

        ctx.append("}\n\n").append("export const ")
                .append(ctx.type.getSimpleTypeName())
                .append(": ")
                .append(ctx.type.getSimpleTypeName())
                .append("Static = ")
                .append(compatibility.javaType(ctx.type.getValue().getName()))
                .append(ENDL)
                .append("\n\n");

        return ctx.toString();
    }

    /**
     * 
     * @param m
     * @param type
     * @param declaredTypeMap
     * @param packageResolution
     * @return
     */
    public <E extends Executable> String getMethodParametersAndReturnDecl(E m, TSType type,
            java.util.Map<String, TSType> declaredTypeMap, boolean packageResolution) {
        final Context ctx = new Context(type, declaredTypeMap);

        return getMethodParametersAndReturnDecl(ctx, m, packageResolution);
    }

    /**
     * 
     * @param ctx
     * @param m
     * @param packageResolution
     * @return
     */
    private <E extends Executable> String getMethodParametersAndReturnDecl(Context ctx, E m,
            boolean packageResolution) {
        final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);

        final Consumer<TypeVariable<?>> addTypeVar = tv -> TypeVarSet.add(tv.getName());

        final Parameter[] params = m.getParameters();

        final String params_string = Arrays.stream(params).map((tp) -> {

            final String name = getParameterName(tp);

            if (tp.isVarArgs()) {

                String type = null;
                if (tp.getParameterizedType() instanceof GenericArrayType) {

                    type = convertJavaToTS(((GenericArrayType) tp.getParameterizedType()).getGenericComponentType(), m,
                            ctx.type, ctx.declaredTypeMap, packageResolution, Optional.of(addTypeVar));
                } else {
                    type = convertJavaToTS(tp.getType().getComponentType(), m, ctx.type, ctx.declaredTypeMap,
                            packageResolution, Optional.of(addTypeVar));
                }
                return String.format("...%s:%s[]", name, type);

            }

            final String type = convertJavaToTS(tp.getParameterizedType(), m, ctx.type, ctx.declaredTypeMap,
                    packageResolution, Optional.of(addTypeVar));
            return String.format("%s:%s", name, type);
        }).collect(Collectors.joining(", "));

        final Type returnType = (m instanceof Method) ? ((Method) m).getGenericReturnType() : ctx.type.getValue();

        final String tsReturnType = convertJavaToTS(returnType, m, ctx.type, ctx.declaredTypeMap, packageResolution,
                Optional.of(addTypeVar));

        final StringBuilder result = new StringBuilder();

        if (!TypeVarSet.isEmpty()) {
            result.append('<').append(TypeVarSet.stream().collect(Collectors.joining(","))).append('>');
        }
        return result.append("( ").append(params_string).append(" ):").append(tsReturnType).toString();
    }

    /**
     * 
     * @param ctx
     * @param m
     * @return
     */
    private String getMethodDecl(Context ctx, final Method m, boolean optional) {

        final StringBuilder sb = new StringBuilder();

        if (Modifier.isStatic(m.getModifiers())) {

            if (ctx.type.getValue().isInterface()) {
                sb.append("// ");
            }

            sb.append("static ").append(m.getName());
        } else {
            sb.append(m.getName());
            if (optional)
                sb.append('?');
        }

        sb.append(getMethodParametersAndReturnDecl(ctx, m, true));

        return sb.toString();

    }

    class Context implements Cloneable {
        final TSType type;
        final java.util.Map<String, TSType> declaredTypeMap;
        final StringBuilder sb = new StringBuilder();

        public Context(TSType type, Map<String, TSType> declaredClassMap) {
            Objects.requireNonNull(type, "type is null!");
            Objects.requireNonNull(declaredClassMap, "declaredClassMap is null!");

            this.type = type;
            this.declaredTypeMap = declaredClassMap;
        }

        /**
         * 
         * @param cs
         * @return
         */
        Context append(CharSequence cs) {
            sb.append(cs);
            return this;
        }

        /**
         * 
         * @param ch
         * @return
         */
        Context append(char ch) {
            sb.append(ch);
            return this;
        }

        /**
         * 
         * @return
         */
        Context getClassDecl() {

            final StringBuilder inherited = new StringBuilder();

            if (type.getValue().isInterface()) {
                sb.append("interface ");
            } else {

                if (type.getValue().isEnum())
                    sb.append("/* enum */");

                if (type.hasAlias())
                    sb.append("declare ");

                sb.append("class ");

                final TSType superclass = TSType.of(type.getValue().getSuperclass());

                if (superclass != null) {
                    inherited.append(" extends ").append(getTypeName(superclass, type, true));
                }
            }

            final Class<?>[] interfaces = type.getValue().getInterfaces();

            if (interfaces.length > 0) {

                final String ifc = Arrays.stream(interfaces).map(c -> TSType.of(c))
                        .map(t -> getTypeName(t, type, true)).collect(Collectors.joining(", "));
                inherited.append((type.getValue().isInterface()) ? " extends " : " implements ").append(ifc);

            }

            sb.append(getTypeName(type, type, true));

            if (inherited.length() > 0 || type.hasAlias()) {

                sb.append("/*");

                if (type.hasAlias())
                    sb.append(type.getValue().getName());
                if (inherited.length() > 0)
                    sb.append(inherited);

                sb.append("*/");
            }

            sb.append(" {");

            return this;
        }

        /**
         * 
         * @return
         */
        Context processEnumDecl() {
            if (type.getValue().isEnum()) {
                type.setExport(true); // force export
                // fix #4
                // Arrays.stream(type.getValue().getEnumConstants())
                Arrays.stream(type.getValue().getFields())
                        .filter(f -> f.isEnumConstant())
                        .forEach((c) -> sb.append('\t')
                                        .append("// ")
                                        .append(c.getName())
                                        .append(':')
                                        .append(type.getSimpleTypeName())
                                        .append(';')
                                        .append('\n'));
                sb.append('\n');
            }

            return this;

        }

        /**
         *
         * @param level
         * @return
         */
        Context processMemberClasses(int level) {

            final Class<?> memberClasses[] = type.getValue().getClasses();

            if (memberClasses.length == 0)
                return this;

            // sb.append( "export module " )
            // .append(type.getSimpleTypeName())
            // .append(" {\n\n")
            // ;

            Stream.of(memberClasses).peek(c -> debug("nested class name[%s]", c.getName()))
                    // .filter(distinctByKey( c -> c.getSimpleName() ))
                    .filter(distinctByKey(c -> c.getName())).map(cl -> TSType.of(cl))
                    .peek(t -> debug("nested type name[%s]", t.getTypeName()))
                    .map(t -> processClass(level + 1, t, declaredTypeMap))
                    .forEach(decl -> sb.append(decl));

            // sb.append("\n} // end module ")
            // .append(type.getSimpleTypeName())
            // .append('\n')
            // ;
            return this;
        }

        private Context processEnumType() {

            // fix #4
            // Arrays.stream(type.getValue().getEnumConstants())
            Arrays.stream(type.getValue().getFields())
                    .filter(f -> f.isEnumConstant())
                    .forEach((c) -> sb.append('\t')
                                        .append(c.getName())
                                        .append(':')
                                        .append(type.getTypeName())
                                        .append(';')
                                        .append('\n'));
            sb.append('\n');

            return this;

        }

        public Context clone() {
            return new Context(type, declaredTypeMap);
        }

        /**
         * 
         */
        @Override
        public String toString() {
            return sb.toString();
        }

    } // end Context

    /**
     * 
     * @param tstype
     * @param declaredTypeMap
     * @return
     */
    public Context contextOf(TSType tstype, java.util.Map<String, TSType> declaredTypeMap,
            Compatibility compatibility) {
        return new Context(tstype, declaredTypeMap);
    }

    /**
     *
     * @param level
     * @param tstype
     * @param declaredTypeMap
     * @return
     */
    public String processClass(int level, TSType tstype, java.util.Map<String, TSType> declaredTypeMap) {

        final Context ctx = contextOf(tstype, declaredTypeMap, compatibility);

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
                            .append(getMethodParametersAndReturnDecl(ctx, m, false))
                            // Rhino compatibility ???
                            //.append("\n\t")
                            //.append(getMethodDecl(ctx, m, false /* non optional */))
                            .append(ENDL));

            methods.stream()
                .filter(m -> !Modifier.isAbstract(m.getModifiers()))
                    .map(m -> getMethodDecl(ctx, m, true /* optional */))
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

            methodSet.stream().map(md -> getMethodDecl(ctx, md, false /* optional */)).sorted()
                    .forEach((decl) -> ctx.append('\t').append(decl).append(ENDL));
        }

        ctx.append("\n} // end ").append(tstype.getSimpleTypeName()).append('\n');

        // NESTED CLASSES
        // if( level == 0 ) ctx.processMemberClasses( level );

        if (tstype.supportNamespace())
            ctx.append("\n} // end namespace ").append(tstype.getNamespace()).append('\n');

        return ctx.toString();

    }

}
