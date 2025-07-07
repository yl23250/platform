package com.biobt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.user.domain.dto.RoleRequest;
import com.biobt.user.domain.entity.Role;
import com.biobt.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Set;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 创建角色
     */
    Role createRole(RoleRequest role);

    /**
     * 更新角色
     */
    Role updateRole(RoleRequest role, Long roleId);

    /**
     * 删除角色
     */
    void deleteRole(Long roleId);
    void deleteRoles(List<Long> roleIds);
    
    /**
     * 根据ID获取角色
     */
    Role getRoleById(Long roleId);

    /**
     * 根据角色编码获取角色
     */
    Role getRoleByCode(String roleCode);

    /**
     * 分页查询角色
     */
    IPage<Role> getRoles(String roleName, String roleCode, String roleType, Integer status, Page<Role> page);

    /**
     * 获取所有角色
     */
    List<Role> getAllRoles();

    /**
     * 获取启用的角色
     */
    List<Role> getEnabledRoles();

    /**
     * 获取用户角色列表
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 获取用户角色编码集合
     */
    Set<String> getUserRoleCodes(Long userId);

    /**
     * 为用户分配角色
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * 移除用户角色
     */
    void removeRolesFromUser(Long userId, List<Long> roleIds);

    /**
     * 设置用户角色（覆盖原有角色）
     */
    void setUserRoles(Long userId, List<Long> roleIds);

    /**
     * 检查用户是否有指定角色
     */
    boolean hasRole(Long userId, String roleCode);

    /**
     * 检查用户是否有指定角色（支持多个角色码）
     */
    boolean hasAnyRole(Long userId, String... roleCodes);

    /**
     * 检查用户是否有所有指定角色
     */
    boolean hasAllRoles(Long userId, String... roleCodes);

    /**
     * 获取角色的用户列表
     */
    List<User> getRoleUsers(Long roleId);

    /**
     * 获取角色的用户数量
     */
    long getRoleUserCount(Long roleId);

    /**
     * 启用角色
     */
    void enableRole(Long roleId);

    /**
     * 禁用角色
     */
    void disableRole(Long roleId);

    /**
     * 批量启用角色
     */
    void batchEnableRoles(List<Long> roleIds);

    /**
     * 批量禁用角色
     */
    void batchDisableRoles(List<Long> roleIds);

    /**
     * 检查角色编码是否存在
     */
    boolean existsByRoleCode(String roleCode);

    /**
     * 检查角色编码是否存在（排除指定ID）
     */
    boolean existsByRoleCodeAndIdNot(String roleCode, Long excludeId);

    /**
     * 获取默认角色
     */
    List<Role> getDefaultRoles();

    /**
     * 设置默认角色
     */
    void setDefaultRole(Long roleId, boolean isDefault);

    /**
     * 获取系统角色
     */
    List<Role> getSystemRoles();

    /**
     * 获取业务角色
     */
    List<Role> getBusinessRoles();

    /**
     * 获取自定义角色
     */
    List<Role> getCustomRoles();

    /**
     * 复制角色
     */
    Role copyRole(Long sourceRoleId, String newRoleCode, String newRoleName);

    /**
     * 刷新角色缓存
     */
    void refreshRoleCache(Long roleId);

    /**
     * 清空所有角色缓存
     */
    void clearAllRoleCache();

    /**
     * 获取角色层级关系
     */
    List<Role> getRoleHierarchy();

    /**
     * 检查角色是否可以删除
     */
    boolean canDeleteRole(Long roleId);

    /**
     * 获取用户有效角色（考虑时间范围）
     */
    List<Role> getUserEffectiveRoles(Long userId);

    /**
     * 批量分配角色给用户
     */
    void batchAssignRolesToUsers(List<Long> userIds, List<Long> roleIds);

    /**
     * 批量移除用户角色
     */
    void batchRemoveRolesFromUsers(List<Long> userIds, List<Long> roleIds);

    /**
     * 分页查询角色
     */
    IPage<Role> getRolePage(RoleRequest query, long pageNum, int pageSize);

    /**
     * 分页查询角色用户
     */
    IPage<User> getRoleUserPage(Long roleId, int pageNum, int pageSize);
}