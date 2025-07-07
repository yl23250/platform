package com.biobt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.user.domain.dto.OAuthLoginResponse;
import com.biobt.user.domain.entity.OAuthClient;
import com.biobt.user.domain.entity.Permission;
import com.biobt.user.domain.entity.UserOAuth;
import com.biobt.user.domain.entity.User;
import com.biobt.common.core.domain.PageRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * OAuth服务接口
 */
public interface OAuthService extends IService<OAuthClient> {
    
    // ==================== OAuth客户端管理 ====================
    
    /**
     * 创建OAuth客户端
     */
    OAuthClient createOAuthClient(OAuthClient oauthClient);
    
    /**
     * 更新OAuth客户端
     */
    OAuthClient updateOAuthClient(OAuthClient oauthClient);
    
    /**
     * 删除OAuth客户端
     */
    void deleteOAuthClient(Long clientId);
    
    /**
     * 根据ID获取OAuth客户端
     */
    OAuthClient getOAuthClientById(Long clientId);
    
    /**
     * 根据客户端ID获取OAuth客户端
     */
    OAuthClient getOAuthClientByClientId(String clientId);
    
    /**
     * 根据提供商类型获取OAuth客户端
     */
    List<OAuthClient> getOAuthClientsByProvider(String providerType);
    
    /**
     * 分页查询OAuth客户端
     */
    IPage<OAuthClient> getOAuthClients(String clientName, String providerType, Integer status, Page<OAuthClient> page);
    
    /**
     * 获取所有启用的OAuth客户端
     */
    List<OAuthClient> getEnabledOAuthClients();
    
    /**
     * 启用OAuth客户端
     */
    void enableOAuthClient(Long clientId);
    
    /**
     * 禁用OAuth客户端
     */
    void disableOAuthClient(Long clientId);
    
    // ==================== OAuth登录流程 ====================
    
    /**
     * 获取OAuth授权URL
     */
    String getAuthorizationUrl(String providerType, String state);
    
    /**
     * 处理OAuth回调
     */
    Map<String, Object> handleOAuthCallback(String providerType, String code, String state);
    
    /**
     * OAuth登录
     */
    User oauthLogin(String providerType, String code, String state);
    
    /**
     * 通过访问令牌获取用户信息
     */
    Map<String, Object> getUserInfoByAccessToken(String providerType, String accessToken);
    
    /**
     * 刷新访问令牌
     */
    Map<String, Object> refreshAccessToken(String providerType, String refreshToken);
    
    // ==================== 用户OAuth绑定管理 ====================
    
    /**
     * 绑定OAuth账号
     */
    UserOAuth bindOAuthAccount(Long userId, String providerType, String code, String state);
    
    /**
     * 解绑OAuth账号
     */
    void unbindOAuthAccount(Long userId, String providerType);
    
    /**
     * 解绑OAuth账号（通过绑定ID）
     */
    void unbindOAuthAccountById(Long bindingId);
    
    /**
     * 获取用户的OAuth绑定列表
     */
    List<UserOAuth> getUserOAuthBindings(Long userId);
    
    /**
     * 获取用户指定提供商的OAuth绑定
     */
    UserOAuth getUserOAuthBinding(Long userId, String providerType);
    
    /**
     * 获取当前用户的OAuth绑定列表
     */
    List<UserOAuth> getCurrentUserOAuthBindings();
    
    /**
     * 根据绑定ID获取OAuth绑定
     */
    UserOAuth getOAuthBindingById(Long bindingId);
    
    /**
     * 检查用户是否绑定了指定OAuth账号
     */
    boolean isOAuthAccountBound(Long userId, String providerType);
    
    /**
     * 根据第三方用户ID查找绑定关系
     */
    UserOAuth findOAuthBindingByOpenId(String providerType, String openId);
    
    /**
     * 根据联合ID查找绑定关系
     */
    List<UserOAuth> findOAuthBindingsByUnionId(String providerType, String unionId);
    
    // ==================== OAuth用户注册 ====================
    
    /**
     * 自动注册OAuth用户
     */
    User autoRegisterOAuthUser(String providerType, Map<String, Object> userInfo);
    
    /**
     * 手动注册OAuth用户
     */
    User manualRegisterOAuthUser(String providerType, Map<String, Object> userInfo, 
                                Map<String, Object> additionalInfo);
    
    /**
     * 检查是否允许自动注册
     */
    boolean isAutoRegisterEnabled(String providerType);
    
    /**
     * 映射OAuth用户信息到系统用户
     */
    User mapOAuthUserToSystemUser(String providerType, Map<String, Object> oauthUserInfo);
    
    // ==================== OAuth令牌管理 ====================
    
    /**
     * 更新OAuth令牌
     */
    void updateOAuthTokens(Long bindingId, String accessToken, String refreshToken, 
                          Long expiresIn);
    
    /**
     * 检查OAuth令牌是否过期
     */
    boolean isOAuthTokenExpired(Long bindingId);
    
    /**
     * 自动刷新过期的OAuth令牌
     */
    boolean autoRefreshExpiredToken(Long bindingId);
    
    /**
     * 撤销OAuth令牌
     */
    void revokeOAuthToken(Long bindingId);
    
    // ==================== OAuth统计和监控 ====================
    
    /**
     * 获取OAuth登录统计
     */
    Map<String, Object> getOAuthLoginStatistics();
    
    /**
     * 获取OAuth绑定统计
     */
    Map<String, Object> getOAuthBindingStatistics();
    
    /**
     * 获取提供商使用统计
     */
    Map<String, Object> getProviderUsageStatistics();
    
    // ==================== OAuth配置和验证 ====================
    
    /**
     * 验证OAuth客户端配置
     */
    boolean validateOAuthClientConfig(OAuthClient oauthClient);
    
    /**
     * 测试OAuth客户端连接
     */
    boolean testOAuthClientConnection(String providerType);
    
    /**
     * 获取OAuth提供商支持的功能
     */
    Map<String, Object> getProviderCapabilities(String providerType);
    
    /**
     * 同步OAuth用户信息
     */
    void syncOAuthUserInfo(Long bindingId);
    
    /**
     * 批量同步OAuth用户信息
     */
    void batchSyncOAuthUserInfo(String providerType);
    
    // ==================== OAuth安全管理 ====================
    
    /**
     * 验证OAuth状态参数
     */
    boolean validateOAuthState(String state);
    
    /**
     * 生成OAuth状态参数
     */
    String generateOAuthState();
    
    /**
     * 检查OAuth安全风险
     */
    Map<String, Object> checkOAuthSecurityRisks(Long userId);
    
    /**
     * 记录OAuth操作日志
     */
    void logOAuthOperation(String operation, String providerType, Long userId, 
                          String details, boolean success);

    String getAuthorizeUrl(String provider, String redirectUri, String state);

    OAuthLoginResponse handleCallback(String provider, String code, String state, HttpServletRequest request, HttpServletResponse response);
}