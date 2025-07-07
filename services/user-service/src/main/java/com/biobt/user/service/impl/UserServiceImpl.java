package com.biobt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biobt.common.core.domain.PageResponse;
import com.biobt.common.core.exception.BusinessException;
import com.biobt.common.core.utils.SecurityUtils;
import com.biobt.user.domain.dto.*;
import com.biobt.user.domain.entity.User;
import com.biobt.user.mapper.UserMapper;
import com.biobt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse createUser(CreateUserRequest request) {
        log.info("创建用户: {}", request.getUsername());
        
        // 检查用户名是否已存在
        if (existsByUsername(request.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (StringUtils.hasText(request.getEmail()) && existsByEmail(request.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 检查手机号是否已存在
        if (StringUtils.hasText(request.getPhone()) && existsByPhone(request.getPhone())) {
            throw new BusinessException("手机号已存在");
        }
        
        // 创建用户实体
        User user = User.builder()
                .tenantId(request.getTenantId() != null ? Long.valueOf(request.getTenantId()) : null)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .realName(request.getRealName())
                .nickname(request.getNickname())
                .avatar(request.getAvatar())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .lockStatus(request.getLockStatus() != null ? request.getLockStatus() : 0)
                .passwordUpdateTime(LocalDateTime.now())
                .remarks(request.getRemarks())
                .createBy(SecurityUtils.getCurrentUserId())
                .build();
        
        // 保存用户
        userMapper.insert(user);
        
        log.info("用户创建成功: id={}, username={}", user.getId(), user.getUsername());
        return UserResponse.from(user);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return UserResponse.from(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return UserResponse.from(user);
    }

    @Override
    public UserResponse getCurrentUser() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException("未登录");
        }
        return getUserById(currentUserId);
    }

    @Override
    public PageResponse<UserResponse> getUserPage(UserPageRequest request) {
        Page<User> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<User> userPage = userMapper.selectUserPage(page, request);
        
        List<UserResponse> userResponses = userPage.getRecords().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
        
        return PageResponse.<UserResponse>builder()
                .records(userResponses)
                .total(userPage.getTotal())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("更新用户: id={}", id);
        
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查邮箱是否已被其他用户使用
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(existingUser.getEmail())) {
            if (existsByEmail(request.getEmail())) {
                throw new BusinessException("邮箱已被其他用户使用");
            }
        }
        
        // 检查手机号是否已被其他用户使用
        if (StringUtils.hasText(request.getPhone()) && !request.getPhone().equals(existingUser.getPhone())) {
            if (existsByPhone(request.getPhone())) {
                throw new BusinessException("手机号已被其他用户使用");
            }
        }
        
        // 更新用户信息
        User updateUser = new User();
        updateUser.setId(id);
        BeanUtils.copyProperties(request, updateUser);
        updateUser.setUpdateBy(SecurityUtils.getCurrentUserId());
        updateUser.setUpdateTime(LocalDateTime.now());
        
        userMapper.updateById(updateUser);
        
        log.info("用户更新成功: id={}", id);
        return getUserById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        log.info("删除用户: id={}", id);
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 软删除
        user.setDeleted(1);
        user.setUpdateBy(SecurityUtils.getCurrentUserId());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        log.info("用户删除成功: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(List<Long> ids) {
        log.info("批量删除用户: ids={}", ids);
        
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("用户ID列表不能为空");
        }
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int count = userMapper.batchSoftDelete(ids, currentUserId);
        
        log.info("批量删除用户成功: count={}", count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableUser(Long id) {
        updateUserStatus(id, 1, "启用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableUser(Long id) {
        updateUserStatus(id, 0, "禁用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableUsers(List<Long> ids) {
        batchUpdateUserStatus(ids, 1, "批量启用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableUsers(List<Long> ids) {
        batchUpdateUserStatus(ids, 0, "批量禁用");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockUser(Long id) {
        updateUserLockStatus(id, 1, "锁定");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockUser(Long id) {
        updateUserLockStatus(id, 0, "解锁");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockUsers(List<Long> ids) {
        batchUpdateUserLockStatus(ids, 1, "批量锁定");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockUsers(List<Long> ids) {
        batchUpdateUserLockStatus(ids, 0, "批量解锁");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String newPassword) {
        log.info("重置用户密码: id={}", id);
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        int count = userMapper.updatePassword(id, encodedPassword, LocalDateTime.now(), currentUserId);
        if (count == 0) {
            throw new BusinessException("密码重置失败");
        }
        
        log.info("用户密码重置成功: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(ChangePasswordRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException("未登录");
        }
        
        log.info("修改用户密码: userId={}", currentUserId);
        
        User user = userMapper.selectById(currentUserId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证原密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }
        
        // 更新密码
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        int count = userMapper.updatePassword(currentUserId, encodedPassword, LocalDateTime.now(), currentUserId);
        if (count == 0) {
            throw new BusinessException("密码修改失败");
        }
        
        log.info("用户密码修改成功: userId={}", currentUserId);
    }

    @Override
    public boolean existsByUsername(String username) {
        User user = userMapper.selectByUsernameIncludeDeleted(username);
        return user != null && Integer.valueOf(0).equals(user.getDeleted());
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        User user = userMapper.selectByEmailIncludeDeleted(email);
        return user != null && Integer.valueOf(0).equals(user.getDeleted());
    }

    @Override
    public boolean existsByPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        User user = userMapper.selectByPhoneIncludeDeleted(phone);
        return user != null && Integer.valueOf(0).equals(user.getDeleted());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginInfo(Long userId, String loginIp, String userAgent) {
        log.debug("更新用户登录信息: userId={}, loginIp={}", userId, loginIp);
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.warn("用户不存在，无法更新登录信息: userId={}", userId);
            return;
        }
        
        int loginCount = user.getLoginCount() != null ? user.getLoginCount() + 1 : 1;
        userMapper.updateLoginInfo(userId, LocalDateTime.now(), loginIp, loginCount);
    }

    @Override
    public List<String> getUserPermissions(Long userId) {
        return userMapper.selectUserPermissions(userId);
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        List<String> permissions = getUserPermissions(userId);
        return permissions.contains(permission);
    }

    @Override
    public UserStatistics getUserStatistics() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);
        LocalDateTime monthStart = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        
        Long totalUsers = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getDeleted, 0));
        Long activeUsers = userMapper.countByStatus(1);
        Long lockedUsers = userMapper.countByLockStatus(1);
        Long todayRegistrations = userMapper.countTodayRegistrations(todayStart, todayEnd);
        Long monthlyRegistrations = userMapper.countMonthlyRegistrations(monthStart, todayEnd);
        
        return new UserStatistics(totalUsers, activeUsers, lockedUsers, todayRegistrations, monthlyRegistrations);
    }

    @Override
    public List<UserResponse> getRecentLoginUsers(int days, int limit) {
        List<User> users = userMapper.selectRecentLoginUsers(days, limit);
        return users.stream().map(UserResponse::simple).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getLongTimeNoLoginUsers(int days, int limit) {
        List<User> users = userMapper.selectLongTimeNoLoginUsers(days, limit);
        return users.stream().map(UserResponse::simple).collect(Collectors.toList());
    }

    @Override
    public byte[] exportUsers(UserPageRequest request) {
        // TODO: 实现用户数据导出功能
        throw new BusinessException("导出功能暂未实现");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportResult importUsers(MultipartFile file) {
        // TODO: 实现用户数据导入功能
        throw new BusinessException("导入功能暂未实现");
    }

    @Override
    public boolean isCurrentUser(Long userId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }

    /**
     * 更新用户状态
     */
    private void updateUserStatus(Long id, Integer status, String operation) {
        log.info("{}用户: id={}", operation, id);
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setStatus(status);
        user.setUpdateBy(SecurityUtils.getCurrentUserId());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        log.info("用户{}成功: id={}", operation, id);
    }

    /**
     * 批量更新用户状态
     */
    private void batchUpdateUserStatus(List<Long> ids, Integer status, String operation) {
        log.info("{}: ids={}", operation, ids);
        
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("用户ID列表不能为空");
        }
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int count = userMapper.batchUpdateStatus(ids, status, currentUserId);
        
        log.info("{}成功: count={}", operation, count);
    }

    /**
     * 更新用户锁定状态
     */
    private void updateUserLockStatus(Long id, Integer lockStatus, String operation) {
        log.info("{}用户: id={}", operation, id);
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        user.setLockStatus(lockStatus);
        user.setUpdateBy(SecurityUtils.getCurrentUserId());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        log.info("用户{}成功: id={}", operation, id);
    }

    /**
     * 批量更新用户锁定状态
     */
    private void batchUpdateUserLockStatus(List<Long> ids, Integer lockStatus, String operation) {
        log.info("{}: ids={}", operation, ids);
        
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("用户ID列表不能为空");
        }
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int count = (lockStatus == 1) ? userMapper.batchLock(ids, currentUserId) : userMapper.batchUnlock(ids, currentUserId);
        
        log.info("{}成功: count={}", operation, count);
    }
}