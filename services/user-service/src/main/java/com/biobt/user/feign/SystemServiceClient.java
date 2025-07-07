package com.biobt.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 系统服务 Feign 客户端
 *
 * @author biobt
 * @since 2024-01-01
 */
@FeignClient(name = "system-service", path = "/api/system")
public interface SystemServiceClient {

    /**
     * 获取租户信息
     *
     * @param tenantId 租户ID
     * @return 租户信息
     */
    @GetMapping("/tenant/{tenantId}")
    Map<String, Object> getTenantInfo(@PathVariable("tenantId") Long tenantId);

    /**
     * 获取角色列表
     *
     * @param roleIds 角色ID列表
     * @return 角色列表
     */
    @PostMapping("/roles/batch")
    List<Map<String, Object>> getRolesByIds(@RequestBody List<Long> roleIds);

    /**
     * 获取权限列表
     *
     * @param permissionIds 权限ID列表
     * @return 权限列表
     */
    @PostMapping("/permissions/batch")
    List<Map<String, Object>> getPermissionsByIds(@RequestBody List<Long> permissionIds);

    /**
     * 记录操作日志
     *
     * @param logData 日志数据
     */
    @PostMapping("/log/operation")
    void recordOperationLog(@RequestBody Map<String, Object> logData);

    /**
     * 发送系统通知
     *
     * @param notificationData 通知数据
     */
    @PostMapping("/notification/send")
    void sendNotification(@RequestBody Map<String, Object> notificationData);
}