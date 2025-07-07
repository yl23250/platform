package com.biobt.user.annotation;

import java.lang.annotation.*;

/**
 * 权限检查注解
 * 用于方法级别的权限控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {
    
    /**
     * 权限编码
     */
    String[] value() default {};
    
    /**
     * 权限编码（别名）
     */
    String[] permissions() default {};
    
    /**
     * 逻辑关系（AND/OR）
     */
    Logical logical() default Logical.AND;
    
    /**
     * 是否检查数据权限
     */
    boolean checkDataPermission() default false;
    
    /**
     * 数据权限资源标识
     */
    String dataResource() default "";
    
    /**
     * 数据权限操作类型
     */
    DataOperation dataOperation() default DataOperation.SELECT;
    
    /**
     * 错误消息
     */
    String message() default "权限不足";
    
    /**
     * 逻辑关系枚举
     */
    enum Logical {
        /**
         * 且（需要所有权限）
         */
        AND,
        /**
         * 或（需要任一权限）
         */
        OR
    }
    
    /**
     * 数据操作类型枚举
     */
    enum DataOperation {
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
        
        DataOperation(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
}