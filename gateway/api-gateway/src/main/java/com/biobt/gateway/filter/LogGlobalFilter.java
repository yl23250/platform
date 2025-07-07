package com.biobt.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 日志记录全局过滤器
 * 记录请求和响应的详细信息
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Component
@Slf4j
public class LogGlobalFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        
        // 记录请求信息
        String requestId = generateRequestId();
        String method = request.getMethod().name();
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        String clientIp = getClientIp(request);
        String userAgent = request.getHeaders().getFirst("User-Agent");
        
        log.info("[{}] 请求开始 - {} {} {} - IP: {} - UA: {}", 
                requestId, method, path, query != null ? "?" + query : "", clientIp, userAgent);
        
        // 将请求ID添加到响应头
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("X-Request-Id", requestId);
        
        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                // 记录响应信息
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                int statusCode = response.getStatusCode() != null ? response.getStatusCode().value() : 0;
                
                log.info("[{}] 请求完成 - {} {} - 状态码: {} - 耗时: {}ms", 
                        requestId, method, path, statusCode, duration);
                
                // 记录慢请求
                if (duration > 1000) {
                    log.warn("[{}] 慢请求警告 - {} {} - 耗时: {}ms", 
                            requestId, method, path, duration);
                }
            })
        );
    }
    
    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return String.valueOf(System.currentTimeMillis()) + "-" + 
               String.valueOf((int)(Math.random() * 10000));
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }
    
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // 最高优先级，第一个执行
    }
}