package org.bsc.java2ts;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JSRun {

    private static final String JS_ENGINE_NAME = "nashorn";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception  {

		final ScriptEngineManager manager = new ScriptEngineManager();

        final ScriptEngine service = manager.getEngineByName(JS_ENGINE_NAME);
        
        try( java.io.Reader app = new java.io.FileReader("app.js")) {
        		service.eval( app );
        }
	}

}
