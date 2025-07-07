package com.biobt.user.domain.entity;

import com.biobt.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * OAuth客户端实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_oauth_client", indexes = {
    @Index(name = "idx_client_id", columnList = "client_id"),
    @Index(name = "idx_provider_type", columnList = "provider_type"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
public class OAuthClient extends BaseEntity {
    
    /**
     * 客户端ID
     */
    @NotBlank(message = "客户端ID不能为空")
    @Size(max = 100, message = "客户端ID长度不能超过100个字符")
    @Column(name = "client_id", nullable = false, unique = true, length = 100)
    private String clientId;
    
    /**
     * 客户端密钥
     */
    @NotBlank(message = "客户端密钥不能为空")
    @Size(max = 200, message = "客户端密钥长度不能超过200个字符")
    @Column(name = "client_secret", nullable = false, length = 200)
    private String clientSecret;
    
    /**
     * 客户端名称
     */
    @NotBlank(message = "客户端名称不能为空")
    @Size(max = 100, message = "客户端名称长度不能超过100个字符")
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;
    
    /**
     * 提供商类型（github, google, wechat, dingtalk, feishu等）
     */
    @NotBlank(message = "提供商类型不能为空")
    @Size(max = 50, message = "提供商类型长度不能超过50个字符")
    @Column(name = "provider_type", nullable = false, length = 50)
    private String providerType;
    
    /**
     * 提供商名称
     */
    @Size(max = 100, message = "提供商名称长度不能超过100个字符")
    @Column(name = "provider_name", length = 100)
    private String providerName;
    
    /**
     * 授权URL
     */
    @Size(max = 500, message = "授权URL长度不能超过500个字符")
    @Column(name = "authorization_uri", length = 500)
    private String authorizationUri;
    
    /**
     * 令牌URL
     */
    @Size(max = 500, message = "令牌URL长度不能超过500个字符")
    @Column(name = "token_uri", length = 500)
    private String tokenUri;
    
    /**
     * 用户信息URL
     */
    @Size(max = 500, message = "用户信息URL长度不能超过500个字符")
    @Column(name = "user_info_uri", length = 500)
    private String userInfoUri;
    
    /**
     * 重定向URI
     */
    @Size(max = 500, message = "重定向URI长度不能超过500个字符")
    @Column(name = "redirect_uri", length = 500)
    private String redirectUri;
    
    /**
     * 授权范围
     */
    @Size(max = 200, message = "授权范围长度不能超过200个字符")
    @Column(name = "scope", length = 200)
    private String scope;
    
    /**
     * 状态（1-启用，0-禁用）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    /**
     * 是否自动注册用户
     */
    @Column(name = "auto_register", nullable = false)
    private Boolean autoRegister = true;
    
    /**
     * 默认角色ID（自动注册用户时分配的角色）
     */
    @Column(name = "default_role_id")
    private Long defaultRoleId;
    
    /**
     * 用户信息映射配置（JSON格式）
     */
    @Column(name = "user_mapping", columnDefinition = "TEXT")
    private String userMapping;
    
    /**
     * 图标URL
     */
    @Size(max = 500, message = "图标URL长度不能超过500个字符")
    @Column(name = "icon_url", length = 500)
    private String iconUrl;
    
    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /**
     * 配置信息（JSON格式，存储额外的配置参数）
     */
    @Column(name = "config_info", columnDefinition = "TEXT")
    private String configInfo;
    
    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
    
    /**
     * 扩展信息（JSON格式）
     */
    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo;
    
    /**
     * 判断客户端是否可用
     */
    public boolean isEnabled() {
        return status == 1;
    }
    
    /**
     * 判断是否为GitHub提供商
     */
    public boolean isGitHub() {
        return "github".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为Google提供商
     */
    public boolean isGoogle() {
        return "google".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为微信提供商
     */
    public boolean isWeChat() {
        return "wechat".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为钉钉提供商
     */
    public boolean isDingTalk() {
        return "dingtalk".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为飞书提供商
     */
    public boolean isFeishu() {
        return "feishu".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为企业微信提供商
     */
    public boolean isWeChatWork() {
        return "wechatwork".equalsIgnoreCase(providerType);
    }
}