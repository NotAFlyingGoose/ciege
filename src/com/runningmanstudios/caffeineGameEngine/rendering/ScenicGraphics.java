/**
 * com.runningmanstudios.caffeineGameEngine.rendering contains classes for rendering things to the screen
 * @see com.runningmanstudios.caffeineGameEngine.rendering.ScenicGraphics
 */
package com.runningmanstudios.caffeineGameEngine.rendering;

import com.runningmanstudios.caffeineGameEngine.rendering.text.RichCharacter;
import com.runningmanstudios.caffeineGameEngine.rendering.text.RichTextFormatter;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.util.Arrays;

/**
 * ScenicGraphics is an extension of the basic abilities of Graphics and Graphics2D and
 * adds more functionality
 */
public class ScenicGraphics implements Serializable {
    private transient final Graphics2D g2d;
    private transient final Stroke originalStroke;
    private final Font originalFont;
    public boolean antialias = true;
    public boolean antialiasText = true;
    public boolean normalization = true;
    private Font lastFont;
    private Color lastColor;
    private final int lineHeightDist = 10;

    /**
     * creates a ScenicGraphics object
     * @param g2d takes in Graphics2D as a parameter
     */
    public ScenicGraphics(Graphics2D g2d){
        this.g2d = g2d;
        this.originalStroke = g2d.getStroke();
        this.originalFont = g2d.getFont();
        this.lastFont = g2d.getFont();
        this.lastColor = g2d.getColor();
    }

    /**
     * get the Graphics2D from this object
     * @return Graphics2D
     */
    public Graphics2D getGraphics(){
        return this.g2d;
    }

    /**
     * DISPLAY METHODS
     * for drawing to the screen
     */

    /**
     * draws a point at position x, y
     * @param x x of the point
     * @param y y of the point
     */
    public void displayPoint(long x, long y){
        this.g2d.drawLine((int)x, (int)y, (int)x, (int)y);
    }

    /**
     * draws a line from position x1, y1 to x2, y2
     * @param x1 x1 of the point
     * @param y1 y1 of the point
     * @param x2 x1 of the point
     * @param y2 y1 of the point
     */
    public void displayLine(long x1, long y1, long x2, long y2){
        this.g2d.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }

