package com.example.lnsa.configuration;


import com.example.lnsa.services.AmqpEventConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Properties;

@Configuration
public class AmqpConsuming {

    @Resource(name="encryptedProperties")
    Properties encryptedProperties;

    // create a new command exchange if it does not exist
    @Bean
    public Exchange eventExchange() {
        return new TopicExchange(encryptedProperties.getProperty("spring.rabbitmq.exchange.command"));
    }

    // create a new command queue if it does not exist
    @Bean
    public Queue queue() {
        return new Queue(encryptedProperties.getProperty("spring.rabbitmq.queue.command"));
    }

    // bind the command queue to the command exchange and receive all topics into the queue
    @Bean
    public Binding binding(Queue queue, Exchange eventExchange) {

        return new Binding(queue.getName(),
                Binding.DestinationType.QUEUE,
                eventExchange.getName(),
                "*",
                null);
        }

    @Bean
    public AmqpEventConsumer eventReceiver() {
        return new AmqpEventConsumer();
    }
}
