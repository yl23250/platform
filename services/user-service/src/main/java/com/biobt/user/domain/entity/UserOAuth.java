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
 * 用户OAuth绑定实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_oauth", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_provider_type", columnList = "provider_type"),
    @Index(name = "idx_open_id", columnList = "open_id"),
    @Index(name = "idx_union_id", columnList = "union_id"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id"),
    @Index(name = "uk_provider_openid", columnList = "provider_type,open_id", unique = true)
})
public class UserOAuth extends BaseEntity {
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * OAuth客户端ID
     */
    @Column(name = "client_id")
    private Long clientId;
    
    /**
     * 提供商类型（github, google, wechat, dingtalk, feishu等）
     */
    @NotBlank(message = "提供商类型不能为空")
    @Size(max = 50, message = "提供商类型长度不能超过50个字符")
    @Column(name = "provider_type", nullable = false, length = 50)
    private String providerType;
    
    /**
     * 第三方用户唯一标识
     */
    @NotBlank(message = "第三方用户标识不能为空")
    @Size(max = 100, message = "第三方用户标识长度不能超过100个字符")
    @Column(name = "open_id", nullable = false, length = 100)
    private String openId;
    
    /**
     * 第三方用户联合标识（用于多应用统一）
     */
    @Size(max = 100, message = "联合标识长度不能超过100个字符")
    @Column(name = "union_id", length = 100)
    private String unionId;
    
    /**
     * 第三方用户名
     */
    @Size(max = 100, message = "第三方用户名长度不能超过100个字符")
    @Column(name = "oauth_username", length = 100)
    private String oauthUsername;
    
    /**
     * 第三方用户昵称
     */
    @Size(max = 100, message = "第三方用户昵称长度不能超过100个字符")
    @Column(name = "oauth_nickname", length = 100)
    private String oauthNickname;
    
    /**
     * 第三方用户邮箱
     */
    @Size(max = 100, message = "第三方用户邮箱长度不能超过100个字符")
    @Column(name = "oauth_email", length = 100)
    private String oauthEmail;
    
    /**
     * 第三方用户头像
     */
    @Size(max = 500, message = "第三方用户头像长度不能超过500个字符")
    @Column(name = "oauth_avatar", length = 500)
    private String oauthAvatar;
    
    /**
     * 访问令牌
     */
    @Column(name = "access_token", columnDefinition = "TEXT")
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String refreshToken;
    
    /**
     * 令牌过期时间
     */
    @Column(name = "token_expire_time")
    private LocalDateTime tokenExpireTime;
    
    /**
     * 绑定状态（1-已绑定，0-已解绑）
     */
    @Column(name = "bind_status", nullable = false)
    private Integer bindStatus = 1;
    
    /**
     * 绑定时间
     */
    @Column(name = "bind_time")
    private LocalDateTime bindTime;
    
    /**
     * 解绑时间
     */
    @Column(name = "unbind_time")
    private LocalDateTime unbindTime;
    
    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 登录次数
     */
    @Column(name = "login_count")
    private Integer loginCount = 0;
    
    /**
     * 第三方用户原始信息（JSON格式）
     */
    @Column(name = "raw_user_info", columnDefinition = "TEXT")
    private String rawUserInfo;
    
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
     * 关联的用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    /**
     * 关联的OAuth客户端
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private OAuthClient oauthClient;
    
    /**
     * 判断绑定是否有效
     */
    public boolean isValid() {
        return bindStatus == 1;
    }
    
    /**
     * 判断令牌是否过期
     */
    public boolean isTokenExpired() {
        return tokenExpireTime != null && tokenExpireTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * 判断是否为GitHub绑定
     */
    public boolean isGitHub() {
        return "github".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为Google绑定
     */
    public boolean isGoogle() {
        return "google".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为微信绑定
     */
    public boolean isWeChat() {
        return "wechat".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为钉钉绑定
     */
    public boolean isDingTalk() {
        return "dingtalk".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为飞书绑定
     */
    public boolean isFeishu() {
        return "feishu".equalsIgnoreCase(providerType);
    }
    
    /**
     * 判断是否为企业微信绑定
     */
    public boolean isWeChatWork() {
        return "wechatwork".equalsIgnoreCase(providerType);
    }
}