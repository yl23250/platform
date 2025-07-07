package com.biobt.common.core.domain;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {
    
    /**
     * 页码（从1开始）
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 1000, message = "每页大小不能超过1000")
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection = "DESC";
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 开始时间（用于时间范围查询）
     */
    private String startTime;
    
    /**
     * 结束时间（用于时间范围查询）
     */
    private String endTime;
    
    /**
     * 获取偏移量
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
    
    /**
     * 获取排序SQL
     */
    public String getOrderBySql() {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return "";
        }
        
        // 防止SQL注入，只允许字母、数字、下划线
        String cleanOrderBy = orderBy.replaceAll("[^a-zA-Z0-9_]", "");
        String cleanDirection = "DESC".equalsIgnoreCase(orderDirection) ? "DESC" : "ASC";
        
        return cleanOrderBy + " " + cleanDirection;
    }
    
    /**
     * 验证排序字段是否安全
     */
    public boolean isValidOrderBy() {
        if (orderBy == null) {
            return true;
        }
        // 只允许字母、数字、下划线
        return orderBy.matches("^[a-zA-Z0-9_]+$");
    }
}