
import {test as future_test} from "./future.test.rhino";

import {
  URI,
  Optional,
  MemoryType,
} from "./j2ts/jdk8-types";


const u1 = "http://localhost:8000/site/";
const u2 = "/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";
const u3 = "http://localhost:8000/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";

let uri = URI.create( u1 );

print( uri.resolve( u2 ).toString() );
print( URI.create( u1 + u2 ).normalize().toString() );
print( uri.resolve( u3 ).toString() );

print( Optional.empty().map( { apply:e => "element: " + e } ).orElse("nil") );
print( Optional.of("HELLO").map( { apply:e => "element: " + e } ).orElse("nil") );


// TEST ENUM

print(MemoryType.HEAP);
print(MemoryType.HEAP.name());
print(MemoryType.HEAP.ordinal());

/*
color_test();
mustache_test();
validator_test();
stream_test();
*/
future_test();
