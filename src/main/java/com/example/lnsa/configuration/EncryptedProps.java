package com.example.lnsa.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.springframework.context.annotation.Configuration;

import java.security.Security;
import java.util.Properties;

@Configuration
public class EncryptedProps {

    private static final Logger log = LoggerFactory.getLogger(EncryptedProps.class);

    @Bean
    Properties encryptedProperties() {

        Security.addProvider(new BouncyCastleProvider());
        StandardPBEStringEncryptor BCencryptor = new StandardPBEStringEncryptor();
        BCencryptor.setProviderName("BC");
        BCencryptor.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
        BCencryptor.setKeyObtentionIterations(2377); // likely more than sufficient
        BCencryptor.setPassword(System.getenv("LNASA_KEY"));

        Properties props = new EncryptableProperties(BCencryptor);
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("encrypted.properties"));
            log.info("Encrypted properties file loaded.");
        }
        catch (Exception e) {
            log.error("Unable to load encrypted.properties file.");
        }

        return props;
    }

}