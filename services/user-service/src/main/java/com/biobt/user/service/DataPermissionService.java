package com.biobt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biobt.user.domain.entity.DataPermission;
import com.biobt.common.core.domain.PageRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.biobt.user.domain.entity.Permission;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据权限服务接口
 */
public interface DataPermissionService extends IService<DataPermission> {
    
    /**
     * 创建数据权限
     */
    DataPermission createDataPermission(DataPermission dataPermission);
    
    /**
     * 更新数据权限
     */
    DataPermission updateDataPermission(DataPermission dataPermission);
    
    /**
     * 删除数据权限
     */
    void deleteDataPermission(Long dataPermissionId);
    
    /**
     * 根据ID获取数据权限
     */
    DataPermission getDataPermissionById(Long dataPermissionId);
    
    /**
     * 分页查询数据权限
     */
    IPage<DataPermission> getDataPermissions(String resourceType, String resourceId, String subjectType, Long subjectId, Integer status, Page<DataPermission> page);
    
    /**
     * 获取用户的行权限条件
     */
    String getUserRowPermissionCondition(Long userId, String resourceId, Integer operationType);
    
    /**
     * 获取角色的行权限条件
     */
    String getRoleRowPermissionCondition(Long roleId, String resourceId, Integer operationType);
    
    /**
     * 获取用户的列权限配置
     */
    Map<String, Object> getUserColumnPermission(Long userId, String resourceId, Integer operationType);
    
    /**
     * 获取角色的列权限配置
     */
    Map<String, Object> getRoleColumnPermission(Long roleId, String resourceId, Integer operationType);
    
    /**
     * 检查用户是否有指定资源的数据权限
     */
    boolean hasDataPermission(Long userId, String resourceId, Integer operationType);
    
    /**
     * 检查角色是否有指定资源的数据权限
     */
    boolean roleHasDataPermission(Long roleId, String resourceId, Integer operationType);
    
    /**
     * 获取用户可访问的列集合
     */
    Set<String> getUserAccessibleColumns(Long userId, String resourceId, Integer operationType);
    
    /**
     * 获取用户被拒绝的列集合
     */
    Set<String> getUserDeniedColumns(Long userId, String resourceId, Integer operationType);
    
    /**
     * 为用户分配数据权限
     */
    void assignDataPermissionToUser(Long userId, String resourceId, Integer permissionType, 
                                   Integer operationType, String rowCondition, String columnPermission);
    
    /**
     * 为角色分配数据权限
     */
    void assignDataPermissionToRole(Long roleId, String resourceId, Integer permissionType, 
                                   Integer operationType, String rowCondition, String columnPermission);
    
    /**
     * 移除用户数据权限
     */
    void removeUserDataPermission(Long userId, String resourceId, Integer operationType);
    
    /**
     * 移除角色数据权限
     */
    void removeRoleDataPermission(Long roleId, String resourceId, Integer operationType);
    
    /**
     * 获取用户的所有数据权限
     */
    List<DataPermission> getUserDataPermissions(Long userId);
    
    /**
     * 获取角色的所有数据权限
     */
    List<DataPermission> getRoleDataPermissions(Long roleId);
    
    /**
     * 获取资源的所有数据权限
     */
    List<DataPermission> getResourceDataPermissions(String resourceId);
    
    /**
     * 批量创建数据权限
     */
    void batchCreateDataPermissions(List<DataPermission> dataPermissions);
    
    /**
     * 批量删除数据权限
     */
    void batchDeleteDataPermissions(List<Long> dataPermissionIds);
    
    /**
     * 启用数据权限
     */
    void enableDataPermission(Long dataPermissionId);
    
    /**
     * 禁用数据权限
     */
    void disableDataPermission(Long dataPermissionId);
    
    /**
     * 批量启用数据权限
     */
    void batchEnableDataPermissions(List<Long> dataPermissionIds);
    
    /**
     * 批量禁用数据权限
     */
    void batchDisableDataPermissions(List<Long> dataPermissionIds);
    
    /**
     * 构建SQL查询条件（行权限）
     */
    String buildRowPermissionSql(Long userId, String resourceId, Integer operationType);
    
    /**
     * 过滤查询结果列（列权限）
     */
    Map<String, Object> filterResultColumns(Long userId, String resourceId, 
                                           Integer operationType, Map<String, Object> originalData);
    
    /**
     * 过滤查询结果列表（列权限）
     */
    List<Map<String, Object>> filterResultColumnsList(Long userId, String resourceId, 
                                                      Integer operationType, List<Map<String, Object>> originalDataList);
    
    /**
     * 验证数据权限配置
     */
    boolean validateDataPermissionConfig(String rowCondition, String columnPermission);
    
    /**
     * 获取数据权限统计信息
     */
    Map<String, Object> getDataPermissionStatistics();
    
    /**
     * 刷新数据权限缓存
     */
    void refreshDataPermissionCache(Long userId);
    
    /**
     * 清空所有数据权限缓存
     */
    void clearAllDataPermissionCache();
    
    /**
     * 检查数据权限冲突
     */
    List<DataPermission> checkDataPermissionConflicts(Long subjectId, Integer subjectType, String resourceId);
    
    /**
     * 解析行权限条件中的变量
     */
    String parseRowConditionVariables(String rowCondition, Long userId);
    
    /**
     * 获取用户数据权限范围
     */
    Map<String, Object> getUserDataScope(Long userId);
    
    /**
     * 检查用户是否可以访问指定数据行
     */
    boolean canAccessDataRow(Long userId, String resourceId, Map<String, Object> rowData);
    
    /**
     * 检查用户是否可以访问指定数据列
     */
    boolean canAccessDataColumn(Long userId, String resourceId, String columnName);
}