package com.soho.spring.cache.annotation;

import java.lang.annotation.*;

/**
 * 多级缓存注解实现
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiCache {

    // 缓存过期时间/单位秒,默认2分钟
    int local_exp() default 120;

    // 开启本地缓存级别
    boolean local_enable() default false;

    // 远程缓存过期时间/单位秒,默认60分钟
    int remote_exp() default 3600;

    // 分布式锁有效时间
    long lock_timeout() default 5000;

    // 开启远程缓存级别
    boolean remote_enable() default true;

    // 缓存前缀
    String prefix() default "multi_level_cache";

    // spel获取参数值
    String spel() default "";

}