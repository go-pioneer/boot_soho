package com.soho.rabbitmq.core;

import com.alibaba.fastjson.JSON;
import com.soho.rabbitmq.model.DLXMessage;
import com.soho.rabbitmq.model.MQConstant;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQProducter {

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定即时队列
     *
     * @param exchange 信道名称
     * @param queue    队列名称
     * @param message  消息内容
     */
    public void send(String exchange, String queue, Object message) {
        rabbitTemplate.convertAndSend(exchange, queue, JSON.toJSONString(message));
    }

    /**
     * 发送消息到即时队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     */
    public void send(String queue, Object message) {
        send(MQConstant.DEFAULT_EXCHANGE, queue, message);
    }

    /**
     * 延迟发送消息到延时队列
     *
     * @param exchange 信道名称
     * @param queue    队列名称
     * @param message  消息内容
     * @param delay    延迟时间 单位毫秒
     */
    public void send(String exchange, String queue, Object message, Long delay, Integer retries) {
        if (delay == null || delay <= 0) {
            send(exchange, queue, message);
            return;
        }
        DLXMessage dlxMessage = new DLXMessage(queue, message, delay);
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(delay.toString());
                return message;
            }
        };
        dlxMessage.setExchange(exchange);
        if (retries != null) {
            dlxMessage.setRetries(retries + 1);
        }
        rabbitTemplate.convertAndSend(MQConstant.DELAY_EXCHANGE, MQConstant.DELAY_DEAD_LETTER_QUEUE, JSON.toJSONString(dlxMessage), processor);
    }

    /**
     * 延迟发送消息到延时队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     * @param delay   延迟时间 单位毫秒
     */
    public void send(String queue, Object message, Long delay) {
        send(MQConstant.DEFAULT_EXCHANGE, queue, message, delay, null);
    }

    /**
     * 延迟发送消息到延时队列
     *
     * @param exchange 信道名称
     * @param queue    队列名称
     * @param message  消息内容
     * @param delay    延迟时间 单位毫秒
     */
    public void send(String exchange, String queue, Object message, Long delay) {
        send(exchange, queue, message, delay, null);
    }

    /**
     * 延迟发送消息到延时队列
     *
     * @param queue   队列名称
     * @param message 消息内容
     * @param delay   延迟时间 单位毫秒
     * @param retries 失败尝试次数
     */
    public void send(String queue, Object message, Long delay, Integer retries) {
        send(MQConstant.DEFAULT_EXCHANGE, queue, message, delay, retries);
    }

}