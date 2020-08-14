/**
 * com.runningmanstudios.caffeineGameEngine.utilities is a bunch of utility classes that make it easier to do certain operations
 */
package com.runningmanstudios.caffeineGameEngine.util;

import java.io.Serializable;

/**
 * Fraction
 * a simple fraction class for better math with decimal points (e.g. 0.333 * 3 = 0.999 but 1/3 * 3 = 1)
 */
public class Fraction implements Serializable {
    public long numerator;
    public long denominator;

    /**
     * creates a fraction from a double
     * @param number double to convert into a fraction
     */
    public Fraction(double number) {
        if (!FancyMath.isWhole(number)) {
            long l = FancyMath.getDecimalString(number,true).length()-1;
            long amt = (long) Math.pow(10, l);
            this.numerator = (long) (number * amt);
            this.denominator = amt;
        } else {
            this.numerator = (long) number;
            this.denominator = 1;
        }

        this.simplify();
    }

    /**
     * creates a fraction from a numerator and a denominator
     * @param numerator numerator of fraction
     * @param denominator denominator of fraction
     */
    public Fraction(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * creates a fraction from a String (e.g. "1/3", "0.333")
     * @param value String to convert to a fraction
     */
    public Fraction(String value) {
        if (value.contains("/") && !value.contains(".")){
            String[] parts = value.split("/");
            this.numerator = Long.parseLong(parts[0]);
            this.denominator = Long.parseLong(parts[1]);
        } else {
            double number = Double.parseDouble(value);
            if (!FancyMath.isWhole(number)) {
                long l = FancyMath.getDecimalString(number, true).length()-1;
                long amt = (long) Math.pow(10, l);
                this.numerator = (long) (number * amt);
                this.denominator = amt;
            } else {
                this.numerator = (long) number;
                this.denominator = 1;
            }

            this.simplify();
        }
    }

    /**
     * returns two fractions with the same denominator
     * @param f Fraction
     * @return Two Fractions with the same denominator
     */
    public Fraction[] sameDenominator(Fraction f){
        Fraction nf1 = new Fraction(this.numerator * f.denominator, this.denominator * f.denominator);
        Fraction nf2 = new Fraction(f.numerator * this.denominator, f.denominator * this.denominator);
        return new Fraction[]{nf1, nf2};
    }

    /**
     * Adds two fractions together
     * @param f Second Fraction (Addend)
     * @return First Fraction plus Second Fraction (Sum)
     */
    public Fraction add(Fraction f) {
        Fraction[] nf = this.sameDenominator(f);
        return new Fraction(nf[0].numerator + nf[1].numerator, nf[0].denominator).simplify();
    }

    /**
     * Subtracts a fraction by another
     * @param f Fraction (Subtrahend)
     * @return First Fraction minus Second Fraction (Difference)
     */
    public Fraction subtract(Fraction f) {
        Fraction[] nf = this.sameDenominator(f);
        return new Fraction(nf[0].numerator - nf[1].numerator, nf[0].denominator).simplify();
    }

    /**
     * Multiples a fraction by another
     * @param f Second Fraction (Multiplier)
     * @return First Fraction multiplied by Second Fraction (Product)
     */
    public Fraction multiply(Fraction f) {
        return new Fraction(this.numerator * f.numerator, this.denominator * f.denominator).simplify();
    }

    /**
     * Divides a fraction by another
     * @param f Second Fraction (Divisor)
     * @return First Fraction multiplied by Second Fraction (Quotient)
     */
    public Fraction divide(Fraction f) {
        Fraction recriprocal = new Fraction(f.denominator, f.numerator);
        return this.multiply(recriprocal);
    }

    /**
     * simplifies the fraction
     * @return simplified fraction
     */
    public Fraction simplify() {
        int gcd = 1;
        for(int i = 1; i <= this.numerator && i <= this.denominator; i++)
        {
            if(this.numerator%i==0 && this.denominator%i==0)
                gcd = i;
        }
        return new Fraction(this.numerator/gcd, this.denominator/gcd);
    }

    /**
     * turns a fraction into a decimal
     * @return fraction -> float
     */
    public float toDeciamal() {
        return ((float) this.numerator) / this.denominator;
    }

    /**
     * turns a fraction into a String
     * @return "numerator/denominator"
     */
    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }
    
}
