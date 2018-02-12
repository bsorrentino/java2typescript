//
// STARTUP
//

load('classpath:jvm-npm.js');

require.paths = [
  "../../../target/ts"
];

java.lang.System.setProperty( "jvm-npm.debug", "true");


// PLOYFILL
//The $ENV variable provides the shell environment variables.
//The $ARG variable is an array of the program command-line arguments.
print( "args", $ARG.length );

var process = { argv:$ARG, env:{TERM:'color'} } ;

var exports = {};
load('../../../target/ts/main.js');
