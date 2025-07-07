-- 用户服务初始数据插入脚本
-- 创建时间: 2024-01-01
-- 描述: 插入系统初始数据，包括超级管理员、系统角色和权限

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 插入系统角色数据
-- ----------------------------
INSERT INTO `sys_role` (`id`, `tenant_id`, `role_name`, `role_code`, `role_sort`, `data_scope`, `status`, `remarks`, `create_by`, `create_time`) VALUES
(1, 0, '超级管理员', 'SUPER_ADMIN', 1, 1, 1, '超级管理员角色，拥有所有权限', 1, NOW()),
(2, 0, '租户管理员', 'TENANT_ADMIN', 2, 2, 1, '租户管理员角色，管理租户内所有数据', 1, NOW()),
(3, 0, '项目经理', 'PROJECT_MANAGER', 3, 3, 1, '项目经理角色，管理项目相关数据', 1, NOW()),
(4, 0, '销售经理', 'SALES_MANAGER', 4, 3, 1, '销售经理角色，管理销售相关数据', 1, NOW()),
(5, 0, '财务经理', 'FINANCE_MANAGER', 5, 3, 1, '财务经理角色，管理财务相关数据', 1, NOW()),
(6, 0, '客服人员', 'CUSTOMER_SERVICE', 6, 4, 1, '客服人员角色，处理客户服务', 1, NOW()),
(7, 0, '普通用户', 'ORDINARY_USER', 7, 5, 1, '普通用户角色，基础权限', 1, NOW());

-- ----------------------------
-- 插入系统权限数据
-- ----------------------------
-- 系统管理模块
INSERT INTO `sys_permission` (`id`, `parent_id`, `permission_name`, `permission_code`, `permission_type`, `path`, `component`, `icon`, `sort`, `status`, `visible`, `remarks`, `create_by`, `create_time`) VALUES
(1, 0, '系统管理', 'system', 1, '/system', 'Layout', 'system', 1, 1, 1, '系统管理目录', 1, NOW()),
(2, 1, '用户管理', 'system:user', 1, '/system/user', 'system/user/index', 'user', 1, 1, 1, '用户管理菜单', 1, NOW()),
(3, 2, '用户查询', 'system:user:query', 2, '', '', '', 1, 1, 1, '用户查询按钮', 1, NOW()),
(4, 2, '用户新增', 'system:user:add', 2, '', '', '', 2, 1, 1, '用户新增按钮', 1, NOW()),
(5, 2, '用户修改', 'system:user:edit', 2, '', '', '', 3, 1, 1, '用户修改按钮', 1, NOW()),
(6, 2, '用户删除', 'system:user:remove', 2, '', '', '', 4, 1, 1, '用户删除按钮', 1, NOW()),
(7, 2, '用户导出', 'system:user:export', 2, '', '', '', 5, 1, 1, '用户导出按钮', 1, NOW()),
(8, 2, '用户导入', 'system:user:import', 2, '', '', '', 6, 1, 1, '用户导入按钮', 1, NOW()),
(9, 2, '重置密码', 'system:user:resetPwd', 2, '', '', '', 7, 1, 1, '重置密码按钮', 1, NOW()),

(10, 1, '角色管理', 'system:role', 1, '/system/role', 'system/role/index', 'peoples', 2, 1, 1, '角色管理菜单', 1, NOW()),
(11, 10, '角色查询', 'system:role:query', 2, '', '', '', 1, 1, 1, '角色查询按钮', 1, NOW()),
(12, 10, '角色新增', 'system:role:add', 2, '', '', '', 2, 1, 1, '角色新增按钮', 1, NOW()),
(13, 10, '角色修改', 'system:role:edit', 2, '', '', '', 3, 1, 1, '角色修改按钮', 1, NOW()),
(14, 10, '角色删除', 'system:role:remove', 2, '', '', '', 4, 1, 1, '角色删除按钮', 1, NOW()),
(15, 10, '角色导出', 'system:role:export', 2, '', '', '', 5, 1, 1, '角色导出按钮', 1, NOW()),

(16, 1, '权限管理', 'system:permission', 1, '/system/permission', 'system/permission/index', 'tree', 3, 1, 1, '权限管理菜单', 1, NOW()),
(17, 16, '权限查询', 'system:permission:query', 2, '', '', '', 1, 1, 1, '权限查询按钮', 1, NOW()),
(18, 16, '权限新增', 'system:permission:add', 2, '', '', '', 2, 1, 1, '权限新增按钮', 1, NOW()),
(19, 16, '权限修改', 'system:permission:edit', 2, '', '', '', 3, 1, 1, '权限修改按钮', 1, NOW()),
(20, 16, '权限删除', 'system:permission:remove', 2, '', '', '', 4, 1, 1, '权限删除按钮', 1, NOW()),

