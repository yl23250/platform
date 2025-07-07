package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import com.biobt.user.domain.entity.OAuthClient;

/**
 * OAuth客户端请求DTO
 */
@Data
@Schema(description = "OAuth客户端请求")
public class OAuthClientRequest {
    
    @Schema(description = "客户端ID", example = "github_client")
    @NotBlank(message = "客户端ID不能为空")
    @Size(max = 100, message = "客户端ID长度不能超过100个字符")
    private String clientId;
    
    @Schema(description = "客户端密钥", example = "github_secret_123")
    @NotBlank(message = "客户端密钥不能为空")
    @Size(max = 200, message = "客户端密钥长度不能超过200个字符")
    private String clientSecret;
    
    @Schema(description = "客户端名称", example = "GitHub登录")
    @NotBlank(message = "客户端名称不能为空")
    @Size(max = 100, message = "客户端名称长度不能超过100个字符")
    private String clientName;
    
    @Schema(description = "提供商类型", example = "GITHUB", allowableValues = {"GITHUB", "GITEE", "WECHAT", "QQ", "DINGTALK", "FEISHU", "CUSTOM"})
    @NotBlank(message = "提供商类型不能为空")
    private String providerType;
    
    @Schema(description = "授权地址", example = "https://github.com/login/oauth/authorize")
    @NotBlank(message = "授权地址不能为空")
    @Size(max = 500, message = "授权地址长度不能超过500个字符")
    private String authorizationUri;
    
    @Schema(description = "Token地址", example = "https://github.com/login/oauth/access_token")
    @NotBlank(message = "Token地址不能为空")
    @Size(max = 500, message = "Token地址长度不能超过500个字符")
    private String tokenUri;
    
    @Schema(description = "用户信息地址", example = "https://api.github.com/user")
    @NotBlank(message = "用户信息地址不能为空")
    @Size(max = 500, message = "用户信息地址长度不能超过500个字符")
    private String userInfoUri;
    
    @Schema(description = "回调地址", example = "http://localhost:8080/api/oauth/callback/github")
    @NotBlank(message = "回调地址不能为空")
    @Size(max = 500, message = "回调地址长度不能超过500个字符")
    private String redirectUri;
    
    @Schema(description = "授权范围", example = "user:email")
    @Size(max = 200, message = "授权范围长度不能超过200个字符")
    private String scope;
    
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    @Schema(description = "是否自动注册", example = "true")
    private Boolean autoRegister;
    
    @Schema(description = "默认角色ID", example = "2")
    private Long defaultRoleId;
    
    @Schema(description = "用户信息映射配置", example = "{\"username\": \"login\", \"email\": \"email\", \"avatar\": \"avatar_url\"}")
    private String userMapping;
    
    @Schema(description = "图标地址", example = "https://github.com/favicon.ico")
    @Size(max = 500, message = "图标地址长度不能超过500个字符")
    private String iconUrl;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "配置信息")
    private Map<String, Object> configInfo;
    
    @Schema(description = "备注", example = "GitHub第三方登录配置")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
    
    /**
     * 转换为实体对象
     */
    public OAuthClient toEntity() {
        OAuthClient client = new OAuthClient();
        client.setClientId(this.clientId);
        client.setClientSecret(this.clientSecret);
        client.setClientName(this.clientName);
        client.setProviderType(this.providerType);
        client.setAuthorizationUri(this.authorizationUri);
        client.setTokenUri(this.tokenUri);
        client.setUserInfoUri(this.userInfoUri);
        client.setRedirectUri(this.redirectUri);
        client.setScope(this.scope);
        client.setStatus(this.status);
        client.setAutoRegister(this.autoRegister);
        client.setDefaultRoleId(this.defaultRoleId);
        client.setUserMapping(this.userMapping);
        client.setIconUrl(this.iconUrl);
        client.setSortOrder(this.sortOrder);
        client.setRemark(this.remark);
        return client;
    }
}