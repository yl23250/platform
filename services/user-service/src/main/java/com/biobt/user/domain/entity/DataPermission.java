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

/**
 * 数据权限实体（行权限、列权限）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_data_permission", indexes = {
    @Index(name = "idx_resource_type", columnList = "resource_type"),
    @Index(name = "idx_resource_id", columnList = "resource_id"),
    @Index(name = "idx_subject_type", columnList = "subject_type"),
    @Index(name = "idx_subject_id", columnList = "subject_id"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
public class DataPermission extends BaseEntity {
    
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
     * 资源类型（1-表，2-视图，3-接口，4-文件）
     */
    @Column(name = "resource_type", nullable = false)
    private Integer resourceType;
    
    /**
     * 资源标识（表名、视图名、接口路径等）
     */
    @NotBlank(message = "资源标识不能为空")
    @Size(max = 200, message = "资源标识长度不能超过200个字符")
    @Column(name = "resource_id", nullable = false, length = 200)
    private String resourceId;
    
    /**
     * 权限主体类型（1-用户，2-角色，3-部门，4-岗位）
     */
    @Column(name = "subject_type", nullable = false)
    private Integer subjectType;
    
    /**
     * 权限主体ID
     */
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
    
    /**
     * 权限类型（1-行权限，2-列权限，3-行列权限）
     */
    @Column(name = "permission_type", nullable = false)
    private Integer permissionType;
    
    /**
     * 操作类型（1-查询，2-新增，4-修改，8-删除，可组合）
     */
    @Column(name = "operation_type", nullable = false)
    private Integer operationType;
    
    /**
     * 行权限条件（SQL WHERE条件）
     */
    @Column(name = "row_condition", columnDefinition = "TEXT")
    private String rowCondition;
    
    /**
     * 列权限配置（JSON格式，包含允许/拒绝的列）
     */
    @Column(name = "column_permission", columnDefinition = "TEXT")
    private String columnPermission;
    
    /**
     * 权限策略（1-允许，2-拒绝）
     */
    @Column(name = "permission_policy", nullable = false)
    private Integer permissionPolicy = 1;
    
    /**
     * 优先级（数字越小优先级越高）
     */
    @Column(name = "priority", nullable = false)
    private Integer priority = 100;
    
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
     * 判断权限是否有效
     */
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return status == 1 && 
               (effectiveTime == null || effectiveTime.isBefore(now)) &&
               (expireTime == null || expireTime.isAfter(now));
    }
    
    /**
     * 判断是否为表资源
     */
    public boolean isTableResource() {
        return resourceType == 1;
    }
    
    /**
     * 判断是否为视图资源
     */
    public boolean isViewResource() {
        return resourceType == 2;
    }
    
    /**
     * 判断是否为接口资源
     */
    public boolean isApiResource() {
        return resourceType == 3;
    }
    
    /**
     * 判断是否为文件资源
     */
    public boolean isFileResource() {
        return resourceType == 4;
    }
    
    /**
     * 判断主体是否为用户
     */
    public boolean isUserSubject() {
        return subjectType == 1;
    }
    
    /**
     * 判断主体是否为角色
     */
    public boolean isRoleSubject() {
        return subjectType == 2;
    }
    
    /**
     * 判断主体是否为部门
     */
    public boolean isDeptSubject() {
        return subjectType == 3;
    }
    
    /**
     * 判断主体是否为岗位
     */
    public boolean isPositionSubject() {
        return subjectType == 4;
    }
    
    /**
     * 判断是否为行权限
     */
    public boolean isRowPermission() {
        return permissionType == 1 || permissionType == 3;
    }
    
    /**
     * 判断是否为列权限
     */
    public boolean isColumnPermission() {
        return permissionType == 2 || permissionType == 3;
    }
    
    /**
     * 判断是否包含查询操作
     */
    public boolean hasSelectOperation() {
        return (operationType & 1) == 1;
    }
    
    /**
     * 判断是否包含新增操作
     */
    public boolean hasInsertOperation() {
        return (operationType & 2) == 2;
    }
    
    /**
     * 判断是否包含修改操作
     */
    public boolean hasUpdateOperation() {
        return (operationType & 4) == 4;
    }
    
    /**
     * 判断是否包含删除操作
     */
    public boolean hasDeleteOperation() {
        return (operationType & 8) == 8;
    }
    
    /**
     * 判断是否为允许策略
     */
    public boolean isAllowPolicy() {
        return permissionPolicy == 1;
    }
    
    /**
     * 判断是否为拒绝策略
     */
    public boolean isDenyPolicy() {
        return permissionPolicy == 2;
    }
}