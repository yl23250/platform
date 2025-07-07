package com.biobt.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 创建用户请求
 */
@Data
@Schema(description = "创建用户请求")
public class CreateUserRequest {
    
    @Schema(description = "用户名", example = "john_doe")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;
    
    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    @Schema(description = "确认密码", example = "123456")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    
    @Schema(description = "真实姓名", example = "张三")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;
    
    @Schema(description = "昵称", example = "小张")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
    
    @Schema(description = "邮箱", example = "john@example.com")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "手机号", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Schema(description = "头像URL")
    private String avatar;
    
    @Schema(description = "性别", example = "M", allowableValues = {"M", "F", "U"})
    @Pattern(regexp = "^[MFU]$", message = "性别只能是M、F或U")
    private String gender;
    
    @Schema(description = "生日")
    private LocalDateTime birthday;
    
    @Schema(description = "部门ID", example = "1")
    private Long deptId;
    
    @Schema(description = "职位", example = "软件工程师")
    @Size(max = 50, message = "职位长度不能超过50个字符")
    private String position;
    
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"})
    @Min(value = 0, message = "用户状态只能是0或1")
    @Max(value = 1, message = "用户状态只能是0或1")
    private Integer status = 1;
    
    @Schema(description = "是否为管理员", example = "false")
    private Boolean isAdmin = false;
    
    @Schema(description = "账号过期时间")
    private LocalDateTime expireTime;
    
    @Schema(description = "个人简介")
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    private String bio;
    
    @Schema(description = "角色ID列表")
    private Long[] roleIds;
    
    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
    
    @Schema(description = "租户ID")
    private String tenantId;
    
    @Schema(description = "锁定状态")
    private Integer lockStatus;
    
    @Schema(description = "备注信息")
    private String remarks;
    
    /**
     * 验证密码是否一致
     */
    @AssertTrue(message = "两次输入的密码不一致")
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
    
    /**
     * 验证邮箱和手机号至少有一个
     */
    @AssertTrue(message = "邮箱和手机号至少需要填写一个")
    public boolean hasEmailOrPhone() {
        return (email != null && !email.trim().isEmpty()) || 
               (phone != null && !phone.trim().isEmpty());
    }
    
    // Getter methods (manually added due to Lombok issues)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
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
    public LocalDateTime getExpireTime() { return expireTime; }
    public String getBio() { return bio; }
    public Long[] getRoleIds() { return roleIds; }
    public String getRemark() { return remark; }
    public String getTenantId() { return tenantId; }
    public Integer getLockStatus() { return lockStatus; }
    public String getRemarks() { return remarks; }
}