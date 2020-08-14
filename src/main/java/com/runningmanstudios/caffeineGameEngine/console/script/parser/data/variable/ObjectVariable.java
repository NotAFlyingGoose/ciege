package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable;

import com.runningmanstudios.caffeineGameEngine.console.script.parser.ScriptReader;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable.Structure;

public class ObjectVariable extends Variable {
    Structure structure;
    final Object[] startArgs;
    public ObjectVariable(String title, Structure structure, Object[] startArgs, ScriptReader caller) {
        super(title, structure);
        this.startArgs = startArgs;
        this.structure = (Structure) structure.clone();
        this.structure.execute(caller, startArgs);
    }

    public Structure getStructure() {
        return structure;
    }

    public Object[] getStartingParameters() {
        return startArgs;
    }
}
