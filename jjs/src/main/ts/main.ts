

let b = "hello jjs";

let a = new java.util.ArrayList<string>();
a.add( "item1");
a.add( "item2" );
a.add( java.util.Optional.of("item3"));

print( b);

a.stream().forEach( (e) => {
  print( e );
});
