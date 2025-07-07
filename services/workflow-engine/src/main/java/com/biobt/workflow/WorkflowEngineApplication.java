package com.biobt.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 工作流引擎启动类
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.biobt.workflow", "com.biobt.common"})
@EnableDiscoveryClient
public class WorkflowEngineApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WorkflowEngineApplication.class, args);
    }
}