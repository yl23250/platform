package com.biobt.platform.taskengine.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final String code;
    private final HttpStatus status;
    
    public BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
        this.status = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.status = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
    
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String code, String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }
    
    // 常用业务异常静态方法
    
    public static BusinessException taskNotFound(String taskId) {
        return new BusinessException("TASK_NOT_FOUND", 
                String.format("任务不存在: %s", taskId), HttpStatus.NOT_FOUND);
    }
    
    public static BusinessException taskAlreadyRunning(String taskId) {
        return new BusinessException("TASK_ALREADY_RUNNING", 
                String.format("任务正在运行中: %s", taskId), HttpStatus.CONFLICT);
    }
    
    public static BusinessException taskExecutionFailed(String taskId, String reason) {
        return new BusinessException("TASK_EXECUTION_FAILED", 
                String.format("任务执行失败: %s, 原因: %s", taskId, reason));
    }
    
    public static BusinessException invalidTaskStatus(String taskId, String currentStatus, String expectedStatus) {
        return new BusinessException("INVALID_TASK_STATUS", 
                String.format("任务状态无效: %s, 当前状态: %s, 期望状态: %s", taskId, currentStatus, expectedStatus));
    }
    
    public static BusinessException cronExpressionInvalid(String cronExpression) {
        return new BusinessException("CRON_EXPRESSION_INVALID", 
                String.format("Cron表达式无效: %s", cronExpression));
    }
    
    public static BusinessException taskCodeDuplicate(String taskCode) {
        return new BusinessException("TASK_CODE_DUPLICATE", 
                String.format("任务编码重复: %s", taskCode), HttpStatus.CONFLICT);
    }
    
    public static BusinessException executorClassNotFound(String executorClass) {
        return new BusinessException("EXECUTOR_CLASS_NOT_FOUND", 
                String.format("执行器类不存在: %s", executorClass));
    }
    
    public static BusinessException executorMethodNotFound(String executorClass, String executorMethod) {
        return new BusinessException("EXECUTOR_METHOD_NOT_FOUND", 
                String.format("执行器方法不存在: %s.%s", executorClass, executorMethod));
    }
    
    public static BusinessException taskTimeoutExceeded(String taskId, long timeoutSeconds) {
        return new BusinessException("TASK_TIMEOUT_EXCEEDED", 
                String.format("任务执行超时: %s, 超时时间: %d秒", taskId, timeoutSeconds));
    }
    
    public static BusinessException maxRetryCountExceeded(String taskId, int maxRetryCount) {
        return new BusinessException("MAX_RETRY_COUNT_EXCEEDED", 
                String.format("任务重试次数超限: %s, 最大重试次数: %d", taskId, maxRetryCount));
    }
    
    public static BusinessException taskScheduleConflict(String taskId, String conflictReason) {
        return new BusinessException("TASK_SCHEDULE_CONFLICT", 
                String.format("任务调度冲突: %s, 冲突原因: %s", taskId, conflictReason), HttpStatus.CONFLICT);
    }
    
    public static BusinessException resourceNotAvailable(String resourceType, String resourceId) {
        return new BusinessException("RESOURCE_NOT_AVAILABLE", 
                String.format("资源不可用: %s[%s]", resourceType, resourceId));
    }
    
    public static BusinessException parameterRequired(String parameterName) {
        return new BusinessException("PARAMETER_REQUIRED", 
                String.format("参数必填: %s", parameterName));
    }
    
    public static BusinessException parameterInvalid(String parameterName, String reason) {
        return new BusinessException("PARAMETER_INVALID", 
                String.format("参数无效: %s, 原因: %s", parameterName, reason));
    }
    
    public static BusinessException operationNotAllowed(String operation, String reason) {
        return new BusinessException("OPERATION_NOT_ALLOWED", 
                String.format("操作不允许: %s, 原因: %s", operation, reason), HttpStatus.FORBIDDEN);
    }
    
    public static BusinessException systemBusy() {
        return new BusinessException("SYSTEM_BUSY", 
                "系统繁忙，请稍后重试", HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    public static BusinessException configurationError(String configKey, String reason) {
        return new BusinessException("CONFIGURATION_ERROR", 
                String.format("配置错误: %s, 原因: %s", configKey, reason));
    }
}