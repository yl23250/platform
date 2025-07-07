package com.biobt.platform.workflowengine.config;

import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Flowable工作流引擎配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class FlowableConfig {
    
    @Value("${flowable.database-schema-update:true}")
    private String databaseSchemaUpdate;
    
    @Value("${flowable.async-executor-activate:true}")
    private boolean asyncExecutorActivate;
    
    /**
     * 流程引擎配置
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource) {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        
        // 数据源配置
        configuration.setDataSource(dataSource);
        
        // 数据库模式更新策略
        configuration.setDatabaseSchemaUpdate(databaseSchemaUpdate);
        
        // 异步执行器
        configuration.setAsyncExecutorActivate(asyncExecutorActivate);
        
        // 邮件服务器配置（可选）
        // configuration.setMailServerHost("smtp.example.com");
        // configuration.setMailServerPort(587);
        // configuration.setMailServerUsername("username");
        // configuration.setMailServerPassword("password");
        
        return configuration;
    }
    
    /**
     * 流程引擎
     */
    @Bean
    public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.buildProcessEngine();
    }
    
    /**
     * 运行时服务
     */
    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }
    
    /**
     * 任务服务
     */
    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }
    
    /**
     * 历史服务
     */
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }
    
    /**
     * 仓库服务
     */
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }
    
    /**
     * 管理服务
     */
    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }
    
    /**
     * 身份服务
     */
    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }
    
    /**
     * 表单服务
     */
    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }
}