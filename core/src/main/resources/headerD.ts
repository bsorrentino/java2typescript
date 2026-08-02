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
  	interface Class<T> {
		getResource(res : string) : any /*java.net.URL*/
	}
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

  // Java.extend( SuperType, ...moreTypes ) returns a subclass "adapter" constructor. `new`-ing it
  // takes the supertype constructor arguments followed by an object literal that
  // overrides/implements the supertype's methods; the resulting instance is an InstanceType<T>.
  // Typed loosely (args:any[]) since the forwarded ctor args and the override object vary per type.
  export function extend<T extends new ( ...args:any[] ) => any>( type:T, ...moreTypes:any[] ):new ( ...args:any[] ) => InstanceType<T>;

}

//
// Generated declarations
//

