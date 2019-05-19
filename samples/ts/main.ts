
import chalk from 'chalk';
import { test as future_test } from './future.test';
import { MemoryType, Optional, URI } from './j2ts/jdk8-types';
import { test as marked_test } from './marked.test';
import { test as promise_test } from './promise.test';



const u1 = "http://localhost:8000/site/";
const u2 = "/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";
const u3 = "http://localhost:8000/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";

let uri = URI.create( u1 );

print( uri.resolve( u2 ).toString() );
print( URI.create( u1 + u2 ).normalize().toString() );
print( uri.resolve( u3 ).toString() );

print( Optional.empty().map( e => "element: " + e).orElse("nil") );
print( Optional.of("HELLO").map( e => "element: " + e).orElse("nil") );


// TEST ENUM

print( 
`
MemoryType.HEAP         = ${MemoryType.HEAP}
MemoryType.HEAP.name    = ${MemoryType.HEAP.name()}
MemoryType.HEAP.ordinal = ${MemoryType.HEAP.ordinal()}
`    
)


let async_start = async () => {

    /*
    
    mustache_test();
    validator_test();
    stream_test();
    */
    //color_test();

    future_test();
    marked_test();
    print( await promise_test() );

    //print( chalk.blue( "HELLO WORLD!") );
}


async_start();