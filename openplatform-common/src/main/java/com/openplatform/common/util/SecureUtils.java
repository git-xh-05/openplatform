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

package com.openplatform.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.openplatform.common.config.RsaProperties;
import com.openplatform.common.constant.RegexConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.util.validation.ValidationUtils;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 加密/解密工具类
 *
 * @author Charles7c
 * @since 2022/12/21 21:41
 */
public class SecureUtils {

    private static final Logger log = LoggerFactory.getLogger(SecureUtils.class);

    private SecureUtils() {
    }

    /**
     * 公钥加密
     *
     * @param data 要加密的内容
     * @return 加密后的内容
     */
    public static String encryptByRsaPublicKey(String data) {
        String publicKey = RsaProperties.getPublicKey();
        ValidationUtils.throwIfBlank(publicKey, "请配置 RSA 公钥");
        return encryptByRsaPublicKey(data, publicKey);
    }

    /**
     * 私钥解密
     *
     * @param data 要解密的内容（Base64 加密过）
     * @return 解密后的内容
     */
    public static String decryptByRsaPrivateKey(String data) {
        String privateKey = RsaProperties.getPrivateKey();
        ValidationUtils.throwIfBlank(privateKey, "请配置 RSA 私钥");
        return decryptByRsaPrivateKey(data, privateKey);
    }

    /**
     * 公钥加密
     *
     * @param data      要加密的内容
     * @param publicKey 公钥
     * @return 加密后的内容
     */
    public static String encryptByRsaPublicKey(String data, String publicKey) {
        return new String(SecureUtil.rsa(null, publicKey).encrypt(data, KeyType.PublicKey));
    }

    /**
     * 私钥解密
     *
     * @param data       要解密的内容（Base64 加密过）
     * @param privateKey 私钥
     * @return 解密后的内容
     */
    public static String decryptByRsaPrivateKey(String data, String privateKey) {
        try {
            // 使用 Java 标准 KeyFactory + Cipher 解密，避免 Hutool 解析 PKCS#8 的问题
            byte[] privateKeyBytes = Base64.decode(privateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", ""));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey key = keyFactory.generatePrivate(spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decode(data)));
        } catch (Exception e) {
            log.error("RSA decrypt failed via Java API: {}", e.getMessage(), e);
            throw new RuntimeException("RSA decrypt failed", e);
        }
    }

    /**
     * 解密密码
     *
     * @param encryptedPasswordByRsaPublicKey 密码（已被 Rsa 公钥加密）
     * @param errorMsg                        错误信息
     * @return 解密后的密码
     */
    public static String decryptPasswordByRsaPrivateKey(String encryptedPasswordByRsaPublicKey, String errorMsg) {
        return decryptPasswordByRsaPrivateKey(encryptedPasswordByRsaPublicKey, errorMsg, false);
    }

    /**
     * 解密密码
     *
     * @param encryptedPasswordByRsaPublicKey 密码（已被 Rsa 公钥加密）
     * @param errorMsg                        错误信息
     * @param isVerifyPattern                 是否验证密码格式
     * @return 解密后的密码
     */
    public static String decryptPasswordByRsaPrivateKey(String encryptedPasswordByRsaPublicKey,
                                                        String errorMsg,
                                                        boolean isVerifyPattern) {
        String rawPassword;
        try {
            rawPassword = decryptByRsaPrivateKey(encryptedPasswordByRsaPublicKey);
        } catch (Exception e) {
            log.error("RSA decrypt failed, data length={}, error: {}", 
                encryptedPasswordByRsaPublicKey != null ? encryptedPasswordByRsaPublicKey.length() : 0, 
                e.getMessage(), e);
            rawPassword = null;
        }
        ValidationUtils.throwIfBlank(rawPassword, errorMsg);
        if (isVerifyPattern) {
            ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        }
        return rawPassword;
    }
}
