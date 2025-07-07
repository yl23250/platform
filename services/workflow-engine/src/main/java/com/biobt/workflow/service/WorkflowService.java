package com.biobt.workflow.service;

import com.biobt.workflow.entity.WorkflowDefinition;
import com.biobt.workflow.entity.WorkflowInstance;
import com.biobt.workflow.entity.WorkflowTask;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作流服务类
 * 提供流程定义、实例和任务管理功能
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Service
@Slf4j
public class WorkflowService {
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private ManagementService managementService;
    
    /**
     * 部署流程定义
     */
    @Transactional
    public WorkflowDefinition deployProcess(String processName, String fileName, InputStream inputStream) {
        try {
            // 部署流程
            Deployment deployment = repositoryService.createDeployment()
                    .name(processName)
                    .addInputStream(fileName, inputStream)
                    .deploy();
            
            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .singleResult();
            
            // 创建工作流定义实体
            WorkflowDefinition workflowDefinition = new WorkflowDefinition();
            workflowDefinition.setProcessDefinitionId(processDefinition.getId());
            workflowDefinition.setProcessKey(processDefinition.getKey());
            workflowDefinition.setProcessName(processDefinition.getName());
            workflowDefinition.setVersion(processDefinition.getVersion());
            workflowDefinition.setCategory(processDefinition.getCategory());
            workflowDefinition.setDeploymentId(deployment.getId());
            workflowDefinition.setIsActive(true);
            workflowDefinition.setIsStartable(true);
            workflowDefinition.setStatus(WorkflowDefinition.ProcessStatus.PUBLISHED);
            
            log.info("流程部署成功: {} - {}", processDefinition.getKey(), processDefinition.getName());
            return workflowDefinition;
            
        } catch (Exception e) {
            log.error("流程部署失败: {}", processName, e);
            throw new RuntimeException("流程部署失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 启动流程实例
     */
    @Transactional
    public WorkflowInstance startProcess(String processKey, String businessKey, 
                                       String title, String startUserId, Map<String, Object> variables) {
        try {
            // 启动流程实例
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    processKey, businessKey, variables);
            
            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(
                    processInstance.getProcessDefinitionId());
            
            // 创建工作流实例实体
            WorkflowInstance workflowInstance = new WorkflowInstance();
            workflowInstance.setProcessInstanceId(processInstance.getId());
            workflowInstance.setProcessDefinitionId(processInstance.getProcessDefinitionId());
            workflowInstance.setProcessKey(processDefinition.getKey());
            workflowInstance.setProcessName(processDefinition.getName());
            workflowInstance.setBusinessKey(businessKey);
            workflowInstance.setTitle(title);
            workflowInstance.setStarterUserId(startUserId);
            workflowInstance.setStartTime(LocalDateTime.now());
            workflowInstance.setStatus(WorkflowInstance.ProcessInstanceStatus.RUNNING);
            
            log.info("流程实例启动成功: {} - {}", processInstance.getId(), title);
            return workflowInstance;
            
        } catch (Exception e) {
            log.error("流程实例启动失败: {} - {}", processKey, title, e);
            throw new RuntimeException("流程实例启动失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 完成任务
     */
    @Transactional
    public void completeTask(String taskId, String userId, Map<String, Object> variables, String comment) {
        try {
            // 设置任务分配人
            taskService.setAssignee(taskId, userId);
            
            // 添加任务评论
            if (comment != null && !comment.trim().isEmpty()) {
                taskService.addComment(taskId, null, comment);
            }
            
            // 完成任务
            taskService.complete(taskId, variables);
            
            log.info("任务完成成功: {} - 用户: {}", taskId, userId);
            
        } catch (Exception e) {
            log.error("任务完成失败: {} - 用户: {}", taskId, userId, e);
            throw new RuntimeException("任务完成失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户待办任务
     */
    public List<WorkflowTask> getUserTasks(String userId) {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                    .taskAssignee(userId)
                    .orderByTaskCreateTime()
                    .desc()
                    .list();
            
            return tasks.stream().map(this::convertToWorkflowTask).collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("获取用户待办任务失败: {}", userId, e);
            throw new RuntimeException("获取用户待办任务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取候选任务
     */
    public List<WorkflowTask> getCandidateTasks(String userId, List<String> groupIds) {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                    .taskCandidateOrAssigned(userId)
                    .orderByTaskCreateTime()
                    .desc()
                    .list();
            
            return tasks.stream().map(this::convertToWorkflowTask).collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("获取候选任务失败: {}", userId, e);
            throw new RuntimeException("获取候选任务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 认领任务
     */
    @Transactional
    public void claimTask(String taskId, String userId) {
        try {
            taskService.claim(taskId, userId);
            log.info("任务认领成功: {} - 用户: {}", taskId, userId);
            
        } catch (Exception e) {
            log.error("任务认领失败: {} - 用户: {}", taskId, userId, e);
            throw new RuntimeException("任务认领失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 委托任务
     */
    @Transactional
    public void delegateTask(String taskId, String fromUserId, String toUserId) {
        try {
            taskService.delegateTask(taskId, toUserId);
            log.info("任务委托成功: {} - 从: {} 到: {}", taskId, fromUserId, toUserId);
            
        } catch (Exception e) {
            log.error("任务委托失败: {} - 从: {} 到: {}", taskId, fromUserId, toUserId, e);
            throw new RuntimeException("任务委托失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取流程实例
     */
    public WorkflowInstance getProcessInstance(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            
            if (processInstance != null) {
                return convertToWorkflowInstance(processInstance);
            }
            return null;
            
        } catch (Exception e) {
            log.error("获取流程实例失败: {}", processInstanceId, e);
            throw new RuntimeException("获取流程实例失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 转换Task为WorkflowTask
     */
    private WorkflowTask convertToWorkflowTask(Task task) {
        WorkflowTask workflowTask = new WorkflowTask();
        workflowTask.setTaskId(task.getId());
        workflowTask.setProcessInstanceId(task.getProcessInstanceId());
        workflowTask.setProcessDefinitionId(task.getProcessDefinitionId());
        workflowTask.setTaskDefinitionKey(task.getTaskDefinitionKey());
        workflowTask.setTaskName(task.getName());
        workflowTask.setDescription(task.getDescription());
        workflowTask.setAssignee(task.getAssignee());
        workflowTask.setOwner(task.getOwner());
        workflowTask.setPriority(task.getPriority());
        workflowTask.setFormKey(task.getFormKey());
        workflowTask.setExecutionId(task.getExecutionId());
        workflowTask.setCategory(task.getCategory());
        workflowTask.setIsSuspended(task.isSuspended());
        
        if (task.getCreateTime() != null) {
            workflowTask.setTaskCreateTime(task.getCreateTime().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        
        if (task.getDueDate() != null) {
            workflowTask.setDueDate(task.getDueDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        
        return workflowTask;
    }
    
    /**
     * 转换ProcessInstance为WorkflowInstance
     */
    private WorkflowInstance convertToWorkflowInstance(ProcessInstance processInstance) {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setProcessInstanceId(processInstance.getId());
        workflowInstance.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        workflowInstance.setBusinessKey(processInstance.getBusinessKey());
        workflowInstance.setIsSuspended(processInstance.isSuspended());
        
        return workflowInstance;
    }
}