package com.soho.spring.utils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * 封装数学计算工具类,统一规范舍位不进
 * add-加法 subtract-减法 multiply-乘法 divide-除法
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
    public static String add(Object s1, Object s2, int scale) {
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
    public static String add(Object s1, Object s2) {
        return add(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a-b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String subtract(Object s1, Object s2, int scale) {
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
    public static String subtract(Object s1, Object s2) {
        return subtract(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a*b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String multiply(Object s1, Object s2, int scale) {
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
    public static String multiply(Object s1, Object s2) {
        return multiply(s1, s2, DEFAULT_SCALE);
    }

    /**
     * a*b=?
     *
     * @param s1    数值a
     * @param s2    数值b
     * @param scale 保留位数
     * @return String 计算结果
     */
    public static String divide(Object s1, Object s2, int scale) {
        BigDecimal b1 = toBigDecimal(s1);
        BigDecimal b2 = toBigDecimal(s2);
        BigDecimal decimal = BigDecimal.ZERO;
        if (b1.compareTo(BigDecimal.ZERO) != 0 && b2.compareTo(BigDecimal.ZERO) != 0) {
            decimal = b1.divide(b2, 32, BigDecimal.ROUND_DOWN);
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
    public static String divide(Object s1, Object s2) {
        return divide(s1, s2, DEFAULT_SCALE);
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
     * @param object 数值参数
     * @param scale  保留位数
     * @return 数值字符串
     */
    public static String digit(Object object, int scale) {
        if (scale <= 0)
            scale = 0;
        String str1 = "";
        String str2 = "";
        String number1 = toBigDecimal(object).toPlainString();
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

    // 截断或填充差位0
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

    /**
     * 格式化数值
     *
     * @param object 数值参数
     * @return 数值字符串
     */
    public static String digit(Object object) {
        return digit(object, DEFAULT_SCALE);
    }

    /**
     * 转换参数对象为数值
     *
     * @param object 参数对象
     * @return 数值对象
     */
    public static BigDecimal toBigDecimal(Object object) {
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
        System.out.println(EMath.add(5.46578974654, 1, 5));
        System.out.println(EMath.add("5.46578974654", 1));


        System.out.println(EMath.subtract(1278.456878764654f, 8.46787d, 18));
        System.out.println(EMath.subtract(new BigDecimal(1278.456878764654), 8.46787));

        System.out.println(EMath.multiply("-0.057487", new BigDecimal(1.05458787), 15));
        System.out.println(EMath.multiply(0.057487, 1.05458787));

        System.out.println(EMath.subtract(0.0000000000000008, 0.00000000000000000000009, 50));
        System.out.println(EMath.divide("0", new BigDecimal(3)));
        System.out.println(EMath.toBigDecimal(EMath.divide(0, new BigDecimal(3))));

        System.out.println(EMath.digit(0.045787431, 8));
        System.out.println(EMath.digit(new BigDecimal("0.0000000000000001"), 50));
    }

}

