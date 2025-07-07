package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * 权限请求DTO
 */
@Data
@Schema(description = "权限请求")
public class PermissionRequest {
    
    @Schema(description = "权限编码", example = "user:create")
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    private String permissionCode;
    
    @Schema(description = "权限名称", example = "创建用户")
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称长度不能超过100个字符")
    private String permissionName;
    
    @Schema(description = "权限描述", example = "允许创建新用户")
    @Size(max = 500, message = "权限描述长度不能超过500个字符")
    private String description;
    
    @Schema(description = "父级权限ID", example = "1")
    private Long parentId;
    
    @Schema(description = "资源类型", example = "MENU", allowableValues = {"MENU", "BUTTON", "API", "DATA", "FIELD"})
    @NotBlank(message = "资源类型不能为空")
    private String resourceType;
    
    @Schema(description = "资源路径", example = "/api/users")
    @Size(max = 200, message = "资源路径长度不能超过200个字符")
    private String resourcePath;
    
    @Schema(description = "HTTP方法", example = "POST", allowableValues = {"GET", "POST", "PUT", "DELETE", "PATCH"})
    private String httpMethod;
    
    @Schema(description = "图标", example = "user-plus")
    @Size(max = 50, message = "图标长度不能超过50个字符")
    private String icon;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    @Schema(description = "是否可见", example = "true")
    private Boolean isVisible;
    
    @Schema(description = "是否缓存", example = "true")
    private Boolean isCache;
    
    @Schema(description = "组件路径", example = "@/views/user/index")
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
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
}