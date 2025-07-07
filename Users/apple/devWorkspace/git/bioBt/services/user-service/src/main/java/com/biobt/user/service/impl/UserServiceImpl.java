package com.biobt.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biobt.user.dto.CreateUserRequest;
import com.biobt.user.dto.UpdateUserRequest;
import com.biobt.user.dto.UserPageRequest;
import com.biobt.user.dto.UserResponse;
import com.biobt.user.entity.User;
import com.biobt.user.mapper.UserMapper;
import com.biobt.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String DEFAULT_PASSWORD = "123456";
    private static final Integer DEFAULT_TENANT_ID = 0;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        log.info("创建用户: {}", request.getUsername());
        
        // 验证用户名是否已存在
        if (existsByUsername(request.getUsername(), request.getTenantId(), null)) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 验证邮箱是否已存在
        if (StrUtil.isNotBlank(request.getEmail()) && 
            existsByEmail(request.getEmail(), request.getTenantId(), null)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 验证手机号是否已存在
        if (StrUtil.isNotBlank(request.getPhone()) && 
            existsByPhone(request.getPhone(), request.getTenantId(), null)) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 创建用户实体
        User user = new User();
        BeanUtil.copyProperties(request, user);
        
        // 设置默认值
        if (user.getTenantId() == null) {
            user.setTenantId(Long.valueOf(DEFAULT_TENANT_ID));
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }
        if (user.getIsLocked() == null) {
            user.setIsLocked(0); // 默认未锁定
        }
        if (user.getGender() == null) {
            user.setGender(0); // 默认未知
        }
        
        // 加密密码
        String password = StrUtil.isNotBlank(request.getPassword()) ? 
                         request.getPassword() : DEFAULT_PASSWORD;
        user.setPassword(BCrypt.hashpw(password));
        user.setPasswordUpdateTime(LocalDateTime.now());
        
        // 设置创建信息
        Long currentUserId = getCurrentUserId();
        user.setCreatedBy(currentUserId);
        user.setUpdatedBy(currentUserId);
        
        // 保存用户
        boolean saved = save(user);
        if (!saved) {
            throw new RuntimeException("创建用户失败");
        }
        
        log.info("用户创建成功: {} (ID: {})", user.getUsername(), user.getId());
        return UserResponse.fromUser(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        return UserResponse.fromUser(user);
    }

    @Override
    public UserResponse getUserByUsername(String username, Long tenantId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
               .eq(User::getTenantId, tenantId)
               .eq(User::getDeleted, 0);
        
        User user = getOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return UserResponse.fromUser(user);
    }

    @Override
    public UserResponse getCurrentUser() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("未登录");
        }
        return getUserById(currentUserId);
    }

    @Override
    public IPage<UserResponse> getUserPage(UserPageRequest request) {
        Page<User> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        IPage<User> userPage = baseMapper.selectUserPage(
            page,
            request.getTenantId(),
            request.getUsername(),
            request.getEmail(),
            request.getPhone(),
            request.getRealName(),
            request.getStatus(),
            request.getIsLocked(),
            request.getStartTime(),
            request.getEndTime()
        );
        
        // 转换为响应对象
        Page<UserResponse> responsePage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserResponse> responseList = userPage.getRecords().stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
        responsePage.setRecords(responseList);
        
        return responsePage;
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("更新用户: {}", id);
        
        User existingUser = getById(id);
        if (existingUser == null || existingUser.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证邮箱是否已存在（排除当前用户）
        if (StrUtil.isNotBlank(request.getEmail()) && 
            existsByEmail(request.getEmail(), existingUser.getTenantId(), id)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 验证手机号是否已存在（排除当前用户）
        if (StrUtil.isNotBlank(request.getPhone()) && 
            existsByPhone(request.getPhone(), existingUser.getTenantId(), id)) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 更新用户信息
        BeanUtil.copyProperties(request, existingUser, "id", "tenantId", "username", "password", 
                               "createdTime", "createdBy", "deleted", "version");
        
        existingUser.setUpdatedBy(getCurrentUserId());
        existingUser.setUpdatedTime(LocalDateTime.now());
        
        boolean updated = updateById(existingUser);
        if (!updated) {
            throw new RuntimeException("更新用户失败");
        }
        
        log.info("用户更新成功: {} (ID: {})", existingUser.getUsername(), id);
        return UserResponse.fromUser(existingUser);
    }

    @Override
    public Boolean deleteUser(Long id) {
        log.info("删除用户: {}", id);
        
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        // 软删除
        user.setDeleted(1);
        user.setUpdatedBy(getCurrentUserId());
        user.setUpdatedTime(LocalDateTime.now());
        
        boolean deleted = updateById(user);
        if (deleted) {
            log.info("用户删除成功: {} (ID: {})", user.getUsername(), id);
        }
        
        return deleted;
    }

    @Override
    public Boolean batchDeleteUsers(List<Long> ids) {
        log.info("批量删除用户: {}", ids);
        
        if (CollUtil.isEmpty(ids)) {
            return true;
        }
        
        Long currentUserId = getCurrentUserId();
        int count = baseMapper.batchSoftDelete(ids, currentUserId);
        
        log.info("批量删除用户完成，影响行数: {}", count);
        return count > 0;
    }

    @Override
    public Boolean enableUser(Long id) {
        return updateUserStatus(id, 1, "启用");
    }

    @Override
    public Boolean disableUser(Long id) {
        return updateUserStatus(id, 0, "禁用");
    }

    @Override
    public Boolean batchEnableUsers(List<Long> ids) {
        return batchUpdateUserStatus(ids, 1, "批量启用");
    }

    @Override
    public Boolean batchDisableUsers(List<Long> ids) {
        return batchUpdateUserStatus(ids, 0, "批量禁用");
    }

    @Override
    public Boolean lockUser(Long id) {
        log.info("锁定用户: {}", id);
        
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setIsLocked(1);
        user.setLockTime(LocalDateTime.now());
        user.setUpdatedBy(getCurrentUserId());
        user.setUpdatedTime(LocalDateTime.now());
        
        boolean updated = updateById(user);
        if (updated) {
            log.info("用户锁定成功: {} (ID: {})", user.getUsername(), id);
        }
        
        return updated;
    }

    @Override
    public Boolean unlockUser(Long id) {
        log.info("解锁用户: {}", id);
        
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setIsLocked(0);
        user.setLockTime(null);
        user.setUpdatedBy(getCurrentUserId());
        user.setUpdatedTime(LocalDateTime.now());
        
        boolean updated = updateById(user);
        if (updated) {
            log.info("用户解锁成功: {} (ID: {})", user.getUsername(), id);
        }
        
        return updated;
    }

    @Override
    public Boolean batchLockUsers(List<Long> ids) {
        log.info("批量锁定用户: {}", ids);
        
        if (CollUtil.isEmpty(ids)) {
            return true;
        }
        
        Long currentUserId = getCurrentUserId();
        int count = baseMapper.batchLock(ids, currentUserId);
        
        log.info("批量锁定用户完成，影响行数: {}", count);
        return count > 0;
    }

    @Override
    public Boolean batchUnlockUsers(List<Long> ids) {
        log.info("批量解锁用户: {}", ids);
        
        if (CollUtil.isEmpty(ids)) {
            return true;
        }
        
        Long currentUserId = getCurrentUserId();
        int count = baseMapper.batchUnlock(ids, currentUserId);
        
        log.info("批量解锁用户完成，影响行数: {}", count);
        return count > 0;
    }

    @Override
    public Boolean resetPassword(Long id, String newPassword) {
        log.info("重置用户密码: {}", id);
        
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        String password = StrUtil.isNotBlank(newPassword) ? newPassword : DEFAULT_PASSWORD;
        String hashedPassword = BCrypt.hashpw(password);
        
        Long currentUserId = getCurrentUserId();
        int count = baseMapper.updatePassword(id, hashedPassword, currentUserId);
        
        if (count > 0) {
            log.info("用户密码重置成功: {} (ID: {})", user.getUsername(), id);
        }
        
        return count > 0;
    }

    @Override
    public Boolean changePassword(Long id, String oldPassword, String newPassword) {
        log.info("修改用户密码: {}", id);
        
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        
        String hashedPassword = BCrypt.hashpw(newPassword);
        Long currentUserId = getCurrentUserId();
        int count = baseMapper.updatePassword(id, hashedPassword, currentUserId);
        
        if (count > 0) {
            log.info("用户密码修改成功: {} (ID: {})", user.getUsername(), id);
        }
        
        return count > 0;
    }

    @Override
    public Boolean existsByUsername(String username, Long tenantId, Long excludeId) {
        User user = baseMapper.selectByUsernameIncludeDeleted(username, tenantId);
        if (user == null || user.getDeleted() == 1) {
            return false;
        }
        return excludeId == null || !user.getId().equals(excludeId);
    }

    @Override
    public Boolean existsByEmail(String email, Long tenantId, Long excludeId) {
        User user = baseMapper.selectByEmailIncludeDeleted(email, tenantId);
        if (user == null || user.getDeleted() == 1) {
            return false;
        }
        return excludeId == null || !user.getId().equals(excludeId);
    }

    @Override
    public Boolean existsByPhone(String phone, Long tenantId, Long excludeId) {
        User user = baseMapper.selectByPhoneIncludeDeleted(phone, tenantId);
        if (user == null || user.getDeleted() == 1) {
            return false;
        }
        return excludeId == null || !user.getId().equals(excludeId);
    }

    @Override
    public Boolean updateLoginInfo(Long userId, String loginIp) {
        int count = baseMapper.updateLoginInfo(userId, loginIp, LocalDateTime.now());
        return count > 0;
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return baseMapper.selectUserPermissions(userId);
    }

    @Override
    public Boolean hasPermission(Long userId, String permission) {
        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permission);
    }

    @Override
    public UserStatistics getUserStatistics(Long tenantId) {
        Long totalCount = baseMapper.countByTenantId(tenantId);
        Long enabledCount = baseMapper.countByStatus(tenantId, 1);
        Long disabledCount = baseMapper.countByStatus(tenantId, 0);
        Long lockedCount = baseMapper.countByLockStatus(tenantId, 1);
        
        // 这里简化处理，实际应该通过SQL查询
        Long recentLoginCount = 0L;
        Long longNoLoginCount = 0L;
        
        return new UserStatistics(totalCount, enabledCount, disabledCount, 
                                 lockedCount, recentLoginCount, longNoLoginCount);
    }

    @Override
    public List<UserResponse> getRecentLoginUsers(Long tenantId, Integer limit) {
        List<User> users = baseMapper.selectRecentLoginUsers(tenantId, limit);
        return users.stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getLongTimeNoLoginUsers(Long tenantId, Integer days, Integer limit) {
        List<User> users = baseMapper.selectLongTimeNoLoginUsers(tenantId, days, limit);
        return users.stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> exportUsers(UserPageRequest request) {
        // 设置大的页面大小来获取所有数据
        request.setPageNum(1);
        request.setPageSize(10000);
        
        IPage<UserResponse> page = getUserPage(request);
        return page.getRecords();
    }

    @Override
    public ImportResult importUsers(List<CreateUserRequest> users) {
        ImportResult result = new ImportResult();
        result.setTotalCount(users.size());
        result.setSuccessCount(0);
        result.setFailureCount(0);
        
        for (int i = 0; i < users.size(); i++) {
            CreateUserRequest request = users.get(i);
            try {
                createUser(request);
                result.setSuccessCount(result.getSuccessCount() + 1);
            } catch (Exception e) {
                result.setFailureCount(result.getFailureCount() + 1);
                result.addErrorMessage(String.format("第%d行导入失败: %s", i + 1, e.getMessage()));
                log.error("导入用户失败: {}", request.getUsername(), e);
            }
        }
        
        return result;
    }

    /**
     * 更新用户状态
     */
    private Boolean updateUserStatus(Long id, Integer status, String operation) {
        log.info("{}用户: {}", operation, id);
        
        User user = getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setStatus(status);
        user.setUpdatedBy(getCurrentUserId());
        user.setUpdatedTime(LocalDateTime.now());
        
        boolean updated = updateById(user);
        if (updated) {
            log.info("用户{}成功: {} (ID: {})", operation, user.getUsername(), id);
        }
        
        return updated;
    }

    /**
     * 批量更新用户状态
     */
    private Boolean batchUpdateUserStatus(List<Long> ids, Integer status, String operation) {
        log.info("{}: {}", operation, ids);
        
        if (CollUtil.isEmpty(ids)) {
            return true;
        }
        
        Long currentUserId = getCurrentUserId();
        int count = status == 1 ? 
                   baseMapper.batchEnable(ids, currentUserId) : 
                   baseMapper.batchDisable(ids, currentUserId);
        
        log.info("{}完成，影响行数: {}", operation, count);
        return count > 0;
    }

    /**
     * 获取当前用户ID
     * TODO: 从安全上下文中获取当前用户ID
     */
    private Long getCurrentUserId() {
        // 这里应该从Spring Security上下文中获取当前用户ID
        // 暂时返回默认值
        return 1L;
    }
}