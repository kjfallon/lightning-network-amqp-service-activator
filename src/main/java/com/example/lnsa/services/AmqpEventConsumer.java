package com.example.lnsa.services;

import com.example.lnsa.components.LndCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Properties;

public class AmqpEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(AmqpEventConsumer.class);

    @Resource(name="encryptedProperties")
    Properties encryptedProperties;

    @Autowired
    LndCommands lndCommands;

    // TODO encryptedProperties is null at this point, hardcoding command queue name for time being
    //private final String commandQueue = encryptedProperties.getProperty("spring.rabbitmq.queue.command");

    @RabbitListener(queues="LN_COMMAND_QUEUE")
    public void receive(String message) {
        log.info("AMQP Received from 'LN_COMMAND_QUEUE' message '" + message  + "'");
        lndCommands.receivedCommand(message);
        }

}
