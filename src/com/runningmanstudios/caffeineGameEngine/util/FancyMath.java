/**
 * com.runningmanstudios.caffeineGameEngine.utilities is a bunch of utility classes that make it easier to do certain operations
 */
package com.runningmanstudios.caffeineGameEngine.util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Util contains several QOL methods math-wise
 */
public class FancyMath implements Serializable {
    /**
     * take a string and convert it into a math operation
     * e.g. "1 + 1" -> 2
     */
    private static final ArrayList<Double> answers = new ArrayList<Double>();
    public static double stringMath(String operation) {
        operation = operation.replace("pi",Math.PI + "");
        double result = eval(operation);
        return result;
    }
    private static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                this.ch = (++this.pos < str.length()) ? str.charAt(this.pos) : -1;
            }

            boolean eat(int charToEat) {
                while (this.ch == ' ') this.nextChar();
                if (this.ch == charToEat) {
                    this.nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                this.nextChar();
                double x = this.parseExpression();
                if (this.pos < str.length()) throw new RuntimeException("Unexpected: " + (char) this.ch);
                answers.add(x);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = this.parseTerm();
                for (;;) {
                    if      (this.eat('+')) x += this.parseTerm(); // addition
                    else if (this.eat('-')) x -= this.parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = this.parseFactor();
                for (;;) {
                    if      (this.eat('*')) x *= this.parseFactor(); // multiplication
                    else if (this.eat('/')) x /= this.parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (this.eat('+')) return this.parseFactor(); // unary plus
                if (this.eat('-')) return -this.parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (this.eat('(')) { // parentheses
                    x = this.parseExpression();
                    this.eat(')');
                } else if ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') { // numbers
                    while ((this.ch >= '0' && this.ch <= '9') || this.ch == '.') this.nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (this.ch >= 'a' && this.ch <= 'z') { // functions
                    while (this.ch >= 'a' && this.ch <= 'z') this.nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = this.parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("ans")) x = this.getLastAnswer(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) this.ch);
                }

                if (this.eat('^')) x = Math.pow(x, this.parseFactor()); // exponentiation

                return x;
            }
            double getLastAnswer(double x) {
                if (x>0 && x<=answers.size()){
                    return answers.get(answers.size()-(int)x);
                }
                if (x==-1){
                    return 4.815162342;
                }

                return 0.0;
            }
        }.parse();
    }

    /**
     * converts a number on a range to another, maintaining ratio
     * @param oldMin Minimum number of the old range
     * @param oldMax Maximum number of the old range
     * @param newMin Minimum number of the new range
     * @param newMax Maximum number of the new range
     * @param value Value to change
     * @return changed value
     */
    public static double range(double oldMin, double oldMax, double newMin, double newMax, double value){
        double OldRange = (oldMax - oldMin);
        double NewRange = (newMax - newMin);
        double NewValue = (((value - oldMin) * NewRange) / OldRange) + oldMin;
        return NewValue;
    }

    /**
     * clamps a number between a range, but does not change the ratio
     * @param val value to clamp
     * @param min minimum value
     * @param max maximum value
     * @return value if value is in between min and max
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * clamps a number between 0 and 1, but does not change the ratio
     * @param val value to clamp
     * @return value if value is in between 0 and 1
     */
    public static float clamp01(float val) {
        return clamp(val, 0, 1);
    }

    /**
     * round a number to the nearest n
     * @param to round number to
     * @param value number to round
     * @return rounded number
     */
    public static double round(double to, double value) {
        // Smaller multiple
        double a = (value / to) * to;

        // Larger multiple
        double b = a + to;

        // Return of closest of two
        return (value - a > b - value)? b : a;
    }

    /**
     * input a decimal (e.g. 1.53) and get the decimal point as a string (e.g. ".53")
     * @param value value to get the decimal of
     * @param includeDecimalPoint whether or not to include the point in the String or not
     * @return only the decimal point part of value
     */
    public static String getDecimalString(double value, boolean includeDecimalPoint) {
        String doubleAsString = String.valueOf(value);
        int indexOfDecimal = doubleAsString.indexOf(".");
        return doubleAsString.substring(indexOfDecimal + (includeDecimalPoint ? 0 : 1));
    }

    /**
     * input a decimal (e.g. 1.53) and get the decimal point as a long (e.g. 0.53)
     * @param value value to get the decimal of
     * @param includeDecimalPoint whether or not to include the point in the Long or not
     * @return only the decimal point part of value
     */
    public static long getDecimalLong(double value, boolean includeDecimalPoint) {
        String doubleAsString = String.valueOf(value);
        int indexOfDecimal = doubleAsString.indexOf(".");
        return Long.parseLong(doubleAsString.substring(indexOfDecimal + (includeDecimalPoint ? 0 : 1)));
    }

    /**
     * input a decimal (e.g. 1.53) and get the integer (e.g. 1)
     * @return only the integer part of value
     */
    public static int getNumber(double value) {
        String doubleAsString = String.valueOf(value);
        int indexOfDecimal = doubleAsString.indexOf(".");
        return Integer.parseInt(doubleAsString.substring(0, indexOfDecimal));
    }

    /**
     * input a decimal (e.g. 1.53) and get the integer (e.g. 1)
     * @return only the integer part of value
     */
    public static String getNumberString(double value) {
        String doubleAsString = String.valueOf(value);
        int indexOfDecimal = doubleAsString.indexOf(".");
        return doubleAsString.substring(0, indexOfDecimal);
    }

    /**
     * get the greatest common factor of two numbers
     * @return gcd of v1 and v2
     */
    public static double getGCD(double v1, double v2) {
        int gcd = 1;
        for(int i = 1; i <= v1 && i <= v2; i++)
        {
            if(v1%i==0 && v2%i==0)
                gcd = i;
        }
        return gcd;
    }

    /**
     * is a double a whole number
     * @return whether or not number is a whole number
     */
    public static boolean isWhole(double number) {
        return number - Math.floor(number) == 0;
    }

    /**
     * Smooth min method based on https://www.iquilezles.org/www/articles/smin/smin.htm
     * @param a first number
     * @param b second number
     * @param k how smooth to make the minimum
     * @return the smooth minimum of a and b
     */
    public static float smin(float a, float b, float k) {
        float h = Math.max(0, Math.min(1, ((b-a)/k)+.5f));
        float m = h * (1 - h) * k;
        float n = h * (a) + (1 - h) * b - m * 0.5f;
        return n;
    }

    /**
     * to get the smooth maximum, you just insert a -k value for smooth minimum
     * @param a first number
     * @param b second number
     * @param k how smooth to make the minimum
     * @return the smooth maximum of a and b
     */
    public static float smax(float a, float b, float k) {
        return smin(a, b, -k);
    }


    /**
     * Bias method, taken from a clip of a Sebastian Lague video : https://www.youtube.com/watch?v=lctXaT9pxA0
     * as bias goes closer to 1, x will fall to 0 more. if bias is 0, x will be unaffected.
     * @param x number
     * @param bias bias
     * @return biased number
     */
    public static float bias (float x, float bias) {
        //adjust input to make control more linear
        float k = (float)Math.pow(1.0f - bias, 3);
        // Equation based on : https://www.shadertoy.com/view/Xd2yRd
        return (x * k) / (x * k - x + 1);
    }
}
