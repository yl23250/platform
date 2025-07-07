package com.biobt.user.util;

import com.biobt.user.domain.dto.CreateUserRequest;
import com.biobt.user.domain.dto.UpdateUserRequest;
import com.biobt.user.domain.dto.UserPageRequest;
import com.biobt.user.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 测试数据构建工具类
 *
 * @author biobt
 * @since 2024-01-01
 */
public class TestDataBuilder {

    /**
     * 创建默认的用户实体
     */
    public static User createDefaultUser() {
        User user = new User();
        user.setId(1L);
        user.setTenantId(1L);
        user.setUsername("testuser");
        user.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa"); // encoded "password"
        user.setEmail("test@example.com");
        user.setPhone("13800138000");
        user.setRealName("测试用户");
        user.setNickname("测试");
        user.setAvatar("https://example.com/avatar.jpg");
        user.setGender(1);
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setStatus(1);
        user.setLockStatus(0);
        user.setLastLoginTime(LocalDateTime.now().minusDays(1));
        user.setLastLoginIp("192.168.1.100");
        user.setLoginCount(10);
        user.setPasswordUpdateTime(LocalDateTime.now().minusDays(30));
        user.setRemarks("测试用户备注");
        user.setCreateTime(LocalDateTime.now().minusDays(30));
        user.setUpdateTime(LocalDateTime.now().minusDays(1));
        user.setCreateBy("system");
        user.setUpdateBy("admin");
        user.setDeleted(0);
        return user;
    }

    /**
     * 创建用户列表
     */
    public static List<User> createUserList() {
        User user1 = createDefaultUser();
        
        User user2 = createDefaultUser();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        user2.setPhone("13800138001");
        user2.setRealName("测试用户2");
        user2.setNickname("测试2");
        
        User user3 = createDefaultUser();
        user3.setId(3L);
        user3.setUsername("testuser3");
        user3.setEmail("test3@example.com");
        user3.setPhone("13800138002");
        user3.setRealName("测试用户3");
        user3.setNickname("测试3");
        user3.setStatus(0); // 禁用状态
        
        return Arrays.asList(user1, user2, user3);
    }

    /**
     * 创建默认的创建用户请求
     */
    public static CreateUserRequest createDefaultCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setTenantId(1L);
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setEmail("newuser@example.com");
        request.setPhone("13900139000");
        request.setRealName("新用户");
        request.setNickname("新用户昵称");
        request.setAvatar("https://example.com/new-avatar.jpg");
        request.setGender(2);
        request.setBirthday(LocalDate.of(1995, 6, 15));
        request.setRemarks("新用户备注");
        request.setRoleIds(Arrays.asList(2L, 3L));
        return request;
    }

    /**
     * 创建默认的更新用户请求
     */
    public static UpdateUserRequest createDefaultUpdateUserRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("updated@example.com");
        request.setPhone("13900139999");
        request.setRealName("更新后的用户");
        request.setNickname("更新昵称");
        request.setAvatar("https://example.com/updated-avatar.jpg");
        request.setGender(1);
        request.setBirthday(LocalDate.of(1992, 8, 20));
        request.setRemarks("更新后的备注");
        request.setRoleIds(Arrays.asList(2L));
        return request;
    }

    /**
     * 创建默认的用户分页请求
     */
    public static UserPageRequest createDefaultUserPageRequest() {
        UserPageRequest request = new UserPageRequest();
        request.setPageNum(1);
        request.setPageSize(10);
        request.setTenantId(1L);
        return request;
    }

    /**
     * 创建带搜索条件的用户分页请求
     */
    public static UserPageRequest createUserPageRequestWithSearch() {
        UserPageRequest request = createDefaultUserPageRequest();
        request.setKeyword("test");
        request.setStatus(1);
        request.setLockStatus(0);
        request.setStartDate(LocalDateTime.now().minusDays(30));
        request.setEndDate(LocalDateTime.now());
        return request;
    }

    /**
     * 创建无效的创建用户请求（缺少必填字段）
     */
    public static CreateUserRequest createInvalidCreateUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        // 故意不设置必填字段
        request.setNickname("无效用户");
        return request;
    }

    /**
     * 创建重复用户名的创建用户请求
     */
    public static CreateUserRequest createDuplicateUsernameRequest() {
        CreateUserRequest request = createDefaultCreateUserRequest();
        request.setUsername("admin"); // 使用已存在的用户名
        request.setEmail("duplicate@example.com");
        request.setPhone("13700137000");
        return request;
    }

    /**
     * 创建重复邮箱的创建用户请求
     */
    public static CreateUserRequest createDuplicateEmailRequest() {
        CreateUserRequest request = createDefaultCreateUserRequest();
        request.setUsername("uniqueuser");
        request.setEmail("admin@example.com"); // 使用已存在的邮箱
        request.setPhone("13700137001");
        return request;
    }

    /**
     * 创建重复手机号的创建用户请求
     */
    public static CreateUserRequest createDuplicatePhoneRequest() {
        CreateUserRequest request = createDefaultCreateUserRequest();
        request.setUsername("uniqueuser2");
        request.setEmail("unique@example.com");
        request.setPhone("13800138000"); // 使用已存在的手机号
        return request;
    }

    /**
     * 创建管理员用户
     */
    public static User createAdminUser() {
        User admin = createDefaultUser();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPhone("13800138000");
        admin.setRealName("系统管理员");
        admin.setNickname("管理员");
        return admin;
    }

    /**
     * 创建普通用户
     */
    public static User createRegularUser() {
        User user = createDefaultUser();
        user.setId(2L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPhone("13800138001");
        user.setRealName("普通用户");
        user.setNickname("用户");
        return user;
    }

    /**
     * 创建禁用用户
     */
    public static User createDisabledUser() {
        User user = createDefaultUser();
        user.setId(3L);
        user.setUsername("disabled_user");
        user.setEmail("disabled@example.com");
        user.setPhone("13800138002");
        user.setRealName("禁用用户");
        user.setNickname("禁用");
        user.setStatus(0);
        return user;
    }

    /**
     * 创建锁定用户
     */
    public static User createLockedUser() {
        User user = createDefaultUser();
        user.setId(4L);
        user.setUsername("locked_user");
        user.setEmail("locked@example.com");
        user.setPhone("13800138003");
        user.setRealName("锁定用户");
        user.setNickname("锁定");
        user.setLockStatus(1);
        return user;
    }

    /**
     * 创建已删除用户
     */
    public static User createDeletedUser() {
        User user = createDefaultUser();
        user.setId(5L);
        user.setUsername("deleted_user");
        user.setEmail("deleted@example.com");
        user.setPhone("13800138004");
        user.setRealName("已删除用户");
        user.setNickname("已删除");
        user.setDeleted(1);
        return user;
    }

    /**
     * 创建用户ID列表
     */
    public static List<Long> createUserIdList() {
        return Arrays.asList(1L, 2L, 3L, 4L, 5L);
    }

    /**
     * 创建角色ID列表
     */
    public static List<Long> createRoleIdList() {
        return Arrays.asList(1L, 2L, 3L);
    }

    /**
     * 创建权限代码列表
     */
    public static List<String> createPermissionCodeList() {
        return Arrays.asList(
            "user:view",
            "user:create",
            "user:update",
            "user:delete",
            "role:view",
            "role:create"
        );
    }
}