package com.soho.rabbitmq.annotation;


import com.soho.rabbitmq.model.MQConstant;

import java.lang.annotation.*;

/**
 * MQ异步通知发送注解实现
 * 业务以Map数据集合回传且存在指定field字段,如new FastMap<>().add("mq", new Object());
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbiiMQ {

    // MQ信道名称
    String exchange() default MQConstant.DEFAULT_EXCHANGE;

    // MQ通道名称
    String queue() default "";

    // 指定map参数key
    String[] mapkey() default {};

    // MQ延时发送时间,单位毫秒
    long delay() default 0;

    // 是否删除mapkey对应数值,true.删除 false.保留
    boolean remove() default false;

}