package com.biobt.user.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.user.annotation.RequiresPermission;
import com.biobt.user.annotation.RequiresRole;
import com.biobt.user.domain.entity.OAuthClient;
import com.biobt.user.domain.entity.UserOAuth;
import com.biobt.user.domain.dto.OAuthClientRequest;
import com.biobt.user.domain.dto.OAuthClientResponse;
import com.biobt.user.domain.dto.OAuthLoginRequest;
import com.biobt.user.domain.dto.OAuthLoginResponse;
import com.biobt.user.domain.dto.UserOAuthResponse;
import com.biobt.user.service.OAuthService;
import com.biobt.common.core.domain.ApiResponse;
import com.biobt.common.core.domain.PageResponse;
import com.biobt.common.core.exception.BusinessException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * OAuth控制器
 * 提供OAuth登录和管理的REST API接口
 */
@Tag(name = "OAuth管理", description = "OAuth登录和管理相关接口")
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Validated
public class OAuthController {
    
    private final OAuthService oauthService;
    
    // ==================== OAuth登录相关接口 ====================
    
    /**
     * 获取OAuth授权URL
     */
    @Operation(summary = "获取OAuth授权URL", description = "获取第三方OAuth授权登录URL")
    @GetMapping("/authorize/{provider}")
    public ApiResponse<String> getAuthorizeUrl(
            @Parameter(description = "OAuth提供商") @PathVariable @NotEmpty String provider,
            @Parameter(description = "回调地址") @RequestParam(required = false) String redirectUri,
            @Parameter(description = "状态参数") @RequestParam(required = false) String state) {
        String authorizeUrl = oauthService.getAuthorizeUrl(provider, redirectUri, state);
        return ApiResponse.success(authorizeUrl);
    }
    
    /**
     * OAuth回调处理
     */
    @Operation(summary = "OAuth回调处理", description = "处理第三方OAuth回调")
    @GetMapping("/callback/{provider}")
    public ApiResponse<OAuthLoginResponse> handleCallback(
            @Parameter(description = "OAuth提供商") @PathVariable @NotEmpty String provider,
            @Parameter(description = "授权码") @RequestParam @NotEmpty String code,
            @Parameter(description = "状态参数") @RequestParam(required = false) String state,
            HttpServletRequest request,
            HttpServletResponse response) {
        OAuthLoginResponse loginResponse = oauthService.handleCallback(provider, code, state, request, response);
        return ApiResponse.success(loginResponse);
    }
    
    /**
     * OAuth登录
     */
    @Operation(summary = "OAuth登录", description = "使用OAuth进行登录")
    @PostMapping("/login")
    public ApiResponse<OAuthLoginResponse> oauthLogin(@Valid @RequestBody OAuthLoginRequest request) {
        OAuthLoginResponse loginResponse = oauthService.oauthLogin(request);
        return ApiResponse.success(loginResponse);
    }
    
    /**
     * 绑定OAuth账号
     */
    @Operation(summary = "绑定OAuth账号", description = "为当前用户绑定第三方OAuth账号")
    @PostMapping("/bind")
    @RequiresPermission("oauth:bind")
    public ApiResponse<UserOAuthResponse> bindOAuthAccount(@Valid @RequestBody OAuthLoginRequest request) {
        UserOAuth userOAuth = oauthService.bindOAuthAccount(request);
        return ApiResponse.success(UserOAuthResponse.from(userOAuth));
    }
    
    /**
     * 解绑OAuth账号
     */
    @Operation(summary = "解绑OAuth账号", description = "解绑指定的第三方OAuth账号")
    @DeleteMapping("/unbind/{id}")
    @RequiresPermission("oauth:unbind")
    public ApiResponse<Void> unbindOAuthAccount(
            @Parameter(description = "OAuth绑定ID") @PathVariable @NotNull Long id) {
        oauthService.unbindOAuthAccount(id);
        return ApiResponse.success();
    }
    
    /**
     * 刷新OAuth Token
     */
    @Operation(summary = "刷新OAuth Token", description = "刷新OAuth访问令牌")
    @PostMapping("/refresh-token/{id}")
    @RequiresPermission("oauth:refresh")
    public ApiResponse<Map<String, Object>> refreshOAuthToken(
            @Parameter(description = "OAuth绑定ID") @PathVariable @NotNull Long id) {
        // 获取绑定信息
        UserOAuth userOAuth = oauthService.getOAuthBindingById(id);
        if (userOAuth == null) {
            throw new BusinessException("OAuth绑定不存在");
        }
        Map<String, Object> result = oauthService.refreshAccessToken(
                userOAuth.getProviderType(), userOAuth.getRefreshToken());
        return ApiResponse.success(result);
    }
    
    // ==================== 用户OAuth账号管理 ====================
    
