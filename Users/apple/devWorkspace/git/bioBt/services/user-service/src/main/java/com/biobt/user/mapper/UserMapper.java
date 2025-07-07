package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Mapper接口
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户（包含已删除）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND tenant_id = #{tenantId}")
    User selectByUsernameIncludeDeleted(@Param("username") String username, @Param("tenantId") Long tenantId);

    /**
     * 根据邮箱查询用户（包含已删除）
     *
     * @param email 邮箱
     * @param tenantId 租户ID
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE email = #{email} AND tenant_id = #{tenantId}")
    User selectByEmailIncludeDeleted(@Param("email") String email, @Param("tenantId") Long tenantId);

    /**
     * 根据手机号查询用户（包含已删除）
     *
     * @param phone 手机号
     * @param tenantId 租户ID
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE phone = #{phone} AND tenant_id = #{tenantId}")
    User selectByPhoneIncludeDeleted(@Param("phone") String phone, @Param("tenantId") Long tenantId);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param tenantId 租户ID
     * @param username 用户名（模糊查询）
     * @param email 邮箱（模糊查询）
     * @param phone 手机号（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param status 状态
     * @param isLocked 是否锁定
     * @param startTime 创建开始时间
     * @param endTime 创建结束时间
     * @return 用户分页列表
     */
    IPage<User> selectUserPage(
            Page<User> page,
            @Param("tenantId") Long tenantId,
            @Param("username") String username,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("realName") String realName,
            @Param("status") Integer status,
            @Param("isLocked") Integer isLocked,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 批量软删除用户
     *
     * @param userIds 用户ID列表
     * @param updatedBy 更新人
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE sys_user SET deleted = 1, updated_time = NOW(), updated_by = #{updatedBy} " +
            "WHERE id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND deleted = 0" +
            "</script>")
    int batchSoftDelete(@Param("userIds") List<Long> userIds, @Param("updatedBy") Long updatedBy);

    /**
     * 批量启用用户
     *
     * @param userIds 用户ID列表
     * @param updatedBy 更新人
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE sys_user SET status = 1, updated_time = NOW(), updated_by = #{updatedBy} " +
            "WHERE id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND deleted = 0" +
            "</script>")
    int batchEnable(@Param("userIds") List<Long> userIds, @Param("updatedBy") Long updatedBy);

    /**
     * 批量禁用用户
     *
     * @param userIds 用户ID列表
     * @param updatedBy 更新人
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE sys_user SET status = 0, updated_time = NOW(), updated_by = #{updatedBy} " +
            "WHERE id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND deleted = 0" +
            "</script>")
    int batchDisable(@Param("userIds") List<Long> userIds, @Param("updatedBy") Long updatedBy);

    /**
     * 批量锁定用户
     *
     * @param userIds 用户ID列表
     * @param updatedBy 更新人
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE sys_user SET is_locked = 1, lock_time = NOW(), updated_time = NOW(), updated_by = #{updatedBy} " +
            "WHERE id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND deleted = 0" +
            "</script>")
    int batchLock(@Param("userIds") List<Long> userIds, @Param("updatedBy") Long updatedBy);

    /**
     * 批量解锁用户
     *
     * @param userIds 用户ID列表
     * @param updatedBy 更新人
     * @return 影响行数
     */
    @Update("<script>" +
            "UPDATE sys_user SET is_locked = 0, lock_time = NULL, updated_time = NOW(), updated_by = #{updatedBy} " +
            "WHERE id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "AND deleted = 0" +
            "</script>")
    int batchUnlock(@Param("userIds") List<Long> userIds, @Param("updatedBy") Long updatedBy);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @param loginTime 登录时间
     * @return 影响行数
     */
    @Update("UPDATE sys_user SET last_login_time = #{loginTime}, last_login_ip = #{loginIp}, " +
            "login_count = login_count + 1, updated_time = NOW() " +
            "WHERE id = #{userId} AND deleted = 0")
    int updateLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp, @Param("loginTime") LocalDateTime loginTime);

    /**
     * 更新用户密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     * @param updatedBy 更新人
     * @return 影响行数
     */
    @Update("UPDATE sys_user SET password = #{newPassword}, password_update_time = NOW(), " +
            "updated_time = NOW(), updated_by = #{updatedBy}, version = version + 1 " +
            "WHERE id = #{userId} AND deleted = 0")
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword, @Param("updatedBy") Long updatedBy);

    /**
     * 根据租户ID统计用户数量
     *
     * @param tenantId 租户ID
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE tenant_id = #{tenantId} AND deleted = 0")
    Long countByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 根据状态统计用户数量
     *
     * @param tenantId 租户ID
     * @param status 状态
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE tenant_id = #{tenantId} AND status = #{status} AND deleted = 0")
    Long countByStatus(@Param("tenantId") Long tenantId, @Param("status") Integer status);

    /**
     * 根据锁定状态统计用户数量
     *
     * @param tenantId 租户ID
     * @param isLocked 是否锁定
     * @return 用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE tenant_id = #{tenantId} AND is_locked = #{isLocked} AND deleted = 0")
    Long countByLockStatus(@Param("tenantId") Long tenantId, @Param("isLocked") Integer isLocked);

    /**
     * 查询最近登录的用户列表
     *
     * @param tenantId 租户ID
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM sys_user WHERE tenant_id = #{tenantId} AND deleted = 0 " +
            "AND last_login_time IS NOT NULL " +
            "ORDER BY last_login_time DESC LIMIT #{limit}")
    List<User> selectRecentLoginUsers(@Param("tenantId") Long tenantId, @Param("limit") Integer limit);

    /**
     * 查询长时间未登录的用户列表
     *
     * @param tenantId 租户ID
     * @param days 天数
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM sys_user WHERE tenant_id = #{tenantId} AND deleted = 0 " +
            "AND (last_login_time IS NULL OR last_login_time < DATE_SUB(NOW(), INTERVAL #{days} DAY)) " +
            "ORDER BY created_time DESC LIMIT #{limit}")
    List<User> selectLongTimeNoLoginUsers(@Param("tenantId") Long tenantId, @Param("days") Integer days, @Param("limit") Integer limit);

    /**
     * 查询用户及其角色信息
     *
     * @param userId 用户ID
     * @return 用户信息（包含角色）
     */
    User selectUserWithRoles(@Param("userId") Long userId);

    /**
     * 查询用户的权限列表
     *
     * @param userId 用户ID
     * @return 权限代码列表
     */
    List<String> selectUserPermissions(@Param("userId") Long userId);
}