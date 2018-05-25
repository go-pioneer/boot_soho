package com.soho.spring.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MVC表单GGK注解
 *
 * @author shadow
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GGKToken {

    public String failUrl() default "";

    public String ggkUrl() default "/ggk/init";

}