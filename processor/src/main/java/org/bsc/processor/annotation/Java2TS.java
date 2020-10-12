package org.bsc.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author bsorrentino
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target( {ElementType.TYPE, ElementType.PACKAGE} )
public @interface Java2TS {
    String name() default "";
    Type[] declare() default {};
}