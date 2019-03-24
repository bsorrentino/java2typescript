import * as colors from "colors/safe";

import {
    Stream, 
    Arrays, 
  } from "./ts/jdk8-types";
  
  
export function test() {

    let b = "hello jjs";

    let a = Arrays.asList( "item1", "item2", "item3", "item4.1" );
    
    print( colors.red(b) );
    
    a.stream().forEach( e => {
      print( colors.green(e) );
    });
    
    Stream.of<string>( "<item2>" ).forEach( e => {
      print( colors.green(e) );
    });
}  
