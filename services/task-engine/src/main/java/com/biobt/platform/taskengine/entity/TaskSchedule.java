package com.biobt.platform.taskengine.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务调度实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "task_schedule")
public class TaskSchedule extends BaseEntity {
    
    /**
     * 调度ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    
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
     * 调度名称
     */
    @Column(name = "schedule_name", nullable = false, length = 200)
    private String scheduleName;
    
    /**
     * 调度类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false)
    private ScheduleType scheduleType;
    
    /**
     * Cron表达式
     */
    @Column(name = "cron_expression", length = 100)
    private String cronExpression;
    
    /**
     * 固定延迟(秒)
     */
    @Column(name = "fixed_delay")
    private Long fixedDelay;
    
    /**
     * 固定频率(秒)
     */
    @Column(name = "fixed_rate")
    private Long fixedRate;
    
    /**
     * 初始延迟(秒)
     */
    @Column(name = "initial_delay")
    private Long initialDelay;
    
    /**
     * 调度状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status", nullable = false)
    private ScheduleStatus scheduleStatus;
    
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
     * 下次执行时间
     */
    @Column(name = "next_fire_time")
    private LocalDateTime nextFireTime;
    
    /**
     * 上次执行时间
     */
    @Column(name = "prev_fire_time")
    private LocalDateTime prevFireTime;
    
    /**
     * 执行次数
     */
    @Column(name = "fire_count")
    private Long fireCount;
    
    /**
     * 最大执行次数
     */
    @Column(name = "max_fire_count")
    private Long maxFireCount;
    
    /**
     * 失败次数
     */
    @Column(name = "failure_count")
    private Long failureCount;
    
    /**
     * 成功次数
     */
    @Column(name = "success_count")
    private Long successCount;
    
    /**
     * 时区
     */
    @Column(name = "time_zone", length = 50)
    private String timeZone;
    
    /**
     * 调度参数
     */
    @Column(name = "schedule_params", columnDefinition = "TEXT")
    private String scheduleParams;
    
    /**
     * 调度描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 创建用户
     */
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    /**
     * 更新用户
     */
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
    
    /**
     * 调度类型枚举
     */
    public enum ScheduleType {
        /**
         * Cron表达式
         */
        CRON,
        
        /**
         * 固定延迟
         */
        FIXED_DELAY,
        
        /**
         * 固定频率
         */
        FIXED_RATE,
        
        /**
         * 一次性
         */
        ONCE,
        
        /**
         * 手动触发
         */
        MANUAL
    }
    
    /**
     * 调度状态枚举
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL,
        
        /**
         * 暂停
         */
        PAUSED,
        
        /**
         * 阻塞
         */
        BLOCKED,
        
        /**
         * 错误
         */
        ERROR,
        
        /**
         * 完成
         */
        COMPLETE
    }
}