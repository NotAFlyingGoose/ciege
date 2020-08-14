package com.runningmanstudios.caffeineGameEngine.console.script.token.basic;


import com.runningmanstudios.caffeineGameEngine.console.script.checks.CompileException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.ScriptReader;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;

import java.util.LinkedList;

public class FunctionToken extends Token {
    String functionName;
    ScriptTokenizer tokenizer = new ScriptTokenizer("args");
    //ScriptReader functionReader;
    public FunctionToken(String name, String parameters, String id) {
        super(name, id);
        this.functionName = name;
        tokenizer.tokenize(parameters);
        //functionReader = new ScriptReader(tokenizer).parse();
    }
    public String getFunctionName() {
        return functionName;
    }

    public String[] getParameters(boolean preventNonVars, LinkedList<Variable> variables) {
        StringBuilder param = new StringBuilder();
        for (Token token : tokenizer.getTokens()) {
            if (preventNonVars && !token.getID().equals(ScriptTokenizer.VARIABLE) && !token.getID().equals(ScriptTokenizer.COMMA)) throw new CompileException("You cannot give a value other than a variable for this:" + token.getID());

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
        }

        return args;
    }
}
