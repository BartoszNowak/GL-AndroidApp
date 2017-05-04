package com.example.bartekpc.gl_shoppinglist;

import java.util.Locale;

public class DecimalFormatUtils
{
    private static final float EPSILON = 0.004f;

    public static String formatCurrency(float number)
    {
        if(Math.abs(Math.round(number) - number) < EPSILON)
        {
            return String.format(Locale.getDefault(), "%.0f", number);
        }
        else
        {
            return String.format(Locale.getDefault(), "%.2f", number);
        }
    }

    public static String formatAmount(float number)
    {
        if(Math.abs(Math.round(number) - number) < EPSILON)
        {
            return String.format(Locale.getDefault(), "%.0f", number);
        }
        else
        {
            return Float.toString(number);
        }
    }
}
