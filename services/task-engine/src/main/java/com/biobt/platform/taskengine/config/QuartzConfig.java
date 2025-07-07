package com.biobt.platform.taskengine.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Quartz调度器配置
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Configuration
public class QuartzConfig {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 调度器工厂Bean
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        
        // 设置数据源
        factory.setDataSource(dataSource);
        
        // 设置Quartz属性
        factory.setQuartzProperties(quartzProperties());
        
        // 设置调度器名称
        factory.setSchedulerName("TaskEngineScheduler");
        
        // 应用启动完成后启动调度器
        factory.setStartupDelay(10);
        
        // 应用关闭时等待任务完成
        factory.setWaitForJobsToCompleteOnShutdown(true);
        
        // 覆盖已存在的任务
        factory.setOverwriteExistingJobs(true);
        
        return factory;
    }
    
    /**
     * Quartz属性配置
     */
    private Properties quartzProperties() {
        Properties properties = new Properties();
        
        // 调度器属性
        properties.setProperty("org.quartz.scheduler.instanceName", "TaskEngineScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        
        // 线程池属性
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "20");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        
        // JobStore属性
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "10000");
        properties.setProperty("org.quartz.jobStore.useProperties", "false");
        
        return properties;
    }
    
    /**
     * 调度器
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
        return schedulerFactoryBean.getScheduler();
    }
}