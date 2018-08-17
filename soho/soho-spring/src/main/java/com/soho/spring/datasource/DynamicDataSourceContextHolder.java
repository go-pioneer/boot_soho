package com.soho.spring.datasource;

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> currentDatesource = new ThreadLocal<>();

    public static String get() {
        return currentDatesource.get();
    }

    public static void set(String value) {
        currentDatesource.set(value);
    }

    public static void clear() {
        currentDatesource.remove();
    }

}