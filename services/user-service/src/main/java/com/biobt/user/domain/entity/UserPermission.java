package com.biobt.user.domain.entity;

import com.biobt.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户权限关联实体（用户直接权限授权）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_permission", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_permission_id", columnList = "permission_id"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id"),
    @Index(name = "uk_user_permission", columnList = "user_id,permission_id", unique = true)
})
public class UserPermission extends BaseEntity {
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * 权限ID
     */
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
    
    /**
     * 授权类型（1-授予，2-拒绝）
     */
    @Column(name = "grant_type", nullable = false)
    private Integer grantType = 1;
    
    /**
     * 授权人ID
     */
    @Column(name = "granted_by")
    private Long grantedBy;
    
    /**
     * 授权时间
     */
    @Column(name = "granted_time")
    private LocalDateTime grantedTime;
    
    /**
     * 生效时间
     */
    @Column(name = "effective_time")
    private LocalDateTime effectiveTime;
    
    /**
     * 失效时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 状态（1-正常，0-禁用）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    /**
     * 数据权限配置（JSON格式）
     */
    @Column(name = "data_permission", columnDefinition = "TEXT")
    private String dataPermission;
    
    /**
     * 字段权限配置（JSON格式）
     */
    @Column(name = "field_permission", columnDefinition = "TEXT")
    private String fieldPermission;
    
    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
    
    /**
     * 扩展信息（JSON格式）
     */
    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo;
    
    /**
     * 关联的用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    /**
     * 关联的权限
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;
    
    /**
     * 判断授权是否有效
     */
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return status == 1 && 
               (effectiveTime == null || effectiveTime.isBefore(now)) &&
               (expireTime == null || expireTime.isAfter(now));
    }
    
    /**
     * 判断是否为授予权限
     */
    public boolean isGrant() {
        return grantType == 1;
    }
    
    /**
     * 判断是否为拒绝权限
     */
    public boolean isDeny() {
        return grantType == 2;
    }
}