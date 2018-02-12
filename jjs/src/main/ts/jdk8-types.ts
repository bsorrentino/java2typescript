//
// EXPORT DECLARATIONS
// 
//

/// <reference path="jdk8.d.ts"/>

interface CollectionsStatic {

	readonly class:any;
	addAll( arg0:java.util.Collection<any/*T*/>, ...arg1:any /*java.lang.Object*/[] ):boolean;
	asLifoQueue<E>( arg0:any /*java.util.Deque*/ ):any /*java.util.Queue*/;
	binarySearch( arg0:java.util.List<any/*T*/>, arg1:any/*T*/, arg2:Comparator<any/*T*/> ):int;
	binarySearch( arg0:java.util.List<java.lang.Comparable<any/*T*/>>, arg1:any/*T*/ ):int;
	checkedCollection<E>( arg0:java.util.Collection<any>, arg1:java.lang.Class<any> ):java.util.Collection<any>;
	checkedList<E>( arg0:java.util.List<any>, arg1:java.lang.Class<any> ):java.util.List<any>;
	checkedMap<K,V>( arg0:java.util.Map<any, any>, arg1:java.lang.Class<any>, arg2:java.lang.Class<any> ):java.util.Map<any, any>;
	checkedNavigableMap<K,V>( arg0:any /*java.util.NavigableMap*/, arg1:java.lang.Class<any>, arg2:java.lang.Class<any> ):any /*java.util.NavigableMap*/;
	checkedNavigableSet<E>( arg0:any /*java.util.NavigableSet*/, arg1:java.lang.Class<any> ):any /*java.util.NavigableSet*/;
	checkedQueue<E>( arg0:any /*java.util.Queue*/, arg1:java.lang.Class<any> ):any /*java.util.Queue*/;
	checkedSet<E>( arg0:java.util.Set<any>, arg1:java.lang.Class<any> ):java.util.Set<any>;
	checkedSortedMap<K,V>( arg0:any /*java.util.SortedMap*/, arg1:java.lang.Class<any>, arg2:java.lang.Class<any> ):any /*java.util.SortedMap*/;
	checkedSortedSet<E>( arg0:any /*java.util.SortedSet*/, arg1:java.lang.Class<any> ):any /*java.util.SortedSet*/;
	copy( arg0:java.util.List<any/*T*/>, arg1:java.util.List<any/*T*/> ):void;
	disjoint( arg0:java.util.Collection<any /*java.lang.Object*/>, arg1:java.util.Collection<any /*java.lang.Object*/> ):boolean;
	emptyEnumeration<E>(  ):any /*java.util.Enumeration*/;
	emptyIterator<E>(  ):java.util.Iterator<any>;
	emptyList<E>(  ):java.util.List<any>;
	emptyListIterator<E>(  ):any /*java.util.ListIterator*/;
	emptyMap<K,V>(  ):java.util.Map<any, any>;
	emptyNavigableMap<K,V>(  ):any /*java.util.NavigableMap*/;
	emptyNavigableSet<E>(  ):any /*java.util.NavigableSet*/;
	emptySet<E>(  ):java.util.Set<any>;
	emptySortedMap<K,V>(  ):any /*java.util.SortedMap*/;
	emptySortedSet<E>(  ):any /*java.util.SortedSet*/;
	enumeration<E>( arg0:java.util.Collection<any> ):any /*java.util.Enumeration*/;
	fill( arg0:java.util.List<any/*T*/>, arg1:any/*T*/ ):void;
	frequency( arg0:java.util.Collection<any /*java.lang.Object*/>, arg1:any /*java.lang.Object*/ ):int;
	indexOfSubList( arg0:java.util.List<any /*java.lang.Object*/>, arg1:java.util.List<any /*java.lang.Object*/> ):int;
	lastIndexOfSubList( arg0:java.util.List<any /*java.lang.Object*/>, arg1:java.util.List<any /*java.lang.Object*/> ):int;
	list<E>( arg0:any /*java.util.Enumeration*/ ):java.util.ArrayList<any>;
	max( arg0:java.util.Collection<any/*T*/> ):any/*T*/;
	max( arg0:java.util.Collection<any/*T*/>, arg1:Comparator<any/*T*/> ):any/*T*/;
	min( arg0:java.util.Collection<any/*T*/> ):any/*T*/;
	min( arg0:java.util.Collection<any/*T*/>, arg1:Comparator<any/*T*/> ):any/*T*/;
	nCopies<E>( arg0:int, arg1:any/*T*/ ):java.util.List<any>;
	newSetFromMap<E>( arg0:java.util.Map<any, boolean|null> ):java.util.Set<any>;
	replaceAll( arg0:java.util.List<any>, arg1:any/*T*/, arg2:any/*T*/ ):boolean;
	reverse( arg0:java.util.List<any /*java.lang.Object*/> ):void;
	reverseOrder<T>(  ):Comparator<any>;
	reverseOrder<T>( arg0:Comparator<any> ):Comparator<any>;
	rotate( arg0:java.util.List<any /*java.lang.Object*/>, arg1:int ):void;
	shuffle( arg0:java.util.List<any /*java.lang.Object*/> ):void;
	shuffle( arg0:java.util.List<any /*java.lang.Object*/>, arg1:any /*java.util.Random*/ ):void;
	singleton<E>( arg0:any/*T*/ ):java.util.Set<any>;
	singletonList<E>( arg0:any/*T*/ ):java.util.List<any>;
	singletonMap<K,V>( arg0:any/*K*/, arg1:any/*V*/ ):java.util.Map<any, any>;
	sort( arg0:java.util.List<any> ):void;
	sort( arg0:java.util.List<any>, arg1:Comparator<any/*T*/> ):void;
	swap( arg0:java.util.List<any /*java.lang.Object*/>, arg1:int, arg2:int ):void;
	synchronizedCollection<E>( arg0:java.util.Collection<any> ):java.util.Collection<any>;
	synchronizedList<E>( arg0:java.util.List<any> ):java.util.List<any>;
	synchronizedMap<K,V>( arg0:java.util.Map<any, any> ):java.util.Map<any, any>;
	synchronizedNavigableMap<K,V>( arg0:any /*java.util.NavigableMap*/ ):any /*java.util.NavigableMap*/;
	synchronizedNavigableSet<E>( arg0:any /*java.util.NavigableSet*/ ):any /*java.util.NavigableSet*/;
	synchronizedSet<E>( arg0:java.util.Set<any> ):java.util.Set<any>;
	synchronizedSortedMap<K,V>( arg0:any /*java.util.SortedMap*/ ):any /*java.util.SortedMap*/;
	synchronizedSortedSet<E>( arg0:any /*java.util.SortedSet*/ ):any /*java.util.SortedSet*/;
	unmodifiableCollection<E>( arg0:java.util.Collection<any/*T*/> ):java.util.Collection<any>;
	unmodifiableList<E>( arg0:java.util.List<any/*T*/> ):java.util.List<any>;
	unmodifiableMap<K,V>( arg0:java.util.Map<any/*K*/, any/*V*/> ):java.util.Map<any, any>;
	unmodifiableNavigableMap<K,V>( arg0:any /*java.util.NavigableMap*/ ):any /*java.util.NavigableMap*/;
	unmodifiableNavigableSet<E>( arg0:any /*java.util.NavigableSet*/ ):any /*java.util.NavigableSet*/;
	unmodifiableSet<E>( arg0:java.util.Set<any/*T*/> ):java.util.Set<any>;
	unmodifiableSortedMap<K,V>( arg0:any /*java.util.SortedMap*/ ):any /*java.util.SortedMap*/;
	unmodifiableSortedSet<E>( arg0:any /*java.util.SortedSet*/ ):any /*java.util.SortedSet*/;
}

