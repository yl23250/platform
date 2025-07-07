package com.biobt.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.user.dto.CreateUserRequest;
import com.biobt.user.dto.UpdateUserRequest;
import com.biobt.user.dto.UserPageRequest;
import com.biobt.user.dto.UserResponse;
import com.biobt.user.entity.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
public interface UserService extends IService<User> {

    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @return 用户响应
     */
    UserResponse createUser(CreateUserRequest request);

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户响应
     */
    UserResponse getUserById(Long id);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 用户响应
     */
    UserResponse getUserByUsername(String username, Long tenantId);

    /**
     * 获取当前用户信息
     *
     * @return 用户响应
     */
    UserResponse getCurrentUser();

    /**
     * 分页查询用户列表
     *
     * @param request 分页查询请求
     * @return 用户分页响应
     */
    IPage<UserResponse> getUserPage(UserPageRequest request);

    /**
     * 更新用户信息
     *
     * @param id 用户ID
     * @param request 更新用户请求
     * @return 用户响应
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /**
     * 软删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean deleteUser(Long id);

    /**
     * 批量软删除用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    Boolean batchDeleteUsers(List<Long> ids);

    /**
     * 启用用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean enableUser(Long id);

    /**
     * 禁用用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean disableUser(Long id);

    /**
     * 批量启用用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    Boolean batchEnableUsers(List<Long> ids);

    /**
     * 批量禁用用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    Boolean batchDisableUsers(List<Long> ids);

    /**
     * 锁定用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean lockUser(Long id);

    /**
     * 解锁用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    Boolean unlockUser(Long id);

    /**
     * 批量锁定用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    Boolean batchLockUsers(List<Long> ids);

    /**
     * 批量解锁用户
     *
     * @param ids 用户ID列表
     * @return 是否成功
     */
    Boolean batchUnlockUsers(List<Long> ids);

    /**
     * 重置用户密码
     *
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean resetPassword(Long id, String newPassword);

    /**
     * 修改用户密码
     *
     * @param id 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean changePassword(Long id, String oldPassword, String newPassword);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    Boolean existsByUsername(String username, Long tenantId, Long excludeId);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @param tenantId 租户ID
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    Boolean existsByEmail(String email, Long tenantId, Long excludeId);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @param tenantId 租户ID
     * @param excludeId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    Boolean existsByPhone(String phone, Long tenantId, Long excludeId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 是否成功
     */
    Boolean updateLoginInfo(Long userId, String loginIp);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限代码列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 检查用户是否有指定权限
     *
     * @param userId 用户ID
     * @param permission 权限代码
     * @return 是否有权限
     */
    Boolean hasPermission(Long userId, String permission);

    /**
     * 获取用户统计信息
     *
     * @param tenantId 租户ID
     * @return 统计信息
     */
    UserStatistics getUserStatistics(Long tenantId);

    /**
     * 获取最近登录的用户列表
     *
     * @param tenantId 租户ID
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserResponse> getRecentLoginUsers(Long tenantId, Integer limit);

    /**
     * 获取长时间未登录的用户列表
     *
     * @param tenantId 租户ID
     * @param days 天数
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserResponse> getLongTimeNoLoginUsers(Long tenantId, Integer days, Integer limit);

    /**
     * 导出用户数据
     *
     * @param request 查询条件
     * @return 用户列表
     */
    List<UserResponse> exportUsers(UserPageRequest request);

    /**
     * 批量导入用户
     *
     * @param users 用户列表
     * @return 导入结果
     */
    ImportResult importUsers(List<CreateUserRequest> users);

    /**
     * 用户统计信息
     */
    class UserStatistics {
        private Long totalCount;
        private Long enabledCount;
        private Long disabledCount;
        private Long lockedCount;
        private Long recentLoginCount;
        private Long longNoLoginCount;

        // 构造函数
        public UserStatistics() {}

        public UserStatistics(Long totalCount, Long enabledCount, Long disabledCount, 
                            Long lockedCount, Long recentLoginCount, Long longNoLoginCount) {
            this.totalCount = totalCount;
            this.enabledCount = enabledCount;
            this.disabledCount = disabledCount;
            this.lockedCount = lockedCount;
            this.recentLoginCount = recentLoginCount;
            this.longNoLoginCount = longNoLoginCount;
        }

        // Getter和Setter方法
        public Long getTotalCount() { return totalCount; }
        public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }

        public Long getEnabledCount() { return enabledCount; }
        public void setEnabledCount(Long enabledCount) { this.enabledCount = enabledCount; }

        public Long getDisabledCount() { return disabledCount; }
        public void setDisabledCount(Long disabledCount) { this.disabledCount = disabledCount; }

        public Long getLockedCount() { return lockedCount; }
        public void setLockedCount(Long lockedCount) { this.lockedCount = lockedCount; }

        public Long getRecentLoginCount() { return recentLoginCount; }
        public void setRecentLoginCount(Long recentLoginCount) { this.recentLoginCount = recentLoginCount; }

        public Long getLongNoLoginCount() { return longNoLoginCount; }
        public void setLongNoLoginCount(Long longNoLoginCount) { this.longNoLoginCount = longNoLoginCount; }
    }

    /**
     * 导入结果
     */
    class ImportResult {
        private Integer totalCount;
        private Integer successCount;
        private Integer failureCount;
        private List<String> errorMessages;

        // 构造函数
        public ImportResult() {
            this.errorMessages = new java.util.ArrayList<>();
        }

        public ImportResult(Integer totalCount, Integer successCount, Integer failureCount) {
            this.totalCount = totalCount;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.errorMessages = new java.util.ArrayList<>();
        }

        // Getter和Setter方法
        public Integer getTotalCount() { return totalCount; }
        public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }

        public Integer getSuccessCount() { return successCount; }
        public void setSuccessCount(Integer successCount) { this.successCount = successCount; }

        public Integer getFailureCount() { return failureCount; }
        public void setFailureCount(Integer failureCount) { this.failureCount = failureCount; }

        public List<String> getErrorMessages() { return errorMessages; }
        public void setErrorMessages(List<String> errorMessages) { this.errorMessages = errorMessages; }

        public void addErrorMessage(String message) {
            if (this.errorMessages == null) {
                this.errorMessages = new java.util.ArrayList<>();
            }
            this.errorMessages.add(message);
        }
    }
}