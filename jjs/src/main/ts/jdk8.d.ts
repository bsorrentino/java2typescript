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
declare namespace java.lang {

interface Iterable<T> {

	forEach( arg0:Consumer<T> ):void;
	iterator(  ):java.util.Iterator<T>;
	spliterator(  ):any /* java.util.Spliterator */;

} // end Iterable<T>

} // end namespace java.lang
declare namespace java.lang {

interface CharSequence {

	charAt( arg0:int ):any /* char */;
	chars(  ):any /* java.util.stream.IntStream */;
	codePoints(  ):any /* java.util.stream.IntStream */;
	length(  ):int;
	subSequence( arg0:int, arg1:int ):CharSequence;
	toString(  ):string;

} // end CharSequence

} // end namespace java.lang
declare namespace java.lang {

class String/* extends ObjectString implements java.io.Serializable, Comparable<any>, CharSequence*/ {

	bytes:bytearray;
	empty:boolean;
	charAt( arg0:int ):any /* char */;
	chars(  ):any /* java.util.stream.IntStream */;
	codePointAt( arg0:int ):int;
	codePointBefore( arg0:int ):int;
	codePointCount( arg0:int, arg1:int ):int;
	codePoints(  ):any /* java.util.stream.IntStream */;
	compareTo( arg0:string ):int;
	compareToIgnoreCase( arg0:string ):int;
	concat( arg0:string ):string;
	contains( arg0:CharSequence ):boolean;
	contentEquals( arg0:CharSequence ):boolean;
	contentEquals( arg0:any /* java.lang.StringBuffer */ ):boolean;
	endsWith( arg0:string ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	equalsIgnoreCase( arg0:string ):boolean;
	getBytes( arg0:any /* java.nio.charset.Charset */ ):bytearray;
	getBytes( arg0:int, arg1:int, arg2:bytearray, arg3:int ):void;
	getBytes( arg0:string ):bytearray;
	getChars( arg0:chararray, arg1:int ):void;
	getChars( arg0:int, arg1:int, arg2:chararray, arg3:int ):void;
	hashCode(  ):int;
	indexOf( arg0:int ):int;
	indexOf( arg0:int, arg1:int ):int;
	indexOf( arg0:string ):int;
	indexOf( arg0:string, arg1:int ):int;
	indexOfSupplementary( arg0:int, arg1:int ):int;
	intern(  ):string;
	lastIndexOf( arg0:int ):int;
	lastIndexOf( arg0:int, arg1:int ):int;
	lastIndexOf( arg0:string ):int;
	lastIndexOf( arg0:string, arg1:int ):int;
	lastIndexOfSupplementary( arg0:int, arg1:int ):int;
	length(  ):int;
	matches( arg0:string ):boolean;
	nonSyncContentEquals( arg0:any /* java.lang.AbstractStringBuilder */ ):boolean;
	offsetByCodePoints( arg0:int, arg1:int ):int;
	regionMatches( arg0:boolean, arg1:int, arg2:string, arg3:int, arg4:int ):boolean;
	regionMatches( arg0:int, arg1:string, arg2:int, arg3:int ):boolean;
	replace( arg0:CharSequence, arg1:CharSequence ):string;
	replace( arg0:any /* char */, arg1:any /* char */ ):string;
	replaceAll( arg0:string, arg1:string ):string;
	replaceFirst( arg0:string, arg1:string ):string;
	split( arg0:string ):[any] /* [Ljava.lang.String; */;
	split( arg0:string, arg1:int ):[any] /* [Ljava.lang.String; */;
	startsWith( arg0:string ):boolean;
	startsWith( arg0:string, arg1:int ):boolean;
	static checkBounds( arg0:bytearray, arg1:int, arg2:int ):void;
	static copyValueOf( arg0:chararray ):string;
	static copyValueOf( arg0:chararray, arg1:int, arg2:int ):string;
	static format( arg0:any /* java.util.Locale */, arg1:string, arg2:[any] /* [Ljava.lang.Object; */ ):string;
	static format( arg0:string, arg1:[any] /* [Ljava.lang.Object; */ ):string;
	static indexOf( arg0:chararray, arg1:int, arg2:int, arg3:chararray, arg4:int, arg5:int, arg6:int ):int;
	static indexOf( arg0:chararray, arg1:int, arg2:int, arg3:string, arg4:int ):int;
	static join( arg0:CharSequence, arg1:Iterable<any> ):string;
	static join( arg0:CharSequence, arg1:[any] /* [Ljava.lang.CharSequence; */ ):string;
	static lastIndexOf( arg0:chararray, arg1:int, arg2:int, arg3:chararray, arg4:int, arg5:int, arg6:int ):int;
	static lastIndexOf( arg0:chararray, arg1:int, arg2:int, arg3:string, arg4:int ):int;
	static valueOf( arg0:any /* char */ ):string;
	static valueOf( arg0:any /* java.lang.Object */ ):string;
	static valueOf( arg0:boolean ):string;
	static valueOf( arg0:chararray ):string;
	static valueOf( arg0:chararray, arg1:int, arg2:int ):string;
	static valueOf( arg0:double ):string;
	static valueOf( arg0:float ):string;
	static valueOf( arg0:int ):string;
	static valueOf( arg0:long ):string;
	subSequence( arg0:int, arg1:int ):CharSequence;
	substring( arg0:int ):string;
	substring( arg0:int, arg1:int ):string;
	toCharArray(  ):chararray;
	toLowerCase(  ):string;
	toLowerCase( arg0:any /* java.util.Locale */ ):string;
	toString(  ):string;
	toUpperCase(  ):string;
	toUpperCase( arg0:any /* java.util.Locale */ ):string;
	trim(  ):string;

} // end String

} // end namespace java.lang
declare namespace java.util.stream {

interface BaseStream<T, S>/*BaseStream<T, S> extends java.lang.AutoCloseable*/ {

	// parallel?:boolean;
	close(  ):void;
	iterator(  ):java.util.Iterator<any>;
	onClose( arg0:Runnable ):BaseStream<T, S>;
	parallel(  ):BaseStream<T, S>;
	sequential(  ):BaseStream<T, S>;
	spliterator(  ):any /* java.util.Spliterator */;
	unordered(  ):BaseStream<T, S>;

} // end BaseStream<T, S>

} // end namespace java.util.stream
declare namespace java.util.stream {

interface Stream<T>/*Stream<T> extends BaseStream<T, any>*/ {

