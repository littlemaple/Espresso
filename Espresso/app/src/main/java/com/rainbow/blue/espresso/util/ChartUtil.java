package com.rainbow.blue.espresso.util;

import android.support.annotation.NonNull;

import com.rainbow.blue.espresso.chart.Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2015/10/20.
 */
public class ChartUtil {
    /**
     * The number of individual points (samples) in the chart series to draw onscreen.
     */
    public static final int DRAW_STEPS = 30;
    private static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};

    /**
     * The simple math function Y = fun(X) to draw on the chart.
     *
     * @param x The X value
     * @return The Y value
     */
    public static float fun(float x) {
        return (float) Math.pow(x, 3) - x / 4;
    }

    /**
     * Rounds the given number to the given number of significant digits. Based on an answer on
     * <a href="http://stackoverflow.com/questions/202302">Stack Overflow</a>.
     */
    public static float roundToOneSignificantFigure(double num) {
        final float d = (float) Math.ceil((float) Math.log10(num < 0 ? -num : num));
        final int power = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, power);
        final long shifted = Math.round(num * magnitude);
        return shifted / magnitude;
    }

    /**
     * Formats a float value to the given number of decimals. Returns the length of the string.
     * The string begins at out.length - [return value].
     */
    public static int formatFloat(final char[] out, float val, int digits) {
        boolean negative = false;
        if (val == 0) {
            out[out.length - 1] = '0';
            return 1;
        }
        if (val < 0) {
            negative = true;
            val = -val;
        }
        if (digits > POW10.length) {
            digits = POW10.length - 1;
        }
        val *= POW10[digits];
        long lval = Math.round(val);
        int index = out.length - 1;
        int charCount = 0;
        while (lval != 0 || charCount < (digits + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            out[index--] = (char) (digit + '0');
            charCount++;
            if (charCount == digits) {
                out[index--] = '.';
                charCount++;
            }
        }
        if (negative) {
            out[index--] = '-';
            charCount++;
        }
        return charCount;
    }


    public static String formatDuration(int seconds) {
        int hour = seconds / 3600;
        if (hour > 0) {
            int min = (seconds % 3600) / 60;
            int second = seconds % 60;
            return hour + "h" + min + "m" + second + "s";
        } else {
            int min = (seconds % 3600) / 60;
            int second = seconds % 60;
            return min + "m" + second + "s";
        }
    }

    @Deprecated
    public static List<Integer> getSpecialLabel(float start, float end, @NonNull Mode mode) {
        List<Integer> list = new ArrayList<>();
        if (mode == Mode.Sec) {
            int pre = (int) (start / (60 * 5) * 5 * 60);
            int next = (int) (end / (60 * 5) * 5 * 60);
            while (pre < next) {
                list.add(pre);
                pre += 5 * 60;
            }
        } else {
            int pre = (int) (start / (60 * 60) * 60 * 60);
            int next = (int) (end / (60 * 60) * 60 * 60);
            while (pre < next) {
                list.add(pre);
                pre += 60 * 60;
            }
        }
        return list;
    }
}
