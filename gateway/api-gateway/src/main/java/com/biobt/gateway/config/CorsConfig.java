package com.biobt.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置
 * 解决前后端分离的跨域问题
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // 允许的源
        corsConfig.addAllowedOriginPattern("*");
        
        // 允许的请求头
        corsConfig.addAllowedHeader("*");
        
        // 允许的请求方法
        corsConfig.addAllowedMethod("*");
        
        // 允许携带凭证
        corsConfig.setAllowCredentials(true);
        
        // 预检请求的缓存时间（秒）
        corsConfig.setMaxAge(3600L);
        
        // 暴露的响应头
        corsConfig.addExposedHeader("X-Request-Id");
        corsConfig.addExposedHeader("X-RateLimit-Limit");
        corsConfig.addExposedHeader("X-RateLimit-Remaining");
        corsConfig.addExposedHeader("X-RateLimit-Reset");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}