package com.biobt.message.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息模板实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "msg_template")
public class MessageTemplate extends BaseEntity {
    
    /**
     * 模板编码
     */
    @Column(name = "template_code", length = 50, nullable = false, unique = true)
    private String templateCode;
    
    /**
     * 模板名称
     */
    @Column(name = "template_name", length = 100, nullable = false)
    private String templateName;
    
    /**
     * 模板类型：EMAIL-邮件，SMS-短信，INTERNAL-站内信，PUSH-推送
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "template_type", length = 20, nullable = false)
    private TemplateType templateType;
    
    /**
     * 模板分类
     */
    @Column(name = "category", length = 50)
    private String category;
    
    /**
     * 模板标题
     */
    @Column(name = "title", length = 200)
    private String title;
    
    /**
     * 模板内容
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;
    
    /**
     * 模板参数（JSON格式）
     */
    @Lob
    @Column(name = "parameters")
    private String parameters;
    
    /**
     * 是否启用
     */
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;
    
    /**
     * 发送方式：SYNC-同步，ASYNC-异步
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "send_mode", length = 10, nullable = false)
    private SendMode sendMode = SendMode.ASYNC;
    
    /**
     * 优先级：1-最高，5-最低
     */
    @Column(name = "priority")
    private Integer priority = 3;
    
    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount = 3;
    
    /**
     * 模板描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 模板类型枚举
     */
    public enum TemplateType {
        EMAIL,    // 邮件
        SMS,      // 短信
        INTERNAL, // 站内信
        PUSH      // 推送
    }
    
    /**
     * 发送方式枚举
     */
    public enum SendMode {
        SYNC,  // 同步
        ASYNC  // 异步
    }
}