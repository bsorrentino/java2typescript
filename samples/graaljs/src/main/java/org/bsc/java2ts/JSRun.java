package org.bsc.java2ts;

import java.util.function.Predicate;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.io.IOAccess;

public class JSRun {
    public static void main(String[] args) throws Exception  {

        try(final Context context = Context.newBuilder("js")
                //.allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL)
                //.allowCreateThread(true)
                .allowHostClassLookup( s -> true )
                .allowIO(IOAccess.ALL)
                //.allowExperimentalOptions(true)
                //.option("js.experimental-foreign-object-prototype", "true")
                .option("js.esm-eval-returns-exports", "true")
                .option("engine.WarnInterpreterOnly", "false")

                .build()) {
            ;

            context.getBindings("js").putMember("$ARG", args);

            if (args.length == 0) {
                System.out.println("usage:\tJSRun.Graaljs <file>.js");
            }
            try (java.io.Reader app = new java.io.FileReader(args[0])) {
                final Source source = Source.newBuilder("js", app, args[0])
                        .mimeType("application/javascript+module")
                        .build();
                context.eval(source);
            }
        }
    }

    public static class Graaljs {

        public static void mainjSR223(String[] args) throws Exception  {
            final ScriptEngineManager manager = new ScriptEngineManager();

            final ScriptEngine service = manager.getEngineByName("graal.js");
            final Bindings bindings = service.getBindings(ScriptContext.ENGINE_SCOPE);
            // @see https://github.com/graalvm/graaljs/blob/master/docs/user/ScriptEngine.md
            bindings.put("polyglot.js.allowHostAccess", true);
            bindings.put("polyglot.js.allowHostClassLookup", (Predicate<String>) s -> true);
            bindings.put("polyglot.js.allowIO", true);

            service.put( "$ARG", args );

            if( args.length == 0 ) {
                System.out.println( "usage:\tJSRun.Graaljs <file>.js");
            }
            try( java.io.Reader app = new java.io.FileReader(args[0])) {
                    service.eval( app );
            }
        }


    }



}
