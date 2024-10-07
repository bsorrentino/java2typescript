package org.bsc.java2ts;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.net.URI;
import java.util.List;

public class CommonJsImporter extends ImporterTopLevel {

    public CommonJsImporter(Context ctx, boolean sealed) {
        super(ctx, sealed);
    }

    /**
     * fix problem for Typescript generation
     * Object.defineProperty(exports, "__esModule", { value: true });
     * @param object empty javascript object
     */
    public void defineES6Exports(Scriptable object) {
        ScriptableObject.putProperty(this, "exports", object);
    }

    public void installRequire(Context ctx, List<URI> uris, boolean sandboxed) {


        RequireBuilder rb = new RequireBuilder();
        rb.setSandboxed(sandboxed);
        rb.setModuleScriptProvider(
                new SoftCachingModuleScriptProvider(new UrlModuleSourceProvider(uris, null)));
        Require require = rb.createRequire(ctx, this);

        require.install(this);
    }


}