-- 业务管理模块
(21, 0, '业务管理', 'business', 1, '/business', 'Layout', 'business', 2, 1, 1, '业务管理目录', 1, NOW()),
(22, 21, '客户管理', 'business:customer', 1, '/business/customer', 'business/customer/index', 'peoples', 1, 1, 1, '客户管理菜单', 1, NOW()),
(23, 22, '客户查询', 'business:customer:query', 2, '', '', '', 1, 1, 1, '客户查询按钮', 1, NOW()),
(24, 22, '客户新增', 'business:customer:add', 2, '', '', '', 2, 1, 1, '客户新增按钮', 1, NOW()),
(25, 22, '客户修改', 'business:customer:edit', 2, '', '', '', 3, 1, 1, '客户修改按钮', 1, NOW()),
(26, 22, '客户删除', 'business:customer:remove', 2, '', '', '', 4, 1, 1, '客户删除按钮', 1, NOW()),

(27, 21, '项目管理', 'business:project', 1, '/business/project', 'business/project/index', 'project', 2, 1, 1, '项目管理菜单', 1, NOW()),
(28, 27, '项目查询', 'business:project:query', 2, '', '', '', 1, 1, 1, '项目查询按钮', 1, NOW()),
(29, 27, '项目新增', 'business:project:add', 2, '', '', '', 2, 1, 1, '项目新增按钮', 1, NOW()),
(30, 27, '项目修改', 'business:project:edit', 2, '', '', '', 3, 1, 1, '项目修改按钮', 1, NOW()),
(31, 27, '项目删除', 'business:project:remove', 2, '', '', '', 4, 1, 1, '项目删除按钮', 1, NOW()),

(32, 21, '合同管理', 'business:contract', 1, '/business/contract', 'business/contract/index', 'contract', 3, 1, 1, '合同管理菜单', 1, NOW()),
(33, 32, '合同查询', 'business:contract:query', 2, '', '', '', 1, 1, 1, '合同查询按钮', 1, NOW()),
(34, 32, '合同新增', 'business:contract:add', 2, '', '', '', 2, 1, 1, '合同新增按钮', 1, NOW()),
(35, 32, '合同修改', 'business:contract:edit', 2, '', '', '', 3, 1, 1, '合同修改按钮', 1, NOW()),
(36, 32, '合同删除', 'business:contract:remove', 2, '', '', '', 4, 1, 1, '合同删除按钮', 1, NOW()),

(37, 21, '订单管理', 'business:order', 1, '/business/order', 'business/order/index', 'order', 4, 1, 1, '订单管理菜单', 1, NOW()),
(38, 37, '订单查询', 'business:order:query', 2, '', '', '', 1, 1, 1, '订单查询按钮', 1, NOW()),
(39, 37, '订单新增', 'business:order:add', 2, '', '', '', 2, 1, 1, '订单新增按钮', 1, NOW()),
(40, 37, '订单修改', 'business:order:edit', 2, '', '', '', 3, 1, 1, '订单修改按钮', 1, NOW()),
(41, 37, '订单删除', 'business:order:remove', 2, '', '', '', 4, 1, 1, '订单删除按钮', 1, NOW()),

-- 报表管理模块
(42, 0, '报表管理', 'report', 1, '/report', 'Layout', 'chart', 3, 1, 1, '报表管理目录', 1, NOW()),
(43, 42, '销售报表', 'report:sales', 1, '/report/sales', 'report/sales/index', 'money', 1, 1, 1, '销售报表菜单', 1, NOW()),
(44, 43, '销售报表查询', 'report:sales:query', 2, '', '', '', 1, 1, 1, '销售报表查询按钮', 1, NOW()),
(45, 43, '销售报表导出', 'report:sales:export', 2, '', '', '', 2, 1, 1, '销售报表导出按钮', 1, NOW()),

(46, 42, '财务报表', 'report:finance', 1, '/report/finance', 'report/finance/index', 'money', 2, 1, 1, '财务报表菜单', 1, NOW()),
(47, 46, '财务报表查询', 'report:finance:query', 2, '', '', '', 1, 1, 1, '财务报表查询按钮', 1, NOW()),
(48, 46, '财务报表导出', 'report:finance:export', 2, '', '', '', 2, 1, 1, '财务报表导出按钮', 1, NOW()),

