
import * as colors from "colors/safe";

let b = "hello jjs";

let a = java.util.Arrays.asList( [ "item1", "item2", "item3"] );

print( colors.red(b) );

a.stream().forEach( (e) => {
  print( colors.green(e) );
});
