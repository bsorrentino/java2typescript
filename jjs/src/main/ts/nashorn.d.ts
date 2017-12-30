//
// Nashorn
//

declare function print( ...args: any[]):void

declare function load( module:string ):void

declare namespace Java {

  export function type<T>( t:string):T;

  export function from<T>( list:java.util.List<T>):Array<T> ;
}
