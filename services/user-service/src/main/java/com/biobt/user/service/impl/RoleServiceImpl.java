package com.biobt.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biobt.common.core.exception.BusinessException;
import com.biobt.common.core.utils.SecurityUtils;
import com.biobt.user.domain.dto.RoleRequest;
import com.biobt.user.domain.entity.Role;
import com.biobt.user.domain.entity.User;
import com.biobt.user.mapper.RoleMapper;
import com.biobt.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色服务实现类
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public Role createRole(RoleRequest roleRequest) {
        // 验证角色编码唯一性
        if (roleMapper.existsByCode(roleRequest.getRoleCode(), null)) {
            throw new BusinessException("角色编码已存在");
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleRequest, role);
        // 设置创建信息
        role.setCreateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        role.setCreateTime(LocalDateTime.now());
        
        // 保存角色
        save(role);
        
        log.info("创建角色成功: {}", role.getRoleCode());
        return role;
    }

    @Override
    @Transactional
    @CacheEvict(value = "role", key = "#role.id")
    public Role updateRole(RoleRequest roleRequest, Long roleId) {
        // 检查角色是否存在
        Role existingRole = getById(roleId);
        if (existingRole == null) {
            throw new BusinessException("角色不存在");
        }
        
        // 验证角色编码唯一性
        if (roleMapper.existsByCode(roleRequest.getRoleCode(), roleId)) {
            throw new BusinessException("角色编码已存在");
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleRequest, role);
        // 设置更新信息
        role.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        role.setUpdateTime(LocalDateTime.now());

        // 更新角色
        updateById(role);
        
        log.info("更新角色成功: {}", role.getRoleCode());
        return role;
    }

    @Override
    @Transactional
    @CacheEvict(value = "role", key = "#roleId")
    public void deleteRole(Long roleId) {
        // 检查角色是否存在
        Role role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        // 检查角色是否可以删除
        if (!roleMapper.canDelete(roleId)) {
            throw new BusinessException("角色正在使用中，无法删除");
        }
        
        // 软删除角色
        role.setDeleted(true);
        role.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        
        log.info("删除角色成功: {}", role.getRoleCode());
    }

    @Override
    public void deleteRoles(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            deleteRole(roleId);
        }

    }

    @Override
    @Cacheable(value = "role", key = "#roleId")
    public Role getRoleById(Long roleId) {
        return getById(roleId);
    }

    @Override
    public Role getRoleByCode(String roleCode) {
        return roleMapper.selectByCode(roleCode);
    }

    @Override
    public IPage<Role> getRoles(String roleName, String roleCode, String roleType, Integer status, Page<Role> page) {
        // 创建查询请求对象
        RoleRequest request = new RoleRequest();
        request.setRoleName(roleName);
        request.setRoleCode(roleCode);
        request.setRoleType(roleType);
        request.setStatus(status);
        return roleMapper.selectRolePage(page, request);
    }

    @Override
    public List<Role> getAllRoles() {
        return list();
    }

    @Override
    public List<Role> getEnabledRoles() {
        return roleMapper.selectAllEnabled();
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        return roleMapper.selectUserRoles(userId);
    }

    @Override
    public Set<String> getUserRoleCodes(Long userId) {
        List<String> roleCodes = roleMapper.selectUserRoleCodes(userId);
        return new HashSet<>(roleCodes);
    }

    @Override
    public List<User> getRoleUsers(Long roleId) {
        return roleMapper.selectRoleUsers(roleId);
    }

    @Override
    public List<Role> getDefaultRoles() {
        return roleMapper.selectDefaultRoles();
    }

    @Override
    public List<Role> getSystemRoles() {
        return roleMapper.selectSystemRoles();
    }

    @Override
    public List<Role> getBusinessRoles() {
        return roleMapper.selectBusinessRoles();
    }

    @Override
    public List<Role> getCustomRoles() {
        return roleMapper.selectCustomRoles();
    }

    @Override
    public List<Role> getRoleHierarchy() {
        return roleMapper.selectRoleHierarchy(null);
    }

    @Override
    public boolean hasRole(Long userId, String roleCode) {
        return roleMapper.checkUserRole(userId, roleCode);
    }

    @Override
    public boolean hasAllRoles(Long userId, String[] roleCodes) {
        return roleMapper.checkUserAllRoles(userId, roleCodes);
    }

    @Override
    public boolean hasAnyRole(Long userId, String[] roleCodes) {
        return roleMapper.checkUserAnyRole(userId, roleCodes);
    }

    @Override
    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int totalResult = 0;
        for (Long roleId : roleIds) {
            totalResult += roleMapper.assignRoleToUser(userId, roleId, currentUserId);
        }
        log.info("为用户 {} 分配角色成功，影响行数: {}", userId, totalResult);
    }

    @Override
    @Transactional
    public void removeRolesFromUser(Long userId, List<Long> roleIds) {
        int totalResult = 0;
        for (Long roleId : roleIds) {
            totalResult += roleMapper.removeRoleFromUser(userId, roleId);
        }
        log.info("移除用户 {} 的角色成功，影响行数: {}", userId, totalResult);
    }

    @Override
    @Transactional
    public void setUserRoles(Long userId, List<Long> roleIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = roleMapper.setUserRoles(userId, roleIds, currentUserId);
        log.info("设置用户 {} 的角色成功，影响行数: {}", userId, result);
    }

    @Override
    @Transactional
    public void batchAssignRolesToUsers(List<Long> userIds, List<Long> roleIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int totalResult = 0;
        for (Long roleId : roleIds) {
            totalResult += roleMapper.batchAssignRoleToUsers(userIds, roleId, currentUserId);
        }
        log.info("批量为用户分配角色成功，影响行数: {}", totalResult);
    }

    @Override
    @Transactional
    public void batchRemoveRoleFromUsers(List<Long> userIds, Long roleId) {
        int result = roleMapper.batchRemoveRoleFromUsers(userIds, roleId);
        log.info("批量移除用户的角色成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchRemoveRolesFromUsers(List<Long> userIds, List<Long> roleIds) {
        int totalResult = 0;
        for (Long roleId : roleIds) {
            totalResult += roleMapper.batchRemoveRoleFromUsers(userIds, roleId);
        }
        log.info("批量移除用户的多个角色成功，影响行数: {}", totalResult);
    }

    @Override
    public IPage<Role> getRolePage(RoleRequest query, long l, int pageSize) {
        return null;
    }

    @Override
    public IPage<User> getRoleUserPage(Long roleId, int pageNum, int pageSize) {
        return null;
    }

    @Override
    @Transactional
    public void enableRole(Long roleId) {
        Role role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        role.setStatus(1);
        role.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        
        log.info("启用角色成功: {}", role.getRoleCode());
    }

    @Override
    @Transactional
    public void disableRole(Long roleId) {
        Role role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        role.setStatus(0);
        role.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        
        log.info("禁用角色成功: {}", role.getRoleCode());
    }

    @Override
    @Transactional
    public void batchEnableRoles(List<Long> roleIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = roleMapper.batchEnable(roleIds, currentUserId);
        log.info("批量启用角色成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDisableRoles(List<Long> roleIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = roleMapper.batchDisable(roleIds, currentUserId);
        log.info("批量禁用角色成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDeleteRoles(List<Long> roleIds) {
        // 检查角色是否可以删除
        for (Long roleId : roleIds) {
            if (!roleMapper.canDelete(roleId)) {
                Role role = getById(roleId);
                throw new BusinessException("角色 " + (role != null ? role.getRoleName() : roleId) + " 正在使用中，无法删除");
            }
        }
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = roleMapper.batchDelete(roleIds, currentUserId);
        log.info("批量删除角色成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public Role copyRole(Long sourceRoleId, String newRoleCode, String newRoleName) {
        // 检查源角色是否存在
        Role sourceRole = getById(sourceRoleId);
        if (sourceRole == null) {
            throw new BusinessException("源角色不存在");
        }
        
        // 验证新角色编码唯一性
        if (roleMapper.existsByCode(newRoleCode, null)) {
            throw new BusinessException("角色编码已存在");
        }
        
        // 创建新角色
        Role newRole = new Role();
        newRole.setRoleCode(newRoleCode);
        newRole.setRoleName(newRoleName);
        newRole.setDescription(sourceRole.getDescription());
        newRole.setRoleType(sourceRole.getRoleType());
        newRole.setStatus(sourceRole.getStatus());
        newRole.setSortOrder(sourceRole.getSortOrder());
        newRole.setIsDefault(false); // 复制的角色默认不是默认角色
        newRole.setRoleLevel(sourceRole.getRoleLevel());
        newRole.setDataScope(sourceRole.getDataScope());
        newRole.setDataScopeDepts(sourceRole.getDataScopeDepts());
        newRole.setEffectiveTime(sourceRole.getEffectiveTime());
        newRole.setExpireTime(sourceRole.getExpireTime());
        newRole.setExtraInfo(sourceRole.getExtraInfo());
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Long newRoleId = roleMapper.copyRole(sourceRoleId, newRole, currentUserId);
        newRole.setId(newRoleId);
        
        log.info("复制角色成功: {} -> {}", sourceRole.getRoleCode(), newRoleCode);
        return newRole;
    }

    @Override
    public boolean existsByCode(String roleCode) {
        return roleMapper.existsByCode(roleCode, null);
    }

    @Override
    public boolean existsByRoleCode(String roleCode) {
        return roleMapper.existsByCode(roleCode, null);
    }

    @Override
    public boolean existsByRoleCodeAndIdNot(String roleCode, Long excludeId) {
        return roleMapper.existsByCode(roleCode, excludeId);
    }

    @Override
    public long getRoleUserCount(Long roleId) {
        return roleMapper.countRoleUsers(roleId);
    }

    @Override
    @Transactional
    public void setDefaultRole(Long roleId, boolean isDefault) {
        Role role = getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        
        role.setIsDefault(isDefault ? 1 : 0);
        role.setUpdateBy(String.valueOf(SecurityUtils.getCurrentUserId()));
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
        
        log.info("设置角色默认状态成功: {} - {}", role.getRoleCode(), isDefault);
    }

    @Override
    @CacheEvict(value = "role", key = "#roleId")
    public void refreshRoleCache(Long roleId) {
        log.info("刷新角色缓存成功: {}", roleId);
    }

    @Override
    @CacheEvict(value = "role", allEntries = true)
    public void clearAllRoleCache() {
        log.info("清空所有角色缓存成功");
    }

    @Override
    public boolean canDelete(Long roleId) {
        return roleMapper.canDelete(roleId);
    }

    @Override
    public boolean canDeleteRole(Long roleId) {
        return roleMapper.canDelete(roleId);
    }

    @Override
    public List<Role> getUserEffectiveRoles(Long userId) {
        return roleMapper.selectUserEffectiveRoles(userId);
    }
}