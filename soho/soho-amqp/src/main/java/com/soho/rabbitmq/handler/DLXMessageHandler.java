package com.soho.rabbitmq.handler;

import com.alibaba.fastjson.JSON;
import com.soho.rabbitmq.core.MQProducter;
import com.soho.rabbitmq.model.DLXMessage;
import com.soho.rabbitmq.model.MQConstant;
import com.soho.spring.extend.collection.FastMap;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RabbitListener(queues = MQConstant.DELAY_REPEAT_TRADE_QUEUE)
public class DLXMessageHandler {

    @Autowired
    private MQProducter producter;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @RabbitHandler
    public void process(String body) {
        DLXMessage dlxMessage = null;
        try {
            dlxMessage = StringUtils.isEmpty(body) ? null : JSON.parseObject(body, DLXMessage.class);
            if (dlxMessage != null) {
                producter.send(dlxMessage.getExchange(), dlxMessage.getQueue(), dlxMessage.getContent());
            }
        } catch (Exception e) {
            if (dlxMessage == null) {
                if (mongoTemplate != null) {
                    FastMap map = new FastMap();
                    map.add("title", "信道转发功能");
                    map.add("message", body);
                    map.add("exception", "参数转换为空");
                    mongoTemplate.save(map.done(), "mq_exlog");
                }
            } else {
                int retries = dlxMessage.getRetries();
                if (retries < 5) {
                    producter.send(dlxMessage.getQueue(), dlxMessage.getContent(), dlxMessage.getDelay() * (retries + 1), retries);
                } else {
                    if (mongoTemplate != null) {
                        FastMap map = new FastMap();
                        map.add("title", "信道转发功能");
                        map.add("message", body);
                        map.add("exception", e.getMessage());
                        mongoTemplate.save(map.done(), "mq_exlog");
                    }
                }
            }
            e.printStackTrace();
        }
    }

}