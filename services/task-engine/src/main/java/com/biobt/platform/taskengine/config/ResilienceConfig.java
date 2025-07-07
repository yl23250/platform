package com.biobt.platform.taskengine.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 弹性配置 - 熔断器、重试、超时等
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class ResilienceConfig {
    
    /**
     * 熔断器配置
     */
    @Bean
    public CircuitBreaker taskEngineCircuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 失败率阈值50%
                .waitDurationInOpenState(Duration.ofSeconds(30)) // 熔断器打开状态等待时间
                .slidingWindowSize(10) // 滑动窗口大小
                .minimumNumberOfCalls(5) // 最小调用次数
                .permittedNumberOfCallsInHalfOpenState(3) // 半开状态允许的调用次数
                .automaticTransitionFromOpenToHalfOpenEnabled(true) // 自动从打开状态转换到半开状态
                .build();
        
        return CircuitBreaker.of("taskEngine", config);
    }
    
    /**
     * 重试配置
     */
    @Bean
    public Retry taskEngineRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3) // 最大重试次数
                .waitDuration(Duration.ofSeconds(1)) // 重试间隔
                .retryOnException(throwable -> {
                    // 定义哪些异常需要重试
                    return throwable instanceof RuntimeException;
                })
                .build();
        
        return Retry.of("taskEngine", config);
    }
    
    /**
     * 超时配置
     */
    @Bean
    public TimeLimiter taskEngineTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(30)) // 超时时间30秒
                .cancelRunningFuture(true) // 取消正在运行的Future
                .build();
        
        return TimeLimiter.of("taskEngine", config);
    }
    
    /**
     * 消息服务熔断器
     */
    @Bean
    public CircuitBreaker messageServiceCircuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(60) // 消息服务容错率稍高
                .waitDurationInOpenState(Duration.ofSeconds(20))
                .slidingWindowSize(8)
                .minimumNumberOfCalls(3)
                .build();
        
        return CircuitBreaker.of("messageService", config);
    }
    
    /**
     * 工作流服务熔断器
     */
    @Bean
    public CircuitBreaker workflowServiceCircuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(40) // 工作流服务要求更高的可用性
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .slidingWindowSize(15)
                .minimumNumberOfCalls(8)
                .build();
        
        return CircuitBreaker.of("workflowService", config);
    }
}