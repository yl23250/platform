package com.biobt.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.biobt.common.core.result.Result;
import com.biobt.common.web.annotation.ApiVersion;
import com.biobt.user.dto.*;
import com.biobt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户管理控制器
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/v1/users")
@ApiVersion("1.0")
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public Result<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("创建用户请求: {}", request.getUsername());
        UserResponse response = userService.createUser(request);
        return Result.success(response);
    }

    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<UserResponse> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        UserResponse response = userService.getUserById(id);
        return Result.success(response);
    }

    @Operation(summary = "根据用户名获取用户", description = "根据用户名和租户ID获取用户信息")
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<UserResponse> getUserByUsername(
            @Parameter(description = "用户名", required = true)
            @PathVariable String username,
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId) {
        UserResponse response = userService.getUserByUsername(username, tenantId);
        return Result.success(response);
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/current")
    public Result<UserResponse> getCurrentUser() {
        UserResponse response = userService.getCurrentUser();
        return Result.success(response);
    }

    @Operation(summary = "分页查询用户", description = "根据条件分页查询用户列表")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<IPage<UserResponse>> getUserPage(@Valid @RequestBody UserPageRequest request) {
        IPage<UserResponse> page = userService.getUserPage(request);
        return Result.success(page);
    }

    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<UserResponse> updateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("更新用户请求: {}", id);
        UserResponse response = userService.updateUser(id, request);
        return Result.success(response);
    }

    @Operation(summary = "删除用户", description = "软删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Boolean> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        log.info("删除用户请求: {}", id);
        Boolean result = userService.deleteUser(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除用户", description = "批量软删除用户")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Boolean> batchDeleteUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestBody @NotEmpty List<Long> ids) {
        log.info("批量删除用户请求: {}", ids);
        Boolean result = userService.batchDeleteUsers(ids);
        return Result.success(result);
    }

    @Operation(summary = "启用用户", description = "启用指定用户")
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> enableUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        log.info("启用用户请求: {}", id);
        Boolean result = userService.enableUser(id);
        return Result.success(result);
    }

    @Operation(summary = "禁用用户", description = "禁用指定用户")
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> disableUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        log.info("禁用用户请求: {}", id);
        Boolean result = userService.disableUser(id);
        return Result.success(result);
    }

    @Operation(summary = "批量启用用户", description = "批量启用用户")
    @PutMapping("/batch/enable")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> batchEnableUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestBody @NotEmpty List<Long> ids) {
        log.info("批量启用用户请求: {}", ids);
        Boolean result = userService.batchEnableUsers(ids);
        return Result.success(result);
    }

    @Operation(summary = "批量禁用用户", description = "批量禁用用户")
    @PutMapping("/batch/disable")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> batchDisableUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestBody @NotEmpty List<Long> ids) {
        log.info("批量禁用用户请求: {}", ids);
        Boolean result = userService.batchDisableUsers(ids);
        return Result.success(result);
    }

    @Operation(summary = "锁定用户", description = "锁定指定用户")
    @PutMapping("/{id}/lock")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> lockUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        log.info("锁定用户请求: {}", id);
        Boolean result = userService.lockUser(id);
        return Result.success(result);
    }

    @Operation(summary = "解锁用户", description = "解锁指定用户")
    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> unlockUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        log.info("解锁用户请求: {}", id);
        Boolean result = userService.unlockUser(id);
        return Result.success(result);
    }

    @Operation(summary = "批量锁定用户", description = "批量锁定用户")
    @PutMapping("/batch/lock")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> batchLockUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestBody @NotEmpty List<Long> ids) {
        log.info("批量锁定用户请求: {}", ids);
        Boolean result = userService.batchLockUsers(ids);
        return Result.success(result);
    }

    @Operation(summary = "批量解锁用户", description = "批量解锁用户")
    @PutMapping("/batch/unlock")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Boolean> batchUnlockUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestBody @NotEmpty List<Long> ids) {
        log.info("批量解锁用户请求: {}", ids);
        Boolean result = userService.batchUnlockUsers(ids);
        return Result.success(result);
    }

    @Operation(summary = "重置密码", description = "重置用户密码")
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('user:reset-password')")
    public Result<Boolean> resetPassword(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "新密码")
            @RequestParam(required = false) String newPassword) {
        log.info("重置用户密码请求: {}", id);
        Boolean result = userService.resetPassword(id, newPassword);
        return Result.success(result);
    }

    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    @PutMapping("/{id}/change-password")
    public Result<Boolean> changePassword(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("修改用户密码请求: {}", id);
        Boolean result = userService.changePassword(id, request.getOldPassword(), request.getNewPassword());
        return Result.success(result);
    }

    @Operation(summary = "检查用户名是否存在", description = "检查用户名在指定租户下是否已存在")
    @GetMapping("/exists/username")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<Boolean> existsByUsername(
            @Parameter(description = "用户名", required = true)
            @RequestParam String username,
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId,
            @Parameter(description = "排除的用户ID")
            @RequestParam(required = false) Long excludeId) {
        Boolean exists = userService.existsByUsername(username, tenantId, excludeId);
        return Result.success(exists);
    }

    @Operation(summary = "检查邮箱是否存在", description = "检查邮箱在指定租户下是否已存在")
    @GetMapping("/exists/email")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<Boolean> existsByEmail(
            @Parameter(description = "邮箱", required = true)
            @RequestParam String email,
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId,
            @Parameter(description = "排除的用户ID")
            @RequestParam(required = false) Long excludeId) {
        Boolean exists = userService.existsByEmail(email, tenantId, excludeId);
        return Result.success(exists);
    }

    @Operation(summary = "检查手机号是否存在", description = "检查手机号在指定租户下是否已存在")
    @GetMapping("/exists/phone")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<Boolean> existsByPhone(
            @Parameter(description = "手机号", required = true)
            @RequestParam String phone,
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId,
            @Parameter(description = "排除的用户ID")
            @RequestParam(required = false) Long excludeId) {
        Boolean exists = userService.existsByPhone(phone, tenantId, excludeId);
        return Result.success(exists);
    }

    @Operation(summary = "获取用户权限", description = "获取指定用户的权限列表")
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<List<String>> getUserPermissions(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id) {
        List<String> permissions = userService.getUserPermissions(id);
        return Result.success(permissions);
    }

    @Operation(summary = "检查用户权限", description = "检查用户是否具有指定权限")
    @GetMapping("/{id}/permissions/{permission}")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<Boolean> hasPermission(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull Long id,
            @Parameter(description = "权限标识", required = true)
            @PathVariable String permission) {
        Boolean hasPermission = userService.hasPermission(id, permission);
        return Result.success(hasPermission);
    }

    @Operation(summary = "获取用户统计信息", description = "获取指定租户的用户统计信息")
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<UserService.UserStatistics> getUserStatistics(
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId) {
        UserService.UserStatistics statistics = userService.getUserStatistics(tenantId);
        return Result.success(statistics);
    }

    @Operation(summary = "获取最近登录用户", description = "获取最近登录的用户列表")
    @GetMapping("/recent-login")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<List<UserResponse>> getRecentLoginUsers(
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId,
            @Parameter(description = "限制数量")
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<UserResponse> users = userService.getRecentLoginUsers(tenantId, limit);
        return Result.success(users);
    }

    @Operation(summary = "获取长时间未登录用户", description = "获取长时间未登录的用户列表")
    @GetMapping("/long-no-login")
    @PreAuthorize("hasAuthority('user:read')")
    public Result<List<UserResponse>> getLongTimeNoLoginUsers(
            @Parameter(description = "租户ID")
            @RequestParam(required = false, defaultValue = "0") Long tenantId,
            @Parameter(description = "天数")
            @RequestParam(required = false, defaultValue = "30") Integer days,
            @Parameter(description = "限制数量")
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<UserResponse> users = userService.getLongTimeNoLoginUsers(tenantId, days, limit);
        return Result.success(users);
    }

    @Operation(summary = "导出用户", description = "根据条件导出用户数据")
    @PostMapping("/export")
    @PreAuthorize("hasAuthority('user:export')")
    public Result<List<UserResponse>> exportUsers(@Valid @RequestBody UserPageRequest request) {
        log.info("导出用户请求");
        List<UserResponse> users = userService.exportUsers(request);
        return Result.success(users);
    }

    @Operation(summary = "导入用户", description = "批量导入用户数据")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('user:import')")
    public Result<UserService.ImportResult> importUsers(
            @Parameter(description = "用户数据列表", required = true)
            @Valid @RequestBody List<CreateUserRequest> users) {
        log.info("导入用户请求，数量: {}", users.size());
        UserService.ImportResult result = userService.importUsers(users);
        return Result.success(result);
    }
}