package com.biobt.gateway.filter;

import com.alibaba.fastjson2.JSON;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流全局过滤器
 * 基于令牌桶算法实现分布式限流
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Component
@Slf4j
public class RateLimitGlobalFilter implements GlobalFilter, Ordered {
    
    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;
    
    @Value("${gateway.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;
    
    @Value("${gateway.rate-limit.default-capacity:20}")
    private long defaultCapacity;
    
    @Value("${gateway.rate-limit.default-tokens:10}")
    private long defaultTokens;
    
    @Value("${gateway.rate-limit.default-seconds:1}")
    private long defaultSeconds;
    
    // 本地缓存，避免频繁创建Bucket
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!rateLimitEnabled) {
            return chain.filter(exchange);
        }
        
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 生成限流key（基于IP + 路径）
        String clientIp = getClientIp(request);
        String rateLimitKey = generateRateLimitKey(clientIp, path);
        
        log.debug("限流检查 - IP: {}, 路径: {}, Key: {}", clientIp, path, rateLimitKey);
        
        // 获取或创建令牌桶
        Bucket bucket = getBucket(rateLimitKey);
        
        // 尝试获取令牌
        if (bucket.tryConsume(1)) {
            log.debug("限流通过 - Key: {}", rateLimitKey);
            return chain.filter(exchange);
        } else {
            log.warn("触发限流 - IP: {}, 路径: {}", clientIp, path);
            return rateLimited(exchange.getResponse());
        }
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
    
    /**
     * 生成限流key
     */
    private String generateRateLimitKey(String clientIp, String path) {
        return String.format("rate_limit:%s:%s", clientIp, path.replaceAll("/", "_"));
    }
    
    /**
     * 获取或创建令牌桶
     */
    private Bucket getBucket(String key) {
        return bucketCache.computeIfAbsent(key, k -> {
            // 创建令牌桶配置
            Bandwidth limit = Bandwidth.classic(
                    defaultCapacity, 
                    Refill.intervally(defaultTokens, Duration.ofSeconds(defaultSeconds))
            );
            
            log.debug("创建新的令牌桶 - Key: {}, 容量: {}, 补充速率: {}/{} 秒", 
                    key, defaultCapacity, defaultTokens, defaultSeconds);
            
            return Bucket4j.builder()
                    .addLimit(limit)
                    .build();
        });
    }
    
    /**
     * 返回限流响应
     */
    private Mono<Void> rateLimited(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().add("X-RateLimit-Limit", String.valueOf(defaultCapacity));
        response.getHeaders().add("X-RateLimit-Remaining", "0");
        response.getHeaders().add("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() + defaultSeconds * 1000));
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 429);
        result.put("message", "请求过于频繁，请稍后再试");
        result.put("timestamp", System.currentTimeMillis());
        
        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }
    
    @Override
    public int getOrder() {
        return -50; // 在认证过滤器之后执行
    }
}