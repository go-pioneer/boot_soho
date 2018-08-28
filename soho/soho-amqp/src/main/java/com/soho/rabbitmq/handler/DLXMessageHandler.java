package com.soho.rabbitmq.handler;

import com.rabbitmq.client.Channel;
import com.soho.rabbitmq.core.MQProducter;
import com.soho.rabbitmq.model.DLXMessage;
import com.soho.rabbitmq.model.MQConstant;
import com.soho.spring.mvc.model.FastMap;
import com.soho.spring.utils.SpringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = MQConstant.DELAY_REPEAT_TRADE_QUEUE)
public class DLXMessageHandler extends DefaultHandler {

    @Autowired
    private MQProducter producter;

    @RabbitHandler
    public void process(byte[] body, long tag, Channel channel) {
        super.process(body, tag, channel);
    }

    @Override
    protected void doProcess(Object object) {
        DLXMessage message = (DLXMessage) object;
        producter.send(message.getQueue(), message.getContent());
    }

    @Override
    protected void doException(Object object, Exception e) {
        DLXMessage message = (DLXMessage) object;
        if (message.getRetries() < 5) {
            producter.send(message.getQueue(), message.getContent(), message.getDelay() * message.getRetries(), message.getRetries());
        } else {
            MongoTemplate template = SpringUtils.getBean(MongoTemplate.class);
            if (template != null) {
                FastMap map = new FastMap();
                map.add("title", "信道转发功能");
                map.add("message", message);
                map.add("exception", e.getMessage());
                template.save(map.done(), "mq_exlog");
            }
        }
    }

}