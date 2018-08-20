package com.soho.spring.cache.aop;

import java.lang.annotation.*;

/**
 * 多级缓存注解实现
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    // 本地缓存
    boolean local() default true;

    // 远程缓存
    boolean remote() default false;

    // 本地缓存副本有效时间/秒
    int expire() default 60;

}