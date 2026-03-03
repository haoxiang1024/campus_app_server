package com.school.config;

import cn.hutool.jwt.Claims;
import com.alibaba.fastjson.JSON;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.utils.ServerResponse;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserStatusInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Value("${jwt.secret}")
    private String secret;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //  从请求头拿到当前登录用户ID
        Integer userId = getCurrentUserId(request);
        if (userId == null) {
            return true; // 未登录不处理
        }
        // 查询用户是否被禁用
        User user = userMapper.getUserById(userId);
        if (user != null && user.getstate() == 0) { // 0=禁用
            ServerResponse result = ServerResponse.createServerResponseByFail(401, "您的账号已被禁用，请联系管理员");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }

        return true;
    }

    private Integer getCurrentUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null; // 无Token，未登录
        }
        String token = authHeader.replace("Bearer ", "").trim();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            Claims claims = (Claims) Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Token中的所有数据：" + claims);
            Object userIdObj = claims.getClaim("");
            if (userIdObj != null) {
                return Integer.parseInt(userIdObj.toString());
            }
        } catch (Exception e) {
            // Token过期/非法/解析失败，返回null即可
            System.out.println("Token解析失败：" + e.getMessage());
            return null;
        }
        return null;
    }
}
