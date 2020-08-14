package com.runningmanstudios.caffeineGameEngine.console.script.parser;

import com.runningmanstudios.caffeineGameEngine.console.script.CScript;
import com.runningmanstudios.caffeineGameEngine.console.script.ScriptManager;
import com.runningmanstudios.caffeineGameEngine.console.script.OutPutter;
import com.runningmanstudios.caffeineGameEngine.console.script.checks.ParserException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable.CustomFunction;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable.Function;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.executable.Structure;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.*;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;
import com.runningmanstudios.caffeineGameEngine.console.script.token.basic.*;

import java.util.LinkedList;

public class ScriptReader {
    ScriptTokenizer tokenizer;
    private final LinkedList<Token> tokens;
    private final LinkedList<Variable> variables = new LinkedList<>();
    private final LinkedList<Function> functions = new LinkedList<>();
    private final LinkedList<Structure> structures = new LinkedList<>();
    public final OutPutter output;

    //custom functions
    public final LinkedList<CustomFunction> customFunctions = new LinkedList<>();
    private Object typeReturn = null;
    private boolean parsing = false;

    public ScriptReader(OutPutter output, ScriptTokenizer tokenizer) {
        this.output = output;
        this.tokenizer = tokenizer;
        this.tokens = tokenizer.getTokens();

        //printing data to the screen
        customFunctions.add(new CustomFunction("print", "str") {
            @Override
            public Object onRun() {
                output.print(getVariable("str").getStringVariable().getContent());
                return null;
            }
        });
        customFunctions.add(new CustomFunction("println", "str") {
            @Override
            public Object onRun() {
                output.println(getVariable("str").getStringVariable().getContent());
                return null;
            }
        });
        customFunctions.add(new CustomFunction("printvars", "") {
            @Override
            public Object onRun() {
                for (Variable variable : ScriptReader.this.variables) {
                    output.println(variable);
                }
                return null;
            }
        });
        customFunctions.add(new CustomFunction("printfuncs", "") {
            @Override
            public Object onRun() {
                for (Function function : ScriptReader.this.functions) {
                    output.println(function);
                }
                return null;
            }
        });
        customFunctions.add(new CustomFunction("printstructs", "") {
            @Override
            public Object onRun() {
                for (Structure structure : ScriptReader.this.structures) {
                    output.println(structure);
                }
                return null;
            }
        });

        //Math functions
        customFunctions.add(new CustomFunction("root", "num", "n") {
            @Override
            public Object onRun() {
                Variable number = getVariable("num");
                Variable nth = getVariable("n");
                double n1;
                double n2;
                try {
                    n1 = Double.parseDouble((String) number.getData());
                    n2 = Double.parseDouble((String) nth.getData());
                } catch (Exception e) {
                    throw new ParserException("You must provide a number as a parameter for root(num, n)");
                }
                return NumberVariable.format(Math.round(Math.pow(n1, 1.0 / n2)));
            }
        });
        customFunctions.add(new CustomFunction("sqrt", "num") {
            @Override
            public Object onRun() {
                Variable number = getVariable("num");
                double n1;
                try {
                    n1 = Double.parseDouble((String) number.getData());
                } catch (Exception e) {
                    throw new ParserException("You must provide a number as a parameter for sqrt(num)");
                }
                return NumberVariable.format(Math.sqrt(n1));
            }
        });
        customFunctions.add(new CustomFunction("cbrt", "num") {
            @Override
            public Object onRun() {
                Variable number = getVariable("num");
                double n1;
                try {
                    n1 = Double.parseDouble((String) number.getData());
                } catch (Exception e) {
                    throw new ParserException("You must provide a number as a parameter for cbrt(num)");
                }
                return NumberVariable.format(Math.cbrt(n1));
            }
        });
        customFunctions.add(new CustomFunction("rnd", "num") {
            @Override
            public Object onRun() {
                Variable number = getVariable("num");
                double n1;
                try {
                    n1 = Double.parseDouble((String) number.getData());
                } catch (Exception e) {
                    throw new ParserException("You must provide a number as a parameter for rnd(num)");
                }
                return NumberVariable.format(Math.round(n1));
            }
        });
    }

    public Object getTypeReturn() {
        return typeReturn;
    }

