package com.school.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import com.alibaba.fastjson.JSON;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.utils.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
/**
 * 用户状态拦截器
 * 用于验证用户身份并检查用户状态，确保被禁用的用户无法访问系统资源
 * 实现 HandlerInterceptor 接口，在请求处理前进行拦截校验
 */
public class UserStatusInterceptor implements HandlerInterceptor {

    @Autowired
    /** 用户数据访问对象，用于查询用户信息 */
    private UserMapper userMapper;

    @Value("${jwt.secret}")
    /** JWT 令牌签名密钥，从配置文件中读取 */
    private String secret;

    /**
     * 请求预处理方法
     * 在目标资源执行前调用，用于验证用户身份和检查用户状态
     * 
     * @param request HTTP 请求对象，用于获取请求信息和头部数据
     * @param response HTTP 响应对象，用于设置响应状态和返回错误信息
     * @param handler 被拦截的处理对象
     * @return boolean true-继续执行后续处理；false-终止请求并返回错误响应
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否为免拦截的公开接口（登录、注册、获取 IM 用户 Token）
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/login") || requestURI.contains("/register") || requestURI.contains("/getIMUserToken")) {
            return true;
        }

//        System.out.println("===== 拦截器执行 =====");
//        System.out.println("请求URL：" + requestURI);
        // 获取 Authorization 请求头并解析用户 ID
        String authHeader = request.getHeader("Authorization");
       
        Integer userId = getCurrentUserId(request);
       
        // 如果未携带 Token 或 Token 解析失败，则放行请求
        if (userId == null) {
            return true;
        }

        // 根据用户 ID 查询用户信息并校验用户状态
        User user = userMapper.getUserById(userId);
        if (user != null) {
            System.out.println("用户状态：" + user.getstate());
            // 检查用户是否被禁用（state=0 表示禁用）
            if (user.getstate() == 0) {
                // 用户被禁用，返回 401 错误和提示信息
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

    /**
     * 从 JWT Token 中解析当前用户 ID
     * 通过解析 Authorization 头中的 Bearer Token 获取用户身份信息
     * 
     * @param request HTTP 请求对象，用于获取 Authorization 请求头
     * @return Integer 用户 ID，如果 Token 无效或解析失败则返回 null
     */
    private Integer getCurrentUserId(HttpServletRequest request) {
        // 获取并校验 Authorization 请求头
        String authHeader = request.getHeader("Authorization");
        
        if (StringUtils.isEmpty(authHeader)) {
            return null;
        }

        // 提取 Token 字符串，支持 Bearer 前缀格式
        String token;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "").trim();
        } else {
            // 兼容处理：缺少 Bearer 前缀时直接使用原值
            token = authHeader.trim();
        }

        // 校验 Token 非空及格式正确性（标准 JWT 包含 3 段，用.分隔）
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        
        if (token.split("\\.").length != 3) {
            return null;
        }

        try {
            // 使用配置的密钥创建签名密钥对象
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
            // 解析并验证 JWT Token，提取 Claims 负载数据
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        
            // 从 Claims 中获取 userId 字段并转换为 Integer 类型
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                return Integer.parseInt(userIdObj.toString());
            } else {
                return null;
            }

        } catch (SignatureException e) {
            // 捕获签名验证异常，表示 Token 签名与当前密钥不匹配
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // 捕获其他解析异常（如 Token 过期、格式错误等）
            e.printStackTrace();
            return null;
        }
    }
}