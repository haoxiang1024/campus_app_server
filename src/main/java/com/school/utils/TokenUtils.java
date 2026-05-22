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


    public String generateToken(Integer userId) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));


        String jwt = Jwts.builder()
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(secretKey)
                .compact();

        return jwt;
    }
}