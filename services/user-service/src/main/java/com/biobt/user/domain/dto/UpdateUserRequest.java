package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 更新用户请求
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Data
@Schema(description = "更新用户请求")
public class UpdateUserRequest {

    @Schema(description = "邮箱", example = "john@example.com")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "真实姓名", example = "张三")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    @Schema(description = "昵称", example = "小张")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatar;

    @Schema(description = "性别：0-未知，1-男，2-女", example = "1")
    @Min(value = 0, message = "性别值必须在0-2之间")
    @Max(value = 2, message = "性别值必须在0-2之间")
    private Integer gender;

    @Schema(description = "生日", example = "1990-01-01")
    @Past(message = "生日必须是过去的日期")
    private LocalDate birthday;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    @Min(value = 0, message = "状态值必须在0-1之间")
    @Max(value = 1, message = "状态值必须在0-1之间")
    private Integer status;

    @Schema(description = "锁定状态：0-正常，1-锁定", example = "0")
    @Min(value = 0, message = "锁定状态值必须在0-1之间")
    @Max(value = 1, message = "锁定状态值必须在0-1之间")
    private Integer lockStatus;

    @Schema(description = "角色ID列表", example = "[1, 2, 3]")
    private List<Long> roleIds;

    @Schema(description = "备注", example = "用户备注信息")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remarks;

    /**
     * 检查是否有邮箱或手机号
     *
     * @return 是否有邮箱或手机号
     */
    public boolean hasEmailOrPhone() {
        return (email != null && !email.trim().isEmpty()) || 
               (phone != null && !phone.trim().isEmpty());
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

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
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
}