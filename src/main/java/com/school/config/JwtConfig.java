package com.school.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类（统一管理密钥和过期时间）
 */
@Configuration
public class JwtConfig {

    // 从配置文件读取JWT密钥
    @Value("${jwt.secret}")
    private String secret;

    // 从配置文件读取Token过期时间
    @Value("${jwt.expire}")
    private long expire;

    // 获取密钥
    public String getSecret() {
        return secret;
    }

    // 获取过期时间
    public long getExpire() {
        return expire;
    }
}