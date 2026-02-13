package com.school;

import com.school.services.api.UserService;
import com.school.services.impl.UserServiceImpl;
import com.school.utils.EmailVerificationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SpringBootTest
@EnableFeignClients
class SchoolApplicationTests {
//    private static long appid;
//    @Value("${im.sdk.appid}")
//    public  void setAppid(long appid) {
//        SchoolApplicationTests.appid = appid;
//    }
//
//    private static String key;
//    @Value("${im.key}")
//    public  void setKey(String key) {
//        SchoolApplicationTests.key = key;
//    }
//
//    private static String userid;
//    @Value("${im.userid}")
//   public  void setUserid(String userid) {
//        SchoolApplicationTests.userid = userid;
//    }
//@Autowired
//private UserServiceImpl userService;
//
//    @Test
//    void im() {
//
//
//    }
//    @Test
//    void email() {
//        boolean isValid = EmailVerificationUtils.verifyCode("3502777299@qq.com", "122485");
//        if(isValid){
//            System.out.println("成功");
//        }else {
//            System.out.println("失败");
//
//        }
//
//    }
//    private String getRandomImageUrl(String query) throws Exception {
//        if (query == null || query.isBlank()) {
//            throw new IllegalArgumentException("图片搜索关键词不能为空");
//        }
//        // 构建搜狗图片搜索URL
//        String url = "https://pic.sogou.com/pics?query=" + URLEncoder.encode(query, "utf-8");
//        URLConnection connection = new URL(url).openConnection();
//        // 模拟浏览器请求头，避免被拦截
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
//        connection.setConnectTimeout(5000);
//        connection.setReadTimeout(5000);
//
//        StringBuilder result = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//            }
//        }
//
//        // 正则匹配图片URL
//        String pattern = "\"thumbUrl\":\"(.*?)\"";
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(result.toString());
//
//        ArrayList<String> imageUrlList = new ArrayList<>();
//        while (m.find()) {
//            String imageUrl = m.group(1)
//                    .replaceAll("\\\\u002F", "/")
//                    .replaceAll("\\\\", "");
//            if (imageUrl.startsWith("http")) { // 过滤有效URL
//                imageUrlList.add(imageUrl);
//            }
//        }
//
//        // 无图片时返回默认URL
//        if (imageUrlList.isEmpty()) {
//            return "https://img0.baidu.com/it/u=1234567890,1234567890&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
//        }
//
//        // 随机返回一个URL
//        Random random = new Random();
//        int index = random.nextInt(imageUrlList.size());
//        return imageUrlList.get(index);
//    }
//    @Test
//    public void batchInsertLostData() {
//        // 1. 配置批量参数
//        String[] keywords = {"扇子", "手环", "项链", "耳塞 "}; // 图片关键词
//        int countPerKeyword = 3; // 每个关键词生成3条数据
//
//        // 2. 循环生成+插入数据
//        for (String keyword : keywords) {
//
//            for (int i = 0; i < countPerKeyword; i++) {
//                try {
//                    // 3.1 生成动态参数（避免数据重复）
//                    long dynamicPubDate = System.currentTimeMillis(); // 动态时间戳
//                    String dynamicPhone = "18682675519"; // 动态手机号
//                    String dynamicTitle = "找到" + keyword ; // 动态标题
//                    String dynamicContent = "找到了" + keyword + "，联系电话：" + dynamicPhone; // 动态内容
//
//
//                    // 3.2 获取网络图片URL
//                    String imageUrl = getRandomImageUrl(keyword);
//
//                    // 3.3 直接拼接完整JSON（无需updateJsonByKey，直接包含img字段）
//                    String newLostJson = String.format(
//                            "{\"img\":\"%s\",\"phone\":\"%s\",\"lostfoundtypeId\":5,\"stick\":0,\"place\":\"教学楼\",\"state\":\"未认领\",\"title\":\"%s\",\"pubDate\":%d,\"userId\":5,\"content\":\"%s\"}",
//                            imageUrl,          // img字段直接设为网络图片URL
//                            dynamicPhone,      // 动态手机号
//
//                            dynamicTitle,      // 动态标题
//                            dynamicPubDate,    // 动态时间戳
//                            dynamicContent     // 动态内容
//                    );
//
//                    // 3.4 核心：直接调用lostDetail.addLost插入数据库
//                    //lostDetail.addLostFound(newLostJson);
//                    // 统计成功数
//
//                    // 延迟100ms，避免搜狗图片接口拦截
//                    Thread.sleep(100);
//
//                } catch (Exception e) {
//                    // 统计失败数，单条失败不中断整体循环
//                }
//            }
//        }
//
//    }
}
