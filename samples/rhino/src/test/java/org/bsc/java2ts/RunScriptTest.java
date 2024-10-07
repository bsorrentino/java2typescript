package org.bsc.java2ts;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RunScriptTest {

    @Test
    public void testRequireExists() throws Exception {

        final String js = "var test = require('test-cjs.js');\ntest();\n";

        ContextFactory factoryCtx = new ContextFactory();

        URL classpathResourceURL = getClass().getResource("/");
        Assert.assertNotNull( "classpathResourceURL is null", classpathResourceURL );

        String cwd = System.getProperty("user.dir");
        final List<URI> modules = Arrays.asList( classpathResourceURL.toURI(), Paths.get(cwd).toUri() );

        Object result = factoryCtx.call( ctx -> {

            CommonJsImporter topLevel = new CommonJsImporter(ctx, false);

            ctx.initStandardObjects(topLevel, false);
            topLevel.installRequire(ctx, modules, true);

            Scriptable newScope = ctx.newObject(topLevel);
            newScope.setPrototype(topLevel);

            return ctx.evaluateString(newScope, js, "<cmd>", 1, null);
        });

        Assert.assertEquals( "The CommonJS require function works!", result );
    }

    @Test
    public void testES6Module() throws Exception {

        final String js =   "\"use strict\"\n" +
                            "Object.defineProperty(exports, \"__esModule\", { value: true });\n" +
                            "let test2_1 = require(\"test-mjs\");\n" +
                            "test2_1.test();";

        ContextFactory factoryCtx = new ContextFactory();

        URL classpathResourceURL = getClass().getResource("/");
        Assert.assertNotNull( "classpathResourceURL is null", classpathResourceURL );

        String cwd = System.getProperty("user.dir");
        final List<URI> modules = Arrays.asList( classpathResourceURL.toURI(), Paths.get(cwd).toUri() );

        Object result = factoryCtx.call( ctx -> {
            ctx.setLanguageVersion(Context.VERSION_ES6);
            // ctx.setOptimizationLevel(-1);
            CommonJsImporter topLevel = new CommonJsImporter(ctx, false);

            ScriptableObject scope = ctx.initStandardObjects(topLevel, false);

            topLevel.defineES6Exports(ctx.newObject(scope));

            topLevel.installRequire(ctx, modules, true);

            Scriptable newScope = ctx.newObject(topLevel);
            newScope.setPrototype(topLevel);

            return ctx.evaluateString(newScope, js, "<cmd>", 1, null);
        });

        Assert.assertEquals( "The ES6 import function works!", result );
    }

}
