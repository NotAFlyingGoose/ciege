package com.runningmanstudios.caffeineGameEngine.console.script.parser.data;

public abstract class Data {
    protected String title;
    public Data(String title) {
        this.title = title;
    }
    public boolean titleMatch(String s) {
        return this.title.equals(s);
    }

    @Override
    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }
}
