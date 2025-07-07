package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * OAuth登录响应DTO
 */
@Data
@Schema(description = "OAuth登录响应")
public class OAuthLoginResponse {
    
    @Schema(description = "登录是否成功", example = "true")
    private Boolean success;
    
    @Schema(description = "响应消息", example = "登录成功")
    private String message;
    
    @Schema(description = "用户ID", example = "123")
    private Long userId;
    
    @Schema(description = "用户名", example = "john_doe")
    private String username;
    
    @Schema(description = "昵称", example = "John Doe")
    private String nickname;
    
    @Schema(description = "邮箱", example = "john@example.com")
    private String email;
    
    @Schema(description = "头像", example = "https://avatars.githubusercontent.com/u/12345678")
    private String avatar;
    
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    
    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;
    
    @Schema(description = "令牌过期时间（秒）", example = "3600")
    private Long expiresIn;
    
    @Schema(description = "令牌过期时间")
    private LocalDateTime expireTime;
    
    @Schema(description = "OAuth提供商类型", example = "GITHUB")
    private String providerType;
    
    @Schema(description = "OAuth绑定ID", example = "456")
    private Long oauthBindingId;
    
    @Schema(description = "是否新用户", example = "false")
    private Boolean isNewUser;
    
    @Schema(description = "是否新绑定", example = "true")
    private Boolean isNewBinding;
    
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;
    
    @Schema(description = "登录IP", example = "192.168.1.100")
    private String loginIp;
    
    @Schema(description = "用户角色列表")
    private String[] roles;
    
    @Schema(description = "用户权限列表")
    private String[] permissions;
    
    @Schema(description = "用户信息")
    private UserResponse userInfo;
    
    @Schema(description = "OAuth绑定信息")
    private UserOAuthResponse oauthInfo;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
    
    /**
     * 创建成功响应
     */
    public static OAuthLoginResponse success(String message) {
        OAuthLoginResponse response = new OAuthLoginResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setLoginTime(LocalDateTime.now());
        return response;
    }
    
    /**
     * 创建失败响应
     */
    public static OAuthLoginResponse failure(String message) {
        OAuthLoginResponse response = new OAuthLoginResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
    
    /**
     * 设置用户信息
     */
    public OAuthLoginResponse withUser(Long userId, String username, String nickname, String email, String avatar) {
        this.setUserId(userId);
        this.setUsername(username);
        this.setNickname(nickname);
        this.setEmail(email);
        this.setAvatar(avatar);
        return this;
    }
    
    /**
     * 设置令牌信息
     */
    public OAuthLoginResponse withToken(String accessToken, String refreshToken, String tokenType, Long expiresIn) {
        this.setAccessToken(accessToken);
        this.setRefreshToken(refreshToken);
        this.setTokenType(tokenType);
        this.setExpiresIn(expiresIn);
        if (expiresIn != null) {
            this.setExpireTime(LocalDateTime.now().plusSeconds(expiresIn));
        }
        return this;
    }
    
    /**
     * 设置OAuth信息
     */
    public OAuthLoginResponse withOAuth(String providerType, Long oauthBindingId, Boolean isNewUser, Boolean isNewBinding) {
        this.setProviderType(providerType);
        this.setOauthBindingId(oauthBindingId);
        this.setIsNewUser(isNewUser);
        this.setIsNewBinding(isNewBinding);
        return this;
    }
    
    /**
     * 设置登录信息
     */
    public OAuthLoginResponse withLogin(String loginIp, String[] roles, String[] permissions) {
        this.setLoginIp(loginIp);
        this.setRoles(roles);
        this.setPermissions(permissions);
        return this;
    }
}