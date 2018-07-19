package com.example.lnsa.services;

import com.example.lnsa.entities.ScheduledWorker;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableAsync
@EnableScheduling
@Service
public class SchedulingService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    @Qualifier("asyncWorkerLndHeartbeat")
    private ScheduledWorker asyncWorkerLndHeartbeat;

    // perform hearbeat check every five seconds
    @Scheduled(fixedRate = 5000)
    public void runAsyncWorkerLndHeartbeat() {
        log.debug("Launching asyncWorkerLndHeartbeat async worker");
        asyncWorkerLndHeartbeat.work();
        log.debug("asyncWorkerLndHeartbeat async worker returned");
    }

}