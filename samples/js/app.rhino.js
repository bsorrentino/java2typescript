//
// STARTUP
//

load('classpath:jvm-npm.js');

require.paths = [
  "src/main/ts",
  "target/ts/src/main/ts",
  "target/ts/target"
];

java.lang.System.setProperty( "jvm-npm.debug", "false");


// PLOYFILL
//The $ENV variable provides the shell environment variables.
//The $ARG variable is an array of the program command-line arguments.
print( "args", $ARG.length );

var process = { argv:$ARG, env:{TERM:'color'} } ;

var exports = {};
load('target/ts/src/main/ts/main.rhino.js');
