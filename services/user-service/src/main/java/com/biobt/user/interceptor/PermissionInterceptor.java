package com.biobt.user.interceptor;

import com.biobt.user.annotation.RequiresPermission;
import com.biobt.user.annotation.RequiresRole;
import com.biobt.user.annotation.DataPermission;
import com.biobt.user.service.PermissionService;
import com.biobt.user.service.RoleService;
import com.biobt.user.service.DataPermissionService;
import com.biobt.common.core.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 权限拦截器
 * 处理权限注解的AOP逻辑
 */
@Slf4j
@Aspect
@Component
@Order(100)
@RequiredArgsConstructor
public class PermissionInterceptor {
    
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final DataPermissionService dataPermissionService;
    
    /**
     * 权限检查切面
     */
    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new SecurityException("用户未登录");
        }
        
        // 获取权限编码
        String[] permissions = getPermissions(requiresPermission);
        if (permissions.length == 0) {
            return joinPoint.proceed();
        }
        
        // 检查权限
        boolean hasPermission = checkUserPermissions(userId, permissions, requiresPermission.logical());
        if (!hasPermission) {
            throw new SecurityException(requiresPermission.message());
        }
        
        // 检查数据权限
        if (requiresPermission.checkDataPermission()) {
            checkDataPermission(userId, requiresPermission, joinPoint);
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * 角色检查切面
     */
    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new SecurityException("用户未登录");
        }
        
        // 获取角色编码
        String[] roles = getRoles(requiresRole);
        if (roles.length == 0) {
            return joinPoint.proceed();
        }
        
        // 检查角色
        boolean hasRole = checkUserRoles(userId, roles, requiresRole.logical());
        if (!hasRole) {
            throw new SecurityException(requiresRole.message());
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * 数据权限检查切面
     */
    @Around("@annotation(dataPermission)")
    public Object checkDataPermission(ProceedingJoinPoint joinPoint, DataPermission dataPermission) throws Throwable {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new SecurityException("用户未登录");
        }
        
        // 超级管理员跳过检查
        if (dataPermission.ignoreAdmin() && SecurityUtils.isAdmin()) {
            return joinPoint.proceed();
        }
        
        // 检查数据权限
        boolean hasDataPermission = dataPermissionService.hasDataPermission(
            userId, 
            dataPermission.resource(), 
            dataPermission.operation().getValue()
        );
        
        if (!hasDataPermission) {
            throw new SecurityException(dataPermission.message());
        }
        
        // 处理返回结果的数据过滤
        Object result = joinPoint.proceed();
        return filterDataResult(userId, dataPermission, result);
    }
    
    /**
     * 获取权限编码
     */
    private String[] getPermissions(RequiresPermission requiresPermission) {
        String[] permissions = requiresPermission.value();
        if (permissions.length == 0) {
            permissions = requiresPermission.permissions();
        }
        return permissions;
    }
    
    /**
     * 获取角色编码
     */
    private String[] getRoles(RequiresRole requiresRole) {
        String[] roles = requiresRole.value();
        if (roles.length == 0) {
            roles = requiresRole.roles();
        }
        return roles;
    }
    
    /**
     * 检查用户权限
     */
    private boolean checkUserPermissions(Long userId, String[] permissions, RequiresPermission.Logical logical) {
        if (logical == RequiresPermission.Logical.AND) {
            return permissionService.hasAllPermissions(userId, permissions);
        } else {
            return permissionService.hasAnyPermission(userId, permissions);
        }
    }
    
    /**
     * 检查用户角色
     */
    private boolean checkUserRoles(Long userId, String[] roles, RequiresRole.Logical logical) {
        if (logical == RequiresRole.Logical.AND) {
            return roleService.hasAllRoles(userId, roles);
        } else {
            return roleService.hasAnyRole(userId, roles);
        }
    }
    
    /**
     * 检查数据权限
     */
    private void checkDataPermission(Long userId, RequiresPermission requiresPermission, ProceedingJoinPoint joinPoint) {
        if (!StringUtils.hasText(requiresPermission.dataResource())) {
            return;
        }
        
        boolean hasDataPermission = dataPermissionService.hasDataPermission(
            userId,
            requiresPermission.dataResource(),
            requiresPermission.dataOperation().getValue()
        );
        
        if (!hasDataPermission) {
            throw new SecurityException("数据权限不足");
        }
    }
    
    /**
     * 过滤数据结果
     */
    @SuppressWarnings("unchecked")
    private Object filterDataResult(Long userId, DataPermission dataPermission, Object result) {
        if (result == null) {
            return null;
        }
        
        // 如果不启用列权限，直接返回
        if (!dataPermission.enableColumnPermission()) {
            return result;
        }
        
        try {
            // 处理Map类型结果
            if (result instanceof Map) {
                return dataPermissionService.filterResultColumns(
                    userId,
                    dataPermission.resource(),
                    dataPermission.operation().getValue(),
                    (Map<String, Object>) result
                );
            }
            
            // 处理List<Map>类型结果
            if (result instanceof List) {
                List<?> list = (List<?>) result;
                if (!list.isEmpty() && list.get(0) instanceof Map) {
                    return dataPermissionService.filterResultColumnsList(
                        userId,
                        dataPermission.resource(),
                        dataPermission.operation().getValue(),
                        (List<Map<String, Object>>) result
                    );
                }
            }
            
            // 其他类型暂不处理
            return result;
            
        } catch (Exception e) {
            log.warn("数据权限过滤失败: {}", e.getMessage());
            return result;
        }
    }
    
    /**
     * 组合权限和角色检查切面
     */
    @Around("@annotation(requiresPermission) && @annotation(requiresRole)")
    public Object checkPermissionAndRole(ProceedingJoinPoint joinPoint, 
                                        RequiresPermission requiresPermission,
                                        RequiresRole requiresRole) throws Throwable {
        // 先检查角色
        checkRole(joinPoint, requiresRole);
        // 再检查权限
        return checkPermission(joinPoint, requiresPermission);
    }
    
    /**
     * 获取方法上的注解
     */
    private <T> T getMethodAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }
    
    /**
     * 记录权限检查日志
     */
    private void logPermissionCheck(String type, Long userId, String[] codes, boolean result) {
        log.debug("权限检查 - 类型: {}, 用户ID: {}, 编码: {}, 结果: {}", 
                 type, userId, Arrays.toString(codes), result);
    }
}