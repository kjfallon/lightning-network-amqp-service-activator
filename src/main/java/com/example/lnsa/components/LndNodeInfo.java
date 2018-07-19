package com.example.lnsa.components;

import org.lightningj.lnd.wrapper.message.GetInfoResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LndNodeInfo {

    @Bean
    GetInfoResponse nodeInfo() {
        GetInfoResponse nodeInfo = new GetInfoResponse();
        return nodeInfo;
    }
}
