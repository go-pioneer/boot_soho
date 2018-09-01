package com.soho.rabbitmq.model;

public final class MQConstant {

    //exchange name
    public static final String DELAY_EXCHANGE = "DELAY_EXCHANGE";

    //default name
    public static final String DEFAULT_EXCHANGE = "DEFAULT_EXCHANGE";

    //DLX QUEUE
    public static final String DELAY_DEAD_LETTER_QUEUE = "DELAY_DEAD_LETTER_QUEUE";

    //DLX repeat QUEUE 死信转发队列
    public static final String DELAY_REPEAT_TRADE_QUEUE = "DELAY_REPEAT_TRADE_QUEUE";

}