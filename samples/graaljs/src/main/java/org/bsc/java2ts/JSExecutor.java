package org.bsc.java2ts;

import java.util.concurrent.Executor;

public class JSExecutor implements Executor {

    public JSExecutor() {}

    @Override
    public void execute(Runnable command) {
        command.run(); 
    }

}
