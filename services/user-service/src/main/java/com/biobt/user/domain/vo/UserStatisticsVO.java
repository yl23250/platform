package com.biobt.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用户统计视图对象
 *
 * @author biobt
 * @since 2024-01-01
 */
@Data
@Schema(description = "用户统计视图对象")
public class UserStatisticsVO {

    @Schema(description = "总用户数")
    private Long totalUsers;

    @Schema(description = "活跃用户数")
    private Long activeUsers;

    @Schema(description = "禁用用户数")
    private Long disabledUsers;

    @Schema(description = "锁定用户数")
    private Long lockedUsers;

    @Schema(description = "今日注册用户数")
    private Long todayRegistrations;

    @Schema(description = "本月注册用户数")
    private Long monthlyRegistrations;

    @Schema(description = "用户登录趋势数据")
    private List<Map<String, Object>> loginTrend;

    @Schema(description = "用户注册趋势数据")
    private List<Map<String, Object>> registrationTrend;

    @Schema(description = "活跃用户排行")
    private List<Map<String, Object>> activeUserRanking;

    @Schema(description = "按性别统计")
    private Map<String, Long> genderStatistics;

    @Schema(description = "按年龄段统计")
    private Map<String, Long> ageStatistics;

    @Schema(description = "按地区统计")
    private Map<String, Long> regionStatistics;
}