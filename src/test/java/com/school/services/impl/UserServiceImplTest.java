package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.RongCloudApi;
import com.school.services.api.UserService;
import com.school.utils.Util;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SpringBootTest

class UserServiceImplTest {
    @Autowired
    private RongCloudApi rongCloudApi;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    UserService userService;
    @Test
        void registerUser() {
        try {
            //rongCloudApi.refresh("1","admin","67197891-bab2-439b-8cfd-21e4d01d4db5.jpg");
            //rongCloudApi.unblock("2");
            for (int i = 0; i <1; i++) {
                // 初始化Faker对象并指定中文环境
                Faker faker = new Faker(new Locale("zh-CN"));
                // 生成随机手机号
                String phoneNumber = faker.phoneNumber().cellPhone();
                // 生成随机邮箱
                String email = faker.internet().emailAddress();
                userService.register(phoneNumber,email,"a12345678",0);

            }
//            List<User> userList = userMapper.getalll();
//            for (User user : userList) {
//                String response = rongCloudApi.getToken(String.valueOf(user.getId()), user.getNickname(), user.getPhoto());
//                System.out.println("服务商返回原始数据: " + response);
//                JSONObject jsonObject = JSON.parseObject(response);
//                String token = jsonObject.getString("token");
//                user.setIm_token(token);
//                userMapper.updateUserInfo(user);
//            }
        } catch (Exception e) {
            System.err.println("调用接口失败: " + e.getMessage());
        }
    }
}