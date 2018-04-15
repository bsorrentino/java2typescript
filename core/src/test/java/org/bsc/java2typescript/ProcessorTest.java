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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bsc.java2typescript.TSType;
import org.bsc.java2typescript.TypescriptConverter;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public class ProcessorTest {

    final TypescriptConverter converter = new TypescriptConverter();
	
	private java.util.Map<String,TSType> declaredClassMap( Class<?> ... classes) {
		return Stream.of( classes )
			.collect( Collectors.toMap( c -> c.getName(), c -> TSType.from(c) ))
			;		
	}
	private java.util.Map<String,TSType> declaredTypeMap( TSType ... types) {
		return Stream.of( types )
			.collect( Collectors.toMap( t -> t.getValue().getName(), t -> t ))
			;		
	}
	
	@Test
	public void testWildcardType() throws Exception {
		final TSType type = TSType.from(Sample1.class);
		{
			final Method m = type.getValue().getMethod("merge", Sample2.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.from(String.class), TSType.from(Sample2.class)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("<T>( source:Sample2<Sample2<T>> ):void"));
		}
		{
			final Method m = type.getValue().getMethod("merge", BiConsumer.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.from(String.class), TSType.from(Sample2.class), TSType.from(BiConsumer.class)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("<T>( source:BiConsumer<E, Sample2<Sample2<T>>> ):void"));
		}
		{
			final Method m = type.getValue().getMethod("concatMap", Function.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.from(String.class), TSType.from(Sample2.class), TSType.from(Function.class, "Func", false)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("<T>( mapper:Func<E, Sample2<T>> ):T"));
		}
	
	}
	
	
	@Test
	public void testFunctionalInterface() throws Exception {
		final TSType type = TSType.from(Sample1.class);
		{
			final Method m = type.getValue().getMethod("transform", java.util.function.Function.class );	
			final Type pType = m.getGenericParameterTypes()[0];
			final String result = TypescriptConverter.convertJavaToTS(pType, m, 
					type,
					declaredTypeMap( TSType.from(String.class), TSType.from(java.util.function.Function.class, "Func", false)), 
					true, 
					Optional.empty());
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("Func<E, string>"));
			
			
		}
		{
			final Method m = type.getValue().getMethod("transform", java.util.function.Function.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.from(String.class), TSType.from(java.util.function.Function.class, "Func", false)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("( transformer:Func<E, string> ):string"));
		}
		{
			final Method m = type.getValue().getMethod("creator", java.util.concurrent.Callable.class );	
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					type, 
					declaredTypeMap( TSType.from(String.class), TSType.from(java.util.concurrent.Callable.class, "Supplier", false)), 
					true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());		
			Assert.assertThat( result, IsEqual.equalTo("( supplier:Supplier<E> ):E"));
		}
		
	}

	@Test
	public void testClassDecl() throws Exception {
		
		{
			final String result  = 
					converter.getClassDecl( TSType.from(ArrayList.class), Collections.emptyMap());
		
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
			final String result = TypescriptConverter.convertJavaToTS(rType, m, 
					TSType.from(type),
					declaredTypeMap( TSType.from(String.class), TSType.from(java.util.List.class, "List", true) ), 
					true, 
					Optional.empty());
			Assert.assertThat( result, IsNull.notNullValue());	
			Assert.assertThat( result, IsEqual.equalTo("List<string>"));
		}
		{
			final Method m = type.getMethod("getAttributeList", java.util.List.class);			
			
			final String result = converter.getMethodParametersAndReturnDecl( m, 
					TSType.from(type), 
					declaredTypeMap( TSType.from(String.class), TSType.from(java.util.List.class, "List", true) ),  
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
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				Collections.emptyMap(), 
				true, 
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any/*C*/"));
		}
		{
		final Method m = type.getMethod("method5");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				Collections.emptyMap(), 
				true, 
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any/*C*/"));
		}
		{
		final Method m = type.getMethod("method1_1");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				declaredClassMap(Sample2.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("Sample2<string>"));
		}
		{
		final Method m = type.getMethod("method1_2");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
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
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				declaredClassMap(java.util.function.BiPredicate.class),
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
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				Collections.emptyMap(),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any /*java.util.function.BiPredicate*/"));
		}		
		{
		final Method m = type.getMethod("method1");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				declaredClassMap(java.util.Map.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("java.util.Map<E, string>"));
		}

		{
		final Method m = type.getMethod("method2", Sample2.class);			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				Collections.emptyMap(),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("string"));
		
		final Type pType = m.getParameters()[0].getParameterizedType();
		final String rresult = TypescriptConverter.convertJavaToTS(pType, m, 
				TSType.from(type),
				declaredClassMap(Sample2.class),
				true,
				Optional.empty());
		Assert.assertThat( rresult, IsNull.notNullValue());		
		Assert.assertThat( rresult, IsEqual.equalTo("Sample2<string>"));
		
		}
		{
		final Method m = type.getMethod("method2_1", Sample2.class);			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				declaredClassMap(Sample2.class, CharSequence.class),
				true,
				Optional.empty());
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("string"));
		
		final Type pType = m.getParameters()[0].getParameterizedType();
		final String rresult = TypescriptConverter.convertJavaToTS(pType, m, 
				TSType.from(type),
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
			final String rresult = TypescriptConverter.convertJavaToTS(pType, m, 
					TSType.from(type),
					declaredClassMap(Sample2.class, Consumer.class),
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
		
		final String result = converter.getMethodParametersAndReturnDecl( m, TSType.from(type), Collections.emptyMap(), true) ;
		
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
	
	String getReturnType( Class<?> type, String methonName, Class<?> ...args ) throws Exception {
		return getReturnType(Collections.emptyMap(), type, methonName, (Class<?>[])args);
	}
	
	String getReturnType( java.util.Map<String, TSType> declaredClassMap, Class<?> type, String methodName, Class<?> ...args ) throws Exception 
	{
		final Method m = type.getMethod(methodName, (Class<?>[])args);			
		return getReturnType( declaredClassMap, type, m);
	}
	
	String getReturnType( java.util.Map<String, TSType> declaredClassMap, Class<?> type, Method m ) throws Exception 
	{
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptConverter.convertJavaToTS(rType, m, 
				TSType.from(type),
				declaredClassMap, 
				true, 
				Optional.empty());
		return result;
	}
}
