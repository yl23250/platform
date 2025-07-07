-- 测试数据

-- 插入测试角色
INSERT INTO sys_role (id, tenant_id, role_code, role_name, description, status, sort_order, create_by, create_time) VALUES
(1, 1, 'ADMIN', '管理员', '系统管理员角色', 1, 1, 'system', NOW()),
(2, 1, 'USER', '普通用户', '普通用户角色', 1, 2, 'system', NOW()),
(3, 1, 'MANAGER', '经理', '部门经理角色', 1, 3, 'system', NOW());

-- 插入测试权限
INSERT INTO sys_permission (id, parent_id, permission_code, permission_name, permission_type, path, status, sort_order, create_by, create_time) VALUES
(1, 0, 'system', '系统管理', 1, '/system', 1, 1, 'system', NOW()),
(2, 1, 'system:user', '用户管理', 1, '/system/user', 1, 1, 'system', NOW()),
(3, 2, 'system:user:list', '用户列表', 2, '', 1, 1, 'system', NOW()),
(4, 2, 'system:user:create', '创建用户', 2, '', 1, 2, 'system', NOW()),
(5, 2, 'system:user:update', '更新用户', 2, '', 1, 3, 'system', NOW()),
(6, 2, 'system:user:delete', '删除用户', 2, '', 1, 4, 'system', NOW()),
(7, 1, 'system:role', '角色管理', 1, '/system/role', 1, 2, 'system', NOW()),
(8, 7, 'system:role:list', '角色列表', 2, '', 1, 1, 'system', NOW()),
(9, 7, 'system:role:create', '创建角色', 2, '', 1, 2, 'system', NOW()),
(10, 7, 'system:role:update', '更新角色', 2, '', 1, 3, 'system', NOW());

-- 插入测试用户
INSERT INTO sys_user (id, tenant_id, username, password, email, phone, real_name, nickname, gender, birthday, status, lock_status, login_count, password_update_time, create_by, create_time) VALUES
(1, 1, 'admin', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQqiJW4CyLM6c1hpQAVqDAa', 'admin@example.com', '13800138000', '系统管理员', '管理员', 1, '1980-01-01', 1, 0, 10, NOW(), 'system', NOW()),
(2, 1, 'testuser', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQqiJW4CyLM6c1hpQAVqDAa', 'test@example.com', '13800138001', '测试用户', '测试', 2, '1990-05-15', 1, 0, 5, NOW(), 'admin', NOW()),
(3, 1, 'manager', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQqiJW4CyLM6c1hpQAVqDAa', 'manager@example.com', '13800138002', '部门经理', '经理', 1, '1985-10-20', 1, 0, 8, NOW(), 'admin', NOW()),
(4, 1, 'disabled_user', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQqiJW4CyLM6c1hpQAVqDAa', 'disabled@example.com', '13800138003', '禁用用户', '禁用', 1, '1992-03-10', 0, 0, 2, NOW(), 'admin', NOW()),
(5, 1, 'locked_user', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkGQqiJW4CyLM6c1hpQAVqDAa', 'locked@example.com', '13800138004', '锁定用户', '锁定', 2, '1988-12-25', 1, 1, 3, NOW(), 'admin', NOW());

-- 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id, create_by, create_time) VALUES
(1, 1, 'system', NOW()),  -- admin用户拥有管理员角色
(2, 2, 'admin', NOW()),   -- testuser用户拥有普通用户角色
(3, 3, 'admin', NOW()),   -- manager用户拥有经理角色
(3, 2, 'admin', NOW()),   -- manager用户同时拥有普通用户角色
(4, 2, 'admin', NOW()),   -- disabled_user用户拥有普通用户角色
(5, 2, 'admin', NOW());   -- locked_user用户拥有普通用户角色

-- 插入角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id, create_by, create_time) VALUES
-- 管理员角色拥有所有权限
(1, 1, 'system', NOW()),
(1, 2, 'system', NOW()),
(1, 3, 'system', NOW()),
(1, 4, 'system', NOW()),
(1, 5, 'system', NOW()),
(1, 6, 'system', NOW()),
(1, 7, 'system', NOW()),
(1, 8, 'system', NOW()),
(1, 9, 'system', NOW()),
(1, 10, 'system', NOW()),
-- 普通用户角色只有查看权限
(2, 1, 'system', NOW()),
(2, 2, 'system', NOW()),
(2, 3, 'system', NOW()),
(2, 7, 'system', NOW()),
(2, 8, 'system', NOW()),
-- 经理角色拥有用户管理权限
(3, 1, 'system', NOW()),
(3, 2, 'system', NOW()),
(3, 3, 'system', NOW()),
(3, 4, 'system', NOW()),
(3, 5, 'system', NOW()),
(3, 7, 'system', NOW()),
(3, 8, 'system', NOW());