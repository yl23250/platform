package com.biobt.workflow.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工作流定义实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wf_definition")
public class WorkflowDefinition extends BaseEntity {
    
    /**
     * 流程定义ID（Flowable引擎ID）
     */
    @Column(name = "process_definition_id", length = 64)
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
     * 流程描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 流程版本
     */
    @Column(name = "version", nullable = false)
    private Integer version;
    
    /**
     * 流程分类
     */
    @Column(name = "category", length = 50)
    private String category;
    
    /**
     * BPMN XML内容
     */
    @Lob
    @Column(name = "bpmn_xml")
    private String bpmnXml;
    
    /**
     * 流程图片
     */
    @Column(name = "diagram_resource_name", length = 200)
    private String diagramResourceName;
    
    /**
     * 是否激活
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * 是否可启动
     */
    @Column(name = "is_startable", nullable = false)
    private Boolean isStartable = true;
    
    /**
     * 部署ID
     */
    @Column(name = "deployment_id", length = 64)
    private String deploymentId;
    
    /**
     * 表单Key
     */
    @Column(name = "form_key", length = 100)
    private String formKey;
    
    /**
     * 流程状态：DRAFT-草稿，PUBLISHED-已发布，DEPRECATED-已废弃
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProcessStatus status = ProcessStatus.DRAFT;
    
    /**
     * 流程状态枚举
     */
    public enum ProcessStatus {
        DRAFT,      // 草稿
        PUBLISHED,  // 已发布
        DEPRECATED  // 已废弃
    }
}