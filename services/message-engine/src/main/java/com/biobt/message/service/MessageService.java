package com.biobt.message.service;

import com.biobt.message.entity.MessageRecord;
import com.biobt.message.entity.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 消息服务类
 * 提供消息发送和管理功能
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Service
@Slf4j
public class MessageService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Value("${message.email.from:}")
    private String emailFrom;
    
    @Value("${message.email.from-name:BioBt平台}")
    private String emailFromName;
    
    @Value("${message.queue.email-topic:MESSAGE_EMAIL}")
    private String emailTopic;
    
    @Value("${message.queue.sms-topic:MESSAGE_SMS}")
    private String smsTopic;
    
    @Value("${message.queue.internal-topic:MESSAGE_INTERNAL}")
    private String internalTopic;
    
    @Value("${message.queue.push-topic:MESSAGE_PUSH}")
    private String pushTopic;
    
    /**
     * 发送消息
     */
    @Transactional
    public MessageRecord sendMessage(MessageTemplate template, String receiver, 
                                   Map<String, Object> parameters, String businessType, String businessId) {
        // 创建消息记录
        MessageRecord record = createMessageRecord(template, receiver, parameters, businessType, businessId);
        
        try {
            // 根据模板类型和发送方式处理消息
            if (template.getSendMode() == MessageTemplate.SendMode.SYNC) {
                // 同步发送
                sendMessageSync(record);
            } else {
                // 异步发送
                sendMessageAsync(record);
            }
            
            log.info("消息发送请求成功: {} - {} - {}", record.getMessageId(), 
                    record.getMessageType(), record.getReceiver());
            
        } catch (Exception e) {
            record.setSendStatus(MessageRecord.SendStatus.FAILED);
            record.setErrorMessage(e.getMessage());
            log.error("消息发送失败: {} - {}", record.getMessageId(), e.getMessage(), e);
        }
        
        return record;
    }
    
    /**
     * 同步发送消息
     */
    private void sendMessageSync(MessageRecord record) {
        switch (record.getMessageType()) {
            case EMAIL:
                sendEmailSync(record);
                break;
            case SMS:
                sendSmsSync(record);
                break;
            case INTERNAL:
                sendInternalMessageSync(record);
                break;
            case PUSH:
                sendPushSync(record);
                break;
            default:
                throw new IllegalArgumentException("不支持的消息类型: " + record.getMessageType());
        }
    }
    
    /**
     * 异步发送消息
     */
    private void sendMessageAsync(MessageRecord record) {
        String topic = getTopicByMessageType(record.getMessageType());
        rocketMQTemplate.asyncSend(topic, record, new MessageSendCallback(record));
        record.setSendStatus(MessageRecord.SendStatus.SENDING);
    }
    
    /**
     * 同步发送邮件
     */
    private void sendEmailSync(MessageRecord record) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailFrom, emailFromName);
            helper.setTo(record.getReceiver().split(","));
            helper.setSubject(record.getTitle());
            helper.setText(record.getContent(), true);
            
            // 处理抄送
            if (record.getCcReceiver() != null && !record.getCcReceiver().trim().isEmpty()) {
                helper.setCc(record.getCcReceiver().split(","));
            }
            
            // 处理密送
            if (record.getBccReceiver() != null && !record.getBccReceiver().trim().isEmpty()) {
                helper.setBcc(record.getBccReceiver().split(","));
            }
            
            mailSender.send(message);
            
            record.setSendStatus(MessageRecord.SendStatus.SUCCESS);
            record.setSendTime(LocalDateTime.now());
            
            log.info("邮件发送成功: {} - {}", record.getMessageId(), record.getReceiver());
            
        } catch (Exception e) {
            record.setSendStatus(MessageRecord.SendStatus.FAILED);
            record.setErrorMessage(e.getMessage());
            log.error("邮件发送失败: {} - {}", record.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }
    
    /**
     * 同步发送短信
     */
    private void sendSmsSync(MessageRecord record) {
        try {
            // TODO: 集成短信服务提供商（阿里云、腾讯云等）
            log.info("短信发送功能待实现: {} - {}", record.getMessageId(), record.getReceiver());
            
            record.setSendStatus(MessageRecord.SendStatus.SUCCESS);
            record.setSendTime(LocalDateTime.now());
            
        } catch (Exception e) {
            record.setSendStatus(MessageRecord.SendStatus.FAILED);
            record.setErrorMessage(e.getMessage());
            log.error("短信发送失败: {} - {}", record.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("短信发送失败", e);
        }
    }
    
    /**
     * 同步发送站内信
     */
    private void sendInternalMessageSync(MessageRecord record) {
        try {
            // 通过WebSocket推送站内信
            messagingTemplate.convertAndSendToUser(
                    record.getReceiver(), 
                    "/queue/message", 
                    record
            );
            
            record.setSendStatus(MessageRecord.SendStatus.SUCCESS);
            record.setSendTime(LocalDateTime.now());
            
            log.info("站内信发送成功: {} - {}", record.getMessageId(), record.getReceiver());
            
        } catch (Exception e) {
            record.setSendStatus(MessageRecord.SendStatus.FAILED);
            record.setErrorMessage(e.getMessage());
            log.error("站内信发送失败: {} - {}", record.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("站内信发送失败", e);
        }
    }
    
    /**
     * 同步发送推送
     */
    private void sendPushSync(MessageRecord record) {
        try {
            // TODO: 集成推送服务（极光推送、个推等）
            log.info("推送发送功能待实现: {} - {}", record.getMessageId(), record.getReceiver());
            
            record.setSendStatus(MessageRecord.SendStatus.SUCCESS);
            record.setSendTime(LocalDateTime.now());
            
        } catch (Exception e) {
            record.setSendStatus(MessageRecord.SendStatus.FAILED);
            record.setErrorMessage(e.getMessage());
            log.error("推送发送失败: {} - {}", record.getMessageId(), e.getMessage(), e);
            throw new RuntimeException("推送发送失败", e);
        }
    }
    
    /**
     * 渲染模板内容
     */
    public String renderTemplate(String templateContent, Map<String, Object> parameters) {
        try {
            Context context = new Context();
            if (parameters != null) {
                context.setVariables(parameters);
            }
            return templateEngine.process(templateContent, context);
        } catch (Exception e) {
            log.error("模板渲染失败: {}", e.getMessage(), e);
            return templateContent; // 返回原始内容
        }
    }
    
    /**
     * 创建消息记录
     */
    private MessageRecord createMessageRecord(MessageTemplate template, String receiver, 
                                            Map<String, Object> parameters, String businessType, String businessId) {
        MessageRecord record = new MessageRecord();
        record.setMessageId(UUID.randomUUID().toString());
        record.setTemplateCode(template.getTemplateCode());
        record.setMessageType(MessageRecord.MessageType.valueOf(template.getTemplateType().name()));
        record.setSender(emailFrom);
        record.setReceiver(receiver);
        record.setTitle(renderTemplate(template.getTitle(), parameters));
        record.setContent(renderTemplate(template.getContent(), parameters));
        record.setBusinessType(businessType);
        record.setBusinessId(businessId);
        record.setPriority(template.getPriority());
        record.setMaxRetryCount(template.getRetryCount());
        record.setSendStatus(MessageRecord.SendStatus.PENDING);
        
        return record;
    }
    
    /**
     * 根据消息类型获取队列主题
     */
    private String getTopicByMessageType(MessageRecord.MessageType messageType) {
        switch (messageType) {
            case EMAIL:
                return emailTopic;
            case SMS:
                return smsTopic;
            case INTERNAL:
                return internalTopic;
            case PUSH:
                return pushTopic;
            default:
                throw new IllegalArgumentException("不支持的消息类型: " + messageType);
        }
    }
    
    /**
     * 消息发送回调
     */
    private static class MessageSendCallback implements org.apache.rocketmq.client.producer.SendCallback {
        private final MessageRecord record;
        
        public MessageSendCallback(MessageRecord record) {
            this.record = record;
        }
        
        @Override
        public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
            log.info("消息队列发送成功: {} - {}", record.getMessageId(), sendResult.getMsgId());
        }
        
        @Override
        public void onException(Throwable e) {
            log.error("消息队列发送失败: {} - {}", record.getMessageId(), e.getMessage(), e);
        }
    }
}