-- 个人中心模块
(49, 0, '个人中心', 'profile', 1, '/profile', 'Layout', 'user', 4, 1, 1, '个人中心目录', 1, NOW()),
(50, 49, '个人信息', 'profile:info', 1, '/profile/info', 'profile/info/index', 'user', 1, 1, 1, '个人信息菜单', 1, NOW()),
(51, 50, '个人信息查询', 'profile:info:query', 2, '', '', '', 1, 1, 1, '个人信息查询按钮', 1, NOW()),
(52, 50, '个人信息修改', 'profile:info:edit', 2, '', '', '', 2, 1, 1, '个人信息修改按钮', 1, NOW()),
(53, 50, '密码修改', 'profile:info:resetPwd', 2, '', '', '', 3, 1, 1, '密码修改按钮', 1, NOW());

-- ----------------------------
-- 插入超级管理员用户数据
-- ----------------------------
INSERT INTO `sys_user` (`id`, `tenant_id`, `username`, `password`, `email`, `phone`, `real_name`, `nickname`, `avatar`, `gender`, `birthday`, `status`, `lock_status`, `last_login_time`, `last_login_ip`, `login_count`, `password_update_time`, `remarks`, `create_by`, `create_time`) VALUES
(1, 0, 'admin', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'admin@biobt.com', '13800138000', '系统管理员', '超级管理员', NULL, 1, '1990-01-01', 1, 0, NULL, NULL, 0, NOW(), '系统超级管理员账号', 1, NOW());

-- ----------------------------
-- 插入测试用户数据
-- ----------------------------
INSERT INTO `sys_user` (`id`, `tenant_id`, `username`, `password`, `email`, `phone`, `real_name`, `nickname`, `avatar`, `gender`, `birthday`, `status`, `lock_status`, `last_login_time`, `last_login_ip`, `login_count`, `password_update_time`, `remarks`, `create_by`, `create_time`) VALUES
(2, 1, 'tenant_admin', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'tenant@biobt.com', '13800138001', '租户管理员', '租户管理员', NULL, 1, '1985-05-15', 1, 0, NULL, NULL, 0, NOW(), '租户管理员测试账号', 1, NOW()),
(3, 1, 'project_manager', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'pm@biobt.com', '13800138002', '项目经理', '项目经理', NULL, 1, '1988-08-20', 1, 0, NULL, NULL, 0, NOW(), '项目经理测试账号', 1, NOW()),
(4, 1, 'sales_manager', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'sales@biobt.com', '13800138003', '销售经理', '销售经理', NULL, 2, '1987-03-10', 1, 0, NULL, NULL, 0, NOW(), '销售经理测试账号', 1, NOW()),
(5, 1, 'finance_manager', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'finance@biobt.com', '13800138004', '财务经理', '财务经理', NULL, 2, '1989-12-05', 1, 0, NULL, NULL, 0, NOW(), '财务经理测试账号', 1, NOW()),
(6, 1, 'customer_service', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'cs@biobt.com', '13800138005', '客服人员', '客服人员', NULL, 2, '1992-07-18', 1, 0, NULL, NULL, 0, NOW(), '客服人员测试账号', 1, NOW()),
(7, 1, 'ordinary_user', '$2a$10$7JB720yubVSOfvVWbazBuOWShWzhxyiV5c/xCVLHBqJOremONwjjS', 'user@biobt.com', '13800138006', '普通用户', '普通用户', NULL, 1, '1995-11-25', 1, 0, NULL, NULL, 0, NOW(), '普通用户测试账号', 1, NOW());

-- ----------------------------
-- 插入用户角色关联数据
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`, `create_time`) VALUES
(1, 1, 1, NOW()), -- 超级管理员 -> 超级管理员角色
(2, 2, 1, NOW()), -- 租户管理员 -> 租户管理员角色
(3, 3, 1, NOW()), -- 项目经理 -> 项目经理角色
(4, 4, 1, NOW()), -- 销售经理 -> 销售经理角色
(5, 5, 1, NOW()), -- 财务经理 -> 财务经理角色
(6, 6, 1, NOW()), -- 客服人员 -> 客服人员角色
(7, 7, 1, NOW()); -- 普通用户 -> 普通用户角色

-- ----------------------------
-- 插入角色权限关联数据
-- ----------------------------
-- 超级管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`)
SELECT 1, id, 1, NOW() FROM `sys_permission`;

