package com.biobt.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 消息引擎启动类
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.biobt.message", "com.biobt.common"})
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
public class MessageEngineApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MessageEngineApplication.class, args);
    }
}