package com.biobt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.common.core.domain.PageResponse;
import com.biobt.user.domain.dto.*;
import com.biobt.user.domain.entity.Permission;
import com.biobt.user.domain.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务接口
 *
 * @author BioBt Team
 * @since 1.0.0
 */
public interface UserService extends IService<User> {

    /**
     * 创建用户
     *
     * @param request 创建用户请求
     * @return 用户响应信息
     */
    UserResponse createUser(CreateUserRequest request);

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户响应信息
     */
    UserResponse getUserById(Long id);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户响应信息
     */
    UserResponse getUserByUsername(String username);

    /**
     * 获取当前登录用户
     *
     * @return 用户响应信息
     */
    UserResponse getCurrentUser();

    /**
     * 分页查询用户列表
     *
     * @param request 分页查询请求
     * @return 分页用户列表
     */
    PageResponse<UserResponse> getUserPage(UserPageRequest request);

    /**
     * 更新用户信息
     *
     * @param id      用户ID
     * @param request 更新用户请求
     * @return 用户响应信息
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /**
     * 删除用户（软删除）
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户（软删除）
     *
     * @param ids 用户ID列表
     */
    void deleteUsers(List<Long> ids);

    /**
     * 启用用户
     *
     * @param id 用户ID
     */
    void enableUser(Long id);

    /**
     * 禁用用户
     *
     * @param id 用户ID
     */
    void disableUser(Long id);

    /**
     * 批量启用用户
     *
     * @param ids 用户ID列表
     */
    void enableUsers(List<Long> ids);

    /**
     * 批量禁用用户
     *
     * @param ids 用户ID列表
     */
    void disableUsers(List<Long> ids);

    /**
     * 锁定用户
     *
     * @param id 用户ID
     */
    void lockUser(Long id);

    /**
     * 解锁用户
     *
     * @param id 用户ID
     */
    void unlockUser(Long id);

    /**
     * 批量锁定用户
     *
     * @param ids 用户ID列表
     */
    void lockUsers(List<Long> ids);

    /**
     * 批量解锁用户
     *
     * @param ids 用户ID列表
     */
    void unlockUsers(List<Long> ids);

    /**
     * 重置用户密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long id, String newPassword);

    /**
     * 修改密码
     *
     * @param request 修改密码请求
     */
    void changePassword(ChangePasswordRequest request);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 更新用户登录信息
     *
     * @param userId    用户ID
     * @param loginIp   登录IP
     * @param userAgent 用户代理
     */
    void updateLoginInfo(Long userId, String loginIp, String userAgent);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 检查用户是否有指定权限
     *
     * @param userId     用户ID
     * @param permission 权限编码
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permission);

    /**
     * 获取用户统计信息
     *
     * @return 用户统计信息
     */
    UserStatistics getUserStatistics();

    /**
     * 获取最近登录的用户
     *
     * @param days 天数
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserResponse> getRecentLoginUsers(int days, int limit);

    /**
     * 获取长时间未登录的用户
     *
     * @param days 天数
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserResponse> getLongTimeNoLoginUsers(int days, int limit);

    /**
     * 导出用户数据
     *
     * @param request 查询条件
     * @return 导出文件字节数组
     */
    byte[] exportUsers(UserPageRequest request);

    /**
     * 导入用户数据
     *
     * @param file 导入文件
     * @return 导入结果
     */
    ImportResult importUsers(MultipartFile file);

    /**
     * 判断是否为当前用户
     *
     * @param userId 用户ID
     * @return 是否为当前用户
     */
    boolean isCurrentUser(Long userId);

    /**
     * 用户统计信息
     */
    class UserStatistics {
        private Long totalUsers;
        private Long activeUsers;
        private Long lockedUsers;
        private Long todayRegistrations;
        private Long monthlyRegistrations;
        
        // 构造函数、getter和setter
        public UserStatistics() {}
        
        public UserStatistics(Long totalUsers, Long activeUsers, Long lockedUsers, 
                            Long todayRegistrations, Long monthlyRegistrations) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.lockedUsers = lockedUsers;
            this.todayRegistrations = todayRegistrations;
            this.monthlyRegistrations = monthlyRegistrations;
        }
        
        public Long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
        
        public Long getActiveUsers() { return activeUsers; }
        public void setActiveUsers(Long activeUsers) { this.activeUsers = activeUsers; }
        
        public Long getLockedUsers() { return lockedUsers; }
        public void setLockedUsers(Long lockedUsers) { this.lockedUsers = lockedUsers; }
        
        public Long getTodayRegistrations() { return todayRegistrations; }
        public void setTodayRegistrations(Long todayRegistrations) { this.todayRegistrations = todayRegistrations; }
        
        public Long getMonthlyRegistrations() { return monthlyRegistrations; }
        public void setMonthlyRegistrations(Long monthlyRegistrations) { this.monthlyRegistrations = monthlyRegistrations; }
    }

    /**
     * 导入结果
     */
    class ImportResult {
        private int totalCount;
        private int successCount;
        private int failureCount;
        private List<String> errorMessages;
        
        // 构造函数、getter和setter
        public ImportResult() {}
        
        public ImportResult(int totalCount, int successCount, int failureCount, List<String> errorMessages) {
            this.totalCount = totalCount;
            this.successCount = successCount;
            this.failureCount = failureCount;
            this.errorMessages = errorMessages;
        }
        
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
        
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        
        public int getFailureCount() { return failureCount; }
        public void setFailureCount(int failureCount) { this.failureCount = failureCount; }
        
        public List<String> getErrorMessages() { return errorMessages; }
        public void setErrorMessages(List<String> errorMessages) { this.errorMessages = errorMessages; }
    }
}