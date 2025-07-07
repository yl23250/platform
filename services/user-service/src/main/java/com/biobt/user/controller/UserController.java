package com.biobt.user.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.common.core.domain.ApiResponse;
import com.biobt.common.core.domain.PageRequest;
import com.biobt.common.core.domain.PageResponse;
import com.biobt.user.domain.dto.*;
import com.biobt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户相关的增删改查操作")
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    
    /**
     * 创建用户
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建新的用户账号")
    @PreAuthorize("hasAuthority('user:create')")
    public ApiResponse<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        log.info("创建用户: {}", request.getUsername());
        UserResponse response = userService.createUser(request);
        return ApiResponse.success("用户创建成功", response);
    }
    
    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @PreAuthorize("hasAuthority('user:read') or @userService.isCurrentUser(#id)")
    public ApiResponse<UserResponse> getUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        log.info("获取用户详情: {}", id);
        UserResponse response = userService.getUserById(id);
        return ApiResponse.success(response);
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public ApiResponse<UserResponse> getCurrentUser() {
        log.info("获取当前用户信息");
        UserResponse response = userService.getCurrentUser();
        return ApiResponse.success(response);
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    @PreAuthorize("hasAuthority('user:read')")
    public ApiResponse<PageResponse<UserResponse>> getUsers(
            @Valid PageRequest pageRequest,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "用户状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId) {
        log.info("分页查询用户列表: page={}, size={}, keyword={}", 
                pageRequest.getPageNum(), pageRequest.getPageSize(), keyword);
        
        UserPageRequest userPageRequest = new UserPageRequest();
        userPageRequest.setPageNum(pageRequest.getPageNum());
        userPageRequest.setPageSize(pageRequest.getPageSize());
        userPageRequest.setKeyword(keyword);
        userPageRequest.setStatus(status);
        // TODO: Add deptId support to UserPageRequest if needed
        
        PageResponse<UserResponse> response = userService.getUserPage(userPageRequest);
        return ApiResponse.success(response);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
    @PreAuthorize("hasAuthority('user:update') or @userService.isCurrentUser(#id)")
    public ApiResponse<UserResponse> updateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("更新用户信息: {}", id);
        UserResponse response = userService.updateUser(id, request);
        return ApiResponse.success("用户信息更新成功", response);
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "软删除指定用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public ApiResponse<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        log.info("删除用户: {}", id);
        userService.deleteUser(id);
        return ApiResponse.success("用户删除成功", null);
    }
    
    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量软删除多个用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public ApiResponse<Void> batchDeleteUsers(
            @Parameter(description = "用户ID列表") @RequestBody @NotNull Long[] ids) {
        log.info("批量删除用户: {}", (Object) ids);
        userService.deleteUsers(Arrays.asList(ids));
        return ApiResponse.success("用户批量删除成功", null);
    }
    
    /**
     * 启用用户
     */
    @PutMapping("/{id}/enable")
    @Operation(summary = "启用用户", description = "启用指定用户账号")
    @PreAuthorize("hasAuthority('user:update')")
    public ApiResponse<Void> enableUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        log.info("启用用户: {}", id);
        userService.enableUser(id);
        return ApiResponse.success("用户启用成功", null);
    }
    
    /**
     * 禁用用户
     */
    @PutMapping("/{id}/disable")
    @Operation(summary = "禁用用户", description = "禁用指定用户账号")
    @PreAuthorize("hasAuthority('user:update')")
    public ApiResponse<Void> disableUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        log.info("禁用用户: {}", id);
        userService.disableUser(id);
        return ApiResponse.success("用户禁用成功", null);
    }
    
    /**
     * 锁定用户
     */
    @PutMapping("/{id}/lock")
    @Operation(summary = "锁定用户", description = "锁定指定用户账号")
    @PreAuthorize("hasAuthority('user:update')")
    public ApiResponse<Void> lockUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        log.info("锁定用户: {}", id);
        userService.lockUser(id);
        return ApiResponse.success("用户锁定成功", null);
    }
    
    /**
     * 解锁用户
     */
    @PutMapping("/{id}/unlock")
    @Operation(summary = "解锁用户", description = "解锁指定用户账号")
    @PreAuthorize("hasAuthority('user:update')")
    public ApiResponse<Void> unlockUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        log.info("解锁用户: {}", id);
        userService.unlockUser(id);
        return ApiResponse.success("用户解锁成功", null);
    }
    
    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    @Operation(summary = "重置用户密码", description = "重置指定用户的密码为默认密码")
    @PreAuthorize("hasAuthority('user:update')")
    public ApiResponse<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "新密码") @RequestParam @NotNull String newPassword) {
        log.info("重置用户密码: {}", id);
        userService.resetPassword(id, newPassword);
        return ApiResponse.success("密码重置成功", null);
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/change-password")
    @Operation(summary = "修改密码", description = "当前用户修改自己的密码")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("用户修改密码");
        userService.changePassword(request);
        return ApiResponse.success("密码修改成功", null);
    }
    
    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    @Operation(summary = "检查用户名", description = "检查用户名是否已存在")
    public ApiResponse<Boolean> checkUsername(
            @Parameter(description = "用户名") @RequestParam @NotNull String username) {
        boolean exists = userService.existsByUsername(username);
        return ApiResponse.success(!exists); // 返回是否可用
    }
    
    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    @Operation(summary = "检查邮箱", description = "检查邮箱是否已存在")
    public ApiResponse<Boolean> checkEmail(
            @Parameter(description = "邮箱") @RequestParam @NotNull String email) {
        boolean exists = userService.existsByEmail(email);
        return ApiResponse.success(!exists); // 返回是否可用
    }
    
    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check-phone")
    @Operation(summary = "检查手机号", description = "检查手机号是否已存在")
    public ApiResponse<Boolean> checkPhone(
            @Parameter(description = "手机号") @RequestParam @NotNull String phone) {
        boolean exists = userService.existsByPhone(phone);
        return ApiResponse.success(!exists); // 返回是否可用
    }
}