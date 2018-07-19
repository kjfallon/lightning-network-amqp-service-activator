package com.example.lnsa;

import com.example.lnsa.components.StartUpTasks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableScheduling
public class LightningNetworkServiceActivatorApplication {

    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext springBootAppContext = SpringApplication.run(LightningNetworkServiceActivatorApplication.class, args);

        // Display information about this Lighting Node at launch
        springBootAppContext.getBean(StartUpTasks.class).appLaunchTasks();

        // Do not immediately exit, instead wait for shutdown request
        final CountDownLatch closeLatch = springBootAppContext.getBean(CountDownLatch.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                closeLatch.countDown();
            }
        });
        closeLatch.await();
    }

}
