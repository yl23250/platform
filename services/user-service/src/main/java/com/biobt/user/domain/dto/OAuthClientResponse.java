package com.biobt.user.domain.dto;

import com.biobt.user.domain.entity.OAuthClient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * OAuth客户端响应DTO
 */
@Data
@Schema(description = "OAuth客户端响应")
public class OAuthClientResponse {
    
    @Schema(description = "客户端主键ID", example = "1")
    private Long id;
    
    @Schema(description = "客户端ID", example = "github_client")
    private String clientId;
    
    @Schema(description = "客户端名称", example = "GitHub登录")
    private String clientName;
    
    @Schema(description = "提供商类型", example = "GITHUB")
    private String providerType;
    
    @Schema(description = "授权地址", example = "https://github.com/login/oauth/authorize")
    private String authorizationUri;
    
    @Schema(description = "Token地址", example = "https://github.com/login/oauth/access_token")
    private String tokenUri;
    
    @Schema(description = "用户信息地址", example = "https://api.github.com/user")
    private String userInfoUri;
    
    @Schema(description = "回调地址", example = "http://localhost:8080/api/oauth/callback/github")
    private String redirectUri;
    
    @Schema(description = "授权范围", example = "user:email")
    private String scope;
    
    @Schema(description = "状态", example = "1")
    private Integer status;
    
    @Schema(description = "是否自动注册", example = "true")
    private Boolean autoRegister;
    
    @Schema(description = "默认角色ID", example = "2")
    private Long defaultRoleId;
    
    @Schema(description = "用户信息映射配置", example = "{\"username\": \"login\", \"email\": \"email\", \"avatar\": \"avatar_url\"}")
    private String userMapping;
    
    @Schema(description = "图标地址", example = "https://github.com/favicon.ico")
    private String iconUrl;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "配置信息")
    private Map<String, Object> configInfo;
    
    @Schema(description = "备注", example = "GitHub第三方登录配置")
    private String remark;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "创建人")
    private String createBy;
    
    @Schema(description = "更新人")
    private String updateBy;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "是否为GitHub类型")
    private Boolean isGithub;
    
    @Schema(description = "是否为Gitee类型")
    private Boolean isGitee;
    
    @Schema(description = "是否为微信类型")
    private Boolean isWechat;
    
    @Schema(description = "是否为QQ类型")
    private Boolean isQq;
    
    @Schema(description = "是否为钉钉类型")
    private Boolean isDingtalk;
    
    @Schema(description = "是否为飞书类型")
    private Boolean isFeishu;
    
    @Schema(description = "绑定用户数量")
    private Long bindUserCount;
    
    /**
     * 从OAuthClient实体转换为OAuthClientResponse
     */
    public static OAuthClientResponse from(OAuthClient client) {
        if (client == null) {
            return null;
        }
        
        OAuthClientResponse response = new OAuthClientResponse();
        response.setId(client.getId());
        response.setClientId(client.getClientId());
        response.setClientName(client.getClientName());
        response.setProviderType(client.getProviderType());
        response.setAuthorizationUri(client.getAuthorizationUri());
        response.setTokenUri(client.getTokenUri());
        response.setUserInfoUri(client.getUserInfoUri());
        response.setRedirectUri(client.getRedirectUri());
        response.setScope(client.getScope());
        response.setStatus(client.getStatus());
        response.setAutoRegister(client.getAutoRegister());
        response.setDefaultRoleId(client.getDefaultRoleId());
        response.setUserMapping(client.getUserMapping());
        response.setIconUrl(client.getIconUrl());
        response.setSortOrder(client.getSortOrder());
        // 配置信息和扩展信息暂时设为null，后续可以添加JSON解析
        response.setConfigInfo(null);
        response.setRemark(client.getRemark());
        response.setExtraInfo(null);
        response.setCreateTime(client.getCreateTime());
        response.setUpdateTime(client.getUpdateTime());
        response.setCreateBy(client.getCreateBy());
        response.setUpdateBy(client.getUpdateBy());
        
        // 设置计算属性
        response.setEnabled(client.isEnabled());
        response.setIsGithub(client.isGitHub());
        response.setIsGitee(false); // Gitee方法不存在，设为false
        response.setIsWechat(client.isWeChat());
        response.setIsQq(false); // QQ方法不存在，设为false
        response.setIsDingtalk(client.isDingTalk());
        response.setIsFeishu(client.isFeishu());
        
        // 绑定用户数量暂时设为0，后续可以通过服务层查询
        response.setBindUserCount(0L);
        
        return response;
    }
    
    /**
     * 从OAuthClient实体列表转换为OAuthClientResponse列表
     */
    public static List<OAuthClientResponse> fromList(List<OAuthClient> clients) {
        if (clients == null) {
            return null;
        }
        return clients.stream()
                .map(OAuthClientResponse::from)
                .toList();
    }
}