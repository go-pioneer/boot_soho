package com.soho.spring.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

/**
 * 封装数学计算工具类
 * A-加法 S-减法 M-乘法 D-除法
 * LT-小于 LTE-小于等于 GT-大于 GTE-大于等于 EQ-等于
 *
 * @Created by shadow on 2017/5/17.
 */
public class EMath {

    private final static int DEFAULT_SCALE = 4;  // 默认保留位数

    private static int[] numbers = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * a+b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String A(Object s1, Object s2, int scale) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        BigDecimal decimal = b1.add(b2);
        return digit(decimal, scale);
    }

    /**
     * a+b=?
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return String 计算结果
     */
    public static String A(Object s1, Object s2) {
        return A(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a-b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String S(Object s1, Object s2, int scale) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        BigDecimal decimal = b1.subtract(b2);
        return digit(decimal, scale);
    }

    /**
     * a-b=?
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return String 计算结果
     */
    public static String S(Object s1, Object s2) {
        return S(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a*b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String M(Object s1, Object s2, int scale) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        if (b1.compareTo(BigDecimal.ZERO) == 0 || b2.compareTo(BigDecimal.ZERO) == 0) {
            return digit(0, scale);
        }
        BigDecimal decimal = b1.multiply(b2);
        return digit(decimal, scale);
    }

    /**
     * a*b=?
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return String 计算结果
     */
    public static String M(Object s1, Object s2) {
        return M(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a*b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String D(Object s1, Object s2, int scale) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        BigDecimal decimal = BigDecimal.ZERO;
        if (b1.compareTo(BigDecimal.ZERO) != 0 && b2.compareTo(BigDecimal.ZERO) != 0) {
            decimal = b1.divide(b2, 20, BigDecimal.ROUND_DOWN);
        }
        return digit(decimal, scale);
    }

    /**
     * a*b=?
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return String 计算结果
     */
    public static String D(Object s1, Object s2) {
        return D(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a < b = true or false
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return true or false
     */
    public static boolean LT(Object s1, Object s2) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        if (b1.compareTo(b2) < 0) {
            return true;
        }
        return false;
    }

    /**
     * a > b = true or false
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return true or false
     */
    public static boolean GT(Object s1, Object s2) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        if (b1.compareTo(b2) > 0) {
            return true;
        }
        return false;
    }

    /**
     * a <= b = true or false
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return true or false
     */
    public static boolean LTE(Object s1, Object s2) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        if (b1.compareTo(b2) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * a >= b = true or false
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return true or false
     */
    public static boolean GTE(Object s1, Object s2) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        if (b1.compareTo(b2) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * a == b = true or false
     *
     * @param s1 数值a
     * @param s2 数值b
     * @return true or false
     */
    public static boolean EQ(Object s1, Object s2) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        if (b1.compareTo(b2) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 格式化数值
     *
     * @param number 数值参数
     * @param scale  保留位数
     * @return 数值字符串
     */
    public static String digit(Object number, int scale) {
        StringBuffer buffer = new StringBuffer("0");
        if (scale > 0) {
            buffer.append(".");
        }
        for (int i = 0; i < scale; i++) {
            buffer.append("0");
        }
        return new DecimalFormat(buffer.toString()).format(toBigDecimal(number));
    }

    /**
     * 格式化数值
     *
     * @param number 数值参数
     * @return 数值字符串
     */
    public static String digit(Object number) {
        return digit(number, DEFAULT_SCALE);
    }

    /**
     * 转换参数对象为数值
     *
     * @param object 参数对象
     * @return 数值对象
     */
    private static BigDecimal toBigDecimal(Object object) {
        String string = null;
        if (object instanceof Integer) {
            string = Integer.toString((Integer) object);
        } else if (object instanceof Long) {
            string = Long.toString((Long) object);
        } else if (object instanceof Double) {
            string = Double.toString((Double) object);
        } else if (object instanceof Float) {
            string = Float.toString((Float) object);
        } else if (object instanceof BigDecimal) {
            return (BigDecimal) object;
        } else {
            string = String.valueOf(object);
        }
        return new BigDecimal(string);
    }

    /**
     * 生成指定位数随机码
     *
     * @param len 随机码位数
     * @return 随机码字符串
     */
    public static String getIntSN(int len) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < (len < 1 ? 1 : len); i++) {
            buffer.append(numbers[random.nextInt(numbers.length)]);
        }
        return buffer.toString();
    }

    /**
     * 生成UUID随机码
     *
     * @return UUID随机码
     */
    public static String getStrSN() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(A(5.46578974654, 1));
        System.out.println(S(1278.456878764654, 8.46787));
        System.out.println(M(0.057487, 1.05458787));
        System.out.println(D(1, 3));
        System.out.println(digit(1.045787431));
    }

}
