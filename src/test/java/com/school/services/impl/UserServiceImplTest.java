package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.LostFoundService;
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
    @Autowired
    private LostFoundService lostFoundService;
    @Autowired
    private MockDataGenerator mockDataGenerator;
    @Test
        void registerUser() {

            //rongCloudApi.refresh("1","admin","67197891-bab2-439b-8cfd-21e4d01d4db5.jpg");
            //rongCloudApi.unblock("2");
//            for (int i = 0; i <1; i++) {
//                // 初始化Faker对象并指定中文环境
//                Faker faker = new Faker(new Locale("zh-CN"));
//                // 生成随机手机号
//                String phoneNumber = faker.phoneNumber().cellPhone();
//                // 生成随机邮箱
//                String email = faker.internet().emailAddress();
//                userService.register(phoneNumber,email,"a12345678",0);
//
//            }
            List<User> userList = userMapper.getalll();
            for (User user : userList) {
                String response = rongCloudApi.getToken(String.valueOf(user.getId()), user.getNickname(), user.getPhoto());
                System.out.println("服务商返回原始数据: " + response);
                JSONObject jsonObject = JSON.parseObject(response);
                String token = jsonObject.getString("token");
                user.setIm_token(token);
                userMapper.updateUserInfo(user);
            }

// 配置基础参数

//            String[] keywords = {"水杯", "手机", "黑色签字笔", "耳机"};
//            int countPerKeyword = 3;
//            String saveDirectory = "upload/";
//
//            // 遍历关键词生成数据
//            for (String keyword : keywords) {
//                for (int i = 0; i < countPerKeyword; i++) {
//
//                        // 生成基础文本与位置参数
//                        long dynamicPubDate = System.currentTimeMillis();
//                        String dynamicPhone = "18682675519";
//                        String dynamicTitle = "找到" + keyword;
//                        String dynamicContent = "找到了" + keyword + "，联系电话：" + dynamicPhone;
//                        double randomLon = 116.40 + Math.random() * 0.1;
//                        double randomLat = 39.90 + Math.random() * 0.1;
//
//                        // 获取网络图片URL
//                        String photoUrl = Util.ImageSearch(keyword);
//
//                        // 生成目标文件路径
//                        String fileName = UUID.randomUUID() + ".jpg";
//                        Path targetPath = Paths.get(saveDirectory, fileName);
//
//                        // 创建目录
//                        Files.createDirectories(targetPath.getParent());
//
//                        // 下载图片到本地
//                        URL url = new URI(photoUrl).toURL();
//                        try (InputStream in = url.openStream()) {
//                            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
//                        }
//
//                        // 获取本地图片路径
//                        String localImagePath = saveDirectory + fileName;
//
//                        // 拼接完整JSON数据
//                        String newLostJson = String.format(
//                                "{" +
//                                        "\"title\":\"%s\"," +
//                                        "\"img\":\"%s\"," +
//                                        "\"pubDate\":%d," +
//                                        "\"content\":\"%s\"," +
//                                        "\"phone\":\"%s\"," +
//                                        "\"state\":\"待认领\"," +
//                                        "\"stick\":0," +
//                                        "\"lostfoundtypeId\":1," +
//                                        "\"userId\":1," +
//                                        "\"nickname\":\"模拟用户\"," +
//                                        "\"type\":\"招领\"," +
//                                        "\"longitude\":%f," +
//                                        "\"latitude\":%f" +
//                                        "}",
//                                dynamicTitle,
//                                fileName,
//                                dynamicPubDate,
//                                dynamicContent,
//                                dynamicPhone,
//                                randomLon,
//                                randomLat
//                        );
//
//                        // 写入数据库
//                        lostFoundService.addLostFound(newLostJson);
//
//
//
//
//                }
//            }
       // mockDataGenerator.generateData();
    }
}