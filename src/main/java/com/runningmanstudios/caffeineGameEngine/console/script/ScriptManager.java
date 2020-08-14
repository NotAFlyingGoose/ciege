package com.runningmanstudios.caffeineGameEngine.console.script;

import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.StringVariable;

import java.util.HashMap;
import java.util.Map;

public class ScriptManager {
    public final Map<String, CScript> scripts = new HashMap<>();

    public ScriptManager useScript(String location, CScript script) {
        scripts.put(location, script);
        return this;
    }

    public ScriptManager removeScript(String location) {
        scripts.remove(location);
        return this;
    }

    public CScript getScript(String location) {
        return scripts.getOrDefault(new StringVariable("loc",location).getContent(), null);
    }

    @Override
    public String toString() {
        return scripts.toString();
    }
}
