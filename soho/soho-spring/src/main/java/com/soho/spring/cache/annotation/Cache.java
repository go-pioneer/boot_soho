package com.soho.spring.cache.annotation;

import java.lang.annotation.*;

/**
 * 多级缓存注解实现
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    // 缓存过期时间/单位秒,默认2分钟
    int local_exp() default 120;

    // 远程缓存过期时间/单位秒,默认60分钟
    int remote_exp() default 3600;

}