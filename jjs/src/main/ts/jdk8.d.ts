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
declare namespace java.net {

class URI/* extends java.lang.ObjectURI implements java.lang.Comparable<any>, java.io.Serializable*/ {

	compareTo( arg0:URI ):int;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	getAuthority(  ):string;
	getFragment(  ):string;
	getHost(  ):string;
	getPath(  ):string;
	getPort(  ):int;
	getQuery(  ):string;
	getRawAuthority(  ):string;
	getRawFragment(  ):string;
	getRawPath(  ):string;
	getRawQuery(  ):string;
	getRawSchemeSpecificPart(  ):string;
	getRawUserInfo(  ):string;
	getScheme(  ):string;
	getSchemeSpecificPart(  ):string;
	getUserInfo(  ):string;
	isAbsolute(  ):boolean;
	isOpaque(  ):boolean;
	normalize(  ):URI;
	parseServerAuthority(  ):URI;
	relativize( arg0:URI ):URI;
	resolve( arg0:URI ):URI;
	resolve( arg0:string ):URI;
	toASCIIString(  ):string;
	toString(  ):string;
	toURL(  ):URL;

} // end URI

} // end namespace java.net
declare namespace java.lang {

class System/* extends Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	static arraycopy( arg0:any /* java.lang.Object */, arg1:int, arg2:any /* java.lang.Object */, arg3:int, arg4:int ):void;
	static clearProperty( arg0:string ):string;
	static console(  ):any /* java.io.Console */;
	static currentTimeMillis(  ):long;
	static exit( arg0:int ):void;
	static gc(  ):void;
	static getProperties(  ):any /* java.util.Properties */;
	static getProperty( arg0:string ):string;
	static getProperty( arg0:string, arg1:string ):string;
	static getSecurityManager(  ):any /* java.lang.SecurityManager */;
	static getenv( arg0:string ):string;
	static getenv<K,V>(  ):java.util.Map<any, any>;
	static identityHashCode( arg0:any /* java.lang.Object */ ):int;
	static inheritedChannel(  ):any /* java.nio.channels.Channel */;
	static lineSeparator(  ):string;
	static load( arg0:string ):void;
	static loadLibrary( arg0:string ):void;
	static mapLibraryName( arg0:string ):string;
	static nanoTime(  ):long;
	static runFinalization(  ):void;
	static runFinalizersOnExit( arg0:boolean ):void;
	static setErr( arg0:any /* java.io.PrintStream */ ):void;
	static setIn( arg0:any /* java.io.InputStream */ ):void;
	static setOut( arg0:any /* java.io.PrintStream */ ):void;
	static setProperties( arg0:any /* java.util.Properties */ ):void;
	static setProperty( arg0:string, arg1:string ):string;
	static setSecurityManager( arg0:any /* java.lang.SecurityManager */ ):void;
	toString(  ):string;

} // end System

} // end namespace java.lang
declare namespace java.net {

class URL/* extends java.lang.ObjectURL implements java.io.Serializable*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	getAuthority(  ):string;
	getContent(  ):any /* java.lang.Object */;
	getContent( arg0:[any] /* [Ljava.lang.Class; */ ):any /* java.lang.Object */;
	getDefaultPort(  ):int;
	getFile(  ):string;
	getHost(  ):string;
	getPath(  ):string;
	getPort(  ):int;
	getProtocol(  ):string;
	getQuery(  ):string;
	getRef(  ):string;
	getUserInfo(  ):string;
	openConnection(  ):any /* java.net.URLConnection */;
	openConnection( arg0:any /* java.net.Proxy */ ):any /* java.net.URLConnection */;
	openStream(  ):any /* java.io.InputStream */;
	sameFile( arg0:URL ):boolean;
	static setURLStreamHandlerFactory( arg0:any /* java.net.URLStreamHandlerFactory */ ):void;
	toExternalForm(  ):string;
	toString(  ):string;
	toURI(  ):URI;

} // end URL

} // end namespace java.net
declare namespace java.nio.file {

class Paths/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	static get( arg0:java.net.URI ):Path;
	static get( arg0:string, arg1:[any] /* [Ljava.lang.String; */ ):Path;
	toString(  ):string;

} // end Paths

} // end namespace java.nio.file
declare namespace java.nio.file {

interface Path/*Path extends java.lang.Comparable<any>, java.lang.Iterable<any>, Watchable*/ {

