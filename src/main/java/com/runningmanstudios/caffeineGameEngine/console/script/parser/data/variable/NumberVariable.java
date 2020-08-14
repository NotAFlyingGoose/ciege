package com.runningmanstudios.caffeineGameEngine.console.script.parser.data.variable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberVariable extends Variable {
    private static final DecimalFormat df = new DecimalFormat("0.#######");

    public NumberVariable(String title, String number) {
        super(title, format(number));
    }

    public NumberVariable(String title, Number number) {
        super(title, format(number.doubleValue()));
    }

    public static String format(String number) {
        double n = Double.parseDouble(number);
        if(n == (long) n)
            return String.format("%d",(long)n);
        else
            return String.format("%s",n);
    }
    public static String format(double number) {
        if(number == (long) number)
            return String.format("%d",(long)number);
        else
            return String.format("%s",number);
    }

    public double getNumberDouble() {
        try {
            return Double.parseDouble((String) getData());
        } catch (Exception e) {return 0;}
    }

    public int getNumberInt() {
        try {
            return Integer.parseInt((String) getData());
        } catch (Exception e) {return 0;}
    }

    public String getNumberString() {
        return df.format(getNumberDouble());
    }

    public Number getNumber() {
        try {
            return NumberFormat.getInstance().parse(getNumberString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public StringVariable getStringVariable() {
        return new StringVariable(getTitle(), getNumberString());
    }
}
