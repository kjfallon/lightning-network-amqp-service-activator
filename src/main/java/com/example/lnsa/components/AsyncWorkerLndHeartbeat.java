package com.example.lnsa.components;

import com.example.lnsa.entities.ScheduledWorker;
import com.example.lnsa.services.AmqpEventPublisher;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.SynchronousLndAPI;
import org.lightningj.lnd.wrapper.ValidationException;
import org.lightningj.lnd.wrapper.message.GetInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component("asyncWorkerLndHeartbeat")
public class AsyncWorkerLndHeartbeat implements ScheduledWorker {

    private static final Logger log = LoggerFactory.getLogger(AsyncWorkerLndHeartbeat.class);

    @Resource(name = "encryptedProperties")
    Properties encryptedProperties;

    @Autowired
    SynchronousLndAPI syncLnd;

    @Autowired
    GetInfoResponse nodeInfo;

    @Autowired
    AmqpEventPublisher amqpPub;

    @Async
    public void work() {
        String threadName = Thread.currentThread().getName();
        log.debug("Scheduled worker " + threadName + " has began working.");

        GetInfoResponse infoResponse = null;
        try {
            infoResponse = syncLnd.getInfo();
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        if (infoResponse != null) {
            log.debug("Lnd heartbeat success");
            String nodeAlias = infoResponse.getAlias();
            amqpPub.sendMessage(encryptedProperties.getProperty("spring.rabbitmq.exchange.heartbeat"), "LND_OK " + nodeAlias);
        } else {
            log.error("Lnd heartbeat failure");
            amqpPub.sendMessage(encryptedProperties.getProperty("spring.rabbitmq.exchange.heartbeat"), "LND_FAIL " + nodeInfo.getAlias());
        }

        log.debug("Scheduled worker " + threadName + " has completed work.");
    }
}
