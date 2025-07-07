package com.biobt.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建用户请求DTO
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@Schema(description = "创建用户请求")
public class CreateUserRequest {

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50位之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @Schema(description = "密码")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100位")
    private String email;

    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "真实姓名")
    @Size(max = 50, message = "真实姓名长度不能超过50位")
    private String realName;

    @Schema(description = "昵称")
    @Size(max = 50, message = "昵称长度不能超过50位")
    private String nickname;

    @Schema(description = "头像URL")
    @Size(max = 500, message = "头像URL长度不能超过500位")
    private String avatar;

    @Schema(description = "性别：0-未知，1-男，2-女")
    @Min(value = 0, message = "性别值必须在0-2之间")
    @Max(value = 2, message = "性别值必须在0-2之间")
    private Integer gender;

    @Schema(description = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "状态：0-禁用，1-启用")
    @Min(value = 0, message = "状态值必须在0-1之间")
    @Max(value = 1, message = "状态值必须在0-1之间")
    private Integer status;

    @Schema(description = "是否锁定：0-未锁定，1-已锁定")
    @Min(value = 0, message = "锁定状态值必须在0-1之间")
    @Max(value = 1, message = "锁定状态值必须在0-1之间")
    private Integer isLocked;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500位")
    private String remarks;

    // Getters and Setters
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 检查是否有邮箱或手机号
     */
    public boolean hasEmailOrPhone() {
        return (email != null && !email.trim().isEmpty()) || 
               (phone != null && !phone.trim().isEmpty());
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "tenantId=" + tenantId +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", isLocked=" + isLocked +
                ", roleIds=" + roleIds +
                '}';
    }
}