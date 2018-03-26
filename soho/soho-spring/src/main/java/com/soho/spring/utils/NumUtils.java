package com.soho.spring.utils;

import java.math.BigDecimal;

/**
 * Created by shadow on 2017/5/17.
 */
public class NumUtils {

    public static String add(Object s1, Object s2, int scale) {
        BigDecimal b1 = new BigDecimal(s1.toString());
        BigDecimal b2 = new BigDecimal(s2.toString());
        BigDecimal ret = b1.add(b2);
        ret = ret.setScale(scale, BigDecimal.ROUND_HALF_DOWN);
        return ret.toString();
    }

    public static String subtract(Object s1, Object s2, int scale) {
        BigDecimal b1 = new BigDecimal(s1.toString());
        BigDecimal b2 = new BigDecimal(s2.toString());
        BigDecimal ret = b1.subtract(b2);
        ret = ret.setScale(scale, BigDecimal.ROUND_HALF_DOWN);
        return ret.toString();
    }

    public static String multiply(Object s1, Object s2, int scale) {
        BigDecimal b1 = new BigDecimal(s1.toString());
        BigDecimal b2 = new BigDecimal(s2.toString());
        BigDecimal ret = b1.multiply(b2);
        ret = ret.setScale(scale, BigDecimal.ROUND_HALF_DOWN);
        return ret.toString();
    }

    public static String divide(Object s1, Object s2, int scale) {
        BigDecimal b1 = new BigDecimal(s1.toString());
        BigDecimal b2 = new BigDecimal(s2.toString());
        if (b2.compareTo(new BigDecimal(0)) == 0) {
            return "0";
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_DOWN).toString();
    }

    // s1 < s2
    public static boolean compareToLT(Object s1, Object s2) {
        if (new BigDecimal(s1.toString()).compareTo(new BigDecimal(s2.toString())) < 0) {
            return true;
        }
        return false;
    }

    // s1 > s2
    public static boolean compareToGT(Object s1, Object s2) {
        if (new BigDecimal(s1.toString()).compareTo(new BigDecimal(s2.toString())) > 0) {
            return true;
        }
        return false;
    }

    // s1 <= s2
    public static boolean compareToLTE(Object s1, Object s2) {
        if (new BigDecimal(s1.toString()).compareTo(new BigDecimal(s2.toString())) <= 0) {
            return true;
        }
        return false;
    }

    // s1 >= s2
    public static boolean compareToGTE(Object s1, Object s2) {
        if (new BigDecimal(s1.toString()).compareTo(new BigDecimal(s2.toString())) >= 0) {
            return true;
        }
        return false;
    }

    // s1 = s2
    public static boolean compareToEQ(Object s1, Object s2) {
        if (new BigDecimal(s1.toString()).compareTo(new BigDecimal(s2.toString())) == 0) {
            return true;
        }
        return false;
    }

    // split number str
    public static String digit(Object number, int scale) {
        if (scale <= 0)
            scale = 0;
        String str1 = "";
        String str2 = "";
        String number1 = number.toString();
        if (number1.indexOf(".") != -1) {
            String[] split_str = number1.split("\\.");
            str1 = split_str[0];
            str2 = split_str[1];
            if (str2.length() > scale) {
                str2 = str2.substring(0, scale);
                if (!"".equals(str2)) {
                    str2 = cover4zero(str2, scale);
                }
            } else {
                str2 = cover4zero(str2, scale);
            }
            if (!"".equals(str2)) {
                str1 = str1 + "." + str2;
            }
        } else {
            str1 = number1;
            if (scale > 0) {
                str2 = cover4zero("0", scale);
            }
            if (!"".equals(str2)) {
                str1 = str1 + "." + str2;
            }
        }
        return str1;
    }

    private static String cover4zero(String str2, int scale) {
        if (str2.length() >= scale) {
            return str2;
        }
        int len = scale - str2.length();
        for (int i = 0; i < len; i++) {
            str2 = str2 + "0";
        }
        return str2;
    }

}
