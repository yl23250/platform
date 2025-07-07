package com.biobt.user.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.user.annotation.RequiresPermission;
import com.biobt.user.annotation.RequiresRole;
import com.biobt.user.domain.entity.Role;
import com.biobt.user.domain.entity.User;
import com.biobt.user.domain.dto.RoleRequest;
import com.biobt.user.domain.dto.RoleResponse;
import com.biobt.user.domain.dto.UserResponse;
import com.biobt.user.service.RoleService;
import com.biobt.common.core.domain.ApiResponse;
import com.biobt.common.core.domain.PageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 * 提供角色管理的REST API接口
 */
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {
    
    private final RoleService roleService;
    
    /**
     * 创建角色
     */
    @Operation(summary = "创建角色", description = "创建新的角色")
    @PostMapping
    @RequiresPermission("role:create")
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        Role role = roleService.createRole(request);
        return ApiResponse.success(RoleResponse.from(role));
    }
    
    /**
     * 更新角色
     */
    @Operation(summary = "更新角色", description = "更新指定角色信息")
    @PutMapping("/{id}")
    @RequiresPermission("role:update")
    public ApiResponse<RoleResponse> updateRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody RoleRequest request) {
        Role role = roleService.updateRole(request,id);
        return ApiResponse.success(RoleResponse.from(role));
    }
    
    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "删除指定角色")
    @DeleteMapping("/{id}")
    @RequiresPermission("role:delete")
    public ApiResponse<Void> deleteRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success();
    }
    
    /**
     * 批量删除角色
     */
    @Operation(summary = "批量删除角色", description = "批量删除指定角色")
    @DeleteMapping("/batch")
    @RequiresPermission("role:delete")
    public ApiResponse<Void> deleteRoles(
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> ids) {
        roleService.deleteRoles(ids);
        return ApiResponse.success();
    }
    
    /**
     * 获取角色详情
     */
    @Operation(summary = "获取角色详情", description = "根据ID获取角色详情")
    @GetMapping("/{id}")
    @RequiresPermission("role:view")
    public ApiResponse<RoleResponse> getRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        Role role = roleService.getRoleById(id);
        return ApiResponse.success(RoleResponse.from(role));
    }
    
    /**
     * 根据编码获取角色
     */
    @Operation(summary = "根据编码获取角色", description = "根据角色编码获取角色详情")
    @GetMapping("/code/{code}")
    @RequiresPermission("role:view")
    public ApiResponse<RoleResponse> getRoleByCode(
            @Parameter(description = "角色编码") @PathVariable @NotEmpty String code) {
        Role role = roleService.getRoleByCode(code);
        return ApiResponse.success(RoleResponse.from(role));
    }
    
    /**
     * 分页查询角色
     */
    @Operation(summary = "分页查询角色", description = "分页查询角色列表")
    @GetMapping
    @RequiresPermission("role:list")
    public ApiResponse<PageResponse<RoleResponse>> getRoles(
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "角色编码") @RequestParam(required = false) String roleCode,
            @Parameter(description = "角色类型") @RequestParam(required = false) String roleType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        
        RoleRequest query = new RoleRequest();
        query.setRoleName(roleName);
        query.setRoleCode(roleCode);
        query.setRoleType(roleType);
        query.setStatus(status);
        
        IPage<Role> pageResult = roleService.getRolePage(query, pageNum, pageSize);
        List<RoleResponse> responses = pageResult.getRecords().stream()
                .map(RoleResponse::from)
                .toList();
        PageResponse<RoleResponse> responsePageResult = PageResponse.of(
                responses, pageResult.getTotal(), pageResult.getCurrent(), pageResult.getSize());
        return ApiResponse.success(responsePageResult);
    }
    
    /**
     * 获取所有角色
     */
    @Operation(summary = "获取所有角色", description = "获取所有角色列表")
    @GetMapping("/all")
    @RequiresPermission("role:list")
    public ApiResponse<List<RoleResponse>> getAllRoles(
            @Parameter(description = "是否只显示启用的") @RequestParam(defaultValue = "false") boolean onlyEnabled) {
        List<Role> roles = onlyEnabled ? roleService.getEnabledRoles() : roleService.getAllRoles();
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取用户角色列表
     */
    @Operation(summary = "获取用户角色列表", description = "获取指定用户的角色列表")
    @GetMapping("/user/{userId}")
    @RequiresPermission("role:user")
    public ApiResponse<List<RoleResponse>> getUserRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        List<Role> roles = roleService.getUserRoles(userId);
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取角色用户列表
     */
    @Operation(summary = "获取角色用户列表", description = "获取指定角色的用户列表")
    @GetMapping("/{roleId}/users")
    @RequiresPermission("role:user")
    public ApiResponse<PageResponse<UserResponse>> getRoleUsers(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long roleId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<User> pageResult = roleService.getRoleUserPage(roleId, pageNum, pageSize);
        List<UserResponse> responses = pageResult.getRecords().stream()
                .map(UserResponse::from)
                .toList();
        PageResponse<UserResponse> responsePageResult = new PageResponse<>(
                responses, pageResult.getTotal(), pageResult.getCurrent(), pageResult.getSize());
        return ApiResponse.success(responsePageResult);
    }
    
    /**
     * 为用户分配角色
     */
    @Operation(summary = "为用户分配角色", description = "为指定用户分配角色")
    @PostMapping("/user/{userId}/assign")
    @RequiresPermission("role:assign")
    public ApiResponse<Void> assignRolesToUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> roleIds) {
        roleService.assignRolesToUser(userId, roleIds);
        return ApiResponse.success();
    }
    
    /**
     * 移除用户角色
     */
    @Operation(summary = "移除用户角色", description = "移除指定用户的角色")
    @DeleteMapping("/user/{userId}/remove")
    @RequiresPermission("role:assign")
    public ApiResponse<Void> removeRolesFromUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> roleIds) {
        roleService.removeRolesFromUser(userId, roleIds);
        return ApiResponse.success();
    }
    
    /**
     * 设置用户角色
     */
    @Operation(summary = "设置用户角色", description = "设置指定用户的角色（覆盖原有角色）")
    @PutMapping("/user/{userId}/set")
    @RequiresPermission("role:assign")
    public ApiResponse<Void> setUserRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        roleService.setUserRoles(userId, roleIds);
        return ApiResponse.success();
    }
    
    /**
     * 批量分配角色给用户
     */
    @Operation(summary = "批量分配角色给用户", description = "批量为用户分配角色")
    @PostMapping("/batch/assign")
    @RequiresPermission("role:assign")
    public ApiResponse<Void> batchAssignRolesToUsers(
            @Parameter(description = "用户ID列表") @RequestParam @NotEmpty List<Long> userIds,
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> roleIds) {
        roleService.batchAssignRolesToUsers(userIds, roleIds);
        return ApiResponse.success();
    }
    
    /**
     * 批量移除用户角色
     */
    @Operation(summary = "批量移除用户角色", description = "批量移除用户的角色")
    @DeleteMapping("/batch/remove")
    @RequiresPermission("role:assign")
    public ApiResponse<Void> batchRemoveRolesFromUsers(
            @Parameter(description = "用户ID列表") @RequestParam @NotEmpty List<Long> userIds,
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> roleIds) {
        roleService.batchRemoveRolesFromUsers(userIds, roleIds);
        return ApiResponse.success();
    }
    
    /**
     * 检查用户角色
     */
    @Operation(summary = "检查用户角色", description = "检查用户是否拥有指定角色")
    @PostMapping("/user/{userId}/check")
    @RequiresPermission("role:check")
    public ApiResponse<Map<String, Boolean>> checkUserRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId,
            @Parameter(description = "角色编码列表") @RequestBody @NotEmpty List<String> roleCodes) {
        Map<String, Boolean> result = roleService.checkUserRoles(userId, roleCodes);
        return ApiResponse.success(result);
    }
    
    /**
     * 启用角色
     */
    @Operation(summary = "启用角色", description = "启用指定角色")
    @PutMapping("/{id}/enable")
    @RequiresPermission("role:update")
    public ApiResponse<Void> enableRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        roleService.enableRole(id);
        return ApiResponse.success();
    }
    
    /**
     * 禁用角色
     */
    @Operation(summary = "禁用角色", description = "禁用指定角色")
    @PutMapping("/{id}/disable")
    @RequiresPermission("role:update")
    public ApiResponse<Void> disableRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id) {
        roleService.disableRole(id);
        return ApiResponse.success();
    }
    
    /**
     * 批量启用角色
     */
    @Operation(summary = "批量启用角色", description = "批量启用指定角色")
    @PutMapping("/batch/enable")
    @RequiresPermission("role:update")
    public ApiResponse<Void> enableRoles(
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> ids) {
        roleService.enableRoles(ids);
        return ApiResponse.success();
    }
    
    /**
     * 批量禁用角色
     */
    @Operation(summary = "批量禁用角色", description = "批量禁用指定角色")
    @PutMapping("/batch/disable")
    @RequiresPermission("role:update")
    public ApiResponse<Void> disableRoles(
            @Parameter(description = "角色ID列表") @RequestBody @NotEmpty List<Long> ids) {
        roleService.disableRoles(ids);
        return ApiResponse.success();
    }
    
    /**
     * 复制角色
     */
    @Operation(summary = "复制角色", description = "复制指定角色")
    @PostMapping("/{id}/copy")
    @RequiresPermission("role:create")
    public ApiResponse<RoleResponse> copyRole(
            @Parameter(description = "角色ID") @PathVariable @NotNull Long id,
            @Parameter(description = "新角色名称") @RequestParam @NotEmpty String newRoleName,
            @Parameter(description = "新角色编码") @RequestParam @NotEmpty String newRoleCode) {
        Role role = roleService.copyRole(id, newRoleName, newRoleCode);
        return ApiResponse.success(RoleResponse.from(role));
    }
    
    /**
     * 获取默认角色
     */
    @Operation(summary = "获取默认角色", description = "获取默认角色列表")
    @GetMapping("/default")
    @RequiresPermission("role:list")
    public ApiResponse<List<RoleResponse>> getDefaultRoles() {
        List<Role> roles = roleService.getDefaultRoles();
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取系统角色
     */
    @Operation(summary = "获取系统角色", description = "获取系统角色列表")
    @GetMapping("/system")
    @RequiresPermission("role:list")
    public ApiResponse<List<RoleResponse>> getSystemRoles() {
        List<Role> roles = roleService.getSystemRoles();
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取业务角色
     */
    @Operation(summary = "获取业务角色", description = "获取业务角色列表")
    @GetMapping("/business")
    @RequiresPermission("role:list")
    public ApiResponse<List<RoleResponse>> getBusinessRoles() {
        List<Role> roles = roleService.getBusinessRoles();
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取自定义角色
     */
    @Operation(summary = "获取自定义角色", description = "获取自定义角色列表")
    @GetMapping("/custom")
    @RequiresPermission("role:list")
    public ApiResponse<List<RoleResponse>> getCustomRoles() {
        List<Role> roles = roleService.getCustomRoles();
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取角色层级结构
     */
    @Operation(summary = "获取角色层级结构", description = "获取角色层级结构")
    @GetMapping("/hierarchy")
    @RequiresPermission("role:list")
    public ApiResponse<List<RoleResponse>> getRoleHierarchy() {
        List<Role> roles = roleService.getRoleHierarchy();
        List<RoleResponse> responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 检查角色编码是否存在
     */
    @Operation(summary = "检查角色编码是否存在", description = "检查角色编码是否已存在")
    @GetMapping("/check-code/{code}")
    @RequiresPermission("role:view")
    public ApiResponse<Boolean> checkRoleCodeExists(
            @Parameter(description = "角色编码") @PathVariable @NotEmpty String code,
            @Parameter(description = "排除的角色ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = roleService.existsByRoleCode(code, excludeId);
        return ApiResponse.success(exists);
    }
    
    /**
     * 刷新角色缓存
     */
    @Operation(summary = "刷新角色缓存", description = "刷新角色相关缓存")
    @PostMapping("/cache/refresh")
    @RequiresRole("ADMIN")
    public ApiResponse<Void> refreshRoleCache() {
        roleService.refreshCache();
        return ApiResponse.success();
    }
    
    /**
     * 清空角色缓存
     */
    @Operation(summary = "清空角色缓存", description = "清空角色相关缓存")
    @DeleteMapping("/cache/clear")
    @RequiresRole("ADMIN")
    public ApiResponse<Void> clearRoleCache() {
        roleService.clearCache();
        return ApiResponse.success();
    }
}