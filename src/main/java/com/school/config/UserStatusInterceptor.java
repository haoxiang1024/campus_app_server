package com.school.config;

import com.alibaba.fastjson.JSON;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.utils.ServerResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class UserStatusInterceptor implements HandlerInterceptor {



    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if (requestURI.contains("/login") || requestURI.contains("/register") || requestURI.contains("/getIMUserToken")) {
            return true;
        }

        Integer userId = getCurrentUserId(request);


        if (userId == null) {
            return true;
        }


        User user = userMapper.getUserById(userId);
        if (user != null) {
            if (user.getstate() == 0) {

                response.setStatus(401);
                ServerResponse result = ServerResponse.createServerResponseByFail(401, "您的账号已被禁用，请联系管理员");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }
        } else {
            System.out.println("用户不存在，userId：" + userId);
        }

        return true;
    }


    private Integer getCurrentUserId(HttpServletRequest request) throws ExpiredJwtException {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.isEmpty(authHeader)) {
            return null;
        }


        String token;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "").trim();
        } else {
            token = authHeader.trim();
        }

        if (StringUtils.isEmpty(token)) {
            return null;
        }

        if (token.split("\\.").length != 3) {
            return null;
        }

        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                return Integer.parseInt(userIdObj.toString());
            } else {
                return null;
            }

        } catch (SignatureException e) {

            e.printStackTrace();
            return null;
        }
    }
}