	compareTo( arg0:Path ):int;
	endsWith( arg0:Path ):boolean;
	endsWith( arg0:string ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<any> ):void;
	getFileName(  ):Path;
	getFileSystem(  ):any /* java.nio.file.FileSystem */;
	getName( arg0:int ):Path;
	getNameCount(  ):int;
	getParent(  ):Path;
	getRoot(  ):Path;
	isAbsolute(  ):boolean;
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
	toUri(  ):java.net.URI;

} // end Path

} // end namespace java.nio.file
declare namespace java.lang {

interface Iterable<T> {

	forEach( arg0:Consumer<T> ):void;
	iterator(  ):java.util.Iterator<T>;
	spliterator(  ):any /* java.util.Spliterator */;

} // end Iterable<T>

} // end namespace java.lang
declare namespace java.util {

class HashMap<K, V>/* extends AbstractMap<K, V>HashMap<K, V> implements Map<K, V>, java.lang.Cloneable, java.io.Serializable*/ {

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
	getOrDefault( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):any /* java.lang.Object */;
	isEmpty(  ):boolean;
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
	toString(  ):string;
	values(  ):Collection<any>;

} // end HashMap<K, V>
export module HashMap {

class SimpleImmutableEntry<K, V>/* extends java.lang.ObjectSimpleImmutableEntry<K, V> implements Entry<K, V>, java.io.Serializable*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	getKey(  ):any /* java.lang.Object */;
	getValue(  ):any /* java.lang.Object */;
	setValue( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	toString(  ):string;

} // end SimpleImmutableEntry<K, V>
class SimpleEntry<K, V>/* extends java.lang.ObjectSimpleEntry<K, V> implements Entry<K, V>, java.io.Serializable*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	getKey(  ):any /* java.lang.Object */;
	getValue(  ):any /* java.lang.Object */;
	setValue( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;
	toString(  ):string;

} // end SimpleEntry<K, V>

} // end module HashMap

} // end namespace java.util
declare namespace java.util {

interface Set<E>/*Set<E> extends Collection<E>*/ {

	add( arg0:any /* java.lang.Object */ ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	isEmpty(  ):boolean;
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

interface Iterator<E> {

	forEachRemaining( arg0:Consumer<E> ):void;
	hasNext(  ):boolean;
	next(  ):any /* java.lang.Object */;
	remove(  ):void;

} // end Iterator<E>

} // end namespace java.util
declare namespace java.util {

interface List<E>/*List<E> extends Collection<E>*/ {

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
	indexOf( arg0:any /* java.lang.Object */ ):int;
	isEmpty(  ):boolean;
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

class HashSet<E>/* extends AbstractSet<E>HashSet<E> implements Set<E>, java.lang.Cloneable, java.io.Serializable*/ {

	add( arg0:any /* java.lang.Object */ ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	clone(  ):any /* java.lang.Object */;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	isEmpty(  ):boolean;
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
	toString(  ):string;

} // end HashSet<E>

} // end namespace java.util
declare namespace java.util.stream {

interface Stream<T>/*Stream<T> extends BaseStream<T, any>*/ {

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

class Arrays/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	toString(  ):string;

} // end Arrays

} // end namespace java.util
declare namespace java.lang {

class String/* extends ObjectString implements java.io.Serializable, Comparable<any>, CharSequence*/ {

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
	getBytes(  ):bytearray;
	getBytes( arg0:any /* java.nio.charset.Charset */ ):bytearray;
	getBytes( arg0:int, arg1:int, arg2:bytearray, arg3:int ):void;
	getBytes( arg0:string ):bytearray;
	getChars( arg0:int, arg1:int, arg2:chararray, arg3:int ):void;
	indexOf( arg0:int ):int;
	indexOf( arg0:int, arg1:int ):int;
	indexOf( arg0:string ):int;
	indexOf( arg0:string, arg1:int ):int;
	intern(  ):string;
	isEmpty(  ):boolean;
	lastIndexOf( arg0:int ):int;
	lastIndexOf( arg0:int, arg1:int ):int;
	lastIndexOf( arg0:string ):int;
	lastIndexOf( arg0:string, arg1:int ):int;
	length(  ):int;
	matches( arg0:string ):boolean;
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
declare namespace java.util {

class Collections/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	toString(  ):string;

} // end Collections

} // end namespace java.util
declare namespace java.util {

class Optional<T>/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	filter( arg0:Predicate<T> ):Optional<T>;
	flatMap( arg0:any /* java.util.function.Function */ ):Optional<T>;
	get(  ):any /* java.lang.Object */;
	ifPresent( arg0:Consumer<T> ):void;
	isPresent(  ):boolean;
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
declare namespace java.nio.file {

/* enum */class AccessMode/* extends java.lang.Enum<any>*/ {

	static READ:AccessMode;
	static WRITE:AccessMode;
	static EXECUTE:AccessMode;

	compareTo( arg0:any /* java.lang.Enum */ ):int;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	getDeclaringClass(  ):java.lang.Class<any>;
	name(  ):string;
	ordinal(  ):int;
	static valueOf( arg0:string ):AccessMode;
	static valueOf<E>( arg0:java.lang.Class<any>, arg1:string ):any /* java.lang.Enum */;
	static values(  ):[any] /* [Ljava.nio.file.AccessMode; */;
	toString(  ):string;

} // end AccessMode

} // end namespace java.nio.file
declare namespace java.util.stream {

interface BaseStream<T, S>/*BaseStream<T, S> extends java.lang.AutoCloseable*/ {

