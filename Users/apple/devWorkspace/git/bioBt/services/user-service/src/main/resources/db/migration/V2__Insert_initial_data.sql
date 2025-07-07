-- BioBt Platform 用户服务数据库迁移脚本 V2
-- 插入初始化数据

-- 设置字符集
SET NAMES utf8mb4;

-- 初始化超级管理员用户
-- 密码: admin123 (BCrypt加密)
INSERT INTO `sys_user` (`id`, `tenant_id`, `username`, `password`, `email`, `real_name`, `nickname`, `status`, `created_by`, `updated_by`, `remark`) 
VALUES (1, 0, 'admin', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'admin@biobt.com', '超级管理员', '管理员', 1, 1, 1, '系统初始化创建的超级管理员账户')
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 初始化系统角色
INSERT INTO `sys_role` (`id`, `tenant_id`, `role_code`, `role_name`, `description`, `sort_order`, `status`, `created_by`, `updated_by`) VALUES
(1, 0, 'SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 1, 1, 1, 1),
(2, 0, 'TENANT_ADMIN', '租户管理员', '租户管理员，管理租户内所有资源', 2, 1, 1, 1),
(3, 0, 'PROJECT_MANAGER', '项目经理', '项目经理，管理项目相关业务', 3, 1, 1, 1),
(4, 0, 'SALES_MANAGER', '销售经理', '销售经理，管理销售相关业务', 4, 1, 1, 1),
(5, 0, 'FINANCE_MANAGER', '财务经理', '财务经理，管理财务相关业务', 5, 1, 1, 1),
(6, 0, 'CUSTOMER_SERVICE', '客服人员', '客服人员，处理客户服务相关业务', 6, 1, 1, 1),
(7, 0, 'ORDINARY_USER', '普通用户', '普通用户，基础权限', 7, 1, 1, 1)
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 初始化系统权限
INSERT INTO `sys_permission` (`id`, `tenant_id`, `parent_id`, `permission_code`, `permission_name`, `permission_type`, `path`, `component`, `icon`, `sort_order`, `status`, `created_by`, `updated_by`) VALUES
-- 系统管理模块
(1, 0, 0, 'SYSTEM', '系统管理', 1, '/system', 'Layout', 'system', 1, 1, 1, 1),
(2, 0, 1, 'USER_MANAGE', '用户管理', 1, '/system/user', 'system/user/index', 'user', 1, 1, 1, 1),
(3, 0, 2, 'USER_CREATE', '新增用户', 2, '', '', '', 1, 1, 1, 1),
(4, 0, 2, 'USER_UPDATE', '修改用户', 2, '', '', '', 2, 1, 1, 1),
(5, 0, 2, 'USER_DELETE', '删除用户', 2, '', '', '', 3, 1, 1, 1),
(6, 0, 2, 'USER_QUERY', '查询用户', 2, '', '', '', 4, 1, 1, 1),
(7, 0, 2, 'USER_EXPORT', '导出用户', 2, '', '', '', 5, 1, 1, 1),
(8, 0, 2, 'USER_RESET_PASSWORD', '重置密码', 2, '', '', '', 6, 1, 1, 1),

(10, 0, 1, 'ROLE_MANAGE', '角色管理', 1, '/system/role', 'system/role/index', 'role', 2, 1, 1, 1),
(11, 0, 10, 'ROLE_CREATE', '新增角色', 2, '', '', '', 1, 1, 1, 1),
(12, 0, 10, 'ROLE_UPDATE', '修改角色', 2, '', '', '', 2, 1, 1, 1),
(13, 0, 10, 'ROLE_DELETE', '删除角色', 2, '', '', '', 3, 1, 1, 1),
(14, 0, 10, 'ROLE_QUERY', '查询角色', 2, '', '', '', 4, 1, 1, 1),
(15, 0, 10, 'ROLE_ASSIGN_PERMISSION', '分配权限', 2, '', '', '', 5, 1, 1, 1),

(20, 0, 1, 'PERMISSION_MANAGE', '权限管理', 1, '/system/permission', 'system/permission/index', 'permission', 3, 1, 1, 1),
(21, 0, 20, 'PERMISSION_CREATE', '新增权限', 2, '', '', '', 1, 1, 1, 1),
(22, 0, 20, 'PERMISSION_UPDATE', '修改权限', 2, '', '', '', 2, 1, 1, 1),
(23, 0, 20, 'PERMISSION_DELETE', '删除权限', 2, '', '', '', 3, 1, 1, 1),
(24, 0, 20, 'PERMISSION_QUERY', '查询权限', 2, '', '', '', 4, 1, 1, 1),

(30, 0, 1, 'TENANT_MANAGE', '租户管理', 1, '/system/tenant', 'system/tenant/index', 'tenant', 4, 1, 1, 1),
(31, 0, 30, 'TENANT_CREATE', '新增租户', 2, '', '', '', 1, 1, 1, 1),
(32, 0, 30, 'TENANT_UPDATE', '修改租户', 2, '', '', '', 2, 1, 1, 1),
(33, 0, 30, 'TENANT_DELETE', '删除租户', 2, '', '', '', 3, 1, 1, 1),
(34, 0, 30, 'TENANT_QUERY', '查询租户', 2, '', '', '', 4, 1, 1, 1),

-- 业务管理模块
(100, 0, 0, 'BUSINESS', '业务管理', 1, '/business', 'Layout', 'business', 2, 1, 1, 1),
(101, 0, 100, 'CUSTOMER_MANAGE', '客户管理', 1, '/business/customer', 'business/customer/index', 'customer', 1, 1, 1, 1),
(102, 0, 101, 'CUSTOMER_CREATE', '新增客户', 2, '', '', '', 1, 1, 1, 1),
(103, 0, 101, 'CUSTOMER_UPDATE', '修改客户', 2, '', '', '', 2, 1, 1, 1),
(104, 0, 101, 'CUSTOMER_DELETE', '删除客户', 2, '', '', '', 3, 1, 1, 1),
(105, 0, 101, 'CUSTOMER_QUERY', '查询客户', 2, '', '', '', 4, 1, 1, 1),
(106, 0, 101, 'CUSTOMER_EXPORT', '导出客户', 2, '', '', '', 5, 1, 1, 1),

(110, 0, 100, 'PROJECT_MANAGE', '项目管理', 1, '/business/project', 'business/project/index', 'project', 2, 1, 1, 1),
(111, 0, 110, 'PROJECT_CREATE', '新增项目', 2, '', '', '', 1, 1, 1, 1),
(112, 0, 110, 'PROJECT_UPDATE', '修改项目', 2, '', '', '', 2, 1, 1, 1),
(113, 0, 110, 'PROJECT_DELETE', '删除项目', 2, '', '', '', 3, 1, 1, 1),
(114, 0, 110, 'PROJECT_QUERY', '查询项目', 2, '', '', '', 4, 1, 1, 1),
(115, 0, 110, 'PROJECT_EXPORT', '导出项目', 2, '', '', '', 5, 1, 1, 1),

(120, 0, 100, 'CONTRACT_MANAGE', '合同管理', 1, '/business/contract', 'business/contract/index', 'contract', 3, 1, 1, 1),
(121, 0, 120, 'CONTRACT_CREATE', '新增合同', 2, '', '', '', 1, 1, 1, 1),
(122, 0, 120, 'CONTRACT_UPDATE', '修改合同', 2, '', '', '', 2, 1, 1, 1),
(123, 0, 120, 'CONTRACT_DELETE', '删除合同', 2, '', '', '', 3, 1, 1, 1),
(124, 0, 120, 'CONTRACT_QUERY', '查询合同', 2, '', '', '', 4, 1, 1, 1),
(125, 0, 120, 'CONTRACT_EXPORT', '导出合同', 2, '', '', '', 5, 1, 1, 1),
(126, 0, 120, 'CONTRACT_APPROVE', '审批合同', 2, '', '', '', 6, 1, 1, 1),

(130, 0, 100, 'ORDER_MANAGE', '订单管理', 1, '/business/order', 'business/order/index', 'order', 4, 1, 1, 1),
(131, 0, 130, 'ORDER_CREATE', '新增订单', 2, '', '', '', 1, 1, 1, 1),
(132, 0, 130, 'ORDER_UPDATE', '修改订单', 2, '', '', '', 2, 1, 1, 1),
(133, 0, 130, 'ORDER_DELETE', '删除订单', 2, '', '', '', 3, 1, 1, 1),
(134, 0, 130, 'ORDER_QUERY', '查询订单', 2, '', '', '', 4, 1, 1, 1),
(135, 0, 130, 'ORDER_EXPORT', '导出订单', 2, '', '', '', 5, 1, 1, 1),
(136, 0, 130, 'ORDER_APPROVE', '审批订单', 2, '', '', '', 6, 1, 1, 1),

(140, 0, 100, 'PAYMENT_MANAGE', '支付管理', 1, '/business/payment', 'business/payment/index', 'payment', 5, 1, 1, 1),
(141, 0, 140, 'PAYMENT_CREATE', '新增支付', 2, '', '', '', 1, 1, 1, 1),
(142, 0, 140, 'PAYMENT_UPDATE', '修改支付', 2, '', '', '', 2, 1, 1, 1),
(143, 0, 140, 'PAYMENT_DELETE', '删除支付', 2, '', '', '', 3, 1, 1, 1),
(144, 0, 140, 'PAYMENT_QUERY', '查询支付', 2, '', '', '', 4, 1, 1, 1),
(145, 0, 140, 'PAYMENT_EXPORT', '导出支付', 2, '', '', '', 5, 1, 1, 1),
(146, 0, 140, 'PAYMENT_REFUND', '退款处理', 2, '', '', '', 6, 1, 1, 1),

-- 报表管理模块
(200, 0, 0, 'REPORT', '报表管理', 1, '/report', 'Layout', 'report', 3, 1, 1, 1),
(201, 0, 200, 'BUSINESS_REPORT', '业务报表', 1, '/report/business', 'report/business/index', 'chart', 1, 1, 1, 1),
(202, 0, 201, 'BUSINESS_REPORT_VIEW', '查看业务报表', 2, '', '', '', 1, 1, 1, 1),
(203, 0, 201, 'BUSINESS_REPORT_EXPORT', '导出业务报表', 2, '', '', '', 2, 1, 1, 1),

(210, 0, 200, 'FINANCIAL_REPORT', '财务报表', 1, '/report/financial', 'report/financial/index', 'money', 2, 1, 1, 1),
(211, 0, 210, 'FINANCIAL_REPORT_VIEW', '查看财务报表', 2, '', '', '', 1, 1, 1, 1),
(212, 0, 210, 'FINANCIAL_REPORT_EXPORT', '导出财务报表', 2, '', '', '', 2, 1, 1, 1),

(220, 0, 200, 'STATISTICAL_REPORT', '统计报表', 1, '/report/statistical', 'report/statistical/index', 'data', 3, 1, 1, 1),
(221, 0, 220, 'STATISTICAL_REPORT_VIEW', '查看统计报表', 2, '', '', '', 1, 1, 1, 1),
(222, 0, 220, 'STATISTICAL_REPORT_EXPORT', '导出统计报表', 2, '', '', '', 2, 1, 1, 1),

-- 个人中心模块
(300, 0, 0, 'PERSONAL', '个人中心', 1, '/personal', 'Layout', 'user', 4, 1, 1, 1),
(301, 0, 300, 'PERSONAL_INFO', '个人信息', 1, '/personal/info', 'personal/info/index', 'info', 1, 1, 1, 1),
(302, 0, 301, 'PERSONAL_INFO_UPDATE', '修改个人信息', 2, '', '', '', 1, 1, 1, 1),
(303, 0, 301, 'PERSONAL_PASSWORD_CHANGE', '修改密码', 2, '', '', '', 2, 1, 1, 1),

(310, 0, 300, 'PERSONAL_LOG', '操作日志', 1, '/personal/log', 'personal/log/index', 'log', 2, 1, 1, 1),
(311, 0, 310, 'PERSONAL_LOG_VIEW', '查看操作日志', 2, '', '', '', 1, 1, 1, 1)
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 分配超级管理员角色给admin用户
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `created_by`) 
VALUES (1, 1, 1)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配所有权限给超级管理员角色
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 1, `id`, 1 FROM `sys_permission` WHERE `deleted` = 0
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配租户管理员权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 2, `id`, 1 FROM `sys_permission` 
WHERE `deleted` = 0 
AND `permission_code` IN (
    'BUSINESS', 'CUSTOMER_MANAGE', 'CUSTOMER_CREATE', 'CUSTOMER_UPDATE', 'CUSTOMER_DELETE', 'CUSTOMER_QUERY', 'CUSTOMER_EXPORT',
    'PROJECT_MANAGE', 'PROJECT_CREATE', 'PROJECT_UPDATE', 'PROJECT_DELETE', 'PROJECT_QUERY', 'PROJECT_EXPORT',
    'CONTRACT_MANAGE', 'CONTRACT_CREATE', 'CONTRACT_UPDATE', 'CONTRACT_DELETE', 'CONTRACT_QUERY', 'CONTRACT_EXPORT', 'CONTRACT_APPROVE',
    'ORDER_MANAGE', 'ORDER_CREATE', 'ORDER_UPDATE', 'ORDER_DELETE', 'ORDER_QUERY', 'ORDER_EXPORT', 'ORDER_APPROVE',
    'PAYMENT_MANAGE', 'PAYMENT_CREATE', 'PAYMENT_UPDATE', 'PAYMENT_DELETE', 'PAYMENT_QUERY', 'PAYMENT_EXPORT', 'PAYMENT_REFUND',
    'REPORT', 'BUSINESS_REPORT', 'BUSINESS_REPORT_VIEW', 'BUSINESS_REPORT_EXPORT',
    'FINANCIAL_REPORT', 'FINANCIAL_REPORT_VIEW', 'FINANCIAL_REPORT_EXPORT',
    'STATISTICAL_REPORT', 'STATISTICAL_REPORT_VIEW', 'STATISTICAL_REPORT_EXPORT',
    'PERSONAL', 'PERSONAL_INFO', 'PERSONAL_INFO_UPDATE', 'PERSONAL_PASSWORD_CHANGE',
    'PERSONAL_LOG', 'PERSONAL_LOG_VIEW'
)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配项目经理权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 3, `id`, 1 FROM `sys_permission` 
WHERE `deleted` = 0 
AND `permission_code` IN (
    'BUSINESS', 'CUSTOMER_MANAGE', 'CUSTOMER_QUERY',
    'PROJECT_MANAGE', 'PROJECT_CREATE', 'PROJECT_UPDATE', 'PROJECT_DELETE', 'PROJECT_QUERY', 'PROJECT_EXPORT',
    'CONTRACT_MANAGE', 'CONTRACT_QUERY',
    'ORDER_MANAGE', 'ORDER_QUERY',
    'REPORT', 'BUSINESS_REPORT', 'BUSINESS_REPORT_VIEW',
    'PERSONAL', 'PERSONAL_INFO', 'PERSONAL_INFO_UPDATE', 'PERSONAL_PASSWORD_CHANGE',
    'PERSONAL_LOG', 'PERSONAL_LOG_VIEW'
)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配销售经理权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 4, `id`, 1 FROM `sys_permission` 
WHERE `deleted` = 0 
AND `permission_code` IN (
    'BUSINESS', 'CUSTOMER_MANAGE', 'CUSTOMER_CREATE', 'CUSTOMER_UPDATE', 'CUSTOMER_DELETE', 'CUSTOMER_QUERY', 'CUSTOMER_EXPORT',
    'CONTRACT_MANAGE', 'CONTRACT_CREATE', 'CONTRACT_UPDATE', 'CONTRACT_QUERY', 'CONTRACT_EXPORT',
    'ORDER_MANAGE', 'ORDER_CREATE', 'ORDER_UPDATE', 'ORDER_QUERY', 'ORDER_EXPORT',
    'REPORT', 'BUSINESS_REPORT', 'BUSINESS_REPORT_VIEW', 'BUSINESS_REPORT_EXPORT',
    'PERSONAL', 'PERSONAL_INFO', 'PERSONAL_INFO_UPDATE', 'PERSONAL_PASSWORD_CHANGE',
    'PERSONAL_LOG', 'PERSONAL_LOG_VIEW'
)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配财务经理权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 5, `id`, 1 FROM `sys_permission` 
WHERE `deleted` = 0 
AND `permission_code` IN (
    'BUSINESS', 'PAYMENT_MANAGE', 'PAYMENT_QUERY', 'PAYMENT_REFUND',
    'REPORT', 'FINANCIAL_REPORT', 'FINANCIAL_REPORT_VIEW', 'FINANCIAL_REPORT_EXPORT',
    'STATISTICAL_REPORT', 'STATISTICAL_REPORT_VIEW', 'STATISTICAL_REPORT_EXPORT',
    'PERSONAL', 'PERSONAL_INFO', 'PERSONAL_INFO_UPDATE', 'PERSONAL_PASSWORD_CHANGE',
    'PERSONAL_LOG', 'PERSONAL_LOG_VIEW'
)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配客服人员权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 6, `id`, 1 FROM `sys_permission` 
WHERE `deleted` = 0 
AND `permission_code` IN (
    'BUSINESS', 'CUSTOMER_MANAGE', 'CUSTOMER_QUERY',
    'ORDER_MANAGE', 'ORDER_QUERY',
    'PERSONAL', 'PERSONAL_INFO', 'PERSONAL_INFO_UPDATE', 'PERSONAL_PASSWORD_CHANGE',
    'PERSONAL_LOG', 'PERSONAL_LOG_VIEW'
)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 分配普通用户权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `created_by`)
SELECT 7, `id`, 1 FROM `sys_permission` 
WHERE `deleted` = 0 
AND `permission_code` IN (
    'PERSONAL', 'PERSONAL_INFO', 'PERSONAL_INFO_UPDATE', 'PERSONAL_PASSWORD_CHANGE',
    'PERSONAL_LOG', 'PERSONAL_LOG_VIEW'
)
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;

