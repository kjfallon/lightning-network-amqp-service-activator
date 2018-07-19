package com.example.lnsa.components;

import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.SynchronousLndAPI;
import org.lightningj.lnd.wrapper.ValidationException;
import org.lightningj.lnd.wrapper.message.GetInfoResponse;
import org.lightningj.lnd.wrapper.message.ListChannelsResponse;
import org.lightningj.lnd.wrapper.message.ListPeersResponse;
import org.lightningj.lnd.wrapper.message.WalletBalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class StartUpTasks {

    @Autowired
    SynchronousLndAPI syncLnd;

    @Autowired
    GetInfoResponse nodeInfo;

    // create logger
    private static final Logger log = LoggerFactory.getLogger(StartUpTasks.class);

    // Use the synchronous API channel to query and display some info about this node
    // As well populate the nodeInfo bean with this node's ID information
    public void appLaunchTasks(){
        GetInfoResponse infoResponse = null;
        WalletBalanceResponse walletBalanceResponse = null;
        ListPeersResponse peerResponse = null;
        ListChannelsResponse channelsResponse = null;
        try {
            infoResponse = syncLnd.getInfo();
            BeanUtils.copyProperties(infoResponse,nodeInfo);
            walletBalanceResponse = syncLnd.walletBalance();
            peerResponse = syncLnd.listPeers();
            channelsResponse = syncLnd.listChannels(false, false, false, false);
            } catch (StatusException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        log.info("Startup Lnd Info: " + nodeInfo.toJsonAsString(false));
        log.info("Startup Lnd Wallet balance : " + walletBalanceResponse.toJsonAsString(false));
        log.info("Startup Lnd Peers: " + peerResponse.toJsonAsString(false));
        log.info("Startup Lnd Channels: " + channelsResponse.toJsonAsString(false));

        }

}
