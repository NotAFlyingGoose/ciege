package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable;

import com.runningmanstudios.caffeineGameEngine.console.script.OutPutter;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.ScriptReader;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.Data;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;

public abstract class Executable extends Data {
    ScriptReader parser;
    String[] args;
    public Executable(String title, String[] args, ScriptTokenizer tokenizer, OutPutter output) {
        super(title);
        this.parser = new ScriptReader(output, tokenizer);
        this.parser.setName(title);
        this.args = args;
    }

    public abstract void execute(ScriptReader caller, Object[] args);
}
