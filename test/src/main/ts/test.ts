/// <reference path="../../../target/ts/jdk8.d.ts" />

export = 0 // TSC FIX DON'T REMOVE

import { String } from "./utils";

let list = new java.util.ArrayList<string>();


list.add( "a1" );
list.add( "a2" );
list.add( "a3" );
list.add("CASOLLA");


list.stream()
    .filter((v) => v!="CASOLLA" )
    .forEach( (v) => print(v) )
    ;

print( String(list) );    