    public ScriptReader parse() {
        parsing = true;
        switch (tokenizer.getName()) {
            case "__script__" -> {
                output.println("Scripter v0.2");
                tokens.addAll(0, new ScriptTokenizer().tokenize("var __name__=\"__script__\"").getTokens());
            }
            case "__used__" -> tokens.addAll(0, new ScriptTokenizer().tokenize("var __name__=\"__used__\"").getTokens());
            case "__unknown__" -> tokens.addAll(0, new ScriptTokenizer().tokenize("var __name__=\"__unknown__\"").getTokens());
        }
        try {
            tokenLoop:
            for (int i = 0; i < tokens.size()+5; i++) {
                if (!parsing) break;

                Token lastToken4 = ScriptTokenizer.getTokenIfExists(tokens, i, 4);
                Token lastToken3 = ScriptTokenizer.getTokenIfExists(tokens, i, 3);
                Token lastToken2 = ScriptTokenizer.getTokenIfExists(tokens, i, 2);
                Token lastToken1 = ScriptTokenizer.getTokenIfExists(tokens, i, 1);
                Token lastToken = ScriptTokenizer.getTokenIfExists(tokens, i, 0);

                tokenCheck:
                switch (lastToken3.getID()) {
                    //declaring a new variable
                    case ScriptTokenizer.VARIABLEDEC:
                        if (lastToken2.getID().equals(ScriptTokenizer.EQUALS)) {
                            if (lastToken1.getID().equals(ScriptTokenizer.MATH)) {
                                String result = ((MathToken) lastToken1).getValue(variables);
                                if (MathToken.isNumeric(result)) {
                                    addVariable(new NumberVariable(lastToken3.getValue(), result));
                                } else {
                                    addVariable(new StringVariable(lastToken3.getValue(), result));
                                }
                            } else if (lastToken1.getID().equals(ScriptTokenizer.BOOLEANCONDITION)) {
                                int result = ((BoolToken) lastToken1).getValue(variables);
                                addVariable(new BooleanVariable(lastToken3.getValue(), BooleanVariable.parse(result)));
                            } else {
                                String varName = lastToken3.getValue();
                                switch (lastToken1.getID()) {
                                    case ScriptTokenizer.VARIABLE -> {
                                        Variable set = getVariable(lastToken1.getValue());
                                        if (set instanceof StringVariable invar) {
                                            addVariable(new StringVariable(varName, invar.getContentRaw()));
                                        }
                                        else if (set instanceof BooleanVariable invar) {
                                            addVariable(new BooleanVariable(varName, invar.isTrue()));
                                        }
                                        else if (set instanceof NumberVariable invar) {
                                            addVariable(new NumberVariable(varName, invar.getNumber()));
                                        }
                                        else if (set instanceof ObjectVariable invar) {
                                            addVariable(new ObjectVariable(varName, invar.getStructure(), invar.getStartingParameters(), this));
                                        }
                                        else {
                                            addVariable(new Variable(varName, set.getData()));
                                        }
                                    }
                                    case ScriptTokenizer.STRING -> addVariable(new StringVariable(varName, lastToken1.getValue()));
                                    case ScriptTokenizer.NUMBER -> addVariable(new NumberVariable(varName, lastToken1.getValue()));
                                    case ScriptTokenizer.BOOLEAN -> addVariable(new BooleanVariable(varName, lastToken1.getValue()));
                                    case ScriptTokenizer.STRUCTUREINIT -> addVariable(new ObjectVariable(varName, getStructure(lastToken1.getValue()), ((StructureToken)lastToken1).getParameters(false, variables), this));
                                    case ScriptTokenizer.FUNCTION -> {
                                        Object typeReturn = findTypeReturn((FunctionToken)lastToken1);
                                        if (typeReturn==null) throw new ParserException("That function does not return anything. cannot assign to variable " + varName);
                                        addVariable(new Variable(varName, typeReturn));
                                    }
                                    default -> {
                                        if (!isValid(lastToken1.getValue())) throw new ParserException("Unknown Type assignment: " + lastToken1.getValue() + " to " + varName);
                                        else addVariable(new Variable(varName, lastToken1.getValue()));
                                    }
                                }
                            }
                        }
                        break;
                        //changing an old variable
                    case ScriptTokenizer.VARIABLE:
                        if (lastToken2.getID().equals(ScriptTokenizer.EQUALS)) {
                            if (lastToken1.getID().equals(ScriptTokenizer.MATH)) {
                                String result = ((MathToken) lastToken1).getValue(variables);
                                if (MathToken.isNumeric(result)) {
                                    setVariable(new NumberVariable(lastToken3.getValue(), result));
                                } else {
                                    setVariable(new StringVariable(lastToken3.getValue(), result));
                                }
                            } else if (lastToken1.getID().equals(ScriptTokenizer.BOOLEANCONDITION)) {
                                int result = ((BoolToken) lastToken1).getValue(variables);
                                setVariable(new BooleanVariable(lastToken3.getValue(), BooleanVariable.parse(result)));
                            } else {
                                String varName = lastToken3.getValue();
                                switch (lastToken1.getID()) {
                                    case ScriptTokenizer.VARIABLE -> {
                                        Variable set = getVariable(lastToken1.getValue());
                                        if (set instanceof StringVariable invar) {
                                            setVariable(new StringVariable(varName, invar.getContentRaw()));
                                        }
                                        else if (set instanceof BooleanVariable invar) {
                                            setVariable(new BooleanVariable(varName, invar.isTrue()));
                                        }
                                        else if (set instanceof NumberVariable invar) {
                                            setVariable(new NumberVariable(varName, invar.getNumber()));
                                        }
                                        else if (set instanceof ObjectVariable invar) {
                                            setVariable(new ObjectVariable(varName, invar.getStructure(), invar.getStartingParameters(), this));
                                        }
                                        else {
                                            setVariable(new Variable(varName, set.getData()));
                                        }
                                    }
                                    case ScriptTokenizer.STRING -> setVariable(new StringVariable(varName, lastToken1.getValue()));
                                    case ScriptTokenizer.NUMBER -> setVariable(new NumberVariable(varName, lastToken1.getValue()));
                                    case ScriptTokenizer.BOOLEAN -> setVariable(new BooleanVariable(varName, lastToken1.getValue()));
                                    case ScriptTokenizer.STRUCTUREINIT -> setVariable(new ObjectVariable(varName, getStructure(lastToken1.getValue()), ((StructureToken)lastToken1).getParameters(false, variables), this));
                                    case ScriptTokenizer.FUNCTION -> {
                                        Object typeReturn = findTypeReturn((FunctionToken)lastToken1);
                                        if (typeReturn==null) throw new ParserException("That function does not return anything. cannot assign to variable " + varName);
                                        setVariable(new Variable(varName, typeReturn));
                                    }
                                    default -> {
                                        if (!isValid(lastToken1.getValue())) throw new ParserException("Unknown Type assignment: " + lastToken1.getValue() + " to " + varName);
                                        else setVariable(new Variable(varName, lastToken1.getValue()));
                                    }
                                }
                            }
                        }
                        break;
                    case ScriptTokenizer.FUNCTIONDEC:
                        if (lastToken2.getID().equals(ScriptTokenizer.CODEBLOCK)) {
                            FunctionToken functionDecToken = (FunctionToken) lastToken3;
                            Function function = new Function(functionDecToken.getFunctionName(), functionDecToken.getParameters(true, new LinkedList<>()), ((CodeToken)lastToken2).getTokenizer(), output);
                            addFunction(function);
                        } else {
                            throw new ParserException("You must give code in curly braces \"{}\" to create a valid function");
                        }
                        break;
                    case ScriptTokenizer.FUNCTION:
                        FunctionToken functionRefToken = (FunctionToken) lastToken3;

                        for (CustomFunction customFunction : customFunctions) {
                            if (customFunction.matches(functionRefToken.getFunctionName())) {
                                customFunction.execute(functionRefToken.getParameters(false, variables));
                                break tokenCheck;
                            }
                        }
                        getFunction(functionRefToken.getFunctionName()).execute(this, functionRefToken.getParameters(false, variables));
                        break;
                    //declaring a new structure type
                    case ScriptTokenizer.STRUCTUREDEC:
                        if (lastToken2.getID().equals(ScriptTokenizer.CODEBLOCK)) {
                            StructureToken structureDecToken = (StructureToken) lastToken3;
                            Structure structure = new Structure(structureDecToken.getStructureName(), structureDecToken.getParameters(true, new LinkedList<>()), ((CodeToken)lastToken2).getTokenizer(), output);
                            addStructure(structure);
                        } else {
                            throw new ParserException("You must give code in curly braces \"{}\" to create a valid structure");
                        }
                        break;
                        //returning a variable
                    case ScriptTokenizer.TYPERETURN:
                        typeReturn = ((ReferenceToken)lastToken3).getReference().getValue();
                        break tokenLoop;
                        //returning a variable
                    case ScriptTokenizer.IMPORT:
                        importScript(((ReferenceToken)lastToken3).getReference().getValue());
                        break;
                        //condition statements (if, while)
                    case ScriptTokenizer.CONDITIONSTATEMENT:
                        if (lastToken2.getID().equals(ScriptTokenizer.CODEBLOCK)) {
                            FunctionToken conditionStatement = (FunctionToken) lastToken3;

                            String[] cond = conditionStatement.getParameters(false, variables);
                            if (cond.length>1) throw new ParserException("Cannot do conditional operation on switch");

                            Function function = new Function(conditionStatement.getFunctionName(), new String[]{""}, ((CodeToken)lastToken2).getTokenizer(), output);

                            if (conditionStatement.getFunctionName().equals("if")) {
                                if (cond[0].equals("1")) {
                                    function.execute(this, new Object[]{""});
                                }
                            }
                        } else {
                            throw new ParserException("You must give code in curly braces \"{}\" to create a valid statement");
                        }
                        break;
                }

            }
        } catch (Exception e) { output.printError(e); }
        if (tokenizer.getName().equals("__script__")) output.println("\nScript end");
        parsing = false;
        return this;
    }

