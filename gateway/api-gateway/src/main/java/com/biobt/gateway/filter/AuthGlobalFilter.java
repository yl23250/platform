package com.biobt.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证全局过滤器
 * 负责JWT token的验证和用户信息的传递
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Value("${jwt.prefix}")
    private String tokenPrefix;
    
    @Value("${gateway.whitelist}")
    private List<String> whitelist;
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        log.debug("请求路径: {}", path);
        
        // 检查是否在白名单中
        if (isWhitelisted(path)) {
            log.debug("路径 {} 在白名单中，跳过认证", path);
            return chain.filter(exchange);
        }
        
        // 获取token
        String token = getToken(request);
        if (!StringUtils.hasText(token)) {
            log.warn("请求路径 {} 缺少认证token", path);
            return unauthorized(exchange.getResponse(), "缺少认证token");
        }
        
        try {
            // 验证token
            DecodedJWT decodedJWT = verifyToken(token);
            
            // 提取用户信息
            String userId = decodedJWT.getClaim("userId").asString();
            String username = decodedJWT.getClaim("username").asString();
            String tenantId = decodedJWT.getClaim("tenantId").asString();
            
            log.debug("用户 {} (ID: {}, 租户: {}) 认证成功", username, userId, tenantId);
            
            // 将用户信息添加到请求头中，传递给下游服务
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-Username", username)
                    .header("X-Tenant-Id", tenantId)
                    .build();
            
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
            
        } catch (JWTVerificationException e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return unauthorized(exchange.getResponse(), "无效的认证token");
        } catch (Exception e) {
            log.error("认证过程中发生异常", e);
            return unauthorized(exchange.getResponse(), "认证失败");
        }
    }
    
    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhitelisted(String path) {
        return whitelist.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
    
    /**
     * 从请求中获取token
     */
    private String getToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(tokenHeader);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length());
        }
        return null;
    }
    
    /**
     * 验证JWT token
     */
    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
    
    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        
        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }
    
    @Override
    public int getOrder() {
        return -100; // 优先级较高，在其他过滤器之前执行
    }
}