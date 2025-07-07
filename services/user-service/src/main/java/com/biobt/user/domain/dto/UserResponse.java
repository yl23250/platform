package com.biobt.user.domain.dto;

import com.biobt.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户响应信息")
public class UserResponse {
    
    @Schema(description = "用户ID", example = "1")
    private Long id;
    
    @Schema(description = "用户名", example = "john_doe")
    private String username;
    
    @Schema(description = "真实姓名", example = "张三")
    private String realName;
    
    @Schema(description = "昵称", example = "小张")
    private String nickname;
    
    @Schema(description = "邮箱", example = "john@example.com")
    private String email;
    
    @Schema(description = "手机号", example = "13800138000")
    private String phone;
    
    @Schema(description = "头像URL")
    private String avatar;
    
    @Schema(description = "性别", example = "M")
    private String gender;
    
    @Schema(description = "性别显示名称", example = "男")
    private String genderName;
    
    @Schema(description = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;
    
    @Schema(description = "部门ID", example = "1")
    private Long deptId;
    
    @Schema(description = "部门名称", example = "技术部")
    private String deptName;
    
    @Schema(description = "职位", example = "软件工程师")
    private String position;
    
    @Schema(description = "用户状态", example = "1")
    private Integer status;
    
    @Schema(description = "用户状态名称", example = "正常")
    private String statusName;
    
    @Schema(description = "是否为管理员", example = "false")
    private Boolean isAdmin;
    
    @Schema(description = "是否锁定", example = "false")
    private Boolean isLocked;
    
    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    
    @Schema(description = "最后登录IP", example = "192.168.1.100")
    private String lastLoginIp;
    
    @Schema(description = "登录次数", example = "10")
    private Integer loginCount;
    
    @Schema(description = "账号过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
    
    @Schema(description = "个人简介")
    private String bio;
    
    @Schema(description = "角色列表")
    private List<RoleInfo> roles;
    
    @Schema(description = "权限列表")
    private List<String> permissions;
    
    @Schema(description = "租户ID", example = "1")
    private Long tenantId;
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @Schema(description = "创建人")
    private String createBy;
    
    @Schema(description = "更新人")
    private String updateBy;
    
    @Schema(description = "备注")
    private String remark;
    
    /**
     * 角色信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "角色信息")
    public static class RoleInfo {
        @Schema(description = "角色ID", example = "1")
        private Long id;
        
        @Schema(description = "角色编码", example = "ADMIN")
        private String code;
        
        @Schema(description = "角色名称", example = "管理员")
        private String name;
        
        @Schema(description = "角色描述")
        private String description;
    }
    
    /**
     * 手动实现的Builder模式
     */
    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }
    
    public static class UserResponseBuilder {
        private UserResponse userResponse = new UserResponse();
        
        public UserResponseBuilder id(Long id) {
            userResponse.id = id;
            return this;
        }
        
        public UserResponseBuilder username(String username) {
            userResponse.username = username;
            return this;
        }
        
        public UserResponseBuilder realName(String realName) {
            userResponse.realName = realName;
            return this;
        }
        
        public UserResponseBuilder nickname(String nickname) {
            userResponse.nickname = nickname;
            return this;
        }
        
        public UserResponseBuilder email(String email) {
            userResponse.email = email;
            return this;
        }
        
        public UserResponseBuilder phone(String phone) {
            userResponse.phone = phone;
            return this;
        }
        
        public UserResponseBuilder avatar(String avatar) {
            userResponse.avatar = avatar;
            return this;
        }
        
        public UserResponseBuilder gender(String gender) {
            userResponse.gender = gender;
            return this;
        }
        
        public UserResponseBuilder genderName(String genderName) {
            userResponse.genderName = genderName;
            return this;
        }
        
        public UserResponseBuilder birthday(LocalDateTime birthday) {
            userResponse.birthday = birthday;
            return this;
        }
        
        public UserResponseBuilder deptId(Long deptId) {
            userResponse.deptId = deptId;
            return this;
        }
        
        public UserResponseBuilder deptName(String deptName) {
            userResponse.deptName = deptName;
            return this;
        }
        
        public UserResponseBuilder position(String position) {
            userResponse.position = position;
            return this;
        }
        
        public UserResponseBuilder status(Integer status) {
            userResponse.status = status;
            return this;
        }
        
        public UserResponseBuilder statusName(String statusName) {
            userResponse.statusName = statusName;
            return this;
        }
        
        public UserResponseBuilder isAdmin(Boolean isAdmin) {
            userResponse.isAdmin = isAdmin;
            return this;
        }
        
        public UserResponseBuilder isLocked(Boolean isLocked) {
            userResponse.isLocked = isLocked;
            return this;
        }
        
        public UserResponseBuilder lastLoginTime(LocalDateTime lastLoginTime) {
            userResponse.lastLoginTime = lastLoginTime;
            return this;
        }
        
        public UserResponseBuilder lastLoginIp(String lastLoginIp) {
            userResponse.lastLoginIp = lastLoginIp;
            return this;
        }
        
        public UserResponseBuilder loginCount(Integer loginCount) {
            userResponse.loginCount = loginCount;
            return this;
        }
        
        public UserResponseBuilder expireTime(LocalDateTime expireTime) {
            userResponse.expireTime = expireTime;
            return this;
        }
        
        public UserResponseBuilder bio(String bio) {
            userResponse.bio = bio;
            return this;
        }
        
        public UserResponseBuilder roles(List<RoleInfo> roles) {
            userResponse.roles = roles;
            return this;
        }
        
        public UserResponseBuilder permissions(List<String> permissions) {
            userResponse.permissions = permissions;
            return this;
        }
        
        public UserResponseBuilder tenantId(Long tenantId) {
            userResponse.tenantId = tenantId;
            return this;
        }
        
        public UserResponseBuilder createTime(LocalDateTime createTime) {
            userResponse.createTime = createTime;
            return this;
        }
        
        public UserResponseBuilder updateTime(LocalDateTime updateTime) {
            userResponse.updateTime = updateTime;
            return this;
        }
        
        public UserResponseBuilder createBy(String createBy) {
            userResponse.createBy = createBy;
            return this;
        }
        
        public UserResponseBuilder updateBy(String updateBy) {
            userResponse.updateBy = updateBy;
            return this;
        }
        
        public UserResponseBuilder remark(String remark) {
            userResponse.remark = remark;
            return this;
        }
        
        public UserResponse build() {
            return userResponse;
        }
    }

    /**
     * 从User实体转换为UserResponse
     */
    public static UserResponse from(User user) {
        if (user == null) {
            return null;
        }
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .genderName(getGenderName(user.getGender()))
                .birthday(user.getBirthday())
                .deptId(user.getDeptId())
                .position(user.getPosition())
                .status(user.getStatus())
                .statusName(getStatusName(user.getStatus()))
                .isAdmin(user.getIsAdmin())
                .isLocked(user.getIsLocked())
                .lastLoginTime(user.getLastLoginTime())
                .lastLoginIp(user.getLastLoginIp())
                .loginCount(user.getLoginCount())
                .expireTime(user.getExpireTime())
                .bio(user.getBio())
                .tenantId(user.getTenantId())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .createBy(user.getCreateBy())
                .updateBy(user.getUpdateBy())
                .remark(user.getRemark())
                .build();
    }
    
    /**
     * 获取性别显示名称
     */
    private static String getGenderName(String gender) {
        if (gender == null) {
            return "未知";
        }
        switch (gender) {
            case "M":
                return "男";
            case "F":
                return "女";
            default:
                return "未知";
        }
    }
    
    /**
     * 获取状态显示名称
     */
    private static String getStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        return status == 1 ? "正常" : "禁用";
    }
    
    /**
     * 简化版用户信息（用于列表显示）
     */
    public static UserResponse simple(User user) {
        if (user == null) {
            return null;
        }
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .statusName(getStatusName(user.getStatus()))
                .isLocked(user.getIsLocked())
                .lastLoginTime(user.getLastLoginTime())
                .createTime(user.getCreateTime())
                .build();
    }
}