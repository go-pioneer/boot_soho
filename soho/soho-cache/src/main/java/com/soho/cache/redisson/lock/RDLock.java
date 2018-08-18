package com.soho.cache.redisson.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义分布式注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RDLock {

    // 等待时间
    int waitime() default 300;

    // 超时时间
    int timeout() default 3000;

    // 睡眠时间
    int sleep() default 50;

    // 时间单位
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    // 锁关键字
    String key() default "redisson_distributed_lock";

    // 是否锁单独用户
    boolean user() default false;

    // 扩展关键字
    String exkey() default "";

}