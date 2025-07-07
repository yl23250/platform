package com.biobt.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.dto.*;
import com.biobt.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户控制器集成测试类
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;
    private UserPageRequest pageRequest;

    @BeforeEach
    void setUp() {
        // 初始化用户响应
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setTenantId(0L);
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");
        userResponse.setPhone("13800138000");
        userResponse.setRealName("测试用户");
        userResponse.setNickname("测试");
        userResponse.setGender(1);
        userResponse.setBirthday(LocalDate.of(1990, 1, 1));
        userResponse.setStatus(1);
        userResponse.setIsLocked(0);
        userResponse.setCreatedTime(LocalDateTime.now());
        userResponse.setCreatedBy(1L);
        userResponse.setUpdatedTime(LocalDateTime.now());
        userResponse.setUpdatedBy(1L);

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

        // 初始化分页请求
        pageRequest = new UserPageRequest();
        pageRequest.setPageNum(1);
        pageRequest.setPageSize(10);
        pageRequest.setTenantId(0L);
    }

    @Test
    @WithMockUser(authorities = "user:create")
    void testCreateUser_Success() throws Exception {
        // Given
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(authorities = "user:create")
    void testCreateUser_ValidationError() throws Exception {
        // Given
        createRequest.setUsername(""); // 设置无效用户名

        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testGetUserById_Success() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testGetUserByUsername_Success() throws Exception {
        // Given
        when(userService.getUserByUsername("testuser", 0L)).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/users/username/testuser")
                        .param("tenantId", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @WithMockUser
    void testGetCurrentUser_Success() throws Exception {
        // Given
        when(userService.getCurrentUser()).thenReturn(userResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/users/current"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testGetUserPage_Success() throws Exception {
        // Given
        Page<UserResponse> page = new Page<>(1, 10, 1);
        page.setRecords(Arrays.asList(userResponse));
        when(userService.getUserPage(any(UserPageRequest.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(post("/api/v1/users/page")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.records[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(authorities = "user:update")
    void testUpdateUser_Success() throws Exception {
        // Given
        UserResponse updatedResponse = new UserResponse();
        updatedResponse.setId(1L);
        updatedResponse.setUsername("testuser");
        updatedResponse.setEmail("updated@example.com");
        when(userService.updateUser(eq(1L), any(UpdateUserRequest.class))).thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("updated@example.com"));
    }

    @Test
    @WithMockUser(authorities = "user:delete")
    void testDeleteUser_Success() throws Exception {
        // Given
        when(userService.deleteUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/v1/users/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:delete")
    void testBatchDeleteUsers_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(userService.batchDeleteUsers(ids)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/v1/users/batch")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:update")
    void testEnableUser_Success() throws Exception {
        // Given
        when(userService.enableUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1/enable")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:update")
    void testDisableUser_Success() throws Exception {
        // Given
        when(userService.disableUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1/disable")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:update")
    void testLockUser_Success() throws Exception {
        // Given
        when(userService.lockUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1/lock")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:update")
    void testUnlockUser_Success() throws Exception {
        // Given
        when(userService.unlockUser(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1/unlock")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:reset-password")
    void testResetPassword_Success() throws Exception {
        // Given
        when(userService.resetPassword(1L, "newpassword")).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1/reset-password")
                        .with(csrf())
                        .param("newPassword", "newpassword"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser
    void testChangePassword_Success() throws Exception {
        // Given
        ChangePasswordRequest changeRequest = new ChangePasswordRequest();
        changeRequest.setOldPassword("oldpassword");
        changeRequest.setNewPassword("newpassword");
        changeRequest.setConfirmPassword("newpassword");
        when(userService.changePassword(1L, "oldpassword", "newpassword")).thenReturn(true);

        // When & Then
        mockMvc.perform(put("/api/v1/users/1/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testExistsByUsername_Success() throws Exception {
        // Given
        when(userService.existsByUsername("testuser", 0L, null)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/v1/users/exists/username")
                        .param("username", "testuser")
                        .param("tenantId", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testGetUserPermissions_Success() throws Exception {
        // Given
        List<String> permissions = Arrays.asList("user:read", "user:write", "user:delete");
        when(userService.getUserPermissions(1L)).thenReturn(permissions);

        // When & Then
        mockMvc.perform(get("/api/v1/users/1/permissions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0]").value("user:read"))
                .andExpect(jsonPath("$.data[1]").value("user:write"))
                .andExpect(jsonPath("$.data[2]").value("user:delete"));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testHasPermission_Success() throws Exception {
        // Given
        when(userService.hasPermission(1L, "user:read")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/v1/users/1/permissions/user:read"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(authorities = "user:read")
    void testGetUserStatistics_Success() throws Exception {
        // Given
        UserService.UserStatistics statistics = new UserService.UserStatistics(100L, 80L, 20L, 5L, 10L, 3L);
        when(userService.getUserStatistics(0L)).thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/api/v1/users/statistics")
                        .param("tenantId", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(100))
                .andExpect(jsonPath("$.data.enabledCount").value(80))
                .andExpect(jsonPath("$.data.disabledCount").value(20))
                .andExpect(jsonPath("$.data.lockedCount").value(5));
    }

    @Test
    @WithMockUser(authorities = "user:export")
    void testExportUsers_Success() throws Exception {
        // Given
        List<UserResponse> users = Arrays.asList(userResponse);
        when(userService.exportUsers(any(UserPageRequest.class))).thenReturn(users);

        // When & Then
        mockMvc.perform(post("/api/v1/users/export")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(authorities = "user:import")
    void testImportUsers_Success() throws Exception {
        // Given
        List<CreateUserRequest> users = Arrays.asList(createRequest);
        UserService.ImportResult importResult = new UserService.ImportResult();
        importResult.setTotalCount(1);
        importResult.setSuccessCount(1);
        importResult.setFailureCount(0);
        when(userService.importUsers(any())).thenReturn(importResult);

        // When & Then
        mockMvc.perform(post("/api/v1/users/import")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(users)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(1))
                .andExpect(jsonPath("$.data.successCount").value(1))
                .andExpect(jsonPath("$.data.failureCount").value(0));
    }

    @Test
    void testCreateUser_Unauthorized() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "other:permission")
    void testCreateUser_Forbidden() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}