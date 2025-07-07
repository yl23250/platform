package com.biobt.common.core.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 * 用于获取当前登录用户信息
 *
 * @author BioBt Team
 * @since 1.0.0
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID，如果未登录返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            // 如果是UserDetails实现，尝试从用户名解析ID
            // 这里假设用户名就是用户ID，实际项目中可能需要调整
            try {
                return Long.parseLong(((UserDetails) principal).getUsername());
            } catch (NumberFormatException e) {
                // 如果用户名不是数字，返回默认值
                return 1L;
            }
        } else if (principal instanceof String) {
            // 如果principal是字符串，尝试解析为Long
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                return 1L;
            }
        }
        
        // 默认返回系统用户ID
        return 1L;
    }
    
    /**
     * 获取当前登录用户名
     *
     * @return 用户名，如果未登录返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        
        return null;
    }
    
    /**
     * 获取当前认证信息
     *
     * @return Authentication对象
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    /**
     * 判断当前用户是否已认证
     *
     * @return true表示已认证，false表示未认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
}