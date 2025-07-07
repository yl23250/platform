package com.biobt.user.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.user.annotation.RequiresPermission;
import com.biobt.user.annotation.RequiresRole;
import com.biobt.user.domain.entity.Permission;
import com.biobt.user.domain.dto.PermissionRequest;
import com.biobt.user.domain.dto.PermissionResponse;
import com.biobt.user.service.PermissionService;
import com.biobt.common.core.domain.ApiResponse;
import com.biobt.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 权限控制器
 * 提供权限管理的REST API接口
 */
@Tag(name = "权限管理", description = "权限管理相关接口")
@RestController
@RequestMapping("/api/permissions")
@Validated
public class PermissionController extends BaseController<Permission, PermissionResponse> {

    private final PermissionService permissionService;

    public PermissionController(PermissionService service) {
        super(service, entity -> {
            PermissionResponse vo = new PermissionResponse();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
        this.permissionService = service;
    }

    /**
     * 创建权限
     */
    @Operation(summary = "创建权限", description = "创建新的权限")
    @PostMapping
    @RequiresPermission("permission:create")
    public ApiResponse<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        Permission permission = service.createPermission(request);
        return ApiResponse.success(PermissionResponse.from(permission));
    }
    
    /**
     * 更新权限
     */
    @Operation(summary = "更新权限", description = "更新指定权限信息")
    @PutMapping("/{id}")
    @RequiresPermission("permission:update")
    public ApiResponse<PermissionResponse> updatePermission(
            @Parameter(description = "权限ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody PermissionRequest request) {
        Permission permission = permissionService.updatePermission(id, request);
        return ApiResponse.success(PermissionResponse.from(permission));
    }
    
    /**
     * 删除权限
     */
    @Operation(summary = "删除权限", description = "删除指定权限")
    @DeleteMapping("/{id}")
    @RequiresPermission("permission:delete")
    public ApiResponse<Void> deletePermission(
            @Parameter(description = "权限ID") @PathVariable @NotNull Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.success();
    }
    
    /**
     * 批量删除权限
     */
    @Operation(summary = "批量删除权限", description = "批量删除指定权限")
    @DeleteMapping("/batch")
    @RequiresPermission("permission:delete")
    public ApiResponse<Void> deletePermissions(
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> ids) {
        permissionService.deletePermissions(ids);
        return ApiResponse.success();
    }
    
    /**
     * 获取权限详情
     */
    @Operation(summary = "获取权限详情", description = "根据ID获取权限详情")
    @GetMapping("/{id}")
    @RequiresPermission("permission:view")
    public ApiResponse<PermissionResponse> getPermission(
            @Parameter(description = "权限ID") @PathVariable @NotNull Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return ApiResponse.success(PermissionResponse.from(permission));
    }
    
    /**
     * 根据编码获取权限
     */
    @Operation(summary = "根据编码获取权限", description = "根据权限编码获取权限详情")
    @GetMapping("/code/{code}")
    @RequiresPermission("permission:view")
    public ApiResponse<PermissionResponse> getPermissionByCode(
            @Parameter(description = "权限编码") @PathVariable @NotEmpty String code) {
        Permission permission = permissionService.getPermissionByCode(code);
        return ApiResponse.success(PermissionResponse.from(permission));
    }
    
    /**
     * 分页查询权限
     */
    @Operation(summary = "分页查询权限", description = "分页查询权限列表")
    @GetMapping
    @RequiresPermission("permission:list")
    public ApiResponse<PageResponse<PermissionResponse>> getPermissions(
            @Parameter(description = "权限名称") @RequestParam(required = false) String permissionName,
            @Parameter(description = "权限编码") @RequestParam(required = false) String permissionCode,
            @Parameter(description = "资源类型") @RequestParam(required = false) String resourceType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "父级ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        PermissionRequest query = new PermissionRequest();
        query.setPermissionName(permissionName);
        query.setPermissionCode(permissionCode);
        query.setResourceType(resourceType);
        query.setStatus(status);
        query.setParentId(parentId);
        
        IPage<Permission> pageResult = permissionService.getPermissionPage(query, pageNum, pageSize);
        List<PermissionResponse> responses = pageResult.getRecords().stream()
                .map(PermissionResponse::from)
                .toList();
        PageResponse<PermissionResponse> responsePageResult = PageResponse.of(
                responses, pageResult.getTotal(),pageResult.getCurrent(), pageResult.getSize());
        return ApiResponse.success(responsePageResult);
    }
    
    /**
     * 获取权限树
     */
    @Operation(summary = "获取权限树", description = "获取权限树形结构")
    @GetMapping("/tree")
    @RequiresPermission("permission:tree")
    public ApiResponse<List<PermissionResponse>> getPermissionTree(
            @Parameter(description = "资源类型") @RequestParam(required = false) String resourceType,
            @Parameter(description = "是否只显示启用的") @RequestParam(defaultValue = "false") boolean onlyEnabled) {
        List<Permission> permissions = permissionService.getPermissionTree(resourceType, onlyEnabled);
        List<PermissionResponse> responses = permissions.stream()
                .map(PermissionResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取用户权限列表
     */
    @Operation(summary = "获取用户权限列表", description = "获取指定用户的权限列表")
    @GetMapping("/user/{userId}")
    @RequiresPermission("permission:user")
    public ApiResponse<List<PermissionResponse>> getUserPermissions(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "资源类型") @RequestParam(required = false) String resourceType) {
        List<Permission> permissions = permissionService.getUserPermissions(userId, resourceType);
        List<PermissionResponse> responses = permissions.stream()
                .map(PermissionResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取用户权限编码列表
     */
    @Operation(summary = "获取用户权限编码列表", description = "获取指定用户的权限编码列表")
    @GetMapping("/user/{userId}/codes")
    @RequiresPermission("permission:user")
    public ApiResponse<List<String>> getUserPermissionCodes(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "资源类型") @RequestParam(required = false) String resourceType) {
        List<String> codes = permissionService.getUserPermissionCodes(userId, resourceType);
        return ApiResponse.success(codes);
    }
    
    /**
     * 获取角色权限列表
     */
    @Operation(summary = "获取角色权限列表", description = "获取指定角色的权限列表")
    @GetMapping("/role/{roleId}")
    @RequiresPermission("permission:role")
    public ApiResponse<List<PermissionResponse>> getRolePermissions(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long roleId,
            @Parameter(description = "资源类型") @RequestParam(required = false) String resourceType) {
        List<Permission> permissions = permissionService.getRolePermissions(roleId, resourceType);
        List<PermissionResponse> responses = permissions.stream()
                .map(PermissionResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 为角色分配权限
     */
    @Operation(summary = "为角色分配权限", description = "为指定角色分配权限")
    @PostMapping("/role/{roleId}/assign")
    @RequiresPermission("permission:assign")
    public ApiResponse<Void> assignPermissionsToRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> permissionIds) {
        permissionService.assignPermissionsToRole(roleId, permissionIds);
        return ApiResponse.success();
    }
    
    /**
     * 移除角色权限
     */
    @Operation(summary = "移除角色权限", description = "移除指定角色的权限")
    @DeleteMapping("/role/{roleId}/remove")
    @RequiresPermission("permission:assign")
    public ApiResponse<Void> removePermissionsFromRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> permissionIds) {
        permissionService.removePermissionsFromRole(roleId, permissionIds);
        return ApiResponse.success();
    }
    
    /**
     * 为用户分配权限
     */
    @Operation(summary = "为用户分配权限", description = "为指定用户直接分配权限")
    @PostMapping("/user/{userId}/assign")
    @RequiresPermission("permission:assign")
    public ApiResponse<Void> assignPermissionsToUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> permissionIds) {
        permissionService.assignPermissionsToUser(userId, permissionIds);
        return ApiResponse.success();
    }
    
    /**
     * 移除用户权限
     */
    @Operation(summary = "移除用户权限", description = "移除指定用户的直接权限")
    @DeleteMapping("/user/{userId}/remove")
    @RequiresPermission("permission:assign")
    public ApiResponse<Void> removePermissionsFromUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> permissionIds) {
        permissionService.removePermissionsFromUser(userId, permissionIds);
        return ApiResponse.success();
    }
    
    /**
     * 检查用户权限
     */
    @Operation(summary = "检查用户权限", description = "检查用户是否拥有指定权限")
    @PostMapping("/user/{userId}/check")
    @RequiresPermission("permission:check")
    public ApiResponse<Map<String, Boolean>> checkUserPermissions(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "权限编码列表") @RequestBody @NotEmpty List<String> permissionCodes) {
        Map<String, Boolean> result = permissionService.checkUserPermissions(userId, permissionCodes);
        return ApiResponse.success(result);
    }
    
    /**
     * 启用权限
     */
    @Operation(summary = "启用权限", description = "启用指定权限")
    @PutMapping("/{id}/enable")
    @RequiresPermission("permission:update")
    public ApiResponse<Void> enablePermission(
            @Parameter(description = "权限ID") @PathVariable @NotNull Long id) {
        permissionService.enablePermission(id);
        return ApiResponse.success();
    }
    
    /**
     * 禁用权限
     */
    @Operation(summary = "禁用权限", description = "禁用指定权限")
    @PutMapping("/{id}/disable")
    @RequiresPermission("permission:update")
    public ApiResponse<Void> disablePermission(
            @Parameter(description = "权限ID") @PathVariable @NotNull Long id) {
        permissionService.disablePermission(id);
        return ApiResponse.success();
    }
    
    /**
     * 批量启用权限
     */
    @Operation(summary = "批量启用权限", description = "批量启用指定权限")
    @PutMapping("/batch/enable")
    @RequiresPermission("permission:update")
    public ApiResponse<Void> enablePermissions(
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> ids) {
        permissionService.enablePermissions(ids);
        return ApiResponse.success();
    }
    
    /**
     * 批量禁用权限
     */
    @Operation(summary = "批量禁用权限", description = "批量禁用指定权限")
    @PutMapping("/batch/disable")
    @RequiresPermission("permission:update")
    public ApiResponse<Void> disablePermissions(
            @Parameter(description = "权限ID列表") @RequestBody @NotEmpty List<Long> ids) {
        permissionService.disablePermissions(ids);
        return ApiResponse.success();
    }
    
    /**
     * 刷新权限缓存
     */
    @Operation(summary = "刷新权限缓存", description = "刷新权限相关缓存")
    @PostMapping("/cache/refresh")
    @RequiresRole("ADMIN")
    public ApiResponse<Void> refreshPermissionCache() {
        permissionService.refreshCache();
        return ApiResponse.success();
    }
    
    /**
     * 清空权限缓存
     */
    @Operation(summary = "清空权限缓存", description = "清空权限相关缓存")
    @DeleteMapping("/cache/clear")
    @RequiresRole("ADMIN")
    public ApiResponse<Void> clearPermissionCache() {
        permissionService.clearCache();
        return ApiResponse.success();
    }
}