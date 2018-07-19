package com.example.lnsa.configuration;

import com.example.lnsa.entities.MacaroonCallCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

@Configuration
public class LndMacroon {

    private static final Logger log = LoggerFactory.getLogger(EncryptedProps.class);

    @Resource(name="encryptedProperties")
    Properties encryptedProperties;

    @Bean
    MacaroonCallCredential lndMacaroon() {
        String macaroonString = null;
        try {
            File lndMacaroonFile = ResourceUtils.getFile("classpath:" + encryptedProperties.getProperty("lnd_api_macaroon_path"));
            byte[] lndMacaroonBytes = Files.readAllBytes(lndMacaroonFile.toPath());
            macaroonString = Hex.encodeHexString(lndMacaroonBytes);
        } catch (IOException e) {
            log.error("Failure creating Macaroon");
            e.printStackTrace();
        }
        MacaroonCallCredential macaroon = new MacaroonCallCredential(macaroonString);

        return macaroon;
    }
}
