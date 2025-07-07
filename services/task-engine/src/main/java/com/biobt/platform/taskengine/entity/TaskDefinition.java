package com.biobt.platform.taskengine.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务定义实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "task_definition")
public class TaskDefinition extends BaseEntity {
    
    /**
     * 任务ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    
    /**
     * 任务编码
     */
    @Column(name = "task_code", nullable = false, unique = true, length = 100)
    private String taskCode;
    
    /**
     * 任务名称
     */
    @Column(name = "task_name", nullable = false, length = 200)
    private String taskName;
    
    /**
     * 任务描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 任务类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;
    
    /**
     * 任务分类
     */
    @Column(name = "category", length = 100)
    private String category;
    
    /**
     * 执行器类名
     */
    @Column(name = "executor_class", nullable = false, length = 500)
    private String executorClass;
    
    /**
     * 执行器方法
     */
    @Column(name = "executor_method", length = 100)
    private String executorMethod;
    
    /**
     * 任务参数
     */
    @Column(name = "task_params", columnDefinition = "TEXT")
    private String taskParams;
    
    /**
     * Cron表达式
     */
    @Column(name = "cron_expression", length = 100)
    private String cronExpression;
    
    /**
     * 任务状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;
    
    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;
    
    /**
     * 超时时间(秒)
     */
    @Column(name = "timeout_seconds")
    private Integer timeoutSeconds;
    
    /**
     * 最大重试次数
     */
    @Column(name = "max_retry_count")
    private Integer maxRetryCount;
    
    /**
     * 重试间隔(秒)
     */
    @Column(name = "retry_interval")
    private Integer retryInterval;
    
    /**
     * 是否允许并发执行
     */
    @Column(name = "allow_concurrent")
    private Boolean allowConcurrent;
    
    /**
     * 失败策略
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "failure_strategy")
    private FailureStrategy failureStrategy;
    
    /**
     * 告警邮箱
     */
    @Column(name = "alert_email", length = 500)
    private String alertEmail;
    
    /**
     * 下次执行时间
     */
    @Column(name = "next_execution_time")
    private LocalDateTime nextExecutionTime;
    
    /**
     * 最后执行时间
     */
    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;
    
    /**
     * 任务类型枚举
     */
    public enum TaskType {
        /**
         * 定时任务
         */
        SCHEDULED,
        
        /**
         * 立即执行
         */
        IMMEDIATE,
        
        /**
         * 延时任务
         */
        DELAYED,
        
        /**
         * 循环任务
         */
        RECURRING,
        
        /**
         * 工作流任务
         */
        WORKFLOW
    }
    
    /**
     * 任务状态枚举
     */
    public enum TaskStatus {
        /**
         * 草稿
         */
        DRAFT,
        
        /**
         * 启用
         */
        ENABLED,
        
        /**
         * 禁用
         */
        DISABLED,
        
        /**
         * 已删除
         */
        DELETED
    }
    
    /**
     * 失败策略枚举
     */
    public enum FailureStrategy {
        /**
         * 继续执行
         */
        CONTINUE,
        
        /**
         * 停止执行
         */
        STOP,
        
        /**
         * 重试
         */
        RETRY,
        
        /**
         * 告警
         */
        ALERT
    }
}