package com.biobt.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含所有实体的公共字段
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 租户ID
     */
    @Column(name = "tenant_id")
    private Long tenantId;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;
    
    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;
    
    /**
     * 删除标记
     */
    @Column(name = "deleted")
    private Boolean deleted = false;
    
    /**
     * 版本号（乐观锁）
     */
    @Version
    @Column(name = "version")
    private Integer version = 0;
    
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
        
        // 从上下文获取租户ID
        if (tenantId == null) {
            tenantId = getCurrentTenantId();
        }
        
        // 从上下文获取当前用户
        if (createBy == null) {
            createBy = getCurrentUserId();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
        
        // 从上下文获取当前用户
        if (updateBy == null) {
            updateBy = getCurrentUserId();
        }
    }
    
    /**
     * 获取当前租户ID
     */
    private Long getCurrentTenantId() {
        // TODO: 从TenantContextHolder获取
        return null;
    }
    
    /**
     * 获取当前用户ID
     */
    private String getCurrentUserId() {
        // TODO: 从SecurityContextHolder获取
        return null;
    }
}