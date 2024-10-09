//
// STARTUP
//

// load('./target/js/jvm-npm.js');

// print( typeof require );

require.paths = [
  "node_emul"
];

// java.lang.System.setProperty( 'jvm-npm.debug', 'false');

var process = {
  argv:'',
  platform:'macosx',
  env: {
    TERM:'color'
  },
  stderr:undefined,
  stdout:undefined
}

load('./target/js/main.js');
