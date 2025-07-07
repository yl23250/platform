package com.biobt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biobt.common.core.exception.BusinessException;
import com.biobt.common.core.utils.SecurityUtils;
import com.biobt.user.domain.entity.DataPermission;
import com.biobt.user.mapper.DataPermissionMapper;
import com.biobt.user.service.DataPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据权限服务实现类
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataPermissionServiceImpl extends ServiceImpl<DataPermissionMapper, DataPermission> implements DataPermissionService {

    private final DataPermissionMapper dataPermissionMapper;

    @Override
    @Transactional
    public DataPermission createDataPermission(DataPermission dataPermission) {
        // 验证数据权限配置
        Map<String, Object> validationResult = dataPermissionMapper.validateDataPermissionConfig(dataPermission);
        if (!(Boolean) validationResult.get("valid")) {
            throw new BusinessException("数据权限配置无效: " + validationResult.get("message"));
        }
        
        // 设置创建信息
        dataPermission.setCreateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setCreateTime(LocalDateTime.now());
        
        // 保存数据权限
        save(dataPermission);
        
        log.info("创建数据权限成功: {}-{}", dataPermission.getResourceType(), dataPermission.getResourceId());
        return dataPermission;
    }

    @Override
    @Transactional
    @CacheEvict(value = "dataPermission", key = "#dataPermission.id")
    public DataPermission updateDataPermission(DataPermission dataPermission) {
        // 检查数据权限是否存在
        DataPermission existingDataPermission = getById(dataPermission.getId());
        if (existingDataPermission == null) {
            throw new BusinessException("数据权限不存在");
        }
        
        // 验证数据权限配置
        Map<String, Object> validationResult = dataPermissionMapper.validateDataPermissionConfig(dataPermission);
        if (!(Boolean) validationResult.get("valid")) {
            throw new BusinessException("数据权限配置无效: " + validationResult.get("message"));
        }
        
        // 设置更新信息
        dataPermission.setUpdateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setUpdateTime(LocalDateTime.now());
        
        // 更新数据权限
        updateById(dataPermission);
        
        log.info("更新数据权限成功: {}-{}", dataPermission.getResourceType(), dataPermission.getResourceId());
        return dataPermission;
    }

    @Override
    @Transactional
    @CacheEvict(value = "dataPermission", key = "#dataPermissionId")
    public void deleteDataPermission(Long dataPermissionId) {
        // 检查数据权限是否存在
        DataPermission dataPermission = getById(dataPermissionId);
        if (dataPermission == null) {
            throw new BusinessException("数据权限不存在");
        }
        
        // 软删除数据权限
        dataPermission.setDeleted(true);
        dataPermission.setUpdateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setUpdateTime(LocalDateTime.now());
        updateById(dataPermission);
        
        log.info("删除数据权限成功: {}-{}", dataPermission.getResourceType(), dataPermission.getResourceId());
    }

    @Override
    @Cacheable(value = "dataPermission", key = "#dataPermissionId")
    public DataPermission getDataPermissionById(Long dataPermissionId) {
        return getById(dataPermissionId);
    }

    @Override
    public IPage<DataPermission> getDataPermissions(String resourceType, String resourceId, String subjectType, Long subjectId, Integer status, Page<DataPermission> page) {
        return dataPermissionMapper.selectDataPermissionPage(page, resourceType, resourceId, subjectType, subjectId, status);
    }

    @Override
    public String getUserRowPermissionCondition(Long userId, String resourceId, Integer operationType) {
        return dataPermissionMapper.getUserRowPermissionCondition(userId, resourceId, operationType);
    }

    @Override
    public String getRoleRowPermissionCondition(Long roleId, String resourceId, Integer operationType) {
        return dataPermissionMapper.getRoleRowPermissionCondition(roleId, resourceId, operationType);
    }

    @Override
    public Map<String, Object> getUserColumnPermission(Long userId, String resourceId, Integer operationType) {
        return dataPermissionMapper.getUserColumnPermissionConfig(userId, resourceId, operationType);
    }

    @Override
    public Map<String, Object> getRoleColumnPermission(Long roleId, String resourceId, Integer operationType) {
        return dataPermissionMapper.getRoleColumnPermissionConfig(roleId, resourceId, operationType);
    }

    @Override
    public boolean hasDataPermission(Long userId, String resourceId, Integer operationType) {
        return dataPermissionMapper.checkUserDataPermission(userId, resourceId, operationType);
    }

    @Override
    public boolean roleHasDataPermission(Long roleId, String resourceId, Integer operationType) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", roleId)
                   .eq("subject_type", 2)
                   .eq("resource_id", resourceId)
                   .eq("operation_type", operationType)
                   .eq("status", 1);
        return count(queryWrapper) > 0;
    }

    @Override
    public Set<String> getUserAccessibleColumns(Long userId, String resourceId, Integer operationType) {
        Map<String, Object> columnConfig = getUserColumnPermission(userId, resourceId, operationType);
        if (columnConfig == null || columnConfig.isEmpty()) {
            return new HashSet<>();
        }
        
        @SuppressWarnings("unchecked")
        List<String> allowedColumns = (List<String>) columnConfig.get("allowedColumns");
        return allowedColumns != null ? new HashSet<>(allowedColumns) : new HashSet<>();
    }

    @Override
    public Set<String> getUserDeniedColumns(Long userId, String resourceId, Integer operationType) {
        Map<String, Object> columnConfig = getUserColumnPermission(userId, resourceId, operationType);
        if (columnConfig == null || columnConfig.isEmpty()) {
            return new HashSet<>();
        }
        
        @SuppressWarnings("unchecked")
        List<String> deniedColumns = (List<String>) columnConfig.get("deniedColumns");
        return deniedColumns != null ? new HashSet<>(deniedColumns) : new HashSet<>();
    }

    @Override
    @Transactional
    public void assignDataPermissionToUser(Long userId, String resourceId, Integer permissionType, 
                                           Integer operationType, String rowCondition, String columnPermission) {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setSubjectId(userId);
        dataPermission.setSubjectType(1); // 用户类型
        dataPermission.setResourceId(resourceId);
        dataPermission.setPermissionType(permissionType);
        dataPermission.setOperationType(operationType);
        dataPermission.setRowCondition(rowCondition);
        dataPermission.setColumnPermission(columnPermission);
        dataPermission.setStatus(1);
        dataPermission.setCreateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setCreateTime(LocalDateTime.now());
        
        save(dataPermission);
        log.info("为用户 {} 分配数据权限成功", userId);
    }

    @Override
    @Transactional
    public void removeUserDataPermission(Long userId, String resourceId, Integer operationType) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", userId)
                   .eq("subject_type", 1)
                   .eq("resource_id", resourceId)
                   .eq("operation_type", operationType);
        
        boolean result = remove(queryWrapper);
        if (!result) {
            throw new BusinessException("移除用户数据权限失败");
        }
        log.info("移除用户 {} 的数据权限成功", userId);
    }

    @Override
    @Transactional
    public void assignDataPermissionToRole(Long roleId, String resourceId, Integer permissionType, 
                                          Integer operationType, String rowCondition, String columnPermission) {
        DataPermission dataPermission = new DataPermission();
        dataPermission.setSubjectId(roleId);
        dataPermission.setSubjectType(2); // 角色类型
        dataPermission.setResourceId(resourceId);
        dataPermission.setPermissionType(permissionType);
        dataPermission.setOperationType(operationType);
        dataPermission.setRowCondition(rowCondition);
        dataPermission.setColumnPermission(columnPermission);
        dataPermission.setStatus(1);
        dataPermission.setCreateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setCreateTime(LocalDateTime.now());
        
        save(dataPermission);
        log.info("为角色 {} 分配数据权限成功", roleId);
    }

    @Override
    @Transactional
    public void removeRoleDataPermission(Long roleId, String resourceId, Integer operationType) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", roleId)
                   .eq("subject_type", 2)
                   .eq("resource_id", resourceId)
                   .eq("operation_type", operationType);
        
        boolean result = remove(queryWrapper);
        if (!result) {
            throw new BusinessException("移除角色数据权限失败");
        }
        log.info("移除角色 {} 的数据权限成功", roleId);
    }

    @Override
    public List<DataPermission> getUserDataPermissions(Long userId) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", userId).eq("subject_type", 1);
        return list(queryWrapper);
    }

    @Override
    public List<DataPermission> getRoleDataPermissions(Long roleId) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", roleId).eq("subject_type", 2);
        return list(queryWrapper);
    }

    @Override
    public List<DataPermission> getResourceDataPermissions(String resourceId) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_id", resourceId);
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public void batchEnableDataPermissions(List<Long> dataPermissionIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = dataPermissionMapper.batchEnable(dataPermissionIds, currentUserId);
        log.info("批量启用数据权限成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDisableDataPermissions(List<Long> dataPermissionIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = dataPermissionMapper.batchDisable(dataPermissionIds, currentUserId);
        log.info("批量禁用数据权限成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchDeleteDataPermissions(List<Long> dataPermissionIds) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        int result = dataPermissionMapper.batchDelete(dataPermissionIds, currentUserId);
        log.info("批量删除数据权限成功，影响行数: {}", result);
    }

    @Override
    @Transactional
    public void batchCreateDataPermissions(List<DataPermission> dataPermissions) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        LocalDateTime now = LocalDateTime.now();
        
        dataPermissions.forEach(permission -> {
            permission.setCreateBy(currentUserId);
            permission.setCreateTime(now);
            permission.setStatus(1);
        });
        
        saveBatch(dataPermissions);
        log.info("批量创建数据权限成功，数量: {}", dataPermissions.size());
    }

    @Override
    @Transactional
    public void enableDataPermission(Long dataPermissionId) {
        DataPermission dataPermission = getById(dataPermissionId);
        if (dataPermission == null) {
            throw new BusinessException("数据权限不存在");
        }
        
        dataPermission.setStatus(1);
        dataPermission.setUpdateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setUpdateTime(LocalDateTime.now());
        updateById(dataPermission);
        
        log.info("启用数据权限成功: {}", dataPermissionId);
    }

    @Override
    @Transactional
    public void disableDataPermission(Long dataPermissionId) {
        DataPermission dataPermission = getById(dataPermissionId);
        if (dataPermission == null) {
            throw new BusinessException("数据权限不存在");
        }
        
        dataPermission.setStatus(0);
        dataPermission.setUpdateBy(SecurityUtils.getCurrentUserId());
        dataPermission.setUpdateTime(LocalDateTime.now());
        updateById(dataPermission);
        
        log.info("禁用数据权限成功: {}", dataPermissionId);
    }

    @Override
    public String buildRowPermissionSql(Long userId, String resourceId, Integer operationType) {
        return getUserRowPermissionCondition(userId, resourceId, operationType);
    }

    @Override
    public Map<String, Object> filterResultColumns(Long userId, String resourceId, 
                                                   Integer operationType, Map<String, Object> originalData) {
        if (originalData == null || originalData.isEmpty()) {
            return originalData;
        }
        
        Set<String> allowedColumns = getUserAccessibleColumns(userId, resourceId, operationType);
        Set<String> deniedColumns = getUserDeniedColumns(userId, resourceId, operationType);
        
        Map<String, Object> filteredData = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : originalData.entrySet()) {
            String columnName = entry.getKey();
            
            // 如果有允许列表，只保留允许的列
            if (!allowedColumns.isEmpty()) {
                if (allowedColumns.contains(columnName)) {
                    filteredData.put(columnName, entry.getValue());
                }
            }
            // 如果有拒绝列表，排除拒绝的列
            else if (!deniedColumns.isEmpty()) {
                if (!deniedColumns.contains(columnName)) {
                    filteredData.put(columnName, entry.getValue());
                }
            }
            // 没有权限配置，返回所有列
            else {
                filteredData.put(columnName, entry.getValue());
            }
        }
        
        return filteredData;
    }

    @Override
    public List<Map<String, Object>> filterResultColumnsList(Long userId, String resourceId, 
                                                             Integer operationType, List<Map<String, Object>> originalDataList) {
        if (originalDataList == null || originalDataList.isEmpty()) {
            return originalDataList;
        }
        
        return originalDataList.stream()
                .map(data -> filterResultColumns(userId, resourceId, operationType, data))
                .collect(Collectors.toList());
    }

    @Override
    public boolean validateDataPermissionConfig(String rowCondition, String columnPermission) {
        // 简单的验证逻辑
        if (rowCondition != null && rowCondition.trim().isEmpty()) {
            return false;
        }
        if (columnPermission != null && columnPermission.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> getDataPermissionStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总数据权限数
        long totalCount = count();
        statistics.put("totalCount", totalCount);
        
        // 启用的数据权限数
        QueryWrapper<DataPermission> enabledQuery = new QueryWrapper<>();
        enabledQuery.eq("status", 1);
        long enabledCount = count(enabledQuery);
        statistics.put("enabledCount", enabledCount);
        
        // 禁用的数据权限数
        statistics.put("disabledCount", totalCount - enabledCount);
        
        return statistics;
    }

    @Override
    @CacheEvict(value = "dataPermission", key = "#userId")
    public void refreshDataPermissionCache(Long userId) {
        log.info("刷新用户 {} 的数据权限缓存成功", userId);
    }

    @Override
    @CacheEvict(value = "dataPermission", allEntries = true)
    public void clearAllDataPermissionCache() {
        log.info("清空所有数据权限缓存成功");
    }

    @Override
    public List<DataPermission> checkDataPermissionConflicts(Long subjectId, Integer subjectType, String resourceId) {
        QueryWrapper<DataPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id", subjectId)
                   .eq("subject_type", subjectType)
                   .eq("resource_id", resourceId)
                   .eq("status", 1);
        
        List<DataPermission> permissions = list(queryWrapper);
        
        // 检查是否有冲突的权限配置
        // 这里可以根据具体业务逻辑来判断冲突
        return permissions;
    }

    @Override
    public String parseRowConditionVariables(String rowCondition, Long userId) {
        if (rowCondition == null || rowCondition.trim().isEmpty()) {
            return rowCondition;
        }
        
        // 替换变量，例如 ${userId} -> 实际用户ID
        String parsedCondition = rowCondition.replace("${userId}", String.valueOf(userId));
        
        return parsedCondition;
    }

    @Override
    public Map<String, Object> getUserDataScope(Long userId) {
        Map<String, Object> dataScope = new HashMap<>();
        
        // 获取用户的所有数据权限
        List<DataPermission> userPermissions = getUserDataPermissions(userId);
        
        // 按资源类型分组
        Map<String, List<DataPermission>> permissionsByResource = userPermissions.stream()
                .collect(Collectors.groupingBy(DataPermission::getResourceId));
        
        dataScope.put("permissions", permissionsByResource);
        dataScope.put("totalCount", userPermissions.size());
        
        return dataScope;
    }

    @Override
    public boolean canAccessDataRow(Long userId, String resourceId, Map<String, Object> rowData) {
        String rowCondition = getUserRowPermissionCondition(userId, resourceId, 1);
        
        if (rowCondition == null || rowCondition.trim().isEmpty()) {
            return true; // 没有行权限限制
        }
        
        // 这里需要根据具体的业务逻辑来判断行数据是否满足权限条件
        // 简化实现，实际应该解析SQL条件并应用到行数据上
        return true;
    }

    @Override
    public boolean canAccessDataColumn(Long userId, String resourceId, String columnName) {
        Set<String> allowedColumns = getUserAccessibleColumns(userId, resourceId, 1);
        Set<String> deniedColumns = getUserDeniedColumns(userId, resourceId, 1);
        
        // 如果有允许列表，检查列是否在允许列表中
        if (!allowedColumns.isEmpty()) {
            return allowedColumns.contains(columnName);
        }
        
        // 如果有拒绝列表，检查列是否不在拒绝列表中
        if (!deniedColumns.isEmpty()) {
            return !deniedColumns.contains(columnName);
        }
        
        // 没有权限配置，默认允许访问
        return true;
    }


}