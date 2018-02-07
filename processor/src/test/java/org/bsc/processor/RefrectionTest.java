package org.bsc.processor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public class RefrectionTest {

	final TypescriptProcessor processor = new TypescriptProcessor();
	
	private java.util.Map<String,Class<?>> declaredClassMap( Class<?> ... classes) {
		return Stream.of( classes )
			.collect( Collectors.toMap( c -> c.getName(), c -> c) )
			;		
	}
	
	@Test
	public void testSample1() throws Exception {
		
		final Class<?> type = Sample1.class;
		
		Stream.of(type.getTypeParameters()).forEach( v -> System.out.println(v.getName())) ;
		
		
		{
		final Method m = type.getMethod("method4");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, Collections.emptyMap(), true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any/*C*/"));
		}
		{
		final Method m = type.getMethod("method5");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, Collections.emptyMap(), true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any/*C*/"));
		}
		{
		final Method m = type.getMethod("method1_1");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, 
				declaredClassMap(Sample2.class),
				true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("Sample2<string>"));
		}
		{
		final Method m = type.getMethod("method1_2");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, 
				declaredClassMap(Sample2.class,java.lang.Comparable.class),
				true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("java.lang.Comparable<Sample2<any>>"));
		}
		{
		final Method m = type.getMethod("method1_3");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, 
				declaredClassMap(java.util.function.BiPredicate.class),
				true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("BiPredicate<any, any>"));
		}		
		{
		final Method m = type.getMethod("method1_3");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, 
				Collections.emptyMap(),
				true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("any /*java.util.function.BiPredicate*/"));
		}		
		{
		final Method m = type.getMethod("method1");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, 
				declaredClassMap(java.util.Map.class),
				true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("java.util.Map<E, string>"));
		}

		{
		final Method m = type.getMethod("method2", Sample2.class);			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, 
				Collections.emptyMap(),
				true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("string"));
		
		final Type pType = m.getParameters()[0].getParameterizedType();
		final String rresult = TypescriptHelper.convertJavaToTS(pType, type, 
				declaredClassMap(Sample2.class),
				true);
		Assert.assertThat( rresult, IsNull.notNullValue());		
		Assert.assertThat( rresult, IsEqual.equalTo("Sample2<string>"));
		
		}
		
		{
		final Method m = type.getMethod("method3");			
		final Type rType = m.getGenericReturnType();
		final String result = TypescriptHelper.convertJavaToTS(rType, type, Collections.emptyMap(), true);
		Assert.assertThat( result, IsNull.notNullValue());		
		Assert.assertThat( result, IsEqual.equalTo("E"));
		}
		
	}
}
