package com.runningmanstudios.caffeineGameEngine.console.script.token.basic;

import com.runningmanstudios.caffeineGameEngine.console.script.checks.CompileException;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.NumberVariable;
import com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable.Variable;
import com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer;

import java.util.Arrays;
import java.util.List;

import static com.runningmanstudios.caffeineGameEngine.console.script.token.ScriptTokenizer.*;

public class MathToken extends Token {
    String operation;
    Token value1;
    Token value2;
    private boolean prevented = false;

    public MathToken(String operation, Token value1, Token value2) {
        super("["+value1.getValue()+"]" + operation + "["+value2.getValue()+"]", MATH);
        this.operation = operation;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getValue(List<Variable> variables) {
        //values to apply math to
        String value1 = getMatch(this.value1, variables);
        String value2 = getMatch(this.value2, variables);

        //switch variable names with their values
        for (Variable v : variables) {
            if (v.titleMatch(value1)) value1 = (String) v.getData();
            if (v.titleMatch(value2)) value2 = (String) v.getData();
        }
        switch (operation) {
            case "+" -> {
                if (isNumeric(value1) && isNumeric(value2)) {
                    return getNumericCalculation(value1, value2, '+');
                }
                boolean stringmath = false;
                ScriptTokenizer tokenizer = new ScriptTokenizer("__math__").tokenize(value1 + " " + value2, false);
                if (tokenizer.getTokens().get(0).getID().equals(STRING)) {
                    value1 = tokenizer.getTokens().get(0).getValue().substring(1, tokenizer.getTokens().get(0).getValue().length()-1);
                    stringmath = true;
                }
                if (tokenizer.getTokens().get(1).getID().equals(STRING)) {
                    value2 = tokenizer.getTokens().get(1).getValue().substring(1, tokenizer.getTokens().get(1).getValue().length()-1);
                    stringmath = true;
                }

                if (stringmath) {
                    return "\"" + value1 + value2 + "\"";
                } else {
                    try {
                        if (tokenizer.getTokens().get(0).getValue().equals("true")) value1 = "1";
                        else if (tokenizer.getTokens().get(0).getValue().equals("false")) value1 = "0";

                        if (tokenizer.getTokens().get(1).getValue().equals("true")) value1 = "1";
                        else if (tokenizer.getTokens().get(1).getValue().equals("false")) value2 = "0";

                        return Double.parseDouble(getNumericCalculation(value1, value2, '+')) > 0 ? "true" : "false";
                    } catch (Exception e) { throw new CompileException("unknown variable in math operation: " + (value1 + "+" + value2)); }
                }
            }
            case "-" -> {
                return getNumericCalculation(value1, value2, '-');
            }
            case "*" -> {
                return getNumericCalculation(value1, value2, '*');
            }
            case "/" -> {
                return getNumericCalculation(value1, value2, '/');
            }
            case "^" -> {
                return getNumericCalculation(value1, value2, '^');
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

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String getNumericCalculation(String value1, String value2, char operation) {
        double n1 = Double.parseDouble(value1);
        double n2 = Double.parseDouble(value2);
        return switch (operation) {
            case '+' -> (NumberVariable.format(n1 + n2)) + "";
            case '-' -> (NumberVariable.format(n1 - n2)) + "";
            case '*' -> (NumberVariable.format(n1 * n2)) + "";
            case '/' -> (NumberVariable.format(n1 / n2)) + "";
            case '^' -> (NumberVariable.format(Math.pow(n1, n2))) + "";
            default -> "";
        };
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
                System.out.println(Arrays.toString(result));
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