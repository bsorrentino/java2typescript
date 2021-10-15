package org.bsc.java2typescript;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 
 * @author bsorrentino
 *
 */
public class Issue7Test extends AbstractConverterTest {

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

	private Java2TSConverter converter;

	@Before
	public void initConverter() {
		converter =  Java2TSConverter.builder()
				.compatibility(Java2TSConverter.Compatibility.NASHORN)
				.build();
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
			
			assertNotNull( result );
			assertEquals( "( reducer:java.util.Map$Entry<K, V> ):void", result);
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
			
			assertNotNull( result );
			assertEquals( "<E>( reducer:java.util.List<java.util.Map$Entry<E, V>> ):void", result );
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
			
			assertNotNull( result );
			assertEquals( "(  ):java.util.Map$Entry<K, V>", result);
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
			
			assertNotNull( result);
			assertEquals( "<K,V>(  ):TestIssue7$TestBean1<K, V>", result);
		}

		
	}
	@Test
	public void testReduceEntries() throws Exception {
		final Class<?> type = TestBean.class;

		{
			final Method m = type.getMethod("reduceEntries", Long.TYPE, BiFunction.class);
			final String result =
					converter.getMethodParametersAndReturnDecl(m,
							TSType.of(type),
							declaredTypeMap(TSType.of(BiFunction.class), TSType.of(Map.Entry.class)),
							true);
			assertNotNull(result);
			assertEquals(
					"( parallelismThreshold:long, reducer:java.util.function.BiFunction<java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>> ):java.util.Map$Entry<K, V>",
					result);

		}
	}
}