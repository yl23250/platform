package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.UserPageRequest;
import com.biobt.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户数据访问层
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户（包括已删除的）
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsernameIncludeDeleted(@Param("username") String username);

    /**
     * 根据邮箱查询用户（包括已删除的）
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmailIncludeDeleted(@Param("email") String email);

    /**
     * 根据手机号查询用户（包括已删除的）
     *
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhoneIncludeDeleted(@Param("phone") String phone);

    /**
     * 分页查询用户列表
     *
     * @param page    分页参数
     * @param request 查询条件
     * @return 用户分页列表
     */
    IPage<User> selectUserPage(Page<User> page, @Param("request") UserPageRequest request);

    /**
     * 查询用户详情（包含角色信息）
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    Map<String, Object> selectUserWithRoles(@Param("userId") Long userId);

    /**
     * 查询用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectUserPermissions(@Param("userId") Long userId);

    /**
     * 批量软删除用户
     *
     * @param ids      用户ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchSoftDelete(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量启用用户
     *
     * @param ids      用户ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchEnable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量禁用用户
     *
     * @param ids      用户ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDisable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量锁定用户
     *
     * @param ids      用户ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchLock(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量解锁用户
     *
     * @param ids      用户ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchUnlock(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 更新用户登录信息
     *
     * @param userId        用户ID
     * @param lastLoginTime 最后登录时间
     * @param lastLoginIp   最后登录IP
     * @param loginCount    登录次数
     * @return 影响行数
     */
    int updateLoginInfo(@Param("userId") Long userId,
                       @Param("lastLoginTime") LocalDateTime lastLoginTime,
                       @Param("lastLoginIp") String lastLoginIp,
                       @Param("loginCount") Integer loginCount);

    /**
     * 更新用户密码
     *
     * @param userId              用户ID
     * @param password            新密码
     * @param passwordUpdateTime  密码更新时间
     * @param updateBy           更新者
     * @return 影响行数
     */
    int updatePassword(@Param("userId") Long userId,
                      @Param("password") String password,
                      @Param("passwordUpdateTime") LocalDateTime passwordUpdateTime,
                      @Param("updateBy") Long updateBy);

    /**
     * 统计租户用户数量
     *
     * @param tenantId 租户ID
     * @return 用户数量
     */
    Long countByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 统计指定状态的用户数量
     *
     * @param status 用户状态
     * @return 用户数量
     */
    Long countByStatus(@Param("status") Integer status);

    /**
     * 统计锁定状态的用户数量
     *
     * @param lockStatus 锁定状态
     * @return 用户数量
     */
    Long countByLockStatus(@Param("lockStatus") Integer lockStatus);

    /**
     * 统计今日注册用户数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户数量
     */
    Long countTodayRegistrations(@Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime);

    /**
     * 统计本月注册用户数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户数量
     */
    Long countMonthlyRegistrations(@Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最近登录的用户
     *
     * @param days  天数
     * @param limit 限制数量
     * @return 用户列表
     */
    List<User> selectRecentLoginUsers(@Param("days") int days, @Param("limit") int limit);

    /**
     * 查询长时间未登录的用户
     *
     * @param days  天数
     * @param limit 限制数量
     * @return 用户列表
     */
    List<User> selectLongTimeNoLoginUsers(@Param("days") int days, @Param("limit") int limit);

    /**
     * 查询用户登录趋势数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 登录趋势数据
     */
    List<Map<String, Object>> selectLoginTrend(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户注册趋势数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势数据
     */
    List<Map<String, Object>> selectRegistrationTrend(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 查询活跃用户排行
     *
     * @param limit 限制数量
     * @return 活跃用户排行
     */
    List<Map<String, Object>> selectActiveUserRanking(@Param("limit") int limit);

    /**
     * 批量插入用户
     *
     * @param users 用户列表
     * @return 影响行数
     */
    int batchInsert(@Param("users") List<User> users);

    /**
     * 批量更新用户状态
     *
     * @param ids      用户ID列表
     * @param status   状态
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids,
                         @Param("status") Integer status,
                         @Param("updateBy") Long updateBy);

    /**
     * 软删除过期用户
     *
     * @param expireTime 过期时间
     * @param updateBy   更新者
     * @return 影响行数
     */
    int softDeleteExpiredUsers(@Param("expireTime") LocalDateTime expireTime,
                              @Param("updateBy") Long updateBy);

    /**
     * 清理用户敏感信息
     *
     * @param userIds 用户ID列表
     * @return 影响行数
     */
    int cleanSensitiveInfo(@Param("userIds") List<Long> userIds);
}