package org.bsc.java2typescript;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiFunction;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.Assert;

public class TestIssue7 extends AbstractConverterTest {

    interface TestBean<K,V> {

        void method1( Map.Entry<? extends K,V> reducer);
        <E extends K> void method2( java.util.List<Map.Entry<E,V>> reducer);
    
        Map.Entry<K,V>	reduceEntries(long parallelismThreshold, BiFunction<Map.Entry<K,V>,Map.Entry<K,V>,? extends Map.Entry<K,V>> reducer);
    
    }

	@Test
	public void testMethod1() throws Exception {
		final Class<?> type = TestBean.class;
		
		{
			final Method m = type.getMethod("method1", Map.Entry.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.from(type), 
									declaredTypeMap( TSType.from(BiFunction.class), TSType.from(Map.Entry.class)),
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
									TSType.from(type), 
									declaredTypeMap( TSType.from(java.util.List.class), TSType.from(Map.Entry.class)),
									true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("<E>( reducer:java.util.List<java.util.Map$Entry<E, V>> ):void"));
		}

		
	}
	@Test
	public void testReduceEntries() throws Exception {
		final Class<?> type = TestBean.class;
		
		{
		final Method m = type.getMethod("reduceEntries", Long.TYPE, BiFunction.class);
		final String result = 
			converter.getMethodParametersAndReturnDecl( m, 
								TSType.from(type), 
								declaredTypeMap( TSType.from(BiFunction.class), TSType.from(Map.Entry.class)),
								true) ;
		
		Assert.assertThat( result, IsNull.notNullValue());
		Assert.assertThat( result, IsEqual.equalTo("( parallelismThreshold:long, reducer:java.util.function.BiFunction<java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>> ):java.util.Map$Entry<K, V>"));
		}
		
		
	}
}