//
// STARTUP
//

load('classpath:jvm-npm.js');

java.lang.System.setProperty( "jvm-npm.debug", "true");

// PLOYFILL
//The $ENV variable provides the shell environment variables.
//The $ARG variable is an array of the program command-line arguments.
print( "args", $ARG.length );

var process = { argv:$ARGS, env:{TERM:'color'} } ;

var exports = {};
load('./main.js');
