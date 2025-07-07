package com.biobt.platform.taskengine.controller;

import com.biobt.common.core.controller.BaseController;
import com.biobt.common.core.result.Result;
import com.biobt.platform.taskengine.entity.TaskDefinition;
import com.biobt.platform.taskengine.entity.TaskExecution;
import com.biobt.platform.taskengine.service.TaskEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 任务引擎控制器
 * 
 * @author BioBt Platform
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/task-engine")
@RequiredArgsConstructor
@Tag(name = "任务引擎", description = "任务引擎管理API")
public class TaskEngineController {
    
    private final TaskEngineService taskEngineService;
    
    /**
     * 创建任务定义
     */
    @PostMapping("/tasks")
    @Operation(summary = "创建任务定义", description = "创建新的任务定义")
    public Result<TaskDefinition> createTask(@RequestBody TaskDefinition taskDefinition) {
        log.info("创建任务定义: {}", taskDefinition.getTaskCode());
        
        try {
            TaskDefinition result = taskEngineService.createTaskDefinition(taskDefinition);
            return Result.success(result);
        } catch (Exception e) {
            log.error("创建任务定义失败", e);
            return Result.error("创建任务定义失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新任务定义
     */
    @PutMapping("/tasks/{taskId}")
    @Operation(summary = "更新任务定义", description = "更新指定的任务定义")
    public Result<TaskDefinition> updateTask(
            @Parameter(description = "任务ID") @PathVariable Long taskId,
            @RequestBody TaskDefinition taskDefinition) {
        log.info("更新任务定义: {}", taskId);
        
        try {
            taskDefinition.setTaskId(taskId);
            TaskDefinition result = taskEngineService.updateTaskDefinition(taskDefinition);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新任务定义失败", e);
            return Result.error("更新任务定义失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除任务定义
     */
    @DeleteMapping("/tasks/{taskId}")
    @Operation(summary = "删除任务定义", description = "删除指定的任务定义")
    public Result<Void> deleteTask(@Parameter(description = "任务ID") @PathVariable Long taskId) {
        log.info("删除任务定义: {}", taskId);
        
        try {
            taskEngineService.deleteTaskDefinition(taskId);
            return Result.success();
        } catch (Exception e) {
            log.error("删除任务定义失败", e);
            return Result.error("删除任务定义失败: " + e.getMessage());
        }
    }
    
    /**
     * 启动任务
     */
    @PostMapping("/tasks/{taskId}/start")
    @Operation(summary = "启动任务", description = "启动指定的任务")
    public Result<Void> startTask(@Parameter(description = "任务ID") @PathVariable Long taskId) {
        log.info("启动任务: {}", taskId);
        
        try {
            taskEngineService.startTask(taskId);
            return Result.success();
        } catch (Exception e) {
            log.error("启动任务失败", e);
            return Result.error("启动任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 停止任务
     */
    @PostMapping("/tasks/{taskId}/stop")
    @Operation(summary = "停止任务", description = "停止指定的任务")
    public Result<Void> stopTask(@Parameter(description = "任务ID") @PathVariable Long taskId) {
        log.info("停止任务: {}", taskId);
        
        try {
            taskEngineService.stopTask(taskId);
            return Result.success();
        } catch (Exception e) {
            log.error("停止任务失败", e);
            return Result.error("停止任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 暂停任务
     */
    @PostMapping("/tasks/{taskId}/pause")
    @Operation(summary = "暂停任务", description = "暂停指定的任务")
    public Result<Void> pauseTask(@Parameter(description = "任务ID") @PathVariable Long taskId) {
        log.info("暂停任务: {}", taskId);
        
        try {
            taskEngineService.pauseTask(taskId);
            return Result.success();
        } catch (Exception e) {
            log.error("暂停任务失败", e);
            return Result.error("暂停任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 恢复任务
     */
    @PostMapping("/tasks/{taskId}/resume")
    @Operation(summary = "恢复任务", description = "恢复指定的任务")
    public Result<Void> resumeTask(@Parameter(description = "任务ID") @PathVariable Long taskId) {
        log.info("恢复任务: {}", taskId);
        
        try {
            taskEngineService.resumeTask(taskId);
            return Result.success();
        } catch (Exception e) {
            log.error("恢复任务失败", e);
            return Result.error("恢复任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 立即执行任务
     */
    @PostMapping("/tasks/{taskId}/execute")
    @Operation(summary = "立即执行任务", description = "立即执行指定的任务")
    public Result<Void> executeTask(
            @Parameter(description = "任务ID") @PathVariable Long taskId,
            @RequestBody(required = false) Map<String, Object> params) {
        log.info("立即执行任务: {}", taskId);
        
        try {
            CompletableFuture<Void> future = taskEngineService.executeTaskImmediately(taskId, params);
            return Result.success();
        } catch (Exception e) {
            log.error("立即执行任务失败", e);
            return Result.error("立即执行任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取任务执行历史
     */
    @GetMapping("/tasks/{taskId}/executions")
    @Operation(summary = "获取任务执行历史", description = "获取指定任务的执行历史记录")
    public Result<Page<TaskExecution>> getTaskExecutions(
            @Parameter(description = "任务ID") @PathVariable Long taskId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "20") int size) {
        log.info("获取任务执行历史: {}", taskId);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TaskExecution> result = taskEngineService.getTaskExecutionHistory(taskId, pageable);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取任务执行历史失败", e);
            return Result.error("获取任务执行历史失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取任务统计信息
     */
    @GetMapping("/tasks/{taskId}/statistics")
    @Operation(summary = "获取任务统计信息", description = "获取指定任务的统计信息")
    public Result<Map<String, Object>> getTaskStatistics(
            @Parameter(description = "任务ID") @PathVariable Long taskId) {
        log.info("获取任务统计信息: {}", taskId);
        
        try {
            Map<String, Object> result = taskEngineService.getTaskStatistics(taskId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取任务统计信息失败", e);
            return Result.error("获取任务统计信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查任务引擎服务健康状态")
    public Result<Map<String, Object>> health() {
        return Result.success(Map.of(
            "status", "UP",
            "service", "task-engine",
            "timestamp", System.currentTimeMillis()
        ));
    }
}