package org.bsc.java2typescript;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 * @author bsorrentino
 *
 */
public class ConverterTest extends AbstractConverterTest {

    @FunctionalInterface
    interface Action {
        
        default void m1() {};
        
        void apply();
    }
    interface Action2 {
        
        void m1();
        
        void apply();
    }

    interface TestBean {

        void method1( Map.Entry<Object, java.util.List<?>> p1 );
        void method2( Function<String, ? extends java.util.List<?>> p1 );
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
			final Method m = type.getMethod("method1", java.util.Map.Entry.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m,
									TSType.of(type),
									declaredTypeMap( TSType.of(Map.Entry.class), TSType.of(java.util.List.class)),
									true ) ;
			
			assertNotNull( result );
			assertEquals(
    "( p1:java.util.Map$Entry<any /*java.lang.Object*/, java.util.List<any /*java.lang.Object*/>> ):void",
                result);
		}

		
	}
    @Test
	public void testMethod2() throws Exception {
		final Class<?> type = TestBean.class;
		

		{
			final Method m = type.getMethod("method2", Function.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m,
									TSType.of(type),
									declaredTypeMap( TSType.of(Function.class), TSType.of(java.util.List.class)),
									true) ;
			
			assertNotNull( result );
			assertEquals( "( p1:java.util.function.Function<string, java.util.List<any /*java.lang.Object*/>> ):void", result);
		}
	
	}

    @Test
    public void functionalInterfaceTest() {
        
        
        assertTrue(TSType.of(java.lang.Runnable.class).isFunctional());
        {
            TSType t = TSType.of(Consumer.class);
            assertTrue(t.isFunctional());
            t.setFunctional(false);
            assertTrue(t.isFunctional());

        }
        assertTrue(TSType.of(Action.class).isFunctional());
        {
            TSType t = TSType.of(Action2.class);
            assertFalse(t.isFunctional());
            t.setFunctional(true);
            assertFalse(t.isFunctional());
        }

    }
    
}
