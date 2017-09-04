/// <reference path="../../../target/ts/jdk8.d.ts" />

export = 0 // TSC FIX DON'T REMOVE

import { toString } from "./utils";

let list = new java.util.ArrayList<string>();


list.add( "a1" );
list.add( "a2" );
list.add( "a3" );
list.add("Casolla");

let builder = java.util.stream.Stream.builder();

list.stream()
    .filter((v) => !toString(v).equalsIgnoreCase("CASOLLA") )
    .forEach( (v) => print(v) )
    ;

print( toString(list) );    

