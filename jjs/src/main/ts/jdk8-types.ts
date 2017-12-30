/// <reference path="jdk8.d.ts" />


interface StreamStatic {

	concat<T>( arg0:java.util.stream.Stream<T>, arg1:java.util.stream.Stream<T> ):java.util.stream.Stream<T>;
	empty<T>(  ):java.util.stream.Stream<T>;
	generate<T>( arg0:Supplier<T> ):java.util.stream.Stream<T>;
	iterate<T>( arg0:any /* java.lang.Object */, arg1:UnaryOperator<T> ):java.util.stream.Stream<T>;
	of<T>( arg0:[any] /* [Ljava.lang.Object; */ ):java.util.stream.Stream<T>;
	of<T>( arg0:any /* java.lang.Object */ ):java.util.stream.Stream<T>;
}

export const Stream: StreamStatic = Java.type("java.util.stream.Stream");
