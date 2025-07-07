-- BioBt Platform 数据库初始化脚本
-- 创建数据库、用户和基础表结构

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ====================================
-- 创建数据库
-- ====================================

-- 用户服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_user` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_user_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_user_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_user_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 租户服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_tenant` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_tenant_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_tenant_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_tenant_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 客户服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_customer` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_customer_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_customer_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_customer_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 项目服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_project` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_project_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_project_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_project_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 合同服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_contract` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_contract_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_contract_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_contract_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 订单服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_order` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_order_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_order_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_order_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 支付服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_payment` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_payment_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_payment_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_payment_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 报表服务数据库
CREATE DATABASE IF NOT EXISTS `biobt_report` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_report_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_report_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `biobt_report_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ====================================
-- 创建数据库用户
-- ====================================

-- 创建应用用户
CREATE USER IF NOT EXISTS 'biobt_app'@'%' IDENTIFIED BY 'biobt_app_2024';
CREATE USER IF NOT EXISTS 'biobt_readonly'@'%' IDENTIFIED BY 'biobt_readonly_2024';

-- 授权应用用户
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_user.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_user_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_user_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_user_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_tenant.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_tenant_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_tenant_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_tenant_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_customer.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_customer_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_customer_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_customer_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_project.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_project_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_project_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_project_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_contract.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_contract_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_contract_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_contract_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_order.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_order_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_order_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_order_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_payment.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_payment_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_payment_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_payment_prod.* TO 'biobt_app'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_report.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_report_dev.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_report_test.* TO 'biobt_app'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON biobt_report_prod.* TO 'biobt_app'@'%';

-- 授权只读用户
GRANT SELECT ON biobt_user.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_user_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_user_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_user_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_tenant.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_tenant_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_tenant_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_tenant_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_customer.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_customer_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_customer_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_customer_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_project.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_project_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_project_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_project_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_contract.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_contract_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_contract_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_contract_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_order.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_order_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_order_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_order_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_payment.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_payment_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_payment_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_payment_prod.* TO 'biobt_readonly'@'%';

GRANT SELECT ON biobt_report.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_report_dev.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_report_test.* TO 'biobt_readonly'@'%';
GRANT SELECT ON biobt_report_prod.* TO 'biobt_readonly'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- ====================================
-- 用户服务表结构
-- ====================================

USE `biobt_user`;

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `is_locked` TINYINT NOT NULL DEFAULT 0 COMMENT '是否锁定：0-未锁定，1-锁定',
    `lock_time` DATETIME DEFAULT NULL COMMENT '锁定时间',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `login_count` INT NOT NULL DEFAULT 0 COMMENT '登录次数',
    `password_update_time` DATETIME DEFAULT NULL COMMENT '密码更新时间',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`, `tenant_id`, `deleted`),
    UNIQUE KEY `uk_email` (`email`, `tenant_id`, `deleted`),
    UNIQUE KEY `uk_phone` (`phone`, `tenant_id`, `deleted`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`, `tenant_id`, `deleted`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `sys_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父权限ID',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `permission_type` TINYINT NOT NULL DEFAULT 1 COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    `path` VARCHAR(200) DEFAULT NULL COMMENT '路径',
    `component` VARCHAR(200) DEFAULT NULL COMMENT '组件',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `updated_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`, `tenant_id`, `deleted`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ====================================
-- 初始化基础数据
-- ====================================

-- 初始化超级管理员用户
INSERT INTO `sys_user` (`id`, `tenant_id`, `username`, `password`, `email`, `real_name`, `nickname`, `status`, `created_by`, `updated_by`) 
VALUES (1, 0, 'admin', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'admin@biobt.com', '超级管理员', '管理员', 1, 1, 1)
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 初始化系统角色
INSERT INTO `sys_role` (`id`, `tenant_id`, `role_code`, `role_name`, `description`, `sort_order`, `status`, `created_by`, `updated_by`) VALUES
(1, 0, 'SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 1, 1, 1, 1),
(2, 0, 'TENANT_ADMIN', '租户管理员', '租户管理员，管理租户内所有资源', 2, 1, 1, 1),
(3, 0, 'USER', '普通用户', '普通用户，基础权限', 3, 1, 1, 1)
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 初始化系统权限
INSERT INTO `sys_permission` (`id`, `tenant_id`, `parent_id`, `permission_code`, `permission_name`, `permission_type`, `path`, `component`, `icon`, `sort_order`, `status`, `created_by`, `updated_by`) VALUES
-- 系统管理
(1, 0, 0, 'SYSTEM', '系统管理', 1, '/system', 'Layout', 'system', 1, 1, 1, 1),
(2, 0, 1, 'USER_MANAGE', '用户管理', 1, '/system/user', 'system/user/index', 'user', 1, 1, 1, 1),
(3, 0, 1, 'ROLE_MANAGE', '角色管理', 1, '/system/role', 'system/role/index', 'role', 2, 1, 1, 1),
(4, 0, 1, 'PERMISSION_MANAGE', '权限管理', 1, '/system/permission', 'system/permission/index', 'permission', 3, 1, 1, 1),
(5, 0, 1, 'TENANT_MANAGE', '租户管理', 1, '/system/tenant', 'system/tenant/index', 'tenant', 4, 1, 1, 1),

-- 业务管理
(10, 0, 0, 'BUSINESS', '业务管理', 1, '/business', 'Layout', 'business', 2, 1, 1, 1),
(11, 0, 10, 'CUSTOMER_MANAGE', '客户管理', 1, '/business/customer', 'business/customer/index', 'customer', 1, 1, 1, 1),
(12, 0, 10, 'PROJECT_MANAGE', '项目管理', 1, '/business/project', 'business/project/index', 'project', 2, 1, 1, 1),
(13, 0, 10, 'CONTRACT_MANAGE', '合同管理', 1, '/business/contract', 'business/contract/index', 'contract', 3, 1, 1, 1),
(14, 0, 10, 'ORDER_MANAGE', '订单管理', 1, '/business/order', 'business/order/index', 'order', 4, 1, 1, 1),
(15, 0, 10, 'PAYMENT_MANAGE', '支付管理', 1, '/business/payment', 'business/payment/index', 'payment', 5, 1, 1, 1),

-- 报表管理
(20, 0, 0, 'REPORT', '报表管理', 1, '/report', 'Layout', 'report', 3, 1, 1, 1),
(21, 0, 20, 'BUSINESS_REPORT', '业务报表', 1, '/report/business', 'report/business/index', 'chart', 1, 1, 1, 1),
(22, 0, 20, 'FINANCIAL_REPORT', '财务报表', 1, '/report/financial', 'report/financial/index', 'money', 2, 1, 1, 1)
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 分配超级管理员角色给admin用户
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `created_by`) 
VALUES (1, 1, 1)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配所有权限给超级管理员角色
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 1, `id`, 1 FROM `sys_permission` WHERE `deleted` = 0
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- ====================================
-- 复制表结构到其他环境数据库
-- ====================================

-- 开发环境
CREATE DATABASE IF NOT EXISTS `biobt_user_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `biobt_user_dev`;
CREATE TABLE IF NOT EXISTS `sys_user` LIKE `biobt_user`.`sys_user`;
CREATE TABLE IF NOT EXISTS `sys_role` LIKE `biobt_user`.`sys_role`;
CREATE TABLE IF NOT EXISTS `sys_permission` LIKE `biobt_user`.`sys_permission`;
CREATE TABLE IF NOT EXISTS `sys_user_role` LIKE `biobt_user`.`sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_role_permission` LIKE `biobt_user`.`sys_role_permission`;

