package com.biobt.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.CreateUserRequest;
import com.biobt.user.domain.dto.UpdateUserRequest;
import com.biobt.user.domain.dto.UserPageRequest;
import com.biobt.user.domain.entity.User;
import com.biobt.user.mapper.UserMapper;
import com.biobt.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户服务实现类测试
 *
 * @author biobt
 * @since 2024-01-01
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testUser = new User();
        testUser.setId(1L);
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
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
        testUser.setDeleted(0);
        testUser.setVersion(1);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setTenantId(1L);
        createUserRequest.setUsername("newuser");
        createUserRequest.setPassword("password123");
        createUserRequest.setEmail("newuser@example.com");
        createUserRequest.setPhone("13900139000");
        createUserRequest.setRealName("新用户");
        createUserRequest.setNickname("新用户昵称");
        createUserRequest.setGender(1);
        createUserRequest.setBirthday(LocalDate.of(1995, 5, 15));
        createUserRequest.setRoleIds(Arrays.asList(1L, 2L));

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("updated@example.com");
        updateUserRequest.setPhone("13700137000");
        updateUserRequest.setRealName("更新用户");
        updateUserRequest.setNickname("更新昵称");
        updateUserRequest.setGender(2);
        updateUserRequest.setStatus(1);
        updateUserRequest.setLockStatus(0);
        updateUserRequest.setRoleIds(Arrays.asList(2L, 3L));
    }

    @Test
    void testCreateUser() {
        // Given
        when(userMapper.selectByUsernameIncludeDeleted(anyString())).thenReturn(null);
        when(userMapper.selectByEmailIncludeDeleted(anyString())).thenReturn(null);
        when(userMapper.selectByPhoneIncludeDeleted(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // When
        Long userId = userService.createUser(createUserRequest);

        // Then
        assertNotNull(userId);
        verify(userMapper).selectByUsernameIncludeDeleted(createUserRequest.getUsername());
        verify(userMapper).selectByEmailIncludeDeleted(createUserRequest.getEmail());
        verify(userMapper).selectByPhoneIncludeDeleted(createUserRequest.getPhone());
        verify(passwordEncoder).encode(createUserRequest.getPassword());
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testCreateUserWithDuplicateUsername() {
        // Given
        when(userMapper.selectByUsernameIncludeDeleted(anyString())).thenReturn(testUser);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userService.createUser(createUserRequest);
        });

        verify(userMapper).selectByUsernameIncludeDeleted(createUserRequest.getUsername());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testGetUserById() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // When
        User result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userMapper).selectById(1L);
    }

    @Test
    void testGetUserByUsername() {
        // Given
        when(userMapper.selectOne(any())).thenReturn(testUser);

        // When
        User result = userService.getUserByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userMapper).selectOne(any());
    }

    @Test
    void testUpdateUser() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.selectByEmailIncludeDeleted(anyString())).thenReturn(null);
        when(userMapper.selectByPhoneIncludeDeleted(anyString())).thenReturn(null);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        boolean result = userService.updateUser(1L, updateUserRequest);

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testDeleteUser() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        boolean result = userService.deleteUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testBatchDeleteUsers() {
        // Given
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        when(userMapper.batchSoftDelete(eq(userIds), anyString())).thenReturn(3);

        // When
        boolean result = userService.batchDeleteUsers(userIds);

        // Then
        assertTrue(result);
        verify(userMapper).batchSoftDelete(eq(userIds), anyString());
    }

    @Test
    void testEnableUser() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        boolean result = userService.enableUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testDisableUser() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        boolean result = userService.disableUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testLockUser() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        boolean result = userService.lockUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testUnlockUser() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // When
        boolean result = userService.unlockUser(1L);

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    void testResetPassword() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userMapper.updatePassword(eq(1L), anyString(), any(LocalDateTime.class), anyString())).thenReturn(1);

        // When
        boolean result = userService.resetPassword(1L, "newPassword123");

        // Then
        assertTrue(result);
        verify(userMapper).selectById(1L);
        verify(passwordEncoder).encode("newPassword123");
        verify(userMapper).updatePassword(eq(1L), anyString(), any(LocalDateTime.class), anyString());
    }

    @Test
    void testExistsByUsername() {
        // Given
        when(userMapper.selectOne(any())).thenReturn(testUser);

        // When
        boolean result = userService.existsByUsername("testuser");

        // Then
        assertTrue(result);
        verify(userMapper).selectOne(any());
    }

    @Test
    void testExistsByEmail() {
        // Given
        when(userMapper.selectOne(any())).thenReturn(testUser);

        // When
        boolean result = userService.existsByEmail("test@example.com");

        // Then
        assertTrue(result);
        verify(userMapper).selectOne(any());
    }

    @Test
    void testExistsByPhone() {
        // Given
        when(userMapper.selectOne(any())).thenReturn(testUser);

        // When
        boolean result = userService.existsByPhone("13800138000");

        // Then
        assertTrue(result);
        verify(userMapper).selectOne(any());
    }

    @Test
    void testGetUserPage() {
        // Given
        UserPageRequest pageRequest = new UserPageRequest();
        pageRequest.setPageNum(1);
        pageRequest.setPageSize(10);
        pageRequest.setUsername("test");
        
        Page<User> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(testUser));
        page.setTotal(1);
        
        when(userMapper.selectUserPage(any(IPage.class), eq(pageRequest))).thenReturn(page);

        // When
        IPage<User> result = userService.getUserPage(pageRequest);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getRecords().size());
        verify(userMapper).selectUserPage(any(IPage.class), eq(pageRequest));
    }

    @Test
    void testUpdateLoginInfo() {
        // Given
        LocalDateTime loginTime = LocalDateTime.now();
        String loginIp = "192.168.1.1";
        when(userMapper.updateLoginInfo(1L, loginTime, loginIp, 1)).thenReturn(1);

        // When
        boolean result = userService.updateLoginInfo(1L, loginTime, loginIp);

        // Then
        assertTrue(result);
        verify(userMapper).updateLoginInfo(1L, loginTime, loginIp, 1);
    }
}