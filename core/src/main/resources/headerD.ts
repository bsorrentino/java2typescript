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
// Nashorn compatibility
//

declare function print( ...args: any[] ):void

declare function load( module:string ):void

declare namespace Java {

  // Typed overload: for keys known to the generated registry, returns the concrete class
  // (constructor + statics). Declared first so it takes precedence over the generic overload.
  // {{REGISTRY_TYPE}} is substituted by the annotation processor (see the 'ts.registry' option).
  export function type<K extends keyof {{REGISTRY_TYPE}}>( k:K ):{{REGISTRY_TYPE}}[K];

  // Fallback so unknown class names still compile.
  export function type<T>( t:string ):T;

  export function from<T>( list:java.util.List<T> ):Array<T> ;

}

//
// Generated declarations
//

