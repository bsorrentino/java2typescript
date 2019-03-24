import mustache = require("mustache");

export function test() {

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

}