	close(  ):void;
	isParallel(  ):boolean;
	iterator(  ):java.util.Iterator<any>;
	onClose( arg0:Runnable ):BaseStream<T, S>;
	parallel(  ):BaseStream<T, S>;
	sequential(  ):BaseStream<T, S>;
	spliterator(  ):any /* java.util.Spliterator */;
	unordered(  ):BaseStream<T, S>;

} // end BaseStream<T, S>

} // end namespace java.util.stream
declare namespace java.util {

interface Collection<E>/*Collection<E> extends java.lang.Iterable<E>*/ {

	add( arg0:any /* java.lang.Object */ ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	isEmpty(  ):boolean;
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
	isEmpty(  ):boolean;
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

	// static comparingByKey<T>(  ):Comparator<any>;
	// static comparingByKey<T>( arg0:Comparator<any> ):Comparator<any>;
	// static comparingByValue<T>(  ):Comparator<any>;
	// static comparingByValue<T>( arg0:Comparator<any> ):Comparator<any>;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	getKey(  ):any /* java.lang.Object */;
	getValue(  ):any /* java.lang.Object */;
	setValue( arg0:any /* java.lang.Object */ ):any /* java.lang.Object */;

} // end Entry<K, V>

} // end module Map

} // end namespace java.util
declare namespace java.nio.file {

class Files/* extends java.lang.Object*/ {

	equals( arg0:any /* java.lang.Object */ ):boolean;
	static copy( arg0:Path, arg1:Path, arg2:[any] /* [Ljava.nio.file.CopyOption; */ ):Path;
	static copy( arg0:Path, arg1:any /* java.io.OutputStream */ ):long;
	static copy( arg0:any /* java.io.InputStream */, arg1:Path, arg2:[any] /* [Ljava.nio.file.CopyOption; */ ):long;
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
	static getAttribute( arg0:Path, arg1:string, arg2:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.lang.Object */;
	static getFileAttributeView( arg0:Path, arg1:java.lang.Class<any>, arg2:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.FileAttributeView */;
	static getFileStore( arg0:Path ):any /* java.nio.file.FileStore */;
	static getLastModifiedTime( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.FileTime */;
	static getOwner( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):any /* java.nio.file.attribute.UserPrincipal */;
	static getPosixFilePermissions<E>( arg0:Path, arg1:[any] /* [Ljava.nio.file.LinkOption; */ ):java.util.Set<any>;
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
declare namespace java.util {

class ArrayList<E>/* extends AbstractList<E>ArrayList<E> implements List<E>, RandomAccess, java.lang.Cloneable, java.io.Serializable*/ {

	add( arg0:any /* java.lang.Object */ ):boolean;
	add( arg0:int, arg1:any /* java.lang.Object */ ):void;
	addAll( arg0:Collection<E> ):boolean;
	addAll( arg0:int, arg1:Collection<E> ):boolean;
	clear(  ):void;
	clone(  ):any /* java.lang.Object */;
	contains( arg0:any /* java.lang.Object */ ):boolean;
	containsAll( arg0:Collection<E> ):boolean;
	ensureCapacity( arg0:int ):void;
	equals( arg0:any /* java.lang.Object */ ):boolean;
	forEach( arg0:Consumer<E> ):void;
	get( arg0:int ):any /* java.lang.Object */;
	indexOf( arg0:any /* java.lang.Object */ ):int;
	isEmpty(  ):boolean;
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
	toString(  ):string;
	trimToSize(  ):void;

} // end ArrayList<E>

} // end namespace java.util
