//
// EXPORT DECLARATIONS
// 
//

/// <reference path="jdk8.d.ts"/>

interface URIStatic {

	readonly class:any;
	create( arg0:string ):java.net.URI;
}

export const URI: URIStatic = Java.type("java.net.URI");


interface StreamStatic {

	readonly class:any;
	builder<T>(  ):any /* java.util.stream.Stream$Builder */;
	concat<T>( arg0:java.util.stream.Stream<T>, arg1:java.util.stream.Stream<T> ):java.util.stream.Stream<T>;
	empty<T>(  ):java.util.stream.Stream<T>;
	generate<T>( arg0:Supplier<T> ):java.util.stream.Stream<T>;
	iterate<T>( arg0:any /* java.lang.Object */, arg1:UnaryOperator<T> ):java.util.stream.Stream<T>;
	of<T>( arg0:[any] /* [Ljava.lang.Object; */ ):java.util.stream.Stream<T>;
	of<T>( arg0:any /* java.lang.Object */ ):java.util.stream.Stream<T>;
}

export const Stream: StreamStatic = Java.type("java.util.stream.Stream");


interface ArraysStatic {

	readonly class:any;
	asList<E>( arg0:[any] /* [Ljava.lang.Object; */ ):java.util.List<any>;
	binarySearch( arg0:[any] /* [D */, arg1:double ):int;
	binarySearch( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:double ):int;
	binarySearch( arg0:[any] /* [F */, arg1:float ):int;
	binarySearch( arg0:[any] /* [F */, arg1:int, arg2:int, arg3:float ):int;
	binarySearch( arg0:[any] /* [I */, arg1:int ):int;
	binarySearch( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:int ):int;
	binarySearch( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:long ):int;
	binarySearch( arg0:[any] /* [J */, arg1:long ):int;
	binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.Object */ ):int;
	binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.Object */, arg2:Comparator<any> ):int;
	binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */ ):int;
	binarySearch( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */, arg4:Comparator<any> ):int;
	binarySearch( arg0:[any] /* [S */, arg1:any /* short */ ):int;
	binarySearch( arg0:[any] /* [S */, arg1:int, arg2:int, arg3:any /* short */ ):int;
	binarySearch( arg0:bytearray, arg1:any /* byte */ ):int;
	binarySearch( arg0:bytearray, arg1:int, arg2:int, arg3:any /* byte */ ):int;
	binarySearch( arg0:chararray, arg1:any /* char */ ):int;
	binarySearch( arg0:chararray, arg1:int, arg2:int, arg3:any /* char */ ):int;
	copyOf( arg0:[any] /* [D */, arg1:int ):[any] /* [D */;
	copyOf( arg0:[any] /* [F */, arg1:int ):[any] /* [F */;
	copyOf( arg0:[any] /* [I */, arg1:int ):[any] /* [I */;
	copyOf( arg0:[any] /* [J */, arg1:int ):[any] /* [J */;
	copyOf( arg0:[any] /* [Ljava.lang.Object; */, arg1:int ):[any] /* [Ljava.lang.Object; */;
	copyOf( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:java.lang.Class<any> ):[any] /* [Ljava.lang.Object; */;
	copyOf( arg0:[any] /* [S */, arg1:int ):[any] /* [S */;
	copyOf( arg0:[any] /* [Z */, arg1:int ):[any] /* [Z */;
	copyOf( arg0:bytearray, arg1:int ):bytearray;
	copyOf( arg0:chararray, arg1:int ):chararray;
	copyOfRange( arg0:[any] /* [D */, arg1:int, arg2:int ):[any] /* [D */;
	copyOfRange( arg0:[any] /* [F */, arg1:int, arg2:int ):[any] /* [F */;
	copyOfRange( arg0:[any] /* [I */, arg1:int, arg2:int ):[any] /* [I */;
	copyOfRange( arg0:[any] /* [J */, arg1:int, arg2:int ):[any] /* [J */;
	copyOfRange( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):[any] /* [Ljava.lang.Object; */;
	copyOfRange( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:java.lang.Class<any> ):[any] /* [Ljava.lang.Object; */;
	copyOfRange( arg0:[any] /* [S */, arg1:int, arg2:int ):[any] /* [S */;
	copyOfRange( arg0:[any] /* [Z */, arg1:int, arg2:int ):[any] /* [Z */;
	copyOfRange( arg0:bytearray, arg1:int, arg2:int ):bytearray;
	copyOfRange( arg0:chararray, arg1:int, arg2:int ):chararray;
	deepEquals( arg0:[any] /* [Ljava.lang.Object; */, arg1:[any] /* [Ljava.lang.Object; */ ):boolean;
	deepHashCode( arg0:[any] /* [Ljava.lang.Object; */ ):int;
	deepToString( arg0:[any] /* [Ljava.lang.Object; */ ):string;
	equals( arg0:[any] /* [D */, arg1:[any] /* [D */ ):boolean;
	equals( arg0:[any] /* [F */, arg1:[any] /* [F */ ):boolean;
	equals( arg0:[any] /* [I */, arg1:[any] /* [I */ ):boolean;
	equals( arg0:[any] /* [J */, arg1:[any] /* [J */ ):boolean;
	equals( arg0:[any] /* [Ljava.lang.Object; */, arg1:[any] /* [Ljava.lang.Object; */ ):boolean;
	equals( arg0:[any] /* [S */, arg1:[any] /* [S */ ):boolean;
	equals( arg0:[any] /* [Z */, arg1:[any] /* [Z */ ):boolean;
	equals( arg0:bytearray, arg1:bytearray ):boolean;
	equals( arg0:chararray, arg1:chararray ):boolean;
	fill( arg0:[any] /* [D */, arg1:double ):void;
	fill( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:double ):void;
	fill( arg0:[any] /* [F */, arg1:float ):void;
	fill( arg0:[any] /* [F */, arg1:int, arg2:int, arg3:float ):void;
	fill( arg0:[any] /* [I */, arg1:int ):void;
	fill( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:int ):void;
	fill( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:long ):void;
	fill( arg0:[any] /* [J */, arg1:long ):void;
	fill( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.lang.Object */ ):void;
	fill( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.lang.Object */ ):void;
	fill( arg0:[any] /* [S */, arg1:any /* short */ ):void;
	fill( arg0:[any] /* [S */, arg1:int, arg2:int, arg3:any /* short */ ):void;
	fill( arg0:[any] /* [Z */, arg1:boolean ):void;
	fill( arg0:[any] /* [Z */, arg1:int, arg2:int, arg3:boolean ):void;
	fill( arg0:bytearray, arg1:any /* byte */ ):void;
	fill( arg0:bytearray, arg1:int, arg2:int, arg3:any /* byte */ ):void;
	fill( arg0:chararray, arg1:any /* char */ ):void;
	fill( arg0:chararray, arg1:int, arg2:int, arg3:any /* char */ ):void;
	hashCode( arg0:[any] /* [D */ ):int;
	hashCode( arg0:[any] /* [F */ ):int;
	hashCode( arg0:[any] /* [I */ ):int;
	hashCode( arg0:[any] /* [J */ ):int;
	hashCode( arg0:[any] /* [Ljava.lang.Object; */ ):int;
	hashCode( arg0:[any] /* [S */ ):int;
	hashCode( arg0:[any] /* [Z */ ):int;
	hashCode( arg0:bytearray ):int;
	hashCode( arg0:chararray ):int;
	parallelPrefix( arg0:[any] /* [D */, arg1:any /* java.util.function.DoubleBinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [D */, arg1:int, arg2:int, arg3:any /* java.util.function.DoubleBinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [I */, arg1:any /* java.util.function.IntBinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [I */, arg1:int, arg2:int, arg3:any /* java.util.function.IntBinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [J */, arg1:any /* java.util.function.LongBinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [J */, arg1:int, arg2:int, arg3:any /* java.util.function.LongBinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.util.function.BinaryOperator */ ):void;
	parallelPrefix( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:any /* java.util.function.BinaryOperator */ ):void;
	parallelSetAll( arg0:[any] /* [D */, arg1:any /* java.util.function.IntToDoubleFunction */ ):void;
	parallelSetAll( arg0:[any] /* [I */, arg1:any /* java.util.function.IntUnaryOperator */ ):void;
	parallelSetAll( arg0:[any] /* [J */, arg1:any /* java.util.function.IntToLongFunction */ ):void;
	parallelSetAll( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.util.function.IntFunction */ ):void;
	parallelSort( arg0:[any] /* [D */ ):void;
	parallelSort( arg0:[any] /* [D */, arg1:int, arg2:int ):void;
	parallelSort( arg0:[any] /* [F */ ):void;
	parallelSort( arg0:[any] /* [F */, arg1:int, arg2:int ):void;
	parallelSort( arg0:[any] /* [I */ ):void;
	parallelSort( arg0:[any] /* [I */, arg1:int, arg2:int ):void;
	parallelSort( arg0:[any] /* [J */ ):void;
	parallelSort( arg0:[any] /* [J */, arg1:int, arg2:int ):void;
	parallelSort( arg0:[any] /* [Ljava.lang.Comparable; */ ):void;
	parallelSort( arg0:[any] /* [Ljava.lang.Comparable; */, arg1:int, arg2:int ):void;
	parallelSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:Comparator<any> ):void;
	parallelSort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:Comparator<any> ):void;
	parallelSort( arg0:[any] /* [S */ ):void;
	parallelSort( arg0:[any] /* [S */, arg1:int, arg2:int ):void;
	parallelSort( arg0:bytearray ):void;
	parallelSort( arg0:bytearray, arg1:int, arg2:int ):void;
	parallelSort( arg0:chararray ):void;
	parallelSort( arg0:chararray, arg1:int, arg2:int ):void;
	setAll( arg0:[any] /* [D */, arg1:any /* java.util.function.IntToDoubleFunction */ ):void;
	setAll( arg0:[any] /* [I */, arg1:any /* java.util.function.IntUnaryOperator */ ):void;
	setAll( arg0:[any] /* [J */, arg1:any /* java.util.function.IntToLongFunction */ ):void;
	setAll( arg0:[any] /* [Ljava.lang.Object; */, arg1:any /* java.util.function.IntFunction */ ):void;
	sort( arg0:[any] /* [D */ ):void;
	sort( arg0:[any] /* [D */, arg1:int, arg2:int ):void;
	sort( arg0:[any] /* [F */ ):void;
	sort( arg0:[any] /* [F */, arg1:int, arg2:int ):void;
	sort( arg0:[any] /* [I */ ):void;
	sort( arg0:[any] /* [I */, arg1:int, arg2:int ):void;
	sort( arg0:[any] /* [J */ ):void;
	sort( arg0:[any] /* [J */, arg1:int, arg2:int ):void;
	sort( arg0:[any] /* [Ljava.lang.Object; */ ):void;
	sort( arg0:[any] /* [Ljava.lang.Object; */, arg1:Comparator<any> ):void;
	sort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):void;
	sort( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int, arg3:Comparator<any> ):void;
	sort( arg0:[any] /* [S */ ):void;
	sort( arg0:[any] /* [S */, arg1:int, arg2:int ):void;
	sort( arg0:bytearray ):void;
	sort( arg0:bytearray, arg1:int, arg2:int ):void;
	sort( arg0:chararray ):void;
	sort( arg0:chararray, arg1:int, arg2:int ):void;
	spliterator( arg0:[any] /* [D */ ):any /* java.util.Spliterator$OfDouble */;
	spliterator( arg0:[any] /* [D */, arg1:int, arg2:int ):any /* java.util.Spliterator$OfDouble */;
	spliterator( arg0:[any] /* [I */ ):any /* java.util.Spliterator$OfInt */;
	spliterator( arg0:[any] /* [I */, arg1:int, arg2:int ):any /* java.util.Spliterator$OfInt */;
	spliterator( arg0:[any] /* [J */ ):any /* java.util.Spliterator$OfLong */;
	spliterator( arg0:[any] /* [J */, arg1:int, arg2:int ):any /* java.util.Spliterator$OfLong */;
	spliterator<T>( arg0:[any] /* [Ljava.lang.Object; */ ):any /* java.util.Spliterator */;
	spliterator<T>( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):any /* java.util.Spliterator */;
	stream( arg0:[any] /* [D */ ):any /* java.util.stream.DoubleStream */;
	stream( arg0:[any] /* [D */, arg1:int, arg2:int ):any /* java.util.stream.DoubleStream */;
	stream( arg0:[any] /* [I */ ):any /* java.util.stream.IntStream */;
	stream( arg0:[any] /* [I */, arg1:int, arg2:int ):any /* java.util.stream.IntStream */;
	stream( arg0:[any] /* [J */ ):any /* java.util.stream.LongStream */;
	stream( arg0:[any] /* [J */, arg1:int, arg2:int ):any /* java.util.stream.LongStream */;
	stream<T>( arg0:[any] /* [Ljava.lang.Object; */ ):java.util.stream.Stream<any>;
	stream<T>( arg0:[any] /* [Ljava.lang.Object; */, arg1:int, arg2:int ):java.util.stream.Stream<any>;
	toString( arg0:[any] /* [D */ ):string;
	toString( arg0:[any] /* [F */ ):string;
	toString( arg0:[any] /* [I */ ):string;
	toString( arg0:[any] /* [J */ ):string;
	toString( arg0:[any] /* [Ljava.lang.Object; */ ):string;
	toString( arg0:[any] /* [S */ ):string;
	toString( arg0:[any] /* [Z */ ):string;
	toString( arg0:bytearray ):string;
	toString( arg0:chararray ):string;
}

