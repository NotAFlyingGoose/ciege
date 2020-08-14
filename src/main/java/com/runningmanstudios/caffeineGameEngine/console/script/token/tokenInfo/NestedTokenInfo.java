package com.runningmanstudios.caffeineGameEngine.console.script.token.tokenInfo;

import com.runningmanstudios.caffeineGameEngine.console.script.checks.CompileException;

import java.util.regex.Pattern;

public class NestedTokenInfo extends TokenInfo {
    private char open;
    private char close;

    public NestedTokenInfo(char open, char close, String tokenId) {
        super(Pattern.compile("^\\"+open), tokenId);
        this.open = open;
        this.close = close;
    }

    @Override
    public String[] matches(String str) {
        String[] result = super.matches(str);
        int depth = 0;
        StringBuilder match = new StringBuilder();
        if (!result[1].equals("")) {
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                match.append(c);
                if (c==open) {
                    depth++;
                }
                if (c==close) {
                    depth--;
                }

                if (depth==0) {
                    str = str.substring(match.length()).trim();
                    result[0] = str;
                    result[1] = match.toString();
                    break;
                }
            }
            if (depth!=0) throw new CompileException("opening a nest without closing: " + open + close);
            else return result;
        } else {
            return result;
        }
    }
}
