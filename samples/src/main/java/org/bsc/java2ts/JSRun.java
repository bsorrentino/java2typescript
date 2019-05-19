package org.bsc.java2ts;

import java.util.function.Predicate;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JSRun {

    public static class Nashorn {
        public static void main(String[] args) throws Exception  {
            final ScriptEngineManager manager = new ScriptEngineManager();

            final ScriptEngine service = manager.getEngineByName("nashorn");
            
            service.put( "$ARG", args );

            if( args.length == 0 ) {
                System.out.printf( "usage:\tJSRun.Nashorn <file>.js\n");
            }
            try( java.io.Reader app = new java.io.FileReader(args[0])) {
                    service.eval( app );
            }
        }
        
    }
    
    public static class Rhino {
        
        public static void main(String[] args) throws Exception  {
            final ScriptEngineManager manager = new ScriptEngineManager();

            final ScriptEngine service = manager.getEngineByName("rhino-npm");
            
            if( args.length == 0 ) {
                System.out.printf( "usage:\tJSRun.Rhino <file>.js\n");
            }
            try( java.io.Reader app = new java.io.FileReader(args[0])) {
                    service.put( "$ARG", args);
                    service.eval( app );
            }
        }
        
    }

    public static class Graaljs {
        public static void main(String[] args) throws Exception  {
            final ScriptEngineManager manager = new ScriptEngineManager();

            final ScriptEngine service = manager.getEngineByName("graal.js");
            final Bindings bindings = service.getBindings(ScriptContext.ENGINE_SCOPE);
            // @see https://github.com/graalvm/graaljs/blob/master/docs/user/ScriptEngine.md
            bindings.put("polyglot.js.allowHostAccess", true);
            bindings.put("polyglot.js.allowHostClassLookup", (Predicate<String>) s -> true);
            bindings.put("polyglot.js.allowIO", true);
            
            service.put( "$ARG", args );

            if( args.length == 0 ) {
                System.out.printf( "usage:\tJSRun.Graaljs <file>.js\n");
            }
            try( java.io.Reader app = new java.io.FileReader(args[0])) {
                    service.eval( app );
            }
        }
        
    }
    
    
    
}
