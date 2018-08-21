package com.soho.spring.datasource.annotation;

import com.soho.spring.configuration.DataSourceConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MVC切换数据源注解
 *
 * @author shadow
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DSHolder {

    // 数据源名称
    public String name() default DataSourceConfiguration.MARSTER_DB;


}