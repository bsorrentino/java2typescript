package org.bsc.java2typescript;

/**
 * 
 */
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

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
	
	
	String transform( java.util.function.Function<E, String> transformer );

	E creator( java.util.concurrent.Callable<E> supplier);

	<T> void merge(Sample2<? extends Sample2<? extends T>> source);
	<T> void merge(BiConsumer<E,Sample2<? extends Sample2<? extends T>>> source);
    <T> T concatMap(Function<? super E, ? extends Sample2<? extends T>> mapper);

	<T> java.util.List<T[]> genericArrayType( E[] c );


}
