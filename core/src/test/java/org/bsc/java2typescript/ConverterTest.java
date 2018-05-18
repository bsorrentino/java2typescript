package org.bsc.java2typescript;



import static org.hamcrest.core.IsEqual.equalTo;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

public class ConverterTest extends AbstractConverterTest {

    interface Action {
        
        default void m1() {};
        
        void apply();
    }
    interface Action2 {
        
        void m1();
        
        void apply();
    }
    @Test
    public void functionalInterfaceTest() {
        
        
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(java.lang.Runnable.class) , equalTo(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Consumer.class) , equalTo(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Action.class) , equalTo(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Action.class), equalTo(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Action2.class) , equalTo(false));
    }
    
}
