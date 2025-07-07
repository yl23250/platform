package com.biobt.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biobt.common.core.exception.BusinessException;
import com.biobt.common.core.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import com.biobt.user.domain.dto.*;
import com.biobt.user.domain.entity.OAuthClient;
import com.biobt.user.domain.entity.User;
import com.biobt.user.domain.entity.UserOAuth;
import com.biobt.user.mapper.OAuthMapper;
import com.biobt.user.service.OAuthService;
import com.biobt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * OAuth服务实现类
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl extends ServiceImpl<OAuthMapper, OAuthClient> implements OAuthService {

    private final OAuthMapper oauthMapper;
    private final UserService userService;

    @Override
    @Transactional
    public OAuthClient createOAuthClient(OAuthClient oauthClient) {
        // 验证客户端ID唯一性
        if (oauthMapper.existsByClientId(oauthClient.getClientId(), null)) {
            throw new BusinessException("客户端ID已存在");
        }
        
        // 设置创建信息
        oauthClient.setCreateBy(SecurityUtils.getCurrentUserId().toString());
        oauthClient.setCreateTime(LocalDateTime.now());
        
        // 保存OAuth客户端
        save(oauthClient);
        
        log.info("创建OAuth客户端成功: {}", oauthClient.getClientId());
        return oauthClient;
    }

    @Override
    @Transactional
    @CacheEvict(value = "oauthClient", key = "#oauthClient.id")
    public OAuthClient updateOAuthClient(OAuthClient oauthClient) {
        // 检查OAuth客户端是否存在
        OAuthClient existingClient = getById(oauthClient.getId());
        if (existingClient == null) {
            throw new BusinessException("OAuth客户端不存在");
        }
        
        // 验证客户端ID唯一性
        if (oauthMapper.existsByClientId(oauthClient.getClientId(), oauthClient.getId())) {
            throw new BusinessException("客户端ID已存在");
        }
        
        // 设置更新信息
        oauthClient.setUpdateBy(SecurityUtils.getCurrentUserId().toString());
        oauthClient.setUpdateTime(LocalDateTime.now());
        
        // 更新OAuth客户端
        updateById(oauthClient);
        
        log.info("更新OAuth客户端成功: {}", oauthClient.getClientId());
        return oauthClient;
    }

    @Override
    @Transactional
    @CacheEvict(value = "oauthClient", key = "#clientId")
    public void deleteOAuthClient(Long clientId) {
        // 检查OAuth客户端是否存在
        OAuthClient oauthClient = getById(clientId);
        if (oauthClient == null) {
            throw new BusinessException("OAuth客户端不存在");
        }
        
        // 软删除OAuth客户端
        oauthClient.setDeleted(true);
        oauthClient.setUpdateBy(SecurityUtils.getCurrentUserId().toString());
        oauthClient.setUpdateTime(LocalDateTime.now());
        updateById(oauthClient);
        
        log.info("删除OAuth客户端成功: {}", oauthClient.getClientId());
    }

    @Override
    @Cacheable(value = "oauthClient", key = "#clientId")
    public OAuthClient getOAuthClientById(Long clientId) {
        return getById(clientId);
    }

    @Override
    public OAuthClient getOAuthClientByClientId(String clientId) {
        return oauthMapper.selectByClientId(clientId);
    }

    @Override
    public IPage<OAuthClient> getOAuthClients(OAuthClientRequest request, Page<OAuthClient> page) {
        return oauthMapper.selectOAuthClientPage(page, request);
    }

    @Override
    public List<OAuthClient> getEnabledOAuthClients() {
        return oauthMapper.selectEnabledClients();
    }

    @Override
    public List<OAuthClient> getOAuthClientsByProvider(String providerType) {
        return oauthMapper.selectByProviderType(providerType);
    }

    @Override
    public String getAuthorizationUrl(String providerType, String state) {
        return getAuthorizeUrl(providerType, null, state);
    }

    @Override
    public String getAuthorizeUrl(String providerType, String redirectUri, String state) {
        OAuthClient client = oauthMapper.selectByProviderType(providerType).stream()
            .filter(OAuthClient::isEnabled)
            .findFirst()
            .orElseThrow(() -> new BusinessException("未找到启用的OAuth客户端"));
        
        // 构建授权URL
        StringBuilder urlBuilder = new StringBuilder(client.getAuthorizationUri());
        urlBuilder.append("?client_id=").append(client.getClientId());
        urlBuilder.append("&response_type=code");
        urlBuilder.append("&redirect_uri=").append(redirectUri != null ? redirectUri : client.getRedirectUri());
        urlBuilder.append("&scope=").append(client.getScope());
        if (StringUtils.hasText(state)) {
            urlBuilder.append("&state=").append(state);
        }
        
        return urlBuilder.toString();
    }

    @Override
    public Map<String, Object> handleOAuthCallback(String providerType, String code, String state) {
        try {
            OAuthLoginResponse response = performOAuthCallback(providerType, code, state);
            Map<String, Object> result = new HashMap<>();
            result.put("success", response.isSuccess());
            result.put("message", response.getMessage());
            result.put("user", response.getUser());
            result.put("newUser", response.isNewUser());
            result.put("newBinding", response.isNewBinding());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

    @Override
    public OAuthLoginResponse handleCallback(String provider, String code, String state, HttpServletRequest request, HttpServletResponse response) {
        return performOAuthCallback(provider, code, state);
    }

    @Transactional
    public OAuthLoginResponse performOAuthCallback(String providerType, String code, String state) {
        try {
            // 获取OAuth客户端配置
            OAuthClient client = oauthMapper.selectByProviderType(providerType).stream()
                .filter(OAuthClient::isEnabled)
                .findFirst()
                .orElseThrow(() -> new BusinessException("未找到启用的OAuth客户端"));
            
            // 这里应该调用第三方OAuth服务获取访问令牌和用户信息
            // 为了演示，这里模拟返回用户信息
            Map<String, Object> userInfo = exchangeCodeForUserInfo(client, code);
            
            // 处理OAuth登录
            OAuthLoginRequest loginRequest = buildOAuthLoginRequest(providerType, userInfo);
            return performOAuthLogin(loginRequest);
            
        } catch (Exception e) {
            log.error("OAuth回调处理失败: {}", e.getMessage(), e);
            return OAuthLoginResponse.failure("OAuth登录失败: " + e.getMessage());
        }
    }

    @Override
    public User oauthLogin(String providerType, String code, String state) {
        OAuthLoginResponse response = performOAuthCallback(providerType, code, state);
        if (response.isSuccess()) {
            return response.getUser();
        } else {
            throw new BusinessException(response.getMessage());
        }
    }

    @Transactional
    public OAuthLoginResponse performOAuthLogin(OAuthLoginRequest request) {
        try {
            // 查找已绑定的用户
            UserOAuth userOAuth = oauthMapper.selectUserOAuthByThirdPartyId(
                request.getProviderType(), 
                request.getOpenId(), 
                "user"
            );
            
            User user;
            boolean isNewUser = false;
            boolean isNewBinding = false;
            
            if (userOAuth != null) {
                // 已绑定用户，直接登录
                UserResponse userResponse = userService.getUserById(userOAuth.getUserId());
                if (userResponse == null) {
                    throw new BusinessException("绑定的用户不存在");
                }
                user = convertToUser(userResponse);
                
                // 更新OAuth令牌信息
                updateOAuthToken(userOAuth, request);
                
            } else {
                // 未绑定用户
                if ("bind".equals(request.getBindMode()) && request.getBindUserId() != null) {
                    // 绑定到指定用户
                    UserResponse userResponse = userService.getUserById(request.getBindUserId());
                    if (userResponse == null) {
                        throw new BusinessException("要绑定的用户不存在");
                    }
                    user = convertToUser(userResponse);
                    
                    // 创建OAuth绑定
                    createUserOAuthBinding(user.getId(), request);
                    isNewBinding = true;
                    
                } else if (request.getAutoRegister()) {
                    // 自动注册新用户
                    user = createUserFromOAuth(request);
                    isNewUser = true;
                    isNewBinding = true;
                    
                    // 创建OAuth绑定
                    createUserOAuthBinding(user.getId(), request);
                    
                } else {
                    throw new BusinessException("用户未绑定，且未开启自动注册");
                }
            }
            
            // 记录登录日志
            recordOAuthLoginLog(user.getId(), request, "SUCCESS", null);
            
            // 构建登录响应
            OAuthLoginResponse response = OAuthLoginResponse.success();
            response.setUser(user);
            response.setNewUser(isNewUser);
            response.setNewBinding(isNewBinding);
            response.setLoginTime(LocalDateTime.now());
            response.setLoginIp(request.getIpAddress());
            
            log.info("OAuth登录成功: 用户={}, 提供商={}", user.getUsername(), request.getProviderType());
            return response;
            
        } catch (Exception e) {
            log.error("OAuth登录失败: {}", e.getMessage(), e);
            
            // 记录失败日志
            if (StringUtils.hasText(request.getOpenId())) {
                recordOAuthLoginLog(null, request, "FAILURE", e.getMessage());
            }
            
            return OAuthLoginResponse.failure(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserOAuth bindOAuthAccount(Long userId, String providerType, String code, String state) {
        // 检查用户是否存在
        UserResponse userResponse = userService.getUserById(userId);
        if (userResponse == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查是否已绑定该提供商
        if (oauthMapper.existsUserOAuthBinding(userId, providerType)) {
            throw new BusinessException("已绑定该OAuth提供商");
        }
        
        // 获取OAuth客户端配置
        OAuthClient client = oauthMapper.selectByProviderType(providerType);
        if (client == null) {
            throw new BusinessException("OAuth提供商配置不存在");
        }
        
        // 通过授权码获取用户信息
        Map<String, Object> userInfo = exchangeCodeForUserInfo(client, code);
        String thirdPartyUserId = (String) userInfo.get("id");
        String accessToken = (String) userInfo.get("access_token");
        
        // 检查第三方用户是否已被其他用户绑定
        if (oauthMapper.existsThirdPartyBinding(providerType, thirdPartyUserId, "user", userId)) {
            throw new BusinessException("该第三方账号已被其他用户绑定");
        }
        
        // 创建OAuth绑定
        UserOAuth userOAuth = new UserOAuth();
        userOAuth.setUserId(userId);
        userOAuth.setProviderType(providerType);
        userOAuth.setThirdPartyUserId(thirdPartyUserId);
        userOAuth.setThirdPartyUserType("user");
        userOAuth.setAccessToken(accessToken);
        userOAuth.setBindTime(LocalDateTime.now());
        userOAuth.setStatus(1);
        
        oauthMapper.insertUserOAuth(userOAuth);
        
        log.info("绑定OAuth账号成功: 用户={}, 提供商={}", userId, providerType);
        return userOAuth;
    }

    @Override
    @Transactional
    public void unbindOAuthAccount(Long userId, String providerType) {
        int result = oauthMapper.deleteUserOAuth(userId, providerType);
        if (result == 0) {
            throw new BusinessException("解绑失败，绑定关系不存在");
        }
        
        log.info("解绑OAuth账号成功: 用户={}, 提供商={}", userId, providerType);
    }

    @Override
    public List<UserOAuth> getUserOAuthBindings(Long userId) {
        return oauthMapper.selectUserOAuthBindings(userId);
    }

    @Override
    public Map<String, Object> refreshOAuthToken(Long userId, String providerType, String refreshToken) {
        return oauthMapper.refreshOAuthToken(userId, providerType, refreshToken);
    }
    
    @Override
    public List<UserOAuth> getCurrentUserOAuthBindings() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return oauthMapper.selectUserOAuthBindings(currentUserId);
    }
    
    @Override
    public UserOAuth getOAuthBindingById(Long bindingId) {
        return oauthMapper.selectUserOAuthById(bindingId);
    }

    @Override
    @Transactional
    public void enableOAuthClient(Long clientId) {
        OAuthClient client = getById(clientId);
        if (client == null) {
            throw new BusinessException("OAuth客户端不存在");
        }
        
        client.setStatus(1);
        client.setUpdateBy(SecurityUtils.getCurrentUserId());
        client.setUpdateTime(LocalDateTime.now());
        updateById(client);
        
        log.info("启用OAuth客户端成功: {}", client.getClientId());
    }

    @Override
    @Transactional
    public void disableOAuthClient(Long clientId) {
        OAuthClient client = getById(clientId);
        if (client == null) {
            throw new BusinessException("OAuth客户端不存在");
        }
        
        client.setStatus(0);
        client.setUpdateBy(SecurityUtils.getCurrentUserId());
        client.setUpdateTime(LocalDateTime.now());
        updateById(client);
        
        log.info("禁用OAuth客户端成功: {}", client.getClientId());
    }

    @Override
    @Transactional
    public void batchEnableOAuthClients(List<Long> clientIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = oauthMapper.batchEnable(clientIds, currentUserId);
        log.info("批量启用OAuth客户端成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDisableOAuthClients(List<Long> clientIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = oauthMapper.batchDisable(clientIds, currentUserId);
        log.info("批量禁用OAuth客户端成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDeleteOAuthClients(List<Long> clientIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = oauthMapper.batchDelete(clientIds, currentUserId);
        log.info("批量删除OAuth客户端成功，影响行数: {}", result);
    }

    @Override
    public Map<String, Object> testOAuthClientConfig(String clientId) {
        return oauthMapper.testOAuthClientConfig(clientId);
    }

    @Override
    public Map<String, Object> getUserInfoByAccessToken(String providerType, String accessToken) {
        // 实现通过访问令牌获取用户信息
        Map<String, Object> userInfo = new HashMap<>();
        // TODO: 根据不同的提供商类型调用相应的API
        return userInfo;
    }

    @Override
    public Map<String, Object> refreshAccessToken(String providerType, String refreshToken) {
        // 实现刷新访问令牌
        Map<String, Object> tokenInfo = new HashMap<>();
        // TODO: 根据不同的提供商类型刷新令牌
        return tokenInfo;
    }

    @Override
    public void unbindOAuthAccount(Long userId, String providerType) {
        oauthMapper.deleteUserOAuthBinding(userId, providerType);
        log.info("解绑OAuth账号成功: 用户={}, 提供商={}", userId, providerType);
    }

    @Override
    public void unbindOAuthAccountById(Long bindingId) {
        oauthMapper.deleteUserOAuthBindingById(bindingId);
        log.info("解绑OAuth账号成功: 绑定ID={}", bindingId);
    }

    @Override
    public List<UserOAuth> getUserOAuthBindings(Long userId) {
        return oauthMapper.selectUserOAuthBindings(userId);
    }

    @Override
    public UserOAuth getUserOAuthBinding(Long userId, String providerType) {
        return oauthMapper.selectUserOAuthByThirdPartyId(providerType, null, "user");
    }

    @Override
    public List<UserOAuth> getCurrentUserOAuthBindings() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return getUserOAuthBindings(currentUserId);
    }

    @Override
    public UserOAuth getOAuthBindingById(Long bindingId) {
        return oauthMapper.selectUserOAuthById(bindingId);
    }

    @Override
    public boolean isOAuthAccountBound(Long userId, String providerType) {
        return oauthMapper.existsUserOAuthBinding(userId, providerType);
    }

    @Override
    public UserOAuth findOAuthBindingByOpenId(String providerType, String openId) {
        return oauthMapper.selectUserOAuthByThirdPartyId(providerType, openId, "user");
    }

    @Override
    public List<UserOAuth> findOAuthBindingsByUnionId(String providerType, String unionId) {
        return oauthMapper.selectUserOAuthByUnionId(providerType, unionId);
    }

    @Override
    public User autoRegisterOAuthUser(String providerType, Map<String, Object> userInfo) {
        // TODO: 实现自动注册OAuth用户
        return null;
    }

    @Override
    public User manualRegisterOAuthUser(String providerType, Map<String, Object> userInfo, Map<String, Object> additionalInfo) {
        // TODO: 实现手动注册OAuth用户
        return null;
    }

    @Override
    public boolean isAutoRegisterEnabled(String providerType) {
        OAuthClient client = oauthMapper.selectByProviderType(providerType).stream()
            .filter(c -> c.getStatus() == 1)
            .findFirst()
            .orElse(null);
        return client != null && client.getAutoRegister();
    }

    @Override
    public User mapOAuthUserToSystemUser(String providerType, Map<String, Object> oauthUserInfo) {
        // TODO: 实现OAuth用户信息映射
        return null;
    }

    @Override
    public void updateOAuthTokens(Long bindingId, String accessToken, String refreshToken, Long expiresIn) {
        oauthMapper.updateOAuthTokens(bindingId, accessToken, refreshToken, expiresIn);
    }

    @Override
    public boolean isOAuthTokenExpired(Long bindingId) {
        return oauthMapper.isOAuthTokenExpired(bindingId);
    }

    @Override
    public boolean autoRefreshExpiredToken(Long bindingId) {
        // TODO: 实现自动刷新过期令牌
        return false;
    }

    @Override
    public void revokeOAuthToken(Long bindingId) {
        oauthMapper.revokeOAuthToken(bindingId);
    }

    @Override
    public Map<String, Object> getOAuthLoginStatistics() {
        return oauthMapper.getOAuthLoginStatistics();
    }

    @Override
    public Map<String, Object> getOAuthBindingStatistics() {
        return oauthMapper.getOAuthBindingStatistics();
    }

    @Override
    public Map<String, Object> getProviderUsageStatistics() {
        return oauthMapper.getProviderUsageStatistics();
    }

    @Override
    public boolean validateOAuthClientConfig(OAuthClient oauthClient) {
        // TODO: 实现OAuth客户端配置验证
        return true;
    }

    @Override
    public boolean testOAuthClientConnection(String providerType) {
        // TODO: 实现OAuth客户端连接测试
        return true;
    }

    @Override
    public Map<String, Object> getProviderCapabilities(String providerType) {
        Map<String, Object> capabilities = new HashMap<>();
        // TODO: 根据提供商类型返回支持的功能
        return capabilities;
    }

    @Override
    public void syncOAuthUserInfo(Long bindingId) {
        // TODO: 实现同步OAuth用户信息
    }

    @Override
    public void batchSyncOAuthUserInfo(String providerType) {
        // TODO: 实现批量同步OAuth用户信息
    }

    @Override
    public boolean validateOAuthState(String state) {
        // TODO: 实现OAuth状态参数验证
        return true;
    }

    @Override
    public String generateOAuthState() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Map<String, Object> checkOAuthSecurityRisks(Long userId) {
        Map<String, Object> risks = new HashMap<>();
        // TODO: 实现OAuth安全风险检查
        return risks;
    }

    @Override
    public void logOAuthOperation(String operation, String providerType, Long userId, String details, boolean success) {
        // TODO: 实现OAuth操作日志记录
        log.info("OAuth操作: 操作={}, 提供商={}, 用户={}, 详情={}, 成功={}", 
                operation, providerType, userId, details, success);
    }

    @Override
    public Map<String, Object> getOAuthStatistics(String providerType, LocalDateTime startTime, LocalDateTime endTime) {
        return oauthMapper.getOAuthStatistics(providerType, startTime, endTime);
    }

    @Override
    public IPage<Map<String, Object>> getOAuthLoginLogs(Long userId, String providerType, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        Page<Map<String, Object>> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        return oauthMapper.selectOAuthLoginLogs(page, userId, providerType, startTime, endTime);
    }

    @Override
    public Map<String, Object> getOAuthGlobalConfig() {
        return oauthMapper.getOAuthGlobalConfig();
    }

    @Override
    @Transactional
    public void updateOAuthGlobalConfig(Map<String, Object> config) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        oauthMapper.updateOAuthGlobalConfig(config, currentUserId);
        log.info("更新OAuth全局配置成功");
    }

    /**
     * 交换授权码获取用户信息
     */
    private Map<String, Object> exchangeCodeForUserInfo(OAuthClient client, String code) {
        // 这里应该调用第三方OAuth API
        // 为了演示，返回模拟数据
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", UUID.randomUUID().toString());
        userInfo.put("username", "oauth_user_" + System.currentTimeMillis());
        userInfo.put("nickname", "OAuth用户");
        userInfo.put("email", "oauth@example.com");
        userInfo.put("avatar", "https://example.com/avatar.jpg");
        userInfo.put("access_token", "mock_access_token");
        userInfo.put("refresh_token", "mock_refresh_token");
        userInfo.put("expires_in", 7200);
        
        return userInfo;
    }

    /**
     * 构建OAuth登录请求
     */
    private OAuthLoginRequest buildOAuthLoginRequest(String providerType, Map<String, Object> userInfo) {
        OAuthLoginRequest request = new OAuthLoginRequest();
        request.setProviderType(providerType);
        request.setOpenId((String) userInfo.get("id"));
        request.setOauthUsername((String) userInfo.get("username"));
        request.setOauthNickname((String) userInfo.get("nickname"));
        request.setOauthEmail((String) userInfo.get("email"));
        request.setOauthAvatar((String) userInfo.get("avatar"));
        request.setAccessToken((String) userInfo.get("access_token"));
        request.setRefreshToken((String) userInfo.get("refresh_token"));
        request.setExpiresIn(userInfo.get("expires_in") != null ? ((Integer) userInfo.get("expires_in")).longValue() : null);
        request.setAutoRegister(true);
        request.setRawUserInfo(userInfo);
        
        return request;
    }

    /**
     * 从OAuth信息创建用户
     */
    private User createUserFromOAuth(OAuthLoginRequest request) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(request.getOauthUsername());
        createUserRequest.setNickname(request.getOauthNickname());
        createUserRequest.setEmail(request.getOauthEmail());
        
        UserResponse userResponse = userService.createUser(createUserRequest);
        
        // 转换为User实体
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setNickname(userResponse.getNickname());
        user.setEmail(userResponse.getEmail());
        user.setAvatar(userResponse.getAvatar());
        user.setStatus(userResponse.getStatus());
        user.setLockStatus(userResponse.getLockStatus());
        
        return user;
    }

    /**
     * 创建用户OAuth绑定
     */
    private void createUserOAuthBinding(Long userId, OAuthLoginRequest request) {
        UserOAuth userOAuth = new UserOAuth();
        userOAuth.setUserId(userId);
        userOAuth.setProviderType(request.getProviderType());
        userOAuth.setThirdPartyUserId(request.getOpenId());
        userOAuth.setThirdPartyUserType("user");
        userOAuth.setThirdPartyUnionId(request.getUnionId());
        userOAuth.setUsername(request.getOauthUsername());
        userOAuth.setNickname(request.getOauthNickname());
        userOAuth.setEmail(request.getOauthEmail());
        userOAuth.setAvatar(request.getOauthAvatar());
        userOAuth.setAccessToken(request.getAccessToken());
        userOAuth.setRefreshToken(request.getRefreshToken());
        userOAuth.setExpiresAt(request.getExpiresIn() != null ? 
            LocalDateTime.now().plusSeconds(request.getExpiresIn()) : null);
        userOAuth.setRawUserInfo(request.getRawUserInfo() != null ? request.getRawUserInfo().toString() : null);
        userOAuth.setBindTime(LocalDateTime.now());
        userOAuth.setStatus(1);
        
        oauthMapper.insertUserOAuth(userOAuth);
    }

    /**
     * 更新OAuth令牌
     */
    private void updateOAuthToken(UserOAuth userOAuth, OAuthLoginRequest request) {
        LocalDateTime expiresAt = request.getExpiresIn() != null ? 
            LocalDateTime.now().plusSeconds(request.getExpiresIn()) : null;
        
        oauthMapper.updateOAuthToken(
            userOAuth.getUserId(),
            request.getProviderType(),
            request.getAccessToken(),
            request.getRefreshToken(),
            expiresAt
        );
    }

    /**
     * 记录OAuth登录日志
     */
    private void recordOAuthLoginLog(Long userId, OAuthLoginRequest request, String result, String errorMessage) {
        oauthMapper.insertOAuthLoginLog(
            userId,
            request.getProviderType(),
            request.getIpAddress(),
            request.getUserAgent(),
            result,
            errorMessage
        );
    }

    /**
     * 将UserResponse转换为User实体
     */
    private User convertToUser(UserResponse userResponse) {
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setNickname(userResponse.getNickname());
        user.setEmail(userResponse.getEmail());
        user.setPhone(userResponse.getPhone());
        user.setRealName(userResponse.getRealName());
        user.setAvatar(userResponse.getAvatar());
        user.setGender(userResponse.getGender());
        user.setBirthday(userResponse.getBirthday());
        user.setStatus(userResponse.getStatus());
        user.setLockStatus(userResponse.getLockStatus());
        user.setLastLoginTime(userResponse.getLastLoginTime());
        user.setLastLoginIp(userResponse.getLastLoginIp());
        user.setLoginCount(userResponse.getLoginCount());
        user.setCreateTime(userResponse.getCreateTime());
        user.setUpdateTime(userResponse.getUpdateTime());
        return user;
    }
}