export const Collections: CollectionsStatic = Java.type("java.util.Collections");


interface URIStatic {

	readonly class:any;
	create( arg0:string ):java.net.URI;
}

export const URI: URIStatic = Java.type("java.net.URI");


interface StreamStatic {

	readonly class:any;
	builder<T>(  ):any /*java.util.stream.Stream$Builder*/;
	concat<T>( arg0:java.util.stream.Stream<T>, arg1:java.util.stream.Stream<T> ):java.util.stream.Stream<T>;
	empty<T>(  ):java.util.stream.Stream<T>;
	generate<T>( arg0:Supplier<T> ):java.util.stream.Stream<T>;
	iterate<T>( arg0:T, arg1:UnaryOperator<T> ):java.util.stream.Stream<T>;
	of<T>( ...arg0:any /*java.lang.Object*/[] ):java.util.stream.Stream<T>;
	of<T>( arg0:T ):java.util.stream.Stream<T>;
}

export const Stream: StreamStatic = Java.type("java.util.stream.Stream");


interface ArraysStatic {

	readonly class:any;
	asList<E>( ...arg0:any /*java.lang.Object*/[] ):java.util.List<any>;
	binarySearch( arg0:[any /*java.lang.Object*/], arg1:any /*java.lang.Object*/ ):int;
	binarySearch( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int, arg3:any /*java.lang.Object*/ ):int;
	binarySearch( arg0:[any /*short*/], arg1:any /*short*/ ):int;
	binarySearch( arg0:[any /*short*/], arg1:int, arg2:int, arg3:any /*short*/ ):int;
	binarySearch( arg0:[any/*T*/], arg1:any/*T*/, arg2:Comparator<any/*T*/> ):int;
	binarySearch( arg0:[any/*T*/], arg1:int, arg2:int, arg3:any/*T*/, arg4:Comparator<any/*T*/> ):int;
	binarySearch( arg0:[double], arg1:double ):int;
	binarySearch( arg0:[double], arg1:int, arg2:int, arg3:double ):int;
	binarySearch( arg0:[float], arg1:float ):int;
	binarySearch( arg0:[float], arg1:int, arg2:int, arg3:float ):int;
	binarySearch( arg0:[int], arg1:int ):int;
	binarySearch( arg0:[int], arg1:int, arg2:int, arg3:int ):int;
	binarySearch( arg0:[long], arg1:int, arg2:int, arg3:long ):int;
	binarySearch( arg0:[long], arg1:long ):int;
	binarySearch( arg0:bytearray, arg1:any /*byte*/ ):int;
	binarySearch( arg0:bytearray, arg1:int, arg2:int, arg3:any /*byte*/ ):int;
	binarySearch( arg0:chararray, arg1:any /*char*/ ):int;
	binarySearch( arg0:chararray, arg1:int, arg2:int, arg3:any /*char*/ ):int;
	copyOf( arg0:[any /*short*/], arg1:int ):[any /*short*/];
	copyOf( arg0:[any/*T*/], arg1:int ):[any/*T*/];
	copyOf( arg0:[any/*U*/], arg1:int, arg2:java.lang.Class<[any/*T*/]> ):[any/*T*/];
	copyOf( arg0:[boolean], arg1:int ):[boolean];
	copyOf( arg0:[double], arg1:int ):[double];
	copyOf( arg0:[float], arg1:int ):[float];
	copyOf( arg0:[int], arg1:int ):[int];
	copyOf( arg0:[long], arg1:int ):[long];
	copyOf( arg0:bytearray, arg1:int ):bytearray;
	copyOf( arg0:chararray, arg1:int ):chararray;
	copyOfRange( arg0:[any /*short*/], arg1:int, arg2:int ):[any /*short*/];
	copyOfRange( arg0:[any/*T*/], arg1:int, arg2:int ):[any/*T*/];
	copyOfRange( arg0:[any/*U*/], arg1:int, arg2:int, arg3:java.lang.Class<[any/*T*/]> ):[any/*T*/];
	copyOfRange( arg0:[boolean], arg1:int, arg2:int ):[boolean];
	copyOfRange( arg0:[double], arg1:int, arg2:int ):[double];
	copyOfRange( arg0:[float], arg1:int, arg2:int ):[float];
	copyOfRange( arg0:[int], arg1:int, arg2:int ):[int];
	copyOfRange( arg0:[long], arg1:int, arg2:int ):[long];
	copyOfRange( arg0:bytearray, arg1:int, arg2:int ):bytearray;
	copyOfRange( arg0:chararray, arg1:int, arg2:int ):chararray;
	deepEquals( arg0:[any /*java.lang.Object*/], arg1:[any /*java.lang.Object*/] ):boolean;
	deepHashCode( arg0:[any /*java.lang.Object*/] ):int;
	deepToString( arg0:[any /*java.lang.Object*/] ):string;
	equals( arg0:[any /*java.lang.Object*/], arg1:[any /*java.lang.Object*/] ):boolean;
	equals( arg0:[any /*short*/], arg1:[any /*short*/] ):boolean;
	equals( arg0:[boolean], arg1:[boolean] ):boolean;
	equals( arg0:[double], arg1:[double] ):boolean;
	equals( arg0:[float], arg1:[float] ):boolean;
	equals( arg0:[int], arg1:[int] ):boolean;
	equals( arg0:[long], arg1:[long] ):boolean;
	equals( arg0:bytearray, arg1:bytearray ):boolean;
	equals( arg0:chararray, arg1:chararray ):boolean;
	fill( arg0:[any /*java.lang.Object*/], arg1:any /*java.lang.Object*/ ):void;
	fill( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int, arg3:any /*java.lang.Object*/ ):void;
	fill( arg0:[any /*short*/], arg1:any /*short*/ ):void;
	fill( arg0:[any /*short*/], arg1:int, arg2:int, arg3:any /*short*/ ):void;
	fill( arg0:[boolean], arg1:boolean ):void;
	fill( arg0:[boolean], arg1:int, arg2:int, arg3:boolean ):void;
	fill( arg0:[double], arg1:double ):void;
	fill( arg0:[double], arg1:int, arg2:int, arg3:double ):void;
	fill( arg0:[float], arg1:float ):void;
	fill( arg0:[float], arg1:int, arg2:int, arg3:float ):void;
	fill( arg0:[int], arg1:int ):void;
	fill( arg0:[int], arg1:int, arg2:int, arg3:int ):void;
	fill( arg0:[long], arg1:int, arg2:int, arg3:long ):void;
	fill( arg0:[long], arg1:long ):void;
	fill( arg0:bytearray, arg1:any /*byte*/ ):void;
	fill( arg0:bytearray, arg1:int, arg2:int, arg3:any /*byte*/ ):void;
	fill( arg0:chararray, arg1:any /*char*/ ):void;
	fill( arg0:chararray, arg1:int, arg2:int, arg3:any /*char*/ ):void;
	hashCode( arg0:[any /*java.lang.Object*/] ):int;
	hashCode( arg0:[any /*short*/] ):int;
	hashCode( arg0:[boolean] ):int;
	hashCode( arg0:[double] ):int;
	hashCode( arg0:[float] ):int;
	hashCode( arg0:[int] ):int;
	hashCode( arg0:[long] ):int;
	hashCode( arg0:bytearray ):int;
	hashCode( arg0:chararray ):int;
	parallelPrefix( arg0:[any/*T*/], arg1:any /*java.util.function.BinaryOperator*/ ):void;
	parallelPrefix( arg0:[any/*T*/], arg1:int, arg2:int, arg3:any /*java.util.function.BinaryOperator*/ ):void;
	parallelPrefix( arg0:[double], arg1:any /*java.util.function.DoubleBinaryOperator*/ ):void;
	parallelPrefix( arg0:[double], arg1:int, arg2:int, arg3:any /*java.util.function.DoubleBinaryOperator*/ ):void;
	parallelPrefix( arg0:[int], arg1:any /*java.util.function.IntBinaryOperator*/ ):void;
	parallelPrefix( arg0:[int], arg1:int, arg2:int, arg3:any /*java.util.function.IntBinaryOperator*/ ):void;
	parallelPrefix( arg0:[long], arg1:any /*java.util.function.LongBinaryOperator*/ ):void;
	parallelPrefix( arg0:[long], arg1:int, arg2:int, arg3:any /*java.util.function.LongBinaryOperator*/ ):void;
	parallelSetAll( arg0:[any/*T*/], arg1:any /*java.util.function.IntFunction*/ ):void;
	parallelSetAll( arg0:[double], arg1:any /*java.util.function.IntToDoubleFunction*/ ):void;
	parallelSetAll( arg0:[int], arg1:any /*java.util.function.IntUnaryOperator*/ ):void;
	parallelSetAll( arg0:[long], arg1:any /*java.util.function.IntToLongFunction*/ ):void;
	parallelSort( arg0:[any /*short*/] ):void;
	parallelSort( arg0:[any /*short*/], arg1:int, arg2:int ):void;
	parallelSort( arg0:[any/*T*/] ):void;
	parallelSort( arg0:[any/*T*/], arg1:Comparator<any/*T*/> ):void;
	parallelSort( arg0:[any/*T*/], arg1:int, arg2:int ):void;
	parallelSort( arg0:[any/*T*/], arg1:int, arg2:int, arg3:Comparator<any/*T*/> ):void;
	parallelSort( arg0:[double] ):void;
	parallelSort( arg0:[double], arg1:int, arg2:int ):void;
	parallelSort( arg0:[float] ):void;
	parallelSort( arg0:[float], arg1:int, arg2:int ):void;
	parallelSort( arg0:[int] ):void;
	parallelSort( arg0:[int], arg1:int, arg2:int ):void;
	parallelSort( arg0:[long] ):void;
	parallelSort( arg0:[long], arg1:int, arg2:int ):void;
	parallelSort( arg0:bytearray ):void;
	parallelSort( arg0:bytearray, arg1:int, arg2:int ):void;
	parallelSort( arg0:chararray ):void;
	parallelSort( arg0:chararray, arg1:int, arg2:int ):void;
	setAll( arg0:[any/*T*/], arg1:any /*java.util.function.IntFunction*/ ):void;
	setAll( arg0:[double], arg1:any /*java.util.function.IntToDoubleFunction*/ ):void;
	setAll( arg0:[int], arg1:any /*java.util.function.IntUnaryOperator*/ ):void;
	setAll( arg0:[long], arg1:any /*java.util.function.IntToLongFunction*/ ):void;
	sort( arg0:[any /*java.lang.Object*/] ):void;
	sort( arg0:[any /*java.lang.Object*/], arg1:int, arg2:int ):void;
	sort( arg0:[any /*short*/] ):void;
	sort( arg0:[any /*short*/], arg1:int, arg2:int ):void;
	sort( arg0:[any/*T*/], arg1:Comparator<any/*T*/> ):void;
	sort( arg0:[any/*T*/], arg1:int, arg2:int, arg3:Comparator<any/*T*/> ):void;
	sort( arg0:[double] ):void;
	sort( arg0:[double], arg1:int, arg2:int ):void;
	sort( arg0:[float] ):void;
	sort( arg0:[float], arg1:int, arg2:int ):void;
	sort( arg0:[int] ):void;
	sort( arg0:[int], arg1:int, arg2:int ):void;
	sort( arg0:[long] ):void;
	sort( arg0:[long], arg1:int, arg2:int ):void;
	sort( arg0:bytearray ):void;
	sort( arg0:bytearray, arg1:int, arg2:int ):void;
	sort( arg0:chararray ):void;
	sort( arg0:chararray, arg1:int, arg2:int ):void;
	spliterator( arg0:[double] ):any /*java.util.Spliterator$OfDouble*/;
	spliterator( arg0:[double], arg1:int, arg2:int ):any /*java.util.Spliterator$OfDouble*/;
	spliterator( arg0:[int] ):any /*java.util.Spliterator$OfInt*/;
	spliterator( arg0:[int], arg1:int, arg2:int ):any /*java.util.Spliterator$OfInt*/;
	spliterator( arg0:[long] ):any /*java.util.Spliterator$OfLong*/;
	spliterator( arg0:[long], arg1:int, arg2:int ):any /*java.util.Spliterator$OfLong*/;
	spliterator<T>( arg0:[any/*T*/] ):any /*java.util.Spliterator*/;
	spliterator<T>( arg0:[any/*T*/], arg1:int, arg2:int ):any /*java.util.Spliterator*/;
	stream( arg0:[double] ):any /*java.util.stream.DoubleStream*/;
	stream( arg0:[double], arg1:int, arg2:int ):any /*java.util.stream.DoubleStream*/;
	stream( arg0:[int] ):any /*java.util.stream.IntStream*/;
	stream( arg0:[int], arg1:int, arg2:int ):any /*java.util.stream.IntStream*/;
	stream( arg0:[long] ):any /*java.util.stream.LongStream*/;
	stream( arg0:[long], arg1:int, arg2:int ):any /*java.util.stream.LongStream*/;
	stream<T>( arg0:[any/*T*/] ):java.util.stream.Stream<any>;
	stream<T>( arg0:[any/*T*/], arg1:int, arg2:int ):java.util.stream.Stream<any>;
	toString( arg0:[any /*java.lang.Object*/] ):string;
	toString( arg0:[any /*short*/] ):string;
	toString( arg0:[boolean] ):string;
	toString( arg0:[double] ):string;
	toString( arg0:[float] ):string;
	toString( arg0:[int] ):string;
	toString( arg0:[long] ):string;
	toString( arg0:bytearray ):string;
	toString( arg0:chararray ):string;
}

export const Arrays: ArraysStatic = Java.type("java.util.Arrays");


interface StringStatic {

	readonly class:any;
	copyValueOf( arg0:chararray ):string;
	copyValueOf( arg0:chararray, arg1:int, arg2:int ):string;
	format( arg0:any /*java.util.Locale*/, arg1:string, ...arg2:any /*java.lang.Object*/[] ):string;
	format( arg0:string, ...arg1:any /*java.lang.Object*/[] ):string;
	join( arg0:java.lang.CharSequence, ...arg1:java.lang.CharSequence[] ):string;
	join( arg0:java.lang.CharSequence, arg1:java.lang.Iterable<java.lang.CharSequence> ):string;
	valueOf( arg0:any /*char*/ ):string;
	valueOf( arg0:any /*java.lang.Object*/ ):string;
	valueOf( arg0:boolean ):string;
	valueOf( arg0:chararray ):string;
	valueOf( arg0:chararray, arg1:int, arg2:int ):string;
	valueOf( arg0:double ):string;
	valueOf( arg0:float ):string;
	valueOf( arg0:int ):string;
	valueOf( arg0:long ):string;
}

export const String: StringStatic = Java.type("java.lang.String");


