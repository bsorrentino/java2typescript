package org.bsc.java2ts;

import org.mozilla.javascript.*;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.commonjs.module.RequireBuilder;
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RhinoTopLevel extends AbstractRhinoTopLevel {

    static class RequireProxy implements Function {

        final Require delegate;

        public RequireProxy(Require delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {

            if (args != null && args.length >= 1) {

                Path path = Paths.get( String.valueOf(args[0]) ).normalize();

                return delegate.call(context, scriptable == this ? delegate : scriptable, scriptable1, new Object[] { path.toString() } );
            }
            return delegate.call(context, scriptable == this ? delegate : scriptable, scriptable1, args);
        }

        @Override
        public Scriptable construct(Context context, Scriptable scriptable, Object[] objects) {
            return delegate.construct(context, scriptable == this ? delegate : scriptable, objects);
        }

        @Override
        public String getClassName() {
            return delegate.getClassName();
        }

        @Override
        public Object get(String s, Scriptable scriptable) {
            return delegate.get(s, scriptable == this ? delegate : scriptable);
        }

        @Override
        public Object get(int i, Scriptable scriptable) {
            return delegate.get(i, scriptable == this ? delegate : scriptable);
        }

        @Override
        public boolean has(String s, Scriptable scriptable) {
            return delegate.has(s, scriptable == this ? delegate : scriptable);
        }

        @Override
        public boolean has(int i, Scriptable scriptable) {
            return delegate.has(i, scriptable == this ? delegate : scriptable);
        }

        @Override
        public void put(String s, Scriptable scriptable, Object o) {
            delegate.put(s, scriptable == this ? delegate : scriptable, o);
        }

        @Override
        public void put(int i, Scriptable scriptable, Object o) {
            delegate.put(i, scriptable == this ? delegate : scriptable , o);
        }

        @Override
        public void delete(String s) {
            delegate.delete(s);
        }

        @Override
        public void delete(int i) {
            delegate.delete(i);
        }

        @Override
        public Scriptable getPrototype() {
            return delegate.getPrototype();
        }

        @Override
        public void setPrototype(Scriptable scriptable) {
            delegate.setPrototype(scriptable == this ? delegate : scriptable);
        }

        @Override
        public Scriptable getParentScope() {
            return delegate.getParentScope();
        }

        @Override
        public void setParentScope(Scriptable scriptable) {
            delegate.setParentScope(scriptable == this ? delegate : scriptable);
        }

        @Override
        public Object[] getIds() {
            return delegate.getIds();
        }

        @Override
        public Object getDefaultValue(Class<?> aClass) {
            return delegate.getDefaultValue(aClass);
        }

        @Override
        public boolean hasInstance(Scriptable scriptable) {
            return delegate.hasInstance(scriptable == this ? delegate : scriptable);
        }
    }

    public static void print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        final RhinoTopLevel _this = deref(thisObj);

        final PrintWriter w = new PrintWriter(System.out);

        _this._print( w, cx, args, funObj );
    }

    public static void load(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws Exception {

        final RhinoTopLevel _this = deref(thisObj);

        _this._load(cx, args, funObj );
    }

    public RhinoTopLevel(Context ctx, boolean sealed) {
        super(ctx, sealed);
    }

    public RhinoTopLevel(Context ctx) {
        super(ctx);
    }

    public void installRequire(Context ctx, boolean sandboxed, UrlModuleSourceProvider provider) {


        RequireBuilder rb = new RequireBuilder();
        rb.setSandboxed(sandboxed);
        rb.setModuleScriptProvider(
                new SoftCachingModuleScriptProvider( provider));
        Require require = rb.createRequire(ctx, this);

        // require.install(this);
        ScriptableObject.putProperty(this, "require", new RequireProxy(require));
    }


}
