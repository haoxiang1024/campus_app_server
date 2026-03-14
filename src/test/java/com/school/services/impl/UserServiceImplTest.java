package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.RongCloudApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

class UserServiceImplTest {
    @Autowired
    private RongCloudApi rongCloudApi;
    @Autowired
    private UserMapper userMapper;
    @Test
        void registerUserToProvider() {
        try {
            //rongCloudApi.refresh("1","admin","67197891-bab2-439b-8cfd-21e4d01d4db5.jpg");
            //rongCloudApi.unblock("2");
//            List<User> userList = userMapper.getalll();
//            for (User user : userList) {
//                String response = rongCloudApi.getToken(String.valueOf(user.getId()), user.getNickname(), user.getPhoto());
//                System.out.println("服务商返回原始数据: " + response);
//                JSONObject jsonObject = JSON.parseObject(response);
//                String token = jsonObject.getString("token");
//                user.setIm_token(token);
////                user.setEmail("3502777299@qq.com");
//                userMapper.updateUserInfo(user);
//            }
        } catch (Exception e) {
            System.err.println("调用接口失败: " + e.getMessage());
        }
    }
}