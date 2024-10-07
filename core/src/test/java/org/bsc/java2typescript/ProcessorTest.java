package org.bsc.java2typescript;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.bsc.java2typescript.Java2TSConverter.Compatibility;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author bsorrentino
 */
public class ProcessorTest extends AbstractConverterTest {

    private Java2TSConverter converter;

    @Before
    public void initConverter() {
        converter =  Java2TSConverter.builder()
                .compatibility(Compatibility.NASHORN)
                .build();
    }

    @Test
    public void testWildcardType() throws Exception {
        final TSType type = TSType.of(Sample1.class);
        {
            final Method m = type.getValue().getMethod("merge", Sample2.class);
            final String result = converter.getMethodParametersAndReturnDecl(m,
                    type,
                    declaredTypeMap(TSType.of(String.class), TSType.of(Sample2.class)),
                    true);

            assertNotNull(result);
            assertEquals( "<T>( source:Sample2<Sample2<T>> ):void", result );
        }
        {
            final Method m = type.getValue().getMethod("merge", BiConsumer.class);
            final String result = converter.getMethodParametersAndReturnDecl(m,
                    type,
                    declaredTypeMap(TSType.of(String.class), TSType.of(Sample2.class), TSType.of(BiConsumer.class).setExport(false).setAlias("BiConsumer")),
                    true);

            assertNotNull(result);
            assertEquals( "<T>( source:BiConsumer<E, Sample2<Sample2<T>>> ):void", result );
        }
        {
            final Method m = type.getValue().getMethod("concatMap", Function.class);
            final String result = converter.getMethodParametersAndReturnDecl(m,
                    type,
                    declaredTypeMap(TSType.of(String.class), TSType.of(Sample2.class), TSType.of(Function.class).setExport(false).setAlias("Func")),
                    true);

            assertNotNull(result);
            assertEquals( "<T>( mapper:Func<E, Sample2<T>> ):T", result );
        }

    }


    @Test
    public void testFunctionalInterface() throws Exception {
        final TSType type = TSType.of(Sample1.class);
        {
            final Method m = type.getValue().getMethod("transform", java.util.function.Function.class);
            final Type pType = m.getGenericParameterTypes()[0];
            final String result = Java2TSConverter.convertJavaToTS(pType, m,
                    type,
                    declaredTypeMap(TSType.of(String.class), TSType.of(java.util.function.Function.class).setAlias("Func").setExport(false)),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "Func<E, string>", result );


        }
        {
            final Method m = type.getValue().getMethod("transform", java.util.function.Function.class);
            final String result = converter.getMethodParametersAndReturnDecl(m,
                    type,
                    declaredTypeMap(TSType.of(String.class), TSType.of(java.util.function.Function.class).setAlias("Func").setExport(false)),
                    true);

            assertNotNull(result);
            assertEquals( "( transformer:Func<E, string> ):string", result );
        }
        {
            final Method m = type.getValue().getMethod("creator", java.util.concurrent.Callable.class);
            final String result = converter.getMethodParametersAndReturnDecl(m,
                    type,
                    declaredTypeMap(TSType.of(String.class), TSType.of(java.util.concurrent.Callable.class).setExport(false).setAlias("Supplier")),
                    true);

            assertNotNull(result);
            assertEquals( "( supplier:Supplier<E> ):E", result );
        }

    }

    @Test
    public void testClassDecl() throws Exception {

        TSConverterContext ctx = TSConverterContext.of(TSType.of(ArrayList.class), Collections.emptyMap(), Java2TSConverter.Options.of(Compatibility.NASHORN));
        {
            final String result = ctx.getClassDecl().toString();

            assertNotNull(result);
            assertEquals( "class ArrayList<E>/* extends AbstractList<E> implements List<E>, RandomAccess, java.lang.Cloneable, java.io.Serializable*/ {", result );
        }

    }

