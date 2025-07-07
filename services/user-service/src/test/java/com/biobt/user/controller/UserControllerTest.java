package com.biobt.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.CreateUserRequest;
import com.biobt.user.domain.dto.UpdateUserRequest;
import com.biobt.user.domain.dto.UserPageRequest;
import com.biobt.user.domain.entity.User;
import com.biobt.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户控制器测试
 *
 * @author biobt
 * @since 2024-01-01
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreateUser() throws Exception {
        // Given
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(1L));

        verify(userService).createUser(any(CreateUserRequest.class));
    }

    @Test
    void testGetUserById() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(testUser);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    void testUpdateUser() throws Exception {
        // Given
        when(userService.updateUser(eq(1L), any(UpdateUserRequest.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).updateUser(eq(1L), any(UpdateUserRequest.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Given
        when(userService.deleteUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).deleteUser(1L);
    }

    @Test
    void testGetUserPage() throws Exception {
        // Given
        Page<User> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(testUser));
        page.setTotal(1);
        
        when(userService.getUserPage(any(UserPageRequest.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/users")
                .param("pageNum", "1")
                .param("pageSize", "10")
                .param("username", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[0].id").value(1L));

        verify(userService).getUserPage(any(UserPageRequest.class));
    }

    @Test
    void testBatchDeleteUsers() throws Exception {
        // Given
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        when(userService.batchDeleteUsers(userIds)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/users/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).batchDeleteUsers(userIds);
    }

    @Test
    void testEnableUser() throws Exception {
        // Given
        when(userService.enableUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/users/1/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).enableUser(1L);
    }

    @Test
    void testDisableUser() throws Exception {
        // Given
        when(userService.disableUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/users/1/disable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).disableUser(1L);
    }

    @Test
    void testLockUser() throws Exception {
        // Given
        when(userService.lockUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/users/1/lock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).lockUser(1L);
    }

    @Test
    void testUnlockUser() throws Exception {
        // Given
        when(userService.unlockUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/users/1/unlock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).unlockUser(1L);
    }

    @Test
    void testResetPassword() throws Exception {
        // Given
        when(userService.resetPassword(eq(1L), anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/users/1/reset-password")
                .param("newPassword", "newPassword123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).resetPassword(eq(1L), eq("newPassword123"));
    }

    @Test
    void testExistsByUsername() throws Exception {
        // Given
        when(userService.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/users/exists/username")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).existsByUsername("testuser");
    }

    @Test
    void testExistsByEmail() throws Exception {
        // Given
        when(userService.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/users/exists/email")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).existsByEmail("test@example.com");
    }

    @Test
    void testExistsByPhone() throws Exception {
        // Given
        when(userService.existsByPhone("13800138000")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/users/exists/phone")
                .param("phone", "13800138000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).existsByPhone("13800138000");
    }

    @Test
    void testCreateUserWithInvalidData() throws Exception {
        // Given
        CreateUserRequest invalidRequest = new CreateUserRequest();
        // 缺少必填字段

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(CreateUserRequest.class));
    }

    @Test
    void testGetNonExistentUser() throws Exception {
        // Given
        when(userService.getUserById(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(999L);
    }
}