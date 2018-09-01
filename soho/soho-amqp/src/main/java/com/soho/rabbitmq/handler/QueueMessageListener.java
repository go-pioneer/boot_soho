package com.soho.rabbitmq.handler;

import com.rabbitmq.client.Channel;
import com.soho.rabbitmq.service.MQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import java.io.IOException;
import java.util.Map;

public abstract class QueueMessageListener implements ChannelAwareMessageListener {

    private final static Logger log = LoggerFactory.getLogger(QueueMessageListener.class);

    @Override
    public void onMessage(Message message, Channel channel) {
        MQService mqService = null;
        try {
            mqService = getMqServiceMap().get(message.getMessageProperties().getConsumerQueue());
            if (mqService != null) {
                mqService.doProcess(message);
            }
        } catch (Exception e) {
            if (mqService != null) {
                mqService.doTryException(message, e);
            } else {
                log.error(e.getMessage(), e);
            }
        } finally {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public abstract Map<String, MQService> getMqServiceMap();

}