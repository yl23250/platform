package com.biobt.workflow.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 工作流实例实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wf_instance")
public class WorkflowInstance extends BaseEntity {
    
    /**
     * 流程实例ID（Flowable引擎ID）
     */
    @Column(name = "process_instance_id", length = 64, nullable = false)
    private String processInstanceId;
    
    /**
     * 流程定义ID
     */
    @Column(name = "process_definition_id", length = 64, nullable = false)
    private String processDefinitionId;
    
    /**
     * 流程定义Key
     */
    @Column(name = "process_key", length = 64, nullable = false)
    private String processKey;
    
    /**
     * 流程名称
     */
    @Column(name = "process_name", length = 100, nullable = false)
    private String processName;
    
    /**
     * 业务Key
     */
    @Column(name = "business_key", length = 100)
    private String businessKey;
    
    /**
     * 业务类型
     */
    @Column(name = "business_type", length = 50)
    private String businessType;
    
    /**
     * 流程标题
     */
    @Column(name = "title", length = 200)
    private String title;
    
    /**
     * 流程发起人
     */
    @Column(name = "starter_user_id", length = 64, nullable = false)
    private String starterUserId;
    
    /**
     * 流程发起人姓名
     */
    @Column(name = "starter_user_name", length = 100)
    private String starterUserName;
    
    /**
     * 流程开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    /**
     * 流程结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    /**
     * 流程持续时间（毫秒）
     */
    @Column(name = "duration")
    private Long duration;
    
    /**
     * 流程状态：RUNNING-运行中，COMPLETED-已完成，SUSPENDED-已挂起，TERMINATED-已终止
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProcessInstanceStatus status = ProcessInstanceStatus.RUNNING;
    
    /**
     * 当前节点
     */
    @Column(name = "current_activity", length = 100)
    private String currentActivity;
    
    /**
     * 当前节点名称
     */
    @Column(name = "current_activity_name", length = 200)
    private String currentActivityName;
    
    /**
     * 流程变量（JSON格式）
     */
    @Lob
    @Column(name = "variables")
    private String variables;
    
    /**
     * 父流程实例ID
     */
    @Column(name = "parent_process_instance_id", length = 64)
    private String parentProcessInstanceId;
    
    /**
     * 流程优先级
     */
    @Column(name = "priority")
    private Integer priority = 50;
    
    /**
     * 流程实例状态枚举
     */
    public enum ProcessInstanceStatus {
        RUNNING,    // 运行中
        COMPLETED,  // 已完成
        SUSPENDED,  // 已挂起
        TERMINATED  // 已终止
    }
}