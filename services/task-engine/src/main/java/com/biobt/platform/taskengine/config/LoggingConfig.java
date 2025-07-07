package com.biobt.platform.taskengine.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;

/**
 * 日志配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class LoggingConfig {
    
    @Value("${logging.file.path:/var/log/biobt/task-engine}")
    private String logPath;
    
    @Value("${logging.level.com.biobt:INFO}")
    private String logLevel;
    
    @Value("${logging.pattern.file:%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{traceId}] %logger{50} - %msg%n}")
    private String filePattern;
    
    /**
     * 请求日志过滤器
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        
        // 包含查询字符串
        filter.setIncludeQueryString(true);
        
        // 包含请求负载
        filter.setIncludePayload(true);
        
        // 最大负载长度
        filter.setMaxPayloadLength(1000);
        
        // 包含请求头
        filter.setIncludeHeaders(false);
        
        // 包含客户端信息
        filter.setIncludeClientInfo(true);
        
        // 日志前缀和后缀
        filter.setBeforeMessagePrefix("REQUEST: ");
        filter.setAfterMessagePrefix("RESPONSE: ");
        
        log.info("请求日志过滤器初始化完成");
        
        return filter;
    }
    
    /**
     * 初始化日志配置
     */
    @PostConstruct
    public void initLogging() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        
        // 配置任务执行日志
        configureTaskExecutionLogger(context);
        
        // 配置审计日志
        configureAuditLogger(context);
        
        // 配置性能日志
        configurePerformanceLogger(context);
        
        // 配置错误日志
        configureErrorLogger(context);
        
        log.info("日志配置初始化完成 - 日志路径: {}, 日志级别: {}", logPath, logLevel);
    }
    
    /**
     * 配置任务执行日志
     */
    private void configureTaskExecutionLogger(LoggerContext context) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(context);
        appender.setName("TASK_EXECUTION");
        appender.setFile(logPath + "/task-execution.log");
        
        // 滚动策略
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setParent(appender);
        policy.setFileNamePattern(logPath + "/task-execution.%d{yyyy-MM-dd}.%i.log.gz");
        policy.setMaxHistory(30);
        policy.start();
        
        // 编码器
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{taskId}] [%X{executionId}] %logger{50} - %msg%n");
        encoder.start();
        
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        
        Logger logger = context.getLogger("TASK_EXECUTION");
        logger.addAppender(appender);
        logger.setAdditive(false);
    }
    
    /**
     * 配置审计日志
     */
    private void configureAuditLogger(LoggerContext context) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(context);
        appender.setName("AUDIT");
        appender.setFile(logPath + "/audit.log");
        
        // 滚动策略
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setParent(appender);
        policy.setFileNamePattern(logPath + "/audit.%d{yyyy-MM-dd}.%i.log.gz");
        policy.setMaxHistory(90); // 审计日志保留更长时间
        policy.start();
        
        // 编码器
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%X{userId}] [%X{operation}] %logger{50} - %msg%n");
        encoder.start();
        
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        
        Logger logger = context.getLogger("AUDIT");
        logger.addAppender(appender);
        logger.setAdditive(false);
    }
    
    /**
     * 配置性能日志
     */
    private void configurePerformanceLogger(LoggerContext context) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(context);
        appender.setName("PERFORMANCE");
        appender.setFile(logPath + "/performance.log");
        
        // 滚动策略
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setParent(appender);
        policy.setFileNamePattern(logPath + "/performance.%d{yyyy-MM-dd}.%i.log.gz");
        policy.setMaxHistory(7);
        policy.start();
        
        // 编码器
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%X{method}] [%X{duration}ms] %logger{50} - %msg%n");
        encoder.start();
        
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        
        Logger logger = context.getLogger("PERFORMANCE");
        logger.addAppender(appender);
        logger.setAdditive(false);
    }
    
    /**
     * 配置错误日志
     */
    private void configureErrorLogger(LoggerContext context) {
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        appender.setContext(context);
        appender.setName("ERROR");
        appender.setFile(logPath + "/error.log");
        
        // 滚动策略
        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
        policy.setContext(context);
        policy.setParent(appender);
        policy.setFileNamePattern(logPath + "/error.%d{yyyy-MM-dd}.%i.log.gz");
        policy.setMaxHistory(30);
        policy.start();
        
        // 编码器
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{errorCode}] %logger{50} - %msg%n%ex");
        encoder.start();
        
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        
        Logger logger = context.getLogger("ERROR");
        logger.addAppender(appender);
        logger.setAdditive(false);
    }
}