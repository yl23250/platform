package com.biobt.platform.taskengine.executor;

import com.biobt.platform.taskengine.entity.TaskExecution;

import java.util.Map;

/**
 * 任务执行器接口
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
public interface TaskExecutor {
    
    /**
     * 执行任务
     * 
     * @param taskExecution 任务执行记录
     * @param parameters 执行参数
     * @return 执行结果
     * @throws Exception 执行异常
     */
    TaskExecutionResult execute(TaskExecution taskExecution, Map<String, Object> parameters) throws Exception;
    
    /**
     * 获取执行器名称
     * 
     * @return 执行器名称
     */
    String getExecutorName();
    
    /**
     * 获取执行器描述
     * 
     * @return 执行器描述
     */
    String getExecutorDescription();
    
    /**
     * 验证参数
     * 
     * @param parameters 参数
     * @return 验证结果
     */
    default boolean validateParameters(Map<String, Object> parameters) {
        return true;
    }
    
    /**
     * 获取所需参数
     * 
     * @return 参数列表
     */
    default String[] getRequiredParameters() {
        return new String[0];
    }
    
    /**
     * 是否支持并发执行
     * 
     * @return 是否支持并发
     */
    default boolean supportsConcurrentExecution() {
        return true;
    }
    
    /**
     * 获取预估执行时间（秒）
     * 
     * @param parameters 参数
     * @return 预估时间
     */
    default long getEstimatedExecutionTime(Map<String, Object> parameters) {
        return 60; // 默认1分钟
    }
    
    /**
     * 任务执行前的准备工作
     * 
     * @param taskExecution 任务执行记录
     * @param parameters 执行参数
     */
    default void beforeExecution(TaskExecution taskExecution, Map<String, Object> parameters) {
        // 默认空实现
    }
    
    /**
     * 任务执行后的清理工作
     * 
     * @param taskExecution 任务执行记录
     * @param parameters 执行参数
     * @param result 执行结果
     */
    default void afterExecution(TaskExecution taskExecution, Map<String, Object> parameters, TaskExecutionResult result) {
        // 默认空实现
    }
    
    /**
     * 任务执行异常处理
     * 
     * @param taskExecution 任务执行记录
     * @param parameters 执行参数
     * @param exception 异常
     */
    default void onExecutionException(TaskExecution taskExecution, Map<String, Object> parameters, Exception exception) {
        // 默认空实现
    }
    
    /**
     * 停止任务执行
     * 
     * @param taskExecution 任务执行记录
     */
    default void stopExecution(TaskExecution taskExecution) {
        // 默认空实现
    }
    
    /**
     * 获取执行进度
     * 
     * @param taskExecution 任务执行记录
     * @return 执行进度（0-100）
     */
    default int getExecutionProgress(TaskExecution taskExecution) {
        return 0;
    }
    
    /**
     * 获取执行状态描述
     * 
     * @param taskExecution 任务执行记录
     * @return 状态描述
     */
    default String getExecutionStatusDescription(TaskExecution taskExecution) {
        return "执行中";
    }
}