/*
 * Project: java2typescript - https://github.com/bsorrentino/java2typescript
 *
 * Author: bsorrentino 
 *
 * TYPESCRIPT DEFINITIONS
 *
 */

type int    = number;
type long   = number;
type float	= number;
type double = number;
type byte   = number;
type char   = string;

type chararray = [byte];
type bytearray = [char];

declare namespace java.lang {

	interface Class<T> {}
	interface AutoCloseable {}
	interface Cloneable {}

	type Object = any;
}

declare namespace java.util {

	interface RandomAccess {}
}

declare namespace java.io {

	interface Closeable {}
	interface Serializable {}
}

//
// Rhino
//

declare const Packages:any;

//
// Nashorn
//

declare function print( ...args: any[] ):void

declare function load( module:string ):void

declare namespace Java {

  export function type<T>( t:string):T;

  export function from<T>( list:java.util.List<T> ):Array<T> ;
  
}
interface BinaryOperator<T>/*java.util.function.BinaryOperator extends BiFunction<T, any, any>*/ {

	<R,U>( arg0:T, arg1:U ):R;
	// static maxBy<T>( arg0:any /*java.util.Comparator*/ ):BinaryOperator<T>;
	// static minBy<T>( arg0:any /*java.util.Comparator*/ ):BinaryOperator<T>;
	andThen?<R,U,V>( arg0:Func<R, V> ):BiFunction<T, U, V>;

} // end BinaryOperator
declare namespace java.util.concurrent {

interface Callable<V> {

	(  ):V;

} // end Callable

} // end namespace java.util.concurrent
declare namespace java.util.concurrent {

interface Executor {

	( arg0:java.lang.Runnable ):void;

} // end Executor

} // end namespace java.util.concurrent
declare namespace java.lang {

class String/* extends Object implements java.io.Serializable, Comparable<any>, CharSequence*/ {

	charAt( arg0:int ):any /*char*/;
	chars(  ):any /*java.util.stream.IntStream*/;
	codePointAt( arg0:int ):int;
	codePointBefore( arg0:int ):int;
	codePointCount( arg0:int, arg1:int ):int;
	codePoints(  ):any /*java.util.stream.IntStream*/;
	compareTo( arg0:string ):int;
	compareToIgnoreCase( arg0:string ):int;
	concat( arg0:string ):string;
	contains( arg0:any /*java.lang.CharSequence*/ ):boolean;
	contentEquals( arg0:any /*java.lang.CharSequence*/ ):boolean;
	contentEquals( arg0:any /*java.lang.StringBuffer*/ ):boolean;
	endsWith( arg0:string ):boolean;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	equalsIgnoreCase( arg0:string ):boolean;
	getBytes(  ):bytearray;
	getBytes( arg0:any /*java.nio.charset.Charset*/ ):bytearray;
	getBytes( arg0:int, arg1:int, arg2:bytearray, arg3:int ):void;
	getBytes( arg0:string ):bytearray;
	getChars( arg0:int, arg1:int, arg2:chararray, arg3:int ):void;
	indexOf( arg0:int ):int;
	indexOf( arg0:int, arg1:int ):int;
	indexOf( arg0:string ):int;
	indexOf( arg0:string, arg1:int ):int;
	intern(  ):string;
	isBlank(  ):boolean;
	isEmpty(  ):boolean;
	lastIndexOf( arg0:int ):int;
	lastIndexOf( arg0:int, arg1:int ):int;
	lastIndexOf( arg0:string ):int;
	lastIndexOf( arg0:string, arg1:int ):int;
	length(  ):int;
	lines(  ):java.util.stream.Stream<string>;
	matches( arg0:string ):boolean;
	offsetByCodePoints( arg0:int, arg1:int ):int;
	regionMatches( arg0:boolean, arg1:int, arg2:string, arg3:int, arg4:int ):boolean;
	regionMatches( arg0:int, arg1:string, arg2:int, arg3:int ):boolean;
	repeat( arg0:int ):string;
	replace( arg0:any /*char*/, arg1:any /*char*/ ):string;
	replace( arg0:any /*java.lang.CharSequence*/, arg1:any /*java.lang.CharSequence*/ ):string;
	replaceAll( arg0:string, arg1:string ):string;
	replaceFirst( arg0:string, arg1:string ):string;
	split( arg0:string ):[string];
	split( arg0:string, arg1:int ):[string];
	startsWith( arg0:string ):boolean;
	startsWith( arg0:string, arg1:int ):boolean;
	strip(  ):string;
	stripLeading(  ):string;
	stripTrailing(  ):string;
	subSequence( arg0:int, arg1:int ):any /*java.lang.CharSequence*/;
	substring( arg0:int ):string;
	substring( arg0:int, arg1:int ):string;
	toCharArray(  ):chararray;
	toLowerCase(  ):string;
	toLowerCase( arg0:any /*java.util.Locale*/ ):string;
	toString(  ):string;
	toUpperCase(  ):string;
	toUpperCase( arg0:any /*java.util.Locale*/ ):string;
	trim(  ):string;

} // end String

} // end namespace java.lang
declare namespace java.util.stream {

interface Stream<T>/* extends BaseStream<T, any>*/ {