	// static builder<T>(  ):any /* java.util.stream.Stream$Builder */;
	// static concat<T>( arg0:Stream<T>, arg1:Stream<T> ):Stream<T>;
	// static empty<T>(  ):Stream<T>;
	// static generate<T>( arg0:Supplier<T> ):Stream<T>;
	// static iterate<T>( arg0:any /* java.lang.Object */, arg1:UnaryOperator<T> ):Stream<T>;
	// static of<T>( arg0:[any] /* [Ljava.lang.Object; */ ):Stream<T>;
	// static of<T>( arg0:any /* java.lang.Object */ ):Stream<T>;
	allMatch( arg0:Predicate<T> ):boolean;
	anyMatch( arg0:Predicate<T> ):boolean;
	close(  ):void;
	collect( arg0:Supplier<T>, arg1:any /* java.util.function.BiConsumer */, arg2:any /* java.util.function.BiConsumer */ ):any /* java.lang.Object */;
	collect( arg0:any /* java.util.stream.Collector */ ):any /* java.lang.Object */;
	count(  ):long;
	distinct(  ):Stream<T>;
	filter( arg0:Predicate<T> ):Stream<T>;
	findAny(  ):java.util.Optional<T>;
	findFirst(  ):java.util.Optional<T>;
	flatMap( arg0:any /* java.util.function.Function */ ):Stream<T>;
	flatMapToDouble( arg0:any /* java.util.function.Function */ ):any /* java.util.stream.DoubleStream */;
	flatMapToInt( arg0:any /* java.util.function.Function */ ):any /* java.util.stream.IntStream */;
	flatMapToLong( arg0:any /* java.util.function.Function */ ):any /* java.util.stream.LongStream */;
	forEach( arg0:Consumer<T> ):void;
	forEachOrdered( arg0:Consumer<T> ):void;
	isParallel(  ):boolean;
	iterator(  ):java.util.Iterator<T>;
	limit( arg0:long ):Stream<T>;
	map( arg0:any /* java.util.function.Function */ ):Stream<T>;
	mapToDouble( arg0:any /* java.util.function.ToDoubleFunction */ ):any /* java.util.stream.DoubleStream */;
	mapToInt( arg0:any /* java.util.function.ToIntFunction */ ):any /* java.util.stream.IntStream */;
	mapToLong( arg0:any /* java.util.function.ToLongFunction */ ):any /* java.util.stream.LongStream */;
	max( arg0:Comparator<T> ):java.util.Optional<T>;
	min( arg0:Comparator<T> ):java.util.Optional<T>;
	noneMatch( arg0:Predicate<T> ):boolean;
	onClose( arg0:Runnable ):BaseStream<T, any>;
	parallel(  ):BaseStream<T, any>;
	peek( arg0:Consumer<T> ):Stream<T>;
	reduce( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.BiFunction */, arg2:any /* java.util.function.BinaryOperator */ ):any /* java.lang.Object */;
	reduce( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.BinaryOperator */ ):any /* java.lang.Object */;
	reduce( arg0:any /* java.util.function.BinaryOperator */ ):java.util.Optional<T>;
	sequential(  ):BaseStream<T, any>;
	skip( arg0:long ):Stream<T>;
	sorted(  ):Stream<T>;
	sorted( arg0:Comparator<T> ):Stream<T>;
	spliterator(  ):any /* java.util.Spliterator */;
	toArray(  ):[any] /* [Ljava.lang.Object; */;
	toArray( arg0:any /* java.util.function.IntFunction */ ):[any] /* [Ljava.lang.Object; */;
	unordered(  ):BaseStream<T, any>;

} // end Stream<T>
export module Stream {

interface Builder<T>/*Builder<T> extends Consumer<T>*/ {

	accept( arg0:any /* java.lang.Object */ ):void;
	add( arg0:any /* java.lang.Object */ ):any /* java.util.stream.Stream$Builder */;
	andThen( arg0:Consumer<T> ):Consumer<T>;
	build(  ):Stream<T>;

} // end Builder<T>

} // end module Stream

} // end namespace java.util.stream
declare namespace java.util {

interface Iterator<E> {

	forEachRemaining( arg0:Consumer<E> ):void;
	hasNext(  ):boolean;
	next(  ):any /* java.lang.Object */;
	remove(  ):void;

} // end Iterator<E>

} // end namespace java.util
declare namespace java.util {

interface Comparator<T> {

	// static comparing<T>( arg0:any /* java.util.function.Function */ ):Comparator<T>;
	// static comparing<T>( arg0:any /* java.util.function.Function */, arg1:Comparator<T> ):Comparator<T>;
	// static comparingDouble<T>( arg0:any /* java.util.function.ToDoubleFunction */ ):Comparator<T>;
	// static comparingInt<T>( arg0:any /* java.util.function.ToIntFunction */ ):Comparator<T>;
	// static comparingLong<T>( arg0:any /* java.util.function.ToLongFunction */ ):Comparator<T>;
	// static naturalOrder<T>(  ):Comparator<T>;
	// static nullsFirst<T>( arg0:Comparator<T> ):Comparator<T>;
	// static nullsLast<T>( arg0:Comparator<T> ):Comparator<T>;
	// static reverseOrder<T>(  ):Comparator<T>;
	compare( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):int;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	reversed(  ):Comparator<T>;
	thenComparing( arg0:Comparator<T> ):Comparator<T>;
	thenComparing( arg0:any /* java.util.function.Function */ ):Comparator<T>;
	thenComparing( arg0:any /* java.util.function.Function */, arg1:Comparator<T> ):Comparator<T>;
	thenComparingDouble( arg0:any /* java.util.function.ToDoubleFunction */ ):Comparator<T>;
	thenComparingInt( arg0:any /* java.util.function.ToIntFunction */ ):Comparator<T>;
	thenComparingLong( arg0:any /* java.util.function.ToLongFunction */ ):Comparator<T>;

} // end Comparator<T>

} // end namespace java.util
declare namespace java.util {

interface Collection<E>/*Collection<E> extends java.lang.Iterable<E>*/ {

