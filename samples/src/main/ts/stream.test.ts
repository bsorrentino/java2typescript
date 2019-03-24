
import {
    Arrays, 
    Stream,
    Collectors

} from "ts/jdk8-types";


export function test() {
    
    let list = Arrays.asList( "A", "B", "C" );

    Stream.of( 4, 10, 9, 7, 34, 100 )
        .filter( n => n%2==0 )
        .map( p => p/2 )
        .reduce( (b,c:any) => b + c )
        .ifPresent( v => print("LIST ELEMENT", v) )
        
    type element = [ number, string];
    
    let r = Stream.of<element>( [4, "Pari"], [10, "Pari"], [9, "Dispari"], [7,"Dispari"], [34, "Pari"], [100,"Pari"], [34, "Pari"] )
        .collect( Collectors.groupingBy( ( [v,k] ) => v ) )
        .values()
        .stream()
        .flatMap( list => list.stream().map( ([v,k]) => v ).distinct() )  
        .forEach( v => print(v) )
        
    //print(r);
    
    let s = list.stream()
        .filter( c => c.charAt(0)!="B")
        //.parallel()
        .collect( Collectors.joining(","));
    
    
    print("COMPLETE", s);
    
    
}
