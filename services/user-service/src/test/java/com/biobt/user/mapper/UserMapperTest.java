package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.UserPageRequest;
import com.biobt.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户Mapper测试
 *
 * @author biobt
 * @since 2024-01-01
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setTenantId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setRealName("测试用户");
        testUser.setNickname("测试");
        testUser.setGender(1);
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        testUser.setStatus(1);
        testUser.setLockStatus(0);
        testUser.setPasswordUpdateTime(LocalDateTime.now());
        testUser.setRemarks("测试用户备注");
        testUser.setCreateBy("admin");
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateBy("admin");
        testUser.setUpdateTime(LocalDateTime.now());
        testUser.setDeleted(0);
        testUser.setVersion(1);
    }

    @Test
    void testInsertUser() {
        // When
        int result = userMapper.insert(testUser);

        // Then
        assertEquals(1, result);
        assertNotNull(testUser.getId());
        assertTrue(testUser.getId() > 0);
    }

    @Test
    void testSelectById() {
        // Given
        userMapper.insert(testUser);
        Long userId = testUser.getId();

        // When
        User result = userMapper.selectById(userId);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getPhone(), result.getPhone());
    }

    @Test
    void testSelectByUsernameIncludeDeleted() {
        // Given
        userMapper.insert(testUser);

        // When
        User result = userMapper.selectByUsernameIncludeDeleted(testUser.getUsername());

        // Then
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void testSelectByEmailIncludeDeleted() {
        // Given
        userMapper.insert(testUser);

        // When
        User result = userMapper.selectByEmailIncludeDeleted(testUser.getEmail());

        // Then
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
    }

    @Test
    void testSelectByPhoneIncludeDeleted() {
        // Given
        userMapper.insert(testUser);

        // When
        User result = userMapper.selectByPhoneIncludeDeleted(testUser.getPhone());

        // Then
        assertNotNull(result);
        assertEquals(testUser.getPhone(), result.getPhone());
    }

    @Test
    void testSelectUserPage() {
        // Given
        userMapper.insert(testUser);
        
        UserPageRequest request = new UserPageRequest();
        request.setPageNum(1);
        request.setPageSize(10);
        request.setUsername("test");
        
        Page<User> page = new Page<>(1, 10);

        // When
        IPage<User> result = userMapper.selectUserPage(page, request);

        // Then
        assertNotNull(result);
        assertTrue(result.getTotal() >= 1);
        assertFalse(result.getRecords().isEmpty());
    }

    @Test
    void testSelectUserWithRoles() {
        // Given
        userMapper.insert(testUser);
        Long userId = testUser.getId();

        // When
        Map<String, Object> result = userMapper.selectUserWithRoles(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.get("userId"));
        assertEquals(testUser.getUsername(), result.get("username"));
    }

    @Test
    void testSelectUserPermissions() {
        // Given
        userMapper.insert(testUser);
        Long userId = testUser.getId();

        // When
        List<String> result = userMapper.selectUserPermissions(userId);

        // Then
        assertNotNull(result);
        // 权限列表可能为空，这取决于测试数据
    }

    @Test
    void testCountByTenantId() {
        // Given
        userMapper.insert(testUser);

        // When
        Long count = userMapper.countByTenantId(testUser.getTenantId());

        // Then
        assertNotNull(count);
        assertTrue(count >= 1);
    }

    @Test
    void testCountByStatus() {
        // Given
        userMapper.insert(testUser);

        // When
        Long count = userMapper.countByStatus(1);

        // Then
        assertNotNull(count);
        assertTrue(count >= 1);
    }

    @Test
    void testCountByLockStatus() {
        // Given
        userMapper.insert(testUser);

        // When
        Long count = userMapper.countByLockStatus(0);

        // Then
        assertNotNull(count);
        assertTrue(count >= 1);
    }

    @Test
    void testCountTodayRegistrations() {
        // Given
        userMapper.insert(testUser);
        LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusDays(1);

        // When
        Long count = userMapper.countTodayRegistrations(startTime, endTime);

        // Then
        assertNotNull(count);
        assertTrue(count >= 1);
    }

    @Test
    void testCountMonthlyRegistrations() {
        // Given
        userMapper.insert(testUser);
        LocalDateTime startTime = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusMonths(1);

        // When
        Long count = userMapper.countMonthlyRegistrations(startTime, endTime);

        // Then
        assertNotNull(count);
        assertTrue(count >= 1);
    }

    @Test
    void testBatchSoftDelete() {
        // Given
        userMapper.insert(testUser);
        List<Long> userIds = Arrays.asList(testUser.getId());

        // When
        int result = userMapper.batchSoftDelete(userIds, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证用户已被软删除
        User deletedUser = userMapper.selectById(testUser.getId());
        assertNull(deletedUser); // MyBatis Plus会自动过滤已删除的记录
    }

    @Test
    void testBatchEnable() {
        // Given
        testUser.setStatus(0); // 先设置为禁用状态
        userMapper.insert(testUser);
        List<Long> userIds = Arrays.asList(testUser.getId());

        // When
        int result = userMapper.batchEnable(userIds, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证用户状态已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(1, updatedUser.getStatus());
    }

    @Test
    void testBatchDisable() {
        // Given
        userMapper.insert(testUser);
        List<Long> userIds = Arrays.asList(testUser.getId());

        // When
        int result = userMapper.batchDisable(userIds, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证用户状态已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(0, updatedUser.getStatus());
    }

    @Test
    void testBatchLock() {
        // Given
        userMapper.insert(testUser);
        List<Long> userIds = Arrays.asList(testUser.getId());

        // When
        int result = userMapper.batchLock(userIds, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证用户锁定状态已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(1, updatedUser.getLockStatus());
    }

    @Test
    void testBatchUnlock() {
        // Given
        testUser.setLockStatus(1); // 先设置为锁定状态
        userMapper.insert(testUser);
        List<Long> userIds = Arrays.asList(testUser.getId());

        // When
        int result = userMapper.batchUnlock(userIds, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证用户锁定状态已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(0, updatedUser.getLockStatus());
    }

    @Test
    void testUpdateLoginInfo() {
        // Given
        userMapper.insert(testUser);
        LocalDateTime loginTime = LocalDateTime.now();
        String loginIp = "192.168.1.1";
        Integer loginCount = 5;

        // When
        int result = userMapper.updateLoginInfo(testUser.getId(), loginTime, loginIp, loginCount);

        // Then
        assertEquals(1, result);
        
        // 验证登录信息已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(loginIp, updatedUser.getLastLoginIp());
        assertEquals(loginCount, updatedUser.getLoginCount());
    }

    @Test
    void testUpdatePassword() {
        // Given
        userMapper.insert(testUser);
        String newPassword = "newEncodedPassword";
        LocalDateTime passwordUpdateTime = LocalDateTime.now();

        // When
        int result = userMapper.updatePassword(testUser.getId(), newPassword, passwordUpdateTime, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证密码已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(newPassword, updatedUser.getPassword());
    }

    @Test
    void testSelectRecentLoginUsers() {
        // Given
        testUser.setLastLoginTime(LocalDateTime.now().minusDays(1));
        userMapper.insert(testUser);

        // When
        List<User> result = userMapper.selectRecentLoginUsers(7, 10);

        // Then
        assertNotNull(result);
        assertTrue(result.size() >= 1);
    }

    @Test
    void testSelectLongTimeNoLoginUsers() {
        // Given
        testUser.setLastLoginTime(LocalDateTime.now().minusDays(100));
        userMapper.insert(testUser);

        // When
        List<User> result = userMapper.selectLongTimeNoLoginUsers(30, 10);

        // Then
        assertNotNull(result);
        assertTrue(result.size() >= 1);
    }

    @Test
    void testBatchInsert() {
        // Given
        User user2 = new User();
        user2.setTenantId(1L);
        user2.setUsername("testuser2");
        user2.setPassword("encodedPassword2");
        user2.setEmail("test2@example.com");
        user2.setPhone("13800138001");
        user2.setRealName("测试用户2");
        user2.setStatus(1);
        user2.setLockStatus(0);
        user2.setCreateBy("admin");
        user2.setCreateTime(LocalDateTime.now());
        
        List<User> users = Arrays.asList(testUser, user2);

        // When
        int result = userMapper.batchInsert(users);

        // Then
        assertEquals(2, result);
    }

    @Test
    void testBatchUpdateStatus() {
        // Given
        userMapper.insert(testUser);
        List<Long> userIds = Arrays.asList(testUser.getId());

        // When
        int result = userMapper.batchUpdateStatus(userIds, 0, "admin");

        // Then
        assertEquals(1, result);
        
        // 验证状态已更新
        User updatedUser = userMapper.selectById(testUser.getId());
        assertEquals(0, updatedUser.getStatus());
    }
}