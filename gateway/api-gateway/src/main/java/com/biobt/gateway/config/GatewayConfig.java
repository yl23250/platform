package com.biobt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置类
 * 定义动态路由和过滤器配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class GatewayConfig {
    
    /**
     * 自定义路由配置
     * 补充application.yml中的路由配置
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 健康检查路由
                .route("health-check", r -> r
                        .path("/health")
                        .uri("http://localhost:8080/actuator/health")
                )
                // API文档聚合路由
                .route("api-docs", r -> r
                        .path("/v3/api-docs/**")
                        .filters(f -> f
                                .rewritePath("/v3/api-docs/(?<segment>.*)", "/v3/api-docs/${segment}")
                        )
                        .uri("lb://user-service")
                )
                // Swagger UI路由
                .route("swagger-ui", r -> r
                        .path("/swagger-ui/**")
                        .uri("lb://user-service")
                )
                // 降级路由
                .route("fallback", r -> r
                        .path("/fallback/**")
                        .uri("forward:/fallback")
                )
                .build();
    }
}