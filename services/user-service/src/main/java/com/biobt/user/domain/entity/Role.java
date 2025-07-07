package com.biobt.user.domain.entity;

import com.biobt.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 角色实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role", indexes = {
    @Index(name = "idx_role_code", columnList = "role_code"),
    @Index(name = "idx_role_name", columnList = "role_name"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
public class Role extends BaseEntity {
    
    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private String roleCode;
    
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100个字符")
    @Column(name = "role_name", nullable = false, length = 100)
    private String roleName;
    
    /**
     * 角色描述
     */
    @Size(max = 500, message = "角色描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 角色类型（1-系统角色，2-业务角色，3-自定义角色）
     */
    @Column(name = "role_type", nullable = false)
    private Integer roleType = 2;
    
    /**
     * 角色状态（1-正常，0-禁用）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /**
     * 是否为默认角色
     */
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    /**
     * 角色级别（数字越小级别越高）
     */
    @Column(name = "role_level")
    private Integer roleLevel = 99;
    
    /**
     * 数据权限范围（1-全部数据，2-本部门及以下，3-本部门，4-仅本人，5-自定义）
     */
    @Column(name = "data_scope")
    private Integer dataScope = 4;
    
    /**
     * 数据权限部门ID集合（JSON格式）
     */
    @Column(name = "data_scope_depts", columnDefinition = "TEXT")
    private String dataScopeDepts;
    
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
     * 扩展信息（JSON格式）
     */
    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo;
    
    /**
     * 角色关联的用户
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;
    
    /**
     * 角色关联的权限
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sys_role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
    
    /**
     * 判断角色是否可用
     */
    public boolean isEnabled() {
        LocalDateTime now = LocalDateTime.now();
        return status == 1 && 
               (effectiveTime == null || effectiveTime.isBefore(now)) &&
               (expireTime == null || expireTime.isAfter(now));
    }
    
    /**
     * 判断是否为系统角色
     */
    public boolean isSystemRole() {
        return roleType == 1;
    }
    
    /**
     * 判断是否为业务角色
     */
    public boolean isBusinessRole() {
        return roleType == 2;
    }
    
    /**
     * 判断是否为自定义角色
     */
    public boolean isCustomRole() {
        return roleType == 3;
    }
}