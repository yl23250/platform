package com.biobt.platform.messageengine.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.common.core.result.Result;
import com.biobt.platform.messageengine.entity.MessageRecord;
import com.biobt.platform.messageengine.entity.MessageTemplate;
import com.biobt.platform.messageengine.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 消息引擎控制器
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/message-engine")
@RequiredArgsConstructor
@Tag(name = "消息引擎", description = "消息引擎管理API")
public class MessageEngineController {
    
    private final MessageService messageService;
    
    /**
     * 发送邮件消息
     */
    @PostMapping("/messages/email")
    @Operation(summary = "发送邮件消息", description = "发送邮件消息")
    public Result<MessageRecord> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String content,
            @RequestParam(required = false) String cc,
            @RequestParam(required = false) String bcc) {
        log.info("发送邮件消息: to={}, subject={}", to, subject);
        
        try {
            MessageRecord result = messageService.sendEmail(to, subject, content, cc, bcc);
            return Result.success(result);
        } catch (Exception e) {
            log.error("发送邮件消息失败", e);
            return Result.error("发送邮件消息失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送短信消息
     */
    @PostMapping("/messages/sms")
    @Operation(summary = "发送短信消息", description = "发送短信消息")
    public Result<MessageRecord> sendSms(
            @RequestParam String phone,
            @RequestParam String content) {
        log.info("发送短信消息: phone={}, content={}", phone, content);
        
        try {
            MessageRecord result = messageService.sendSms(phone, content);
            return Result.success(result);
        } catch (Exception e) {
            log.error("发送短信消息失败", e);
            return Result.error("发送短信消息失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送内部消息
     */
    @PostMapping("/messages/internal")
    @Operation(summary = "发送内部消息", description = "发送内部消息")
    public Result<MessageRecord> sendInternalMessage(
            @RequestParam String userId,
            @RequestParam String title,
            @RequestParam String content) {
        log.info("发送内部消息: userId={}, title={}", userId, title);
        
        try {
            MessageRecord result = messageService.sendInternalMessage(userId, title, content);
            return Result.success(result);
        } catch (Exception e) {
            log.error("发送内部消息失败", e);
            return Result.error("发送内部消息失败: " + e.getMessage());
        }
    }
    
    /**
     * 发送推送消息
     */
    @PostMapping("/messages/push")
    @Operation(summary = "发送推送消息", description = "发送推送消息")
    public Result<MessageRecord> sendPushMessage(
            @RequestParam String userId,
            @RequestParam String title,
            @RequestParam String content) {
        log.info("发送推送消息: userId={}, title={}", userId, title);
        
        try {
            MessageRecord result = messageService.sendPushMessage(userId, title, content);
            return Result.success(result);
        } catch (Exception e) {
            log.error("发送推送消息失败", e);
            return Result.error("发送推送消息失败: " + e.getMessage());
        }
    }
    
    /**
     * 使用模板发送消息
     */
    @PostMapping("/messages/template")
    @Operation(summary = "使用模板发送消息", description = "使用消息模板发送消息")
    public Result<MessageRecord> sendMessageByTemplate(
            @RequestParam String templateCode,
            @RequestParam String receiver,
            @RequestBody Map<String, Object> parameters) {
        log.info("使用模板发送消息: templateCode={}, receiver={}", templateCode, receiver);
        
        try {
            MessageRecord result = messageService.sendMessageByTemplate(templateCode, receiver, parameters);
            return Result.success(result);
        } catch (Exception e) {
            log.error("使用模板发送消息失败", e);
            return Result.error("使用模板发送消息失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建消息模板
     */
    @PostMapping("/templates")
    @Operation(summary = "创建消息模板", description = "创建新的消息模板")
    public Result<MessageTemplate> createTemplate(@RequestBody MessageTemplate template) {
        log.info("创建消息模板: {}", template.getTemplateCode());
        
        try {
            MessageTemplate result = messageService.createTemplate(template);
            return Result.success(result);
        } catch (Exception e) {
            log.error("创建消息模板失败", e);
            return Result.error("创建消息模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新消息模板
     */
    @PutMapping("/templates/{templateId}")
    @Operation(summary = "更新消息模板", description = "更新指定的消息模板")
    public Result<MessageTemplate> updateTemplate(
            @Parameter(description = "模板ID") @PathVariable Long templateId,
            @RequestBody MessageTemplate template) {
        log.info("更新消息模板: {}", templateId);
        
        try {
            template.setTemplateId(templateId);
            MessageTemplate result = messageService.updateTemplate(template);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新消息模板失败", e);
            return Result.error("更新消息模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除消息模板
     */
    @DeleteMapping("/templates/{templateId}")
    @Operation(summary = "删除消息模板", description = "删除指定的消息模板")
    public Result<Void> deleteTemplate(@Parameter(description = "模板ID") @PathVariable Long templateId) {
        log.info("删除消息模板: {}", templateId);
        
        try {
            messageService.deleteTemplate(templateId);
            return Result.success();
        } catch (Exception e) {
            log.error("删除消息模板失败", e);
            return Result.error("删除消息模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取消息记录
     */
    @GetMapping("/messages")
    @Operation(summary = "获取消息记录", description = "分页获取消息记录")
    public Result<Page<MessageRecord>> getMessages(
            @Parameter(description = "消息类型") @RequestParam(required = false) String messageType,
            @Parameter(description = "发送状态") @RequestParam(required = false) String sendStatus,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") int size) {
        log.info("获取消息记录: messageType={}, sendStatus={}", messageType, sendStatus);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MessageRecord> result = messageService.getMessages(messageType, sendStatus, pageable);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取消息记录失败", e);
            return Result.error("获取消息记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取消息模板
     */
    @GetMapping("/templates")
    @Operation(summary = "获取消息模板", description = "分页获取消息模板")
    public Result<Page<MessageTemplate>> getTemplates(
            @Parameter(description = "模板类型") @RequestParam(required = false) String templateType,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") int size) {
        log.info("获取消息模板: templateType={}", templateType);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MessageTemplate> result = messageService.getTemplates(templateType, pageable);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取消息模板失败", e);
            return Result.error("获取消息模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 重发消息
     */
    @PostMapping("/messages/{messageId}/resend")
    @Operation(summary = "重发消息", description = "重新发送指定的消息")
    public Result<MessageRecord> resendMessage(@Parameter(description = "消息ID") @PathVariable String messageId) {
        log.info("重发消息: {}", messageId);
        
        try {
            MessageRecord result = messageService.resendMessage(messageId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("重发消息失败", e);
            return Result.error("重发消息失败: " + e.getMessage());
        }
    }
    
    /**
     * 标记消息为已读
     */
    @PostMapping("/messages/{messageId}/read")
    @Operation(summary = "标记消息为已读", description = "标记指定消息为已读")
    public Result<Void> markMessageAsRead(@Parameter(description = "消息ID") @PathVariable String messageId) {
        log.info("标记消息为已读: {}", messageId);
        
        try {
            messageService.markAsRead(messageId);
            return Result.success();
        } catch (Exception e) {
            log.error("标记消息为已读失败", e);
            return Result.error("标记消息为已读失败: " + e.getMessage());
        }
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查消息引擎服务健康状态")
    public Result<Map<String, Object>> health() {
        return Result.success(Map.of(
            "status", "UP",
            "service", "message-engine",
            "timestamp", System.currentTimeMillis()
        ));
    }
}