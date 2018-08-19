package com.soho.spring.mvc.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义分布式注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTime {

}