package com.soho.spring.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式Redis锁注解实现
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RDLock {

    // 等待时间
    int waitime() default 5000;

    // 超时时间
    int timeout() default 5000;

    // 睡眠时间
    int sleep() default 100;

    // 时间单位
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    // 前缀关键字
    String prefix() default "redisson_distributed";

    // 是否锁单独用户
    boolean user() default false;

    // spel获取参数值
    String spel() default "";

}