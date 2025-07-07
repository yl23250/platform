package com.biobt.user.domain.dto;

import com.biobt.common.core.domain.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * 用户分页查询请求
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户分页查询请求")
public class UserPageRequest extends PageRequest {

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "用户名", example = "john")
    private String username;

    @Schema(description = "邮箱", example = "john@example.com")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "昵称", example = "小张")
    private String nickname;

    @Schema(description = "性别：0-未知，1-男，2-女", example = "1")
    @Min(value = 0, message = "性别值必须在0-2之间")
    @Max(value = 2, message = "性别值必须在0-2之间")
    private Integer gender;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    @Min(value = 0, message = "状态值必须在0-1之间")
    @Max(value = 1, message = "状态值必须在0-1之间")
    private Integer status;

    @Schema(description = "锁定状态：0-正常，1-锁定", example = "0")
    @Min(value = 0, message = "锁定状态值必须在0-1之间")
    @Max(value = 1, message = "锁定状态值必须在0-1之间")
    private Integer lockStatus;

    @Schema(description = "创建时间开始", example = "2024-01-01 00:00:00")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束", example = "2024-12-31 23:59:59")
    private LocalDateTime createTimeEnd;

    @Schema(description = "排序字段", example = "createTime")
    private String sortField;

    @Schema(description = "排序方向：asc-升序，desc-降序", example = "desc")
    private String sortDirection;

    // Getter and Setter methods
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getLockStatus() { return lockStatus; }
    public void setLockStatus(Integer lockStatus) { this.lockStatus = lockStatus; }

    public LocalDateTime getCreateTimeStart() { return createTimeStart; }
    public void setCreateTimeStart(LocalDateTime createTimeStart) { this.createTimeStart = createTimeStart; }

    public LocalDateTime getCreateTimeEnd() { return createTimeEnd; }
    public void setCreateTimeEnd(LocalDateTime createTimeEnd) { this.createTimeEnd = createTimeEnd; }

    public String getSortField() { return sortField; }
    public void setSortField(String sortField) { this.sortField = sortField; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}