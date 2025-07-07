package com.biobt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.user.domain.dto.PermissionRequest;
import com.biobt.user.domain.entity.Permission;
import com.biobt.user.domain.entity.Role;
import com.biobt.user.domain.entity.User;
import com.biobt.common.core.domain.PageRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限服务接口
 */
public interface PermissionService  extends IService<Permission> {
    
    /**
     * 创建权限
     */
    Permission createPermission(Permission permission);
    
    /**
     * 更新权限
     *     Permission updatePermission(Permission permission);
     */

    /**
     * 删除权限
     */
    void deletePermission(Long permissionId);
    
    /**
     * 根据ID获取权限
     */
    Permission getPermissionById(Long permissionId);
    
    /**
     * 根据权限编码获取权限
     */
    Permission getPermissionByCode(String permissionCode);
    
    /**
     * 分页查询权限
     */
    IPage<Permission> getPermissions(String permissionName, String permissionCode, String permissionType, Integer status, Page<Permission> page);
    
    /**
     * 获取权限树
     */
    List<Permission> getPermissionTree();
    
    /**
     * 获取用户权限列表
     */
    List<Permission> getUserPermissions(Long userId);
    
    /**
     * 获取用户权限编码集合
     */
    Set<String> getUserPermissionCodes(Long userId);
    
    /**
     * 获取角色权限列表
     */
    List<Permission> getRolePermissions(Long roleId);
    
    /**
     * 获取角色权限编码集合
     */
    Set<String> getRolePermissionCodes(Long roleId);
    
    /**
     * 为角色分配权限
     */
    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    
    /**
     * 为用户分配直接权限
     */
    void assignPermissionsToUser(Long userId, List<Long> permissionIds);
    
    /**
     * 移除角色权限
     */
    void removePermissionsFromRole(Long roleId, List<Long> permissionIds);
    
    /**
     * 移除用户直接权限
     */
    void removePermissionsFromUser(Long userId, List<Long> permissionIds);
    
    /**
     * 检查用户是否有指定权限
     */
    boolean hasPermission(Long userId, String permissionCode);
    
    /**
     * 检查用户是否有指定权限（支持多个权限码）
     */
    boolean hasAnyPermission(Long userId, String... permissionCodes);
    
    /**
     * 检查用户是否有所有指定权限
     */
    boolean hasAllPermissions(Long userId, String... permissionCodes);
    
    /**
     * 检查角色是否有指定权限
     */
    boolean roleHasPermission(Long roleId, String permissionCode);
    
    /**
     * 获取用户菜单权限
     */
    List<Permission> getUserMenuPermissions(Long userId);
    
    /**
     * 获取用户按钮权限
     */
    List<Permission> getUserButtonPermissions(Long userId);
    
    /**
     * 获取用户接口权限
     */
    List<Permission> getUserApiPermissions(Long userId);
    
    /**
     * 获取用户数据权限
     */
    List<Permission> getUserDataPermissions(Long userId);
    
    /**
     * 获取用户字段权限
     */
    List<Permission> getUserFieldPermissions(Long userId);
    
    /**
     * 刷新用户权限缓存
     */
    void refreshUserPermissionCache(Long userId);
    
    /**
     * 刷新角色权限缓存
     */
    void refreshRolePermissionCache(Long roleId);
    
    /**
     * 清空所有权限缓存
     */
    void clearAllPermissionCache();
    
    /**
     * 启用权限
     */
    void enablePermission(Long permissionId);
    
    /**
     * 禁用权限
     */
    void disablePermission(Long permissionId);
    
    /**
     * 批量启用权限
     */
    void batchEnablePermissions(List<Long> permissionIds);
    
    /**
     * 批量禁用权限
     */
    void batchDisablePermissions(List<Long> permissionIds);
    
    /**
     * 获取权限的子权限
     */
    List<Permission> getChildPermissions(Long parentId);
    
    /**
     * 获取权限的所有后代权限
     */
    List<Permission> getDescendantPermissions(Long parentId);
    
    /**
     * 检查权限编码是否存在
     */
    boolean existsByPermissionCode(String permissionCode);
    
    /**
     * 检查权限编码是否存在（排除指定ID）
     */
    boolean existsByPermissionCodeAndIdNot(String permissionCode, Long excludeId);

    IPage<Permission> getPermissionPage(PermissionRequest query, Integer pageNum, Integer pageSize);
    
    /**
     * 创建权限
     */
    Permission createPermission(PermissionRequest request);
    
    /**
     * 获取用户权限列表（支持资源类型过滤）
     */
    List<Permission> getUserPermissions(Long userId, String resourceType);
    
    /**
     * 获取用户权限编码列表（支持资源类型过滤）
     */
    List<String> getUserPermissionCodes(Long userId, String resourceType);
    
    /**
     * 检查用户权限
     */
    Map<String, Boolean> checkUserPermissions(Long userId, List<String> permissionCodes);
    
    /**
     * 批量启用权限
     */
    void enablePermissions(List<Long> permissionIds);
    
    /**
     * 批量禁用权限
     */
    void disablePermissions(List<Long> permissionIds);
    
    /**
     * 获取角色权限列表（支持资源类型过滤）
     */
    List<Permission> getRolePermissions(Long roleId, String resourceType);
    
    /**
     * 获取角色权限编码列表（支持资源类型过滤）
     */
    List<String> getRolePermissionCodes(Long roleId, String resourceType);
    
    /**
     * 检查角色权限
     */
    Map<String, Boolean> checkRolePermissions(Long roleId, List<String> permissionCodes);
    
    /**
     * 获取权限统计信息
     */
    Map<String, Object> getPermissionStatistics();
    
    /**
     * 获取用户权限统计
     */
    Map<String, Object> getUserPermissionStatistics(Long userId);
    
    /**
     * 获取角色权限统计
     */
    Map<String, Object> getRolePermissionStatistics(Long roleId);
}