	empty?:boolean;
	add( arg0:any /* java.lang.Object */ ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	hashCode(  ):int;
	iterator(  ):Iterator<E>;
	parallelStream(  ):java.util.stream.Stream<E>;
	remove( arg0:any /* java.lang.Object */ ):boolean;
	removeAll( arg0:Collection<E> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	retainAll( arg0:Collection<E> ):boolean;
	size(  ):int;
	spliterator(  ):any /* java.util.Spliterator */;
	stream(  ):java.util.stream.Stream<E>;
	toArray(  ):[any] /* [Ljava.lang.Object; */;
	toArray( arg0:[any] /* [Ljava.lang.Object; */ ):[any] /* [Ljava.lang.Object; */;

} // end Collection<E>

} // end namespace java.util
declare namespace java.util {

interface Map<K, V> {

	empty?:boolean;
	clear(  ):void;
	compute( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.BiFunction */ ):any /* java.lang.Object */;
	computeIfAbsent( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.Function */ ):any /* java.lang.Object */;
	computeIfPresent( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.BiFunction */ ):any /* java.lang.Object */;
	containsKey( arg0:any /* java.lang.Object */ ):boolean;
	containsValue( arg0:any /* java.lang.Object */ ):boolean;
	entrySet(  ):Set<any>;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:any /* java.util.function.BiConsumer */ ):void;
	get( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	getOrDefault( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	hashCode(  ):int;
	keySet(  ):Set<any>;
	merge( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */, arg2:any /* java.util.function.BiFunction */ ):any /* java.lang.Object */;
	put( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	putAll( arg0:Map<K, V> ):void;
	putIfAbsent( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	remove( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	remove( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):boolean;
	replace( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	replace( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */ ):boolean;
	replaceAll( arg0:any /* java.util.function.BiFunction */ ):void;
	size(  ):int;
	values(  ):Collection<any>;

} // end Map<K, V>
export module Map {

interface Entry<K, V> {

	key?:any /* java.lang.Object */;
	value?:any /* java.lang.Object */;
	// static comparingByKey<T>(  ):Comparator<any>;
	// static comparingByKey<T>( arg0:Comparator<any> ):Comparator<any>;
	// static comparingByValue<T>(  ):Comparator<any>;
	// static comparingByValue<T>( arg0:Comparator<any> ):Comparator<any>;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	setValue( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;

} // end Entry<K, V>

} // end module Map

} // end namespace java.util
declare namespace java.util {

interface List<E>/*List<E> extends Collection<E>*/ {

	empty?:boolean;
	add( arg0:any /* java.lang.Object */ ):boolean;
	add( arg0:int, arg1:any /* java.lang.Object */ ):void;
	addAll( arg0:Collection<E> ):boolean;
	addAll( arg0:int, arg1:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	get( arg0:int ):any /* java.lang.Object */;
	hashCode(  ):int;
	indexOf( arg0:any /* java.lang.Object */ ):int;
	iterator(  ):Iterator<E>;
	lastIndexOf( arg0:any /* java.lang.Object */ ):int;
	listIterator(  ):any /* java.util.ListIterator */;
	listIterator( arg0:int ):any /* java.util.ListIterator */;
	parallelStream(  ):java.util.stream.Stream<E>;
	remove( arg0:any /* java.lang.Object */ ):boolean;
	remove( arg0:int ):any /* java.lang.Object */;
	removeAll( arg0:Collection<E> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	replaceAll( arg0:UnaryOperator<E> ):void;
	retainAll( arg0:Collection<E> ):boolean;
	set( arg0:int, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	size(  ):int;
	sort( arg0:Comparator<E> ):void;
	spliterator(  ):any /* java.util.Spliterator */;
	stream(  ):java.util.stream.Stream<E>;
	subList( arg0:int, arg1:int ):List<E>;
	toArray(  ):[any] /* [Ljava.lang.Object; */;
	toArray( arg0:[any] /* [Ljava.lang.Object; */ ):[any] /* [Ljava.lang.Object; */;

} // end List<E>

} // end namespace java.util
declare namespace java.util {

interface Set<E>/*Set<E> extends Collection<E>*/ {

	empty?:boolean;
	add( arg0:any /* java.lang.Object */ ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	hashCode(  ):int;
	iterator(  ):Iterator<E>;
	parallelStream(  ):java.util.stream.Stream<E>;
	remove( arg0:any /* java.lang.Object */ ):boolean;
	removeAll( arg0:Collection<E> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	retainAll( arg0:Collection<E> ):boolean;
	size(  ):int;
	spliterator(  ):any /* java.util.Spliterator */;
	stream(  ):java.util.stream.Stream<E>;
	toArray(  ):[any] /* [Ljava.lang.Object; */;
	toArray( arg0:[any] /* [Ljava.lang.Object; */ ):[any] /* [Ljava.lang.Object; */;

} // end Set<E>

} // end namespace java.util
declare namespace java.util {

class Arrays/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	static asList<E>( arg0:[any] /* [Ljava.lang.Object; */ ):List<any>;
	static binarySearch( arg0:[any] /* [D */, arg1:double ):int;
	static binarySearch( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:double ):int;
	static binarySearch( arg0:[any] /* [F */, arg1:float ):int;
	static binarySearch( arg0:[any] /* [F */, arg1:int, arg2:int, arg3:float ):int;
	static binarySearch( arg0:[any] /* [I */, arg1:int ):int;
	static binarySearch( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:int ):int;
	static binarySearch( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:long ):int;
	static binarySearch( arg0:[any] /* [J */, arg1:long ):int;
	static binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.Object */ ):int;
	static binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.Object */, arg2:Comparator<any> ):int;
	static binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */ ):int;
	static binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */, arg4:Comparator<any> ):int;
	static binarySearch( arg0:[any] /* [S */, arg1:any /* short */ ):int;
	static binarySearch( arg0:[any] /* [S */, arg1:int, arg2:int, arg3:any /* short */ ):int;
	static binarySearch( arg0:bytearray, arg1:any /* byte */ ):int;
	static binarySearch( arg0:bytearray, arg1:int, arg2:int, arg3:any /* byte */ ):int;
	static binarySearch( arg0:chararray, arg1:any /* char */ ):int;
	static binarySearch( arg0:chararray, arg1:int, arg2:int, arg3:any /* char */ ):int;
	static binarySearch0( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:double ):int;
	static binarySearch0( arg0:[any] /* [F */, arg1:int, arg2:int, arg3:float ):int;
	static binarySearch0( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:int ):int;
	static binarySearch0( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:long ):int;
	static binarySearch0( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */ ):int;
	static binarySearch0( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */, arg4:Comparator<any> ):int;
	static binarySearch0( arg0:[any] /* [S */, arg1:int, arg2:int, arg3:any /* short */ ):int;
	static binarySearch0( arg0:bytearray, arg1:int, arg2:int, arg3:any /* byte */ ):int;
	static binarySearch0( arg0:chararray, arg1:int, arg2:int, arg3:any /* char */ ):int;
	static copyOf( arg0:[any] /* [D */, arg1:int ):[any] /* [D */;
	static copyOf( arg0:[any] /* [F */, arg1:int ):[any] /* [F */;
	static copyOf( arg0:[any] /* [I */, arg1:int ):[any] /* [I */;
	static copyOf( arg0:[any] /* [J */, arg1:int ):[any] /* [J */;
	static copyOf( arg0:[any] /* [Ljava.lang.Object; */, arg1:int ):[any] /* [Ljava.lang.Object; */;
	static copyOf( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:java.lang.Class<any> ):[any] /* [Ljava.lang.Object; */;
	static copyOf( arg0:[any] /* [S */, arg1:int ):[any] /* [S */;
	static copyOf( arg0:[any] /* [Z */, arg1:int ):[any] /* [Z */;
	static copyOf( arg0:bytearray, arg1:int ):bytearray;
	static copyOf( arg0:chararray, arg1:int ):chararray;
	static copyOfRange( arg0:[any] /* [D */, arg1:int, arg2:int ):[any] /* [D */;
	static copyOfRange( arg0:[any] /* [F */, arg1:int, arg2:int ):[any] /* [F */;
	static copyOfRange( arg0:[any] /* [I */, arg1:int, arg2:int ):[any] /* [I */;
	static copyOfRange( arg0:[any] /* [J */, arg1:int, arg2:int ):[any] /* [J */;
	static copyOfRange( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):[any] /* [Ljava.lang.Object; */;
	static copyOfRange( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:java.lang.Class<any> ):[any] /* [Ljava.lang.Object; */;
	static copyOfRange( arg0:[any] /* [S */, arg1:int, arg2:int ):[any] /* [S */;
	static copyOfRange( arg0:[any] /* [Z */, arg1:int, arg2:int ):[any] /* [Z */;
	static copyOfRange( arg0:bytearray, arg1:int, arg2:int ):bytearray;
	static copyOfRange( arg0:chararray, arg1:int, arg2:int ):chararray;
	static deepEquals( arg0:[any] /* [Ljava.lang.Object; */, arg1:[any] /* [Ljava.lang.Object; */ ):boolean;
	static deepEquals0( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):boolean;
	static deepHashCode( arg0:[any] /* [Ljava.lang.Object; */ ):int;
	static deepToString( arg0:[any] /* [Ljava.lang.Object; */ ):string;
	static deepToString( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.StringBuilder */, arg2:Set<any> ):void;
	static equals( arg0:[any] /* [D */, arg1:[any] /* [D */ ):boolean;
	static equals( arg0:[any] /* [F */, arg1:[any] /* [F */ ):boolean;
	static equals( arg0:[any] /* [I */, arg1:[any] /* [I */ ):boolean;
	static equals( arg0:[any] /* [J */, arg1:[any] /* [J */ ):boolean;
	static equals( arg0:[any] /* [Ljava.lang.Object; */, arg1:[any] /* [Ljava.lang.Object; */ ):boolean;
	static equals( arg0:[any] /* [S */, arg1:[any] /* [S */ ):boolean;
	static equals( arg0:[any] /* [Z */, arg1:[any] /* [Z */ ):boolean;
	static equals( arg0:bytearray, arg1:bytearray ):boolean;
	static equals( arg0:chararray, arg1:chararray ):boolean;
	static fill( arg0:[any] /* [D */, arg1:double ):void;
	static fill( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:double ):void;
	static fill( arg0:[any] /* [F */, arg1:float ):void;
	static fill( arg0:[any] /* [F */, arg1:int, arg2:int, arg3:float ):void;
	static fill( arg0:[any] /* [I */, arg1:int ):void;
	static fill( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:int ):void;
	static fill( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:long ):void;
	static fill( arg0:[any] /* [J */, arg1:long ):void;
	static fill( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.Object */ ):void;
	static fill( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */ ):void;
	static fill( arg0:[any] /* [S */, arg1:any /* short */ ):void;
	static fill( arg0:[any] /* [S */, arg1:int, arg2:int, arg3:any /* short */ ):void;
	static fill( arg0:[any] /* [Z */, arg1:boolean ):void;
	static fill( arg0:[any] /* [Z */, arg1:int, arg2:int, arg3:boolean ):void;
	static fill( arg0:bytearray, arg1:any /* byte */ ):void;
	static fill( arg0:bytearray, arg1:int, arg2:int, arg3:any /* byte */ ):void;
	static fill( arg0:chararray, arg1:any /* char */ ):void;
	static fill( arg0:chararray, arg1:int, arg2:int, arg3:any /* char */ ):void;
	static hashCode( arg0:[any] /* [D */ ):int;
	static hashCode( arg0:[any] /* [F */ ):int;
	static hashCode( arg0:[any] /* [I */ ):int;
	static hashCode( arg0:[any] /* [J */ ):int;
	static hashCode( arg0:[any] /* [Ljava.lang.Object; */ ):int;
	static hashCode( arg0:[any] /* [S */ ):int;
	static hashCode( arg0:[any] /* [Z */ ):int;
	static hashCode( arg0:bytearray ):int;
	static hashCode( arg0:chararray ):int;
	static legacyMergeSort( arg0:[any] /* [Ljava.lang.Object; */ ):void;
	static legacyMergeSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:Comparator<any> ):void;
	static legacyMergeSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):void;
	static legacyMergeSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:Comparator<any> ):void;
	static mergeSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:[any] /* [Ljava.lang.Object; */, arg2:int, arg3:int, arg4:int ):void;
	static mergeSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:[any] /* [Ljava.lang.Object; */, arg2:int, arg3:int, arg4:int, arg5:Comparator<any> ):void;
	static parallelPrefix( arg0:[any] /* [D */, arg1:any /* java.util.function.DoubleBinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:any /* java.util.function.DoubleBinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [I */, arg1:any /* java.util.function.IntBinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:any /* java.util.function.IntBinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [J */, arg1:any /* java.util.function.LongBinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:any /* java.util.function.LongBinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.util.function.BinaryOperator */ ):void;
	static parallelPrefix( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.util.function.BinaryOperator */ ):void;
	static parallelSetAll( arg0:[any] /* [D */, arg1:any /* java.util.function.IntToDoubleFunction */ ):void;
	static parallelSetAll( arg0:[any] /* [I */, arg1:any /* java.util.function.IntUnaryOperator */ ):void;
	static parallelSetAll( arg0:[any] /* [J */, arg1:any /* java.util.function.IntToLongFunction */ ):void;
	static parallelSetAll( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.util.function.IntFunction */ ):void;
	static parallelSort( arg0:[any] /* [D */ ):void;
	static parallelSort( arg0:[any] /* [D */, arg1:int, arg2:int ):void;
	static parallelSort( arg0:[any] /* [F */ ):void;
	static parallelSort( arg0:[any] /* [F */, arg1:int, arg2:int ):void;
	static parallelSort( arg0:[any] /* [I */ ):void;
	static parallelSort( arg0:[any] /* [I */, arg1:int, arg2:int ):void;
	static parallelSort( arg0:[any] /* [J */ ):void;
	static parallelSort( arg0:[any] /* [J */, arg1:int, arg2:int ):void;
	static parallelSort( arg0:[any] /* [Ljava.lang.Comparable; */ ):void;
	static parallelSort( arg0:[any] /* [Ljava.lang.Comparable; */, arg1:int, arg2:int ):void;
	static parallelSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:Comparator<any> ):void;
	static parallelSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:Comparator<any> ):void;
	static parallelSort( arg0:[any] /* [S */ ):void;
	static parallelSort( arg0:[any] /* [S */, arg1:int, arg2:int ):void;
	static parallelSort( arg0:bytearray ):void;
	static parallelSort( arg0:bytearray, arg1:int, arg2:int ):void;
	static parallelSort( arg0:chararray ):void;
	static parallelSort( arg0:chararray, arg1:int, arg2:int ):void;
	static rangeCheck( arg0:int, arg1:int, arg2:int ):void;
	static setAll( arg0:[any] /* [D */, arg1:any /* java.util.function.IntToDoubleFunction */ ):void;
	static setAll( arg0:[any] /* [I */, arg1:any /* java.util.function.IntUnaryOperator */ ):void;
	static setAll( arg0:[any] /* [J */, arg1:any /* java.util.function.IntToLongFunction */ ):void;
	static setAll( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.util.function.IntFunction */ ):void;
	static sort( arg0:[any] /* [D */ ):void;
	static sort( arg0:[any] /* [D */, arg1:int, arg2:int ):void;
	static sort( arg0:[any] /* [F */ ):void;
	static sort( arg0:[any] /* [F */, arg1:int, arg2:int ):void;
	static sort( arg0:[any] /* [I */ ):void;
	static sort( arg0:[any] /* [I */, arg1:int, arg2:int ):void;
	static sort( arg0:[any] /* [J */ ):void;
	static sort( arg0:[any] /* [J */, arg1:int, arg2:int ):void;
	static sort( arg0:[any] /* [Ljava.lang.Object; */ ):void;
	static sort( arg0:[any] /* [Ljava.lang.Object; */, arg1:Comparator<any> ):void;
	static sort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):void;
	static sort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:Comparator<any> ):void;
	static sort( arg0:[any] /* [S */ ):void;
	static sort( arg0:[any] /* [S */, arg1:int, arg2:int ):void;
	static sort( arg0:bytearray ):void;
	static sort( arg0:bytearray, arg1:int, arg2:int ):void;
	static sort( arg0:chararray ):void;
	static sort( arg0:chararray, arg1:int, arg2:int ):void;
	static spliterator( arg0:[any] /* [D */ ):any /* java.util.Spliterator$OfDouble */;
	static spliterator( arg0:[any] /* [D */, arg1:int, arg2:int ):any /* java.util.Spliterator$OfDouble */;
	static spliterator( arg0:[any] /* [I */ ):any /* java.util.Spliterator$OfInt */;
	static spliterator( arg0:[any] /* [I */, arg1:int, arg2:int ):any /* java.util.Spliterator$OfInt */;
	static spliterator( arg0:[any] /* [J */ ):any /* java.util.Spliterator$OfLong */;
	static spliterator( arg0:[any] /* [J */, arg1:int, arg2:int ):any /* java.util.Spliterator$OfLong */;
	static spliterator<T>( arg0:[any] /* [Ljava.lang.Object; */ ):any /* java.util.Spliterator */;
	static spliterator<T>( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):any /* java.util.Spliterator */;
	static stream( arg0:[any] /* [D */ ):any /* java.util.stream.DoubleStream */;
	static stream( arg0:[any] /* [D */, arg1:int, arg2:int ):any /* java.util.stream.DoubleStream */;
	static stream( arg0:[any] /* [I */ ):any /* java.util.stream.IntStream */;
	static stream( arg0:[any] /* [I */, arg1:int, arg2:int ):any /* java.util.stream.IntStream */;
	static stream( arg0:[any] /* [J */ ):any /* java.util.stream.LongStream */;
	static stream( arg0:[any] /* [J */, arg1:int, arg2:int ):any /* java.util.stream.LongStream */;
	static stream<T>( arg0:[any] /* [Ljava.lang.Object; */ ):java.util.stream.Stream<any>;
	static stream<T>( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):java.util.stream.Stream<any>;
	static swap( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):void;
	static toString( arg0:[any] /* [D */ ):string;
	static toString( arg0:[any] /* [F */ ):string;
	static toString( arg0:[any] /* [I */ ):string;
	static toString( arg0:[any] /* [J */ ):string;
	static toString( arg0:[any] /* [Ljava.lang.Object; */ ):string;
	static toString( arg0:[any] /* [S */ ):string;
	static toString( arg0:[any] /* [Z */ ):string;
	static toString( arg0:bytearray ):string;
	static toString( arg0:chararray ):string;
	toString(  ):string;

} // end Arrays

} // end namespace java.util
declare namespace java.util {

class HashMap<K, V>/* extends AbstractMap<K, V>HashMap<K, V> implements Map<K, V>, java.lang.Cloneable, java.io.Serializable*/ {

