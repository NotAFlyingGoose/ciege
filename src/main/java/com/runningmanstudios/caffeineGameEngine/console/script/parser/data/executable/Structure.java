package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable;

import com.runningmanstudios.caffeineGameEngine.console.script.OutPutter;
import com.runningmanstudios.caffeineGameEngine.console.script.checks.ParserException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.ScriptReader;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;

import java.util.Arrays;

public class Structure extends Executable implements Cloneable {

    OutPutter output;
    ScriptTokenizer tokenizer;
    public Structure(String title, String[] args, ScriptTokenizer tokenizer, OutPutter output) {
        super(title, args, tokenizer, output);
        this.tokenizer = tokenizer;
        this.output = output;
    }

    @Override
    public void execute(ScriptReader caller, Object[] args) {
        int argLength = args.length;
        if (args.length==1&&args[0].equals("")) argLength=0;
        int orgArgLength = this.args.length;
        if (this.args.length==1&&this.args[0].equals("")) orgArgLength=0;

        if (argLength != orgArgLength) throw new ParserException("incorrect number of arguments given while running \""+title+"\"; given : "+argLength+", expected : " + orgArgLength);
        for (int i = 0; i < this.args.length; i++) {
            String parameter = this.args[i];
            String value = (String) args[i];
            try {
                parser.addVariable(new Variable(parameter, value));
            } catch (ParserException p) {
                parser.setVariable(new Variable(parameter, value));
            }
        }
        parser.parse();
    }

    public Function getFunction(String name) {
        return parser.getFunction(name);
    }

    public Variable getVariable(String name) {
        return parser.getVariable(name);
    }

    @Override
    public String toString() {
        return title+"("+ Arrays.toString(this.args).substring(1, Arrays.toString(this.args).length()-1) +")";
    }

    public Object clone()
    {
        try {
        Structure structure = (Structure) super.clone();
        structure.parser = new ScriptReader(output, this.tokenizer);
        return structure;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
