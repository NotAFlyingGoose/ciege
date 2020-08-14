package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable;

import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.Data;

public class Variable extends Data {
    private Object data;

    public Variable(String title, Object data) {
        super(title);
        this.data = data;
    }

    @Override
    public String toString() {
        return title+"="+data;
    }

    /**
     * gets the information that this variable represents e.g. in "i = 1" data is "1"
     */
    public Object getData() {
        return data;
    }

    public void set(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        return title.equals(((Variable)obj).title);
    }

    public StringVariable getStringVariable() {
        return new StringVariable(getTitle(), (String) getData());
    }
}
