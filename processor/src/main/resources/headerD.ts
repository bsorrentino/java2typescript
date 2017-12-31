//
// JAVA TYPESCRIPT DEDINITION
//


type int    = number;
type long   = number;
type float	= number;
type double = number;
type byte   = number;
type char   = string;

type chararray = [byte];
type bytearray = [char];

type Runnable = () => void;

type Supplier<T> = () => T;

type Consumer<T> = ( v:T) => void;

type UnaryOperator<T> = ( v:T ) => T ;

type Predicate<T>  = ( v:T ) => boolean;

type Comparator<T> = ( o1:T, o2:T ) => int;

//
// NASHORN
//

declare function print( ...args: any[]):void

declare function load( module:string ):void

declare namespace Java {

  export function type<T>( t:string):T;

  export function from<T>( list:java.util.List<T>):Array<T> ;
}


declare namespace java.lang {

	interface Class<T> {}
	interface AutoCloseable {}
	interface Cloneable {}

	interface Comparable<T> {

		compareTo?( arg0:T ):number;

	}

	type Object = any;
}

declare namespace java.util {

	interface RandomAccess {}
}

declare namespace java.io {

	interface Closeable {}
	interface Serializable {}
}
