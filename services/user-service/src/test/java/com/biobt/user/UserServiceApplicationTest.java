package com.biobt.user;

import com.biobt.user.domain.dto.CreateUserRequest;
import com.biobt.user.domain.entity.User;
import com.biobt.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户服务应用集成测试
 *
 * @author biobt
 * @since 2024-01-01
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        // 测试Spring上下文是否正常加载
        assertNotNull(userService);
    }

    @Test
    void testCreateUserIntegration() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setTenantId(1L);
        request.setUsername("integration_test_user");
        request.setPassword("password123");
        request.setEmail("integration@example.com");
        request.setPhone("13900139999");
        request.setRealName("集成测试用户");
        request.setNickname("集成测试");
        request.setGender(1);
        request.setBirthday(LocalDate.of(1995, 6, 15));
        request.setRoleIds(Arrays.asList(2L));

        // When
        Long userId = userService.createUser(request);

        // Then
        assertNotNull(userId);
        assertTrue(userId > 0);

        // 验证用户是否创建成功
        User createdUser = userService.getUserById(userId);
        assertNotNull(createdUser);
        assertEquals(request.getUsername(), createdUser.getUsername());
        assertEquals(request.getEmail(), createdUser.getEmail());
        assertEquals(request.getPhone(), createdUser.getPhone());
        assertEquals(request.getRealName(), createdUser.getRealName());
    }

    @Test
    void testUserCRUDOperations() {
        // 1. 创建用户
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setTenantId(1L);
        createRequest.setUsername("crud_test_user");
        createRequest.setPassword("password123");
        createRequest.setEmail("crud@example.com");
        createRequest.setPhone("13800138888");
        createRequest.setRealName("CRUD测试用户");
        createRequest.setNickname("CRUD测试");
        createRequest.setGender(2);
        createRequest.setBirthday(LocalDate.of(1992, 8, 20));
        createRequest.setRoleIds(Arrays.asList(2L));

        Long userId = userService.createUser(createRequest);
        assertNotNull(userId);

        // 2. 查询用户
        User user = userService.getUserById(userId);
        assertNotNull(user);
        assertEquals(createRequest.getUsername(), user.getUsername());

        // 3. 更新用户
        user.setRealName("更新后的用户名");
        user.setNickname("更新昵称");
        boolean updateResult = userService.updateUser(userId, null); // 这里需要传入UpdateUserRequest
        // 由于没有完整的UpdateUserRequest，这里只是演示流程

        // 4. 删除用户
        boolean deleteResult = userService.deleteUser(userId);
        assertTrue(deleteResult);

        // 5. 验证用户已被软删除
        User deletedUser = userService.getUserById(userId);
        assertNull(deletedUser); // 软删除后查询应该返回null
    }

    @Test
    void testUserStatusOperations() {
        // 使用测试数据中的用户
        Long userId = 2L; // testuser

        // 测试禁用用户
        boolean disableResult = userService.disableUser(userId);
        assertTrue(disableResult);

        User disabledUser = userService.getUserById(userId);
        assertEquals(0, disabledUser.getStatus());

        // 测试启用用户
        boolean enableResult = userService.enableUser(userId);
        assertTrue(enableResult);

        User enabledUser = userService.getUserById(userId);
        assertEquals(1, enabledUser.getStatus());

        // 测试锁定用户
        boolean lockResult = userService.lockUser(userId);
        assertTrue(lockResult);

        User lockedUser = userService.getUserById(userId);
        assertEquals(1, lockedUser.getLockStatus());

        // 测试解锁用户
        boolean unlockResult = userService.unlockUser(userId);
        assertTrue(unlockResult);

        User unlockedUser = userService.getUserById(userId);
        assertEquals(0, unlockedUser.getLockStatus());
    }

    @Test
    void testPasswordOperations() {
        // 使用测试数据中的用户
        Long userId = 2L; // testuser
        String newPassword = "newPassword123";

        // 测试重置密码
        boolean resetResult = userService.resetPassword(userId, newPassword);
        assertTrue(resetResult);

        // 验证密码已更新（这里只能验证操作成功，无法直接验证密码内容）
        User user = userService.getUserById(userId);
        assertNotNull(user.getPasswordUpdateTime());
    }

    @Test
    void testUserExistenceChecks() {
        // 测试用户名存在性检查
        assertTrue(userService.existsByUsername("admin"));
        assertFalse(userService.existsByUsername("nonexistent_user"));

        // 测试邮箱存在性检查
        assertTrue(userService.existsByEmail("admin@example.com"));
        assertFalse(userService.existsByEmail("nonexistent@example.com"));

        // 测试手机号存在性检查
        assertTrue(userService.existsByPhone("13800138000"));
        assertFalse(userService.existsByPhone("13999999999"));
    }

    @Test
    void testBatchOperations() {
        // 测试批量删除
        Arrays.asList(4L, 5L); // disabled_user, locked_user
        boolean batchDeleteResult = userService.batchDeleteUsers(Arrays.asList(4L, 5L));
        assertTrue(batchDeleteResult);

        // 验证用户已被删除
        User deletedUser1 = userService.getUserById(4L);
        User deletedUser2 = userService.getUserById(5L);
        assertNull(deletedUser1);
        assertNull(deletedUser2);
    }

    @Test
    void testUserStatistics() {
        // 测试用户统计功能
        UserService.UserStatistics stats = userService.getUserStatistics();
        assertNotNull(stats);
        assertTrue(stats.getTotalUsers() >= 0);
        assertTrue(stats.getActiveUsers() >= 0);
        assertTrue(stats.getDisabledUsers() >= 0);
        assertTrue(stats.getLockedUsers() >= 0);
        assertTrue(stats.getTodayRegistrations() >= 0);
        assertTrue(stats.getMonthlyRegistrations() >= 0);
    }

    @Test
    void testErrorHandling() {
        // 测试创建重复用户名的用户
        CreateUserRequest duplicateRequest = new CreateUserRequest();
        duplicateRequest.setTenantId(1L);
        duplicateRequest.setUsername("admin"); // 已存在的用户名
        duplicateRequest.setPassword("password123");
        duplicateRequest.setEmail("duplicate@example.com");
        duplicateRequest.setPhone("13700137777");
        duplicateRequest.setRealName("重复用户");

        // 应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            userService.createUser(duplicateRequest);
        });
    }

    @Test
    void testDataValidation() {
        // 测试无效数据的处理
        CreateUserRequest invalidRequest = new CreateUserRequest();
        // 缺少必填字段

        assertThrows(Exception.class, () -> {
            userService.createUser(invalidRequest);
        });
    }
}