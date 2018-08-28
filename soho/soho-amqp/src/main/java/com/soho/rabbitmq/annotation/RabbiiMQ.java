package com.soho.rabbitmq.annotation;


import java.lang.annotation.*;

/**
 * MQ异步通知发送注解实现
 * 业务以Map数据集合回传且存在指定field字段,如new FastMap<>().add("mq", new Object());
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbiiMQ {

    // MQ通道名称
    String queue() default "rabbitmq";

    // MAP回传参数读取字段
    String key() default "rabbitmq";

    // 是否移除回传参数字段值
    boolean remove() default false;


}