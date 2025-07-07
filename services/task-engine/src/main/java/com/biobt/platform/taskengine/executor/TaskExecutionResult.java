package com.biobt.platform.taskengine.executor;

import com.biobt.platform.taskengine.entity.TaskExecution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务执行结果
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutionResult {
    
    /**
     * 执行状态
     */
    private TaskExecution.ExecutionStatus status;
    
    /**
     * 执行结果数据
     */
    private Object resultData;
    
    /**
     * 执行消息
     */
    private String message;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 错误堆栈
     */
    private String errorStack;
    
    /**
     * 执行开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 执行耗时（毫秒）
     */
    private Long duration;
    
    /**
     * 执行进度（0-100）
     */
    private Integer progressPercent;
    
    /**
     * 进度描述
     */
    private String progressDescription;
    
    /**
     * 扩展属性
     */
    private Map<String, Object> attributes;
    
    /**
     * 是否需要重试
     */
    private Boolean needRetry;
    
    /**
     * 重试延迟（秒）
     */
    private Long retryDelay;
    
    /**
     * 输出文件路径
     */
    private String outputFilePath;
    
    /**
     * 日志文件路径
     */
    private String logFilePath;
    
    /**
     * 创建成功结果
     */
    public static TaskExecutionResult success() {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.SUCCESS)
                .message("任务执行成功")
                .endTime(LocalDateTime.now())
                .progressPercent(100)
                .needRetry(false)
                .build();
    }
    
    /**
     * 创建成功结果（带数据）
     */
    public static TaskExecutionResult success(Object resultData) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.SUCCESS)
                .resultData(resultData)
                .message("任务执行成功")
                .endTime(LocalDateTime.now())
                .progressPercent(100)
                .needRetry(false)
                .build();
    }
    
    /**
     * 创建成功结果（带数据和消息）
     */
    public static TaskExecutionResult success(Object resultData, String message) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.SUCCESS)
                .resultData(resultData)
                .message(message)
                .endTime(LocalDateTime.now())
                .progressPercent(100)
                .needRetry(false)
                .build();
    }
    
    /**
     * 创建失败结果
     */
    public static TaskExecutionResult failure(String errorMessage) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.FAILED)
                .errorMessage(errorMessage)
                .message("任务执行失败")
                .endTime(LocalDateTime.now())
                .needRetry(true)
                .build();
    }
    
    /**
     * 创建失败结果（带异常）
     */
    public static TaskExecutionResult failure(String errorMessage, Exception exception) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.FAILED)
                .errorMessage(errorMessage)
                .errorStack(getStackTrace(exception))
                .message("任务执行失败")
                .endTime(LocalDateTime.now())
                .needRetry(true)
                .build();
    }
    
    /**
     * 创建失败结果（不重试）
     */
    public static TaskExecutionResult failureNoRetry(String errorMessage) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.FAILED)
                .errorMessage(errorMessage)
                .message("任务执行失败")
                .endTime(LocalDateTime.now())
                .needRetry(false)
                .build();
    }
    
    /**
     * 创建超时结果
     */
    public static TaskExecutionResult timeout() {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.TIMEOUT)
                .errorMessage("任务执行超时")
                .message("任务执行超时")
                .endTime(LocalDateTime.now())
                .needRetry(true)
                .build();
    }
    
    /**
     * 创建取消结果
     */
    public static TaskExecutionResult cancelled() {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.CANCELLED)
                .message("任务已取消")
                .endTime(LocalDateTime.now())
                .needRetry(false)
                .build();
    }
    
    /**
     * 创建运行中结果
     */
    public static TaskExecutionResult running(int progressPercent, String progressDescription) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.RUNNING)
                .progressPercent(progressPercent)
                .progressDescription(progressDescription)
                .message("任务执行中")
                .needRetry(false)
                .build();
    }
    
    /**
     * 创建重试结果
     */
    public static TaskExecutionResult retry(String errorMessage, long retryDelay) {
        return TaskExecutionResult.builder()
                .status(TaskExecution.ExecutionStatus.RETRYING)
                .errorMessage(errorMessage)
                .message("任务准备重试")
                .endTime(LocalDateTime.now())
                .needRetry(true)
                .retryDelay(retryDelay)
                .build();
    }
    
    /**
     * 添加属性
     */
    public TaskExecutionResult addAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new java.util.HashMap<>();
        }
        this.attributes.put(key, value);
        return this;
    }
    
    /**
     * 设置执行时间
     */
    public TaskExecutionResult withDuration(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        if (startTime != null && endTime != null) {
            this.duration = java.time.Duration.between(startTime, endTime).toMillis();
        }
        return this;
    }
    
    /**
     * 设置输出文件
     */
    public TaskExecutionResult withOutputFile(String outputFilePath) {
        this.outputFilePath = outputFilePath;
        return this;
    }
    
    /**
     * 设置日志文件
     */
    public TaskExecutionResult withLogFile(String logFilePath) {
        this.logFilePath = logFilePath;
        return this;
    }
    
    /**
     * 是否执行成功
     */
    public boolean isSuccess() {
        return TaskExecution.ExecutionStatus.SUCCESS.equals(this.status);
    }
    
    /**
     * 是否执行失败
     */
    public boolean isFailure() {
        return TaskExecution.ExecutionStatus.FAILED.equals(this.status);
    }
    
    /**
     * 是否正在运行
     */
    public boolean isRunning() {
        return TaskExecution.ExecutionStatus.RUNNING.equals(this.status);
    }
    
    /**
     * 是否已取消
     */
    public boolean isCancelled() {
        return TaskExecution.ExecutionStatus.CANCELLED.equals(this.status);
    }
    
    /**
     * 是否超时
     */
    public boolean isTimeout() {
        return TaskExecution.ExecutionStatus.TIMEOUT.equals(this.status);
    }
    
    /**
     * 获取异常堆栈信息
     */
    private static String getStackTrace(Exception exception) {
        if (exception == null) {
            return null;
        }
        
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}