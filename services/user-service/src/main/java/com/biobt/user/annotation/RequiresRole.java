package com.biobt.user.annotation;

import java.lang.annotation.*;

/**
 * 角色检查注解
 * 用于方法级别的角色控制
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {
    
    /**
     * 角色编码
     */
    String[] value() default {};
    
    /**
     * 角色编码（别名）
     */
    String[] roles() default {};
    
    /**
     * 逻辑关系（AND/OR）
     */
    Logical logical() default Logical.AND;
    
    /**
     * 错误消息
     */
    String message() default "角色权限不足";
    
    /**
     * 逻辑关系枚举
     */
    enum Logical {
        /**
         * 且（需要所有角色）
         */
        AND,
        /**
         * 或（需要任一角色）
         */
        OR
    }
}