	allMatch( arg0:Predicate<T> ):boolean;
	anyMatch( arg0:Predicate<T> ):boolean;
	close(  ):void;
	collect<A,R>( arg0:Collector<T, A, R> ):R;
	collect<R>( arg0:Supplier<R>, arg1:BiConsumer<R, T>, arg2:BiConsumer<R, R> ):R;
	count(  ):long;
	distinct(  ):Stream<T>;
	dropWhile( arg0:Predicate<T> ):Stream<T>;
	filter( arg0:Predicate<T> ):Stream<T>;
	findAny(  ):java.util.Optional<T>;
	findFirst(  ):java.util.Optional<T>;
	flatMap<R>( arg0:Func<T, Stream<R>> ):Stream<R>;
	flatMapToDouble( arg0:Func<T, any /*java.util.stream.DoubleStream*/> ):any /*java.util.stream.DoubleStream*/;
	flatMapToInt( arg0:Func<T, any /*java.util.stream.IntStream*/> ):any /*java.util.stream.IntStream*/;
	flatMapToLong( arg0:Func<T, any /*java.util.stream.LongStream*/> ):any /*java.util.stream.LongStream*/;
	forEach( arg0:Consumer<T> ):void;
	forEachOrdered( arg0:Consumer<T> ):void;
	isParallel(  ):boolean;
	iterator(  ):java.util.Iterator<T>;
	limit( arg0:long ):Stream<T>;
	map<R>( arg0:Func<T, R> ):Stream<R>;
	mapToDouble( arg0:any /*java.util.function.ToDoubleFunction*/ ):any /*java.util.stream.DoubleStream*/;
	mapToInt( arg0:any /*java.util.function.ToIntFunction*/ ):any /*java.util.stream.IntStream*/;
	mapToLong( arg0:any /*java.util.function.ToLongFunction*/ ):any /*java.util.stream.LongStream*/;
	max( arg0:any /*java.util.Comparator*/ ):java.util.Optional<T>;
	min( arg0:any /*java.util.Comparator*/ ):java.util.Optional<T>;
	noneMatch( arg0:Predicate<T> ):boolean;
	onClose<S>( arg0:java.lang.Runnable ):S;
	parallel<S>(  ):S;
	peek( arg0:Consumer<T> ):Stream<T>;
	reduce( arg0:BinaryOperator<T> ):java.util.Optional<T>;
	reduce( arg0:T, arg1:BinaryOperator<T> ):T;
	reduce<U>( arg0:U, arg1:BiFunction<U, T, U>, arg2:BinaryOperator<U> ):U;
	sequential<S>(  ):S;
	skip( arg0:long ):Stream<T>;
	sorted(  ):Stream<T>;
	sorted( arg0:any /*java.util.Comparator*/ ):Stream<T>;
	spliterator(  ):any /*java.util.Spliterator*/;
	takeWhile( arg0:Predicate<T> ):Stream<T>;
	toArray(  ):[any /*java.lang.Object*/];
	toArray<A>( arg0:any /*java.util.function.IntFunction*/ ):[A];
	unordered<S>(  ):S;

} // end Stream

} // end namespace java.util.stream
interface BiConsumer<T, U>/*java.util.function.BiConsumer*/ {

	( arg0:T, arg1:U ):void;
	andThen?( arg0:BiConsumer<T, U> ):BiConsumer<T, U>;

} // end BiConsumer
interface Supplier<T>/*java.util.function.Supplier*/ {

	(  ):T;

} // end Supplier
declare namespace java.util {

interface Iterator<E> {

	forEachRemaining( arg0:Consumer<E> ):void;
	hasNext(  ):boolean;
	next(  ):E;
	remove(  ):void;

} // end Iterator

} // end namespace java.util
declare namespace java.util {

interface Map$Entry<K, V> {

	// static comparingByKey(  ):any /*java.util.Comparator*/;
	// static comparingByKey( arg0:any /*java.util.Comparator*/ ):any /*java.util.Comparator*/;
	// static comparingByValue(  ):any /*java.util.Comparator*/;
	// static comparingByValue( arg0:any /*java.util.Comparator*/ ):any /*java.util.Comparator*/;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	getKey(  ):K;
	getValue(  ):V;
	setValue( arg0:V ):V;

} // end Map$Entry

} // end namespace java.util
declare namespace java.net {

class URI/* extends java.lang.Object implements java.lang.Comparable<any>, java.io.Serializable*/ {

	compareTo( arg0:URI ):int;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
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
interface UnaryOperator<T>/*java.util.function.UnaryOperator extends Function<T, any>*/ {

	<R>( arg0:T ):R;
	// static identity<T>(  ):UnaryOperator<T>;
	andThen?<R,V>( arg0:Func<R, V> ):Func<T, V>;
	compose?<R,V>( arg0:Func<V, T> ):Func<V, R>;

} // end UnaryOperator
declare namespace java.util.concurrent {

class CompletableFuture<T>/* extends java.lang.Object implements Future<T>, CompletionStage<T>*/ {