    private void importScript(String value) {
        CScript use = output.getScript(value);
        if (use==null) throw new ParserException("Could not find script " + value);
        output.println("using script " + value);
        use.runAs("used");
        ScriptReader useReader = use.getParser();
        for (Variable v : useReader.variables) {
            try {
                getVariable(v.getTitle());
            } catch (Exception e) {
                addVariable(v);
            }
        }
        for (Function f : useReader.functions) {
            try {
                getFunction(f.getTitle());
            } catch (Exception e) {
                addFunction(f);
            }
        }
        for (Structure s : useReader.structures) {
            try {
                getStructure(s.getTitle());
            } catch (Exception e) {
                addStructure(s);
            }
        }
    }

    private Object findTypeReturn(FunctionToken function) {
        for (CustomFunction customFunction : customFunctions) {
            if (customFunction.matches(function.getFunctionName())) {
                customFunction.execute(function.getParameters(false, variables));
                return customFunction.getTypeReturn();
            }
        }
        getFunction(function.getFunctionName()).execute(this, function.getParameters(false, variables));
        return getFunction(function.getFunctionName()).getTypeReturn();
    }

    public void addFunction(Function function) {
        if (functions.contains(function)) throw new ParserException("Function \"" + function.getTitle() + "\" already exists");
        else functions.add(function);
    }