-- 租户管理员权限（除系统管理外的所有权限）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`)
SELECT 2, id, 1, NOW() FROM `sys_permission` WHERE id >= 21;

-- 项目经理权限（业务管理中的项目、合同、订单管理 + 个人中心）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`) VALUES
(3, 21, 1, NOW()), -- 业务管理目录
(3, 27, 1, NOW()), (3, 28, 1, NOW()), (3, 29, 1, NOW()), (3, 30, 1, NOW()), (3, 31, 1, NOW()), -- 项目管理
(3, 32, 1, NOW()), (3, 33, 1, NOW()), (3, 34, 1, NOW()), (3, 35, 1, NOW()), (3, 36, 1, NOW()), -- 合同管理
(3, 37, 1, NOW()), (3, 38, 1, NOW()), (3, 39, 1, NOW()), (3, 40, 1, NOW()), (3, 41, 1, NOW()), -- 订单管理
(3, 49, 1, NOW()), (3, 50, 1, NOW()), (3, 51, 1, NOW()), (3, 52, 1, NOW()), (3, 53, 1, NOW()); -- 个人中心

-- 销售经理权限（客户管理、项目管理、合同管理、订单管理、销售报表 + 个人中心）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`) VALUES
(4, 21, 1, NOW()), -- 业务管理目录
(4, 22, 1, NOW()), (4, 23, 1, NOW()), (4, 24, 1, NOW()), (4, 25, 1, NOW()), (4, 26, 1, NOW()), -- 客户管理
(4, 27, 1, NOW()), (4, 28, 1, NOW()), (4, 29, 1, NOW()), (4, 30, 1, NOW()), (4, 31, 1, NOW()), -- 项目管理
(4, 32, 1, NOW()), (4, 33, 1, NOW()), (4, 34, 1, NOW()), (4, 35, 1, NOW()), (4, 36, 1, NOW()), -- 合同管理
(4, 37, 1, NOW()), (4, 38, 1, NOW()), (4, 39, 1, NOW()), (4, 40, 1, NOW()), (4, 41, 1, NOW()), -- 订单管理
(4, 42, 1, NOW()), -- 报表管理目录
(4, 43, 1, NOW()), (4, 44, 1, NOW()), (4, 45, 1, NOW()), -- 销售报表
(4, 49, 1, NOW()), (4, 50, 1, NOW()), (4, 51, 1, NOW()), (4, 52, 1, NOW()), (4, 53, 1, NOW()); -- 个人中心

-- 财务经理权限（合同管理、订单管理、财务报表 + 个人中心）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`) VALUES
(5, 21, 1, NOW()), -- 业务管理目录
(5, 32, 1, NOW()), (5, 33, 1, NOW()), (5, 34, 1, NOW()), (5, 35, 1, NOW()), (5, 36, 1, NOW()), -- 合同管理
(5, 37, 1, NOW()), (5, 38, 1, NOW()), (5, 39, 1, NOW()), (5, 40, 1, NOW()), (5, 41, 1, NOW()), -- 订单管理
(5, 42, 1, NOW()), -- 报表管理目录
(5, 46, 1, NOW()), (5, 47, 1, NOW()), (5, 48, 1, NOW()), -- 财务报表
(5, 49, 1, NOW()), (5, 50, 1, NOW()), (5, 51, 1, NOW()), (5, 52, 1, NOW()), (5, 53, 1, NOW()); -- 个人中心

-- 客服人员权限（客户管理查询、订单管理查询 + 个人中心）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`) VALUES
(6, 21, 1, NOW()), -- 业务管理目录
(6, 22, 1, NOW()), (6, 23, 1, NOW()), -- 客户管理（仅查询）
(6, 37, 1, NOW()), (6, 38, 1, NOW()), -- 订单管理（仅查询）
(6, 49, 1, NOW()), (6, 50, 1, NOW()), (6, 51, 1, NOW()), (6, 52, 1, NOW()), (6, 53, 1, NOW()); -- 个人中心

-- 普通用户权限（仅个人中心）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`, `create_by`, `create_time`) VALUES
(7, 49, 1, NOW()), (7, 50, 1, NOW()), (7, 51, 1, NOW()), (7, 52, 1, NOW()), (7, 53, 1, NOW()); -- 个人中心

SET FOREIGN_KEY_CHECKS = 1;

-- 注意：所有用户的默认密码都是 "123456"，密码已使用BCrypt加密