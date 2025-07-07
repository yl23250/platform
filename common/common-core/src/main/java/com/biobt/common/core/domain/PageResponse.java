package com.biobt.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应结果
 * @param <T> 数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页码
     */
    private Long pageNum;
    
    /**
     * 每页大小
     */
    private Long pageSize;
    
    /**
     * 总页数
     */
    private Long totalPages;
    
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    
    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;
    
    /**
     * 是否为第一页
     */
    private Boolean isFirst;
    
    /**
     * 是否为最后一页
     */
    private Boolean isLast;
    
    /**
     * 构造分页响应
     */
    public static <T> PageResponse<T> of(List<T> records, Long total, Long pageNum, Long pageSize) {
        PageResponse<T> response = new PageResponse<>();
        response.setRecords(records);
        response.setTotal(total);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        
        // 计算总页数
        Long totalPages = (long) Math.ceil((double) total / pageSize);
        response.setTotalPages(totalPages);
        
        // 计算分页状态
        response.setHasNext(pageNum < totalPages);
        response.setHasPrevious(pageNum > 1);
        response.setIsFirst(pageNum == 1);
        response.setIsLast(pageNum >= totalPages);
        
        return response;
    }
    
    /**
     * 空分页响应
     */
    public static <T> PageResponse<T> empty(Long pageNum, Long pageSize) {
        return of(List.of(), 0L, pageNum, pageSize);
    }
    
    /**
     * 获取开始记录号
     */
    public Long getStartRow() {
        return (pageNum - 1) * pageSize + 1;
    }
    
    /**
     * 获取结束记录号
     */
    public Long getEndRow() {
        return Math.min(pageNum * pageSize, total.intValue());
    }
    
    /**
     * 是否有数据
     */
    public boolean hasData() {
        return records != null && !records.isEmpty();
    }
    
    /**
     * 获取当前页数据数量
     */
    public int getCurrentPageSize() {
        return records != null ? records.size() : 0;
    }
}