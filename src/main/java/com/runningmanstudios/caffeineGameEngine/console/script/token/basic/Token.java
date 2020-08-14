package com.runningmanstudios.caffeineGameEngine.console.script.token.basic;

public class Token {
    private final String id;
    private String value;

    public Token(String value, String id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getID() {
        return id;
    }

    public boolean equals(Token token) {
        return token.id.equals(this.id)&&token.value.equals(this.value);
    }

    @Override
    public String toString() {
        return id + ":" + value;
    }
}
