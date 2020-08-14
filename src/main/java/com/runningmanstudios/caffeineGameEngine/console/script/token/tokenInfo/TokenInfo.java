package com.runningmanstudios.caffeineGameEngine.console.script.token.tokenInfo;

import com.runningmanstudios.caffeineGameEngine.console.script.token.basic.Token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenInfo
{
    public final Pattern regex;
    public final String tokenId;

    public TokenInfo(Pattern regex, String tokenId)
    {
        this.regex = regex;
        this.tokenId = tokenId;
    }

    public String[] matches(String str) {
        Matcher m = regex.matcher(str);
        if (m.find())
        {
            String tok = m.group().trim();
            str = m.replaceFirst("").trim();
            return new String[] {str, tok};
        }
        return new String[] {str, ""};
    }

    public Token getToken(String text, String tokenId) {
        return new Token(text, tokenId);
    }
}

