// JAVA TYPESCRIPT DEDINITION
 
 
type int  	= number;
type long	= number;
type float	= number;
type double	= number;
type byte	= number;
type char	= number;


type chararray = [byte];
type bytearray = [char];
   

declare namespace java.lang {

	interface Class<T> {}
	interface AutoCloseable {}
	interface Cloneable {}
	interface Comparable<T> {

		compareTo?( arg0:T ):number;

	}
}            	

declare namespace java.util {
	
	interface RandomAccess {}
}

declare namespace java.io {
	
	interface Serializable {}
}

declare namespace java.util.stream {

	interface StreamInitializer<T>{ 
		//builder?(  ):any /* java.util.stream.Stream$Builder */;
		concat?( arg0:Stream<T>,arg1:Stream<T> ):Stream<T>;
		empty?(  ):Stream<T>;
		generate?( arg0:any /* java.util.function.Supplier */ ):Stream<T>;
		iterate?( arg0:java.lang.Object,arg1:any /* java.util.function.UnaryOperator */ ):Stream<T>;
		of?( arg0:[any] /* [Ljava.lang.Object; */ ):Stream<T>;
		of?( arg0:java.lang.Object ):Stream<T>;
	
	}

}
