package com.example.lnsa.configuration;

import com.example.lnsa.services.AmqpEventConsumer;
import com.example.lnsa.services.AmqpEventPublisher;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.util.Properties;

@Configuration
public class AmqpConfig {

    @Resource(name="encryptedProperties")
    Properties encryptedProperties;

    // specify the properties we want the automatically configured amqp classes to use
    @Bean
    @Primary
    RabbitProperties rabbitProperties() {

        RabbitProperties customRabbitProperties = new RabbitProperties();
        customRabbitProperties.setHost(encryptedProperties.getProperty("spring.rabbitmq.host"));
        int amqpPort = Integer.parseInt(encryptedProperties.getProperty("spring.rabbitmq.port"));
        customRabbitProperties.setPort(amqpPort);
        customRabbitProperties.setUsername(encryptedProperties.getProperty("spring.rabbitmq.username"));
        customRabbitProperties.setPassword(encryptedProperties.getProperty("spring.rabbitmq.password"));

        return customRabbitProperties;
    }

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


    // create an bean to consume messages
    @Bean
    public AmqpEventConsumer eventReceiver() {
        return new AmqpEventConsumer();
    }

    // create an exchange to send to
    @Bean
    public TopicExchange senderTopicExchange() {
        return new TopicExchange("eventExchange");
    }

    // create a bean to publish messages
    @Bean
    public AmqpEventPublisher eventPublisher(RabbitTemplate rabbitTemplate, TopicExchange senderTopicExchange) {

        return new AmqpEventPublisher(rabbitTemplate, senderTopicExchange);
    }

}
