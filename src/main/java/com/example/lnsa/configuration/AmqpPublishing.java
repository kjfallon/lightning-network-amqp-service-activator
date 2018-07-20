package com.example.lnsa.configuration;

import com.example.lnsa.services.AmqpEventPublisher;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Properties;

@Configuration
public class AmqpPublishing {

    @Resource(name="encryptedProperties")
    Properties encryptedProperties;

    @Bean
    public TopicExchange senderTopicExchange() {
        return new TopicExchange("eventExchange");
    }

    @Bean
    public AmqpEventPublisher eventPublisher(RabbitTemplate rabbitTemplate, TopicExchange senderTopicExchange) {

        // We are not using the spring boot default amqp connection properties from application.properties
        // that are provided within the rabbitTemplate passed to this method.  Instead we are creating a new
        // RabbitTemplate using values from the property file that supports encrypted values.

        CachingConnectionFactory sandlefordWarren = new CachingConnectionFactory();

        sandlefordWarren.setHost(encryptedProperties.getProperty("spring.rabbitmq.host"));
        int amqpPort = Integer.parseInt(encryptedProperties.getProperty("spring.rabbitmq.port"));
        sandlefordWarren.setPort(amqpPort);
        sandlefordWarren.setUsername(encryptedProperties.getProperty("spring.rabbitmq.username"));
        sandlefordWarren.setPassword(encryptedProperties.getProperty("spring.rabbitmq.password"));

        RabbitTemplate template = new RabbitTemplate(sandlefordWarren);

        return new AmqpEventPublisher(template, senderTopicExchange);
    }

}
