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
	
	interface Comparable<T> {

		compareTo?( arg0:T ):number;

	}
}            	

declare namespace java.io {
	
	interface Serializable {}
}