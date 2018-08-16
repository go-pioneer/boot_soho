package com.soho.spring.datasource;

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<DataSourceKey> currentDatesource = new ThreadLocal<>();

    public static DataSourceKey get() {
        return currentDatesource.get();
    }

    public static void set(DataSourceKey value) {
        currentDatesource.set(value);
    }

    public static void clear() {
        currentDatesource.remove();
    }

}