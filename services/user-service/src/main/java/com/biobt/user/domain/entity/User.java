package com.biobt.user.domain.entity;

import com.biobt.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user", indexes = {
    @Index(name = "idx_username", columnList = "username"),
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_phone", columnList = "phone"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
public class User extends BaseEntity {
    
    // 注意：tenantId继承自BaseEntity，类型为Long
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * 密码（加密后）
     */
    @NotBlank(message = "密码不能为空")
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    
    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    @Column(name = "real_name", length = 50)
    private String realName;
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Column(name = "nickname", length = 50)
    private String nickname;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone", unique = true, length = 20)
    private String phone;
    
    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 500)
    private String avatar;
    
    /**
     * 性别（M-男，F-女，U-未知）
     */
    @Pattern(regexp = "^[MFU]$", message = "性别只能是M、F或U")
    @Column(name = "gender", length = 1)
    private String gender;
    
    /**
     * 生日
     */
    @Column(name = "birthday")
    private LocalDateTime birthday;
    
    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private Long deptId;
    
    /**
     * 职位
     */
    @Size(max = 50, message = "职位长度不能超过50个字符")
    @Column(name = "position", length = 50)
    private String position;
    
    /**
     * 用户状态（1-正常，0-禁用）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    /**
     * 是否为超级管理员
     */
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;
    
    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;
    
    /**
     * 登录次数
     */
    @Column(name = "login_count")
    private Integer loginCount = 0;
    
    /**
     * 密码最后修改时间
     */
    @Column(name = "password_update_time")
    private LocalDateTime passwordUpdateTime;
    
    /**
     * 账号过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
    
    /**
     * 是否锁定
     */
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;
    
    /**
     * 锁定时间
     */
    @Column(name = "lock_time")
    private LocalDateTime lockTime;
    
    /**
     * 个人简介
     */
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    @Column(name = "bio", length = 500)
    private String bio;
    
    /**
     * 扩展信息（JSON格式）
     */
    @Column(name = "extra_info", columnDefinition = "TEXT")
    private String extraInfo;
    
    /**
     * 用户关联的角色
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sys_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    /**
     * 用户直接权限
     */
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private Set<UserPermission> userPermissions;
    
    /**
     * 用户OAuth绑定
     */
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private Set<UserOAuth> oauthBindings;
    
    /**
     * 锁定状态（0-未锁定，1-已锁定）
     */
    @Column(name = "lock_status")
    private Integer lockStatus = 0;
    
    /**
     * 判断用户是否可用
     */
    public boolean isEnabled() {
        return status == 1 && !isLocked && 
               (expireTime == null || expireTime.isAfter(LocalDateTime.now()));
    }
    
    /**
     * 判断账号是否未过期
     */
    public boolean isAccountNonExpired() {
        return expireTime == null || expireTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * 判断账号是否未锁定
     */
    public boolean isAccountNonLocked() {
        return !isLocked;
    }
    
    /**
     * 判断密码是否未过期
     */
    public boolean isCredentialsNonExpired() {
        // 密码90天过期
        if (passwordUpdateTime == null) {
            return false;
        }
        return passwordUpdateTime.plusDays(90).isAfter(LocalDateTime.now());
    }
    
    /**
     * 更新登录信息
     */
    public void updateLoginInfo(String loginIp) {
        this.lastLoginTime = LocalDateTime.now();
        this.lastLoginIp = loginIp;
        this.loginCount = (this.loginCount == null ? 0 : this.loginCount) + 1;
    }
    
    /**
     * 锁定账号
     */
    public void lock() {
        this.isLocked = true;
        this.lockTime = LocalDateTime.now();
    }
    
    /**
     * 解锁账号
     */
    public void unlock() {
        this.isLocked = false;
        this.lockTime = null;
    }
    
    /**
     * 更新密码
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.passwordUpdateTime = LocalDateTime.now();
    }
    
    // Getter methods (manually added due to Lombok issues)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRealName() { return realName; }
    public String getNickname() { return nickname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAvatar() { return avatar; }
    public String getGender() { return gender; }
    public LocalDateTime getBirthday() { return birthday; }
    public Long getDeptId() { return deptId; }
    public String getPosition() { return position; }
    public Integer getStatus() { return status; }
    public Boolean getIsAdmin() { return isAdmin; }
    public LocalDateTime getLastLoginTime() { return lastLoginTime; }
    public String getLastLoginIp() { return lastLoginIp; }
    public Integer getLoginCount() { return loginCount; }
    public LocalDateTime getPasswordUpdateTime() { return passwordUpdateTime; }
    public LocalDateTime getExpireTime() { return expireTime; }
    public Boolean getIsLocked() { return isLocked; }
    public LocalDateTime getLockTime() { return lockTime; }
    public String getBio() { return bio; }
    public String getExtraInfo() { return extraInfo; }
    
    // Setter methods (manually added due to Lombok issues)
    public void setId(Long id) { super.setId(id); }
    public void setTenantId(Long tenantId) { super.setTenantId(tenantId); }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRealName(String realName) { this.realName = realName; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBirthday(LocalDateTime birthday) { this.birthday = birthday; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public void setPosition(String position) { this.position = position; }
    public void setStatus(Integer status) { this.status = status; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }
    public void setLastLoginTime(LocalDateTime lastLoginTime) { this.lastLoginTime = lastLoginTime; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }
    public void setLoginCount(Integer loginCount) { this.loginCount = loginCount; }
    public void setPasswordUpdateTime(LocalDateTime passwordUpdateTime) { this.passwordUpdateTime = passwordUpdateTime; }
    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
    public void setLockTime(LocalDateTime lockTime) { this.lockTime = lockTime; }
    public void setBio(String bio) { this.bio = bio; }
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }
    public void setDeleted(Boolean deleted) { super.setDeleted(deleted); }
    public void setDeleted(Integer deleted) { super.setDeleted(deleted != null && deleted == 1); }
    public Boolean getDeleted() { return super.getDeleted(); }
    public void setLockStatus(Integer lockStatus) { this.lockStatus = lockStatus; }
    public Integer getLockStatus() { return lockStatus; }
    public void setUpdateBy(String updateBy) { super.setUpdateBy(updateBy); }
    public void setUpdateBy(Long updateBy) { super.setUpdateBy(updateBy != null ? updateBy.toString() : null); }
    public void setUpdateTime(LocalDateTime updateTime) { super.setUpdateTime(updateTime); }
    public void setCreateBy(String createBy) { super.setCreateBy(createBy); }
    public void setCreateBy(Long createBy) { super.setCreateBy(createBy != null ? createBy.toString() : null); }
    public void setCreateTime(LocalDateTime createTime) { super.setCreateTime(createTime); }
    
    // Builder pattern (manually added due to Lombok issues)
    public static UserBuilder builder() {
        return new UserBuilder();
    }
    
    public static class UserBuilder {
        private Long id;
        private Long tenantId;
        private String username;
        private String password;
        private String realName;
        private String nickname;
        private String email;
        private String phone;
        private String avatar;
        private String gender;
        private LocalDateTime birthday;
        private Long deptId;
        private String position;
        private Integer status;
        private Boolean isAdmin;
        private LocalDateTime expireTime;
        private Integer lockStatus;
        private LocalDateTime passwordUpdateTime;
        private String remarks;
        private Long createBy;
        
        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder tenantId(Long tenantId) { this.tenantId = tenantId; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder realName(String realName) { this.realName = realName; return this; }
        public UserBuilder nickname(String nickname) { this.nickname = nickname; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder phone(String phone) { this.phone = phone; return this; }
        public UserBuilder avatar(String avatar) { this.avatar = avatar; return this; }
        public UserBuilder gender(String gender) { this.gender = gender; return this; }
        public UserBuilder birthday(LocalDateTime birthday) { this.birthday = birthday; return this; }
        public UserBuilder deptId(Long deptId) { this.deptId = deptId; return this; }
        public UserBuilder position(String position) { this.position = position; return this; }
        public UserBuilder status(Integer status) { this.status = status; return this; }
        public UserBuilder isAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; return this; }
        public UserBuilder expireTime(LocalDateTime expireTime) { this.expireTime = expireTime; return this; }
        public UserBuilder lockStatus(Integer lockStatus) { this.lockStatus = lockStatus; return this; }
        public UserBuilder passwordUpdateTime(LocalDateTime passwordUpdateTime) { this.passwordUpdateTime = passwordUpdateTime; return this; }
        public UserBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public UserBuilder createBy(Long createBy) { this.createBy = createBy; return this; }
        
        public User build() {
            User user = new User();
            if (this.id != null) {
                user.setId(this.id);
            }
            if (this.tenantId != null) {
                user.setTenantId(this.tenantId);
            }
            user.setUsername(this.username);
            user.setPassword(this.password);
            user.setRealName(this.realName);
            user.setNickname(this.nickname);
            user.setEmail(this.email);
            user.setPhone(this.phone);
            user.setAvatar(this.avatar);
            user.setGender(this.gender);
            user.setBirthday(this.birthday);
            user.setDeptId(this.deptId);
            user.setPosition(this.position);
            user.setStatus(this.status != null ? this.status : 1);
            user.setIsAdmin(this.isAdmin != null ? this.isAdmin : false);
            user.setExpireTime(this.expireTime);
            user.setIsLocked(this.lockStatus != null ? (this.lockStatus == 1) : false);
            user.setPasswordUpdateTime(this.passwordUpdateTime);
            user.setExtraInfo(this.remarks);
            user.setCreateBy(this.createBy); // Long version that converts to String
            user.setCreateTime(LocalDateTime.now());
            return user;
        }
    }
}