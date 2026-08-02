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
@Target( {ElementType.ANNOTATION_TYPE} )
public @interface Type {
	Class<?> value();
	boolean export()		default false ;
	String alias()			default "";
	boolean functional()	default false;
	// When false, the type is registered for name resolution / as an `extends` target only, and is
	// NOT emitted as a declaration (nor added to the type registry). Use it to reference a type
	// declared in another generated file (e.g. a base class emitted by a separate build).
	boolean declare()		default true;
}
