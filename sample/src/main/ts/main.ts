
import * as colors from "colors/safe";
import mustache = require("mustache");

import {Stream, URI, Arrays, Optional} from "ts/jdk8-types";

let b = "hello jjs";


let a = Arrays.asList( "item1", "item2", "item3", "item4.1" );

print( colors.red(b) );

a.stream().forEach( e => {
  print( colors.green(e) );
});

Stream.of<string>( "<item2>" ).forEach( e => {
  print( colors.green(e) );
});

let template =
`Email addresses of: {{contact.name}}:
{{#contact.emails}}
- {{.}}
{{/contact.emails}}
`;

let result = mustache.render( template,
  { contact: { name:"bsorrentino",
               emails:[
                 "bartolomeo.sorrentino@gmail.com",
                 "bartolomeo.sorrentino@yahoo.com"
               ]}}
);
print(result);


const u1 = "http://localhost:8000/site/";
const u2 = "/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";
const u3 = "http://localhost:8000/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";

let uri = URI.create( u1 );

print( uri.resolve( u2 ).toString() );
print( URI.create( u1 + u2 ).normalize().toString() );
print( uri.resolve( u3 ).toString() );

print( Optional.empty().map( e => "element: " + e).orElse("nil") );
print( Optional.of("HELLO").map( e => "element: " + e).orElse("nil") );
