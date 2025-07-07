package com.biobt.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 用户服务启动类
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {"com.biobt"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.biobt"})
@EnableTransactionManagement
@MapperScan("com.biobt.user.mapper")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}