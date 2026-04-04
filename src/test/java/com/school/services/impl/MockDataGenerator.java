package com.school.services.impl;

import com.school.services.api.LostFoundService;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Random;
@Component
public class MockDataGenerator {

    @Autowired
    private LostFoundService lostFoundService;
    // 定义分类配置实体
    static class CategoryConfig {
        int typeId;
        String[] keywords;
        int totalNeeded;

        CategoryConfig(int typeId, String[] keywords, int totalNeeded) {
            this.typeId = typeId;
            this.keywords = keywords;
            this.totalNeeded = totalNeeded;
        }
    }

    // 定义用户配置实体
    static class UserConfig {
        int id;
        String nickname;
        String phone;

        UserConfig(int id, String nickname, String phone) {
            this.id = id;
            this.nickname = nickname;
            this.phone = phone;
        }
    }

    // 定义地点配置实体
    static class PlaceConfig {
        String name;
        double lon;
        double lat;

        PlaceConfig(String name, double lon, double lat) {
            this.name = name;
            this.lon = lon;
            this.lat = lat;
        }
    }

    // 生成模拟数据
    public void generateData() {
        String saveDirectory = "upload/";
        Random random = new Random();

        // 初始化分类与目标数量
        CategoryConfig[] categories = {
                new CategoryConfig(1, new String[]{"手机", "耳机"}, 6),
                new CategoryConfig(2, new String[]{"身份证", "校园卡"}, 5),
                new CategoryConfig(3, new String[]{"高等数学", "英语词典"}, 5),
                new CategoryConfig(4, new String[]{"水杯", "黑色签字笔"}, 6),
                new CategoryConfig(5, new String[]{"钥匙", "雨伞"}, 5)
        };

        // 初始化用户数据
        UserConfig[] users = {
                new UserConfig(1, "admin", "18682675515"),
                new UserConfig(2, "小卷毛", "15181544770"),
                new UserConfig(3, "小豆豆", "14769903669"),
                new UserConfig(4, "小米", "17788266423"),
                new UserConfig(5, "阿宝", "17673297533")
        };

        // 初始化地点数据
        PlaceConfig[] places = {
                new PlaceConfig("西南科技大学(青义校区)-西7教学楼", 104.693323, 31.543984),
                new PlaceConfig("西南科技大学(青义校区西区图书馆)", 104.694491, 31.540717),
                new PlaceConfig("西南科技大学-7食堂", 104.699591, 31.543621),
                new PlaceConfig("西南科技大学(青义校区东1教学楼)", 104.706371, 31.541627),
                new PlaceConfig("西南科技大学东区-体育场", 104.710198, 31.543931)
        };

        // 遍历分类执行生成
        for (CategoryConfig category : categories) {
            int countPerKeyword = (int) Math.ceil((double) category.totalNeeded / category.keywords.length);
            int generatedCount = 0;

            for (String keyword : category.keywords) {
                for (int i = 0; i < countPerKeyword; i++) {
                    if (generatedCount >= category.totalNeeded) break;

                    try {
                        // 随机获取用户与地点
                        UserConfig user = users[random.nextInt(users.length)];
                        PlaceConfig place = places[random.nextInt(places.length)];

                        // 生成基础文本参数
                        long dynamicPubDate = System.currentTimeMillis();
                        boolean isFound = random.nextBoolean();
                        String actionTitle = isFound ? "找到" : "丢失";
                        String state = isFound ? "待认领" : "寻找中";
                        String type = isFound ? "招领" : "失物";

                        String dynamicTitle = actionTitle + keyword;
                        String dynamicContent = "在 " + place.name + " " + actionTitle + "了" + keyword + "，联系电话：" + user.phone;

                        // 获取网络图片并下载
                        String photoUrl = Util.ImageSearch(keyword);
                        String fileName = UUID.randomUUID() + ".jpg";
                        Path targetPath = Paths.get(saveDirectory, fileName);
                        Files.createDirectories(targetPath.getParent());

                        URL url = new URI(photoUrl).toURL();
                        try (InputStream in = url.openStream()) {
                            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }

                        // 拼接完整JSON数据
                        String newLostJson = String.format(
                                "{" +
                                        "\"title\":\"%s\"," +
                                        "\"img\":\"%s\"," +
                                        "\"pubDate\":%d," +
                                        "\"content\":\"%s\"," +
                                        "\"phone\":\"%s\"," +
                                        "\"state\":\"%s\"," +
                                        "\"stick\":0," +
                                        "\"lostfoundtypeId\":%d," +
                                        "\"userId\":%d," +
                                        "\"nickname\":\"%s\"," +
                                        "\"type\":\"%s\"," +
                                        "\"place\":\"%s\"," +
                                        "\"longitude\":%f," +
                                        "\"latitude\":%f" +
                                        "}",
                                dynamicTitle,
                                fileName,
                                dynamicPubDate,
                                dynamicContent,
                                user.phone,
                                state,
                                category.typeId,
                                user.id,
                                user.nickname,
                                type,
                                place.name,
                                place.lon,
                                place.lat
                        );

                        // 写入数据库

                         //lostFoundService.addLostFound(newLostJson);

                        generatedCount++;
                        Thread.sleep(100);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
