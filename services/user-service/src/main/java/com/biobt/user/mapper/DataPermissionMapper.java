package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.entity.DataPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据权限数据访问层
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Mapper
public interface DataPermissionMapper extends BaseMapper<DataPermission> {

    /**
     * 分页查询数据权限
     *
     * @param page         分页参数
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @param subjectType  主体类型
     * @param subjectId    主体ID
     * @param status       状态
     * @return 数据权限分页列表
     */
    IPage<DataPermission> selectDataPermissionPage(Page<DataPermission> page,
                                                   @Param("resourceType") String resourceType,
                                                   @Param("resourceId") String resourceId,
                                                   @Param("subjectType") String subjectType,
                                                   @Param("subjectId") Long subjectId,
                                                   @Param("status") Integer status);

    /**
     * 获取用户行权限条件
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @return 行权限条件
     */
    String getUserRowPermissionCondition(@Param("userId") Long userId,
                                         @Param("resourceType") String resourceType,
                                         @Param("operation") String operation);

    /**
     * 获取角色行权限条件
     *
     * @param roleId       角色ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @return 行权限条件
     */
    String getRoleRowPermissionCondition(@Param("roleId") Long roleId,
                                         @Param("resourceType") String resourceType,
                                         @Param("operation") String operation);

    /**
     * 获取用户列权限配置
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @return 列权限配置
     */
    Map<String, Object> getUserColumnPermissionConfig(@Param("userId") Long userId,
                                                      @Param("resourceType") String resourceType,
                                                      @Param("operation") String operation);

    /**
     * 获取角色列权限配置
     *
     * @param roleId       角色ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @return 列权限配置
     */
    Map<String, Object> getRoleColumnPermissionConfig(@Param("roleId") Long roleId,
                                                      @Param("resourceType") String resourceType,
                                                      @Param("operation") String operation);

    /**
     * 检查用户数据权限
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @return 是否有权限
     */
    boolean checkUserDataPermission(@Param("userId") Long userId,
                                   @Param("resourceType") String resourceType,
                                   @Param("operation") String operation);

    /**
     * 检查角色数据权限
     *
     * @param roleId       角色ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @return 是否有权限
     */
    boolean checkRoleDataPermission(@Param("roleId") Long roleId,
                                   @Param("resourceType") String resourceType,
                                   @Param("operation") String operation);

    /**
     * 为用户分配数据权限
     *
     * @param userId           用户ID
     * @param dataPermissionId 数据权限ID
     * @param assignBy         分配者
     * @return 影响行数
     */
    int assignDataPermissionToUser(@Param("userId") Long userId,
                                  @Param("dataPermissionId") Long dataPermissionId,
                                  @Param("assignBy") Long assignBy);

    /**
     * 移除用户数据权限
     *
     * @param userId           用户ID
     * @param dataPermissionId 数据权限ID
     * @return 影响行数
     */
    int removeDataPermissionFromUser(@Param("userId") Long userId,
                                    @Param("dataPermissionId") Long dataPermissionId);

    /**
     * 为角色分配数据权限
     *
     * @param roleId           角色ID
     * @param dataPermissionId 数据权限ID
     * @param assignBy         分配者
     * @return 影响行数
     */
    int assignDataPermissionToRole(@Param("roleId") Long roleId,
                                  @Param("dataPermissionId") Long dataPermissionId,
                                  @Param("assignBy") Long assignBy);

    /**
     * 移除角色数据权限
     *
     * @param roleId           角色ID
     * @param dataPermissionId 数据权限ID
     * @return 影响行数
     */
    int removeDataPermissionFromRole(@Param("roleId") Long roleId,
                                    @Param("dataPermissionId") Long dataPermissionId);

    /**
     * 获取用户所有数据权限
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @return 数据权限列表
     */
    List<DataPermission> selectUserDataPermissions(@Param("userId") Long userId,
                                                   @Param("resourceType") String resourceType);

    /**
     * 获取角色所有数据权限
     *
     * @param roleId       角色ID
     * @param resourceType 资源类型
     * @return 数据权限列表
     */
    List<DataPermission> selectRoleDataPermissions(@Param("roleId") Long roleId,
                                                   @Param("resourceType") String resourceType);

    /**
     * 获取资源所有数据权限
     *
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @return 数据权限列表
     */
    List<DataPermission> selectResourceDataPermissions(@Param("resourceType") String resourceType,
                                                       @Param("resourceId") String resourceId);

    /**
     * 批量启用数据权限
     *
     * @param ids      数据权限ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchEnable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量禁用数据权限
     *
     * @param ids      数据权限ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDisable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量删除数据权限
     *
     * @param ids      数据权限ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 构建SQL条件
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param operation    操作类型
     * @param variables    变量映射
     * @return SQL条件
     */
    String buildSqlCondition(@Param("userId") Long userId,
                             @Param("resourceType") String resourceType,
                             @Param("operation") String operation,
                             @Param("variables") Map<String, Object> variables);

    /**
     * 验证数据权限配置
     *
     * @param dataPermission 数据权限
     * @return 验证结果
     */
    Map<String, Object> validateDataPermissionConfig(@Param("dataPermission") DataPermission dataPermission);

    /**
     * 获取数据权限统计
     *
     * @param resourceType 资源类型
     * @param subjectType  主体类型
     * @return 统计信息
     */
    Map<String, Object> getDataPermissionStatistics(@Param("resourceType") String resourceType,
                                                    @Param("subjectType") String subjectType);

    /**
     * 检查数据权限冲突
     *
     * @param dataPermission 数据权限
     * @return 冲突列表
     */
    List<DataPermission> checkDataPermissionConflicts(@Param("dataPermission") DataPermission dataPermission);

    /**
     * 解析权限变量
     *
     * @param condition 权限条件
     * @param variables 变量映射
     * @return 解析后的条件
     */
    String parsePermissionVariables(@Param("condition") String condition,
                                   @Param("variables") Map<String, Object> variables);

    /**
     * 获取用户数据范围
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @return 数据范围
     */
    Map<String, Object> getUserDataScope(@Param("userId") Long userId,
                                         @Param("resourceType") String resourceType);

    /**
     * 检查数据行访问权限
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param resourceId   资源ID
     * @param operation    操作类型
     * @return 是否有权限
     */
    boolean checkDataRowAccess(@Param("userId") Long userId,
                              @Param("resourceType") String resourceType,
                              @Param("resourceId") String resourceId,
                              @Param("operation") String operation);

    /**
     * 检查数据列访问权限
     *
     * @param userId       用户ID
     * @param resourceType 资源类型
     * @param columnName   列名
     * @param operation    操作类型
     * @return 是否有权限
     */
    boolean checkDataColumnAccess(@Param("userId") Long userId,
                                 @Param("resourceType") String resourceType,
                                 @Param("columnName") String columnName,
                                 @Param("operation") String operation);

    /**
     * 清理数据权限缓存
     *
     * @param dataPermissionId 数据权限ID
     */
    void clearDataPermissionCache(@Param("dataPermissionId") Long dataPermissionId);

    /**
     * 刷新数据权限缓存
     *
     * @param dataPermissionId 数据权限ID
     */
    void refreshDataPermissionCache(@Param("dataPermissionId") Long dataPermissionId);
}