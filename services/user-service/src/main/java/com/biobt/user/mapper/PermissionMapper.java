package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.PermissionRequest;
import com.biobt.user.domain.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限数据访问层
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    Permission selectByCode(@Param("permissionCode") String permissionCode);

    /**
     * 分页查询权限列表
     *
     * @param page    分页参数
     * @param request 查询条件
     * @return 权限分页列表
     */
    IPage<Permission> selectPermissionPage(Page<Permission> page, @Param("request") PermissionRequest request);

    /**
     * 查询权限树形结构
     *
     * @param resourceType 资源类型
     * @param onlyEnabled  是否只查询启用的
     * @return 权限树
     */
    List<Permission> selectPermissionTree(@Param("resourceType") String resourceType, @Param("onlyEnabled") boolean onlyEnabled);

    /**
     * 查询用户权限列表
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @return 权限列表
     */
    List<Permission> selectUserPermissions(@Param("userId") Long userId, @Param("resourceType") String resourceType);

    /**
     * 查询用户权限编码列表
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @return 权限编码列表
     */
    List<String> selectUserPermissionCodes(@Param("userId") Long userId, @Param("resourceType") String resourceType);

    /**
     * 查询角色权限列表
     *
     * @param roleId       角色ID
     * @param resourceType 资源类型
     * @return 权限列表
     */
    List<Permission> selectRolePermissions(@Param("roleId") Long roleId, @Param("resourceType") String resourceType);

    /**
     * 查询角色权限编码列表
     *
     * @param roleId       角色ID
     * @param resourceType 资源类型
     * @return 权限编码列表
     */
    List<String> selectRolePermissionCodes(@Param("roleId") Long roleId, @Param("resourceType") String resourceType);

    /**
     * 检查用户是否拥有权限
     *
     * @param userId     用户ID
     * @param permission 权限编码
     * @return 是否拥有权限
     */
    boolean checkUserPermission(@Param("userId") Long userId, @Param("permission") String permission);

    /**
     * 检查用户是否拥有所有权限
     *
     * @param userId      用户ID
     * @param permissions 权限编码列表
     * @return 是否拥有所有权限
     */
    boolean checkUserAllPermissions(@Param("userId") Long userId, @Param("permissions") String[] permissions);

    /**
     * 检查用户是否拥有任一权限
     *
     * @param userId      用户ID
     * @param permissions 权限编码列表
     * @return 是否拥有任一权限
     */
    boolean checkUserAnyPermission(@Param("userId") Long userId, @Param("permissions") String[] permissions);

    /**
     * 批量检查用户权限
     *
     * @param userId          用户ID
     * @param permissionCodes 权限编码列表
     * @return 权限检查结果
     */
    Map<String, Boolean> checkUserPermissions(@Param("userId") Long userId, @Param("permissionCodes") List<String> permissionCodes);

    /**
     * 为用户分配权限
     *
     * @param userId       用户ID
     * @param permissionId 权限ID
     * @param assignBy     分配者
     * @return 影响行数
     */
    int assignPermissionToUser(@Param("userId") Long userId, @Param("permissionId") Long permissionId, @Param("assignBy") Long assignBy);

    /**
     * 移除用户权限
     *
     * @param userId       用户ID
     * @param permissionId 权限ID
     * @return 影响行数
     */
    int removePermissionFromUser(@Param("userId") Long userId, @Param("permissionId") Long permissionId);

    /**
     * 为角色分配权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @param assignBy     分配者
     * @return 影响行数
     */
    int assignPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId, @Param("assignBy") Long assignBy);

    /**
     * 移除角色权限
     *
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 影响行数
     */
    int removePermissionFromRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 批量启用权限
     *
     * @param ids      权限ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchEnable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量禁用权限
     *
     * @param ids      权限ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDisable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量删除权限
     *
     * @param ids      权限ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 查询子权限列表
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> selectChildrenPermissions(@Param("parentId") Long parentId);

    /**
     * 检查权限编码是否存在
     *
     * @param permissionCode 权限编码
     * @param excludeId      排除的权限ID
     * @return 是否存在
     */
    boolean existsByCode(@Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);

    /**
     * 统计权限数量
     *
     * @param resourceType 资源类型
     * @param status       状态
     * @return 权限数量
     */
    Long countPermissions(@Param("resourceType") String resourceType, @Param("status") Integer status);

    /**
     * 查询所有启用的权限
     *
     * @param resourceType 资源类型
     * @return 权限列表
     */
    List<Permission> selectAllEnabled(@Param("resourceType") String resourceType);

    /**
     * 清理权限缓存
     *
     * @param permissionId 权限ID
     */
    void clearPermissionCache(@Param("permissionId") Long permissionId);

    /**
     * 刷新权限缓存
     *
     * @param permissionId 权限ID
     */
    void refreshPermissionCache(@Param("permissionId") Long permissionId);
}