package org.bsc.java2ts;

import org.mozilla.javascript.*;

import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.String.format;

public class AbstractRhinoTopLevel extends ImporterTopLevel {

    private static final String CLASSPATH_PREFIX = "classpath:";

    @SuppressWarnings("unchecked")
    protected static <T extends AbstractRhinoTopLevel> T deref(Scriptable thisObj) {
        AbstractRhinoTopLevel _this = null;

        if( thisObj instanceof AbstractRhinoTopLevel ) {
            _this = (AbstractRhinoTopLevel) thisObj;
        }
        else {

            final Scriptable protoObj = thisObj.getPrototype();
            if( protoObj instanceof AbstractRhinoTopLevel ) {
                _this = (AbstractRhinoTopLevel) protoObj;
            }
            else {
                throw new IllegalStateException( "cannot deref thisObj to  AbstractRhinoTopLevel!");
            }
        }

        return (T) _this;
    }


    protected void _print(PrintWriter out, Context cx, Object[] args, Function funObj) {
        if (args == null) {
            return;
        }

        int row = 0;
        for (Object arg : args) {

            if (row++ > 0) {
                out.print(" ");
            }
            // Convert the arbitrary JavaScript value into a string form.
            out.print(Context.toString(arg));
        }

        out.println();
        out.flush();
    }

    private final java.util.Set<String> moduleCache = new java.util.HashSet<>();


    private String normalizeModuleName( String moduleName ) {

        if( moduleName.startsWith(CLASSPATH_PREFIX) ) {

            return moduleName.substring(CLASSPATH_PREFIX.length());
        }

        return moduleName;


    }


    protected void _load(Context cx, Object[] args, Function funObj) throws Exception{
        if (args == null) {
            return;
        }

        for( Object arg :  args ) {

            final String module = normalizeModuleName(Context.toString(arg));

            if( moduleCache.contains(module)) {
                break;
            }

            final ClassLoader cl = Thread.currentThread().getContextClassLoader();

            final java.io.InputStream is = cl.getResourceAsStream(module);
            if (is != null) {

                try {
                    cx.evaluateReader(this, new java.io.InputStreamReader(is), module, 0, null);

                    moduleCache.add( module );

                } catch (IOException e) {
                    throw new Exception(format("error evaluating module [%s]", module), e);
                }

            } else { // Fallback

                final java.io.File file = new java.io.File(module);

                if (!file.exists()) {
                    throw new Exception(format("module [%s] doesn't exist!", module));
                }
                if (!file.isFile()) {
                    throw new Exception(format("module [%s] is not a file exist!", module));
                }

                try( java.io.FileReader reader = new java.io.FileReader(file) ) {

                    cx.evaluateReader(this, reader, module, 0, null);

                    moduleCache.add( module );

                } catch (IOException e) {
                    throw new Exception(format("error evaluating module [%s]", module), e);
                }

            }
        }
    }

    public AbstractRhinoTopLevel(Context cx) {
        this(cx, false);
    }

    public AbstractRhinoTopLevel(Context cx, boolean sealed) {
        super(cx, sealed);

    }

    @Override
    public void initStandardObjects(Context ctx, boolean sealed) {
        //super.initStandardObjects(cx, sealed);

        /*
         * fix problem for Typescript generation
         * Object.defineProperty(exports, "__esModule", { value: true });
         * @param object empty javascript object
         */
        ScriptableObject.putProperty(this, "exports", ctx.newObject(this));

        final String[] names = { "print", "load" };

        defineFunctionProperties(names, getClass(), ScriptableObject.DONTENUM);

//        final ScriptableObject objProto = (ScriptableObject) getObjectPrototype(this);
//        objProto.defineFunctionProperties(names, getClass(), DONTENUM);

    }

}