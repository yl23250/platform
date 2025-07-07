package com.biobt.common.core.exception;

/**
 * 业务异常
 * 用于处理业务逻辑中的异常情况
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 错误码
     */
    private Integer code;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 错误详情
     */
    private Object details;
    
    public BusinessException() {
        super();
    }
    
    public BusinessException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
    }
    
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    public BusinessException(Integer code, String message, Object details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getDetails() {
        return details;
    }
    
    public void setDetails(Object details) {
        this.details = details;
    }
    
    /**
     * 参数错误异常
     */
    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }
    
    /**
     * 未授权异常
     */
    public static BusinessException unauthorized(String message) {
        return new BusinessException(401, message);
    }
    
    /**
     * 禁止访问异常
     */
    public static BusinessException forbidden(String message) {
        return new BusinessException(403, message);
    }
    
    /**
     * 资源不存在异常
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }
    
    /**
     * 方法不允许异常
     */
    public static BusinessException methodNotAllowed(String message) {
        return new BusinessException(405, message);
    }
    
    /**
     * 冲突异常
     */
    public static BusinessException conflict(String message) {
        return new BusinessException(409, message);
    }
    
    /**
     * 请求过于频繁异常
     */
    public static BusinessException tooManyRequests(String message) {
        return new BusinessException(429, message);
    }
    
    /**
     * 内部服务器错误异常
     */
    public static BusinessException internalServerError(String message) {
        return new BusinessException(500, message);
    }
    
    /**
     * 服务不可用异常
     */
    public static BusinessException serviceUnavailable(String message) {
        return new BusinessException(503, message);
    }
    
    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", details=" + details +
                '}';
    }
}