    /**
     * draws a rectangle to the screen
     * @param rect rectangle to draw
     * @param filled whether or not to fill the rectangle with color
     * @param centered whether or not to center the rectangle of the x and y positions
     */
    public void displayRect(Rectangle rect, boolean filled, boolean centered){
        if (filled) {
            if (centered){
                this.g2d.fillRect(rect.x - (rect.width / 2), rect.y - (rect.height / 2), rect.width, rect.height);
            } else {
                this.g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        } else {
            if (centered){
                this.g2d.drawRect(rect.x - (rect.width / 2), rect.y - (rect.height / 2), rect.width, rect.height);
            } else {
                this.g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }
    /**
     * draws a rectangle to the screen
     * @param x x of the rectangle to draw
     * @param y y of the rectangle to draw
     * @param width width of the rectangle to draw
     * @param height height of the rectangle to draw
     * @param filled whether or not to fill the rectangle with color
     * @param centered whether or not to center the rectangle of the x and y positions
     */
    public void displayRect(long x, long y, long width, long height, boolean filled, boolean centered){
        if (filled) {
            if (centered){
                this.g2d.fillRect((int) x - (int)(width / 2), (int) y - (int)(height / 2), (int) width, (int) height);
            } else {
                this.g2d.fillRect((int) x, (int) y, (int) width, (int) height);
            }
        } else {
            if (centered){
                this.g2d.drawRect((int) x - (int)(width / 2), (int) y - (int)(height / 2), (int) width, (int) height);
            } else {
                this.g2d.drawRect((int) x, (int) y, (int) width, (int) height);
            }
        }
    }

    /**
     * draws a polygon to the screen
     * @param polygon the polygon to draw
     * @param filled whether or not to fill the polygon with color
     */
    public void displayPolygon(Polygon polygon, boolean filled){
        if (filled) {
            this.g2d.fillPolygon(polygon);
        } else {
            this.g2d.drawPolygon(polygon);
        }
    }

    /**
     * draws an oval to the screen
     * @param x x of the oval to draw
     * @param y y of the oval to draw
     * @param width width of the oval to draw
     * @param height height of the oval to draw
     * @param filled whether or not to fill the oval with color
     * @param centered whether or not to center the oval of the x and y positions
     */
    public void displayOval(long x, long y, long width, long height, boolean filled, boolean centered){
        if (filled) {
            if (centered){
                this.g2d.fillOval((int) x - (int)(width / 2), (int) y - (int)(height / 2), (int) width, (int) height);
            } else {
                this.g2d.fillOval((int) x, (int) y, (int) width, (int) height);
            }
        } else {
            if (centered){
                this.g2d.drawOval((int) x - (int)(width / 2), (int) y - (int)(height / 2), (int) width, (int) height);
            } else {
                this.g2d.drawOval((int) x, (int) y, (int) width, (int) height);
            }
        }
    }

    /**
     * draws an oval to the screen
     * @param x x of the circle to draw
     * @param y y of the circle to draw
     * @param radius radius of the circle to draw
     * @param filled whether or not to fill the circle with color
     * @param centered whether or not to center the circle of the x and y positions
     */
    public void displayCircle(long x, long y, long radius, boolean filled, boolean centered){
        if (filled) {
            if (centered){
                this.g2d.fillOval((int) x - (int)(radius / 2), (int) y - (int)(radius / 2), (int) radius, (int) radius);
            } else {
                this.g2d.fillOval((int) x, (int) y, (int) radius, (int) radius);
            }
        } else {
            if (centered){
                this.g2d.drawOval((int) x - (int)(radius / 2), (int) y - (int)(radius / 2), (int) radius, (int) radius);
            } else {
                this.g2d.drawOval((int) x, (int) y, (int) radius, (int) radius);
            }
        }
    }

    public void displayImage(Image image, long x, long y) {
        this.g2d.drawImage(image, (int) x, (int) y, null);
    }

    public void displayImage(Image image, long x, long y, ImageObserver imageObserver) {
        this.g2d.drawImage(image, (int) x, (int) y, imageObserver);
    }

    public void displayImage(Image image, long x, long y, long width, long height, ImageObserver imageObserver) {
        this.g2d.drawImage(image, (int) x, (int) y, (int) width, (int) height, imageObserver);
    }

    public void displayImage(Image image, long x, long y, long width, long height) {
        this.g2d.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
    }

    /**
     * draws text to the screen
     * @param text text to draw
     * @param x x of the text to draw
     * @param y y of the text to draw
     * @param centered whether or not to center the text on the x
     */
    public void displayText(Object text, long x, long y, boolean centered){
        this.displayText(text, x, y, -1, -1, this.g2d.getFont(), centered);
    }

    /**
     * draws rich text to the screen
     * @param richText rich text to draw
     * @param x x of the text to draw
     * @param y y of the text to draw
     * @param centered whether or not to center the text on the x
     */
    public void displayText(RichTextFormatter richText, long x, long y, boolean centered){
        this.displayText(richText, x, y, -1, -1, this.g2d.getFont(), centered);
    }

    /**
     * draws text to the screen
     * @param text text to draw
     * @param x x of the text to draw
     * @param y y of the text to draw
     * @param maxWidth Maximum Width of the text to draw, if text is longer, add a line break. if less than 1 does not do anything
     * @param maxHeight Maximum Height of the text to draw, if text is longer, cutoff. if less than 1 does not do anything
     * @param centered whether or not to center the text on the x
     */
    public void displayText(Object text, long x, long y, int maxWidth, int maxHeight, boolean centered){
        this.displayText(text, x, y, maxWidth, maxHeight, this.g2d.getFont(), centered);
    }

    /**
     * draws rich text to the screen
     * @param richText rich text to draw
     * @param x x of the text to draw
     * @param y y of the text to draw
     * @param maxWidth Maximum Width of the text to draw, if text is longer, add a line break. if less than 1 does not do anything
     * @param maxHeight Maximum Height of the text to draw, if text is longer, cutoff. if less than 1 does not do anything
     * @param centered whether or not to center the text on the x
     */
    public void displayText(RichTextFormatter richText, long x, long y, int maxWidth, int maxHeight, boolean centered){
        this.displayText(richText, x, y, maxWidth, maxHeight, this.g2d.getFont(), centered);
    }

    /**
     * draws text to the screen
     * @param text text to draw
     * @param x x of the text to draw
     * @param y y of the text to draw
     * @param maxWidth Maximum Width of the text to draw, if text is longer, add a line break. if less than 1 does not do anything
     * @param maxHeight Maximum Height of the text to draw, if text is longer, cutoff. if less than 1 does not do anything
     * @param font font of the text to draw, will be reset
     * @param centered whether or not to center the text on the x
     */
    public void displayText(Object text, long x, long y, int maxWidth, int maxHeight, Font font, boolean centered){
        String textToDisplay = text.toString();
        textToDisplay = this.seperateWideString(textToDisplay, maxWidth);
        textToDisplay = this.separateHighString(textToDisplay, maxHeight);
        this.g2d.setFont(font);
        int lineHeight = this.g2d.getFontMetrics().getHeight()+ this.lineHeightDist;
        x = x - (centered ? (this.g2d.getFontMetrics().stringWidth(textToDisplay.split("\n")[0])/2) : 0);
        for (String line : textToDisplay.split("\n")) {
            char[] chars = line.toCharArray();
            long cx = x;
            y += lineHeight / 2;
            int width = 0;
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                this.g2d.drawString(String.valueOf(c), cx += width , y - (this.lineHeightDist / 2.0f));
                width = this.g2d.getFontMetrics().stringWidth(String.valueOf(c));
            }
        }
        this.lastFont();
    }
    /**
     * draws rich text to the screen
     * @param richText rich text to draw
     * @param x x of the text to draw
     * @param y y of the text to draw
     * @param maxWidth Maximum Width of the text to draw, if text is longer, add a line break. if less than 1 does not do anything
     * @param maxHeight Maximum Height of the text to draw, if text is longer, cutoff. if less than 1 does not do anything
     * @param font font of the text to draw, will be reset
     * @param centered whether or not to center the text on the x
     * @see RichTextFormatter
     */
    public void displayText(RichTextFormatter richText, long x, long y, int maxWidth, int maxHeight, Font font, boolean centered){
        String textToDisplay = richText.getRawText();
        textToDisplay = this.seperateWideString(textToDisplay, maxWidth);
        textToDisplay = this.separateHighString(textToDisplay, maxHeight);
        this.g2d.setFont(font);
        int lineHeight = this.g2d.getFontMetrics().getHeight()+ this.lineHeightDist;
        x = x - (centered ? (this.g2d.getFontMetrics().stringWidth(textToDisplay.split("\n")[0])/2) : 0);
        int cur = 0;
        for (int i = 0; i < textToDisplay.split("\n").length; i++) {
            String line = textToDisplay.split("\n")[i];
            char[] chars = line.toCharArray();
            long cx = x;
            y += lineHeight / 2;
            int width = 0;
            for (int j = 0; j < chars.length; j++) {
                RichCharacter richChar = richText.getRichCharacter(cur);
                if (richChar.getColor()!=null) {
                    this.setColor(richChar.getColor());
                }
                Font l = this.g2d.getFont();
                this.g2d.setFont(new Font(this.g2d.getFont().getName(), richChar.getStyle(), this.g2d.getFont().getSize()));
                this.g2d.drawString(String.valueOf(richChar.getCharacter()), cx += width , y - (this.lineHeightDist / 2.0f));
                width = this.g2d.getFontMetrics().stringWidth(String.valueOf(richChar.getCharacter()));
                if (richChar.getColor()!=null) {
                    this.lastColor();
                }
                this.g2d.setFont(l);
                cur++;
            }
            cur++;
        }
        this.lastFont();
    }

    /*
      GRAPHICAL SETTINGS
      for changing style
    */

    /**
     * turns on antialiasing
     * @param antialias to turn on antialiasing
     */
    public void doAntialiasing(boolean antialias) {
        this.antialias = antialias;
    }

    /**
     * turns on text antialiasing
     * @param antialias to turn on text antialiasing
     */
    public void doTextAntialiasing(boolean antialias) {
        this.antialiasText = antialias;
    }

    /**
     * turns on normalization
     * @param normalize to turn on normalizing
     */
    public void doNormalization(boolean normalize) {
        this.normalization = normalize;
    }

    /**
     * sets the stroke size
     * @param thickness size
     */
    public void lineThickness(int thickness){
        this.g2d.setStroke(new BasicStroke(thickness));
    }

    /**
     * resets to the original stroke size
     */
    public void resetLineThickness(){
        this.g2d.setStroke(this.originalStroke);
    }

    /**
     * sets the color
     * @param color color
     */
    public void setColor(Color color){
        this.lastColor = this.g2d.getColor();
        this.g2d.setColor(color);
    }

    /**
     * sets the color to be the last color
     */
    public void lastColor(){
        this.g2d.setColor(this.lastColor);
    }

    /**
     * Sets the current font
     * @param font font to use
     */
    public void setFont(Font font){
        this.lastFont = this.g2d.getFont();
        this.g2d.setFont(font);
    }

    /**
     * sets the size of the current font
     * @param fontSize new size
     */
    public void setFontSize(int fontSize) {
        this.lastFont = this.g2d.getFont();
        this.g2d.setFont(new Font(this.g2d.getFont().getFontName(), this.g2d.getFont().getStyle(), fontSize));
    }

    /**
     * sets the style of the current font
     * @param fontStyle new style
     */
    public void setFontStyle(int fontStyle) {
        this.lastFont = this.g2d.getFont();
        this.g2d.setFont(new Font(this.g2d.getFont().getFontName(), fontStyle, this.g2d.getFont().getSize()));
    }

    /**
     * sets the font to the last font used
     */
    public void lastFont() {
        this.g2d.setFont(this.lastFont);
    }

    /**
     * resets to the original font
     */
    public void resetFont(){
        this.g2d.setFont(this.originalFont);
    }

    /**
     * if text is wider than some value, a line break will be added
     * @param text text to separate
     * @param maxWidth maximum width that the text can be
     * @return new line broken text
     */
    public String seperateWideString(Object text, int maxWidth) {
        if (maxWidth > 0) {
            String[] wordsLB = text.toString().split("\n");
            for (int j = 0; j < wordsLB.length; j++) {
                String wordwithLB = wordsLB[j];
                String[] words = wordwithLB.split(" ");
                int currentAcceptableWidth = 0;
                for (int i = 0; i < words.length; i++) {
                    int len = this.g2d.getFontMetrics().stringWidth(words[i]);
                    currentAcceptableWidth += len;
                    if (currentAcceptableWidth > maxWidth && !words[i].contains("\n")) {
                        words[i] += "\n";
                        currentAcceptableWidth = 0;
                    }
                }
                wordsLB[j] = String.join(" ", words);
                wordsLB[j] = wordsLB[j].replaceAll("\n ", "\n");
                wordsLB[j] = wordsLB[j].trim();
            }
            return String.join("\n", wordsLB);
        }
        return text.toString();
    }

    /**
     * if text is longer than some value, text will be cut off
     * @param text text to separate
     * @param maxHeight maximum height that the text can be
     * @return new cutoff text
     */
    public String separateHighString(Object text, int maxHeight) {
        if (maxHeight > 0) {
            String s = text.toString();
            String[] n = s.split("\n");
            int total = 0;
            int lineHeight = this.g2d.getFontMetrics().getHeight()+ this.lineHeightDist;
            for (String line : n)
                total += lineHeight/2;

            if (total > maxHeight){
                n[n.length-1] = "";
                n[n.length-2] += "...";
                n = Arrays.copyOf(n, n.length-1);
                s = this.separateHighString(String.join("\n", n), maxHeight);
            }

            return s;
        }
        return text.toString();
    }
}
