package com.soho.rabbitmq.service;

import org.springframework.amqp.core.Message;

public interface MQService {

    public void doProcess(Message message) throws Exception;

    public void doTryException(Message message, Exception e);

}
