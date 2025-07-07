package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 角色请求DTO
 */
@Data
@Schema(description = "角色请求")
public class RoleRequest {
    
    @Schema(description = "角色编码", example = "ADMIN")
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;
    
    @Schema(description = "角色名称", example = "管理员")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100个字符")
    private String roleName;
    
    @Schema(description = "角色描述", example = "系统管理员角色")
    @Size(max = 500, message = "角色描述长度不能超过500个字符")
    private String description;
    
    @Schema(description = "角色类型", example = "SYSTEM", allowableValues = {"SYSTEM", "BUSINESS", "CUSTOM"})
    @NotBlank(message = "角色类型不能为空")
    private String roleType;
    
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "是否默认角色", example = "false")
    private Boolean isDefault;
    
    @Schema(description = "角色级别", example = "1")
    private Integer roleLevel;
    
    @Schema(description = "数据范围", example = "ALL", allowableValues = {"ALL", "DEPT", "DEPT_AND_SUB", "SELF", "CUSTOM"})
    private String dataScope;
    
    @Schema(description = "数据范围部门ID列表")
    private List<Long> dataScopeDepts;
    
    @Schema(description = "生效时间")
    private LocalDateTime effectiveTime;
    
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
    
    @Schema(description = "权限ID列表")
    private List<Long> permissionIds;
}