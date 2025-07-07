package com.biobt.user.domain.dto;

import com.biobt.user.domain.entity.UserOAuth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户OAuth绑定响应DTO
 */
@Data
@Schema(description = "用户OAuth绑定响应")
public class UserOAuthResponse {
    
    @Schema(description = "绑定ID", example = "1")
    private Long id;
    
    @Schema(description = "用户ID", example = "123")
    private Long userId;
    
    @Schema(description = "第三方用户ID", example = "github_123456")
    private String thirdPartyUserId;
    
    @Schema(description = "提供商类型", example = "GITHUB")
    private String providerType;
    
    @Schema(description = "第三方用户名", example = "john_doe")
    private String thirdPartyUsername;
    
    @Schema(description = "第三方昵称", example = "John Doe")
    private String thirdPartyNickname;
    
    @Schema(description = "第三方邮箱", example = "john@example.com")
    private String thirdPartyEmail;
    
    @Schema(description = "第三方头像", example = "https://avatar.example.com/john.jpg")
    private String thirdPartyAvatar;
    
    @Schema(description = "访问令牌")
    private String accessToken;
    
    @Schema(description = "刷新令牌")
    private String refreshToken;
    
    @Schema(description = "令牌过期时间")
    private LocalDateTime tokenExpireTime;
    
    @Schema(description = "绑定状态", example = "1")
    private Integer status;
    
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;
    
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    /**
     * 从UserOAuth实体转换为响应DTO
     */
    public static UserOAuthResponse from(UserOAuth userOAuth) {
        if (userOAuth == null) {
            return null;
        }
        
        UserOAuthResponse response = new UserOAuthResponse();
        response.setId(userOAuth.getId());
        response.setUserId(userOAuth.getUserId());
        response.setThirdPartyUserId(userOAuth.getThirdPartyUserId());
        response.setProviderType(userOAuth.getProviderType());
        response.setThirdPartyUsername(userOAuth.getThirdPartyUsername());
        response.setThirdPartyNickname(userOAuth.getThirdPartyNickname());
        response.setThirdPartyEmail(userOAuth.getThirdPartyEmail());
        response.setThirdPartyAvatar(userOAuth.getThirdPartyAvatar());
        response.setAccessToken(userOAuth.getAccessToken());
        response.setRefreshToken(userOAuth.getRefreshToken());
        response.setTokenExpireTime(userOAuth.getTokenExpireTime());
        response.setStatus(userOAuth.getStatus());
        response.setBindTime(userOAuth.getBindTime());
        response.setLastLoginTime(userOAuth.getLastLoginTime());
        response.setExtraInfo(userOAuth.getExtraInfo());
        response.setCreateTime(userOAuth.getCreateTime());
        response.setUpdateTime(userOAuth.getUpdateTime());
        
        return response;
    }
    
    /**
     * 批量转换
     */
    public static List<UserOAuthResponse> fromList(List<UserOAuth> userOAuths) {
        if (userOAuths == null) {
            return null;
        }
        return userOAuths.stream()
                .map(UserOAuthResponse::from)
                .toList();
    }
}