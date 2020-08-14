package com.runningmanstudios.caffeineGameEngine.console.script.token.basic;

import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;

import java.util.LinkedList;
import java.util.List;

public class CodeToken extends Token {
    private final ScriptTokenizer tokenizer;
    private final LinkedList<Token> tokens;

    public CodeToken(String value, char open, char close, String id) {
        super(value, id);
        value = value.substring(value.indexOf(open)+1, value.lastIndexOf(close));
        tokenizer = new ScriptTokenizer(id);
        tokenizer.tokenize(value);
        tokens = tokenizer.getTokens();
    }

    @Override
    public String toString() {
        return tokenizer.getStack();
    }

    public boolean hasInner(String id) {
        for (Token token : tokens) {
            if (token.getID().equals(id)) return true;
        }
        return false;
    }

    public Token getInner(String id) {
        for (Token token : tokens) {
            if (token.getID().equals(id)) return token;
        }
        return ScriptTokenizer.epsilon;
    }

    public Token getLastInner(String id) {
        Token last = null;
        for (Token token : tokens) {
            if (token.getID().equals(id)) last = token;
        }
        if (last==null) return ScriptTokenizer.epsilon;
        else return last;
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    public ScriptTokenizer getTokenizer() {
        return this.tokenizer;
    }

    public String[] getCompile(List<Variable> variables) {
        StringBuilder param = new StringBuilder();
        for (Token token : tokenizer.getTokens()) {
            if (token instanceof MathToken math) {
                param.append(math.getValue(variables));
            } else if (token instanceof BoolToken bool) {
                param.append(bool.getValue(variables));
            } else param.append(token.getValue());
        }
        String[] args = param.toString().split(",");

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            for (Variable var : variables) {
                if (var.getTitle().equals(arg)) {
                    args[i] = (String) var.getData();
                }
            }

            if (args[i].equals("")) {
                args[i] = "null";
            }
        }

        return args;
    }
}
