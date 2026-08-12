-- liquibase formatted sql
-- changeset openplatform:1
-- comment 初始化系统表

CREATE TABLE IF NOT EXISTS `sys_user` (
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`     varchar(64)  NOT NULL                                COMMENT '用户名',
    `nickname`     varchar(64)  NOT NULL                                COMMENT '昵称',
    `password`     varchar(255) NOT NULL                                COMMENT '密码',
    `gender`       varchar(10)  DEFAULT NULL                            COMMENT '性别',
    `email`        varchar(100) DEFAULT NULL                            COMMENT '邮箱',
    `phone`        varchar(20)  DEFAULT NULL                            COMMENT '手机号',
    `avatar`       varchar(500) DEFAULT NULL                            COMMENT '头像',
    `description`  varchar(200) DEFAULT NULL                            COMMENT '描述',
    `status`       tinyint(1)   UNSIGNED NOT NULL DEFAULT 1             COMMENT '状态（1：启用；2：禁用）',
    `is_system`    tinyint(1)   UNSIGNED NOT NULL DEFAULT 0             COMMENT '是否系统内置',
    `pwd_reset_time` datetime   DEFAULT NULL                            COMMENT '最后一次修改密码时间',
    `create_user`  bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time`  datetime     NOT NULL                                COMMENT '创建时间',
    `update_user`  bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time`  datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`      bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_username` (`username`, `deleted`),
    INDEX `idx_create_user` (`create_user`),
    INDEX `idx_update_user` (`update_user`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `sys_role` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(64)  NOT NULL                                COMMENT '名称',
    `code`        varchar(64)  NOT NULL                                COMMENT '编码',
    `description` varchar(200) DEFAULT NULL                            COMMENT '描述',
    `sort`        int(11)      NOT NULL DEFAULT 0                      COMMENT '排序',
    `is_system`   tinyint(1)   UNSIGNED NOT NULL DEFAULT 0             COMMENT '是否系统内置',
    `status`      tinyint(1)   UNSIGNED NOT NULL DEFAULT 1             COMMENT '状态（1：启用；2：禁用）',
    `create_user` bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time` datetime     NOT NULL                                COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`     bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_code` (`code`, `deleted`),
    INDEX `idx_create_user` (`create_user`),
    INDEX `idx_update_user` (`update_user`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title`       varchar(64)  NOT NULL                                COMMENT '标题',
    `parent_id`   bigint(20)   NOT NULL DEFAULT 0                      COMMENT '父级ID',
    `type`        tinyint(1)   UNSIGNED NOT NULL                       COMMENT '类型（1：目录；2：菜单；3：按钮）',
    `path`        varchar(255) DEFAULT NULL                            COMMENT '路由地址',
    `name`        varchar(64)  DEFAULT NULL                            COMMENT '路由名称',
    `component`   varchar(255) DEFAULT NULL                            COMMENT '组件路径',
    `redirect`    varchar(255) DEFAULT NULL                            COMMENT '重定向地址',
    `icon`        varchar(64)  DEFAULT NULL                            COMMENT '图标',
    `is_external` tinyint(1)   UNSIGNED NOT NULL DEFAULT 0             COMMENT '是否外链',
    `is_cache`    tinyint(1)   UNSIGNED NOT NULL DEFAULT 0             COMMENT '是否缓存',
    `is_hidden`   tinyint(1)   UNSIGNED NOT NULL DEFAULT 0             COMMENT '是否隐藏',
    `permission`  varchar(100) DEFAULT NULL                            COMMENT '权限标识',
    `sort`        int(11)      NOT NULL DEFAULT 0                      COMMENT '排序',
    `status`      tinyint(1)   UNSIGNED NOT NULL DEFAULT 1             COMMENT '状态（1：启用；2：禁用）',
    `create_user` bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time` datetime     NOT NULL                                COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`     bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_create_user` (`create_user`),
    INDEX `idx_update_user` (`update_user`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint(20)   NOT NULL                                COMMENT '用户ID',
    `role_id`     bigint(20)   NOT NULL                                COMMENT '角色ID',
    `create_user` bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time` datetime     NOT NULL                                COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`     bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_user_role` (`user_id`, `role_id`, `deleted`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`     bigint(20)   NOT NULL                                COMMENT '角色ID',
    `menu_id`     bigint(20)   NOT NULL                                COMMENT '菜单ID',
    `create_user` bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time` datetime     NOT NULL                                COMMENT '创建时间',
    `update_user` bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time` datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`     bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_role_menu` (`role_id`, `menu_id`, `deleted`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';