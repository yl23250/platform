package com.biobt.user.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * 用于方法级别的数据权限控制（行权限、列权限）
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {
    
    /**
     * 资源标识（表名、视图名等）
     */
    String resource();
    
    /**
     * 操作类型
     */
    Operation operation() default Operation.SELECT;
    
    /**
     * 权限类型
     */
    Type type() default Type.ROW_AND_COLUMN;
    
    /**
     * 是否启用行权限
     */
    boolean enableRowPermission() default true;
    
    /**
     * 是否启用列权限
     */
    boolean enableColumnPermission() default true;
    
    /**
     * 自定义行权限条件（SpEL表达式）
     */
    String rowCondition() default "";
    
    /**
     * 允许的列（为空表示允许所有列）
     */
    String[] allowedColumns() default {};
    
    /**
     * 拒绝的列
     */
    String[] deniedColumns() default {};
    
    /**
     * 用户ID参数名（用于获取当前用户ID）
     */
    String userIdParam() default "userId";
    
    /**
     * 是否忽略超级管理员
     */
    boolean ignoreAdmin() default true;
    
    /**
     * 错误消息
     */
    String message() default "数据权限不足";
    
    /**
     * 操作类型枚举
     */
    enum Operation {
        /**
         * 查询
         */
        SELECT(1),
        /**
         * 新增
         */
        INSERT(2),
        /**
         * 修改
         */
        UPDATE(4),
        /**
         * 删除
         */
        DELETE(8);
        
        private final int value;
        
        Operation(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * 权限类型枚举
     */
    enum Type {
        /**
         * 仅行权限
         */
        ROW_ONLY(1),
        /**
         * 仅列权限
         */
        COLUMN_ONLY(2),
        /**
         * 行权限和列权限
         */
        ROW_AND_COLUMN(3);
        
        private final int value;
        
        Type(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}