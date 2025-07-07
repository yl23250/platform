package com.biobt.message.entity;

import com.biobt.common.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息记录实体
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "msg_record")
public class MessageRecord extends BaseEntity {
    
    /**
     * 消息ID
     */
    @Column(name = "message_id", length = 64, nullable = false, unique = true)
    private String messageId;
    
    /**
     * 模板编码
     */
    @Column(name = "template_code", length = 50, nullable = false)
    private String templateCode;
    
    /**
     * 消息类型：EMAIL-邮件，SMS-短信，INTERNAL-站内信，PUSH-推送
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 20, nullable = false)
    private MessageType messageType;
    
    /**
     * 发送方
     */
    @Column(name = "sender", length = 100)
    private String sender;
    
    /**
     * 接收方
     */
    @Column(name = "receiver", length = 500, nullable = false)
    private String receiver;
    
    /**
     * 抄送方
     */
    @Column(name = "cc_receiver", length = 500)
    private String ccReceiver;
    
    /**
     * 密送方
     */
    @Column(name = "bcc_receiver", length = 500)
    private String bccReceiver;
    
    /**
     * 消息标题
     */
    @Column(name = "title", length = 200)
    private String title;
    
    /**
     * 消息内容
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;
    
    /**
     * 消息参数（JSON格式）
     */
    @Lob
    @Column(name = "parameters")
    private String parameters;
    
    /**
     * 发送状态：PENDING-待发送，SENDING-发送中，SUCCESS-发送成功，FAILED-发送失败
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "send_status", length = 20, nullable = false)
    private SendStatus sendStatus = SendStatus.PENDING;
    
    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private LocalDateTime sendTime;
    
    /**
     * 计划发送时间
     */
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    
    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount = 0;
    
    /**
     * 最大重试次数
     */
    @Column(name = "max_retry_count")
    private Integer maxRetryCount = 3;
    
    /**
     * 错误信息
     */
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
    
    /**
     * 业务类型
     */
    @Column(name = "business_type", length = 50)
    private String businessType;
    
    /**
     * 业务ID
     */
    @Column(name = "business_id", length = 64)
    private String businessId;
    
    /**
     * 优先级：1-最高，5-最低
     */
    @Column(name = "priority")
    private Integer priority = 3;
    
    /**
     * 是否已读（仅站内信）
     */
    @Column(name = "is_read")
    private Boolean isRead = false;
    
    /**
     * 阅读时间（仅站内信）
     */
    @Column(name = "read_time")
    private LocalDateTime readTime;
    
    /**
     * 外部消息ID（第三方平台返回的ID）
     */
    @Column(name = "external_message_id", length = 100)
    private String externalMessageId;
    
    /**
     * 消息类型枚举
     */
    public enum MessageType {
        EMAIL,    // 邮件
        SMS,      // 短信
        INTERNAL, // 站内信
        PUSH      // 推送
    }
    
    /**
     * 发送状态枚举
     */
    public enum SendStatus {
        PENDING,  // 待发送
        SENDING,  // 发送中
        SUCCESS,  // 发送成功
        FAILED    // 发送失败
    }
}