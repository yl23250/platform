package com.biobt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biobt.common.core.exception.BusinessException;
import com.biobt.common.core.utils.SecurityUtils;
import com.biobt.user.domain.dto.PermissionRequest;
import com.biobt.user.domain.entity.Permission;
import com.biobt.user.mapper.PermissionMapper;
import com.biobt.user.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        // 验证权限编码唯一性
        if (permissionMapper.existsByCode(permission.getPermissionCode(), null)) {
            throw new BusinessException("权限编码已存在");
        }
        
        // 设置创建信息
        permission.setCreateBy(SecurityUtils.getCurrentUserId()+"");
        permission.setCreateTime(LocalDateTime.now());
        
        // 保存权限
        save(permission);
        
        log.info("创建权限成功: {}", permission.getPermissionCode());
        return permission;
    }

    @Override
    @Transactional
    public Permission createPermission(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setPermissionName(request.getPermissionName());
        permission.setPermissionCode(request.getPermissionCode());
        permission.setPermissionType(request.getPermissionType());
        permission.setResourceUrl(request.getResourceUrl());
        permission.setParentId(request.getParentId());
        permission.setSortOrder(request.getSortOrder());
        permission.setDescription(request.getDescription());
        
        return createPermission(permission);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", key = "#permission.id")
    public Permission updatePermission(Permission permission) {
        // 检查权限是否存在
        Permission existingPermission = getById(permission.getId());
        if (existingPermission == null) {
            throw new BusinessException("权限不存在");
        }
        
        // 验证权限编码唯一性
        if (permissionMapper.existsByCode(permission.getPermissionCode(), permission.getId())) {
            throw new BusinessException("权限编码已存在");
        }
        
        // 设置更新信息
        permission.setUpdateBy(SecurityUtils.getCurrentUserId()+"");
        permission.setUpdateTime(LocalDateTime.now());
        
        // 更新权限
        updateById(permission);
        
        log.info("更新权限成功: {}", permission.getPermissionCode());
        return permission;
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", key = "#permissionId")
    public void deletePermission(Long permissionId) {
        // 检查权限是否存在
        Permission permission = getById(permissionId);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        
        // 检查是否有子权限
        List<Permission> children = permissionMapper.selectChildrenPermissions(permissionId);
        if (!CollectionUtils.isEmpty(children)) {
            throw new BusinessException("存在子权限，无法删除");
        }
        
        // 软删除权限
        permission.setDeleted(true);
        permission.setUpdateBy(SecurityUtils.getCurrentUserId()+"");
        permission.setUpdateTime(LocalDateTime.now());
        updateById(permission);
        
        log.info("删除权限成功: {}", permission.getPermissionCode());
    }

    @Override
    @Cacheable(value = "permission", key = "#permissionId")
    public Permission getPermissionById(Long permissionId) {
        return getById(permissionId);
    }

    @Override
    public Permission getPermissionByCode(String permissionCode) {
        return permissionMapper.selectByCode(permissionCode);
    }

    @Override
    public IPage<Permission> getPermissions(String permissionName, String permissionCode, String permissionType, Integer status, Page<Permission> page) {
        return null;
    }

    @Override
    public List<Permission> getPermissionTree() {
        return List.of();
    }

    @Override
    public List<Permission> getUserPermissions(Long userId) {
        return permissionMapper.selectUserPermissions(userId, null);
    }

    @Override
    public List<Permission> getUserPermissions(Long userId, String resourceType) {
        return permissionMapper.selectUserPermissions(userId, resourceType);
    }

    @Override
    public Set<String> getUserPermissionCodes(Long userId) {
        List<String> codes = permissionMapper.selectUserPermissionCodes(userId, null);
        return new HashSet<>(codes);
    }

    @Override
    public List<String> getUserPermissionCodes(Long userId, String resourceType) {
        return permissionMapper.selectUserPermissionCodes(userId, resourceType);
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId) {
        return permissionMapper.selectRolePermissions(roleId, null);
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId, String resourceType) {
        return permissionMapper.selectRolePermissions(roleId, resourceType);
    }

    @Override
    public Set<String> getRolePermissionCodes(Long roleId) {
        List<String> codes = permissionMapper.selectRolePermissionCodes(roleId, null);
        return new HashSet<>(codes);
    }

    @Override
    public List<String> getRolePermissionCodes(Long roleId, String resourceType) {
        return permissionMapper.selectRolePermissionCodes(roleId, resourceType);
    }

    @Override
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {

    }

    @Override
    public void assignPermissionsToUser(Long userId, List<Long> permissionIds) {

    }

    @Override
    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {

    }

    @Override
    public void removePermissionsFromUser(Long userId, List<Long> permissionIds) {

    }

    @Override
    public IPage<Permission> getPermissions(PermissionRequest request, Page<Permission> page) {
        return permissionMapper.selectPermissionPage(page, request);
    }

    @Override
    public List<Permission> getPermissionTree(String resourceType, boolean onlyEnabled) {
        List<Permission> permissions = permissionMapper.selectPermissionTree(resourceType, onlyEnabled);
        return buildPermissionTree(permissions);
    }

    @Override
    public List<Permission> getUserPermissions(Long userId, String resourceType) {
        return permissionMapper.selectUserPermissions(userId, resourceType);
    }

    @Override
    public List<String> getUserPermissionCodes(Long userId, String resourceType) {
        return permissionMapper.selectUserPermissionCodes(userId, resourceType);
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId, String resourceType) {
        return permissionMapper.selectRolePermissions(roleId, resourceType);
    }

    @Override
    public List<String> getRolePermissionCodes(Long roleId, String resourceType) {
        return permissionMapper.selectRolePermissionCodes(roleId, resourceType);
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        return permissionMapper.checkUserPermission(userId, permission);
    }

    @Override
    public boolean hasAllPermissions(Long userId, String[] permissions) {
        return permissionMapper.checkUserAllPermissions(userId, permissions);
    }

    @Override
    public boolean roleHasPermission(Long roleId, String permissionCode) {
        return false;
    }

    @Override
    public List<Permission> getUserMenuPermissions(Long userId) {
        return List.of();
    }

    @Override
    public List<Permission> getUserButtonPermissions(Long userId) {
        return List.of();
    }

    @Override
    public List<Permission> getUserApiPermissions(Long userId) {
        return List.of();
    }

    @Override
    public List<Permission> getUserDataPermissions(Long userId) {
        return List.of();
    }

    @Override
    public List<Permission> getUserFieldPermissions(Long userId) {
        return List.of();
    }

    @Override
    public void refreshUserPermissionCache(Long userId) {

    }

    @Override
    public void refreshRolePermissionCache(Long roleId) {

    }

    @Override
    public void clearAllPermissionCache() {

    }

    @Override
    public boolean hasAnyPermission(Long userId, String[] permissions) {
        return permissionMapper.checkUserAnyPermission(userId, permissions);
    }

    @Override
    public Map<String, Boolean> checkUserPermissions(Long userId, List<String> permissionCodes) {
        Map<String, Boolean> result = new HashMap<>();
        for (String code : permissionCodes) {
            result.put(code, hasPermission(userId, code));
        }
        return result;
    }

    @Override
    @Transactional
    public void enablePermissions(List<Long> permissionIds) {
        batchEnablePermissions(permissionIds);
    }

    @Override
    @Transactional
    public void disablePermissions(List<Long> permissionIds) {
        batchDisablePermissions(permissionIds);
    }

    @Override
    public Map<String, Boolean> checkRolePermissions(Long roleId, List<String> permissionCodes) {
        Map<String, Boolean> result = new HashMap<>();
        for (String code : permissionCodes) {
            result.put(code, roleHasPermission(roleId, code));
        }
        return result;
    }

    @Override
    public Map<String, Object> getPermissionStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", count());
        stats.put("enabledCount", lambdaQuery().eq(Permission::getStatus, 1).count());
        stats.put("disabledCount", lambdaQuery().eq(Permission::getStatus, 0).count());
        return stats;
    }

    @Override
    public Map<String, Object> getUserPermissionStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        List<String> userPermissions = getUserPermissionCodes(userId);
        stats.put("totalCount", userPermissions.size());
        stats.put("menuCount", getUserMenuPermissions(userId).size());
        stats.put("buttonCount", getUserButtonPermissions(userId).size());
        stats.put("apiCount", getUserApiPermissions(userId).size());
        return stats;
    }

    @Override
    public Map<String, Object> getRolePermissionStatistics(Long roleId) {
        Map<String, Object> stats = new HashMap<>();
        List<String> rolePermissions = getRolePermissionCodes(roleId);
        stats.put("totalCount", rolePermissions.size());
        return stats;
    }

    @Override
    @Transactional
    public void assignPermissionToUser(Long userId, Long permissionId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = permissionMapper.assignPermissionToUser(userId, permissionId, currentUserId);
        if (result == 0) {
            throw new BusinessException("分配权限失败");
        }
        log.info("为用户 {} 分配权限 {} 成功", userId, permissionId);
    }

    @Override
    @Transactional
    public void removePermissionFromUser(Long userId, Long permissionId) {
        int result = permissionMapper.removePermissionFromUser(userId, permissionId);
        if (result == 0) {
            throw new BusinessException("移除权限失败");
        }
        log.info("移除用户 {} 的权限 {} 成功", userId, permissionId);
    }

    @Override
    @Transactional
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = permissionMapper.assignPermissionToRole(roleId, permissionId, currentUserId);
        if (result == 0) {
            throw new BusinessException("分配权限失败");
        }
        log.info("为角色 {} 分配权限 {} 成功", roleId, permissionId);
    }

    @Override
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        int result = permissionMapper.removePermissionFromRole(roleId, permissionId);
        if (result == 0) {
            throw new BusinessException("移除权限失败");
        }
        log.info("移除角色 {} 的权限 {} 成功", roleId, permissionId);
    }

    @Override
    @Transactional
    public void enablePermission(Long permissionId) {
        Permission permission = getById(permissionId);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        
        permission.setStatus(1);
        permission.setUpdateBy(SecurityUtils.getCurrentUserId());
        permission.setUpdateTime(LocalDateTime.now());
        updateById(permission);
        
        log.info("启用权限成功: {}", permission.getPermissionCode());
    }

    @Override
    @Transactional
    public void disablePermission(Long permissionId) {
        Permission permission = getById(permissionId);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        
        permission.setStatus(0);
        permission.setUpdateBy(SecurityUtils.getCurrentUserId());
        permission.setUpdateTime(LocalDateTime.now());
        updateById(permission);
        
        log.info("禁用权限成功: {}", permission.getPermissionCode());
    }

    @Override
    @Transactional
    public void batchEnablePermissions(List<Long> permissionIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = permissionMapper.batchEnable(permissionIds, currentUserId);
        log.info("批量启用权限成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDisablePermissions(List<Long> permissionIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = permissionMapper.batchDisable(permissionIds, currentUserId);
        log.info("批量禁用权限成功，影响行数: {}", result);
    }

    @Override
    public List<Permission> getChildPermissions(Long parentId) {
        return List.of();
    }

    @Override
    public List<Permission> getDescendantPermissions(Long parentId) {
        return List.of();
    }

    @Override
    public boolean existsByPermissionCode(String permissionCode) {
        return false;
    }

    @Override
    public boolean existsByPermissionCodeAndIdNot(String permissionCode, Long excludeId) {
        return false;
    }

    @Override
    public IPage<Permission> getPermissionPage(PermissionRequest query, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    @Transactional
    public void batchDeletePermissions(List<Long> permissionIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = permissionMapper.batchDelete(permissionIds, currentUserId);
        log.info("批量删除权限成功，影响行数: {}", result);
    }

    @Override
    public boolean existsByCode(String permissionCode) {
        return permissionMapper.existsByCode(permissionCode, null);
    }

    @Override
    public List<Permission> getAllPermissions(String resourceType) {
        return permissionMapper.selectAllEnabled(resourceType);
    }

    @Override
    @CacheEvict(value = "permission", allEntries = true)
    public void refreshCache() {
        log.info("刷新权限缓存成功");
    }

    @Override
    @CacheEvict(value = "permission", allEntries = true)
    public void clearCache() {
        log.info("清空权限缓存成功");
    }

    /**
     * 构建权限树
     */
    private List<Permission> buildPermissionTree(List<Permission> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return new ArrayList<>();
        }
        
        // 按父ID分组
        Map<Long, List<Permission>> permissionMap = permissions.stream()
            .collect(Collectors.groupingBy(p -> p.getParentId() == null ? 0L : p.getParentId()));
        
        // 构建树形结构
        return buildTree(permissionMap, 0L);
    }
    
    /**
     * 递归构建树
     */
    private List<Permission> buildTree(Map<Long, List<Permission>> permissionMap, Long parentId) {
        List<Permission> children = permissionMap.get(parentId);
        if (CollectionUtils.isEmpty(children)) {
            return new ArrayList<>();
        }
        
        return children.stream()
            .peek(permission -> {
                List<Permission> subChildren = buildTree(permissionMap, permission.getId());
                permission.setChildren(subChildren);
            })
            .sorted(Comparator.comparing(Permission::getSortOrder, Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toList());
    }
}