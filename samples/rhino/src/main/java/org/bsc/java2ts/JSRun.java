package org.bsc.java2ts;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JSRun {

    public static class RhinoEngine {

        public static void main(String[] args) throws Exception  {
            final ScriptEngineManager manager = new ScriptEngineManager();

            final ScriptEngine service = manager.getEngineByName("rhino-npm");

            if( args.length == 0 ) {
                System.out.println( "usage:\tJSRun.Rhino <file>.js");
            }
            try( java.io.Reader app = new java.io.FileReader(args[0])) {
                service.put( "$ARG", args);
                service.eval( app );
            }
        }

    }

    public static class Rhino {

        public static void main(String[] args) throws Exception  {
            if( args.length == 0 ) {
                System.out.println( "usage:\tJSRun.Rhino <file>.js");
                return;
            }

            try( java.io.Reader app = new java.io.FileReader(args[0])) {

                ContextFactory factoryCtx = new ContextFactory();

                String cwd = System.getProperty("user.dir");

                System.out.println( "cwd=" + cwd);

                final List<URI> modules = Arrays.asList(
                        Paths.get(cwd).toUri(),
                        Paths.get(cwd, "target","js" ).toUri()
                );

                Object result = factoryCtx.call(ctx -> {
                    ctx.setLanguageVersion(Context.VERSION_ES6);
                    // ctx.setOptimizationLevel(-1);
                    RhinoTopLevel topLevel = new RhinoTopLevel(ctx, false);

                    ctx.initStandardObjects(topLevel, false);

                    topLevel.initStandardObjects(ctx, false);

                    topLevel.installRequire(ctx, true,  new UrlModuleSourceProvider( modules, null ));

                    try {
                        return ctx.evaluateReader(topLevel, app, args[0], 1, null);
                    } catch (IOException e) {
                        return e;
                    }
                });

                System.out.println( result );
            }
        }

    }

}