    public Function getFunction(String functionName) {
        for (Function function : functions) {
            if (function.getTitle().equals(functionName)) {
                return function;
            }
        }
        throw new ParserException("Function \"" + functionName + "\" doesn't exist!");
    }

    public void addStructure(Structure structure) {
        if (structures.contains(structure)) throw new ParserException("Structure \"" + structure.getTitle() + "\" already exists");
        else structures.add(structure);
    }

    public Structure getStructure(String structureName) {
        for (Structure structure : structures) {
            if (structure.getTitle().equals(structureName)) {
                return structure;
            }
        }
        throw new ParserException("Structure \"" + structureName + "\" doesn't exist!");
    }

    public void addVariable(Variable variable) {
        if (variables.contains(variable)) throw new ParserException("Variable \"" + variable.getTitle() + "\" already exists");
        else variables.add(variable);
    }

    public void setVariable(Variable newvar) {
        variables.set(variables.indexOf(getVariable(newvar.getTitle())), newvar);
    }

    public void setObject(String objectName, ObjectVariable object) {
        variables.set(variables.indexOf(getVariable(objectName)), object);
    }

    public Variable getVariable(String variableName) {
        for (Variable variable : variables) {
            if (variable.getTitle().equals(variableName)) {
                return variable;
            }
        }
        throw new ParserException("Variable \"" + variableName + "\" doesn't exist!");
    }

    public void setName(String title) {
        tokenizer.changeName(title);
    }

    public ScriptTokenizer getTokenizer() {
        return tokenizer;
    }

    public void run(String function, String... args) {
        for (CustomFunction customFunction : customFunctions) {
            if (customFunction.matches(function)) {
                customFunction.execute(args);
                return;
            }
        }
        getFunction(function).execute(this, args);
    }

    public boolean isValid(String text) {
        if (StringVariable.isValid(text)) return true;
        if (MathToken.isNumeric(text)) return true;
        return text.equals("true") || text.equals("false") || text.equals("null");
    }

    public void stop() {
        parsing = false;
    }

    public LinkedList<Variable> getVariables() {
        return variables;
    }

    public LinkedList<Function> getFunctions() {
        return functions;
    }
}
