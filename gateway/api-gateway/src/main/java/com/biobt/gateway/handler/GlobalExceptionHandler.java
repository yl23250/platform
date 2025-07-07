package com.biobt.gateway.handler;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 统一处理网关层的异常
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Component
@Order(-1)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        
        // 设置响应头
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        HttpStatus status;
        String message;

        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "请求的服务不存在";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;

            status = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
            message = responseStatusException.getReason();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "网关内部错误";
        }
        
        // 记录异常日志
        log.error("网关异常处理 - {} {} - 状态码: {} - 错误: {}", 
                exchange.getRequest().getMethod().name(),
                exchange.getRequest().getURI().getPath(),
                status.value(),
                ex.getMessage(), ex);
        
        response.setStatusCode(status);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", status.value());
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        result.put("path", exchange.getRequest().getURI().getPath());
        result.put("method", exchange.getRequest().getMethod().name());
        
        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Flux.just(buffer));
    }
}