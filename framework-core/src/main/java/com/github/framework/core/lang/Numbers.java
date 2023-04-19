package com.github.framework.core.lang;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.DecimalFormat;

/**
 * BigDecimal工具类

 */
public class Numbers extends NumberUtils {

    public static final int INT_0 = 0;

    public static final int INT_1 = 1;

    public static final int INT_2 = 2;

    public static final int INT_3 = 3;

    public static final int INT_4 = 4;

    public static final int INT_5 = 5;

    public static final int INT_6 = 6;

    public static final int INT_7 = 7;

    public static final int INT_8 = 8;

    public static final int INT_9 = 9;

    public static final int INT_10 = 10;

    private Numbers() {}

    /**
     * 格化数字
     * @param number
     * @param formatter
     * @return
     */
    public static String format(final Number number , final String formatter) {
        DecimalFormat df = new DecimalFormat(formatter);
        return df.format(number);
    }
}
