package com.biobt.platform.taskengine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Value("${task.engine.execution.core-pool-size:10}")
    private int corePoolSize;
    
    @Value("${task.engine.execution.max-pool-size:50}")
    private int maxPoolSize;
    
    @Value("${task.engine.execution.queue-capacity:1000}")
    private int queueCapacity;
    
    @Value("${task.engine.execution.keep-alive-seconds:60}")
    private int keepAliveSeconds;
    
    @Value("${task.engine.execution.thread-name-prefix:task-executor-}")
    private String threadNamePrefix;
    
    /**
     * 任务执行线程池
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(corePoolSize);
        
        // 最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        
        // 队列容量
        executor.setQueueCapacity(queueCapacity);
        
        // 线程空闲时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        
        // 线程名前缀
        executor.setThreadNamePrefix(threadNamePrefix);
        
        // 拒绝策略：由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待时间
        executor.setAwaitTerminationSeconds(60);
        
        // 初始化
        executor.initialize();
        
        log.info("任务执行线程池初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}", 
                corePoolSize, maxPoolSize, queueCapacity);
        
        return executor;
    }
    
    /**
     * 消息发送线程池
     */
    @Bean(name = "messageExecutor")
    public Executor messageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 消息发送使用较小的线程池
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("message-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        
        log.info("消息发送线程池初始化完成 - 核心线程数: 5, 最大线程数: 20");
        
        return executor;
    }
    
    /**
     * 监控线程池
     */
    @Bean(name = "monitorExecutor")
    public Executor monitorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 监控使用单独的小线程池
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("monitor-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        
        log.info("监控线程池初始化完成 - 核心线程数: 2, 最大线程数: 5");
        
        return executor;
    }
    
    /**
     * 调度线程池
     */
    @Bean(name = "schedulerExecutor")
    public Executor schedulerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 调度器使用专门的线程池
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(120);
        executor.setThreadNamePrefix("scheduler-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        
        log.info("调度线程池初始化完成 - 核心线程数: 8, 最大线程数: 16");
        
        return executor;
    }
}