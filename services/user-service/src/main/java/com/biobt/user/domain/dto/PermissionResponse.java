package com.biobt.user.domain.dto;

import com.biobt.user.domain.entity.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 权限响应DTO
 */
@Data
@Schema(description = "权限响应")
public class PermissionResponse {
    
    @Schema(description = "权限ID", example = "1")
    private Long id;
    
    @Schema(description = "权限编码", example = "user:create")
    private String permissionCode;
    
    @Schema(description = "权限名称", example = "创建用户")
    private String permissionName;
    
    @Schema(description = "权限描述", example = "允许创建新用户")
    private String description;
    
    @Schema(description = "父级权限ID", example = "1")
    private Long parentId;
    
    @Schema(description = "资源类型", example = "MENU")
    private String resourceType;
    
    @Schema(description = "资源路径", example = "/api/users")
    private String resourcePath;
    
    @Schema(description = "HTTP方法", example = "POST")
    private String httpMethod;
    
    @Schema(description = "图标", example = "user-plus")
    private String icon;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "状态", example = "1")
    private Integer status;
    
    @Schema(description = "是否可见", example = "true")
    private Boolean isVisible;
    
    @Schema(description = "是否缓存", example = "true")
    private Boolean isCache;
    
    @Schema(description = "组件路径", example = "@/views/user/index")
    private String component;
    
    @Schema(description = "路由参数", example = "{\"id\": \"123\"}")
    private String routeParams;
    
    @Schema(description = "权限级别", example = "1")
    private Integer permissionLevel;
    
    @Schema(description = "数据权限配置", example = "{\"scope\": \"DEPT\"}")
    private String dataPermission;
    
    @Schema(description = "字段权限配置", example = "{\"fields\": [\"name\", \"email\"]}")
    private String fieldPermission;
    
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
    
    @Schema(description = "子权限列表")
    private List<PermissionResponse> children;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "是否为菜单类型")
    private Boolean isMenu;
    
    @Schema(description = "是否为按钮类型")
    private Boolean isButton;
    
    @Schema(description = "是否为API类型")
    private Boolean isApi;
    
    @Schema(description = "是否为数据权限类型")
    private Boolean isData;
    
    @Schema(description = "是否为字段权限类型")
    private Boolean isField;
    
    /**
     * 从Permission实体转换为PermissionResponse
     */
    public static PermissionResponse from(Permission permission) {
        if (permission == null) {
            return null;
        }
        
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setPermissionCode(permission.getPermissionCode());
        response.setPermissionName(permission.getPermissionName());
        response.setDescription(permission.getDescription());
        response.setParentId(permission.getParentId());
        response.setResourceType(permission.getResourceType());
        response.setResourcePath(permission.getResourcePath());
        response.setHttpMethod(permission.getHttpMethod());
        response.setIcon(permission.getIcon());
        response.setSortOrder(permission.getSortOrder());
        response.setStatus(permission.getStatus());
        response.setIsVisible(permission.getIsVisible());
        response.setIsCache(permission.getIsCache());
        response.setComponent(permission.getComponent());
        response.setRouteParams(permission.getRouteParams());
        response.setPermissionLevel(permission.getPermissionLevel());
        response.setDataPermission(permission.getDataPermission());
        response.setFieldPermission(permission.getFieldPermission());
        response.setExtraInfo(permission.getExtraInfo());
        response.setCreateTime(permission.getCreateTime());
        response.setUpdateTime(permission.getUpdateTime());
        response.setCreateBy(permission.getCreateBy());
        response.setUpdateBy(permission.getUpdateBy());
        
        // 设置计算属性
        response.setEnabled(permission.isEnabled());
        response.setIsMenu(permission.isMenu());
        response.setIsButton(permission.isButton());
        response.setIsApi(permission.isApi());
        response.setIsData(permission.isData());
        response.setIsField(permission.isField());
        
        return response;
    }
    
    /**
     * 从Permission实体列表转换为PermissionResponse列表
     */
    public static List<PermissionResponse> fromList(List<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(PermissionResponse::from)
                .toList();
    }
}