package com.runningmanstudios.caffeineGameEngine.console.script.token;

import com.runningmanstudios.caffeineGameEngine.console.script.checks.CompileException;
import com.runningmanstudios.caffeineGameEngine.console.script.token.basic.*;
import com.runningmanstudios.caffeineGameEngine.console.script.token.tokenInfo.NestedTokenInfo;
import com.runningmanstudios.caffeineGameEngine.console.script.token.tokenInfo.TokenInfo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

public class ScriptTokenizer {
    //these are all the characters that count as a string quote e.g. "hello", 'hello', `hello`.
    public static final String validQuotes = "\"'`";

    public static final String IMPORT = "usag";
    public static final String NULL = "null";
    public static final String TYPERETURN = "typeret";
    public static final String VARIABLEDEC = "vardec";
    public static final String FUNCTIONDEC = "funcdec";
    public static final String STRUCTUREDEC = "structdec";
    public static final String STRUCTUREINIT = "structinit";
    public static final String PARENTHESES = "par";
    public static final String SQUAREBRACKET = "sqrbrck";
    public static final String CODEBLOCK = "codeblock";
    public static final String MATHOPERATOR = "mathsn";
    public static final String MATH = "math";
    public static final String EQUALS = "set";
    public static final String STRING = "string";
    public static final String BOOLEANOPERATOR = "boolop";
    public static final String BOOLEAN = "bool";
    public static final String BOOLEANCONDITION = "boolcond";
    public static final String NUMBER = "num";
    public static final String FUNCTION = "func";
    public static final String DOT = "in";
    public static final String COMMA = "listsep";
    public static final String VARIABLE = "var";
    public static final String CONDITIONSTATEMENT = "state";
    public static final String COMMENT = "com";
    public static final String BLOCKCOMMENT = "blk";

    public static final Set<String> types = new HashSet<>();
    public static final Set<String> advancedTypes = new HashSet<>();
    static {
        types.add(NUMBER);
        types.add(STRING);
        types.add(BOOLEAN);
        types.add(VARIABLE);
        types.add(PARENTHESES);
        types.add(NULL);

        advancedTypes.addAll(types);
        advancedTypes.add(BOOLEANCONDITION);
        advancedTypes.add(MATH);
    }

    public static final Token epsilon = new Token("", "epsilon");
    private final LinkedList<TokenInfo> tokenInformation = new LinkedList<>();
    private LinkedList<Token> tokens = new LinkedList<>();
    private String __name__;

    public ScriptTokenizer() {
        this("script");
    }

