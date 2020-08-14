package com.runningmanstudios.caffeineGameEngine.console.script;

import com.runningmanstudios.caffeineGameEngine.console.script.parser.ScriptReader;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;

public class CScript {
    ScriptReader parser;
    public CScript(OutPutter output, String input) {
        parser = new ScriptReader(output, new ScriptTokenizer().tokenize(input));
    }

    public void run() {
        parser.setName("script");
        parser.parse();
    }

    public void runAs(String name) {
        parser.setName(name);
        parser.parse();
    }

    public ScriptReader getParser() {
        return parser;
    }

}
