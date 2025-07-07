package com.biobt.gateway.handler;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 降级处理器
 * 当服务不可用时提供友好的错误响应
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Component
@Slf4j
public class FallbackHandler implements HandlerFunction<ServerResponse> {
    
    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        String path = request.path();
        String method = request.methodName();
        
        log.warn("服务降级触发 - {} {}", method, path);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", "服务暂时不可用，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("path", path);
        
        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(JSON.toJSONString(result)));
    }
    
    /**
     * 用户服务降级
     */
    public Mono<ServerResponse> userServiceFallback(ServerRequest request) {
        return createFallbackResponse(request, "用户服务暂时不可用");
    }
    
    /**
     * 客户服务降级
     */
    public Mono<ServerResponse> customerServiceFallback(ServerRequest request) {
        return createFallbackResponse(request, "客户服务暂时不可用");
    }
    
    /**
     * 项目服务降级
     */
    public Mono<ServerResponse> projectServiceFallback(ServerRequest request) {
        return createFallbackResponse(request, "项目服务暂时不可用");
    }
    
    /**
     * 合同服务降级
     */
    public Mono<ServerResponse> contractServiceFallback(ServerRequest request) {
        return createFallbackResponse(request, "合同服务暂时不可用");
    }
    
    /**
     * 订单服务降级
     */
    public Mono<ServerResponse> orderServiceFallback(ServerRequest request) {
        return createFallbackResponse(request, "订单服务暂时不可用");
    }
    
    /**
     * 创建降级响应
     */
    private Mono<ServerResponse> createFallbackResponse(ServerRequest request, String message) {
        String path = request.path();
        String method = request.methodName();
        
        log.warn("服务降级 - {} {} - {}", method, path, message);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        result.put("path", path);
        result.put("method", method);
        
        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(JSON.toJSONString(result)));
    }
}