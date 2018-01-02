
import * as colors from "colors/safe";
import mustache = require("mustache");

import {Stream} from "./jdk8-types";

let b = "hello jjs";


let a = java.util.Arrays.asList( [ "item1", "item2", "item3"] );

print( colors.red(b) );

a.stream().forEach( (e) => {
  print( colors.green(e) );
});

Stream.of<string>( "<item2>" ).forEach( (e) => {
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