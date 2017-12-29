//
// STARTUP
//

load('classpath:jvm-npm.js');

java.lang.System.setProperty( "jvm-npm.debug", "true");

// PLOYFILL
var process = { argv:"", env:{ TERM:'color' } };

var exports = {};
load('./main.js');