-- 复制基础数据到开发环境
INSERT INTO `sys_user` SELECT * FROM `biobt_user`.`sys_user` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_role` SELECT * FROM `biobt_user`.`sys_role` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_permission` SELECT * FROM `biobt_user`.`sys_permission` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_user_role` SELECT * FROM `biobt_user`.`sys_user_role` ON DUPLICATE KEY UPDATE `created_time` = `created_time`;
INSERT INTO `sys_role_permission` SELECT * FROM `biobt_user`.`sys_role_permission` ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 测试环境
CREATE DATABASE IF NOT EXISTS `biobt_user_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `biobt_user_test`;
CREATE TABLE IF NOT EXISTS `sys_user` LIKE `biobt_user`.`sys_user`;
CREATE TABLE IF NOT EXISTS `sys_role` LIKE `biobt_user`.`sys_role`;
CREATE TABLE IF NOT EXISTS `sys_permission` LIKE `biobt_user`.`sys_permission`;
CREATE TABLE IF NOT EXISTS `sys_user_role` LIKE `biobt_user`.`sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_role_permission` LIKE `biobt_user`.`sys_role_permission`;

-- 复制基础数据到测试环境
INSERT INTO `sys_user` SELECT * FROM `biobt_user`.`sys_user` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_role` SELECT * FROM `biobt_user`.`sys_role` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_permission` SELECT * FROM `biobt_user`.`sys_permission` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_user_role` SELECT * FROM `biobt_user`.`sys_user_role` ON DUPLICATE KEY UPDATE `created_time` = `created_time`;
INSERT INTO `sys_role_permission` SELECT * FROM `biobt_user`.`sys_role_permission` ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 生产环境
CREATE DATABASE IF NOT EXISTS `biobt_user_prod` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `biobt_user_prod`;
CREATE TABLE IF NOT EXISTS `sys_user` LIKE `biobt_user`.`sys_user`;
CREATE TABLE IF NOT EXISTS `sys_role` LIKE `biobt_user`.`sys_role`;
CREATE TABLE IF NOT EXISTS `sys_permission` LIKE `biobt_user`.`sys_permission`;
CREATE TABLE IF NOT EXISTS `sys_user_role` LIKE `biobt_user`.`sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_role_permission` LIKE `biobt_user`.`sys_role_permission`;

-- 复制基础数据到生产环境
INSERT INTO `sys_user` SELECT * FROM `biobt_user`.`sys_user` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_role` SELECT * FROM `biobt_user`.`sys_role` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_permission` SELECT * FROM `biobt_user`.`sys_permission` ON DUPLICATE KEY UPDATE `updated_time` = `updated_time`;
INSERT INTO `sys_user_role` SELECT * FROM `biobt_user`.`sys_user_role` ON DUPLICATE KEY UPDATE `created_time` = `created_time`;
INSERT INTO `sys_role_permission` SELECT * FROM `biobt_user`.`sys_role_permission` ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 显示创建结果
SELECT 'BioBt Platform 数据库初始化完成！' AS message;
SELECT SCHEMA_NAME AS '已创建的数据库' FROM information_schema.SCHEMATA WHERE SCHEMA_NAME LIKE 'biobt_%' ORDER BY SCHEMA_NAME;
SELECT User AS '已创建的用户', Host FROM mysql.user WHERE User LIKE 'biobt_%' ORDER BY User;