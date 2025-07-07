package com.biobt.platform.taskengine.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务执行记录实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "task_execution")
public class TaskExecution extends BaseEntity {
    
    /**
     * 执行ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long executionId;
    
    /**
     * 任务ID
     */
    @Column(name = "task_id", nullable = false)
    private Long taskId;
    
    /**
     * 任务编码
     */
    @Column(name = "task_code", nullable = false, length = 100)
    private String taskCode;
    
    /**
     * 任务名称
     */
    @Column(name = "task_name", nullable = false, length = 200)
    private String taskName;
    
    /**
     * 执行批次号
     */
    @Column(name = "batch_id", length = 100)
    private String batchId;
    
    /**
     * 执行状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "execution_status", nullable = false)
    private ExecutionStatus executionStatus;
    
    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    /**
     * 执行时长(毫秒)
     */
    @Column(name = "duration")
    private Long duration;
    
    /**
     * 执行参数
     */
    @Column(name = "execution_params", columnDefinition = "TEXT")
    private String executionParams;
    
    /**
     * 执行结果
     */
    @Column(name = "execution_result", columnDefinition = "TEXT")
    private String executionResult;
    
    /**
     * 错误信息
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    /**
     * 错误堆栈
     */
    @Column(name = "error_stack", columnDefinition = "TEXT")
    private String errorStack;
    
    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount;
    
    /**
     * 最大重试次数
     */
    @Column(name = "max_retry_count")
    private Integer maxRetryCount;
    
    /**
     * 下次重试时间
     */
    @Column(name = "next_retry_time")
    private LocalDateTime nextRetryTime;
    
    /**
     * 执行节点
     */
    @Column(name = "execution_node", length = 100)
    private String executionNode;
    
    /**
     * 执行器实例
     */
    @Column(name = "executor_instance", length = 100)
    private String executorInstance;
    
    /**
     * 进度百分比
     */
    @Column(name = "progress_percent")
    private Integer progressPercent;
    
    /**
     * 进度描述
     */
    @Column(name = "progress_description", length = 500)
    private String progressDescription;
    
    /**
     * 业务类型
     */
    @Column(name = "business_type", length = 100)
    private String businessType;
    
    /**
     * 业务ID
     */
    @Column(name = "business_id", length = 100)
    private String businessId;
    
    /**
     * 触发方式
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type")
    private TriggerType triggerType;
    
    /**
     * 触发用户
     */
    @Column(name = "trigger_user", length = 100)
    private String triggerUser;
    
    /**
     * 执行状态枚举
     */
    public enum ExecutionStatus {
        /**
         * 等待执行
         */
        PENDING,
        
        /**
         * 执行中
         */
        RUNNING,
        
        /**
         * 执行成功
         */
        SUCCESS,
        
        /**
         * 执行失败
         */
        FAILED,
        
        /**
         * 已取消
         */
        CANCELLED,
        
        /**
         * 超时
         */
        TIMEOUT,
        
        /**
         * 重试中
         */
        RETRYING
    }
    
    /**
     * 触发方式枚举
     */
    public enum TriggerType {
        /**
         * 定时触发
         */
        SCHEDULED,
        
        /**
         * 手动触发
         */
        MANUAL,
        
        /**
         * API触发
         */
        API,
        
        /**
         * 事件触发
         */
        EVENT,
        
        /**
         * 依赖触发
         */
        DEPENDENCY
    }
}