package org.bsc.java2typescript;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TSConverterContext extends TSConverterStatic implements Cloneable, Function<TSTransformer,TSConverterContext> {
    public final TSType type;
    public final java.util.Map<String, TSType> declaredTypeMap;
    public final Java2TSConverter.Options options;
    final StringBuilder sb = new StringBuilder();

    /**
     * @param tstype
     * @param declaredTypeMap
     * @return
     */
    public static TSConverterContext of(TSType tstype,
                                        java.util.Map<String, TSType> declaredTypeMap,
                                        Java2TSConverter.Options options) {
        return new TSConverterContext(tstype, declaredTypeMap, options);
    }


    private TSConverterContext(TSType type, Map<String, TSType> declaredClassMap, Java2TSConverter.Options options) {
        Objects.requireNonNull(type, "type is null!");
        Objects.requireNonNull(declaredClassMap, "declaredClassMap is null!");

        this.type = type;
        this.declaredTypeMap = declaredClassMap;
        this.options = options;
    }

    @Override
    public TSConverterContext apply(TSTransformer transformer) {
        return transformer.apply(this);
    }

    /**
     *
     * @return
     */
    public Java2TSConverter.Options getOptions() {
        return options;
    }

    /**
     * @param cs
     * @return
     */
    public TSConverterContext append(CharSequence cs) {
        sb.append(cs);
        return this;
    }

    /**
     * @param ch
     * @return
     */
    public TSConverterContext append(char ch) {
        sb.append(ch);
        return this;
    }

    /**
     * @return
     */
    public TSConverterContext getClassDecl() {

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
                inherited.append(" extends ")
                        .append(getTypeName(superclass, type, true));
            }
        }

        final Class<?>[] interfaces = type.getValue().getInterfaces();

        if (interfaces.length > 0) {

            final String ifc = Arrays.stream(interfaces).map(c -> TSType.of(c))
                    .map(t -> getTypeName(t, type, true))
                    .collect(Collectors.joining(", "));

            inherited.append((type.getValue().isInterface()) ? " extends " : " implements ")
                    .append(ifc);

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
     * @return
     */
    public TSConverterContext processEnumDecl() {
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
     * @param
     * @return Context processMemberClasses(int level) {
     * <p>
     * final Class<?> memberClasses[] = type.getValue().getClasses();
     * <p>
     * if (memberClasses.length == 0)
     * return this;
     * <p>
     * // sb.append( "export module " )
     * // .append(type.getSimpleTypeName())
     * // .append(" {\n\n")
     * // ;
     * <p>
     * Stream.of(memberClasses).peek(c -> debug("nested class name[%s]", c.getName()))
     * // .filter(distinctByKey( c -> c.getSimpleName() ))
     * .filter(distinctByKey(c -> c.getName())).map(cl -> TSType.of(cl))
     * .peek(t -> debug("nested type name[%s]", t.getTypeName()))
     * .map(t -> processClass(level + 1, t, declaredTypeMap))
     * .forEach(decl -> sb.append(decl));
     * <p>
     * // sb.append("\n} // end module ")
     * // .append(type.getSimpleTypeName())
     * // .append('\n')
     * // ;
     * return this;
     * }
     */

    public TSConverterContext processEnumType() {

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

    public <E extends Executable> String getMethodParametersAndReturnDecl(E m,
                                                                          boolean packageResolution) {
        final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);

        final Consumer<TypeVariable<?>> addTypeVar = tv -> TypeVarSet.add(tv.getName());

        final Parameter[] params = m.getParameters();

        final String params_string = Arrays.stream(params).map((tp) -> {

            final String name = getParameterName(tp);

            if (tp.isVarArgs()) {

                String typeName = null;
                if (tp.getParameterizedType() instanceof GenericArrayType) {

                    typeName = convertJavaToTS(((GenericArrayType) tp.getParameterizedType()).getGenericComponentType(), m,
                            type, declaredTypeMap, packageResolution, Optional.of(addTypeVar));
                } else {
                    typeName = convertJavaToTS(tp.getType().getComponentType(), m, type, declaredTypeMap,
                            packageResolution, Optional.of(addTypeVar));
                }
                return String.format("...%s:%s[]", name, typeName);

            }

            final String typeName = convertJavaToTS(tp.getParameterizedType(), m, type, declaredTypeMap,
                    packageResolution, Optional.of(addTypeVar));
            return String.format("%s:%s", name, typeName);
        }).collect(Collectors.joining(", "));

        final Type returnType = (m instanceof Method) ? ((Method) m).getGenericReturnType() : type.getValue();

        final String tsReturnType = convertJavaToTS(returnType, m, type, declaredTypeMap, packageResolution,
                Optional.of(addTypeVar));

        final StringBuilder result = new StringBuilder();

        if (!TypeVarSet.isEmpty()) {
            result.append('<').append(TypeVarSet.stream().collect(Collectors.joining(","))).append('>');
        }
        return result.append("( ").append(params_string).append(" ):").append(tsReturnType).toString();
    }

    /**
     * Render a public field as a TypeScript property declaration, e.g.
     * {@code static readonly FOO:string} or {@code name:int}. Enum constants are handled
     * separately (see {@link #processEnumDecl()}) and should be filtered out before calling this.
     *
     * @param f the field
     * @return the TypeScript property declaration (without the trailing separator)
     */
    public String getFieldDecl(final Field f) {

        final StringBuilder sb = new StringBuilder();

        final int mod = f.getModifiers();

        if (Modifier.isStatic(mod)) {
            sb.append("static ");
        }

        if (Modifier.isFinal(mod))
            sb.append("readonly ");

        sb.append(f.getName())
                .append(':')
                .append(convertJavaToTS(f.getGenericType(), f, type, declaredTypeMap, true, Optional.empty()));

        return sb.toString();
    }

    /**
     * Render a public constructor as a TypeScript {@code constructor( ... )} declaration for use
     * inside a {@code declare class}. Unlike a construct signature ({@code new(...)}), a TS
     * constructor cannot carry its own type parameters, so none are emitted here; type variables
     * that belong to the declaring class stay in scope, any others degrade to {@code any}.
     *
     * @param c the constructor
     * @return the TypeScript constructor declaration (without the trailing separator)
     */
    public String getConstructorDecl(final Constructor<?> c) {

        final StringBuilder params = new StringBuilder("( ");

        final Parameter[] ps = c.getParameters();
        for (int i = 0; i < ps.length; i++) {
            if (i > 0)
                params.append(", ");

            final Parameter p = ps[i];
            final String name = getParameterName(p);

            if (p.isVarArgs()) {
                final Type component = (p.getParameterizedType() instanceof GenericArrayType)
                        ? ((GenericArrayType) p.getParameterizedType()).getGenericComponentType()
                        : p.getType().getComponentType();
                final String typeName = convertJavaToTS(component, c, type, declaredTypeMap, true, Optional.empty());
                params.append(String.format("...%s:%s[]", name, typeName));
            } else {
                final String typeName =
                        convertJavaToTS(p.getParameterizedType(), c, type, declaredTypeMap, true, Optional.empty());
                params.append(String.format("%s:%s", name, typeName));
            }
        }

        params.append(" )");

        return "constructor".concat(params.toString());
    }

    public String getMethodDecl(final Method m, boolean optional) {

        final StringBuilder sb = new StringBuilder();

        if (Modifier.isStatic(m.getModifiers())) {

            sb.append("static ").append(m.getName());
        } else {
            sb.append(m.getName());
            if (optional)
                sb.append('?');
        }

        sb.append(getMethodParametersAndReturnDecl(m, true));

        return sb.toString();

    }

    /**
     *
     * @return
     */
    public TSConverterContext clone() {

        return new TSConverterContext(type, declaredTypeMap, options);
    }

    /**
     *
     */
    @Override
    public String toString() {
        return sb.toString();
    }

} // end Context


