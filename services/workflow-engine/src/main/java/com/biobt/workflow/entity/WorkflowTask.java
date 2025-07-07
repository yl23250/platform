package com.biobt.workflow.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 工作流任务实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wf_task")
public class WorkflowTask extends BaseEntity {
    
    /**
     * 任务ID（Flowable引擎ID）
     */
    @Column(name = "task_id", length = 64, nullable = false)
    private String taskId;
    
    /**
     * 流程实例ID
     */
    @Column(name = "process_instance_id", length = 64, nullable = false)
    private String processInstanceId;
    
    /**
     * 流程定义ID
     */
    @Column(name = "process_definition_id", length = 64, nullable = false)
    private String processDefinitionId;
    
    /**
     * 任务定义Key
     */
    @Column(name = "task_definition_key", length = 64, nullable = false)
    private String taskDefinitionKey;
    
    /**
     * 任务名称
     */
    @Column(name = "task_name", length = 200, nullable = false)
    private String taskName;
    
    /**
     * 任务描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 任务分配人
     */
    @Column(name = "assignee", length = 64)
    private String assignee;
    
    /**
     * 任务分配人姓名
     */
    @Column(name = "assignee_name", length = 100)
    private String assigneeName;
    
    /**
     * 任务所有者
     */
    @Column(name = "owner", length = 64)
    private String owner;
    
    /**
     * 候选用户（逗号分隔）
     */
    @Column(name = "candidate_users", length = 1000)
    private String candidateUsers;
    
    /**
     * 候选组（逗号分隔）
     */
    @Column(name = "candidate_groups", length = 1000)
    private String candidateGroups;
    
    /**
     * 任务创建时间
     */
    @Column(name = "task_create_time")
    private LocalDateTime taskCreateTime;
    
    /**
     * 任务到期时间
     */
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    /**
     * 任务完成时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    /**
     * 任务持续时间（毫秒）
     */
    @Column(name = "duration")
    private Long duration;
    
    /**
     * 任务状态：CREATED-已创建，ASSIGNED-已分配，COMPLETED-已完成，DELEGATED-已委托，RESOLVED-已解决
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private TaskStatus status = TaskStatus.CREATED;
    
    /**
     * 任务优先级
     */
    @Column(name = "priority")
    private Integer priority = 50;
    
    /**
     * 表单Key
     */
    @Column(name = "form_key", length = 100)
    private String formKey;
    
    /**
     * 任务变量（JSON格式）
     */
    @Lob
    @Column(name = "task_variables")
    private String taskVariables;
    
    /**
     * 父任务ID
     */
    @Column(name = "parent_task_id", length = 64)
    private String parentTaskId;
    
    /**
     * 执行ID
     */
    @Column(name = "execution_id", length = 64)
    private String executionId;
    
    /**
     * 任务类别
     */
    @Column(name = "category", length = 50)
    private String category;
    
    /**
     * 是否挂起
     */
    @Column(name = "is_suspended")
    private Boolean isSuspended = false;
    
    /**
     * 任务状态枚举
     */
    public enum TaskStatus {
        CREATED,    // 已创建
        ASSIGNED,   // 已分配
        COMPLETED,  // 已完成
        DELEGATED,  // 已委托
        RESOLVED    // 已解决
    }
}