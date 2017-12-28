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

// Nashorn
declare function print( ...args: any[]):void

type Runnable = () => void;

interface Supplier<T> {
	():T; //get():T
}

interface Consumer<T> /*extends Function*/ {
	andThen?( arg0:Consumer<T> ):Consumer<T>;
	( v:T ):void; //accept(t:T):void
}

interface ConsumerConstructor {
    new<T>( args: T): Consumer<T>;
    <T>( args: T): Consumer<T>;
    readonly prototype: Function;
}

declare const Consumer : ConsumerConstructor;

interface UnaryOperator<T>/* extends Function<T,any>*/ {

	andThen?( arg0:any /* java.util.function.Function */ ):any /* java.util.function.Function */;
	compose?( arg0:any /* java.util.function.Function */ ):any /* java.util.function.Function */;
	( v:T ):T //apply(t:T):T

}

interface UnaryOperatorConstructor {
    new<T>( args: T): UnaryOperator<T>;
    <T>( args: T): UnaryOperator<T>;
    /*static */identity<T>(  ):UnaryOperator<T>;
    readonly prototype: Function;
}

declare const UnaryOperator : UnaryOperatorConstructor;

interface Predicate<T>  {

	and?( arg0:Predicate<T> ):Predicate<T>;
	negate?():Predicate<T>;
	or?( arg0:Predicate<T> ):Predicate<T>;
	( v:T ):boolean //test( arg0:T /* java.lang.Object */ ):boolean;

}

interface PredicateConstructor {
    new<T>( args: T): UnaryOperator<T>;
    <T>( args: T): UnaryOperator<T>;

	/*static*/ isEqual<T>( arg0:any /* java.lang.Object */ ):Predicate<T>;

}

declare const Predicate : PredicateConstructor;

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

declare namespace java.util.stream {

	interface StreamConstructor{
		builder<T>(  ):Stream.Builder<T>;
		concat<T>( arg0:Stream<T>,arg1:Stream<T> ):Stream<T>;
		empty<T>(  ):Stream<T>;
		generate<T>( arg0:Supplier<T> ):Stream<T>;
		iterate<T>( seed:T,arg1:UnaryOperator<T> ):Stream<T>;
		of<T>( arg0:Array<T> ):Stream<T>;
		of<T>( arg0:T ):Stream<T>;

	}

	export const Stream : java.util.stream.StreamConstructor;
}
