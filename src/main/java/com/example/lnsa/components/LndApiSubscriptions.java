package com.example.lnsa.components;

import com.example.lnsa.services.AmqpEventPublisher;
import io.grpc.stub.StreamObserver;
import org.lightningj.lnd.wrapper.AsynchronousLndAPI;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.lightningj.lnd.wrapper.message.GetInfoResponse;
import org.lightningj.lnd.wrapper.message.GraphTopologyUpdate;
import org.lightningj.lnd.wrapper.message.Invoice;
import org.lightningj.lnd.wrapper.message.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;


@Component
public class LndApiSubscriptions {

    private static final Logger log = LoggerFactory.getLogger(LndApiSubscriptions.class);

    @Resource(name = "encryptedProperties")
    Properties encryptedProperties;

    @Autowired
    AsynchronousLndAPI asyncLnd;

    @Autowired
    GetInfoResponse nodeInfo;

    @Autowired
    AmqpEventPublisher amqpPub;

    // Use the asynchronous API channel to subscribe to Lightning Network events
    @PostConstruct
    public void init() {

        // Subscribe to ChannelGraph
        log.info("Subscribing to Lightning Network ChannelGraph topology updates.");
        try {
            asyncLnd.subscribeChannelGraph(new StreamObserver<GraphTopologyUpdate>() {
                @Override
                public void onNext(GraphTopologyUpdate value) {
                    log.info("Received GraphTopologyUpdate: " + value.toJsonAsString(true));
                    amqpPub.sendMessage(encryptedProperties.getProperty("spring.rabbitmq.exchange.topology"), nodeInfo.getAlias() + " " + value.toJsonAsString(false));
                }

                @Override
                public void onError(Throwable t) {
                    log.error("Error occurred during subscribeChannelGraph call: " + t.getMessage());
                    t.printStackTrace(System.err);
                }

                @Override
                public void onCompleted() {
                    log.info("subscribeChannelGraph call closed.");
                }
            });
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        // Subscribe to transactions
        log.info("Subscribing to Lightning Network transactions.");
        try {
            asyncLnd.subscribeTransactions(new StreamObserver<Transaction>() {
                @Override
                public void onNext(Transaction value) {
                    log.info("Received Transaction: " + value.toJsonAsString(true));
                    amqpPub.sendMessage(encryptedProperties.getProperty("spring.rabbitmq.exchange.transaction"), nodeInfo.getAlias() + " " + value.toJsonAsString(false));
                }

                @Override
                public void onError(Throwable t) {
                    log.error("Error occurred during subscribeTransaction call: " + t.getMessage());
                    t.printStackTrace(System.err);
                }

                @Override
                public void onCompleted() {
                    log.info("subscribeTransaction call closed.");
                }
            });
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        // Subscribe to invoices
        log.info("Subscribing to Lightning Network invoices.");
        try {
            asyncLnd.subscribeInvoices(new StreamObserver<Invoice>() {
                @Override
                public void onNext(Invoice value) {

                    log.info("Received Invoice: " + value.toJsonAsString(true));
                    amqpPub.sendMessage(encryptedProperties.getProperty("spring.rabbitmq.exchange.invoice"), nodeInfo.getAlias() + " " + value.toJsonAsString(false));
                }

                @Override
                public void onError(Throwable t) {
                    log.error("Error occurred during subscribeInvoices call: " + t.getMessage());
                    t.printStackTrace(System.err);
                }

                @Override
                public void onCompleted() {
                    log.info("subscribeInvoices call closed.");
                }
            });
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

}