	acceptEither( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:Consumer<T> ):CompletableFuture<void>;
	acceptEitherAsync( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:Consumer<T> ):CompletableFuture<void>;
	acceptEitherAsync( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:Consumer<T>, arg2:Executor ):CompletableFuture<void>;
	applyToEither<U>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:Func<T, U> ):CompletableFuture<U>;
	applyToEitherAsync<U>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:Func<T, U> ):CompletableFuture<U>;
	applyToEitherAsync<U>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:Func<T, U>, arg2:Executor ):CompletableFuture<U>;
	cancel( arg0:boolean ):boolean;
	complete( arg0:T ):boolean;
	completeAsync( arg0:Supplier<T> ):CompletableFuture<T>;
	completeAsync( arg0:Supplier<T>, arg1:Executor ):CompletableFuture<T>;
	completeExceptionally( arg0:java.lang.Throwable ):boolean;
	completeOnTimeout( arg0:T, arg1:long, arg2:any /*java.util.concurrent.TimeUnit*/ ):CompletableFuture<T>;
	copy(  ):CompletableFuture<T>;
	defaultExecutor(  ):Executor;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	exceptionally( arg0:Func<java.lang.Throwable, T> ):CompletableFuture<T>;
	get(  ):T;
	get( arg0:long, arg1:any /*java.util.concurrent.TimeUnit*/ ):T;
	getNow( arg0:T ):T;
	getNumberOfDependents(  ):int;
	handle<U>( arg0:BiFunction<T, java.lang.Throwable, U> ):CompletableFuture<U>;
	handleAsync<U>( arg0:BiFunction<T, java.lang.Throwable, U> ):CompletableFuture<U>;
	handleAsync<U>( arg0:BiFunction<T, java.lang.Throwable, U>, arg1:Executor ):CompletableFuture<U>;
	isCancelled(  ):boolean;
	isCompletedExceptionally(  ):boolean;
	isDone(  ):boolean;
	join(  ):T;
	minimalCompletionStage(  ):any /*java.util.concurrent.CompletionStage*/;
	newIncompleteFuture<U>(  ):CompletableFuture<U>;
	obtrudeException( arg0:java.lang.Throwable ):void;
	obtrudeValue( arg0:T ):void;
	orTimeout( arg0:long, arg1:any /*java.util.concurrent.TimeUnit*/ ):CompletableFuture<T>;
	runAfterBoth( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:java.lang.Runnable ):CompletableFuture<void>;
	runAfterBothAsync( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:java.lang.Runnable ):CompletableFuture<void>;
	runAfterBothAsync( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:java.lang.Runnable, arg2:Executor ):CompletableFuture<void>;
	runAfterEither( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:java.lang.Runnable ):CompletableFuture<void>;
	runAfterEitherAsync( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:java.lang.Runnable ):CompletableFuture<void>;
	runAfterEitherAsync( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:java.lang.Runnable, arg2:Executor ):CompletableFuture<void>;
	thenAccept( arg0:Consumer<T> ):CompletableFuture<void>;
	thenAcceptAsync( arg0:Consumer<T> ):CompletableFuture<void>;
	thenAcceptAsync( arg0:Consumer<T>, arg1:Executor ):CompletableFuture<void>;
	thenAcceptBoth<U>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:BiConsumer<T, U> ):CompletableFuture<void>;
	thenAcceptBothAsync<U>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:BiConsumer<T, U> ):CompletableFuture<void>;
	thenAcceptBothAsync<U>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:BiConsumer<T, U>, arg2:Executor ):CompletableFuture<void>;
	thenApply<U>( arg0:Func<T, U> ):CompletableFuture<U>;
	thenApplyAsync<U>( arg0:Func<T, U> ):CompletableFuture<U>;
	thenApplyAsync<U>( arg0:Func<T, U>, arg1:Executor ):CompletableFuture<U>;
	thenCombine<U,V>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:BiFunction<T, U, V> ):CompletableFuture<V>;
	thenCombineAsync<U,V>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:BiFunction<T, U, V> ):CompletableFuture<V>;
	thenCombineAsync<U,V>( arg0:any /*java.util.concurrent.CompletionStage*/, arg1:BiFunction<T, U, V>, arg2:Executor ):CompletableFuture<V>;
	thenCompose<U>( arg0:Func<T, any /*java.util.concurrent.CompletionStage*/> ):CompletableFuture<U>;
	thenComposeAsync<U>( arg0:Func<T, any /*java.util.concurrent.CompletionStage*/> ):CompletableFuture<U>;
	thenComposeAsync<U>( arg0:Func<T, any /*java.util.concurrent.CompletionStage*/>, arg1:Executor ):CompletableFuture<U>;
	thenRun( arg0:java.lang.Runnable ):CompletableFuture<void>;
	thenRunAsync( arg0:java.lang.Runnable ):CompletableFuture<void>;
	thenRunAsync( arg0:java.lang.Runnable, arg1:Executor ):CompletableFuture<void>;
	toCompletableFuture(  ):CompletableFuture<T>;
	toString(  ):string;
	whenComplete( arg0:BiConsumer<T, java.lang.Throwable> ):CompletableFuture<T>;
	whenCompleteAsync( arg0:BiConsumer<T, java.lang.Throwable> ):CompletableFuture<T>;
	whenCompleteAsync( arg0:BiConsumer<T, java.lang.Throwable>, arg1:Executor ):CompletableFuture<T>;

} // end CompletableFuture

} // end namespace java.util.concurrent
declare namespace java.lang {

class Throwable/* extends Object implements java.io.Serializable*/ {

	addSuppressed( arg0:Throwable ):void;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	fillInStackTrace(  ):Throwable;
	getCause(  ):Throwable;
	getLocalizedMessage(  ):string;
	getMessage(  ):string;
	getStackTrace(  ):[any /*java.lang.StackTraceElement*/];
	getSuppressed(  ):[Throwable];
	initCause( arg0:Throwable ):Throwable;
	printStackTrace(  ):void;
	printStackTrace( arg0:any /*java.io.PrintStream*/ ):void;
	printStackTrace( arg0:any /*java.io.PrintWriter*/ ):void;
	setStackTrace( arg0:[any /*java.lang.StackTraceElement*/] ):void;
	toString(  ):string;

} // end Throwable

} // end namespace java.lang
declare namespace java.lang {

class RuntimeException/* extends Exception*/ {

	addSuppressed( arg0:Throwable ):void;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	fillInStackTrace(  ):Throwable;
	getCause(  ):Throwable;
	getLocalizedMessage(  ):string;
	getMessage(  ):string;
	getStackTrace(  ):[any /*java.lang.StackTraceElement*/];
	getSuppressed(  ):[Throwable];
	initCause( arg0:Throwable ):Throwable;
	printStackTrace(  ):void;
	printStackTrace( arg0:any /*java.io.PrintStream*/ ):void;
	printStackTrace( arg0:any /*java.io.PrintWriter*/ ):void;
	setStackTrace( arg0:[any /*java.lang.StackTraceElement*/] ):void;
	toString(  ):string;

} // end RuntimeException

} // end namespace java.lang
interface Consumer<T>/*java.util.function.Consumer*/ {

	( arg0:T ):void;
	andThen?( arg0:Consumer<T> ):Consumer<T>;

} // end Consumer
interface Predicate<T>/*java.util.function.Predicate*/ {

