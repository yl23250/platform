package com.biobt.platform.taskengine.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class MonitoringConfig {
    
    /**
     * 任务执行计数器
     */
    @Bean
    public Counter taskExecutionCounter(MeterRegistry meterRegistry) {
        return Counter.builder("task.execution.total")
                .description("Total number of task executions")
                .register(meterRegistry);
    }
    
    /**
     * 任务执行成功计数器
     */
    @Bean
    public Counter taskExecutionSuccessCounter(MeterRegistry meterRegistry) {
        return Counter.builder("task.execution.success")
                .description("Number of successful task executions")
                .register(meterRegistry);
    }
    
    /**
     * 任务执行失败计数器
     */
    @Bean
    public Counter taskExecutionFailureCounter(MeterRegistry meterRegistry) {
        return Counter.builder("task.execution.failure")
                .description("Number of failed task executions")
                .register(meterRegistry);
    }
    
    /**
     * 任务执行时间计时器
     */
    @Bean
    public Timer taskExecutionTimer(MeterRegistry meterRegistry) {
        return Timer.builder("task.execution.duration")
                .description("Task execution duration")
                .register(meterRegistry);
    }
    
    /**
     * 活跃任务数量指标
     */
    @Bean
    public Gauge activeTasksGauge(MeterRegistry meterRegistry, TaskMetrics taskMetrics) {
        return Gauge.builder("task.active.count")
                .description("Number of active tasks")
                .register(meterRegistry, taskMetrics, TaskMetrics::getActiveTasksCount);
    }
    
    /**
     * 等待任务数量指标
     */
    @Bean
    public Gauge pendingTasksGauge(MeterRegistry meterRegistry, TaskMetrics taskMetrics) {
        return Gauge.builder("task.pending.count")
                .description("Number of pending tasks")
                .register(meterRegistry, taskMetrics, TaskMetrics::getPendingTasksCount);
    }
    
    /**
     * 任务指标收集器
     */
    @Component
    public static class TaskMetrics {
        
        private final AtomicInteger activeTasksCount = new AtomicInteger(0);
        private final AtomicInteger pendingTasksCount = new AtomicInteger(0);
        private final AtomicInteger completedTasksCount = new AtomicInteger(0);
        
        public int getActiveTasksCount() {
            return activeTasksCount.get();
        }
        
        public int getPendingTasksCount() {
            return pendingTasksCount.get();
        }
        
        public int getCompletedTasksCount() {
            return completedTasksCount.get();
        }
        
        public void incrementActiveTasksCount() {
            activeTasksCount.incrementAndGet();
        }
        
        public void decrementActiveTasksCount() {
            activeTasksCount.decrementAndGet();
        }
        
        public void incrementPendingTasksCount() {
            pendingTasksCount.incrementAndGet();
        }
        
        public void decrementPendingTasksCount() {
            pendingTasksCount.decrementAndGet();
        }
        
        public void incrementCompletedTasksCount() {
            completedTasksCount.incrementAndGet();
        }
        
        /**
         * 定期更新指标
         */
        @Scheduled(fixedRate = 30000) // 每30秒更新一次
        public void updateMetrics() {
            // TODO: 从数据库或缓存中获取实际的任务统计数据
            // 这里可以查询数据库获取真实的任务状态统计
        }
    }
}