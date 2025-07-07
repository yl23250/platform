package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * OAuth登录请求DTO
 */
@Data
@Schema(description = "OAuth登录请求")
public class OAuthLoginRequest {
    
    @Schema(description = "OAuth提供商类型", example = "GITHUB", allowableValues = {"GITHUB", "GITEE", "WECHAT", "QQ", "DINGTALK", "FEISHU", "CUSTOM"})
    @NotBlank(message = "OAuth提供商类型不能为空")
    private String providerType;
    
    @Schema(description = "授权码", example = "4/0AX4XfWjQZ1234567890")
    @NotBlank(message = "授权码不能为空")
    @Size(max = 500, message = "授权码长度不能超过500个字符")
    private String code;
    
    @Schema(description = "状态参数", example = "random_state_123")
    @Size(max = 200, message = "状态参数长度不能超过200个字符")
    private String state;
    
    @Schema(description = "回调地址", example = "http://localhost:8080/api/oauth/callback/github")
    @Size(max = 500, message = "回调地址长度不能超过500个字符")
    private String redirectUri;
    
    @Schema(description = "客户端ID", example = "github_client")
    @Size(max = 100, message = "客户端ID长度不能超过100个字符")
    private String clientId;
    
    @Schema(description = "访问令牌", example = "gho_1234567890abcdef")
    @Size(max = 500, message = "访问令牌长度不能超过500个字符")
    private String accessToken;
    
    @Schema(description = "刷新令牌", example = "ghr_1234567890abcdef")
    @Size(max = 500, message = "刷新令牌长度不能超过500个字符")
    private String refreshToken;
    
    @Schema(description = "令牌过期时间（秒）", example = "3600")
    private Long expiresIn;
    
    @Schema(description = "第三方用户ID", example = "12345678")
    @Size(max = 100, message = "第三方用户ID长度不能超过100个字符")
    private String openId;
    
    @Schema(description = "第三方联合ID", example = "unionid_123")
    @Size(max = 100, message = "第三方联合ID长度不能超过100个字符")
    private String unionId;
    
    @Schema(description = "第三方用户名", example = "john_doe")
    @Size(max = 100, message = "第三方用户名长度不能超过100个字符")
    private String oauthUsername;
    
    @Schema(description = "第三方昵称", example = "John Doe")
    @Size(max = 100, message = "第三方昵称长度不能超过100个字符")
    private String oauthNickname;
    
    @Schema(description = "第三方邮箱", example = "john@example.com")
    @Size(max = 100, message = "第三方邮箱长度不能超过100个字符")
    private String oauthEmail;
    
    @Schema(description = "第三方头像", example = "https://avatars.githubusercontent.com/u/12345678")
    @Size(max = 500, message = "第三方头像长度不能超过500个字符")
    private String oauthAvatar;
    
    @Schema(description = "原始用户信息")
    private Map<String, Object> rawUserInfo;
    
    @Schema(description = "是否绑定模式", example = "false")
    private Boolean bindMode;
    
    @Schema(description = "绑定的用户ID（绑定模式下使用）", example = "123")
    private Long bindUserId;
    
    @Schema(description = "自动注册", example = "true")
    private Boolean autoRegister;
    
    @Schema(description = "设备信息")
    private String deviceInfo;
    
    @Schema(description = "IP地址", example = "192.168.1.100")
    private String ipAddress;
    
    @Schema(description = "用户代理", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    private String userAgent;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}