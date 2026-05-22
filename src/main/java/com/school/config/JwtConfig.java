package com.school.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtConfig {


    @Value("${jwt.secret}")
    private String secret;


    @Value("${jwt.expire}")
    private long expire;


    public String getSecret() {
        return secret;
    }


    public long getExpire() {
        return expire;
    }
}