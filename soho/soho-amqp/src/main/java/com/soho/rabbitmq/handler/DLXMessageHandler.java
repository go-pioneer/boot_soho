package com.soho.rabbitmq.handler;

import com.soho.rabbitmq.core.MQProducter;
import com.soho.rabbitmq.model.DLXMessage;
import com.soho.rabbitmq.model.MQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
@RabbitListener(queues = MQConstant.SOHO_REPEAT_TRADE_QUEUE)
public class DLXMessageHandler {

    private final static Logger log = LoggerFactory.getLogger(DLXMessageHandler.class);

    @RabbitHandler
    public void process(byte[] body) {
        try {
            if (body != null && body.length > 0) {
                DLXMessage message = (DLXMessage) SerializationUtils.deserialize(body);
                MQProducter.send(message.getQueue(), message.getContent());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}