"use strict";
var b = "hello jjs";
var a = new java.util.ArrayList();
a.add("item1");
a.add("item2");
a.add(java.util.Optional.of("item3"));
print(b);
a.stream().forEach(function (e) {
    print(e);
});
