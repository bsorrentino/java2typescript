"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var colors = require("colors/safe");
var mustache = require("mustache");
var jdk8_types_1 = require("./jdk8-types");
var b = "hello jjs";
var a = jdk8_types_1.Arrays.asList(["item1", "item2", "item3"]);
print(colors.red(b));
a.stream().forEach(function (e) {
    print(colors.green(e));
});
jdk8_types_1.Stream.of("<item2>").forEach(function (e) {
    print(colors.green(e));
});
var template = "Email addresses of: {{contact.name}}:\n{{#contact.emails}}\n- {{.}}\n{{/contact.emails}}\n";
var result = mustache.render(template, { contact: { name: "bsorrentino",
        emails: [
            "bartolomeo.sorrentino@gmail.com",
            "bartolomeo.sorrentino@yahoo.com"
        ] } });
print(result);
var u1 = "http://localhost:8000/site/";
var u2 = "/spaces/flyingpdf/pdfpageexport.action?pageId=100532618";
var uri = jdk8_types_1.URI.create(u1 + u2);
print(uri.resolve(u2).toString());
print(jdk8_types_1.URI.create(u1 + u2).normalize().toString());
