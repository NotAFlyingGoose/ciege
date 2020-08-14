package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable;

public class BooleanVariable extends NumberVariable {
    public BooleanVariable(String title, boolean value) {
        super(title, value?1:0);
    }

    public BooleanVariable(String title, String value) {
        this(title, Boolean.parseBoolean(value));
    }

    public static boolean parse(double value) {
        return value > 0;
    }

    public static boolean parse(String value) {
        return value.equals("true");
    }

    public boolean isTrue() {
        return getNumberDouble()>0;
    }

    @Override
    public String toString() {
        return getTitle()+"="+isTrue();
    }

    @Override
    public StringVariable getStringVariable() {
        return new StringVariable(getTitle(), isTrue()+"");
    }
}
