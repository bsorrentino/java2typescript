/*
 * Project: java2typescript - https://github.com/bsorrentino/java2typescript
 *
 * Author: bsorrentino 
 *
 * TYPESCRIPT EXPORTED DECLARATIONS
 *
 */
/// <reference path="jdk8.d.ts"/>

interface StreamStatic {

	readonly class:any;
	builder(  ):any /*java.util.stream.Stream$Builder*/;
	concat<T>( arg0:java.util.stream.Stream<T>, arg1:java.util.stream.Stream<T> ):java.util.stream.Stream<T>;
	empty<T>(  ):java.util.stream.Stream<T>;
	generate<T>( arg0:Supplier<T> ):java.util.stream.Stream<T>;
	iterate<T>( arg0:T, arg1:UnaryOperator<T> ):java.util.stream.Stream<T>;
	iterate<T>( arg0:T, arg1:Predicate<T>, arg2:UnaryOperator<T> ):java.util.stream.Stream<T>;
	of<T>( arg0:T ):java.util.stream.Stream<T>;
	of<T>( ...arg0:T[] ):java.util.stream.Stream<T>;
	ofNullable<T>( arg0:T ):java.util.stream.Stream<T>;
}

export const Stream: StreamStatic = Java.type("java.util.stream.Stream");


interface MemoryTypeStatic {

	HEAP:java.lang.management.MemoryType;
	NON_HEAP:java.lang.management.MemoryType;

	readonly class:any;
	valueOf( arg0:string ):java.lang.management.MemoryType;
	valueOf<T>( arg0:java.lang.Class<T>, arg1:string ):T;
	values(  ):[java.lang.management.MemoryType];
}

export const MemoryType: MemoryTypeStatic = Java.type("java.lang.management.MemoryType");


interface StringStatic {

	readonly class:any;
	new( arg0:bytearray ):string;
	new( arg0:bytearray, arg1:int, arg2:int ):string;
	new( arg0:bytearray, arg1:any /*java.nio.charset.Charset*/ ):string;
	new( arg0:bytearray, arg1:string ):string;
	new( arg0:bytearray, arg1:int, arg2:int, arg3:any /*java.nio.charset.Charset*/ ):string;
	new( arg0:any /*java.lang.StringBuilder*/ ):string;
	new( arg0:any /*java.lang.StringBuffer*/ ):string;
	new( arg0:chararray, arg1:int, arg2:int ):string;
	new( arg0:chararray ):string;
	new( arg0:string ):string;
	new(  ):string;
	new( arg0:bytearray, arg1:int, arg2:int, arg3:string ):string;
	new( arg0:bytearray, arg1:int ):string;
	new( arg0:bytearray, arg1:int, arg2:int, arg3:int ):string;
	new( arg0:[int], arg1:int, arg2:int ):string;
	copyValueOf( arg0:chararray, arg1:int, arg2:int ):string;
	copyValueOf( arg0:chararray ):string;
	format( arg0:string, ...arg1:any /*java.lang.Object*/[] ):string;
	format( arg0:any /*java.util.Locale*/, arg1:string, ...arg2:any /*java.lang.Object*/[] ):string;
	join( arg0:any /*java.lang.CharSequence*/, ...arg1:any /*java.lang.CharSequence*/[] ):string;
	join( arg0:any /*java.lang.CharSequence*/, arg1:java.lang.Iterable<any /*java.lang.CharSequence*/> ):string;
	valueOf( arg0:chararray ):string;
	valueOf( arg0:int ):string;
	valueOf( arg0:float ):string;
	valueOf( arg0:boolean ):string;
	valueOf( arg0:long ):string;
	valueOf( arg0:double ):string;
	valueOf( arg0:any /*java.lang.Object*/ ):string;
	valueOf( arg0:any /*char*/ ):string;
	valueOf( arg0:chararray, arg1:int, arg2:int ):string;
}

export const String: StringStatic = Java.type("java.lang.String");


interface CollectorsStatic {

