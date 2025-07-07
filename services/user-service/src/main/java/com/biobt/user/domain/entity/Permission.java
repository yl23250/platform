package com.biobt.user.domain.entity;

import com.biobt.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * 权限实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_permission", indexes = {
    @Index(name = "idx_permission_code", columnList = "permission_code"),
    @Index(name = "idx_parent_id", columnList = "parent_id"),
    @Index(name = "idx_resource_type", columnList = "resource_type"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
public class Permission extends BaseEntity {
    
    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    @Column(name = "permission_code", nullable = false, unique = true, length = 100)
    private String permissionCode;
    
    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称长度不能超过100个字符")
    @Column(name = "permission_name", nullable = false, length = 100)
    private String permissionName;
    
    /**
     * 权限描述
     */
    @Size(max = 500, message = "权限描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 父权限ID
     */
    @Column(name = "parent_id")
    private Long parentId;
    
    /**
     * 资源类型（1-菜单，2-按钮，3-接口，4-数据，5-字段）
     */
    @Column(name = "resource_type", nullable = false)
    private Integer resourceType;
    
    /**
     * 资源路径（URL路径或方法路径）
     */
    @Size(max = 500, message = "资源路径长度不能超过500个字符")
    @Column(name = "resource_path", length = 500)
    private String resourcePath;
    
    /**
     * HTTP方法（GET,POST,PUT,DELETE等）
     */
    @Size(max = 20, message = "HTTP方法长度不能超过20个字符")
    @Column(name = "http_method", length = 20)
    private String httpMethod;
    
    /**
     * 菜单图标
     */
    @Size(max = 100, message = "菜单图标长度不能超过100个字符")
    @Column(name = "icon", length = 100)
    private String icon;
    
    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /**
     * 权限状态（1-正常，0-禁用）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    /**
     * 是否显示（1-显示，0-隐藏）
     */
    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = true;
    
    /**
     * 是否缓存（1-缓存，0-不缓存）
     */
    @Column(name = "is_cache", nullable = false)
    private Boolean isCache = false;
    
    /**
     * 组件路径
     */
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    @Column(name = "component", length = 200)
    private String component;
    
    /**
     * 路由参数
     */
    @Size(max = 500, message = "路由参数长度不能超过500个字符")
    @Column(name = "route_params", length = 500)
    private String routeParams;
    
    /**
     * 权限级别（数字越小级别越高）
     */
    @Column(name = "permission_level")
    private Integer permissionLevel = 99;
    
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
     * 扩展信息（JSON格式）
     */
    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo;
    
    /**
     * 权限关联的角色
     */
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;
    
    /**
     * 子权限
     */
    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    private Set<Permission> children;
    
    /**
     * 判断权限是否可用
     */
    public boolean isEnabled() {
        return status == 1;
    }
    
    /**
     * 判断是否为菜单权限
     */
    public boolean isMenu() {
        return resourceType == 1;
    }
    
    /**
     * 判断是否为按钮权限
     */
    public boolean isButton() {
        return resourceType == 2;
    }
    
    /**
     * 判断是否为接口权限
     */
    public boolean isApi() {
        return resourceType == 3;
    }
    
    /**
     * 判断是否为数据权限
     */
    public boolean isData() {
        return resourceType == 4;
    }
    
    /**
     * 判断是否为字段权限
     */
    public boolean isField() {
        return resourceType == 5;
    }
    
    /**
     * 判断是否为根权限
     */
    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }
}