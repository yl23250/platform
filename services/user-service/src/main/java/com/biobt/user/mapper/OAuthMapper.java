package com.biobt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biobt.user.domain.dto.OAuthClientRequest;
import com.biobt.user.domain.entity.OAuthClient;
import com.biobt.user.domain.entity.UserOAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * OAuth数据访问层
 *
 * @author BioBt Team
 * @since 1.0.0
 */
@Mapper
public interface OAuthMapper extends BaseMapper<OAuthClient> {

    /**
     * 根据客户端ID查询OAuth客户端
     *
     * @param clientId 客户端ID
     * @return OAuth客户端
     */
    OAuthClient selectByClientId(@Param("clientId") String clientId);

    /**
     * 分页查询OAuth客户端
     *
     * @param page    分页参数
     * @param request 查询条件
     * @return OAuth客户端分页列表
     */
    IPage<OAuthClient> selectOAuthClientPage(Page<OAuthClient> page, @Param("request") OAuthClientRequest request);

    /**
     * 查询启用的OAuth客户端
     *
     * @return OAuth客户端列表
     */
    List<OAuthClient> selectEnabledClients();

    /**
     * 根据提供商类型查询OAuth客户端
     *
     * @param providerType 提供商类型
     * @return OAuth客户端列表
     */
    List<OAuthClient> selectByProviderType(@Param("providerType") String providerType);

    /**
     * 批量启用OAuth客户端
     *
     * @param ids      客户端ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchEnable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量禁用OAuth客户端
     *
     * @param ids      客户端ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDisable(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 批量删除OAuth客户端
     *
     * @param ids      客户端ID列表
     * @param updateBy 更新者
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 检查客户端ID是否存在
     *
     * @param clientId  客户端ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByClientId(@Param("clientId") String clientId, @Param("excludeId") Long excludeId);

    /**
     * 测试OAuth客户端配置
     *
     * @param clientId 客户端ID
     * @return 测试结果
     */
    Map<String, Object> testOAuthClientConfig(@Param("clientId") String clientId);

    /**
     * 统计OAuth客户端数量
     *
     * @param providerType 提供商类型
     * @param status       状态
     * @return 客户端数量
     */
    Long countOAuthClients(@Param("providerType") String providerType, @Param("status") Integer status);

    /**
     * 统计OAuth绑定用户数量
     *
     * @param clientId 客户端ID
     * @return 绑定用户数量
     */
    Long countBindUsers(@Param("clientId") String clientId);

    // UserOAuth相关方法

    /**
     * 根据第三方用户ID查询用户OAuth绑定
     *
     * @param providerType   提供商类型
     * @param thirdPartyId   第三方用户ID
     * @param thirdPartyType 第三方用户类型
     * @return 用户OAuth绑定
     */
    UserOAuth selectUserOAuthByThirdPartyId(@Param("providerType") String providerType,
                                           @Param("thirdPartyId") String thirdPartyId,
                                           @Param("thirdPartyType") String thirdPartyType);

    /**
     * 根据用户ID查询OAuth绑定列表
     *
     * @param userId 用户ID
     * @return OAuth绑定列表
     */
    List<UserOAuth> selectUserOAuthBindings(@Param("userId") Long userId);

    /**
     * 根据用户ID和提供商类型查询OAuth绑定
     *
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @return OAuth绑定
     */
    UserOAuth selectUserOAuthByProvider(@Param("userId") Long userId, @Param("providerType") String providerType);

    /**
     * 创建用户OAuth绑定
     *
     * @param userOAuth 用户OAuth绑定
     * @return 影响行数
     */
    int insertUserOAuth(@Param("userOAuth") UserOAuth userOAuth);

    /**
     * 更新用户OAuth绑定
     *
     * @param userOAuth 用户OAuth绑定
     * @return 影响行数
     */
    int updateUserOAuth(@Param("userOAuth") UserOAuth userOAuth);

    /**
     * 删除用户OAuth绑定
     *
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @return 影响行数
     */
    int deleteUserOAuth(@Param("userId") Long userId, @Param("providerType") String providerType);

    /**
     * 更新OAuth令牌
     *
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param expiresAt    过期时间
     * @return 影响行数
     */
    int updateOAuthToken(@Param("userId") Long userId,
                        @Param("providerType") String providerType,
                        @Param("accessToken") String accessToken,
                        @Param("refreshToken") String refreshToken,
                        @Param("expiresAt") LocalDateTime expiresAt);

    /**
     * 刷新OAuth令牌
     *
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @param refreshToken 刷新令牌
     * @return 新的令牌信息
     */
    Map<String, Object> refreshOAuthToken(@Param("userId") Long userId,
                                         @Param("providerType") String providerType,
                                         @Param("refreshToken") String refreshToken);

    /**
     * 检查OAuth绑定是否存在
     *
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @return 是否存在
     */
    boolean existsUserOAuthBinding(@Param("userId") Long userId, @Param("providerType") String providerType);

    /**
     * 检查第三方用户是否已绑定
     *
     * @param providerType   提供商类型
     * @param thirdPartyId   第三方用户ID
     * @param thirdPartyType 第三方用户类型
     * @param excludeUserId  排除的用户ID
     * @return 是否已绑定
     */
    boolean existsThirdPartyBinding(@Param("providerType") String providerType,
                                   @Param("thirdPartyId") String thirdPartyId,
                                   @Param("thirdPartyType") String thirdPartyType,
                                   @Param("excludeUserId") Long excludeUserId);

    /**
     * 获取OAuth统计信息
     *
     * @param providerType 提供商类型
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 统计信息
     */
    Map<String, Object> getOAuthStatistics(@Param("providerType") String providerType,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    /**
     * 获取OAuth登录日志
     *
     * @param page         分页参数
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return OAuth登录日志分页列表
     */
    IPage<Map<String, Object>> selectOAuthLoginLogs(Page<Map<String, Object>> page,
                                                   @Param("userId") Long userId,
                                                   @Param("providerType") String providerType,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 记录OAuth登录日志
     *
     * @param userId       用户ID
     * @param providerType 提供商类型
     * @param loginIp      登录IP
     * @param userAgent    用户代理
     * @param loginResult  登录结果
     * @param errorMessage 错误信息
     * @return 影响行数
     */
    int insertOAuthLoginLog(@Param("userId") Long userId,
                           @Param("providerType") String providerType,
                           @Param("loginIp") String loginIp,
                           @Param("userAgent") String userAgent,
                           @Param("loginResult") String loginResult,
                           @Param("errorMessage") String errorMessage);

    /**
     * 获取OAuth全局配置
     *
     * @return 全局配置
     */
    Map<String, Object> getOAuthGlobalConfig();

    /**
     * 更新OAuth全局配置
     *
     * @param config   配置信息
     * @param updateBy 更新者
     * @return 影响行数
     */
    int updateOAuthGlobalConfig(@Param("config") Map<String, Object> config, @Param("updateBy") Long updateBy);
}