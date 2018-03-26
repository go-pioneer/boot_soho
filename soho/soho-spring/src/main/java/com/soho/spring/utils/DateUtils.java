package com.soho.spring.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by shadow on 2017/7/30.
 */
public class DateUtils {

    public static long GMT8(String s) throws ParseException {
        return GMT8(s, "yyyy-MM-dd HH:mm:ss");
    }

    public static long GMT8(String s, String fmt) throws ParseException {
        return getDateFormat(fmt).parse(s).getTime();
    }

    public static String GMT8_S(long s) throws ParseException {
        return GMT8_S(s, "yyyy-MM-dd HH:mm:ss");
    }

    public static String GMT8_S(long s, String fmt) throws ParseException {
        if (s > 0) {
            return getDateFormat(fmt).format(new Date(s));
        }
        return "";
    }

    // 获取今日开始日期时间戳
    public static Long getTodayStart() {
        try {
            String date = GMT8_S(System.currentTimeMillis(), "yyyy-MM-dd");
            date = date + " 00:00:00";
            return GMT8(date, "yyyy-MM-dd hh:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    // 获取今日结束日期时间戳
    public static Long getTodayEnd() {
        try {
            String date = GMT8_S(System.currentTimeMillis(), "yyyy-MM-dd");
            date = date + " 23:59:59";
            return GMT8(date, "yyyy-MM-dd hh:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    // 获取几天前或几天后的开始时间戳
    public static Long getAnyDayStart(int day) {
        long time = System.currentTimeMillis() + day * 86400000l;
        String fmt = "yyyy-MM-dd 00:00:00";
        String firstday = getDateFormat(fmt).format(new Date(time));
        try {
            return GMT8(firstday, fmt);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0l;
        }
    }

    // 获取几天前或几天后的结束时间戳
    public static Long getAnyDayEnd(int day) {
        long time = System.currentTimeMillis() + day * 86400000l;
        String fmt = "yyyy-MM-dd";
        String lastday = getDateFormat(fmt).format(new Date(time));
        lastday += " 23:59:59";
        try {
            return GMT8(lastday, fmt + " HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
            return 0l;
        }
    }

    // 获取几天前或几天后的具体间戳
    public static Long getAnyDayLast(int day) {
        return System.currentTimeMillis() + day * 86400000l;
    }


    // 获取前月的第一天
    public static long getMonthFirstDay() {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String fmt = "yyyy-MM-dd 00:00:00";
        String firstday = getDateFormat(fmt).format(calendar.getTime());
        try {
            return GMT8(firstday, fmt);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 获取前月的最后一天
    public static long getMonthLastDay() {
        Calendar calendar = getCalendar();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        String fmt = "yyyy-MM-dd";
        String lastday = getDateFormat(fmt).format(calendar.getTime());
        lastday += " 23:59:59";
        try {
            return GMT8(lastday, fmt + " HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 获取当周的第一天
    public static long getWeekFirstDay() {
        Calendar calendar = getCalendar();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) { // 星期天为0转为7
            dayOfWeek = 7;
        }
        int day = dayOfWeek - 1;
        return getAnyDayStart(-day);
    }

    public static long getWeekLastDay() {
        Calendar calendar = getCalendar();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) { // 星期天为0转为7
            dayOfWeek = 7;
        }
        int day = 7 - dayOfWeek;
        return getAnyDayEnd(day);
    }

    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return calendar;
    }

    public static DateFormat getDateFormat(String fmt) {
        DateFormat format = new SimpleDateFormat(fmt);// 构造格式化模板
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format;
    }

    public static void main(String[] args) {
        try {
            System.out.println(GMT8_S(getWeekFirstDay()));
            System.out.println(GMT8_S(getWeekLastDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
