package com.biobt.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API网关启动类
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}