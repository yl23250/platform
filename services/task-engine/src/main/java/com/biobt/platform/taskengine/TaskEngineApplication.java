package com.biobt.platform.taskengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 任务引擎启动类
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableScheduling
public class TaskEngineApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TaskEngineApplication.class, args);
    }
}