	readonly class:any;
	averagingDouble<T>( arg0:any /*java.util.function.ToDoubleFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, double|null>;
	averagingInt<T>( arg0:any /*java.util.function.ToIntFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, double|null>;
	averagingLong<T>( arg0:any /*java.util.function.ToLongFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, double|null>;
	collectingAndThen<RR,A,R,T>( arg0:java.util.stream.Collector<T, A, R>, arg1:Func<R, RR> ):java.util.stream.Collector<T, A, RR>;
	counting<T>(  ):java.util.stream.Collector<T, any /*java.lang.Object*/, long|null>;
	filtering<A,R,T>( arg0:Predicate<T>, arg1:java.util.stream.Collector<T, A, R> ):java.util.stream.Collector<T, any /*java.lang.Object*/, R>;
	flatMapping<A,R,T,U>( arg0:Func<T, java.util.stream.Stream<U>>, arg1:java.util.stream.Collector<U, A, R> ):java.util.stream.Collector<T, any /*java.lang.Object*/, R>;
	groupingBy<K,T>( arg0:Func<T, K> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<K, java.util.List<T>>>;
	groupingBy<A,K,T,D,M>( arg0:Func<T, K>, arg1:Supplier<M>, arg2:java.util.stream.Collector<T, A, D> ):java.util.stream.Collector<T, any /*java.lang.Object*/, M>;
	groupingBy<A,K,T,D>( arg0:Func<T, K>, arg1:java.util.stream.Collector<T, A, D> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<K, D>>;
	groupingByConcurrent<A,K,T,D,M>( arg0:Func<T, K>, arg1:Supplier<M>, arg2:java.util.stream.Collector<T, A, D> ):java.util.stream.Collector<T, any /*java.lang.Object*/, M>;
	groupingByConcurrent<A,K,T,D>( arg0:Func<T, K>, arg1:java.util.stream.Collector<T, A, D> ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.concurrent.ConcurrentMap*/>;
	groupingByConcurrent<K,T>( arg0:Func<T, K> ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.concurrent.ConcurrentMap*/>;
	joining( arg0:any /*java.lang.CharSequence*/ ):java.util.stream.Collector<any /*java.lang.CharSequence*/, any /*java.lang.Object*/, string>;
	joining(  ):java.util.stream.Collector<any /*java.lang.CharSequence*/, any /*java.lang.Object*/, string>;
	joining( arg0:any /*java.lang.CharSequence*/, arg1:any /*java.lang.CharSequence*/, arg2:any /*java.lang.CharSequence*/ ):java.util.stream.Collector<any /*java.lang.CharSequence*/, any /*java.lang.Object*/, string>;
	mapping<A,R,T,U>( arg0:Func<T, U>, arg1:java.util.stream.Collector<U, A, R> ):java.util.stream.Collector<T, any /*java.lang.Object*/, R>;
	maxBy<T>( arg0:any /*java.util.Comparator*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Optional<T>>;
	minBy<T>( arg0:any /*java.util.Comparator*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Optional<T>>;
	partitioningBy<T>( arg0:Predicate<T> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<boolean|null, java.util.List<T>>>;
	partitioningBy<A,T,D>( arg0:Predicate<T>, arg1:java.util.stream.Collector<T, A, D> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<boolean|null, D>>;
	reducing<T>( arg0:T, arg1:BinaryOperator<T> ):java.util.stream.Collector<T, any /*java.lang.Object*/, T>;
	reducing<T,U>( arg0:U, arg1:Func<T, U>, arg2:BinaryOperator<U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, U>;
	reducing<T>( arg0:BinaryOperator<T> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Optional<T>>;
	summarizingDouble<T>( arg0:any /*java.util.function.ToDoubleFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.DoubleSummaryStatistics*/>;
	summarizingInt<T>( arg0:any /*java.util.function.ToIntFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.IntSummaryStatistics*/>;
	summarizingLong<T>( arg0:any /*java.util.function.ToLongFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.LongSummaryStatistics*/>;
	summingDouble<T>( arg0:any /*java.util.function.ToDoubleFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, double|null>;
	summingInt<T>( arg0:any /*java.util.function.ToIntFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, int|null>;
	summingLong<T>( arg0:any /*java.util.function.ToLongFunction*/ ):java.util.stream.Collector<T, any /*java.lang.Object*/, long|null>;
	toCollection<C,T>( arg0:Supplier<C> ):java.util.stream.Collector<T, any /*java.lang.Object*/, C>;
	toConcurrentMap<K,T,U,M>( arg0:Func<T, K>, arg1:Func<T, U>, arg2:BinaryOperator<U>, arg3:Supplier<M> ):java.util.stream.Collector<T, any /*java.lang.Object*/, M>;
	toConcurrentMap<K,T,U>( arg0:Func<T, K>, arg1:Func<T, U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.concurrent.ConcurrentMap*/>;
	toConcurrentMap<K,T,U>( arg0:Func<T, K>, arg1:Func<T, U>, arg2:BinaryOperator<U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, any /*java.util.concurrent.ConcurrentMap*/>;
	toList<T>(  ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.List<T>>;
	toMap<K,T,U,M>( arg0:Func<T, K>, arg1:Func<T, U>, arg2:BinaryOperator<U>, arg3:Supplier<M> ):java.util.stream.Collector<T, any /*java.lang.Object*/, M>;
	toMap<K,T,U>( arg0:Func<T, K>, arg1:Func<T, U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<K, U>>;
	toMap<K,T,U>( arg0:Func<T, K>, arg1:Func<T, U>, arg2:BinaryOperator<U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<K, U>>;
	toSet<T>(  ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Set<T>>;
	toUnmodifiableList<T>(  ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.List<T>>;
	toUnmodifiableMap<K,T,U>( arg0:Func<T, K>, arg1:Func<T, U>, arg2:BinaryOperator<U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<K, U>>;
	toUnmodifiableMap<K,T,U>( arg0:Func<T, K>, arg1:Func<T, U> ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Map<K, U>>;
	toUnmodifiableSet<T>(  ):java.util.stream.Collector<T, any /*java.lang.Object*/, java.util.Set<T>>;
}

export const Collectors: CollectorsStatic = Java.type("java.util.stream.Collectors");


interface URIStatic {

	readonly class:any;
	new( arg0:string, arg1:string, arg2:string, arg3:string, arg4:string ):java.net.URI;
	new( arg0:string, arg1:string, arg2:string, arg3:string ):java.net.URI;
	new( arg0:string, arg1:string, arg2:string ):java.net.URI;
	new( arg0:string ):java.net.URI;
	new( arg0:string, arg1:string, arg2:string, arg3:int, arg4:string, arg5:string, arg6:string ):java.net.URI;
	create( arg0:string ):java.net.URI;
}

export const URI: URIStatic = Java.type("java.net.URI");


interface RuntimeExceptionStatic {

	readonly class:any;
	new( arg0:java.lang.Throwable ):java.lang.RuntimeException;
	new( arg0:string, arg1:java.lang.Throwable ):java.lang.RuntimeException;
	new( arg0:string ):java.lang.RuntimeException;
	new(  ):java.lang.RuntimeException;
}

export const RuntimeException: RuntimeExceptionStatic = Java.type("java.lang.RuntimeException");


interface CollectionsStatic {

	readonly class:any;
	addAll<T>( arg0:java.util.Collection<T>, ...arg1:T[] ):boolean;
	asLifoQueue( arg0:any /*java.util.Deque*/ ):any /*java.util.Queue*/;
	binarySearch<T>( arg0:java.util.List<T>, arg1:T, arg2:any /*java.util.Comparator*/ ):int;
	binarySearch<T>( arg0:java.util.List<java.lang.Comparable<T>>, arg1:T ):int;
	checkedCollection<E>( arg0:java.util.Collection<E>, arg1:java.lang.Class<E> ):java.util.Collection<E>;
	checkedList<E>( arg0:java.util.List<E>, arg1:java.lang.Class<E> ):java.util.List<E>;
	checkedMap<K,V>( arg0:java.util.Map<K, V>, arg1:java.lang.Class<K>, arg2:java.lang.Class<V> ):java.util.Map<K, V>;
	checkedNavigableMap<K,V>( arg0:any /*java.util.NavigableMap*/, arg1:java.lang.Class<K>, arg2:java.lang.Class<V> ):any /*java.util.NavigableMap*/;
	checkedNavigableSet<E>( arg0:any /*java.util.NavigableSet*/, arg1:java.lang.Class<E> ):any /*java.util.NavigableSet*/;
	checkedQueue<E>( arg0:any /*java.util.Queue*/, arg1:java.lang.Class<E> ):any /*java.util.Queue*/;
	checkedSet<E>( arg0:java.util.Set<E>, arg1:java.lang.Class<E> ):java.util.Set<E>;
	checkedSortedMap<K,V>( arg0:any /*java.util.SortedMap*/, arg1:java.lang.Class<K>, arg2:java.lang.Class<V> ):any /*java.util.SortedMap*/;
	checkedSortedSet<E>( arg0:any /*java.util.SortedSet*/, arg1:java.lang.Class<E> ):any /*java.util.SortedSet*/;
	copy<T>( arg0:java.util.List<T>, arg1:java.util.List<T> ):void;
	disjoint( arg0:java.util.Collection<any /*java.lang.Object*/>, arg1:java.util.Collection<any /*java.lang.Object*/> ):boolean;
	emptyEnumeration(  ):any /*java.util.Enumeration*/;
	emptyIterator<T>(  ):java.util.Iterator<T>;
	emptyList<T>(  ):java.util.List<T>;
	emptyListIterator(  ):any /*java.util.ListIterator*/;
	emptyMap<K,V>(  ):java.util.Map<K, V>;
	emptyNavigableMap(  ):any /*java.util.NavigableMap*/;
	emptyNavigableSet(  ):any /*java.util.NavigableSet*/;
	emptySet<T>(  ):java.util.Set<T>;
	emptySortedMap(  ):any /*java.util.SortedMap*/;
	emptySortedSet(  ):any /*java.util.SortedSet*/;
	enumeration<T>( arg0:java.util.Collection<T> ):any /*java.util.Enumeration*/;
	fill<T>( arg0:java.util.List<T>, arg1:T ):void;
	frequency( arg0:java.util.Collection<any /*java.lang.Object*/>, arg1:any /*java.lang.Object*/ ):int;
	indexOfSubList( arg0:java.util.List<any /*java.lang.Object*/>, arg1:java.util.List<any /*java.lang.Object*/> ):int;
	lastIndexOfSubList( arg0:java.util.List<any /*java.lang.Object*/>, arg1:java.util.List<any /*java.lang.Object*/> ):int;
	list( arg0:any /*java.util.Enumeration*/ ):any /*java.util.ArrayList*/;
	max<T>( arg0:java.util.Collection<T> ):T;
	max<T>( arg0:java.util.Collection<T>, arg1:any /*java.util.Comparator*/ ):T;
	min<T>( arg0:java.util.Collection<T> ):T;
	min<T>( arg0:java.util.Collection<T>, arg1:any /*java.util.Comparator*/ ):T;
	nCopies<T>( arg0:int, arg1:T ):java.util.List<T>;
	newSetFromMap<E>( arg0:java.util.Map<E, boolean|null> ):java.util.Set<E>;
	replaceAll<T>( arg0:java.util.List<T>, arg1:T, arg2:T ):boolean;
	reverse( arg0:java.util.List<any /*java.lang.Object*/> ):void;
	reverseOrder( arg0:any /*java.util.Comparator*/ ):any /*java.util.Comparator*/;
	reverseOrder(  ):any /*java.util.Comparator*/;
	rotate( arg0:java.util.List<any /*java.lang.Object*/>, arg1:int ):void;
	shuffle( arg0:java.util.List<any /*java.lang.Object*/>, arg1:any /*java.util.Random*/ ):void;
	shuffle( arg0:java.util.List<any /*java.lang.Object*/> ):void;
	singleton<T>( arg0:T ):java.util.Set<T>;
	singletonList<T>( arg0:T ):java.util.List<T>;
	singletonMap<K,V>( arg0:K, arg1:V ):java.util.Map<K, V>;
	sort<T>( arg0:java.util.List<T> ):void;
	sort<T>( arg0:java.util.List<T>, arg1:any /*java.util.Comparator*/ ):void;
	swap( arg0:java.util.List<any /*java.lang.Object*/>, arg1:int, arg2:int ):void;
	synchronizedCollection<T>( arg0:java.util.Collection<T> ):java.util.Collection<T>;
	synchronizedList<T>( arg0:java.util.List<T> ):java.util.List<T>;
	synchronizedMap<K,V>( arg0:java.util.Map<K, V> ):java.util.Map<K, V>;
	synchronizedNavigableMap( arg0:any /*java.util.NavigableMap*/ ):any /*java.util.NavigableMap*/;
	synchronizedNavigableSet( arg0:any /*java.util.NavigableSet*/ ):any /*java.util.NavigableSet*/;
	synchronizedSet<T>( arg0:java.util.Set<T> ):java.util.Set<T>;
	synchronizedSortedMap( arg0:any /*java.util.SortedMap*/ ):any /*java.util.SortedMap*/;
	synchronizedSortedSet( arg0:any /*java.util.SortedSet*/ ):any /*java.util.SortedSet*/;
	unmodifiableCollection<T>( arg0:java.util.Collection<T> ):java.util.Collection<T>;
	unmodifiableList<T>( arg0:java.util.List<T> ):java.util.List<T>;
	unmodifiableMap<K,V>( arg0:java.util.Map<K, V> ):java.util.Map<K, V>;
	unmodifiableNavigableMap( arg0:any /*java.util.NavigableMap*/ ):any /*java.util.NavigableMap*/;
	unmodifiableNavigableSet( arg0:any /*java.util.NavigableSet*/ ):any /*java.util.NavigableSet*/;
	unmodifiableSet<T>( arg0:java.util.Set<T> ):java.util.Set<T>;
	unmodifiableSortedMap( arg0:any /*java.util.SortedMap*/ ):any /*java.util.SortedMap*/;
	unmodifiableSortedSet( arg0:any /*java.util.SortedSet*/ ):any /*java.util.SortedSet*/;
}

export const Collections: CollectionsStatic = Java.type("java.util.Collections");


interface ConsumerStatic {

	readonly class:any;
	new<T>( arg0:Consumer<T> ):Consumer<T>;
}

export const Consumer: ConsumerStatic = Java.type("java.util.function.Consumer");


interface AccessModeStatic {

	READ:java.nio.file.AccessMode;
	WRITE:java.nio.file.AccessMode;
	EXECUTE:java.nio.file.AccessMode;

	readonly class:any;
	valueOf( arg0:string ):java.nio.file.AccessMode;
	valueOf<T>( arg0:java.lang.Class<T>, arg1:string ):T;
	values(  ):[java.nio.file.AccessMode];
}

export const AccessMode: AccessModeStatic = Java.type("java.nio.file.AccessMode");


interface ArraysStatic {

	readonly class:any;
	asList<T>( ...arg0:T[] ):java.util.List<T>;
	binarySearch( arg0:[double], arg1:int, arg2:int, arg3:double ):int;
	binarySearch( arg0:[float], arg1:float ):int;
	binarySearch( arg0:[float], arg1:int, arg2:int, arg3:float ):int;
	binarySearch( arg0:[double], arg1:double ):int;
	binarySearch( arg0:bytearray, arg1:int, arg2:int, arg3:any /*byte*/ ):int;
	binarySearch( arg0:chararray, arg1:int, arg2:int, arg3:any /*char*/ ):int;
	binarySearch( arg0:chararray, arg1:any /*char*/ ):int;
	binarySearch( arg0:bytearray, arg1:any /*byte*/ ):int;
	binarySearch( arg0:[long], arg1:int, arg2:int, arg3:long ):int;
	binarySearch( arg0:[any /*short*/], arg1:any /*short*/ ):int;
	binarySearch( arg0:[int], arg1:int ):int;
	binarySearch( arg0:[any /*java.lang.Object*/], arg1:any /*java.lang.Object*/ ):int;
	binarySearch<T>( arg0:[T], arg1:int, arg2:int, arg3:T, arg4:any /*java.util.Comparator*/ ):int;
	binarySearch<T>( arg0:[T], arg1:T, arg2:any /*java.util.Comparator*/ ):int;
	binarySearch( arg0:[int], arg1:int, arg2:int, arg3:int ):int;
	binarySearch( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int, arg3:any /*java.lang.Object*/ ):int;
	binarySearch( arg0:[any /*short*/], arg1:int, arg2:int, arg3:any /*short*/ ):int;
	binarySearch( arg0:[long], arg1:long ):int;
	compare( arg0:bytearray, arg1:int, arg2:int, arg3:bytearray, arg4:int, arg5:int ):int;
	compare( arg0:bytearray, arg1:bytearray ):int;
	compare( arg0:[long], arg1:int, arg2:int, arg3:[long], arg4:int, arg5:int ):int;
	compare( arg0:[float], arg1:int, arg2:int, arg3:[float], arg4:int, arg5:int ):int;
	compare( arg0:[int], arg1:int, arg2:int, arg3:[int], arg4:int, arg5:int ):int;
	compare( arg0:[any /*short*/], arg1:int, arg2:int, arg3:[any /*short*/], arg4:int, arg5:int ):int;
	compare( arg0:chararray, arg1:int, arg2:int, arg3:chararray, arg4:int, arg5:int ):int;
	compare( arg0:[any /*short*/], arg1:[any /*short*/] ):int;
	compare( arg0:[boolean], arg1:int, arg2:int, arg3:[boolean], arg4:int, arg5:int ):int;
	compare( arg0:[boolean], arg1:[boolean] ):int;
	compare<T>( arg0:[T], arg1:int, arg2:int, arg3:[T], arg4:int, arg5:int, arg6:any /*java.util.Comparator*/ ):int;
	compare( arg0:[long], arg1:[long] ):int;
	compare<T>( arg0:[T], arg1:int, arg2:int, arg3:[T], arg4:int, arg5:int ):int;
	compare<T>( arg0:[T], arg1:[T] ):int;
	compare( arg0:[float], arg1:[float] ):int;
	compare( arg0:[double], arg1:int, arg2:int, arg3:[double], arg4:int, arg5:int ):int;
	compare( arg0:[double], arg1:[double] ):int;
	compare<T>( arg0:[T], arg1:[T], arg2:any /*java.util.Comparator*/ ):int;
	compare( arg0:chararray, arg1:chararray ):int;
	compare( arg0:[int], arg1:[int] ):int;
	compareUnsigned( arg0:[any /*short*/], arg1:int, arg2:int, arg3:[any /*short*/], arg4:int, arg5:int ):int;
	compareUnsigned( arg0:bytearray, arg1:bytearray ):int;
	compareUnsigned( arg0:[any /*short*/], arg1:[any /*short*/] ):int;
	compareUnsigned( arg0:bytearray, arg1:int, arg2:int, arg3:bytearray, arg4:int, arg5:int ):int;
	compareUnsigned( arg0:[long], arg1:[long] ):int;
	compareUnsigned( arg0:[long], arg1:int, arg2:int, arg3:[long], arg4:int, arg5:int ):int;
	compareUnsigned( arg0:[int], arg1:int, arg2:int, arg3:[int], arg4:int, arg5:int ):int;
	compareUnsigned( arg0:[int], arg1:[int] ):int;
	copyOf<T,U>( arg0:[U], arg1:int, arg2:java.lang.Class<[T]> ):[T];
	copyOf( arg0:[int], arg1:int ):[int];
	copyOf( arg0:bytearray, arg1:int ):bytearray;
	copyOf( arg0:[float], arg1:int ):[float];
	copyOf( arg0:[long], arg1:int ):[long];
	copyOf( arg0:[any /*short*/], arg1:int ):[any /*short*/];
	copyOf( arg0:chararray, arg1:int ):chararray;
	copyOf( arg0:[boolean], arg1:int ):[boolean];
	copyOf( arg0:[double], arg1:int ):[double];
	copyOf<T>( arg0:[T], arg1:int ):[T];
	copyOfRange( arg0:bytearray, arg1:int, arg2:int ):bytearray;
	copyOfRange( arg0:[any /*short*/], arg1:int, arg2:int ):[any /*short*/];
	copyOfRange( arg0:[long], arg1:int, arg2:int ):[long];
	copyOfRange( arg0:[boolean], arg1:int, arg2:int ):[boolean];
	copyOfRange( arg0:[float], arg1:int, arg2:int ):[float];
	copyOfRange( arg0:chararray, arg1:int, arg2:int ):chararray;
	copyOfRange( arg0:[int], arg1:int, arg2:int ):[int];
	copyOfRange<T,U>( arg0:[U], arg1:int, arg2:int, arg3:java.lang.Class<[T]> ):[T];
	copyOfRange<T>( arg0:[T], arg1:int, arg2:int ):[T];
	copyOfRange( arg0:[double], arg1:int, arg2:int ):[double];
	deepEquals( arg0:[any /*java.lang.Object*/], arg1:[any /*java.lang.Object*/] ):boolean;
	deepHashCode( arg0:[any /*java.lang.Object*/] ):int;
	deepToString( arg0:[any /*java.lang.Object*/] ):string;
	equals( arg0:[double], arg1:[double] ):boolean;
	equals( arg0:[float], arg1:[float] ):boolean;
	equals( arg0:[double], arg1:int, arg2:int, arg3:[double], arg4:int, arg5:int ):boolean;
	equals( arg0:[int], arg1:int, arg2:int, arg3:[int], arg4:int, arg5:int ):boolean;
	equals( arg0:[long], arg1:int, arg2:int, arg3:[long], arg4:int, arg5:int ):boolean;
	equals( arg0:chararray, arg1:int, arg2:int, arg3:chararray, arg4:int, arg5:int ):boolean;
	equals( arg0:[boolean], arg1:int, arg2:int, arg3:[boolean], arg4:int, arg5:int ):boolean;
	equals( arg0:[boolean], arg1:[boolean] ):boolean;
	equals( arg0:[any /*short*/], arg1:int, arg2:int, arg3:[any /*short*/], arg4:int, arg5:int ):boolean;
	equals<T>( arg0:[T], arg1:int, arg2:int, arg3:[T], arg4:int, arg5:int, arg6:any /*java.util.Comparator*/ ):boolean;
	equals<T>( arg0:[T], arg1:[T], arg2:any /*java.util.Comparator*/ ):boolean;
	equals( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int, arg3:[any /*java.lang.Object*/], arg4:int, arg5:int ):boolean;
	equals( arg0:[long], arg1:[long] ):boolean;
	equals( arg0:bytearray, arg1:int, arg2:int, arg3:bytearray, arg4:int, arg5:int ):boolean;
	equals( arg0:bytearray, arg1:bytearray ):boolean;
	equals( arg0:[any /*java.lang.Object*/], arg1:[any /*java.lang.Object*/] ):boolean;
	equals( arg0:[float], arg1:int, arg2:int, arg3:[float], arg4:int, arg5:int ):boolean;
	equals( arg0:[int], arg1:[int] ):boolean;
	equals( arg0:chararray, arg1:chararray ):boolean;
	equals( arg0:[any /*short*/], arg1:[any /*short*/] ):boolean;
	fill( arg0:[int], arg1:int ):void;
	fill( arg0:[long], arg1:long ):void;
	fill( arg0:[long], arg1:int, arg2:int, arg3:long ):void;
	fill( arg0:[int], arg1:int, arg2:int, arg3:int ):void;
	fill( arg0:[float], arg1:int, arg2:int, arg3:float ):void;
	fill( arg0:[any /*java.lang.Object*/], arg1:any /*java.lang.Object*/ ):void;
	fill( arg0:bytearray, arg1:any /*byte*/ ):void;
	fill( arg0:[float], arg1:float ):void;
	fill( arg0:chararray, arg1:int, arg2:int, arg3:any /*char*/ ):void;
	fill( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int, arg3:any /*java.lang.Object*/ ):void;
	fill( arg0:chararray, arg1:any /*char*/ ):void;
	fill( arg0:[double], arg1:double ):void;
	fill( arg0:bytearray, arg1:int, arg2:int, arg3:any /*byte*/ ):void;
	fill( arg0:[double], arg1:int, arg2:int, arg3:double ):void;
	fill( arg0:[boolean], arg1:boolean ):void;
	fill( arg0:[boolean], arg1:int, arg2:int, arg3:boolean ):void;
	fill( arg0:[any /*short*/], arg1:any /*short*/ ):void;
	fill( arg0:[any /*short*/], arg1:int, arg2:int, arg3:any /*short*/ ):void;
	hashCode( arg0:[boolean] ):int;
	hashCode( arg0:[float] ):int;
	hashCode( arg0:bytearray ):int;
	hashCode( arg0:[double] ):int;
	hashCode( arg0:[any /*java.lang.Object*/] ):int;
	hashCode( arg0:[long] ):int;
	hashCode( arg0:[int] ):int;
	hashCode( arg0:[any /*short*/] ):int;
	hashCode( arg0:chararray ):int;
	mismatch( arg0:chararray, arg1:chararray ):int;
	mismatch( arg0:[double], arg1:[double] ):int;
	mismatch( arg0:[double], arg1:int, arg2:int, arg3:[double], arg4:int, arg5:int ):int;
	mismatch( arg0:[boolean], arg1:int, arg2:int, arg3:[boolean], arg4:int, arg5:int ):int;
	mismatch( arg0:[any /*short*/], arg1:[any /*short*/] ):int;
	mismatch( arg0:[float], arg1:int, arg2:int, arg3:[float], arg4:int, arg5:int ):int;
	mismatch( arg0:chararray, arg1:int, arg2:int, arg3:chararray, arg4:int, arg5:int ):int;
	mismatch( arg0:bytearray, arg1:int, arg2:int, arg3:bytearray, arg4:int, arg5:int ):int;
	mismatch( arg0:[any /*java.lang.Object*/], arg1:[any /*java.lang.Object*/] ):int;
	mismatch( arg0:[float], arg1:[float] ):int;
	mismatch( arg0:[any /*short*/], arg1:int, arg2:int, arg3:[any /*short*/], arg4:int, arg5:int ):int;
	mismatch( arg0:[long], arg1:int, arg2:int, arg3:[long], arg4:int, arg5:int ):int;
	mismatch<T>( arg0:[T], arg1:int, arg2:int, arg3:[T], arg4:int, arg5:int, arg6:any /*java.util.Comparator*/ ):int;
	mismatch<T>( arg0:[T], arg1:[T], arg2:any /*java.util.Comparator*/ ):int;
	mismatch( arg0:[boolean], arg1:[boolean] ):int;
	mismatch( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int, arg3:[any /*java.lang.Object*/], arg4:int, arg5:int ):int;
	mismatch( arg0:[int], arg1:int, arg2:int, arg3:[int], arg4:int, arg5:int ):int;
	mismatch( arg0:[int], arg1:[int] ):int;
	mismatch( arg0:bytearray, arg1:bytearray ):int;
	mismatch( arg0:[long], arg1:[long] ):int;
	parallelPrefix( arg0:[int], arg1:any /*java.util.function.IntBinaryOperator*/ ):void;
	parallelPrefix( arg0:[double], arg1:any /*java.util.function.DoubleBinaryOperator*/ ):void;
	parallelPrefix( arg0:[double], arg1:int, arg2:int, arg3:any /*java.util.function.DoubleBinaryOperator*/ ):void;
	parallelPrefix( arg0:[int], arg1:int, arg2:int, arg3:any /*java.util.function.IntBinaryOperator*/ ):void;
	parallelPrefix<T>( arg0:[T], arg1:BinaryOperator<T> ):void;
	parallelPrefix( arg0:[long], arg1:int, arg2:int, arg3:any /*java.util.function.LongBinaryOperator*/ ):void;
	parallelPrefix( arg0:[long], arg1:any /*java.util.function.LongBinaryOperator*/ ):void;
	parallelPrefix<T>( arg0:[T], arg1:int, arg2:int, arg3:BinaryOperator<T> ):void;
	parallelSetAll<T>( arg0:[T], arg1:any /*java.util.function.IntFunction*/ ):void;
	parallelSetAll( arg0:[int], arg1:any /*java.util.function.IntUnaryOperator*/ ):void;
	parallelSetAll( arg0:[double], arg1:any /*java.util.function.IntToDoubleFunction*/ ):void;
	parallelSetAll( arg0:[long], arg1:any /*java.util.function.IntToLongFunction*/ ):void;
	parallelSort<T>( arg0:[T] ):void;
	parallelSort( arg0:chararray, arg1:int, arg2:int ):void;
	parallelSort( arg0:[int], arg1:int, arg2:int ):void;
	parallelSort( arg0:[double], arg1:int, arg2:int ):void;
	parallelSort( arg0:[float] ):void;
	parallelSort( arg0:chararray ):void;
	parallelSort( arg0:[any /*short*/], arg1:int, arg2:int ):void;
	parallelSort( arg0:[any /*short*/] ):void;
	parallelSort( arg0:[float], arg1:int, arg2:int ):void;
	parallelSort( arg0:[long], arg1:int, arg2:int ):void;
	parallelSort<T>( arg0:[T], arg1:any /*java.util.Comparator*/ ):void;
	parallelSort( arg0:bytearray ):void;
	parallelSort<T>( arg0:[T], arg1:int, arg2:int ):void;
	parallelSort<T>( arg0:[T], arg1:int, arg2:int, arg3:any /*java.util.Comparator*/ ):void;
	parallelSort( arg0:[double] ):void;
	parallelSort( arg0:bytearray, arg1:int, arg2:int ):void;
	parallelSort( arg0:[long] ):void;
	parallelSort( arg0:[int] ):void;
	setAll( arg0:[double], arg1:any /*java.util.function.IntToDoubleFunction*/ ):void;
	setAll( arg0:[int], arg1:any /*java.util.function.IntUnaryOperator*/ ):void;
	setAll( arg0:[long], arg1:any /*java.util.function.IntToLongFunction*/ ):void;
	setAll<T>( arg0:[T], arg1:any /*java.util.function.IntFunction*/ ):void;
	sort( arg0:[int], arg1:int, arg2:int ):void;
	sort( arg0:[long] ):void;
	sort<T>( arg0:[T], arg1:int, arg2:int, arg3:any /*java.util.Comparator*/ ):void;
	sort<T>( arg0:[T], arg1:any /*java.util.Comparator*/ ):void;
	sort( arg0:[float] ):void;
	sort( arg0:[long], arg1:int, arg2:int ):void;
	sort( arg0:bytearray ):void;
	sort( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int ):void;
	sort( arg0:chararray, arg1:int, arg2:int ):void;
	sort( arg0:[any /*short*/] ):void;
	sort( arg0:[float], arg1:int, arg2:int ):void;
	sort( arg0:[any /*short*/], arg1:int, arg2:int ):void;
	sort( arg0:chararray ):void;
	sort( arg0:[int] ):void;
	sort( arg0:bytearray, arg1:int, arg2:int ):void;
	sort( arg0:[double], arg1:int, arg2:int ):void;
	sort( arg0:[double] ):void;
	sort( arg0:[any /*java.lang.Object*/] ):void;
	spliterator( arg0:[long] ):any /*java.util.Spliterator$OfLong*/;
	spliterator( arg0:[long], arg1:int, arg2:int ):any /*java.util.Spliterator$OfLong*/;
	spliterator( arg0:[double] ):any /*java.util.Spliterator$OfDouble*/;
	spliterator( arg0:[double], arg1:int, arg2:int ):any /*java.util.Spliterator$OfDouble*/;
	spliterator<T>( arg0:[T] ):any /*java.util.Spliterator*/;
	spliterator<T>( arg0:[T], arg1:int, arg2:int ):any /*java.util.Spliterator*/;
	spliterator( arg0:[int] ):any /*java.util.Spliterator$OfInt*/;
	spliterator( arg0:[int], arg1:int, arg2:int ):any /*java.util.Spliterator$OfInt*/;
	stream( arg0:[long], arg1:int, arg2:int ):any /*java.util.stream.LongStream*/;
	stream( arg0:[double] ):any /*java.util.stream.DoubleStream*/;
	stream( arg0:[double], arg1:int, arg2:int ):any /*java.util.stream.DoubleStream*/;
	stream( arg0:[int] ):any /*java.util.stream.IntStream*/;
	stream<T>( arg0:[T] ):java.util.stream.Stream<T>;
	stream<T>( arg0:[T], arg1:int, arg2:int ):java.util.stream.Stream<T>;
	stream( arg0:[int], arg1:int, arg2:int ):any /*java.util.stream.IntStream*/;
	stream( arg0:[long] ):any /*java.util.stream.LongStream*/;
	toString( arg0:[any /*java.lang.Object*/] ):string;
	toString( arg0:[boolean] ):string;
	toString( arg0:bytearray ):string;
	toString( arg0:chararray ):string;
	toString( arg0:[any /*short*/] ):string;
	toString( arg0:[int] ):string;
	toString( arg0:[long] ):string;
	toString( arg0:[double] ):string;
	toString( arg0:[float] ):string;
}

export const Arrays: ArraysStatic = Java.type("java.util.Arrays");


interface ConcurrentHashMapStatic {

	readonly class:any;
	new<K,V>(  ):java.util.concurrent.ConcurrentHashMap<K, V>;
	new<K,V>( arg0:int ):java.util.concurrent.ConcurrentHashMap<K, V>;
	new<K,V>( arg0:java.util.Map<K, V> ):java.util.concurrent.ConcurrentHashMap<K, V>;
	new<K,V>( arg0:int, arg1:float, arg2:int ):java.util.concurrent.ConcurrentHashMap<K, V>;
	new<K,V>( arg0:int, arg1:float ):java.util.concurrent.ConcurrentHashMap<K, V>;
	newKeySet( arg0:int ):any /*java.util.concurrent.ConcurrentHashMap$KeySetView*/;
	newKeySet(  ):any /*java.util.concurrent.ConcurrentHashMap$KeySetView*/;
}

export const ConcurrentHashMap: ConcurrentHashMapStatic = Java.type("java.util.concurrent.ConcurrentHashMap");


interface CallableStatic {

	readonly class:any;
	new<V>( arg0:java.util.concurrent.Callable<V> ):java.util.concurrent.Callable<V>;
}

export const Callable: CallableStatic = Java.type("java.util.concurrent.Callable");


interface IterableStatic {

	readonly class:any;
	new<T>( arg0:java.lang.Iterable<T> ):java.lang.Iterable<T>;
}

export const Iterable: IterableStatic = Java.type("java.lang.Iterable");


interface ExecutorStatic {

	readonly class:any;
	new( arg0:java.util.concurrent.Executor ):java.util.concurrent.Executor;
}

export const Executor: ExecutorStatic = Java.type("java.util.concurrent.Executor");


interface OptionalStatic {

	readonly class:any;
	empty<T>(  ):java.util.Optional<T>;
	of<T>( arg0:T ):java.util.Optional<T>;
	ofNullable<T>( arg0:T ):java.util.Optional<T>;
}

export const Optional: OptionalStatic = Java.type("java.util.Optional");


interface CompletableFutureStatic {

	readonly class:any;
	new<T>(  ):java.util.concurrent.CompletableFuture<T>;
	allOf( ...arg0:java.util.concurrent.CompletableFuture<any /*java.lang.Object*/>[] ):java.util.concurrent.CompletableFuture<void>;
	anyOf( ...arg0:java.util.concurrent.CompletableFuture<any /*java.lang.Object*/>[] ):java.util.concurrent.CompletableFuture<any /*java.lang.Object*/>;
	completedFuture<U>( arg0:U ):java.util.concurrent.CompletableFuture<U>;
	completedStage<U>( arg0:U ):any /*java.util.concurrent.CompletionStage*/;
	delayedExecutor( arg0:long, arg1:any /*java.util.concurrent.TimeUnit*/ ):java.util.concurrent.Executor;
	delayedExecutor( arg0:long, arg1:any /*java.util.concurrent.TimeUnit*/, arg2:java.util.concurrent.Executor ):java.util.concurrent.Executor;
	failedFuture<U>( arg0:java.lang.Throwable ):java.util.concurrent.CompletableFuture<U>;
	failedStage( arg0:java.lang.Throwable ):any /*java.util.concurrent.CompletionStage*/;
	runAsync( arg0:java.lang.Runnable ):java.util.concurrent.CompletableFuture<void>;
	runAsync( arg0:java.lang.Runnable, arg1:java.util.concurrent.Executor ):java.util.concurrent.CompletableFuture<void>;
	supplyAsync<U>( arg0:Supplier<U> ):java.util.concurrent.CompletableFuture<U>;
	supplyAsync<U>( arg0:Supplier<U>, arg1:java.util.concurrent.Executor ):java.util.concurrent.CompletableFuture<U>;
}

export const CompletableFuture: CompletableFutureStatic = Java.type("java.util.concurrent.CompletableFuture");


