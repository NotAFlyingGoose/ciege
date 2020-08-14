package com.runningmanstudios.caffeineGameEngine.console.script.token.basic;

public class ReferenceToken extends Token {
    Token reference;
    String value;
    public ReferenceToken(Token reference, String id) {
        this("ref", reference, id);
    }

    public ReferenceToken(String value, Token reference, String id) {
        super(value+"->"+reference.getValue(), id);
        this.reference = reference;
        this.value = value;
    }

    public Token getReference() {
        return this.reference;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
