package org.bsc.java2typescript;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiFunction;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author bsorrentino
 *
 */
public class TestIssue7 extends AbstractConverterTest {

    interface TestBean<K,V> {

        void method1( Map.Entry<? extends K,V> reducer);
        <E extends K> void method2( java.util.List<Map.Entry<E,V>> reducer);
		
		Map.Entry<K,V> method3();

        Map.Entry<K,V>	reduceEntries(long parallelismThreshold, BiFunction<Map.Entry<K,V>,Map.Entry<K,V>,? extends Map.Entry<K,V>> reducer);
	
		
	}
	
	public static class TestBean1<K,V> {
		
		public TestBean1() {

		}
	}

	@Test
	public void testMethod1() throws Exception {
		final Class<?> type = TestBean.class;
		
		{
			final Method m = type.getMethod("method1", Map.Entry.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.of(type),
									declaredTypeMap( TSType.of(BiFunction.class), TSType.of(Map.Entry.class)),
									true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("( reducer:java.util.Map$Entry<K, V> ):void"));
		}
		
	}
	@Test
	public void testMethod2() throws Exception {
		final Class<?> type = TestBean.class;
		

		{
			final Method m = type.getMethod("method2", java.util.List.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.of(type),
									declaredTypeMap( TSType.of(java.util.List.class), TSType.of(Map.Entry.class)),
									true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("<E>( reducer:java.util.List<java.util.Map$Entry<E, V>> ):void"));
		}

		
	}

	@Test
	public void testMethod3() throws Exception {
		final Class<?> type = TestBean.class;
		

		{
			final Method m = type.getMethod("method3");
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.of(type),
									declaredTypeMap( TSType.of(Map.Entry.class)),
									true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("(  ):java.util.Map$Entry<K, V>"));
		}

		
	}

	@Test
	public void testConstructor() throws Exception {
		final Class<?> type = TestBean1.class;
		

		{
			final Constructor<?> m = type.getConstructor();
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.of(type),
									declaredTypeMap( TSType.of(Map.Entry.class), TSType.of(type) ),
									true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("<K,V>(  ):TestIssue7$TestBean1<K, V>"));
		}

		
	}
	@Test
	public void testReduceEntries() throws Exception {
		final Class<?> type = TestBean.class;
		
		{
		final Method m = type.getMethod("reduceEntries", Long.TYPE, BiFunction.class);
		final String result = 
			converter.getMethodParametersAndReturnDecl( m, 
								TSType.of(type),
								declaredTypeMap( TSType.of(BiFunction.class), TSType.of(Map.Entry.class)),
								true) ;
		
		Assert.assertThat( result, IsNull.notNullValue());
		Assert.assertThat( result, IsEqual.equalTo("( parallelismThreshold:long, reducer:java.util.function.BiFunction<java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>> ):java.util.Map$Entry<K, V>"));
		}
		
		
	}
}