	( arg0:T ):boolean;
	// static isEqual<T>( arg0:any /*java.lang.Object*/ ):Predicate<T>;
	// static not<T>( arg0:Predicate<T> ):Predicate<T>;
	and?( arg0:Predicate<T> ):Predicate<T>;
	negate?(  ):Predicate<T>;
	or?( arg0:Predicate<T> ):Predicate<T>;

} // end Predicate
declare namespace java.util {

class Collections/* extends java.lang.Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	toString(  ):string;

} // end Collections

} // end namespace java.util
declare namespace java.nio.file {

class Files/* extends java.lang.Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	static copy( arg0:Path, arg1:Path, ...arg2:any /*java.nio.file.CopyOption*/[] ):Path;
	static copy( arg0:Path, arg1:any /*java.io.OutputStream*/ ):long;
	static copy( arg0:any /*java.io.InputStream*/, arg1:Path, ...arg2:any /*java.nio.file.CopyOption*/[] ):long;
	static createDirectories( arg0:Path, ...arg1:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createDirectory( arg0:Path, ...arg1:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createFile( arg0:Path, ...arg1:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createLink( arg0:Path, arg1:Path ):Path;
	static createSymbolicLink( arg0:Path, arg1:Path, ...arg2:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createTempDirectory( arg0:Path, arg1:string, ...arg2:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createTempDirectory( arg0:string, ...arg1:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createTempFile( arg0:Path, arg1:string, arg2:string, ...arg3:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static createTempFile( arg0:string, arg1:string, ...arg2:any /*java.nio.file.attribute.FileAttribute*/[] ):Path;
	static delete( arg0:Path ):void;
	static deleteIfExists( arg0:Path ):boolean;
	static exists( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):boolean;
	static find( arg0:Path, arg1:int, arg2:BiPredicate<Path, any /*java.nio.file.attribute.BasicFileAttributes*/>, ...arg3:any /*java.nio.file.FileVisitOption*/[] ):java.util.stream.Stream<Path>;
	static getAttribute( arg0:Path, arg1:string, ...arg2:any /*java.nio.file.LinkOption*/[] ):any /*java.lang.Object*/;
	static getFileAttributeView<V>( arg0:Path, arg1:java.lang.Class<V>, ...arg2:any /*java.nio.file.LinkOption*/[] ):V;
	static getFileStore( arg0:Path ):any /*java.nio.file.FileStore*/;
	static getLastModifiedTime( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):any /*java.nio.file.attribute.FileTime*/;
	static getOwner( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):any /*java.nio.file.attribute.UserPrincipal*/;
	static getPosixFilePermissions( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):java.util.Set<any /*java.nio.file.attribute.PosixFilePermission*/>;
	static isDirectory( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):boolean;
	static isExecutable( arg0:Path ):boolean;
	static isHidden( arg0:Path ):boolean;
	static isReadable( arg0:Path ):boolean;
	static isRegularFile( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):boolean;
	static isSameFile( arg0:Path, arg1:Path ):boolean;
	static isSymbolicLink( arg0:Path ):boolean;
	static isWritable( arg0:Path ):boolean;
	static lines( arg0:Path ):java.util.stream.Stream<string>;
	static lines( arg0:Path, arg1:any /*java.nio.charset.Charset*/ ):java.util.stream.Stream<string>;
	static list( arg0:Path ):java.util.stream.Stream<Path>;
	static move( arg0:Path, arg1:Path, ...arg2:any /*java.nio.file.CopyOption*/[] ):Path;
	static newBufferedReader( arg0:Path ):any /*java.io.BufferedReader*/;
	static newBufferedReader( arg0:Path, arg1:any /*java.nio.charset.Charset*/ ):any /*java.io.BufferedReader*/;
	static newBufferedWriter( arg0:Path, ...arg1:any /*java.nio.file.OpenOption*/[] ):any /*java.io.BufferedWriter*/;
	static newBufferedWriter( arg0:Path, arg1:any /*java.nio.charset.Charset*/, ...arg2:any /*java.nio.file.OpenOption*/[] ):any /*java.io.BufferedWriter*/;
	static newByteChannel( arg0:Path, ...arg1:any /*java.nio.file.OpenOption*/[] ):any /*java.nio.channels.SeekableByteChannel*/;
	static newByteChannel( arg0:Path, arg1:java.util.Set<any /*java.nio.file.OpenOption*/>, ...arg2:any /*java.nio.file.attribute.FileAttribute*/[] ):any /*java.nio.channels.SeekableByteChannel*/;
	static newDirectoryStream( arg0:Path ):any /*java.nio.file.DirectoryStream*/;
	static newDirectoryStream( arg0:Path, arg1:any /*java.nio.file.DirectoryStream$Filter*/ ):any /*java.nio.file.DirectoryStream*/;
	static newDirectoryStream( arg0:Path, arg1:string ):any /*java.nio.file.DirectoryStream*/;
	static newInputStream( arg0:Path, ...arg1:any /*java.nio.file.OpenOption*/[] ):any /*java.io.InputStream*/;
	static newOutputStream( arg0:Path, ...arg1:any /*java.nio.file.OpenOption*/[] ):any /*java.io.OutputStream*/;
	static notExists( arg0:Path, ...arg1:any /*java.nio.file.LinkOption*/[] ):boolean;
	static probeContentType( arg0:Path ):string;
	static readAllBytes( arg0:Path ):bytearray;
	static readAllLines( arg0:Path ):java.util.List<string>;
	static readAllLines( arg0:Path, arg1:any /*java.nio.charset.Charset*/ ):java.util.List<string>;
	static readAttributes( arg0:Path, arg1:string, ...arg2:any /*java.nio.file.LinkOption*/[] ):java.util.Map<string, any /*java.lang.Object*/>;
	static readAttributes<A>( arg0:Path, arg1:java.lang.Class<A>, ...arg2:any /*java.nio.file.LinkOption*/[] ):A;
	static readString( arg0:Path ):string;
	static readString( arg0:Path, arg1:any /*java.nio.charset.Charset*/ ):string;
	static readSymbolicLink( arg0:Path ):Path;
	static setAttribute( arg0:Path, arg1:string, arg2:any /*java.lang.Object*/, ...arg3:any /*java.nio.file.LinkOption*/[] ):Path;
	static setLastModifiedTime( arg0:Path, arg1:any /*java.nio.file.attribute.FileTime*/ ):Path;
	static setOwner( arg0:Path, arg1:any /*java.nio.file.attribute.UserPrincipal*/ ):Path;
	static setPosixFilePermissions( arg0:Path, arg1:java.util.Set<any /*java.nio.file.attribute.PosixFilePermission*/> ):Path;
	static size( arg0:Path ):long;
	static walk( arg0:Path, ...arg1:any /*java.nio.file.FileVisitOption*/[] ):java.util.stream.Stream<Path>;
	static walk( arg0:Path, arg1:int, ...arg2:any /*java.nio.file.FileVisitOption*/[] ):java.util.stream.Stream<Path>;
	static walkFileTree( arg0:Path, arg1:any /*java.nio.file.FileVisitor*/ ):Path;
	static walkFileTree( arg0:Path, arg1:java.util.Set<any /*java.nio.file.FileVisitOption*/>, arg2:int, arg3:any /*java.nio.file.FileVisitor*/ ):Path;
	static write( arg0:Path, arg1:bytearray, ...arg2:any /*java.nio.file.OpenOption*/[] ):Path;
	static write( arg0:Path, arg1:java.lang.Iterable<any /*java.lang.CharSequence*/>, ...arg2:any /*java.nio.file.OpenOption*/[] ):Path;
	static write( arg0:Path, arg1:java.lang.Iterable<any /*java.lang.CharSequence*/>, arg2:any /*java.nio.charset.Charset*/, ...arg3:any /*java.nio.file.OpenOption*/[] ):Path;
	static writeString( arg0:Path, arg1:any /*java.lang.CharSequence*/, ...arg2:any /*java.nio.file.OpenOption*/[] ):Path;
	static writeString( arg0:Path, arg1:any /*java.lang.CharSequence*/, arg2:any /*java.nio.charset.Charset*/, ...arg3:any /*java.nio.file.OpenOption*/[] ):Path;
	toString(  ):string;

} // end Files

} // end namespace java.nio.file
declare namespace java.util.stream {

interface Collector<T, A, R> {

	// static of<A,R,T>( arg0:Supplier<A>, arg1:BiConsumer<A, T>, arg2:BinaryOperator<A>, arg3:Func<A, R>, ...arg4:any /*java.util.stream.Collector$Characteristics*/[] ):Collector<T, A, R>;
	// static of<R,T>( arg0:Supplier<R>, arg1:BiConsumer<R, T>, arg2:BinaryOperator<R>, ...arg3:any /*java.util.stream.Collector$Characteristics*/[] ):Collector<T, R, R>;
	accumulator(  ):BiConsumer<A, T>;
	characteristics(  ):java.util.Set<any /*java.util.stream.Collector$Characteristics*/>;
	combiner(  ):BinaryOperator<A>;
	finisher(  ):Func<A, R>;
	supplier(  ):Supplier<A>;

} // end Collector

} // end namespace java.util.stream
declare namespace java.lang {

interface Comparable<T> {

	compareTo( arg0:T ):int;

} // end Comparable

} // end namespace java.lang
declare namespace java.util {

interface List<E>/* extends Collection<E>*/ {

	// static copyOf<E>( arg0:Collection<E> ):List<E>;
	// static of<E>(  ):List<E>;
	// static of<E>( ...arg0:E[] ):List<E>;
	// static of<E>( arg0:E ):List<E>;
	// static of<E>( arg0:E, arg1:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E, arg7:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E, arg7:E, arg8:E ):List<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E, arg7:E, arg8:E, arg9:E ):List<E>;
	add( arg0:E ):boolean;
	add( arg0:int, arg1:E ):void;
	addAll( arg0:Collection<E> ):boolean;
	addAll( arg0:int, arg1:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /*java.lang.Object*/ ):boolean;
	containsAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	forEach<T>( arg0:Consumer<T> ):void;
	get( arg0:int ):E;
	indexOf( arg0:any /*java.lang.Object*/ ):int;
	isEmpty(  ):boolean;
	iterator(  ):Iterator<E>;
	lastIndexOf( arg0:any /*java.lang.Object*/ ):int;
	listIterator(  ):any /*java.util.ListIterator*/;
	listIterator( arg0:int ):any /*java.util.ListIterator*/;
	parallelStream(  ):java.util.stream.Stream<E>;
	remove( arg0:any /*java.lang.Object*/ ):boolean;
	remove( arg0:int ):E;
	removeAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	replaceAll( arg0:UnaryOperator<E> ):void;
	retainAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	set( arg0:int, arg1:E ):E;
	size(  ):int;
	sort( arg0:any /*java.util.Comparator*/ ):void;
	spliterator(  ):any /*java.util.Spliterator*/;
	stream(  ):java.util.stream.Stream<E>;
	subList( arg0:int, arg1:int ):List<E>;
	toArray(  ):[any /*java.lang.Object*/];
	toArray<T>( arg0:[T] ):[T];
	toArray<T>( arg0:any /*java.util.function.IntFunction*/ ):[T];

} // end List

} // end namespace java.util
declare namespace java.util {

interface Collection<E>/* extends java.lang.Iterable<E>*/ {

	add( arg0:E ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /*java.lang.Object*/ ):boolean;
	containsAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	forEach<T>( arg0:Consumer<T> ):void;
	isEmpty(  ):boolean;
	iterator(  ):Iterator<E>;
	parallelStream(  ):java.util.stream.Stream<E>;
	remove( arg0:any /*java.lang.Object*/ ):boolean;
	removeAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	retainAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	size(  ):int;
	spliterator(  ):any /*java.util.Spliterator*/;
	stream(  ):java.util.stream.Stream<E>;
	toArray(  ):[any /*java.lang.Object*/];
	toArray<T>( arg0:[T] ):[T];
	toArray<T>( arg0:any /*java.util.function.IntFunction*/ ):[T];

} // end Collection

} // end namespace java.util
declare namespace java.nio.file {

/* enum */class AccessMode/* extends java.lang.Enum<any>*/ {

	// READ:AccessMode;
	// WRITE:AccessMode;
	// EXECUTE:AccessMode;

	compareTo<E>( arg0:E ):int;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	getDeclaringClass<E>(  ):java.lang.Class<E>;
	name(  ):string;
	ordinal(  ):int;
	toString(  ):string;

} // end AccessMode

} // end namespace java.nio.file
declare namespace java.util.stream {

class Collectors/* extends java.lang.Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	toString(  ):string;

} // end Collectors

} // end namespace java.util.stream
declare namespace java.util {

class Arrays/* extends java.lang.Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	toString(  ):string;

} // end Arrays

} // end namespace java.util
interface BiFunction<T, U, R>/*java.util.function.BiFunction*/ {

	( arg0:T, arg1:U ):R;
	andThen?<V>( arg0:Func<R, V> ):BiFunction<T, U, V>;

} // end BiFunction
declare namespace java.util.concurrent {

class ConcurrentHashMap<K, V>/* extends java.util.AbstractMap<K, V> implements ConcurrentMap<K, V>, java.io.Serializable*/ {

	clear(  ):void;
	compute( arg0:K, arg1:BiFunction<K, V, V> ):V;
	computeIfAbsent( arg0:K, arg1:Func<K, V> ):V;
	computeIfPresent( arg0:K, arg1:BiFunction<K, V, V> ):V;
	contains( arg0:any /*java.lang.Object*/ ):boolean;
	containsKey( arg0:any /*java.lang.Object*/ ):boolean;
	containsValue( arg0:any /*java.lang.Object*/ ):boolean;
	elements(  ):any /*java.util.Enumeration*/;
	entrySet(  ):java.util.Set<java.util.Map$Entry<K, V>>;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	forEach( arg0:BiConsumer<K, V> ):void;
	forEach( arg0:long, arg1:BiConsumer<K, V> ):void;
	forEach<U>( arg0:long, arg1:BiFunction<K, V, U>, arg2:Consumer<U> ):void;
	forEachEntry( arg0:long, arg1:Consumer<java.util.Map$Entry<K, V>> ):void;
	forEachEntry<U>( arg0:long, arg1:Func<java.util.Map$Entry<K, V>, U>, arg2:Consumer<U> ):void;
	forEachKey( arg0:long, arg1:Consumer<K> ):void;
	forEachKey<U>( arg0:long, arg1:Func<K, U>, arg2:Consumer<U> ):void;
	forEachValue( arg0:long, arg1:Consumer<V> ):void;
	forEachValue<U>( arg0:long, arg1:Func<V, U>, arg2:Consumer<U> ):void;
	get( arg0:any /*java.lang.Object*/ ):V;
	getOrDefault( arg0:any /*java.lang.Object*/, arg1:V ):V;
	isEmpty(  ):boolean;
	keySet(  ):any /*java.util.concurrent.ConcurrentHashMap$KeySetView*/;
	keySet( arg0:V ):any /*java.util.concurrent.ConcurrentHashMap$KeySetView*/;
	keys(  ):any /*java.util.Enumeration*/;
	mappingCount(  ):long;
	merge( arg0:K, arg1:V, arg2:BiFunction<V, V, V> ):V;
	put( arg0:K, arg1:V ):V;
	putAll( arg0:java.util.Map<K, V> ):void;
	putIfAbsent( arg0:K, arg1:V ):V;
	reduce<U>( arg0:long, arg1:BiFunction<K, V, U>, arg2:BiFunction<U, U, U> ):U;
	reduceEntries( arg0:long, arg1:BiFunction<java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>, java.util.Map$Entry<K, V>> ):java.util.Map$Entry<K, V>;
	reduceEntries<U>( arg0:long, arg1:Func<java.util.Map$Entry<K, V>, U>, arg2:BiFunction<U, U, U> ):U;
	reduceEntriesToDouble( arg0:long, arg1:any /*java.util.function.ToDoubleFunction*/, arg2:double, arg3:any /*java.util.function.DoubleBinaryOperator*/ ):double;
	reduceEntriesToInt( arg0:long, arg1:any /*java.util.function.ToIntFunction*/, arg2:int, arg3:any /*java.util.function.IntBinaryOperator*/ ):int;
	reduceEntriesToLong( arg0:long, arg1:any /*java.util.function.ToLongFunction*/, arg2:long, arg3:any /*java.util.function.LongBinaryOperator*/ ):long;
	reduceKeys( arg0:long, arg1:BiFunction<K, K, K> ):K;
	reduceKeys<U>( arg0:long, arg1:Func<K, U>, arg2:BiFunction<U, U, U> ):U;
	reduceKeysToDouble( arg0:long, arg1:any /*java.util.function.ToDoubleFunction*/, arg2:double, arg3:any /*java.util.function.DoubleBinaryOperator*/ ):double;
	reduceKeysToInt( arg0:long, arg1:any /*java.util.function.ToIntFunction*/, arg2:int, arg3:any /*java.util.function.IntBinaryOperator*/ ):int;
	reduceKeysToLong( arg0:long, arg1:any /*java.util.function.ToLongFunction*/, arg2:long, arg3:any /*java.util.function.LongBinaryOperator*/ ):long;
	reduceToDouble( arg0:long, arg1:any /*java.util.function.ToDoubleBiFunction*/, arg2:double, arg3:any /*java.util.function.DoubleBinaryOperator*/ ):double;
	reduceToInt( arg0:long, arg1:any /*java.util.function.ToIntBiFunction*/, arg2:int, arg3:any /*java.util.function.IntBinaryOperator*/ ):int;
	reduceToLong( arg0:long, arg1:any /*java.util.function.ToLongBiFunction*/, arg2:long, arg3:any /*java.util.function.LongBinaryOperator*/ ):long;
	reduceValues( arg0:long, arg1:BiFunction<V, V, V> ):V;
	reduceValues<U>( arg0:long, arg1:Func<V, U>, arg2:BiFunction<U, U, U> ):U;
	reduceValuesToDouble( arg0:long, arg1:any /*java.util.function.ToDoubleFunction*/, arg2:double, arg3:any /*java.util.function.DoubleBinaryOperator*/ ):double;
	reduceValuesToInt( arg0:long, arg1:any /*java.util.function.ToIntFunction*/, arg2:int, arg3:any /*java.util.function.IntBinaryOperator*/ ):int;
	reduceValuesToLong( arg0:long, arg1:any /*java.util.function.ToLongFunction*/, arg2:long, arg3:any /*java.util.function.LongBinaryOperator*/ ):long;
	remove( arg0:any /*java.lang.Object*/ ):V;
	remove( arg0:any /*java.lang.Object*/, arg1:any /*java.lang.Object*/ ):boolean;
	replace( arg0:K, arg1:V ):V;
	replace( arg0:K, arg1:V, arg2:V ):boolean;
	replaceAll( arg0:BiFunction<K, V, V> ):void;
	search<U>( arg0:long, arg1:BiFunction<K, V, U> ):U;
	searchEntries<U>( arg0:long, arg1:Func<java.util.Map$Entry<K, V>, U> ):U;
	searchKeys<U>( arg0:long, arg1:Func<K, U> ):U;
	searchValues<U>( arg0:long, arg1:Func<V, U> ):U;
	size(  ):int;
	toString(  ):string;
	values(  ):java.util.Collection<V>;

} // end ConcurrentHashMap

} // end namespace java.util.concurrent
declare namespace java.nio.file {

interface Path/* extends java.lang.Comparable<any>, java.lang.Iterable<any>, Watchable*/ {

	// static of( arg0:java.net.URI ):Path;
	// static of( arg0:string, ...arg1:string[] ):Path;
	compareTo( arg0:Path ):int;
	endsWith( arg0:Path ):boolean;
	endsWith( arg0:string ):boolean;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	forEach<T>( arg0:Consumer<T> ):void;
	getFileName(  ):Path;
	getFileSystem(  ):any /*java.nio.file.FileSystem*/;
	getName( arg0:int ):Path;
	getNameCount(  ):int;
	getParent(  ):Path;
	getRoot(  ):Path;
	isAbsolute(  ):boolean;
	iterator(  ):java.util.Iterator<Path>;
	normalize(  ):Path;
	register( arg0:any /*java.nio.file.WatchService*/, ...arg1:any /*java.nio.file.WatchEvent$Kind*/[] ):any /*java.nio.file.WatchKey*/;
	register( arg0:any /*java.nio.file.WatchService*/, arg1:[any /*java.nio.file.WatchEvent$Kind*/], ...arg2:any /*java.nio.file.WatchEvent$Modifier*/[] ):any /*java.nio.file.WatchKey*/;
	relativize( arg0:Path ):Path;
	resolve( arg0:Path ):Path;
	resolve( arg0:string ):Path;
	resolveSibling( arg0:Path ):Path;
	resolveSibling( arg0:string ):Path;
	spliterator(  ):any /*java.util.Spliterator*/;
	startsWith( arg0:Path ):boolean;
	startsWith( arg0:string ):boolean;
	subpath( arg0:int, arg1:int ):Path;
	toAbsolutePath(  ):Path;
	toFile(  ):any /*java.io.File*/;
	toRealPath( ...arg0:any /*java.nio.file.LinkOption*/[] ):Path;
	toString(  ):string;
	toUri(  ):java.net.URI;

} // end Path

} // end namespace java.nio.file
interface Func<T, R>/*java.util.function.Function*/ {

	( arg0:T ):R;
	// static identity<T>(  ):Func<T, T>;
	andThen?<V>( arg0:Func<R, V> ):Func<T, V>;
	compose?<V>( arg0:Func<V, T> ):Func<V, R>;

} // end Func
declare namespace java.lang {

interface Runnable {

	(  ):void;

} // end Runnable

} // end namespace java.lang
declare namespace java.util {

interface Set<E>/* extends Collection<E>*/ {

	// static copyOf<E>( arg0:Collection<E> ):Set<E>;
	// static of<E>(  ):Set<E>;
	// static of<E>( ...arg0:E[] ):Set<E>;
	// static of<E>( arg0:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E, arg7:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E, arg7:E, arg8:E ):Set<E>;
	// static of<E>( arg0:E, arg1:E, arg2:E, arg3:E, arg4:E, arg5:E, arg6:E, arg7:E, arg8:E, arg9:E ):Set<E>;
	add( arg0:E ):boolean;
	addAll( arg0:Collection<E> ):boolean;
	clear(  ):void;
	contains( arg0:any /*java.lang.Object*/ ):boolean;
	containsAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	forEach<T>( arg0:Consumer<T> ):void;
	isEmpty(  ):boolean;
	iterator(  ):Iterator<E>;
	parallelStream(  ):java.util.stream.Stream<E>;
	remove( arg0:any /*java.lang.Object*/ ):boolean;
	removeAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	removeIf( arg0:Predicate<E> ):boolean;
	retainAll( arg0:Collection<any /*java.lang.Object*/> ):boolean;
	size(  ):int;
	spliterator(  ):any /*java.util.Spliterator*/;
	stream(  ):java.util.stream.Stream<E>;
	toArray(  ):[any /*java.lang.Object*/];
	toArray<T>( arg0:[T] ):[T];
	toArray<T>( arg0:any /*java.util.function.IntFunction*/ ):[T];

} // end Set

} // end namespace java.util
declare namespace java.lang {

interface Iterable<T> {

	(  ):java.util.Iterator<T>;
	forEach?( arg0:Consumer<T> ):void;
	spliterator?(  ):any /*java.util.Spliterator*/;

} // end Iterable

} // end namespace java.lang
declare namespace java.util {

interface Map<K, V> {

	// static copyOf<K,V>( arg0:Map<K, V> ):Map<K, V>;
	// static entry<K,V>( arg0:K, arg1:V ):Map$Entry<K, V>;
	// static of<K,V>(  ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V, arg8:K, arg9:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V, arg8:K, arg9:V, arg10:K, arg11:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V, arg8:K, arg9:V, arg10:K, arg11:V, arg12:K, arg13:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V, arg8:K, arg9:V, arg10:K, arg11:V, arg12:K, arg13:V, arg14:K, arg15:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V, arg8:K, arg9:V, arg10:K, arg11:V, arg12:K, arg13:V, arg14:K, arg15:V, arg16:K, arg17:V ):Map<K, V>;
	// static of<K,V>( arg0:K, arg1:V, arg2:K, arg3:V, arg4:K, arg5:V, arg6:K, arg7:V, arg8:K, arg9:V, arg10:K, arg11:V, arg12:K, arg13:V, arg14:K, arg15:V, arg16:K, arg17:V, arg18:K, arg19:V ):Map<K, V>;
	// static ofEntries<K,V>( ...arg0:Map$Entry<K, V>[] ):Map<K, V>;
	clear(  ):void;
	compute( arg0:K, arg1:BiFunction<K, V, V> ):V;
	computeIfAbsent( arg0:K, arg1:Func<K, V> ):V;
	computeIfPresent( arg0:K, arg1:BiFunction<K, V, V> ):V;
	containsKey( arg0:any /*java.lang.Object*/ ):boolean;
	containsValue( arg0:any /*java.lang.Object*/ ):boolean;
	entrySet(  ):Set<Map$Entry<K, V>>;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	forEach( arg0:BiConsumer<K, V> ):void;
	get( arg0:any /*java.lang.Object*/ ):V;
	getOrDefault( arg0:any /*java.lang.Object*/, arg1:V ):V;
	isEmpty(  ):boolean;
	keySet(  ):Set<K>;
	merge( arg0:K, arg1:V, arg2:BiFunction<V, V, V> ):V;
	put( arg0:K, arg1:V ):V;
	putAll( arg0:Map<K, V> ):void;
	putIfAbsent( arg0:K, arg1:V ):V;
	remove( arg0:any /*java.lang.Object*/ ):V;
	remove( arg0:any /*java.lang.Object*/, arg1:any /*java.lang.Object*/ ):boolean;
	replace( arg0:K, arg1:V ):V;
	replace( arg0:K, arg1:V, arg2:V ):boolean;
	replaceAll( arg0:BiFunction<K, V, V> ):void;
	size(  ):int;
	values(  ):Collection<V>;

} // end Map

} // end namespace java.util
declare namespace java.lang {

class System/* extends Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	static arraycopy( arg0:any /*java.lang.Object*/, arg1:int, arg2:any /*java.lang.Object*/, arg3:int, arg4:int ):void;
	static clearProperty( arg0:string ):string;
	static console(  ):any /*java.io.Console*/;
	static currentTimeMillis(  ):long;
	static exit( arg0:int ):void;
	static gc(  ):void;
	static getLogger( arg0:string ):any /*java.lang.System$Logger*/;
	static getLogger( arg0:string, arg1:any /*java.util.ResourceBundle*/ ):any /*java.lang.System$Logger*/;
	static getProperties(  ):any /*java.util.Properties*/;
	static getProperty( arg0:string ):string;
	static getProperty( arg0:string, arg1:string ):string;
	static getSecurityManager(  ):any /*java.lang.SecurityManager*/;
	static getenv(  ):java.util.Map<string, string>;
	static getenv( arg0:string ):string;
	static identityHashCode( arg0:any /*java.lang.Object*/ ):int;
	static inheritedChannel(  ):any /*java.nio.channels.Channel*/;
	static lineSeparator(  ):string;
	static load( arg0:string ):void;
	static loadLibrary( arg0:string ):void;
	static mapLibraryName( arg0:string ):string;
	static nanoTime(  ):long;
	static runFinalization(  ):void;
	static setErr( arg0:any /*java.io.PrintStream*/ ):void;
	static setIn( arg0:any /*java.io.InputStream*/ ):void;
	static setOut( arg0:any /*java.io.PrintStream*/ ):void;
	static setProperties( arg0:any /*java.util.Properties*/ ):void;
	static setProperty( arg0:string, arg1:string ):string;
	static setSecurityManager( arg0:any /*java.lang.SecurityManager*/ ):void;
	toString(  ):string;

} // end System

} // end namespace java.lang
declare namespace java.util {

class Optional<T>/* extends java.lang.Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	filter( arg0:Predicate<T> ):Optional<T>;
	flatMap<U>( arg0:Func<T, Optional<U>> ):Optional<U>;
	get(  ):T;
	ifPresent( arg0:Consumer<T> ):void;
	ifPresentOrElse( arg0:Consumer<T>, arg1:java.lang.Runnable ):void;
	isEmpty(  ):boolean;
	isPresent(  ):boolean;
	map<U>( arg0:Func<T, U> ):Optional<U>;
	or( arg0:Supplier<Optional<T>> ):Optional<T>;
	orElse( arg0:T ):T;
	orElseGet( arg0:Supplier<T> ):T;
	orElseThrow(  ):T;
	orElseThrow<X>( arg0:Supplier<X> ):T;
	stream(  ):java.util.stream.Stream<T>;
	toString(  ):string;

} // end Optional

} // end namespace java.util
interface BiPredicate<T, U>/*java.util.function.BiPredicate*/ {

	( arg0:T, arg1:U ):boolean;
	and?( arg0:BiPredicate<T, U> ):BiPredicate<T, U>;
	negate?(  ):BiPredicate<T, U>;
	or?( arg0:BiPredicate<T, U> ):BiPredicate<T, U>;

} // end BiPredicate
declare namespace java.lang.management {

/* enum */class MemoryType/* extends java.lang.Enum<any>*/ {

	// HEAP:MemoryType;
	// NON_HEAP:MemoryType;

	compareTo<E>( arg0:E ):int;
	equals( arg0:any /*java.lang.Object*/ ):boolean;
	getDeclaringClass<E>(  ):java.lang.Class<E>;
	name(  ):string;
	ordinal(  ):int;
	toString(  ):string;

} // end MemoryType

} // end namespace java.lang.management
declare namespace java.net {

class URL/* extends java.lang.Object implements java.io.Serializable*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	getAuthority(  ):string;
	getContent(  ):any /*java.lang.Object*/;
	getContent( arg0:[java.lang.Class<any /*java.lang.Object*/>] ):any /*java.lang.Object*/;
	getDefaultPort(  ):int;
	getFile(  ):string;
	getHost(  ):string;
	getPath(  ):string;
	getPort(  ):int;
	getProtocol(  ):string;
	getQuery(  ):string;
	getRef(  ):string;
	getUserInfo(  ):string;
	openConnection(  ):any /*java.net.URLConnection*/;
	openConnection( arg0:any /*java.net.Proxy*/ ):any /*java.net.URLConnection*/;
	openStream(  ):any /*java.io.InputStream*/;
	sameFile( arg0:URL ):boolean;
	static setURLStreamHandlerFactory( arg0:any /*java.net.URLStreamHandlerFactory*/ ):void;
	toExternalForm(  ):string;
	toString(  ):string;
	toURI(  ):URI;

} // end URL

} // end namespace java.net
declare namespace java.nio.file {

class Paths/* extends java.lang.Object*/ {

	equals( arg0:any /*java.lang.Object*/ ):boolean;
	static get( arg0:java.net.URI ):Path;
	static get( arg0:string, ...arg1:string[] ):Path;
	toString(  ):string;

} // end Paths

} // end namespace java.nio.file
