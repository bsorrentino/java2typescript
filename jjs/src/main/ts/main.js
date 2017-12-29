"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var colors = require("colors/safe");
var b = "hello jjs";
var a = java.util.Arrays.asList(["item1", "item2", "item3"]);
print(colors.red(b));
a.stream().forEach(function (e) {
    print(colors.green(e));
});
