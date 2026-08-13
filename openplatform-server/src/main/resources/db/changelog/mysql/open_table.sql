-- liquibase formatted sql
-- changeset openplatform:3
-- comment 初始化开放平台表

CREATE TABLE IF NOT EXISTS `open_platform_app` (
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(100) NOT NULL                                COMMENT '应用名称',
    `access_key`   varchar(64)  NOT NULL                                COMMENT 'Access Key',
    `secret_key`   varchar(255) NOT NULL                                COMMENT 'Secret Key',
    `expire_time`  datetime     DEFAULT NULL                            COMMENT '过期时间',
    `ip_blacklist` varchar(500) DEFAULT NULL                            COMMENT 'IP黑名单',
    `rate_limit`   int(11)      DEFAULT NULL                            COMMENT '速率限制',
    `description`  varchar(200) DEFAULT NULL                            COMMENT '描述',
    `status`       tinyint(1)   UNSIGNED NOT NULL DEFAULT 1             COMMENT '状态（1：启用；2：禁用）',
    `create_user`  bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time`  datetime     NOT NULL                                COMMENT '创建时间',
    `update_user`  bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time`  datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`      bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_access_key` (`access_key`),
    INDEX `idx_create_user` (`create_user`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开放平台应用表';

CREATE TABLE IF NOT EXISTS `open_api` (
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(100) NOT NULL                                COMMENT 'API名称',
    `path`         varchar(255) NOT NULL                                COMMENT '请求路径',
    `method`       varchar(10)  NOT NULL                                COMMENT '请求方法',
    `service_url`  varchar(255) NOT NULL                                COMMENT '后端服务地址',
    `description`  varchar(200) DEFAULT NULL                            COMMENT '描述',
    `status`       tinyint(1)   UNSIGNED NOT NULL DEFAULT 1             COMMENT '状态（1：启用；2：禁用）',
    `create_user`  bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time`  datetime     NOT NULL                                COMMENT '创建时间',
    `update_user`  bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time`  datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`      bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_path_method` (`path`, `method`),
    INDEX `idx_create_user` (`create_user`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开放平台API表';

CREATE TABLE IF NOT EXISTS `open_api_log` (
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_id`         bigint(20)   NOT NULL                                COMMENT '应用ID',
    `api_id`         bigint(20)   NOT NULL                                COMMENT 'API ID',
    `request_params` text         DEFAULT NULL                            COMMENT '请求参数',
    `response_body`  longtext     DEFAULT NULL                            COMMENT '响应体',
    `status_code`    int(11)      NOT NULL                                COMMENT '状态码',
    `cost_time`      bigint(20)   NOT NULL DEFAULT 0                      COMMENT '耗时(ms)',
    `client_ip`      varchar(64)  DEFAULT NULL                            COMMENT '客户端IP',
    `error_message`  text         DEFAULT NULL                            COMMENT '错误信息',
    `create_user`    bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time`    datetime     NOT NULL                                COMMENT '创建时间',
    `update_user`    bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time`    datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`        bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_app_id` (`app_id`),
    INDEX `idx_api_id` (`api_id`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开放平台调用日志表';

CREATE TABLE IF NOT EXISTS `open_subscribe` (
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_id`       bigint(20)   NOT NULL                                COMMENT '应用ID',
    `api_id`       bigint(20)   NOT NULL                                COMMENT 'API ID',
    `status`       tinyint(1)   UNSIGNED NOT NULL DEFAULT 0             COMMENT '状态（0：待审核；1：已通过；2：已拒绝）',
    `quota_limit`  int(11)      DEFAULT NULL                            COMMENT '配额限制',
    `approve_user` bigint(20)   DEFAULT NULL                            COMMENT '审核人',
    `approve_time` datetime     DEFAULT NULL                            COMMENT '审核时间',
    `create_user`  bigint(20)   NOT NULL                                COMMENT '创建人',
    `create_time`  datetime     NOT NULL                                COMMENT '创建时间',
    `update_user`  bigint(20)   DEFAULT NULL                            COMMENT '修改人',
    `update_time`  datetime     DEFAULT NULL                            COMMENT '修改时间',
    `deleted`      bigint(20)   NOT NULL DEFAULT 0                      COMMENT '是否已删除',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_app_api` (`app_id`, `api_id`),
    INDEX `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开放平台订阅表';