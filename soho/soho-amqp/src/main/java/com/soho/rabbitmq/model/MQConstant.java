package com.soho.rabbitmq.model;

public final class MQConstant {

    //exchange name
    public static final String SOHO_EXCHANGE = "SOHO_EXCHANGE";

    //DLX QUEUE
    public static final String SOHO_DEAD_LETTER_QUEUE = "SOHO_DEAD_LETTER_QUEUE";

    //DLX repeat QUEUE 死信转发队列
    public static final String SOHO_REPEAT_TRADE_QUEUE = "SOHO_REPEAT_TRADE_QUEUE";

    //Hello 测试消息队列名称
    public static final String TEST_QUEUE = "TEST_QUEUE";


}