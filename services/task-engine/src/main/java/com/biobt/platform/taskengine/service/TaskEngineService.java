package com.biobt.platform.taskengine.service;

import com.biobt.platform.taskengine.entity.TaskDefinition;
import com.biobt.platform.taskengine.entity.TaskExecution;
import com.biobt.platform.taskengine.entity.TaskSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 任务引擎服务
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskEngineService {
    
    private final Scheduler scheduler;
    
    /**
     * 创建任务定义
     */
    @Transactional
    public TaskDefinition createTaskDefinition(TaskDefinition taskDefinition) {
        log.info("创建任务定义: {}", taskDefinition.getTaskCode());
        
        // 设置默认值
        if (taskDefinition.getStatus() == null) {
            taskDefinition.setStatus(TaskDefinition.TaskStatus.DRAFT);
        }
        if (taskDefinition.getPriority() == null) {
            taskDefinition.setPriority(5);
        }
        if (taskDefinition.getMaxRetryCount() == null) {
            taskDefinition.setMaxRetryCount(3);
        }
        if (taskDefinition.getRetryInterval() == null) {
            taskDefinition.setRetryInterval(60);
        }
        if (taskDefinition.getAllowConcurrent() == null) {
            taskDefinition.setAllowConcurrent(false);
        }
        
        // TODO: 保存到数据库
        // return taskDefinitionRepository.save(taskDefinition);
        
        return taskDefinition;
    }
    
    /**
     * 更新任务定义
     */
    @Transactional
    public TaskDefinition updateTaskDefinition(TaskDefinition taskDefinition) {
        log.info("更新任务定义: {}", taskDefinition.getTaskCode());
        
        // TODO: 更新数据库
        // TaskDefinition existing = taskDefinitionRepository.findById(taskDefinition.getTaskId())
        //     .orElseThrow(() -> new RuntimeException("任务定义不存在"));
        // BeanUtils.copyProperties(taskDefinition, existing, "taskId", "createTime");
        // return taskDefinitionRepository.save(existing);
        
        return taskDefinition;
    }
    
    /**
     * 删除任务定义
     */
    @Transactional
    public void deleteTaskDefinition(Long taskId) {
        log.info("删除任务定义: {}", taskId);
        
        // 先停止调度
        stopTask(taskId);
        
        // TODO: 软删除任务定义
        // TaskDefinition taskDefinition = taskDefinitionRepository.findById(taskId)
        //     .orElseThrow(() -> new RuntimeException("任务定义不存在"));
        // taskDefinition.setStatus(TaskDefinition.TaskStatus.DELETED);
        // taskDefinitionRepository.save(taskDefinition);
    }
    
    /**
     * 启动任务
     */
    public void startTask(Long taskId) {
        try {
            log.info("启动任务: {}", taskId);
            
            // TODO: 从数据库获取任务定义
            // TaskDefinition taskDefinition = taskDefinitionRepository.findById(taskId)
            //     .orElseThrow(() -> new RuntimeException("任务定义不存在"));
            
            // 创建JobDetail
            JobDetail jobDetail = JobBuilder.newJob(TaskExecutorJob.class)
                    .withIdentity("task_" + taskId, "DEFAULT")
                    .usingJobData("taskId", taskId)
                    .build();
            
            // 创建Trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger_" + taskId, "DEFAULT")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 12 * * ?")) // 默认每天12点
                    .build();
            
            // 调度任务
            scheduler.scheduleJob(jobDetail, trigger);
            
            log.info("任务启动成功: {}", taskId);
            
        } catch (SchedulerException e) {
            log.error("启动任务失败: {}", taskId, e);
            throw new RuntimeException("启动任务失败", e);
        }
    }
    
    /**
     * 停止任务
     */
    public void stopTask(Long taskId) {
        try {
            log.info("停止任务: {}", taskId);
            
            JobKey jobKey = JobKey.jobKey("task_" + taskId, "DEFAULT");
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                log.info("任务停止成功: {}", taskId);
            } else {
                log.warn("任务不存在或已停止: {}", taskId);
            }
            
        } catch (SchedulerException e) {
            log.error("停止任务失败: {}", taskId, e);
            throw new RuntimeException("停止任务失败", e);
        }
    }
    
    /**
     * 暂停任务
     */
    public void pauseTask(Long taskId) {
        try {
            log.info("暂停任务: {}", taskId);
            
            JobKey jobKey = JobKey.jobKey("task_" + taskId, "DEFAULT");
            scheduler.pauseJob(jobKey);
            
            log.info("任务暂停成功: {}", taskId);
            
        } catch (SchedulerException e) {
            log.error("暂停任务失败: {}", taskId, e);
            throw new RuntimeException("暂停任务失败", e);
        }
    }
    
    /**
     * 恢复任务
     */
    public void resumeTask(Long taskId) {
        try {
            log.info("恢复任务: {}", taskId);
            
            JobKey jobKey = JobKey.jobKey("task_" + taskId, "DEFAULT");
            scheduler.resumeJob(jobKey);
            
            log.info("任务恢复成功: {}", taskId);
            
        } catch (SchedulerException e) {
            log.error("恢复任务失败: {}", taskId, e);
            throw new RuntimeException("恢复任务失败", e);
        }
    }
    
    /**
     * 立即执行任务
     */
    @Async
    public CompletableFuture<Void> executeTaskImmediately(Long taskId, Map<String, Object> params) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("立即执行任务: {}", taskId);
                
                // 创建执行记录
                TaskExecution execution = new TaskExecution();
                execution.setTaskId(taskId);
                execution.setExecutionStatus(TaskExecution.ExecutionStatus.RUNNING);
                execution.setStartTime(LocalDateTime.now());
                execution.setTriggerType(TaskExecution.TriggerType.MANUAL);
                
                // TODO: 保存执行记录
                // taskExecutionRepository.save(execution);
                
                // 执行任务逻辑
                executeTask(taskId, params, execution);
                
            } catch (Exception e) {
                log.error("立即执行任务失败: {}", taskId, e);
                throw new RuntimeException("立即执行任务失败", e);
            }
        });
    }
    
    /**
     * 执行任务核心逻辑
     */
    private void executeTask(Long taskId, Map<String, Object> params, TaskExecution execution) {
        try {
            log.info("开始执行任务: {}", taskId);
            
            // TODO: 根据任务定义执行具体逻辑
            // TaskDefinition taskDefinition = taskDefinitionRepository.findById(taskId)
            //     .orElseThrow(() -> new RuntimeException("任务定义不存在"));
            
            // 模拟任务执行
            Thread.sleep(1000);
            
            // 更新执行记录
            execution.setExecutionStatus(TaskExecution.ExecutionStatus.SUCCESS);
            execution.setEndTime(LocalDateTime.now());
            execution.setDuration(System.currentTimeMillis() - execution.getStartTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());
            
            // TODO: 更新执行记录
            // taskExecutionRepository.save(execution);
            
            log.info("任务执行成功: {}", taskId);
            
        } catch (Exception e) {
            log.error("任务执行失败: {}", taskId, e);
            
            // 更新执行记录
            execution.setExecutionStatus(TaskExecution.ExecutionStatus.FAILED);
            execution.setEndTime(LocalDateTime.now());
            execution.setErrorMessage(e.getMessage());
            execution.setErrorStack(getStackTrace(e));
            
            // TODO: 更新执行记录
            // taskExecutionRepository.save(execution);
            
            throw new RuntimeException("任务执行失败", e);
        }
    }
    
    /**
     * 获取任务执行历史
     */
    public Page<TaskExecution> getTaskExecutionHistory(Long taskId, Pageable pageable) {
        log.info("获取任务执行历史: {}", taskId);
        
        // TODO: 从数据库查询
        // return taskExecutionRepository.findByTaskIdOrderByStartTimeDesc(taskId, pageable);
        
        return Page.empty();
    }
    
    /**
     * 获取任务统计信息
     */
    public Map<String, Object> getTaskStatistics(Long taskId) {
        log.info("获取任务统计信息: {}", taskId);
        
        // TODO: 统计任务执行情况
        // Long totalCount = taskExecutionRepository.countByTaskId(taskId);
        // Long successCount = taskExecutionRepository.countByTaskIdAndExecutionStatus(taskId, TaskExecution.ExecutionStatus.SUCCESS);
        // Long failureCount = taskExecutionRepository.countByTaskIdAndExecutionStatus(taskId, TaskExecution.ExecutionStatus.FAILED);
        
        return Map.of(
            "totalCount", 0L,
            "successCount", 0L,
            "failureCount", 0L,
            "successRate", 0.0
        );
    }
    
    /**
     * 获取异常堆栈信息
     */
    private String getStackTrace(Exception e) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * 任务执行Job
     */
    public static class TaskExecutorJob implements Job {
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Long taskId = dataMap.getLong("taskId");
            
            // TODO: 获取TaskEngineService实例并执行任务
            // TaskEngineService taskEngineService = SpringContextHolder.getBean(TaskEngineService.class);
            // taskEngineService.executeTaskImmediately(taskId, Map.of());
            
            System.out.println("执行任务: " + taskId);
        }
    }
}