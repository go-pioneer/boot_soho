package com.soho.rabbitmq.configuration;

import com.soho.rabbitmq.model.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
@Configuration
public class RabbitMQConfiguration {

    @Autowired(required = false)
    private MQConfig config;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(config.getHost(), config.getPort());
        connectionFactory.setUsername(config.getUsername());
        connectionFactory.setPassword(config.getPassword());
        connectionFactory.setVirtualHost(config.getVirtualHost());
        connectionFactory.setPublisherConfirms(config.getPublisherConfirms());
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    /*@Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(new Queue(QUENUE));
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setRetryDeclarationInterval(3);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new ChannelAwareMessageListener() {
            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                try {
                    byte[] body = message.getBody();
                    Object object = SerializationUtils.deserialize(body);
                    System.out.println("消费端接收到消息 : " + JSON.toJSONString(object));
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                }
            }
        });
        return container;
    }*/

    /*********************    延时队列    *****************/
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MQConstant.DELAY_EXCHANGE, true, false);
    }


    @Bean
    public Queue repeatTradeQueue() {
        Queue queue = new Queue(MQConstant.DELAY_REPEAT_TRADE_QUEUE, true, false, false);
        return queue;
    }

    @Bean
    public Binding drepeatTradeBinding() {
        return BindingBuilder.bind(repeatTradeQueue()).to(directExchange()).with(MQConstant.DELAY_REPEAT_TRADE_QUEUE);
    }

    @Bean
    public Queue deadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MQConstant.DELAY_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", MQConstant.DELAY_REPEAT_TRADE_QUEUE);
        Queue queue = new Queue(MQConstant.DELAY_DEAD_LETTER_QUEUE, true, false, false, arguments);
        return queue;
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(directExchange()).with(MQConstant.DELAY_DEAD_LETTER_QUEUE);
    }


    /*********************    测试队列    *****************/
    @Bean
    public Queue queue() {
        return new Queue(MQConstant.TEST_QUEUE, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(MQConstant.TEST_QUEUE);
    }

}