package com.example.lnsa.configuration;


import org.lightningj.lnd.wrapper.AsynchronousLndAPI;
import org.lightningj.lnd.wrapper.ClientSideException;
import org.lightningj.lnd.wrapper.SynchronousLndAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

@Configuration
public class LndApi {

    private static final Logger log = LoggerFactory.getLogger(LndApi.class);

    @Resource(name = "encryptedProperties")
    Properties encryptedProperties;

    @Bean
    AsynchronousLndAPI asyncLnd() {
        log.info("Creating asynchronous channel to Lightning Node API...");
        File lndApiCertFile = null;
        File lndMacaroonFile = null;
        try {
            lndApiCertFile = ResourceUtils.getFile("classpath:" + encryptedProperties.getProperty("lnd_api_cert_path"));
            lndMacaroonFile = ResourceUtils.getFile("classpath:" + encryptedProperties.getProperty("lnd_api_macaroon_path"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int apiPort = Integer.parseInt(encryptedProperties.getProperty("lnd_api_port"));

        // create asynchronous API channel
        AsynchronousLndAPI asynchronousLndAPI = null;
        try {
            asynchronousLndAPI = new AsynchronousLndAPI(encryptedProperties.getProperty("lnd_api_host"),
                    apiPort,
                    lndApiCertFile,
                    lndMacaroonFile);
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (ClientSideException e) {
            e.printStackTrace();
        }

        return asynchronousLndAPI;
    }

    @Bean
    SynchronousLndAPI syncLnd() {
        log.info("Creating synchronous channel to Lightning Node API...");
        File lndApiCertFile = null;
        File lndMacaroonFile = null;
        try {
            lndApiCertFile = ResourceUtils.getFile("classpath:" + encryptedProperties.getProperty("lnd_api_cert_path"));
            lndMacaroonFile = ResourceUtils.getFile("classpath:" + encryptedProperties.getProperty("lnd_api_macaroon_path"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int apiPort = Integer.parseInt(encryptedProperties.getProperty("lnd_api_port"));

        // create synchronous API channel
        SynchronousLndAPI synchronousLndAPI = null;
        try {
            synchronousLndAPI = new SynchronousLndAPI(encryptedProperties.getProperty("lnd_api_host"),
                    apiPort,
                    lndApiCertFile,
                    lndMacaroonFile);
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (ClientSideException e) {
            e.printStackTrace();
        }

        return synchronousLndAPI;
    }
}
