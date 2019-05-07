package org.bsc.java2ts;

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
