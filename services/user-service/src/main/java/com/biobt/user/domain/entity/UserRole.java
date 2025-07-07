package com.biobt.user.domain.entity;

import com.biobt.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_role", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_role_id", columnList = "role_id"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id"),
    @Index(name = "uk_user_role", columnList = "user_id,role_id", unique = true)
})
public class UserRole extends BaseEntity {
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * 角色ID
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    /**
     * 授权类型（1-直接授权，2-继承授权，3-临时授权）
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
     * 数据权限范围覆盖（JSON格式）
     */
    @Column(name = "data_scope_override", columnDefinition = "TEXT")
    private String dataScopeOverride;
    
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
     * 关联的角色
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
    
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
     * 判断是否为直接授权
     */
    public boolean isDirectGrant() {
        return grantType == 1;
    }
    
    /**
     * 判断是否为继承授权
     */
    public boolean isInheritGrant() {
        return grantType == 2;
    }
    
    /**
     * 判断是否为临时授权
     */
    public boolean isTemporaryGrant() {
        return grantType == 3;
    }
}