	empty:boolean;
	afterNodeAccess( arg0:any /* java.util.HashMap$Node */ ):void;
	afterNodeInsertion( arg0:boolean ):void;
	afterNodeRemoval( arg0:any /* java.util.HashMap$Node */ ):void;
	capacity(  ):int;
	clear(  ):void;
	clone(  ):any /* java.lang.Object */;
	compute( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.BiFunction */ ):any /* java.lang.Object */;
	computeIfAbsent( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.Function */ ):any /* java.lang.Object */;
	computeIfPresent( arg0:any /* java.lang.Object */, arg1:any /* java.util.function.BiFunction */ ):any /* java.lang.Object */;
	containsKey( arg0:any /* java.lang.Object */ ):boolean;
	containsValue( arg0:any /* java.lang.Object */ ):boolean;
	entrySet(  ):Set<any>;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:any /* java.util.function.BiConsumer */ ):void;
	get( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	getNode( arg0:int, arg1:any /* java.lang.Object */ ):any /* java.util.HashMap$Node */;
	getOrDefault( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	hashCode(  ):int;
	internalWriteEntries( arg0:any /* java.io.ObjectOutputStream */ ):void;
	keySet(  ):Set<any>;
	loadFactor(  ):float;
	merge( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */, arg2:any /* java.util.function.BiFunction */ ):any /* java.lang.Object */;
	newNode( arg0:int, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */, arg3:any /* java.util.HashMap$Node */ ):any /* java.util.HashMap$Node */;
	newTreeNode( arg0:int, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */, arg3:any /* java.util.HashMap$Node */ ):any /* java.util.HashMap$TreeNode */;
	put( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	putAll( arg0:Map<K, V> ):void;
	putIfAbsent( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	putMapEntries( arg0:Map<K, V>, arg1:boolean ):void;
	putVal( arg0:int, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */, arg3:boolean, arg4:boolean ):any /* java.lang.Object */;
	readObject( arg0:any /* java.io.ObjectInputStream */ ):void;
	reinitialize(  ):void;
	remove( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	remove( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):boolean;
	removeNode( arg0:int, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */, arg3:boolean, arg4:boolean ):any /* java.util.HashMap$Node */;
	replace( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	replace( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */ ):boolean;
	replaceAll( arg0:any /* java.util.function.BiFunction */ ):void;
	replacementNode( arg0:any /* java.util.HashMap$Node */, arg1:any /* java.util.HashMap$Node */ ):any /* java.util.HashMap$Node */;
	replacementTreeNode( arg0:any /* java.util.HashMap$Node */, arg1:any /* java.util.HashMap$Node */ ):any /* java.util.HashMap$TreeNode */;
	resize(  ):[any] /* [Ljava.util.HashMap$Node; */;
	size(  ):int;
	static comparableClassFor<T>( arg0:any /* java.lang.Object */ ):java.lang.Class<any>;
	static compareComparables( arg0:java.lang.Class<any>, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */ ):int;
	static hash( arg0:any /* java.lang.Object */ ):int;
	static tableSizeFor( arg0:int ):int;
	toString(  ):string;
	treeifyBin( arg0:[any] /* [Ljava.util.HashMap$Node; */, arg1:int ):void;
	values(  ):Collection<any>;
	writeObject( arg0:any /* java.io.ObjectOutputStream */ ):void;

} // end HashMap<K, V>
export module HashMap {

class SimpleImmutableEntry<K, V>/* extends java.lang.ObjectSimpleImmutableEntry<K, V> implements Entry<K, V>, java.io.Serializable*/ {

	key:any /* java.lang.Object */;
	value:any /* java.lang.Object */;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	setValue( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	toString(  ):string;

} // end SimpleImmutableEntry<K, V>
class SimpleEntry<K, V>/* extends java.lang.ObjectSimpleEntry<K, V> implements Entry<K, V>, java.io.Serializable*/ {

	key:any /* java.lang.Object */;
	value:any /* java.lang.Object */;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	setValue( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	toString(  ):string;

} // end SimpleEntry<K, V>

} // end module HashMap

} // end namespace java.util
declare namespace java.util {

class HashSet<E>/* extends AbstractSet<E>HashSet<E> implements Set<E>, java.lang.Cloneable, java.io.Serializable*/ {

	empty:boolean;
	add( arg0:any /* java.lang.Object */ ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	clone(  ):any /* java.lang.Object */;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	hashCode(  ):int;
	iterator(  ):Iterator<E>;
	parallelStream(  ):java.util.stream.Stream<E>;
	readObject( arg0:any /* java.io.ObjectInputStream */ ):void;
	remove( arg0:any /* java.lang.Object */ ):boolean;
	removeAll( arg0:Collection<E> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	retainAll( arg0:Collection<E> ):boolean;
	size(  ):int;
	spliterator(  ):any /* java.util.Spliterator */;
	stream(  ):java.util.stream.Stream<E>;
	toArray(  ):[any] /* [Ljava.lang.Object; */;
	toArray( arg0:[any] /* [Ljava.lang.Object; */ ):[any] /* [Ljava.lang.Object; */;
	toString(  ):string;
	writeObject( arg0:any /* java.io.ObjectOutputStream */ ):void;

} // end HashSet<E>

} // end namespace java.util
declare namespace java.util {

class ArrayList<E>/* extends AbstractList<E>ArrayList<E> implements List<E>, RandomAccess, java.lang.Cloneable, java.io.Serializable*/ {

	empty:boolean;
	add( arg0:any /* java.lang.Object */ ):boolean;
	add( arg0:int, arg1:any /* java.lang.Object */ ):void;
	addAll( arg0:Collection<E> ):boolean;
	addAll( arg0:int, arg1:Collection<E> ):boolean;
	batchRemove( arg0:Collection<E>, arg1:boolean ):boolean;
	clear(  ):void;
	clone(  ):any /* java.lang.Object */;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	elementData( arg0:int ):any /* java.lang.Object */;
	ensureCapacity( arg0:int ):void;
	ensureCapacityInternal( arg0:int ):void;
	ensureExplicitCapacity( arg0:int ):void;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	fastRemove( arg0:int ):void;
	forEach( arg0:Consumer<E> ):void;
	get( arg0:int ):any /* java.lang.Object */;
	grow( arg0:int ):void;
	hashCode(  ):int;
	indexOf( arg0:any /* java.lang.Object */ ):int;
	iterator(  ):Iterator<E>;
	lastIndexOf( arg0:any /* java.lang.Object */ ):int;
	listIterator(  ):any /* java.util.ListIterator */;
	listIterator( arg0:int ):any /* java.util.ListIterator */;
	outOfBoundsMsg( arg0:int ):string;
	parallelStream(  ):java.util.stream.Stream<E>;
	rangeCheck( arg0:int ):void;
	rangeCheckForAdd( arg0:int ):void;
	readObject( arg0:any /* java.io.ObjectInputStream */ ):void;
	remove( arg0:any /* java.lang.Object */ ):boolean;
	remove( arg0:int ):any /* java.lang.Object */;
	removeAll( arg0:Collection<E> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	removeRange( arg0:int, arg1:int ):void;
	replaceAll( arg0:UnaryOperator<E> ):void;
	retainAll( arg0:Collection<E> ):boolean;
	set( arg0:int, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	size(  ):int;
	sort( arg0:Comparator<E> ):void;
	spliterator(  ):any /* java.util.Spliterator */;
	static hugeCapacity( arg0:int ):int;
	static subListRangeCheck( arg0:int, arg1:int, arg2:int ):void;
	stream(  ):java.util.stream.Stream<E>;
	subList( arg0:int, arg1:int ):List<E>;
	toArray(  ):[any] /* [Ljava.lang.Object; */;
	toArray( arg0:[any] /* [Ljava.lang.Object; */ ):[any] /* [Ljava.lang.Object; */;
	toString(  ):string;
	trimToSize(  ):void;
	writeObject( arg0:any /* java.io.ObjectOutputStream */ ):void;

} // end ArrayList<E>

} // end namespace java.util
declare namespace java.util {

class Optional<T>/* extends java.lang.Object*/ {

	present:boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	filter( arg0:Predicate<T> ):Optional<T>;
	flatMap( arg0:any /* java.util.function.Function */ ):Optional<T>;
	get(  ):any /* java.lang.Object */;
	hashCode(  ):int;
	ifPresent( arg0:Consumer<T> ):void;
	map( arg0:any /* java.util.function.Function */ ):Optional<T>;
	orElse( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	orElseGet( arg0:Supplier<T> ):any /* java.lang.Object */;
	orElseThrow( arg0:Supplier<T> ):any /* java.lang.Object */;
	static empty<T>(  ):Optional<T>;
	static of<T>( arg0:any /* java.lang.Object */ ):Optional<T>;
	static ofNullable<T>( arg0:any /* java.lang.Object */ ):Optional<T>;
	toString(  ):string;

} // end Optional<T>

} // end namespace java.util
declare namespace java.nio.file {

class Files/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	static asUncheckedRunnable( arg0:java.io.Closeable ):Runnable;
	static copy( arg0:Path, arg1:Path, arg2:[any] /* [Ljava.nio.file.CopyOption; */ ):Path;
	static copy( arg0:Path, arg1:any /* java.io.OutputStream */ ):long;
	static copy( arg0:any /* java.io.InputStream */, arg1:Path, arg2:[any] /* [Ljava.nio.file.CopyOption; */ ):long;
	static copy( arg0:any /* java.io.InputStream */, arg1:any /* java.io.OutputStream */ ):long;
	static createAndCheckIsDirectory( arg0:Path, arg1:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):void;
	static createDirectories( arg0:Path, arg1:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createDirectory( arg0:Path, arg1:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createFile( arg0:Path, arg1:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createLink( arg0:Path, arg1:Path ):Path;
	static createSymbolicLink( arg0:Path, arg1:Path, arg2:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createTempDirectory( arg0:Path, arg1:string, arg2:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createTempDirectory( arg0:string, arg1:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createTempFile( arg0:Path, arg1:string, arg2:string, arg3:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static createTempFile( arg0:string, arg1:string, arg2:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):Path;
	static delete( arg0:Path ):void;
	static deleteIfExists( arg0:Path ):boolean;
	static exists( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):boolean;
	static find<T>( arg0:Path, arg1:int, arg2:any /* java.util.function.BiPredicate */, arg3:[any] /* [Ljava.nio.file.FileVisitOption; */ ):java.util.stream.Stream<any>;
	static followLinks( arg0:[any] /* [Ljava.nio.file.LinkOption; */ ):boolean;
	static getAttribute( arg0:Path, arg1:string, arg2:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.lang.Object */;
	static getFileAttributeView( arg0:Path, arg1:java.lang.Class<any>, arg2:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.FileAttributeView */;
	static getFileStore( arg0:Path ):any /* java.nio.file.FileStore */;
	static getLastModifiedTime( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.FileTime */;
	static getOwner( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.UserPrincipal */;
	static getPosixFilePermissions<E>( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):java.util.Set<any>;
	static isAccessible( arg0:Path, arg1:[any] /* [Ljava.nio.file.AccessMode; */ ):boolean;
	static isDirectory( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):boolean;
	static isExecutable( arg0:Path ):boolean;
	static isHidden( arg0:Path ):boolean;
	static isReadable( arg0:Path ):boolean;
	static isRegularFile( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):boolean;
	static isSameFile( arg0:Path, arg1:Path ):boolean;
	static isSymbolicLink( arg0:Path ):boolean;
	static isWritable( arg0:Path ):boolean;
	static lines<T>( arg0:Path ):java.util.stream.Stream<any>;
	static lines<T>( arg0:Path, arg1:any /* java.nio.charset.Charset */ ):java.util.stream.Stream<any>;
	static list<T>( arg0:Path ):java.util.stream.Stream<any>;
	static move( arg0:Path, arg1:Path, arg2:[any] /* [Ljava.nio.file.CopyOption; */ ):Path;
	static newBufferedReader( arg0:Path ):any /* java.io.BufferedReader */;
	static newBufferedReader( arg0:Path, arg1:any /* java.nio.charset.Charset */ ):any /* java.io.BufferedReader */;
	static newBufferedWriter( arg0:Path, arg1:[any] /* [Ljava.nio.file.OpenOption; */ ):any /* java.io.BufferedWriter */;
	static newBufferedWriter( arg0:Path, arg1:any /* java.nio.charset.Charset */, arg2:[any] /* [Ljava.nio.file.OpenOption; */ ):any /* java.io.BufferedWriter */;
	static newByteChannel( arg0:Path, arg1:[any] /* [Ljava.nio.file.OpenOption; */ ):any /* java.nio.channels.SeekableByteChannel */;
	static newByteChannel( arg0:Path, arg1:java.util.Set<any>, arg2:[any] /* [Ljava.nio.file.attribute.FileAttribute; */ ):any /* java.nio.channels.SeekableByteChannel */;
	static newDirectoryStream<T>( arg0:Path ):any /* java.nio.file.DirectoryStream */;
	static newDirectoryStream<T>( arg0:Path, arg1:any /* java.nio.file.DirectoryStream$Filter */ ):any /* java.nio.file.DirectoryStream */;
	static newDirectoryStream<T>( arg0:Path, arg1:string ):any /* java.nio.file.DirectoryStream */;
	static newInputStream( arg0:Path, arg1:[any] /* [Ljava.nio.file.OpenOption; */ ):any /* java.io.InputStream */;
	static newOutputStream( arg0:Path, arg1:[any] /* [Ljava.nio.file.OpenOption; */ ):any /* java.io.OutputStream */;
	static notExists( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):boolean;
	static probeContentType( arg0:Path ):string;
	static provider( arg0:Path ):any /* java.nio.file.spi.FileSystemProvider */;
	static read( arg0:any /* java.io.InputStream */, arg1:int ):bytearray;
	static readAllBytes( arg0:Path ):bytearray;
	static readAllLines<E>( arg0:Path ):java.util.List<any>;
	static readAllLines<E>( arg0:Path, arg1:any /* java.nio.charset.Charset */ ):java.util.List<any>;
	static readAttributes( arg0:Path, arg1:java.lang.Class<any>, arg2:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.BasicFileAttributes */;
	static readAttributes<K,V>( arg0:Path, arg1:string, arg2:[any] /* [Ljava.nio.file.LinkOption; */ ):java.util.Map<any, any>;
	static readSymbolicLink( arg0:Path ):Path;
	static setAttribute( arg0:Path, arg1:string, arg2:any /* java.lang.Object */, arg3:[any] /* [Ljava.nio.file.LinkOption; */ ):Path;
	static setLastModifiedTime( arg0:Path, arg1:any /* java.nio.file.attribute.FileTime */ ):Path;
	static setOwner( arg0:Path, arg1:any /* java.nio.file.attribute.UserPrincipal */ ):Path;
	static setPosixFilePermissions( arg0:Path, arg1:java.util.Set<any> ):Path;
	static size( arg0:Path ):long;
	static walk<T>( arg0:Path, arg1:[any] /* [Ljava.nio.file.FileVisitOption; */ ):java.util.stream.Stream<any>;
	static walk<T>( arg0:Path, arg1:int, arg2:[any] /* [Ljava.nio.file.FileVisitOption; */ ):java.util.stream.Stream<any>;
	static walkFileTree( arg0:Path, arg1:any /* java.nio.file.FileVisitor */ ):Path;
	static walkFileTree( arg0:Path, arg1:java.util.Set<any>, arg2:int, arg3:any /* java.nio.file.FileVisitor */ ):Path;
	static write( arg0:Path, arg1:bytearray, arg2:[any] /* [Ljava.nio.file.OpenOption; */ ):Path;
	static write( arg0:Path, arg1:java.lang.Iterable<any>, arg2:[any] /* [Ljava.nio.file.OpenOption; */ ):Path;
	static write( arg0:Path, arg1:java.lang.Iterable<any>, arg2:any /* java.nio.charset.Charset */, arg3:[any] /* [Ljava.nio.file.OpenOption; */ ):Path;
	toString(  ):string;

} // end Files

} // end namespace java.nio.file
declare namespace java.nio.file {

interface Path/*Path extends java.lang.Comparable<any>, java.lang.Iterable<any>, Watchable*/ {

	absolute?:boolean;
	fileName?:Path;
	fileSystem?:any /* java.nio.file.FileSystem */;
	name?:any;
	nameCount?:int;
	parent?:Path;
	root?:Path;
	compareTo( arg0:Path ):int;
	endsWith( arg0:Path ):boolean;
	endsWith( arg0:string ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<any> ):void;
	getName( arg0:int ):Path;
	hashCode(  ):int;
	iterator(  ):java.util.Iterator<any>;
	normalize(  ):Path;
	register( arg0:any /* java.nio.file.WatchService */, arg1:[any] /* [Ljava.nio.file.WatchEvent$Kind; */ ):any /* java.nio.file.WatchKey */;
	register( arg0:any /* java.nio.file.WatchService */, arg1:[any] /* [Ljava.nio.file.WatchEvent$Kind; */, arg2:[any] /* [Ljava.nio.file.WatchEvent$Modifier; */ ):any /* java.nio.file.WatchKey */;
	relativize( arg0:Path ):Path;
	resolve( arg0:Path ):Path;
	resolve( arg0:string ):Path;
	resolveSibling( arg0:Path ):Path;
	resolveSibling( arg0:string ):Path;
	spliterator(  ):any /* java.util.Spliterator */;
	startsWith( arg0:Path ):boolean;
	startsWith( arg0:string ):boolean;
	subpath( arg0:int, arg1:int ):Path;
	toAbsolutePath(  ):Path;
	toFile(  ):any /* java.io.File */;
	toRealPath( arg0:[any] /* [Ljava.nio.file.LinkOption; */ ):Path;
	toString(  ):string;
	toUri(  ):any /* java.net.URI */;

} // end Path

} // end namespace java.nio.file
declare namespace java.nio.file {

class Paths/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	static get( arg0:any /* java.net.URI */ ):Path;
	static get( arg0:string, arg1:[any] /* [Ljava.lang.String; */ ):Path;
	toString(  ):string;

} // end Paths

} // end namespace java.nio.file
declare namespace java.nio.file {

/* enum */class AccessMode/* extends java.lang.Enum<any>*/ {

	static READ:AccessMode;
	static WRITE:AccessMode;
	static EXECUTE:AccessMode;

	declaringClass:java.lang.Class<any>;
	compareTo( arg0:any /* java.lang.Enum */ ):int;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	hashCode(  ):int;
	name(  ):string;
	ordinal(  ):int;
	static valueOf( arg0:string ):AccessMode;
	static valueOf<E>( arg0:java.lang.Class<any>, arg1:string ):any /* java.lang.Enum */;
	static values(  ):[any] /* [Ljava.nio.file.AccessMode; */;
	toString(  ):string;

} // end AccessMode

} // end namespace java.nio.file
