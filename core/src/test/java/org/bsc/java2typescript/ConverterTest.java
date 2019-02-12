package org.bsc.java2typescript;



import static org.hamcrest.core.IsEqual.equalTo;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
	public void testMethod1() throws Exception {
		final Class<?> type = TestBean.class;
		

		{
			final Method m = type.getMethod("method1", java.util.Map.Entry.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.from(type), 
									declaredTypeMap( TSType.from(Map.Entry.class), TSType.from(java.util.List.class)),
									true ) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("( p1:java.util.Map$Entry<any /*java.lang.Object*/, java.util.List<any /*java.lang.Object*/>> ):void"));
		}

		
	}
    @Test
	public void testMethod2() throws Exception {
		final Class<?> type = TestBean.class;
		

		{
			final Method m = type.getMethod("method2", Function.class);
			final String result = 
				converter.getMethodParametersAndReturnDecl( m, 
									TSType.from(type), 
									declaredTypeMap( TSType.from(Function.class), TSType.from(java.util.List.class)),
									true) ;
			
			Assert.assertThat( result, IsNull.notNullValue());
			Assert.assertThat( result, IsEqual.equalTo("( p1:java.util.function.Function<string, java.util.List<any /*java.lang.Object*/>> ):void"));
		}
	
	}

    @Test
    public void functionalInterfaceTest() {
        
        
        Assert.assertThat(TSType.from(java.lang.Runnable.class).isFunctional() , equalTo(true));
        {
            TSType t = TSType.from(Consumer.class);
            Assert.assertThat(t.isFunctional() , equalTo(true));
            t.setFunctional(false);
            Assert.assertThat(t.isFunctional() , equalTo(true));

        }
        Assert.assertThat(TSType.from(Action.class).isFunctional() , equalTo(true));
        {
            TSType t = TSType.from(Action2.class);
            Assert.assertThat(t.isFunctional() , equalTo(false));
            t.setFunctional(true);
            Assert.assertThat(t.isFunctional() , equalTo(false));
        }

    }
    
}
