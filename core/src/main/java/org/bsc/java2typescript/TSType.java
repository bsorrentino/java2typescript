package org.bsc.java2typescript;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.lang.String.format;
import java.util.Arrays;

/**
 * 
 * @author bsorrentino
 *
 */
public class TSType extends HashMap<String, Object> {

    private static final String ALIAS = "alias";
    private static final String VALUE = "value";
    private static final String EXPORT = "export";
    private static final String NAMESPACE = "namespace";
    private static final String FUNCTIONAL = "functional";

    protected TSType() {
        super(3);
    }


    public static TSType of() {
        return new TSType() {
            {
                put(VALUE, Void.class);
            }
        };
    }
    public static TSType of(Class<?> cl) {
        return new TSType() {
            {
                put(VALUE, cl);
            }
        };
    }

    /**
     * 
     * @return
     */
    public Class<?> getValue() {
        return getClassFrom(super.get(VALUE));
    }

    /**
     * 
     * @return
     */
    public boolean isExport() {
        return (boolean) super.getOrDefault(EXPORT, false);
    }

    /**
     * 
     * @return
     */
    public TSType setExport(boolean value) {
        super.put(EXPORT, value);
        return this;
    }

    /**
     * 
     * @return
     */
    public boolean hasAlias() {
        final String alias = (String) super.get(ALIAS);
        return alias != null && !alias.isEmpty();
    }

    /**
     * 
     * @return
     */
    public String getAlias() {
        return (String) super.get(ALIAS);
    }

       /**
     * 
     * @return
     */
    public TSType setAlias( String value ) {
        super.put(ALIAS,value);
        return this;
    }

    /**
     * Test is functional interface
     * 
     * @return
     */
    public boolean isFunctional() {

        
        if( !getValue().isInterface()) return false;
        if( getValue().isAnnotationPresent(FunctionalInterface.class)) return true;
        
        return (Boolean)super.getOrDefault( FUNCTIONAL, false) && 
                Arrays.stream(getValue().getDeclaredMethods())
                    .filter( m -> Modifier.isAbstract(m.getModifiers()) )
                    .count() == 1;
    }

    /**
     * 
     */
    public TSType setFunctional( boolean value ) {

        super.put(FUNCTIONAL, value);
        return this;

    }

    
    private String getMemberSimpleTypeName() {

        return format( "%s$%s", getValue().getDeclaringClass().getSimpleName(), getValue().getSimpleName());
    }

    /**
     * 
     * @return
     */
    public final String getTypeName() {
        return (hasAlias()) ? getAlias() : format( "%s.%s", getNamespace(), 
                (getValue().isMemberClass() ? getMemberSimpleTypeName() : getValue().getSimpleName()));
    }

    /**
     * 
     * @return
     */
    public final String getSimpleTypeName() {
        return (hasAlias()) ? getAlias() : 
            ((getValue().isMemberClass()) ? getMemberSimpleTypeName() : getValue().getSimpleName());
    }

    /**
     * 
     * @return
     */
    public final boolean supportNamespace() {
        return !hasAlias();
    }
    
    /**
     * 
     * @return
     */
    public final String getNamespace() {
        return (String) super.getOrDefault(NAMESPACE, getValue().getPackage().getName());
    }
    
    /**
     *
     * @return
     */
    public Set<Field> getFields() {

        final Predicate<Field> std = f -> !f.isSynthetic() && Modifier.isPublic(f.getModifiers())
                && Character.isJavaIdentifierStart(f.getName().charAt(0))
                && f.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

        return Stream.concat(Stream.of(getValue().getFields()), Stream.of(getValue().getDeclaredFields())).filter(std)
                .collect(Collectors.toSet());

    }

    /**
     *
     * @param m
     * @return
     */
    protected boolean testIncludeMethod( Method m ) {
        final int mod = m.getModifiers();
        // Public members are API. A `protected abstract` method is also part of the callable
        // contract: every concrete subclass must implement it, so instances handed to a script
        // (via host interop) always expose it -- e.g. WebSocketAlertWrapper.WebSocketAlertBuilder.raise().
        // Ordinary protected/private concrete methods stay excluded.
        final boolean visible = Modifier.isPublic(mod)
                || (Modifier.isProtected(mod) && Modifier.isAbstract(mod));
        return !m.isBridge() && !m.isSynthetic() && visible
                && Character.isJavaIdentifierStart(m.getName().charAt(0))
                && m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);
    }

    /**
     *
     * @return
     */
    public Stream<Method> getMethodsAsStream() {

        return Stream.concat(
                        Stream.of(getValue().getMethods()),
                        Stream.of(getValue().getDeclaredMethods()))
                .filter(this::testIncludeMethod);
    }

    /**
     *
     * @return
     */
    public final Set<Method> getMethods() {
        return getMethodsAsStream().collect(Collectors.toSet());
    }

    /**
     * 
     * @return
     */
    private Class<?> getMemberClassForName( String fqn ) throws ClassNotFoundException {
        // The source form of a nested type uses '.' for both the package separator and the nesting
        // separator (e.g. a.b.Outer.Inner), but the binary name the class loader expects uses '$'
        // for nesting (a.b.Outer$Inner). We don't know where the package ends, so convert trailing
        // dots to '$' one at a time, right to left, and return the first name that resolves. This
        // handles arbitrarily nested types (a.b.Outer$Middle$Inner), not just a single level.
        final StringBuilder name = new StringBuilder(fqn);
        int i;
        while ((i = name.lastIndexOf(".")) >= 0) {
            name.setCharAt(i, '$');
            try {
                return loadClass(name.toString());
            } catch (ClassNotFoundException ignored) {
                // Not this split; convert the next dot to the left and retry.
            }
        }

        throw new ClassNotFoundException(fqn);
    }

    /**
     * Load a class WITHOUT running its static initializers.
     * <p>
     * Type generation only needs class metadata (methods, fields, modifiers). Initializing an
     * application's classes outside their own runtime (as the 1-arg {@link Class#forName(String)}
     * would) can trigger {@link ExceptionInInitializerError}: e.g. classes whose static
     * initializers rely on framework state that has not been bootstrapped.
     *
     * @param fqn fully qualified class name
     * @return the (uninitialized) loaded class
     * @throws ClassNotFoundException if the class cannot be found
     */
    private static Class<?> loadClass(String fqn) throws ClassNotFoundException {
        return Class.forName(fqn, false, TSType.class.getClassLoader());
    }

    /**
     *
     * @param dt
     * @return
     */
    private Class<?> getClassFrom(Object dt) {
        if (dt instanceof Class)
            return (Class<?>) dt;

        final String fqn = dt.toString();
        try {
            return loadClass(fqn);

        } catch (ClassNotFoundException e1) {

            try {
                return getMemberClassForName(fqn);
    
            } catch (ClassNotFoundException e2) {
                throw new RuntimeException(String.format("class not found [%s]", dt), e1);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if( o instanceof Class ) {
            return getValue().equals(o);
        }
        if( o instanceof TSType ) {
            return getValue().equals(((TSType) o).getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public String toString() {
        return format("TSType{ value: %s }", getValue().getName());
    }
}
