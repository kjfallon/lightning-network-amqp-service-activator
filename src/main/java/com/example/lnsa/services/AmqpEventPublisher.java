package com.example.lnsa.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class AmqpEventPublisher {

    // create logger
    private static final Logger log = LoggerFactory.getLogger(AmqpEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    private final TopicExchange topicExchange;

    @Autowired
    public AmqpEventPublisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }

    public void sendMessage(String routingKey, String message) {

        String eventBody = String.format("'%s'", message);
        rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, eventBody);
        log.info("AMQP Published to '{}' message '{}'", routingKey, eventBody);
    }
}
