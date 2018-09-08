package com.soho.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MVC表单防机器人认证注解
 *
 * @author shadow
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KillRobot {

    // 上个请求地址
    public String goback() default "";

    // 是否认证完
    public boolean reset() default false;

    // 认证地址
    public String valid() default "/ggk/init";

}