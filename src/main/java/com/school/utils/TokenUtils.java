package com.school.utils;

import com.school.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 生成安卓端需要的Bearer Token
     * @param userId 用户ID
     * @return 格式："Bearer eyJhbGciOiJIUzI1NiJ9.xxxx"
     */
    public String generateToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("exp", new Date(System.currentTimeMillis() + jwtConfig.getExpire())); // 过期时间

        String jwt = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();

        return "Bearer " + jwt;
    }
}