    @Test
    public void testAlias() throws Exception {

        final Class<?> type = Sample1.class;

        {
            final Method m = type.getMethod("getAttributeList");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    declaredTypeMap(TSType.of(String.class), TSType.of(java.util.List.class).setExport(false).setAlias("List")),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "List<string>", result );
        }
        {
            final Method m = type.getMethod("getAttributeList", java.util.List.class);

            final String result = converter.getMethodParametersAndReturnDecl(m,
                    TSType.of(type),
                    declaredTypeMap(TSType.of(String.class), TSType.of(java.util.List.class).setExport(false).setAlias("List")),
                    true);

            assertNotNull(result);
            assertEquals( "( intList:List<int|null> ):List<string>", result );
        }

    }

    @Test
    public void testSample1() throws Exception {

        final Class<?> type = Sample1.class;

        Stream.of(type.getTypeParameters()).forEach(v -> System.out.println(v.getName()));

        final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);

        final Consumer<TypeVariable<?>> addTypeVar = tv -> TypeVarSet.add(tv.getName());

        {
            final Method m = type.getMethod("method4");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    Collections.emptyMap(),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "any/*C*/", result );
        }
        {
            final Method m = type.getMethod("method5");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    Collections.emptyMap(),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "any/*C*/", result );
        }
        {
            final Method m = type.getMethod("method1_1");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    declaredClassMap(Sample2.class),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "Sample2<string>", result );
        }
        {
            final Method m = type.getMethod("method1_2");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    declaredClassMap(Sample2.class, java.lang.Comparable.class),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "java.lang.Comparable<Sample2<any>>", result );
        }
        {
            TypeVarSet.clear();
            final Method m = type.getMethod("method1_3");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    declaredTypeMap(TSType.of(java.util.function.BiPredicate.class).setExport(false).setAlias("BiPredicate")),
                    true,
                    Optional.of(addTypeVar));
            assertNotNull(result);
            assertEquals( "BiPredicate<E2, E2>", result );
            assertEquals(1, TypeVarSet.size() );
            assertTrue(TypeVarSet.contains("E2"));

        }
        {
            final Method m = type.getMethod("method1_3");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    Collections.emptyMap(),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "any /*java.util.function.BiPredicate*/", result );
        }
        {
            final Method m = type.getMethod("method1");
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    declaredClassMap(java.util.Map.class),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "java.util.Map<E, string>", result );
        }

        {
            final Method m = type.getMethod("method2", Sample2.class);
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    Collections.emptyMap(),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "string", result );

            final Type pType = m.getParameters()[0].getParameterizedType();
            final String rresult = Java2TSConverter.convertJavaToTS(pType, m,
                    TSType.of(type),
                    declaredClassMap(Sample2.class),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "Sample2<string>", rresult );

        }
        {
            final Method m = type.getMethod("method2_1", Sample2.class);
            final Type rType = m.getGenericReturnType();
            final String result = Java2TSConverter.convertJavaToTS(rType, m,
                    TSType.of(type),
                    declaredClassMap(Sample2.class, CharSequence.class),
                    true,
                    Optional.empty());
            assertNotNull(result);
            assertEquals( "string", result );

            final Type pType = m.getParameters()[0].getParameterizedType();
            final String rresult = Java2TSConverter.convertJavaToTS(pType, m,
                    TSType.of(type),
                    declaredClassMap(Sample2.class),
                    true,
                    Optional.empty());
            assertNotNull(rresult);
            assertEquals( "Sample2<any /*java.lang.CharSequence*/>", rresult );

        }
        {
            final Method m = type.getMethod("method2_2", Consumer.class);
            final String result = getReturnType(
                    declaredClassMap(Sample2.class, CharSequence.class),
                    type,
                    m);

            assertNotNull(result);
            assertEquals( "string", result );

            final Type pType = m.getParameters()[0].getParameterizedType();
            final String rresult = Java2TSConverter.convertJavaToTS(pType, m,
                    TSType.of(type),
                    declaredTypeMap(TSType.of(Sample2.class), TSType.of(Consumer.class).setExport(false).setAlias("Consumer")),
                    true,
                    Optional.empty());
            assertNotNull(rresult);
            assertEquals( "Consumer<E>", rresult );

        }

        {
            final String result = getReturnType(
                    type,
                    "method3");
            assertNotNull(result);
            assertEquals( "E", result );
        }

        {

            final String[] arr = {};
            final Method m = type.getMethod("method6", arr.getClass());

            final String result = converter.getMethodParametersAndReturnDecl(m, TSType.of(type), Collections.emptyMap(), true);

            assertNotNull(result);
            assertEquals( "( ...args:string[] ):void", result );
        }

        {
            final String result = getReturnType(
                    declaredClassMap(java.util.Map.class),
                    type,
                    "getAttributeMap");
            assertNotNull(result);
            assertEquals( "java.util.Map<any /*java.lang.Object*/, any /*java.lang.Object*/>", result );

        }

    }

    @Test
    public void testSample1_GenericArrayType() throws Exception {
        final Class<?> type = Sample1.class;
        final Object[] arr = {};

        final Method m = type.getMethod("genericArrayType", arr.getClass());
        final String result =
                converter.getMethodParametersAndReturnDecl(m,
                        TSType.of(type),
                        declaredTypeMap(TSType.of(java.util.List.class)),
                        true);

        assertNotNull(result);
        assertEquals( "( c:[E] ):java.util.List<T[]>", result );


    }


}
