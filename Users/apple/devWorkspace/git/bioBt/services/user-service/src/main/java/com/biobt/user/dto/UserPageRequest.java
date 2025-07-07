package com.biobt.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

/**
 * 用户分页查询请求
 *
 * @author BioBt Platform
 * @since 2024-01-01
 */
@Schema(description = "用户分页查询请求")
public class UserPageRequest {

    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer pageSize = 10;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "用户名（模糊查询）")
    private String username;

    @Schema(description = "邮箱（模糊查询）")
    private String email;

    @Schema(description = "手机号（模糊查询）")
    private String phone;

    @Schema(description = "真实姓名（模糊查询）")
    private String realName;

    @Schema(description = "昵称（模糊查询）")
    private String nickname;

    @Schema(description = "性别：0-未知，1-男，2-女")
    private Integer gender;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "是否锁定：0-未锁定，1-锁定")
    private Integer isLocked;

    @Schema(description = "创建开始时间")
    private LocalDateTime startTime;

    @Schema(description = "创建结束时间")
    private LocalDateTime endTime;

    @Schema(description = "排序字段")
    private String sortField = "createdTime";

    @Schema(description = "排序方向：asc-升序，desc-降序")
    private String sortOrder = "desc";

    // 构造函数
    public UserPageRequest() {}

    // Getter和Setter方法
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "UserPageRequest{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", tenantId=" + tenantId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", isLocked=" + isLocked +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}