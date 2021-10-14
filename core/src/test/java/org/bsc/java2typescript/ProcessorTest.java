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
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author bsorrentino
 *
 */
public class ProcessorTest extends AbstractConverterTest {

	@Test
	public void testWildcardType() throws Exception {
		final TSType type = TSType.of(Sample1.class);
		{
			final Method m = type.getValue().getMethod("merge", Sample2.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.of(String.class), TSType.of(Sample2.class)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("<T>( source:Sample2<Sample2<T>> ):void"));
		}
		{
			final Method m = type.getValue().getMethod("merge", BiConsumer.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.of(String.class), TSType.of(Sample2.class), TSType.of(BiConsumer.class).setExport(false).setAlias("BiConsumer")), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("<T>( source:BiConsumer<E, Sample2<Sample2<T>>> ):void"));
		}
		{
			final Method m = type.getValue().getMethod("concatMap", Function.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.of(String.class), TSType.of(Sample2.class), TSType.of(Function.class).setExport(false).setAlias("Func")), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("<T>( mapper:Func<E, Sample2<T>> ):T"));
		}
	
	}
	
	
	@Test
	public void testFunctionalInterface() throws Exception {
		final TSType type = TSType.of(Sample1.class);
		{
			final Method m = type.getValue().getMethod("transform", java.util.function.Function.class );	
			final Type pType = m.getGenericParameterTypes()[0];
			final String result = Java2TSConverter.convertJavaToTS(pType, m,
					type,
					declaredTypeMap( TSType.of(String.class), TSType.of(java.util.function.Function.class).setAlias("Func").setExport(false)), 
					true, 
					Optional.empty());
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("Func<E, string>"));
			
			
		}
		{
			final Method m = type.getValue().getMethod("transform", java.util.function.Function.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.of(String.class), TSType.of(java.util.function.Function.class).setAlias("Func").setExport(false)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("( transformer:Func<E, string> ):string"));
		}
		{
			final Method m = type.getValue().getMethod("creator", java.util.concurrent.Callable.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.of(String.class), TSType.of(java.util.concurrent.Callable.class).setExport(false).setAlias("Supplier")), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("( supplier:Supplier<E> ):E"));
		}
		
	}

	@Test
	public void testClassDecl() throws Exception {

        TSConverterContext ctx = TSConverterContext.of(TSType.of(ArrayList.class), Collections.emptyMap(), Java2TSConverter.Options.of(Compatibility.NASHORN ));
		{
			final String result  = ctx.getClassDecl().toString();
		
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("class ArrayList<E>/* extends AbstractList<E> implements List<E>, RandomAccess, java.lang.Cloneable, java.io.Serializable*/ {"));
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
					declaredTypeMap( TSType.of(String.class), TSType.of(java.util.List.class).setExport(false).setAlias("List") ), 
					true, 
					Optional.empty());
			Assert.assertThat( result, IsNull.notNullValue());	
			Assert.assertThat( result, IsEqual.equalTo("List<string>"));
		}
		{
			final Method m = type.getMethod("getAttributeList", java.util.List.class);			
			
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					TSType.of(type), 
					declaredTypeMap( TSType.of(String.class), TSType.of(java.util.List.class).setExport(false).setAlias("List") ),  
					true) ;			
			
			Assert.assertThat( result, IsNull.notNullValue());	
			Assert.assertThat( result, IsEqual.equalTo("( intList:List<int|null> ):List<string>"));
		}
		
	}
	
	@Test
	public void testSample1() throws Exception {
		
		final Class<?> type = Sample1.class;
		
		Stream.of(type.getTypeParameters()).forEach( v -> System.out.println(v.getName())) ;
		
		final java.util.Set<String> TypeVarSet = new java.util.HashSet<>(5);
		
		final Consumer<TypeVariable<?>> addTypeVar = tv -> TypeVarSet.add(tv.getName()) ;

		{
		final Method m = type.getMethod("method4");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				Collections.emptyMap(), 
				true, 
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any/*C*/"));
		}
		{
		final Method m = type.getMethod("method5");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				Collections.emptyMap(), 
				true, 
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any/*C*/"));
		}
		{
		final Method m = type.getMethod("method1_1");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				declaredClassMap(Sample2.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("Sample2<string>"));
		}
		{
		final Method m = type.getMethod("method1_2");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				declaredClassMap(Sample2.class,java.lang.Comparable.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("java.lang.Comparable<Sample2<any>>"));
		}
		{
		TypeVarSet.clear();
		final Method m = type.getMethod("method1_3");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type), 
				declaredTypeMap( TSType.of(java.util.function.BiPredicate.class).setExport(false).setAlias("BiPredicate") ),
				true,
				Optional.of(addTypeVar));
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("BiPredicate<E2, E2>"));
		Assert.assertThat( TypeVarSet.size(), IsEqual.equalTo(1));
		Assert.assertThat( TypeVarSet.contains("E2"), Is.is(true));
		
		}		
		{
		final Method m = type.getMethod("method1_3");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				Collections.emptyMap(),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any /*java.util.function.BiPredicate*/"));
		}		
		{
		final Method m = type.getMethod("method1");			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				declaredClassMap(java.util.Map.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("java.util.Map<E, string>"));
		}

		{
		final Method m = type.getMethod("method2", Sample2.class);			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				Collections.emptyMap(),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("string"));
		
		final Type pType = m.getParameters()[0].getParameterizedType();
		final String rresult = Java2TSConverter.convertJavaToTS(pType, m,
				TSType.of(type),
				declaredClassMap(Sample2.class),
				true,
				Optional.empty());
		Assert.assertThat( rresult, IsNull.notNullValue());		
		Assert.assertThat( rresult, IsEqual.equalTo("Sample2<string>"));
		
		}
		{
		final Method m = type.getMethod("method2_1", Sample2.class);			
		final Type rType = m.getGenericReturnType();
		final String result = Java2TSConverter.convertJavaToTS(rType, m,
				TSType.of(type),
				declaredClassMap(Sample2.class, CharSequence.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("string"));
		
		final Type pType = m.getParameters()[0].getParameterizedType();
		final String rresult = Java2TSConverter.convertJavaToTS(pType, m,
				TSType.of(type),
				declaredClassMap(Sample2.class),
				true,
				Optional.empty());
		Assert.assertThat( rresult, IsNull.notNullValue());		
		Assert.assertThat( rresult, IsEqual.equalTo("Sample2<any /*java.lang.CharSequence*/>"));
		
		}
		{
			final Method m = type.getMethod("method2_2", Consumer.class);			
			final String result = getReturnType( 
					declaredClassMap(Sample2.class, CharSequence.class), 
					type,
					m);
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("string"));
		
			final Type pType = m.getParameters()[0].getParameterizedType();
			final String rresult = Java2TSConverter.convertJavaToTS(pType, m,
					TSType.of(type),
					declaredTypeMap( TSType.of(Sample2.class), TSType.of(Consumer.class).setExport(false).setAlias("Consumer")),
					true,
					Optional.empty());
			Assert.assertThat( rresult, IsNull.notNullValue());		
			Assert.assertThat( rresult, IsEqual.equalTo("Consumer<E>"));
		
		}
		
		{
			final String result = getReturnType( 
					type, 
					"method3");
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("E"));
		}

		{
			
		final String[] arr = {};
		final Method m = type.getMethod("method6", arr.getClass());	
		
		final String result = converter.getMethodParametersAndReturnDecl( m, TSType.of(type), Collections.emptyMap(), true) ;
		
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("( ...args:string[] ):void"));
		}
		
		{
			final String result = getReturnType( 
					declaredClassMap(java.util.Map.class), 
					type, 
					"getAttributeMap");
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("java.util.Map<any /*java.lang.Object*/, any /*java.lang.Object*/>"));
			
		}
		
	}
	
	@Test
	public void testSample1_GenericArrayType() throws Exception {
		final Class<?> type = Sample1.class;
		final Object[] arr = {};
	
		final Method m = type.getMethod("genericArrayType", arr.getClass());
		final String result = 
			converter.getMethodParametersAndReturnDecl( m, 
								TSType.of(type), 
								declaredTypeMap( TSType.of(java.util.List.class)),
								true) ;
		
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("( c:[E] ):java.util.List<T[]>"));

	
	}


}
