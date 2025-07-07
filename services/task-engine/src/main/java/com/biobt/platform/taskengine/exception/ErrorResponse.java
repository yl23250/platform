package com.biobt.platform.taskengine.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 错误响应
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * HTTP状态码
     */
    private Integer status;
    
    /**
     * 错误类型
     */
    private String error;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 错误代码
     */
    private String code;
    
    /**
     * 请求路径
     */
    private String path;
    
    /**
     * 错误详情
     */
    private Map<String, Object> details;
    
    /**
     * 跟踪ID
     */
    private String traceId;
    
    /**
     * 建议操作
     */
    private String suggestion;
    
    /**
     * 帮助链接
     */
    private String helpUrl;
    
    /**
     * 创建简单错误响应
     */
    public static ErrorResponse simple(String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }
    
    /**
     * 创建带状态码的错误响应
     */
    public static ErrorResponse withStatus(int status, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .build();
    }
    
    /**
     * 创建带错误代码的错误响应
     */
    public static ErrorResponse withCode(String code, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(code)
                .message(message)
                .build();
    }
    
    /**
     * 创建完整的错误响应
     */
    public static ErrorResponse full(int status, String error, String code, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .code(code)
                .message(message)
                .path(path)
                .build();
    }
    
    /**
     * 添加详情信息
     */
    public ErrorResponse addDetail(String key, Object value) {
        if (this.details == null) {
            this.details = new java.util.HashMap<>();
        }
        this.details.put(key, value);
        return this;
    }
    
    /**
     * 设置跟踪ID
     */
    public ErrorResponse withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    /**
     * 设置建议操作
     */
    public ErrorResponse withSuggestion(String suggestion) {
        this.suggestion = suggestion;
        return this;
    }
    
    /**
     * 设置帮助链接
     */
    public ErrorResponse withHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
        return this;
    }
}