-- 创建一些测试用户
INSERT INTO `sys_user` (`id`, `tenant_id`, `username`, `password`, `email`, `real_name`, `nickname`, `status`, `created_by`, `updated_by`, `remark`) VALUES
(2, 1, 'tenant_admin', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'tenant_admin@biobt.com', '租户管理员', '租户管理员', 1, 1, 1, '租户管理员测试账户'),
(3, 1, 'project_manager', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'pm@biobt.com', '项目经理', '项目经理', 1, 1, 1, '项目经理测试账户'),
(4, 1, 'sales_manager', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'sales@biobt.com', '销售经理', '销售经理', 1, 1, 1, '销售经理测试账户'),
(5, 1, 'finance_manager', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'finance@biobt.com', '财务经理', '财务经理', 1, 1, 1, '财务经理测试账户'),
(6, 1, 'customer_service', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'cs@biobt.com', '客服人员', '客服人员', 1, 1, 1, '客服人员测试账户'),
(7, 1, 'ordinary_user', '$2a$10$7JB720yubVSOfvVWbGReyO.Tu4QbdZMiD.Ztf/7w4uMaLOy.Hm8jq', 'user@biobt.com', '普通用户', '普通用户', 1, 1, 1, '普通用户测试账户')
ON DUPLICATE KEY UPDATE `updated_time` = CURRENT_TIMESTAMP;

-- 分配角色给测试用户
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `created_by`) VALUES
(2, 2, 1), -- 租户管理员
(3, 3, 1), -- 项目经理
(4, 4, 1), -- 销售经理
(5, 5, 1), -- 财务经理
(6, 6, 1), -- 客服人员
(7, 7, 1)  -- 普通用户
ON DUPLICATE KEY UPDATE `created_time` = `created_time`;