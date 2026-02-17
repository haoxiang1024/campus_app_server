package com.school.services.impl;

import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.RongCloudApi;
import com.school.services.api.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class UserServiceImplTest {
    @Autowired
    private RongCloudApi rongCloudApi;
    @Autowired
    private UserMapper userMapper;
    @Test
        void registerUserToProvider() {
        try {
            List<User> userList = userMapper.getalll();
            for (User user : userList) {
                String response = rongCloudApi.getToken(String.valueOf(user.getId()), user.getNickname(), user.getPhoto());
                System.out.println("服务商返回原始数据: " + response);
            }
        } catch (Exception e) {
            System.err.println("调用接口失败: " + e.getMessage());
        }
    }
}