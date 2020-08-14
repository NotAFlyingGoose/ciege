package com.runningmanstudios.caffeineGameEngine.console.script.token.basic;

import com.runningmanstudios.caffeineGameEngine.console.script.checks.CompileException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.BooleanVariable;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;

import java.util.Arrays;
import java.util.List;

import static com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer.*;

public class BoolToken extends Token {
    String operation;
    Token value1;
    Token value2;
    private boolean prevented = false;

    public BoolToken(String operation, Token value1, Token value2) {
        super("["+value1.getValue()+"]" + operation + "["+value2.getValue()+"]", BOOLEANCONDITION);
        this.operation = operation;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getValue(List<Variable> variables) {
        //values to apply math to
        String value1 = getMatch(this.value1, variables);
        String value2 = getMatch(this.value2, variables);

        //switch variable names with their values
        for (Variable v : variables) {
            if (v.titleMatch(value1)) value1 = (String) v.getData();
            if (v.titleMatch(value2)) value2 = (String) v.getData();
        }
        switch (operation) {
            case "==" -> {
                return getNumericValue(value1, value2, "==");
            }
            case "&&" -> {
                return getNumericValue(value1, value2, "&&");
            }
            case "||" -> {
                return getNumericValue(value1, value2, "||");
            }
            case "<" -> {
                return getNumericValue(value1, value2, "<");
            }
            case ">" -> {
                return getNumericValue(value1, value2, ">");
            }
            case "<=" -> {
                return getNumericValue(value1, value2, "<=");
            }
            case ">=" -> {
                return getNumericValue(value1, value2, ">=");
            }
            default -> throw new CompileException("unknown operation " + value1 + " " + operation + " " + value2);
        }
    }

    public void prevent() {
        this.prevented = true;
    }

    public boolean prevented() {
        return prevented;
    }

    public int getNumericValue(String value1, String value2, String operation) {
        if (value1.equals("true")) value1="1";
        else if (value1.equals("false")) value1="0";
        if (value2.equals("true")) value2="1";
        else if (value2.equals("false")) value2="0";

        boolean string = false;
        double n1 = value1.hashCode();
        double n2 = value2.hashCode();
        try {
            n1 = Double.parseDouble(value1);
            n2 = Double.parseDouble(value2);
        } catch (Exception e) { string = true; }
        boolean b1 = BooleanVariable.parse(n1);
        boolean b2 = BooleanVariable.parse(n2);
        switch (operation) {
            case "==" -> {
                return n1==n2?1:0;
            }
            case "&&" -> {
                if (string) throw new CompileException("You cannot do the operation \""+operation+"\" on a string");
                else return b1&&b2?1:0;
            }
            case "||" -> {
                if (string) throw new CompileException("You cannot do the operation \""+operation+"\" on a string");
                else return b1||b2?1:0;
            }
            case ">" -> {
                if (string) throw new CompileException("You cannot do the operation \""+operation+"\" on a string");
                else return n1>n2?1:0;
            }
            case "<" -> {
                if (string) throw new CompileException("You cannot do the operation \""+operation+"\" on a string");
                else return n1<n2?1:0;
            }
            case ">=" -> {
                if (string) throw new CompileException("You cannot do the operation \""+operation+"\" on a string");
                else return n1>=n2?1:0;
            }
            case "<=" -> {
                if (string) throw new CompileException("You cannot do the operation \""+operation+"\" on a string");
                else return n1<=n2?1:0;
            }
            default -> throw new CompileException("Illegal boolean operation: \""+operation+"\"");
        }
    }

    public String getMatch(Token token, List<Variable> variables) {
        if (token instanceof MathToken math) {
            return math.getValue(variables);
        } else if (token instanceof BoolToken bool) {
            return bool.getValue(variables)+"";
        } else if (token instanceof CodeToken code) {
            String[] result = code.getCompile(variables);
            if (result.length!=1) throw new CompileException("cant do boolean operation with lists: " + Arrays.toString(result));
            else {
                return result[0];
            }
        } else {
            return token.getValue();
        }
    }

    public Token getValue(int value) {
        if (value == 1) {
            return value1;
        } else return value2;
    }
}