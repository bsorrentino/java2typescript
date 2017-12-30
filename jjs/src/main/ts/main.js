"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var colors = require("colors/safe");
var mustache = require("mustache");
var jdk8_types_1 = require("./jdk8-types");
var b = "hello jjs";
var list = new jdk8_types_1.ArrayList();
var a = java.util.Arrays.asList(["item1", "item2", "item3"]);
print(colors.red(b));
a.stream().forEach(function (e) {
    print(colors.green(e));
});
var template = "Email addresses of: {{contact.name}}:\n{{#contact.emails}}\n- {{.}}\n{{/contact.emails}}\n";
var result = mustache.render(template, { contact: { name: "bsorrentino",
        emails: [
            "bartolomeo.sorrentino@gmail.com",
            "bartolomeo.sorrentino@yahoo.com"
        ] } });
print(result);
