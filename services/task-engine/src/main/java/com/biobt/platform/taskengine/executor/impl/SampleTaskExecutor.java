package com.biobt.platform.taskengine.executor.impl;

import com.biobt.platform.taskengine.entity.TaskExecution;
import com.biobt.platform.taskengine.executor.TaskExecutionResult;
import com.biobt.platform.taskengine.executor.TaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 示例任务执行器
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@Component
public class SampleTaskExecutor implements TaskExecutor {
    
    @Override
    public TaskExecutionResult execute(TaskExecution taskExecution, Map<String, Object> parameters) throws Exception {
        LocalDateTime startTime = LocalDateTime.now();
        
        try {
            log.info("开始执行示例任务: {}", taskExecution.getTaskCode());
            
            // 获取参数
            String message = (String) parameters.getOrDefault("message", "Hello World");
            Integer duration = (Integer) parameters.getOrDefault("duration", 5);
            Boolean shouldFail = (Boolean) parameters.getOrDefault("shouldFail", false);
            
            log.info("任务参数 - message: {}, duration: {}, shouldFail: {}", message, duration, shouldFail);
            
            // 模拟任务执行过程
            for (int i = 1; i <= duration; i++) {
                Thread.sleep(1000); // 模拟耗时操作
                
                int progress = (i * 100) / duration;
                log.info("任务执行进度: {}%", progress);
                
                // 检查是否被取消
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("任务被中断: {}", taskExecution.getTaskCode());
                    return TaskExecutionResult.cancelled();
                }
            }
            
            // 模拟失败情况
            if (shouldFail) {
                throw new RuntimeException("模拟任务执行失败");
            }
            
            // 构造执行结果
            Map<String, Object> resultData = Map.of(
                    "message", message,
                    "executionTime", duration,
                    "timestamp", LocalDateTime.now().toString(),
                    "randomValue", ThreadLocalRandom.current().nextInt(1000)
            );
            
            TaskExecutionResult result = TaskExecutionResult.success(resultData, "示例任务执行成功")
                    .withDuration(startTime, LocalDateTime.now());
            
            log.info("示例任务执行完成: {}", taskExecution.getTaskCode());
            
            return result;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("任务被中断: {}", taskExecution.getTaskCode());
            return TaskExecutionResult.cancelled();
            
        } catch (Exception e) {
            log.error("示例任务执行失败: {}", taskExecution.getTaskCode(), e);
            return TaskExecutionResult.failure("任务执行异常: " + e.getMessage(), e)
                    .withDuration(startTime, LocalDateTime.now());
        }
    }
    
    @Override
    public String getExecutorName() {
        return "SampleTaskExecutor";
    }
    
    @Override
    public String getExecutorDescription() {
        return "示例任务执行器，用于演示任务执行流程";
    }
    
    @Override
    public boolean validateParameters(Map<String, Object> parameters) {
        // 验证duration参数
        Object duration = parameters.get("duration");
        if (duration != null) {
            try {
                int durationValue = Integer.parseInt(duration.toString());
                if (durationValue < 1 || durationValue > 300) {
                    log.warn("duration参数超出范围: {}", durationValue);
                    return false;
                }
            } catch (NumberFormatException e) {
                log.warn("duration参数格式错误: {}", duration);
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String[] getRequiredParameters() {
        return new String[]{"message"};
    }
    
    @Override
    public boolean supportsConcurrentExecution() {
        return true;
    }
    
    @Override
    public long getEstimatedExecutionTime(Map<String, Object> parameters) {
        Integer duration = (Integer) parameters.getOrDefault("duration", 5);
        return duration.longValue();
    }
    
    @Override
    public void beforeExecution(TaskExecution taskExecution, Map<String, Object> parameters) {
        log.info("任务执行前准备: {}", taskExecution.getTaskCode());
        
        // 可以在这里进行资源准备、环境检查等
        // 例如：检查文件系统空间、网络连接等
    }
    
    @Override
    public void afterExecution(TaskExecution taskExecution, Map<String, Object> parameters, TaskExecutionResult result) {
        log.info("任务执行后清理: {}, 结果: {}", taskExecution.getTaskCode(), result.getStatus());
        
        // 可以在这里进行资源清理、结果处理等
        // 例如：清理临时文件、发送通知等
    }
    
    @Override
    public void onExecutionException(TaskExecution taskExecution, Map<String, Object> parameters, Exception exception) {
        log.error("任务执行异常处理: {}", taskExecution.getTaskCode(), exception);
        
        // 可以在这里进行异常处理、告警等
        // 例如：发送告警邮件、记录详细日志等
    }
    
    @Override
    public void stopExecution(TaskExecution taskExecution) {
        log.info("停止任务执行: {}", taskExecution.getTaskCode());
        
        // 可以在这里实现任务停止逻辑
        // 例如：设置停止标志、中断线程等
    }
    
    @Override
    public int getExecutionProgress(TaskExecution taskExecution) {
        // 这里可以根据实际情况返回任务执行进度
        // 示例中返回固定值，实际应用中可以根据任务状态计算
        return 50;
    }
    
    @Override
    public String getExecutionStatusDescription(TaskExecution taskExecution) {
        return "示例任务正在执行中...";
    }
}