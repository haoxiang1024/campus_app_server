package com.school.config;

import com.school.utils.ServerResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ServerResponse handleExpiredJwtException(ExpiredJwtException e) {
        return ServerResponse.createServerResponseByFail(401, "Token已过期，请重新登录");
    }
}
