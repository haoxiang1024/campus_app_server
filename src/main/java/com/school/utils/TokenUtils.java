package com.school.utils;

import com.school.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenUtils {

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 生成标准JWT Token（仅返回Token本身，不拼接Bearer）
     * @param userId 用户ID
     * @return 纯JWT Token："eyJhbGciOiJIUzI1NiJ9.xxxx.xxxx"
     */
    public String generateToken(Integer userId) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

        // 生成标准JWT Token
        String jwt = Jwts.builder()
                .claim("userId", userId) // 自定义字段：用户ID
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(secretKey)
                .compact();

        return jwt;
    }
}