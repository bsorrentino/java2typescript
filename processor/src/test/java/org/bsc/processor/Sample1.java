package org.bsc.processor;

import java.util.concurrent.Future;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public interface Sample1<E> {

	java.util.Map<E,String> method1();

	Sample2<String> method1_1();
	
	<E2> Comparable<Sample2<E2>> method1_2();
	
	<E2> BiPredicate<E2, E2> method1_3();
	
	String method2( Sample2<String> s2 );

	String method2_1( Sample2<? extends CharSequence> s2 );
	
	String method2_2( Consumer<? extends E> s2 );
	
	E method3();
	
	<C extends E> C method4();

	<C extends Future<E>> C method5();
	
	void method6( String ...args );
	
	java.util.Map<Object,Object> getAttributeMap();

	java.util.List<String> getAttributeList();

	java.util.List<String> getAttributeList( java.util.List<Integer> intList);
}