export const Arrays: ArraysStatic = Java.type("java.util.Arrays");


interface StringStatic {

	readonly class:any;
	copyValueOf( arg0:chararray ):string;
	copyValueOf( arg0:chararray, arg1:int, arg2:int ):string;
	format( arg0:any /* java.util.Locale */, arg1:string, arg2:[any] /* [Ljava.lang.Object; */ ):string;
	format( arg0:string, arg1:[any] /* [Ljava.lang.Object; */ ):string;
	join( arg0:java.lang.CharSequence, arg1:[any] /* [Ljava.lang.CharSequence; */ ):string;
	join( arg0:java.lang.CharSequence, arg1:java.lang.Iterable<any> ):string;
	valueOf( arg0:any /* char */ ):string;
	valueOf( arg0:any /* java.lang.Object */ ):string;
	valueOf( arg0:boolean ):string;
	valueOf( arg0:chararray ):string;
	valueOf( arg0:chararray, arg1:int, arg2:int ):string;
	valueOf( arg0:double ):string;
	valueOf( arg0:float ):string;
	valueOf( arg0:int ):string;
	valueOf( arg0:long ):string;
}

export const String: StringStatic = Java.type("java.lang.String");


interface CollectionsStatic {

	readonly class:any;
	addAll( arg0:java.util.Collection<any>, arg1:[any] /* [Ljava.lang.Object; */ ):boolean;
	asLifoQueue<E>( arg0:any /* java.util.Deque */ ):any /* java.util.Queue */;
	binarySearch( arg0:java.util.List<any>, arg1:any /* java.lang.Object */ ):int;
	binarySearch( arg0:java.util.List<any>, arg1:any /* java.lang.Object */, arg2:Comparator<any> ):int;
	checkedCollection<E>( arg0:java.util.Collection<any>, arg1:java.lang.Class<any> ):java.util.Collection<any>;
	checkedList<E>( arg0:java.util.List<any>, arg1:java.lang.Class<any> ):java.util.List<any>;
	checkedMap<K,V>( arg0:java.util.Map<any, any>, arg1:java.lang.Class<any>, arg2:java.lang.Class<any> ):java.util.Map<any, any>;
	checkedNavigableMap<K,V>( arg0:any /* java.util.NavigableMap */, arg1:java.lang.Class<any>, arg2:java.lang.Class<any> ):any /* java.util.NavigableMap */;
	checkedNavigableSet<E>( arg0:any /* java.util.NavigableSet */, arg1:java.lang.Class<any> ):any /* java.util.NavigableSet */;
	checkedQueue<E>( arg0:any /* java.util.Queue */, arg1:java.lang.Class<any> ):any /* java.util.Queue */;
	checkedSet<E>( arg0:java.util.Set<any>, arg1:java.lang.Class<any> ):java.util.Set<any>;
	checkedSortedMap<K,V>( arg0:any /* java.util.SortedMap */, arg1:java.lang.Class<any>, arg2:java.lang.Class<any> ):any /* java.util.SortedMap */;
	checkedSortedSet<E>( arg0:any /* java.util.SortedSet */, arg1:java.lang.Class<any> ):any /* java.util.SortedSet */;
	copy( arg0:java.util.List<any>, arg1:java.util.List<any> ):void;
	disjoint( arg0:java.util.Collection<any>, arg1:java.util.Collection<any> ):boolean;
	emptyEnumeration<E>(  ):any /* java.util.Enumeration */;
	emptyIterator<E>(  ):java.util.Iterator<any>;
	emptyList<E>(  ):java.util.List<any>;
	emptyListIterator<E>(  ):any /* java.util.ListIterator */;
	emptyMap<K,V>(  ):java.util.Map<any, any>;
	emptyNavigableMap<K,V>(  ):any /* java.util.NavigableMap */;
	emptyNavigableSet<E>(  ):any /* java.util.NavigableSet */;
	emptySet<E>(  ):java.util.Set<any>;
	emptySortedMap<K,V>(  ):any /* java.util.SortedMap */;
	emptySortedSet<E>(  ):any /* java.util.SortedSet */;
	enumeration<E>( arg0:java.util.Collection<any> ):any /* java.util.Enumeration */;
	fill( arg0:java.util.List<any>, arg1:any /* java.lang.Object */ ):void;
	frequency( arg0:java.util.Collection<any>, arg1:any /* java.lang.Object */ ):int;
	indexOfSubList( arg0:java.util.List<any>, arg1:java.util.List<any> ):int;
	lastIndexOfSubList( arg0:java.util.List<any>, arg1:java.util.List<any> ):int;
	list<E>( arg0:any /* java.util.Enumeration */ ):java.util.ArrayList<any>;
	max( arg0:java.util.Collection<any> ):any /* java.lang.Object */;
	max( arg0:java.util.Collection<any>, arg1:Comparator<any> ):any /* java.lang.Object */;
	min( arg0:java.util.Collection<any> ):any /* java.lang.Object */;
	min( arg0:java.util.Collection<any>, arg1:Comparator<any> ):any /* java.lang.Object */;
	nCopies<E>( arg0:int, arg1:any /* java.lang.Object */ ):java.util.List<any>;
	newSetFromMap<E>( arg0:java.util.Map<any, any> ):java.util.Set<any>;
	replaceAll( arg0:java.util.List<any>, arg1:any /* java.lang.Object */, arg2:any /* java.lang.Object */ ):boolean;
	reverse( arg0:java.util.List<any> ):void;
	reverseOrder<T>(  ):Comparator<any>;
	reverseOrder<T>( arg0:Comparator<any> ):Comparator<any>;
	rotate( arg0:java.util.List<any>, arg1:int ):void;
	shuffle( arg0:java.util.List<any> ):void;
	shuffle( arg0:java.util.List<any>, arg1:any /* java.util.Random */ ):void;
	singleton<E>( arg0:any /* java.lang.Object */ ):java.util.Set<any>;
	singletonList<E>( arg0:any /* java.lang.Object */ ):java.util.List<any>;
	singletonMap<K,V>( arg0:any /* java.lang.Object */, arg1:any /* java.lang.Object */ ):java.util.Map<any, any>;
	sort( arg0:java.util.List<any> ):void;
	sort( arg0:java.util.List<any>, arg1:Comparator<any> ):void;
	swap( arg0:java.util.List<any>, arg1:int, arg2:int ):void;
	synchronizedCollection<E>( arg0:java.util.Collection<any> ):java.util.Collection<any>;
	synchronizedList<E>( arg0:java.util.List<any> ):java.util.List<any>;
	synchronizedMap<K,V>( arg0:java.util.Map<any, any> ):java.util.Map<any, any>;
	synchronizedNavigableMap<K,V>( arg0:any /* java.util.NavigableMap */ ):any /* java.util.NavigableMap */;
	synchronizedNavigableSet<E>( arg0:any /* java.util.NavigableSet */ ):any /* java.util.NavigableSet */;
	synchronizedSet<E>( arg0:java.util.Set<any> ):java.util.Set<any>;
	synchronizedSortedMap<K,V>( arg0:any /* java.util.SortedMap */ ):any /* java.util.SortedMap */;
	synchronizedSortedSet<E>( arg0:any /* java.util.SortedSet */ ):any /* java.util.SortedSet */;
	unmodifiableCollection<E>( arg0:java.util.Collection<any> ):java.util.Collection<any>;
	unmodifiableList<E>( arg0:java.util.List<any> ):java.util.List<any>;
	unmodifiableMap<K,V>( arg0:java.util.Map<any, any> ):java.util.Map<any, any>;
	unmodifiableNavigableMap<K,V>( arg0:any /* java.util.NavigableMap */ ):any /* java.util.NavigableMap */;
	unmodifiableNavigableSet<E>( arg0:any /* java.util.NavigableSet */ ):any /* java.util.NavigableSet */;
	unmodifiableSet<E>( arg0:java.util.Set<any> ):java.util.Set<any>;
	unmodifiableSortedMap<K,V>( arg0:any /* java.util.SortedMap */ ):any /* java.util.SortedMap */;
	unmodifiableSortedSet<E>( arg0:any /* java.util.SortedSet */ ):any /* java.util.SortedSet */;
}

export const Collections: CollectionsStatic = Java.type("java.util.Collections");


