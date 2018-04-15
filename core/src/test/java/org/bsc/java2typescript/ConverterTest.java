package org.bsc.java2typescript;


import java.util.function.Consumer;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {

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
        
        
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(java.lang.Runnable.class) , is(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Consumer.class) , is(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Action.class) , is(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Action.class, (m-> Assert.assertThat(m.getName(), equalTo("apply")))) , is(true));
        Assert.assertThat(TypescriptConverter.isFunctionalInterface(Action2.class) , is(false));
    }
}
