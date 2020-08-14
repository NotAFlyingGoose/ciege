package com.runningmanstudios.caffeineGameEngine.console.script;

public abstract class OutPutter {

    public abstract CScript getScript(String packageName);

    public abstract void print(Object object);

    public abstract void println(Object object);

    public abstract void printError(Exception exception);
}