    /**
     * 获取用户OAuth绑定列表
     */
    @Operation(summary = "获取用户OAuth绑定列表", description = "获取指定用户的OAuth账号绑定列表")
    @GetMapping("/user/{userId}/bindings")
    @RequiresPermission("oauth:view")
    public ApiResponse<List<UserOAuthResponse>> getUserOAuthBindings(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        List<UserOAuth> bindings = oauthService.getUserOAuthBindings(userId);
        List<UserOAuthResponse> responses = bindings.stream()
                .map(UserOAuthResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取当前用户OAuth绑定列表
     */
    @Operation(summary = "获取当前用户OAuth绑定列表", description = "获取当前登录用户的OAuth账号绑定列表")
    @GetMapping("/my/bindings")
    @RequiresPermission("oauth:view")
    public ApiResponse<List<UserOAuthResponse>> getMyOAuthBindings() {
        List<UserOAuth> bindings = oauthService.getCurrentUserOAuthBindings();
        List<UserOAuthResponse> responses = bindings.stream()
                .map(UserOAuthResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 获取OAuth绑定详情
     */
    @Operation(summary = "获取OAuth绑定详情", description = "获取指定OAuth绑定的详情")
    @GetMapping("/binding/{id}")
    @RequiresPermission("oauth:view")
    public ApiResponse<UserOAuthResponse> getOAuthBinding(
            @Parameter(description = "OAuth绑定ID") @PathVariable @NotNull Long id) {
        UserOAuth userOAuth = oauthService.getOAuthBindingById(id);
        return ApiResponse.success(UserOAuthResponse.from(userOAuth));
    }
    
    // ==================== OAuth客户端管理 ====================
    
    /**
     * 创建OAuth客户端
     */
    @Operation(summary = "创建OAuth客户端", description = "创建新的OAuth客户端配置")
    @PostMapping("/clients")
    @RequiresPermission("oauth:client:create")
    public ApiResponse<OAuthClientResponse> createOAuthClient(@Valid @RequestBody OAuthClientRequest request) {
        OAuthClient client = request.toEntity();
        client = oauthService.createOAuthClient(client);
        return ApiResponse.success(OAuthClientResponse.from(client));
    }
    
    /**
     * 更新OAuth客户端
     */
    @Operation(summary = "更新OAuth客户端", description = "更新指定OAuth客户端配置")
    @PutMapping("/clients/{id}")
    @RequiresPermission("oauth:client:update")
    public ApiResponse<OAuthClientResponse> updateOAuthClient(
            @Parameter(description = "客户端ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody OAuthClientRequest request) {
        OAuthClient client = request.toEntity();
        client.setId(id);
        client = oauthService.updateOAuthClient(client);
        return ApiResponse.success(OAuthClientResponse.from(client));
    }
    
    /**
     * 删除OAuth客户端
     */
    @Operation(summary = "删除OAuth客户端", description = "删除指定OAuth客户端配置")
    @DeleteMapping("/clients/{id}")
    @RequiresPermission("oauth:client:delete")
    public ApiResponse<Void> deleteOAuthClient(
            @Parameter(description = "客户端ID") @PathVariable @NotNull Long id) {
        oauthService.deleteOAuthClient(id);
        return ApiResponse.success();
    }
    
    /**
     * 获取OAuth客户端详情
     */
    @Operation(summary = "获取OAuth客户端详情", description = "获取指定OAuth客户端详情")
    @GetMapping("/clients/{id}")
    @RequiresPermission("oauth:client:view")
    public ApiResponse<OAuthClientResponse> getOAuthClient(
            @Parameter(description = "客户端ID") @PathVariable @NotNull Long id) {
        OAuthClient client = oauthService.getOAuthClientById(id);
        return ApiResponse.success(OAuthClientResponse.from(client));
    }
    
    /**
     * 分页查询OAuth客户端
     */
    @Operation(summary = "分页查询OAuth客户端", description = "分页查询OAuth客户端列表")
    @GetMapping("/clients")
    @RequiresPermission("oauth:client:list")
    public ApiResponse<PageResponse<OAuthClientResponse>> getOAuthClients(
            @Parameter(description = "客户端名称") @RequestParam(required = false) String clientName,
            @Parameter(description = "提供商类型") @RequestParam(required = false) String providerType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") Integer size) {
        
        Page<OAuthClient> pageObj = new Page<>((long)page, (long)size);
        IPage<OAuthClient> pageResult = oauthService.getOAuthClients(clientName, providerType, status, pageObj);
        PageResponse<OAuthClientResponse> responsePageResult = PageResponse.of(
                pageResult.getRecords().stream().map(OAuthClientResponse::from).toList(),
                pageResult.getTotal(),
                pageResult.getCurrent(),
                pageResult.getSize()
        );
        return ApiResponse.success(responsePageResult);
    }
    
    /**
     * 获取启用的OAuth客户端
     */
    @Operation(summary = "获取启用的OAuth客户端", description = "获取所有启用的OAuth客户端")
    @GetMapping("/clients/enabled")
    public ApiResponse<List<OAuthClientResponse>> getEnabledOAuthClients() {
        List<OAuthClient> clients = oauthService.getEnabledOAuthClients();
        List<OAuthClientResponse> responses = clients.stream()
                .map(OAuthClientResponse::from)
                .toList();
        return ApiResponse.success(responses);
    }
    
    /**
     * 启用OAuth客户端
     */
    @Operation(summary = "启用OAuth客户端", description = "启用指定OAuth客户端")
    @PutMapping("/clients/{id}/enable")
    @RequiresPermission("oauth:client:update")
    public ApiResponse<Void> enableOAuthClient(
            @Parameter(description = "客户端ID") @PathVariable @NotNull Long id) {
        oauthService.enableOAuthClient(id);
        return ApiResponse.success();
    }
    
    /**
     * 禁用OAuth客户端
     */
    @Operation(summary = "禁用OAuth客户端", description = "禁用指定OAuth客户端")
    @PutMapping("/clients/{id}/disable")
    @RequiresPermission("oauth:client:update")
    public ApiResponse<Void> disableOAuthClient(
            @Parameter(description = "客户端ID") @PathVariable @NotNull Long id) {
        oauthService.disableOAuthClient(id);
        return ApiResponse.success();
    }
    
    /**
     * 测试OAuth客户端配置
     */
    @Operation(summary = "测试OAuth客户端配置", description = "测试OAuth客户端配置是否正确")
    @PostMapping("/clients/{id}/test")
    @RequiresPermission("oauth:client:test")
    public ApiResponse<Map<String, Object>> testOAuthClient(
            @Parameter(description = "客户端ID") @PathVariable @NotNull Long id) {
        // 先获取客户端信息
        OAuthClient client = oauthService.getOAuthClientById(id);
        if (client == null) {
            throw new BusinessException("OAuth客户端不存在");
        }
        boolean testResult = oauthService.testOAuthClientConnection(client.getProviderType());
        Map<String, Object> result = new HashMap<>();
        result.put("success", testResult);
        result.put("message", testResult ? "连接测试成功" : "连接测试失败");
        return ApiResponse.success(result);
    }
    
    // ==================== OAuth统计和监控 ====================
    
    /**
     * 获取OAuth登录统计
     */
    @Operation(summary = "获取OAuth登录统计", description = "获取OAuth登录统计信息")
    @GetMapping("/statistics/login")
    @RequiresPermission("oauth:statistics")
    public ApiResponse<Map<String, Object>> getOAuthLoginStatistics(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "提供商类型") @RequestParam(required = false) String providerType) {
        Map<String, Object> statistics = oauthService.getOAuthLoginStatistics();
        return ApiResponse.success(statistics);
    }
    
    /**
     * 获取OAuth绑定统计
     */
    @Operation(summary = "获取OAuth绑定统计", description = "获取OAuth账号绑定统计信息")
    @GetMapping("/statistics/binding")
    @RequiresPermission("oauth:statistics")
    public ApiResponse<Map<String, Object>> getOAuthBindingStatistics() {
        Map<String, Object> statistics = oauthService.getOAuthBindingStatistics();
        return ApiResponse.success(statistics);
    }
    
    // OAuth错误日志功能暂未实现
    // TODO: 实现OAuth错误日志查询功能
    
    // ==================== OAuth配置和管理 ====================
    
    // OAuth配置管理功能暂未实现
    // TODO: 实现OAuth配置管理功能
    
    // OAuth缓存管理功能暂未实现
    // TODO: 实现OAuth缓存管理功能
    
    /**
     * 同步OAuth用户信息
     */
    @Operation(summary = "同步OAuth用户信息", description = "同步第三方OAuth用户信息")
    @PostMapping("/sync/{id}")
    @RequiresPermission("oauth:sync")
    public ApiResponse<Void> syncOAuthUserInfo(
            @Parameter(description = "OAuth绑定ID") @PathVariable @NotNull Long id) {
        oauthService.syncOAuthUserInfo(id);
        return ApiResponse.success();
    }
    
    /**
     * 批量同步OAuth用户信息
     */
    @Operation(summary = "批量同步OAuth用户信息", description = "批量同步第三方OAuth用户信息")
    @PostMapping("/sync/batch")
    @RequiresPermission("oauth:sync")
    public ApiResponse<Void> batchSyncOAuthUserInfo(
            @Parameter(description = "提供商类型") @RequestParam @NotNull String providerType) {
        oauthService.batchSyncOAuthUserInfo(providerType);
        return ApiResponse.success();
    }
}