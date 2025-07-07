package com.biobt.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.biobt.user.dto.CreateUserRequest;
import com.biobt.user.dto.UpdateUserRequest;
import com.biobt.user.dto.UserPageRequest;
import com.biobt.user.dto.UserResponse;
import com.biobt.user.entity.User;
import com.biobt.user.mapper.UserMapper;
import com.biobt.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户服务测试类
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setTenantId(0L);
        testUser.setUsername("testuser");
        testUser.setPassword("$2a$10$encrypted_password");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setRealName("测试用户");
        testUser.setNickname("测试");
        testUser.setGender(1);
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        testUser.setStatus(1);
        testUser.setIsLocked(0);
        testUser.setLoginCount(0);
        testUser.setCreatedTime(LocalDateTime.now());
        testUser.setCreatedBy(1L);
        testUser.setUpdatedTime(LocalDateTime.now());
        testUser.setUpdatedBy(1L);
        testUser.setDeleted(0);
        testUser.setVersion(1);

        // 初始化创建用户请求
        createRequest = new CreateUserRequest();
        createRequest.setTenantId(0L);
        createRequest.setUsername("newuser");
        createRequest.setPassword("123456");
        createRequest.setEmail("newuser@example.com");
        createRequest.setPhone("13900139000");
        createRequest.setRealName("新用户");
        createRequest.setNickname("新用户");
        createRequest.setGender(1);
        createRequest.setStatus(1);
        createRequest.setIsLocked(0);

        // 初始化更新用户请求
        updateRequest = new UpdateUserRequest();
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhone("13700137000");
        updateRequest.setRealName("更新用户");
        updateRequest.setNickname("更新");
        updateRequest.setGender(2);
        updateRequest.setStatus(1);
    }

    @Test
    void testCreateUser_Success() {
        // Given
        when(userMapper.selectByUsernameIncludeDeleted(anyString(), anyLong())).thenReturn(null);
        when(userMapper.selectByEmailIncludeDeleted(anyString(), anyLong())).thenReturn(null);
        when(userMapper.selectByPhoneIncludeDeleted(anyString(), anyLong())).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // When
        UserResponse response = userService.createUser(createRequest);

        // Then
        assertNotNull(response);
        assertEquals(createRequest.getUsername(), response.getUsername());
        assertEquals(createRequest.getEmail(), response.getEmail());
        assertEquals(createRequest.getPhone(), response.getPhone());
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void testCreateUser_UsernameExists() {
        // Given
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setUsername(createRequest.getUsername());
        existingUser.setDeleted(0);
        when(userMapper.selectByUsernameIncludeDeleted(anyString(), anyLong())).thenReturn(existingUser);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(createRequest);
        });
        assertEquals("用户名已存在", exception.getMessage());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // When
        UserResponse response = userService.getUserById(1L);

        // Then
        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getUsername(), response.getUsername());
        assertEquals(testUser.getEmail(), response.getEmail());
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userMapper.selectById(999L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(999L);
        });
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.selectByEmailIncludeDeleted(anyString(), anyLong())).thenReturn(null);
        when(userMapper.selectByPhoneIncludeDeleted(anyString(), anyLong())).thenReturn(null);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        UserResponse response = userService.updateUser(1L, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals(updateRequest.getEmail(), response.getEmail());
        assertEquals(updateRequest.getPhone(), response.getPhone());
        assertEquals(updateRequest.getRealName(), response.getRealName());
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        Boolean result = userService.deleteUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void testBatchDeleteUsers_Success() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(userMapper.batchSoftDelete(eq(ids), anyLong())).thenReturn(3);

        // When
        Boolean result = userService.batchDeleteUsers(ids);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).batchSoftDelete(eq(ids), anyLong());
    }

    @Test
    void testEnableUser_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        Boolean result = userService.enableUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void testDisableUser_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        Boolean result = userService.disableUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void testLockUser_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        Boolean result = userService.lockUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void testUnlockUser_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        Boolean result = userService.unlockUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateById(any(User.class));
    }

    @Test
    void testResetPassword_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updatePassword(eq(1L), anyString(), anyLong())).thenReturn(1);

        // When
        Boolean result = userService.resetPassword(1L, "newpassword");

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updatePassword(eq(1L), anyString(), anyLong());
    }

    @Test
    void testExistsByUsername_True() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setDeleted(0);
        when(userMapper.selectByUsernameIncludeDeleted("testuser", 0L)).thenReturn(existingUser);

        // When
        Boolean exists = userService.existsByUsername("testuser", 0L, null);

        // Then
        assertTrue(exists);
    }

    @Test
    void testExistsByUsername_False() {
        // Given
        when(userMapper.selectByUsernameIncludeDeleted("nonexistent", 0L)).thenReturn(null);

        // When
        Boolean exists = userService.existsByUsername("nonexistent", 0L, null);

        // Then
        assertFalse(exists);
    }

    @Test
    void testExistsByEmail_True() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("test@example.com");
        existingUser.setDeleted(0);
        when(userMapper.selectByEmailIncludeDeleted("test@example.com", 0L)).thenReturn(existingUser);

        // When
        Boolean exists = userService.existsByEmail("test@example.com", 0L, null);

        // Then
        assertTrue(exists);
    }

    @Test
    void testExistsByPhone_True() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPhone("13800138000");
        existingUser.setDeleted(0);
        when(userMapper.selectByPhoneIncludeDeleted("13800138000", 0L)).thenReturn(existingUser);

        // When
        Boolean exists = userService.existsByPhone("13800138000", 0L, null);

        // Then
        assertTrue(exists);
    }

    @Test
    void testUpdateLoginInfo_Success() {
        // Given
        when(userMapper.updateLoginInfo(eq(1L), eq("192.168.1.1"), any(LocalDateTime.class))).thenReturn(1);

        // When
        Boolean result = userService.updateLoginInfo(1L, "192.168.1.1");

        // Then
        assertTrue(result);
        verify(userMapper, times(1)).updateLoginInfo(eq(1L), eq("192.168.1.1"), any(LocalDateTime.class));
    }

    @Test
    void testGetUserPermissions_Success() {
        // Given
        List<String> permissions = Arrays.asList("user:read", "user:write", "user:delete");
        when(userMapper.selectUserPermissions(1L)).thenReturn(permissions);

        // When
        List<String> result = userService.getUserPermissions(1L);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("user:read"));
        assertTrue(result.contains("user:write"));
        assertTrue(result.contains("user:delete"));
    }

    @Test
    void testHasPermission_True() {
        // Given
        List<String> permissions = Arrays.asList("user:read", "user:write", "user:delete");
        when(userMapper.selectUserPermissions(1L)).thenReturn(permissions);

        // When
        Boolean hasPermission = userService.hasPermission(1L, "user:read");

        // Then
        assertTrue(hasPermission);
    }

    @Test
    void testHasPermission_False() {
        // Given
        List<String> permissions = Arrays.asList("user:read", "user:write");
        when(userMapper.selectUserPermissions(1L)).thenReturn(permissions);

        // When
        Boolean hasPermission = userService.hasPermission(1L, "admin:delete");

        // Then
        assertFalse(hasPermission);
    }

    @Test
    void testGetUserStatistics_Success() {
        // Given
        when(userMapper.countByTenantId(0L)).thenReturn(100L);
        when(userMapper.countByStatus(0L, 1)).thenReturn(80L);
        when(userMapper.countByStatus(0L, 0)).thenReturn(20L);
        when(userMapper.countByLockStatus(0L, 1)).thenReturn(5L);

        // When
        UserService.UserStatistics statistics = userService.getUserStatistics(0L);

        // Then
        assertNotNull(statistics);
        assertEquals(100L, statistics.getTotalCount());
        assertEquals(80L, statistics.getEnabledCount());
        assertEquals(20L, statistics.getDisabledCount());
        assertEquals(5L, statistics.getLockedCount());
    }
}