package com.biobt.user.domain.dto;

import com.biobt.user.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 角色响应DTO
 */
@Data
@Schema(description = "角色响应")
public class RoleResponse {
    
    @Schema(description = "角色ID", example = "1")
    private Long id;
    
    @Schema(description = "角色编码", example = "ADMIN")
    private String roleCode;
    
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;
    
    @Schema(description = "角色描述", example = "系统管理员角色")
    private String description;
    
    @Schema(description = "角色类型", example = "SYSTEM")
    private String roleType;
    
    @Schema(description = "状态", example = "1")
    private Integer status;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "是否默认角色", example = "false")
    private Boolean isDefault;
    
    @Schema(description = "角色级别", example = "1")
    private Integer roleLevel;
    
    @Schema(description = "数据范围", example = "ALL")
    private String dataScope;
    
    @Schema(description = "数据范围部门ID列表")
    private List<Long> dataScopeDepts;
    
    @Schema(description = "生效时间")
    private LocalDateTime effectiveTime;
    
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "创建人ID")
    private Long createBy;
    
    @Schema(description = "更新人ID")
    private Long updateBy;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "是否为系统角色")
    private Boolean isSystem;
    
    @Schema(description = "是否为业务角色")
    private Boolean isBusiness;
    
    @Schema(description = "是否为自定义角色")
    private Boolean isCustom;
    
    @Schema(description = "用户数量")
    private Long userCount;
    
    @Schema(description = "权限列表")
    private List<PermissionResponse> permissions;
    
    /**
     * 从Role实体转换为RoleResponse
     */
    public static RoleResponse from(Role role) {
        if (role == null) {
            return null;
        }
        
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setRoleCode(role.getRoleCode());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setRoleType(role.getRoleType());
        response.setStatus(role.getStatus());
        response.setSortOrder(role.getSortOrder());
        response.setIsDefault(role.getIsDefault());
        response.setRoleLevel(role.getRoleLevel());
        response.setDataScope(role.getDataScope());
        response.setDataScopeDepts(role.getDataScopeDepts());
        response.setEffectiveTime(role.getEffectiveTime());
        response.setExpireTime(role.getExpireTime());
        response.setExtraInfo(role.getExtraInfo());
        response.setCreateTime(role.getCreateTime());
        response.setUpdateTime(role.getUpdateTime());
        response.setCreateBy(role.getCreateBy());
        response.setUpdateBy(role.getUpdateBy());
        
        // 设置计算属性
        response.setEnabled(role.isEnabled());
        response.setIsSystem(role.isSystem());
        response.setIsBusiness(role.isBusiness());
        response.setIsCustom(role.isCustom());
        
        // 设置权限列表（如果已加载）
        if (role.getPermissions() != null) {
            response.setPermissions(PermissionResponse.fromList(role.getPermissions().stream().toList()));
        }
        
        // 设置用户数量（如果已加载）
        if (role.getUsers() != null) {
            response.setUserCount((long) role.getUsers().size());
        }
        
        return response;
    }
    
    /**
     * 从Role实体列表转换为RoleResponse列表
     */
    public static List<RoleResponse> fromList(List<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(RoleResponse::from)
                .toList();
    }
}