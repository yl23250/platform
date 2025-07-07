package com.biobt.workflow.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.common.core.response.ApiResponse;
import com.biobt.workflow.entity.WorkflowDefinition;
import com.biobt.workflow.entity.WorkflowInstance;
import com.biobt.workflow.entity.WorkflowTask;
import com.biobt.workflow.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 工作流控制器
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/workflow")
@Tag(name = "工作流管理", description = "工作流引擎相关接口")
@Slf4j
public class WorkflowController extends BaseController {
    
    @Autowired
    private WorkflowService workflowService;
    
    /**
     * 部署流程定义
     */
    @PostMapping("/deploy")
    @Operation(summary = "部署流程定义", description = "上传并部署BPMN流程定义文件")
    public ApiResponse<WorkflowDefinition> deployProcess(
            @Parameter(description = "流程名称") @RequestParam String processName,
            @Parameter(description = "BPMN文件") @RequestParam MultipartFile file) {
        try {
            WorkflowDefinition definition = workflowService.deployProcess(
                    processName, file.getOriginalFilename(), file.getInputStream());
            return ApiResponse.success(definition);
        } catch (Exception e) {
            log.error("部署流程定义失败", e);
            return ApiResponse.error("部署流程定义失败: " + e.getMessage());
        }
    }
    
    /**
     * 启动流程实例
     */
    @PostMapping("/start")
    @Operation(summary = "启动流程实例", description = "根据流程定义Key启动新的流程实例")
    public ApiResponse<WorkflowInstance> startProcess(
            @Parameter(description = "流程定义Key") @RequestParam String processKey,
            @Parameter(description = "业务Key") @RequestParam(required = false) String businessKey,
            @Parameter(description = "流程标题") @RequestParam String title,
            @Parameter(description = "启动用户ID") @RequestParam String startUserId,
            @Parameter(description = "流程变量") @RequestBody(required = false) Map<String, Object> variables) {
        try {
            WorkflowInstance instance = workflowService.startProcess(
                    processKey, businessKey, title, startUserId, variables);
            return ApiResponse.success(instance);
        } catch (Exception e) {
            log.error("启动流程实例失败", e);
            return ApiResponse.error("启动流程实例失败: " + e.getMessage());
        }
    }
    
    /**
     * 完成任务
     */
    @PostMapping("/task/{taskId}/complete")
    @Operation(summary = "完成任务", description = "完成指定的工作流任务")
    public ApiResponse<Void> completeTask(
            @Parameter(description = "任务ID") @PathVariable String taskId,
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "任务变量") @RequestBody(required = false) Map<String, Object> variables,
            @Parameter(description = "完成意见") @RequestParam(required = false) String comment) {
        try {
            workflowService.completeTask(taskId, userId, variables, comment);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("完成任务失败", e);
            return ApiResponse.error("完成任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户待办任务
     */
    @GetMapping("/tasks/todo")
    @Operation(summary = "获取用户待办任务", description = "获取指定用户的待办任务列表")
    public ApiResponse<List<WorkflowTask>> getUserTasks(
            @Parameter(description = "用户ID") @RequestParam String userId) {
        try {
            List<WorkflowTask> tasks = workflowService.getUserTasks(userId);
            return ApiResponse.success(tasks);
        } catch (Exception e) {
            log.error("获取用户待办任务失败", e);
            return ApiResponse.error("获取用户待办任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取候选任务
     */
    @GetMapping("/tasks/candidate")
    @Operation(summary = "获取候选任务", description = "获取用户可认领的候选任务列表")
    public ApiResponse<List<WorkflowTask>> getCandidateTasks(
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "用户组ID列表") @RequestParam(required = false) List<String> groupIds) {
        try {
            List<WorkflowTask> tasks = workflowService.getCandidateTasks(userId, groupIds);
            return ApiResponse.success(tasks);
        } catch (Exception e) {
            log.error("获取候选任务失败", e);
            return ApiResponse.error("获取候选任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 认领任务
     */
    @PostMapping("/task/{taskId}/claim")
    @Operation(summary = "认领任务", description = "认领指定的候选任务")
    public ApiResponse<Void> claimTask(
            @Parameter(description = "任务ID") @PathVariable String taskId,
            @Parameter(description = "用户ID") @RequestParam String userId) {
        try {
            workflowService.claimTask(taskId, userId);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("认领任务失败", e);
            return ApiResponse.error("认领任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 委托任务
     */
    @PostMapping("/task/{taskId}/delegate")
    @Operation(summary = "委托任务", description = "将任务委托给其他用户")
    public ApiResponse<Void> delegateTask(
            @Parameter(description = "任务ID") @PathVariable String taskId,
            @Parameter(description = "委托人用户ID") @RequestParam String fromUserId,
            @Parameter(description = "被委托人用户ID") @RequestParam String toUserId) {
        try {
            workflowService.delegateTask(taskId, fromUserId, toUserId);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("委托任务失败", e);
            return ApiResponse.error("委托任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取流程实例
     */
    @GetMapping("/instance/{processInstanceId}")
    @Operation(summary = "获取流程实例", description = "根据ID获取流程实例详情")
    public ApiResponse<WorkflowInstance> getProcessInstance(
            @Parameter(description = "流程实例ID") @PathVariable String processInstanceId) {
        try {
            WorkflowInstance instance = workflowService.getProcessInstance(processInstanceId);
            if (instance != null) {
                return ApiResponse.success(instance);
            } else {
                return ApiResponse.notFound("流程实例不存在");
            }
        } catch (Exception e) {
            log.error("获取流程实例失败", e);
            return ApiResponse.error("获取流程实例失败: " + e.getMessage());
        }
    }
}