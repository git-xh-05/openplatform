-- liquibase formatted sql
-- changeset openplatform:2
-- comment 初始化系统数据

INSERT INTO `sys_user` (`id`, `username`, `nickname`, `password`, `status`, `is_system`, `create_user`, `create_time`)
VALUES (1, 'admin', '超级管理员', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EHs', 1, 1, 1, NOW());

INSERT INTO `sys_role` (`id`, `name`, `code`, `sort`, `is_system`, `status`, `create_user`, `create_time`)
VALUES (1, '超级管理员', 'super_admin', 1, 1, 1, 1, NOW());

INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_user`, `create_time`)
VALUES (1, 1, 1, 1, NOW());

-- 系统管理
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
(1000, '系统管理', 0, 1, '/system', 'System', 'Layout', '/system/user', 'settings', 0, 0, 0, NULL, 1, 1, 1, NOW()),
(1100, '用户管理', 1000, 2, '/system/user', 'SystemUser', 'system/user/index', NULL, 'user', 0, 0, 0, NULL, 1, 1, 1, NOW()),
(1101, '列表', 1100, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:user:list', 1, 1, 1, NOW()),
(1102, '详情', 1100, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:user:get', 2, 1, 1, NOW()),
(1103, '新增', 1100, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:user:create', 3, 1, 1, NOW()),
(1104, '修改', 1100, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:user:update', 4, 1, 1, NOW()),
(1105, '删除', 1100, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:user:delete', 5, 1, 1, NOW()),
(1201, '列表', 1200, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:role:list', 1, 1, 1, NOW()),
(1202, '详情', 1200, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:role:get', 2, 1, 1, NOW()),
(1203, '新增', 1200, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:role:create', 3, 1, 1, NOW()),
(1204, '修改', 1200, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:role:update', 4, 1, 1, NOW()),
(1205, '删除', 1200, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:role:delete', 5, 1, 1, NOW()),
(1301, '列表', 1300, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:menu:list', 1, 1, 1, NOW()),
(1302, '详情', 1300, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:menu:get', 2, 1, 1, NOW()),
(1303, '新增', 1300, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:menu:create', 3, 1, 1, NOW()),
(1304, '修改', 1300, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:menu:update', 4, 1, 1, NOW()),
(1305, '删除', 1300, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'system:menu:delete', 5, 1, 1, NOW());

-- 开放平台
INSERT INTO `sys_menu` (`id`, `title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`)
VALUES
(7100, '开放平台', 0, 1, '/open-platform', 'OpenPlatform', 'Layout', '/open-platform/api', 'api', 0, 0, 0, NULL, 2, 1, 1, NOW()),
(7110, 'API 管理', 7100, 2, '/open-platform/api', 'OpenPlatformApi', 'open-platform/api/index', NULL, 'common', 0, 0, 0, NULL, 1, 1, 1, NOW()),
(7111, '列表', 7110, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:api:list', 1, 1, 1, NOW()),
(7112, '详情', 7110, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:api:get', 2, 1, 1, NOW()),
(7113, '新增', 7110, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:api:create', 3, 1, 1, NOW()),
(7114, '修改', 7110, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:api:update', 4, 1, 1, NOW()),
(7115, '删除', 7110, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:api:delete', 5, 1, 1, NOW()),
(7121, '列表', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:list', 1, 1, 1, NOW()),
(7122, '详情', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:get', 2, 1, 1, NOW()),
(7123, '新增', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:create', 3, 1, 1, NOW()),
(7124, '修改', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:update', 4, 1, 1, NOW()),
(7125, '删除', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:delete', 5, 1, 1, NOW()),
(7126, '查看密钥', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:secret', 7, 1, 1, NOW()),
(7127, '重置密钥', 7120, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:app:resetSecret', 8, 1, 1, NOW()),
(7131, '列表', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:list', 1, 1, 1, NOW()),
(7132, '详情', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:get', 2, 1, 1, NOW()),
(7133, '新增', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:create', 3, 1, 1, NOW()),
(7134, '修改', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:update', 4, 1, 1, NOW()),
(7135, '删除', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:delete', 5, 1, 1, NOW()),
(7136, '审核通过', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:approve', 6, 1, 1, NOW()),
(7137, '审核拒绝', 7130, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:subscribe:reject', 7, 1, 1, NOW()),
(7141, '列表', 7140, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:log:list', 1, 1, 1, NOW()),
(7142, '详情', 7140, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:log:get', 2, 1, 1, NOW()),
(7151, '查看', 7150, 3, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, 'open-platform:statistics:view', 1, 1, 1, NOW());

-- 超级管理员拥有所有菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_user`, `create_time`)
SELECT 1, id, 1, NOW() FROM `sys_menu`;