    public ScriptTokenizer(String __name__) {
        this.__name__ = "__"+__name__+"__";
        //tokenInformation.add(new TokenInfo(Pattern.compile("^(var|string|boolean|number) "), PRIMITIVETYPE));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(\\/\\*([^]]+?)\\*\\/)"), BLOCKCOMMENT));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(use) "), IMPORT));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(var) "), VARIABLEDEC));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(func) "), FUNCTIONDEC));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(null)"), NULL));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(struct) "), STRUCTUREDEC));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(new) "), STRUCTUREINIT));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(return) "), TYPERETURN));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(if|while)"), CONDITIONSTATEMENT));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(["+validQuotes+"])(?:\\\\.|[^\\\\])*?\\1"), STRING));
        //tokenInformation.add(new TokenInfo(Pattern.compile("^([\\\"'])(?:\\\\.|[^\\\\])*?\\1"), STRING));//this also works as a string finder
        tokenInformation.add(new NestedTokenInfo('(', ')', PARENTHESES) {
            @Override
            public Token getToken(String text, String tokenId) {
                return new CodeToken(text, '(', ')', tokenId);
            }
        });
        tokenInformation.add(new NestedTokenInfo('{', '}', CODEBLOCK) {
            @Override
            public Token getToken(String text, String tokenId) {
                return new CodeToken(text, '{', '}', tokenId);
            }
        });
        tokenInformation.add(new NestedTokenInfo('[', ']', SQUAREBRACKET) {
            @Override
            public Token getToken(String text, String tokenId) {
                return new CodeToken(text, '[', ']', tokenId);
            }
        });
        tokenInformation.add(new TokenInfo(Pattern.compile("^(\\+|-|\\*|\\/|\\^)"), MATHOPERATOR));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(\\|\\||&&|==|<=|>=|<|>)"), BOOLEANOPERATOR));
        tokenInformation.add(new TokenInfo(Pattern.compile("^="), EQUALS));
        tokenInformation.add(new TokenInfo(Pattern.compile("^([0-9]+)(\\.[0-9]+)?"), NUMBER));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(true|false)"), BOOLEAN));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(\\.)"), DOT));
        tokenInformation.add(new TokenInfo(Pattern.compile("^(,)"), COMMA));
        tokenInformation.add(new TokenInfo(Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*)"), VARIABLE));
    }

    public ScriptTokenizer tokenize(String str) {
        return tokenize(str, true);
    }

    public ScriptTokenizer tokenize(String str, boolean simplify) {
        String s = str.trim();
        tokens.clear();
        while (!s.equals(""))
        {
            boolean match = false;
            for (TokenInfo info : tokenInformation) {
                String[] result = info.matches(s);
                if (!result[1].equals("")) {
                    match = true;
                    s = result[0];
                    tokens.add(info.getToken(result[1].replace("\n", " ").replace("\t", "\s"), info.tokenId));
                    break;
                }
            }
            if (!match) {
                throw new CompileException("Unexpected character in input: "+s.split(" ")[0]);
            }
        }

        if (simplify) {
            this.tokens = simplify(this.tokens);
        }

        return this;
    }

    public LinkedList<Token> simplify(LinkedList<Token> oldTokens) {
        LinkedList<Token> newTokens = new LinkedList<>();

        for (int i = 0; i < oldTokens.size()+6; i++) {
            Token addPar = null;
            Token lastToken4 = getTokenIfExists(oldTokens, i, 4);
            Token lastToken3 = getTokenIfExists(oldTokens, i, 3);
            Token lastToken2 = getTokenIfExists(oldTokens, i, 2);
            Token lastToken1 = getTokenIfExists(oldTokens, i, 1);
            Token lastToken = getTokenIfExists(oldTokens, i, 0);
            Token newToken2 = getTokenIfExists(newTokens, 2);
            Token newToken1 = getTokenIfExists(newTokens, 1);
            Token newToken = getTokenIfExists(newTokens, 0);

            //adding pars
            if (!newToken.getID().equals(STRUCTUREINIT) && !newToken.getID().equals(FUNCTION) && !newToken.getID().equals(STRUCTUREDEC) && !newToken.getID().equals(FUNCTIONDEC) && lastToken3.getID().equals(PARENTHESES)) {
                addPar = lastToken3;
            }
            //adding commas
            if (lastToken3.getID().equals(COMMA)) {
                newTokens.add(lastToken3);
            }

            //detecting a declaration of an object
            else if (lastToken3.getID().equals(STRUCTUREDEC) && lastToken2.getID().equals(VARIABLE)) {
                if (lastToken1.getID().equals(PARENTHESES)) {
                    StructureToken structureToken = new StructureToken(lastToken2.getValue(), replaceLast(lastToken1.getValue().replaceFirst("\\(", ""), "\\)", ""), STRUCTUREDEC);
                    newTokens.add(structureToken);
                } else {
                    throw new CompileException("Structure Declaration without parameter definition for: " + lastToken2.getValue());
                }
            }
            else if (lastToken3.getID().equals(VARIABLEDEC) && lastToken2.getID().equals(VARIABLE)) {
                newTokens.add(new Token(lastToken2.getValue(), VARIABLEDEC));
            }
            else if (lastToken3.getID().equals(FUNCTIONDEC) && lastToken2.getID().equals(VARIABLE)) {
                if (lastToken1.getID().equals(PARENTHESES)) {
                    FunctionToken functionToken = new FunctionToken(lastToken2.getValue(), replaceLast(lastToken1.getValue().replaceFirst("\\(", ""), "\\)", ""), FUNCTIONDEC);
                    newTokens.add(functionToken);
                } else {
                    throw new CompileException("Structure Declaration without parameter definition for: " + lastToken2.getValue());
                }
            }

            //variable checks
            //doing math operations
            else if (newToken.getID().equals(MATH) && lastToken2.getID().equals(MATHOPERATOR) && types.contains(lastToken1.getID())) {
                MathToken math = new MathToken(lastToken2.getValue(), newToken, lastToken1);
                newTokens.remove(newTokens.size()-1);
                newTokens.add(math);
            }
            else if (types.contains(lastToken3.getID()) && lastToken2.getID().equals(MATHOPERATOR) && types.contains(lastToken1.getID())) {
                MathToken math = new MathToken(lastToken2.getValue(), lastToken3, lastToken1);
                newTokens.add(math);
            }
            //doing boolean operations
            else if (newToken.getID().equals(BOOLEANCONDITION) && lastToken2.getID().equals(BOOLEANOPERATOR) && types.contains(lastToken1.getID())) {
                BoolToken bool = new BoolToken(lastToken2.getValue(), newToken, lastToken1);
                newTokens.remove(newTokens.size()-1);
                newTokens.add(bool);
            }
            else if (types.contains(lastToken3.getID()) && lastToken2.getID().equals(BOOLEANOPERATOR) && types.contains(lastToken1.getID())) {
                BoolToken bool = new BoolToken(lastToken2.getValue(), lastToken3, lastToken1);
                newTokens.add(bool);
            }
            //adding symbols
            else if (lastToken3.getID().equals(EQUALS)) {
                newTokens.add(lastToken3);
            }
            else if (lastToken3.getID().equals(NUMBER) && !newToken.getID().equals(MATH) && !newToken.getID().equals(BOOLEANCONDITION)) {
                newTokens.add(new Token(lastToken3.getValue(), NUMBER));
            }
            else if (lastToken3.getID().equals(STRING) && !newToken.getID().equals(MATH) && !newToken.getID().equals(BOOLEANCONDITION)) {
                newTokens.add(new Token(lastToken3.getValue(), STRING));
            }
            else if (lastToken3.getID().equals(BOOLEAN) && !newToken.getID().equals(MATH) && !newToken.getID().equals(BOOLEANCONDITION)) {
                newTokens.add(new Token(lastToken3.getValue(), BOOLEAN));
            }
            else if (lastToken2.getID().equals(CODEBLOCK)) {
                newTokens.add(lastToken2);
            }
            else if (lastToken4.getID().equals(STRUCTUREINIT) && lastToken3.getID().equals(VARIABLE) && lastToken2.getID().equals(PARENTHESES)) {
                StructureToken variable = new StructureToken(lastToken3.getValue(), replaceLast(lastToken2.getValue().replaceFirst("\\(", ""), "\\)", ""), STRUCTUREINIT);
                if (newToken.getID().equals(MATH) && !((MathToken)newToken).prevented() && ((MathToken)newToken).getValue(2).equals(variable)) {
                    ((MathToken) newToken).prevent();
                } else if (newToken.getID().equals(BOOLEANCONDITION) && !((BoolToken)newToken).prevented() && ((BoolToken)newToken).getValue(2).equals(variable)) {
                    ((BoolToken) newToken).prevent();
                } else {
                    newTokens.add(variable);
                }
            }
            else if (!lastToken4.getID().equals(VARIABLEDEC) && lastToken3.getID().equals(VARIABLE) && !lastToken2.getID().equals(PARENTHESES)) {
                Token variable = new Token(lastToken3.getValue(), VARIABLE);
                if (newToken.getID().equals(MATH) && !((MathToken)newToken).prevented() && ((MathToken)newToken).getValue(2).equals(variable)) {
                    ((MathToken) newToken).prevent();
                } else if (newToken.getID().equals(BOOLEANCONDITION) && !((BoolToken)newToken).prevented() && ((BoolToken)newToken).getValue(2).equals(variable)) {
                    ((BoolToken) newToken).prevent();
                } else {
                    newTokens.add(variable);
                }
            }
            else if (lastToken3.getID().equals(SQUAREBRACKET)) {
                newTokens.add(lastToken3);
            }

            //adding parentheses to functions and conditions
            else if (lastToken3.getID().equals(CONDITIONSTATEMENT) && lastToken2.getID().equals(PARENTHESES)) {
                newTokens.add(new FunctionToken(lastToken3.getValue(), replaceLast(lastToken2.getValue().replaceFirst("\\(", ""), "\\)", ""), CONDITIONSTATEMENT));
            }
            else if (lastToken3.getID().equals(DOT)) {
                newTokens.add(lastToken3);
            }
            else if (lastToken3.getID().equals(TYPERETURN)) {
                newTokens.add(lastToken3);
            }
            else if (lastToken3.getID().equals(IMPORT)) {
                newTokens.add(lastToken3);
            }
            else if (!lastToken4.getID().equals(FUNCTIONDEC) && !lastToken4.getID().equals(STRUCTUREDEC) && lastToken3.getID().equals(VARIABLE) && lastToken2.getID().equals(PARENTHESES)) {
                FunctionToken functionToken = new FunctionToken(lastToken3.getValue(), replaceLast(lastToken2.getValue().replaceFirst("\\(", ""), "\\)", ""), FUNCTION);
                newTokens.add(functionToken);
            }

            if (newToken1.getID().equals(VARIABLE) && newToken.getID().equals(MATH) && ((MathToken)newToken).getValue(1).equals(newToken1)) {
                if (newTokens.size()-2==0) newTokens.remove(0);
            }

            if (addPar!=null) newTokens.add(addPar);
        }

        int size = newTokens.size()+3;
        for (int i = 0; i < size; i++) {
            Token lastToken3 = getTokenIfExists(newTokens, i, 3);
            Token lastToken2 = getTokenIfExists(newTokens, i, 2);
            Token lastToken1 = getTokenIfExists(newTokens, i, 1);

            if (lastToken3.getID().equals(FUNCTION) && lastToken2.getID().equals(STRUCTUREINIT)) {
                removeTokenIfExists(newTokens, i, 3);
            }

            if (lastToken3.getID().equals(TYPERETURN) && advancedTypes.contains(lastToken2.getID())) {
                removeTokenIfExists(newTokens, i, 3);
                removeTokenIfExists(newTokens, i, 3);
                newTokens.add(Math.min(i-3, newTokens.size()), new ReferenceToken(lastToken2, TYPERETURN));
            }

            if (lastToken3.getID().equals(IMPORT) && lastToken2.getID().equals(STRING)) {
                removeTokenIfExists(newTokens, i, 3);
                removeTokenIfExists(newTokens, i, 3);
                newTokens.add(Math.min(i-3, newTokens.size()), new ReferenceToken(lastToken2, IMPORT));
            }

            //making sure that functions and structures have code blocks
            if (lastToken3.getID().equals(STRUCTUREDEC)) {
                if (!lastToken2.getID().equals(CODEBLOCK)) {
                    throw new CompileException("Structure Declaration without code block definition for: " + lastToken3.getValue());
                }
            } else if (lastToken3.getID().equals(FUNCTIONDEC)) {
                if (!lastToken2.getID().equals(CODEBLOCK)) {
                    throw new CompileException("Function Declaration without code block definition for: " + lastToken3.getValue());
                } else if (((CodeToken)lastToken2).hasInner(FUNCTIONDEC)){
                    throw new CompileException("Function Declaration with Function Declaration inside: " + lastToken3.getValue());
                } else if (((CodeToken)lastToken2).hasInner(STRUCTUREDEC)){
                    throw new CompileException("Function Declaration with Structure Declaration inside: " + lastToken3.getValue());
                }
            }
        }

        return newTokens;
    }

    public Token getTokenIfExists(LinkedList<Token> tokens, int subtract) {
        return getTokenIfExists(tokens, tokens.size()-1, subtract);
    }

    public static Token getTokenIfExists(LinkedList<Token> tokens, int index, int subtract) {
        try {
            return tokens.get(index - subtract);
        } catch (IndexOutOfBoundsException e) {
            return epsilon;
        }
    }

    public void removeTokenIfExists(LinkedList<Token> tokens, int subtract) {
        removeTokenIfExists(tokens, tokens.size()-1, subtract);
    }

    public static void removeTokenIfExists(LinkedList<Token> tokens, int index, int subtract) {
        try {
            tokens.remove(index - subtract);
        } catch (IndexOutOfBoundsException ignored) {
            ignored.printStackTrace();
        }
    }

    public String getStack() {
        StringBuilder code = new StringBuilder();
        code.append(getName());
        if (tokens.isEmpty()) {
            code.append("\n\tNo Tokens");
        }
        for (Token token : tokens) {
            code.append("\n").append(token.toString().replaceAll("(?m)^", "\t"));
        }
        return code.toString();
    }

    public String getStack(LinkedList<Token> tokens) {
        StringBuilder code = new StringBuilder();
        code.append(getName());
        if (tokens.isEmpty()) {
            code.append("\n\tNo Tokens");
        }
        for (Token token : tokens) {
            code.append("\n").append(token.toString().replaceAll("(?m)^", "\t"));
        }
        return code.toString();
    }

    public ScriptTokenizer printStack() {
        System.out.println(getStack());
        return this;
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    public String getName() {
        return __name__;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

    public void changeName(String title) {
        this.__name__ = "__"+title+"__";
    }

}
