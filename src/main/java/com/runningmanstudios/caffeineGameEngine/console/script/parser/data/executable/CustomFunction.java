package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable;

import com.runningmanstudios.caffeineGameEngine.console.script.checks.ParserException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.Data;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;
import com.runningmanstudios.caffeineGameEngine.console.script.token.basic.Token;

import java.util.*;

public abstract class CustomFunction extends Data {
    Map<String, Variable> variables = new HashMap<>();
    String[] args;
    private Object typeReturn = null;

    public CustomFunction(String title, String... args) {
        super(title);
        this.args = args;
    }

    public void execute(Object[] args) {
        int argLength = args.length;
        if (args.length==1&&args[0].equals("")) argLength=0;
        int orgArgLength = args.length;
        if (this.args.length==1&&this.args[0].equals("")) orgArgLength=0;

        if (argLength != orgArgLength) throw new ParserException("incorrect number of arguments. given : "+argLength+", expected : " + orgArgLength);
        for (int i = 0; i < this.args.length; i++) {
            String parameter = this.args[i];
            String value = (String) args[i];
            variables.put(parameter, new Variable(parameter, value));
        }
        this.typeReturn = this.onRun();
    }

    public abstract Object onRun();

    public Variable getVariable(String variableName) {
        try {
            return variables.get(variableName);
        } catch (Exception e) { throw new ParserException("Function \"" + title + "\" doesn't exist"); }
    }

    @Override
    public String toString() {
        return title+"("+ Arrays.toString(this.args).substring(1, Arrays.toString(this.args).length()-1) +")";
    }

    public boolean matches(String name) {
        return name.equals(title);
    }

    public Object getTypeReturn() {
        return typeReturn;
    }
}
