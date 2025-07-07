package com.biobt.user.config;

import com.biobt.user.interceptor.PermissionInterceptor;
import com.biobt.user.service.PermissionService;
import com.biobt.user.service.RoleService;
import com.biobt.user.service.DataPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 权限配置类
 * 配置权限相关的Bean和AOP设置
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@RequiredArgsConstructor
public class PermissionConfig {
    
    /**
     * 权限拦截器Bean
     */
    @Bean
    @Order(100)
    @ConditionalOnProperty(name = "biobt.security.permission.enabled", havingValue = "true", matchIfMissing = true)
    public PermissionInterceptor permissionInterceptor(PermissionService permissionService,
                                                       RoleService roleService,
                                                       DataPermissionService dataPermissionService) {
        return new PermissionInterceptor(permissionService, roleService, dataPermissionService);
    }
    
    /**
     * 权限缓存配置
     */
    @Bean
    @ConditionalOnProperty(name = "biobt.security.permission.cache.enabled", havingValue = "true", matchIfMissing = true)
    public PermissionCacheConfig permissionCacheConfig() {
        return new PermissionCacheConfig();
    }
    
    /**
     * 数据权限配置
     */
    @Bean
    @ConditionalOnProperty(name = "biobt.security.data-permission.enabled", havingValue = "true", matchIfMissing = true)
    public DataPermissionConfig dataPermissionConfig() {
        return new DataPermissionConfig();
    }
    
    /**
     * OAuth配置
     */
    @Bean
    @ConditionalOnProperty(name = "biobt.security.oauth.enabled", havingValue = "true", matchIfMissing = false)
    public OAuthConfig oauthConfig() {
        return new OAuthConfig();
    }
    
    /**
     * 权限缓存配置类
     */
    public static class PermissionCacheConfig {
        
        /**
         * 权限缓存过期时间（秒）
         */
        private long permissionCacheExpire = 3600;
        
        /**
         * 角色缓存过期时间（秒）
         */
        private long roleCacheExpire = 3600;
        
        /**
         * 数据权限缓存过期时间（秒）
         */
        private long dataPermissionCacheExpire = 1800;
        
        /**
         * 是否启用缓存预热
         */
        private boolean enableCacheWarmup = true;
        
        /**
         * 缓存键前缀
         */
        private String cacheKeyPrefix = "biobt:permission:";
        
        // Getters and Setters
        public long getPermissionCacheExpire() {
            return permissionCacheExpire;
        }
        
        public void setPermissionCacheExpire(long permissionCacheExpire) {
            this.permissionCacheExpire = permissionCacheExpire;
        }
        
        public long getRoleCacheExpire() {
            return roleCacheExpire;
        }
        
        public void setRoleCacheExpire(long roleCacheExpire) {
            this.roleCacheExpire = roleCacheExpire;
        }
        
        public long getDataPermissionCacheExpire() {
            return dataPermissionCacheExpire;
        }
        
        public void setDataPermissionCacheExpire(long dataPermissionCacheExpire) {
            this.dataPermissionCacheExpire = dataPermissionCacheExpire;
        }
        
        public boolean isEnableCacheWarmup() {
            return enableCacheWarmup;
        }
        
        public void setEnableCacheWarmup(boolean enableCacheWarmup) {
            this.enableCacheWarmup = enableCacheWarmup;
        }
        
        public String getCacheKeyPrefix() {
            return cacheKeyPrefix;
        }
        
        public void setCacheKeyPrefix(String cacheKeyPrefix) {
            this.cacheKeyPrefix = cacheKeyPrefix;
        }
    }
    
    /**
     * 数据权限配置类
     */
    public static class DataPermissionConfig {
        
        /**
         * 是否启用行权限
         */
        private boolean enableRowPermission = true;
        
        /**
         * 是否启用列权限
         */
        private boolean enableColumnPermission = true;
        
        /**
         * 默认数据范围
         */
        private String defaultDataScope = "SELF";
        
        /**
         * 是否忽略超级管理员
         */
        private boolean ignoreAdmin = true;
        
        /**
         * SQL注入检查
         */
        private boolean enableSqlInjectionCheck = true;
        
        /**
         * 最大条件长度
         */
        private int maxConditionLength = 1000;
        
        // Getters and Setters
        public boolean isEnableRowPermission() {
            return enableRowPermission;
        }
        
        public void setEnableRowPermission(boolean enableRowPermission) {
            this.enableRowPermission = enableRowPermission;
        }
        
        public boolean isEnableColumnPermission() {
            return enableColumnPermission;
        }
        
        public void setEnableColumnPermission(boolean enableColumnPermission) {
            this.enableColumnPermission = enableColumnPermission;
        }
        
        public String getDefaultDataScope() {
            return defaultDataScope;
        }
        
        public void setDefaultDataScope(String defaultDataScope) {
            this.defaultDataScope = defaultDataScope;
        }
        
        public boolean isIgnoreAdmin() {
            return ignoreAdmin;
        }
        
        public void setIgnoreAdmin(boolean ignoreAdmin) {
            this.ignoreAdmin = ignoreAdmin;
        }
        
        public boolean isEnableSqlInjectionCheck() {
            return enableSqlInjectionCheck;
        }
        
        public void setEnableSqlInjectionCheck(boolean enableSqlInjectionCheck) {
            this.enableSqlInjectionCheck = enableSqlInjectionCheck;
        }
        
        public int getMaxConditionLength() {
            return maxConditionLength;
        }
        
        public void setMaxConditionLength(int maxConditionLength) {
            this.maxConditionLength = maxConditionLength;
        }
    }
    
    /**
     * OAuth配置类
     */
    public static class OAuthConfig {
        
        /**
         * 是否启用自动注册
         */
        private boolean enableAutoRegister = true;
        
        /**
         * 默认角色ID
         */
        private Long defaultRoleId;
        
        /**
         * Token过期时间（秒）
         */
        private long tokenExpire = 7200;
        
        /**
         * 刷新Token过期时间（秒）
         */
        private long refreshTokenExpire = 2592000; // 30天
        
        /**
         * 是否启用状态验证
         */
        private boolean enableStateValidation = true;
        
        /**
         * 回调域名白名单
         */
        private String[] allowedDomains = {};
        
        // Getters and Setters
        public boolean isEnableAutoRegister() {
            return enableAutoRegister;
        }
        
        public void setEnableAutoRegister(boolean enableAutoRegister) {
            this.enableAutoRegister = enableAutoRegister;
        }
        
        public Long getDefaultRoleId() {
            return defaultRoleId;
        }
        
        public void setDefaultRoleId(Long defaultRoleId) {
            this.defaultRoleId = defaultRoleId;
        }
        
        public long getTokenExpire() {
            return tokenExpire;
        }
        
        public void setTokenExpire(long tokenExpire) {
            this.tokenExpire = tokenExpire;
        }
        
        public long getRefreshTokenExpire() {
            return refreshTokenExpire;
        }
        
        public void setRefreshTokenExpire(long refreshTokenExpire) {
            this.refreshTokenExpire = refreshTokenExpire;
        }
        
        public boolean isEnableStateValidation() {
            return enableStateValidation;
        }
        
        public void setEnableStateValidation(boolean enableStateValidation) {
            this.enableStateValidation = enableStateValidation;
        }
        
        public String[] getAllowedDomains() {
            return allowedDomains;
        }
        
        public void setAllowedDomains(String[] allowedDomains) {
            this.allowedDomains = allowedDomains;
        }
    }
}