package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable;

import com.runningmanstudios.caffeineGameEngine.console.script.OutPutter;
import com.runningmanstudios.caffeineGameEngine.console.script.checks.ParserException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.ScriptReader;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;
import com.runningmanstudios.caffeineGameEngine.console.script.token.basic.Token;

import java.util.Arrays;
import java.util.LinkedList;

public class Function extends Executable {
    String[] parameters;
    public Function(String title, String[] args, ScriptTokenizer tokenizer, OutPutter output) {
        super(title, args, tokenizer, output);
        this.parameters = args;
    }

    @Override
    public void execute(ScriptReader caller, Object[] args) {
        for (Variable variable : caller.getVariables()) {
            try {
                parser.addVariable(variable);
            } catch (Exception e) {
                parser.setVariable(variable);
            }
        }
        for (String parameter : parameters) {
            try {
                parser.addVariable(new Variable(parameter, new Object()));
            } catch (Exception e) {
                parser.setVariable(new Variable(parameter, new Object()));
            }
        }
        //new Throwable().printStackTrace();
        int argLength = args.length;
        if (args.length==1&&args[0].equals("")) argLength=0;
        int orgArgLength = this.args.length;
        if (this.args.length==1&&this.args[0].equals("")) orgArgLength=0;

        if (argLength != orgArgLength) throw new ParserException("incorrect number of arguments given while running \""+title+"\"; given : "+argLength+", expected : " + orgArgLength);
        for (int i = 0; i < this.args.length; i++) {
            String parameter = this.args[i];
            String value = (String) args[i];
            parser.setVariable(new Variable(parameter, value));
        }
        parser.parse();

        for (Variable var : caller.getVariables()) {
            try {
                if (!Arrays.asList(parameters).contains(var.getTitle()) && caller.getVariables().contains(parser.getVariable(var.getTitle()))) {
                    caller.setVariable(parser.getVariable(var.getTitle()));
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public String toString() {
        return title+"("+ Arrays.toString(this.args).substring(1, Arrays.toString(this.args).length()-1) +")";
    }

    public Object getTypeReturn() {
        return parser.getTypeReturn();
    }
}
