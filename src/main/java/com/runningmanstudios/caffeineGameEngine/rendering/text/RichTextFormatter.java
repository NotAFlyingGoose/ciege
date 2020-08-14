package com.runningmanstudios.caffeineGameEngine.rendering.text;

import com.runningmanstudios.caffeineGameEngine.rendering.style.HtmlStyle;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * formats text using tags, like &quot;&lt;tag&gt;text&lt;/&gt;&quot;
 */
public class RichTextFormatter implements Serializable {
    private final StringBuilder rawText = new StringBuilder();
    private final ArrayList<String> tags = new ArrayList<>();
    private final ArrayList<RichCharacter> richText = new ArrayList<>();
    private String text;
    private String inside = "/";
    private String insideValue = "/";

    /**
     * creates rich text from tags
     * @param text text including tags
     */
    public RichTextFormatter(String text) {
        this.text = text;

        String s = text;
        int amt = this.countChar(text, '<');
        int subtract = 0;
        for (int i = 0; i < amt; i++) {
            int start = s.indexOf("<") + 1;
            int end = s.indexOf(">");
            s = s.substring(start);
            String o = s.substring(0, s.indexOf(">"));
            String n = o.replace(" ", "");
            n = n.replace(":", "=");
            this.text = this.text.replace(o, n);
            this.tags.add((start+subtract) + ":" + ((end+subtract)- this.countChar(o, ' ')) + ":" + n.toLowerCase());
            subtract+=end+1;
            s = s.substring(s.indexOf(">")+1);
        }
        this.rawText.append(this.text);
        for (char c : this.text.toCharArray()) {
            this.richText.add(new RichCharacter(c, null, 0));
        }

        this.affectTags();
    }

    /**
     * applies the tag's effect to text and then removes the tags from the text
     */
    private void affectTags() {
        for (int i = 0; i < this.rawText.length(); i++) {
            int tagStart = 0;
            String tagName = "default";
            String tagEqual = "default";
            int subtract = 0;
            for (String tag : this.tags) {
                int start = Integer.parseInt(tag.split(":")[0]) - 1;
                int end = Integer.parseInt(tag.split(":")[1]);
                if (start == i) {
                    tagStart = start;
                    String[] tagNames = tag.split(":")[2].split("=");
                    tagName = tagNames[0]!=null?tagNames[0]:"default";
                    tagEqual = tagNames.length>1&&tagNames[1]!=null?tagNames[1]:"default";
                    break;
                }
            }
            tagName = tagName.toLowerCase();
            tagEqual = tagEqual.toLowerCase();
            switch (tagName) {
                case "color", "b", "em", "emb", "/" -> {
                    this.inside = tagName;
                    this.insideValue = tagEqual;
                }
            }

            switch (this.inside) {
                case "color" -> {
                    Color c = HtmlStyle.getColor(this.insideValue);
                    this.richText.get(i).setColor(c);
                }
                case "b" -> this.richText.get(i).addStyle(Font.BOLD);
                case "em" -> this.richText.get(i).addStyle(Font.ITALIC);
                case "emb" -> {
                    this.richText.get(i).addStyle(Font.BOLD);
                    this.richText.get(i).addStyle(Font.ITALIC);
                }
            }
        }

        int amt = this.countChar(this.text, '<');
        for (int i = 0; i < amt; i++) {
            int start = this.rawText.indexOf("<");
            int end = this.rawText.indexOf(">") + 1;
            this.rawText.replace(start, end, "");
            this.richText.subList(start, end).clear();
        }
    }

    /**
     * gets the rich text that was effected by the tags
     * @return rich text
     */
    public ArrayList<RichCharacter> getRichText() {
        return this.richText;
    }

    /**
     * gets a rich character at the index
     * @param index the index
     * @return rich character
     */
    public RichCharacter getRichCharacter(int index) {
        try {
            return this.richText.get(index);
        } catch (IndexOutOfBoundsException e) {
            return new RichCharacter(' ', null, 0);
        }
    }

    /**
     * gets the raw text without tags
     * @return raw text
     */
    public String getRawText() {
        return this.rawText.toString();
    }

    /**
     * gets the amount of times a character appears in a string
     * @param str string to check
     * @param c character to count
     * @return amount of times c appears in str
     */
    private int countChar(String str, char c) {
        int count = 0;
        for(int i=0; i < str.length(); i++) {
            if(str.charAt(i) == c) count++;
        }
        return count;
    }

    /**
     * gets the raw text (the text given without tags)
     * @return raw text
     */
    @Override
    public String toString() {
        return this.rawText.toString();
    }

}
