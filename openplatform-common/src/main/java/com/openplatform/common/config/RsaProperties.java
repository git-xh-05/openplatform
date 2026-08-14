/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openplatform.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * RSA 配置属性
 *
 * @author Zheng Jie（ELADMIN）
 * @author Charles7c
 * @since 2022/12/21 20:21
 */
@Component
public class RsaProperties {

    private static final Logger log = LoggerFactory.getLogger(RsaProperties.class);

    private static String privateKey;
    private static String publicKey;

    private final Environment environment;

    public RsaProperties(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        privateKey = environment.getProperty("continew-starter.encrypt.field.private-key");
        publicKey = environment.getProperty("continew-starter.encrypt.field.public-key");
        log.info("RSA keys initialized, privateKey={}, publicKey={}",
            privateKey != null ? "present(" + privateKey.length() + ")" : "null",
            publicKey != null ? "present(" + publicKey.length() + ")" : "null");
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }
}
