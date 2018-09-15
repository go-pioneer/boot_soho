package com.soho.rabbitmq.configuration;

import com.soho.rabbitmq.model.MQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * RabbitMQ配置
 */
@Configuration
public class RabbitMQConfiguration {

    private final static Logger log = LoggerFactory.getLogger(RabbitMQConfiguration.class);

    @Autowired(required = false)
    private MQConfig mqConfig;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(mqConfig.getHost(), mqConfig.getPort());
        connectionFactory.setUsername(mqConfig.getUsername());
        connectionFactory.setPassword(mqConfig.getPassword());
        connectionFactory.setVirtualHost(mqConfig.getVirtualHost());
        connectionFactory.setPublisherConfirms(mqConfig.getPublisherConfirms());
        log.info("Create ConnectionFactory bean ..");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(MQConstant.DEFAULT_EXCHANGE);
        return template;
    }

    /*********************    延时发送队列配置    *****************/
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(MQConstant.DELAY_EXCHANGE, true, false);
    }


    @Bean
    public Queue repeatTradeQueue() {
        return new Queue(MQConstant.DELAY_REPEAT_TRADE_QUEUE, true, false, false);
    }

    @Bean
    public Binding drepeatTradeBinding() {
        return BindingBuilder.bind(repeatTradeQueue()).to(delayExchange()).with(MQConstant.DELAY_REPEAT_TRADE_QUEUE);
    }

    @Bean
    public Queue deadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MQConstant.DELAY_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", MQConstant.DELAY_REPEAT_TRADE_QUEUE);
        Queue queue = new Queue(MQConstant.DELAY_DEAD_LETTER_QUEUE, true, false, false, arguments);
        log.debug("arguments :" + queue.getArguments());
        return queue;
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(delayExchange()).with(MQConstant.DELAY_DEAD_LETTER_QUEUE);
    }


    /*********************    动态绑定发送队列,监听队列    *****************/
    /*@Bean
    public List<String> queues() throws AmqpException, IOException {
        String exchange = mqConfig.getExchange();
        if (exchange == null || StringUtils.isEmpty(exchange.replaceAll(" ", ""))) {
            exchange = MQConstant.DEFAULT_EXCHANGE;
        }
        List<String> sendQueues = new ArrayList<>();
        List<String> listenerQueues = new ArrayList<>();
        for (Map.Entry entry : ConfigUtils.properties.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (key.startsWith("queue.send.")) {
                sendQueues.add(value.trim());
            }
            if (key.startsWith("queue.listener.")) {
                listenerQueues.add(value.trim());
            }
        }
        if (sendQueues.isEmpty()) {
            log.error("发送队列配置为空,请检测...");
        }
        if (listenerQueues.isEmpty()) {
            log.error("监听队列配置为空,请检测...");
        }
        Set<String> queueSet = new HashSet<>();
        queueSet.addAll(sendQueues);
        queueSet.addAll(listenerQueues);
        if (!queueSet.isEmpty()) {
            connectionFactory().createConnection().createChannel(false).exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true, false, null);
            for (String queueName : queueSet) {
                connectionFactory().createConnection().createChannel(false).queueDeclare(queueName, true, false, false, null);
                connectionFactory().createConnection().createChannel(false).queueBind(queueName, exchange, queueName);
            }
        }
        return listenerQueues;
    }

    // 创建监听器，监听队列
    @Bean
    public SimpleMessageListenerContainer mqMessageContainer() throws AmqpException, IOException {
        if (queueMessageListener == null) {
            return null;
        }
        String[] listenerQueues = queues().toArray(new String[]{});
        if (listenerQueues.length > 0) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
            container.setQueueNames(listenerQueues);
            container.setExposeListenerChannel(true);
            container.setConcurrentConsumers(mqConfig.getConcurrentConsumers());// 并发消费者个数
            container.setMaxConcurrentConsumers(mqConfig.getMaxConcurrentConsumers()); // 最大并发消费者个数
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式为手工确认
            container.setMessageListener(queueMessageListener); // 监听处理类
            return container;
        }
        return null;
    }*/

}