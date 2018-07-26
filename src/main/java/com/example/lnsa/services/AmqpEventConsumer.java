package com.example.lnsa.services;

import com.example.lnsa.components.LndCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

public class AmqpEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AmqpEventConsumer.class);

    @Resource(name = "encryptedProperties")
    Properties encryptedProperties;

    @Autowired
    LndCommands lndCommands;

    @PostConstruct
    public void setProperty() {
        System.setProperty("amqp.queue.name", encryptedProperties.getProperty("spring.rabbitmq.queue.command"));
    }

    @RabbitListener(queues = "${amqp.queue.name}")
    public void receive(String message) {
        log.info("AMQP Received from '" + System.getProperty("amqp.queue.name") + "' message '" + message + "'");
        lndCommands.receivedCommand(message);
    }

}
