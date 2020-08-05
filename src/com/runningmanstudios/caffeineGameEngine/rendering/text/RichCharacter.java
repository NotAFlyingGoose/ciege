package com.runningmanstudios.caffeineGameEngine.rendering.text;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * RichCharacter is a character that has different properties, used with the RichTextFormatter
 * @see RichTextFormatter
 */
public class RichCharacter implements Serializable {
    private final char character;
    private final ArrayList<Integer> styles = new ArrayList<>();
    private Color color;

    /**
     * creates a RichCharacter
     * @param character character to represent
     * @param color color of the character
     * @param styles styles of the character, used in rendering
     */
    public RichCharacter(char character, Color color, int... styles) {
        this.character = character;
        this.color = color;
        for(int i:styles) {
            this.styles.add(i);
        }
    }

    /**
     * gets the character that this RichCharacter represents
     * @return this RichCharacter's char
     */
    public char getCharacter() {
        return this.character;
    }

    /**
     * gets the color of this RichCharacter
     * @return this RichCharacter's color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * sets the color of this RichCharacter
     * @param color new color of the RichCharacter
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * get's the style of this RichCharacter
     * @return the styles of this RichCharacter added together
     * @see java.awt.Font
     */
    public int getStyle() {
        int sum = 0;
        for (int i: this.styles) {
            sum += i;
        }
        return sum;
    }

    /**
     * sets the styles of this RichCharacter
     * @param styles the new styles
     * @see java.awt.Font
     */
    public void setStyles(int... styles) {
        this.styles.clear();
        for(int i:styles) {
            this.styles.add(i);
        }
    }

    /**
     * adds a style to this RichCharacter
     * @param style style to add
     * @see java.awt.Font
     */
    public void addStyle(int style) {
        this.styles.add(style);
    }

    /**
     * get's all the attributes of this RichCharacter and returns them as a String
     * @return this RichCharacter as a string
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("RichCharacter["+ this.character +"]");
        if (this.color != null) {
            s.append("["+ this.color.getRed()+","+ this.color.getGreen()+","+ this.color.getBlue()+"]");
        }
        if (this.styles != null) {
            s.append("["+ this.styles +"]");
        }
        return s.toString();
    }
}
