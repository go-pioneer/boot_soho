package com.soho.rabbitmq.core;

import com.soho.rabbitmq.model.DLXMessage;
import com.soho.rabbitmq.model.MQConstant;
import com.soho.spring.utils.SpringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.SerializationUtils;

public class MQProducter {

    private volatile static RabbitTemplate rabbitTemplate;

    private static RabbitTemplate getRabbitTemplate() {
        if (rabbitTemplate == null) {
            rabbitTemplate = SpringUtils.getBean(RabbitTemplate.class);
        }
        return rabbitTemplate;
    }

    /**
     * 发送消息到队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     */
    public static void send(String queue, Object message) {
        getRabbitTemplate().convertAndSend(MQConstant.SOHO_EXCHANGE, queue, message);
    }

    /**
     * 延迟发送消息到队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     * @param delay   延迟时间 单位毫秒
     */
    public static void send(String queue, Object message, long delay) {
        DLXMessage dlxMessage = new DLXMessage(queue, message, delay);
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(delay + "");
                return message;
            }
        };
        dlxMessage.setExchange(MQConstant.SOHO_EXCHANGE);
        getRabbitTemplate().convertAndSend(MQConstant.SOHO_EXCHANGE, MQConstant.SOHO_DEAD_LETTER_QUEUE, SerializationUtils.serialize(dlxMessage), processor);
    }


}