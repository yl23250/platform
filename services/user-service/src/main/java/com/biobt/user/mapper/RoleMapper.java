package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.RoleRequest;
import com.biobt.user.domain.entity.Role;
import com.biobt.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色数据访问层
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role selectByCode(@Param("roleCode") String roleCode);

    /**
     * 分页查询角色列表
     *
     * @param page    分页参数
     * @param request 查询条件
     * @return 角色分页列表
     */
    IPage<Role> selectRolePage(Page<Role> page, @Param("request") RoleRequest request);

    /**
     * 查询用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectUserRoles(@Param("userId") Long userId);

    /**
     * 查询用户角色编码列表
     *
     * @param userId 用户ID
     * @return 角色编码列表
     */
    List<String> selectUserRoleCodes(@Param("userId") Long userId);

    /**
     * 查询角色用户列表
     *
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> selectRoleUsers(@Param("roleId") Long roleId);

    /**
     * 查询所有启用的角色
     *
     * @return 角色列表
     */
    List<Role> selectAllEnabled();

    /**
     * 查询默认角色列表
     *
     * @return 默认角色列表
     */
    List<Role> selectDefaultRoles();

    /**
     * 查询系统角色列表
     *
     * @return 系统角色列表
     */
    List<Role> selectSystemRoles();

    /**
     * 查询业务角色列表
     *
     * @return 业务角色列表
     */
    List<Role> selectBusinessRoles();

    /**
     * 查询自定义角色列表
     *
     * @return 自定义角色列表
     */
    List<Role> selectCustomRoles();

    /**
     * 查询角色层级结构
     *
     * @param parentId 父角色ID
     * @return 角色层级列表
     */
    List<Role> selectRoleHierarchy(@Param("parentId") Long parentId);

    /**
     * 检查用户是否拥有角色
     *
     * @param userId   用户ID
     * @param roleCode 角色编码
     * @return 是否拥有角色
     */
    boolean checkUserRole(@Param("userId") Long userId, @Param("roleCode") String roleCode);

    /**
     * 检查用户是否拥有所有角色
     *
     * @param userId    用户ID
     * @param roleCodes 角色编码列表
     * @return 是否拥有所有角色
     */
    boolean checkUserAllRoles(@Param("userId") Long userId, @Param("roleCodes") String[] roleCodes);

    /**
     * 检查用户是否拥有任一角色
     *
     * @param userId    用户ID
     * @param roleCodes 角色编码列表
     * @return 是否拥有任一角色
     */
    boolean checkUserAnyRole(@Param("userId") Long userId, @Param("roleCodes") String[] roleCodes);

    /**
     * 为用户分配角色
     *
     * @param userId   用户ID
     * @param roleId   角色ID
     * @param assignBy 分配者
     * @return 影响行数
     */
    int assignRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId, @Param("assignBy") Long assignBy);

    /**
     * 移除用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 影响行数
     */
    int removeRoleFromUser(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 设置用户角色（先清空再分配）
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @param setBy   设置者
     * @return 影响行数
     */
    int setUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds, @Param("setBy") Long setBy);

    /**
     * 批量为用户分配角色
     *
     * @param userIds  用户ID列表
     * @param roleId   角色ID
     * @param assignBy 分配者
     * @return 影响行数
     */
    int batchAssignRoleToUsers(@Param("userIds") List<Long> userIds, @Param("roleId") Long roleId, @Param("assignBy") Long assignBy);

    /**
     * 批量移除用户角色
     *
     * @param userIds 用户ID列表
     * @param roleId  角色ID
     * @return 影响行数
     */
    int batchRemoveRoleFromUsers(@Param("userIds") List<Long> userIds, @Param("roleId") Long roleId);

    /**
     * 批量启用角色
     *
     * @param ids      角色ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchEnable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量禁用角色
     *
     * @param ids      角色ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDisable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量删除角色
     *
     * @param ids      角色ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 复制角色
     *
     * @param sourceRoleId 源角色ID
     * @param targetRole   目标角色
     * @param copyBy       复制者
     * @return 新角色ID
     */
    Long copyRole(@Param("sourceRoleId") Long sourceRoleId, @Param("targetRole") Role targetRole, @Param("copyBy") Long copyBy);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode  角色编码
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsByCode(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);

    /**
     * 检查角色是否可以删除
     *
     * @param roleId 角色ID
     * @return 是否可以删除
     */
    boolean canDelete(@Param("roleId") Long roleId);

    /**
     * 获取用户有效角色
     *
     * @param userId 用户ID
     * @return 有效角色列表
     */
    List<Role> selectUserEffectiveRoles(@Param("userId") Long userId);

    /**
     * 统计角色数量
     *
     * @param roleType 角色类型
     * @param status   状态
     * @return 角色数量
     */
    Long countRoles(@Param("roleType") String roleType, @Param("status") Integer status);

    /**
     * 统计角色用户数量
     *
     * @param roleId 角色ID
     * @return 用户数量
     */
    Long countRoleUsers(@Param("roleId") Long roleId);

    /**
     * 清理角色缓存
     *
     * @param roleId 角色ID
     */
    void clearRoleCache(@Param("roleId") Long roleId);

    /**
     * 刷新角色缓存
     *
     * @param roleId 角色ID
     */
    void refreshRoleCache(@Param("roleId") Long roleId);
}