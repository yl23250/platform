-- 测试数据清理脚本

-- 清理关联表数据
DELETE FROM sys_role_permission;
DELETE FROM sys_user_role;

-- 清理主表数据
DELETE FROM sys_permission;
DELETE FROM sys_role;
DELETE FROM sys_user;

-- 重置自增ID
ALTER TABLE sys_user AUTO_INCREMENT = 1;
ALTER TABLE sys_role AUTO_INCREMENT = 1;
ALTER TABLE sys_permission AUTO_INCREMENT = 1;
ALTER TABLE sys_user_role AUTO_INCREMENT = 1;
ALTER TABLE sys_role_permission AUTO_INCREMENT = 1;