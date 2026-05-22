package com.school.utils;

import com.school.services.api.RongCloudApi;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Util {
    @Value("${file.upload.path}")
    private  String uploadPath;

    private static final int WORK_FACTOR = 12;
    private static RongCloudApi staticRongCloudApi;
    @Autowired
    private  RongCloudApi rongCloudApi;
    @PostConstruct
    public void init(){
        staticRongCloudApi = this.rongCloudApi;
    }

    public static String NickNameRandom() {
        String[] TWO_LETTERS = {"阿宝", "蓝妹", "娃娃", "小花", "小丸", "皮蛋",
                "乐乐", "小兔", "茶茶", "奶糖", "亲亲", "小虎",
                "小猫", "小狗", "小鸟", "小羊", "小龙", "小鹿",
                "小雨", "银子", "小美", "小妞", "爱心", "乖乖",
                "小鱼", "欣欣", "笑笑", "小熊", "小娃", "蜜蜂",
                "糖糖", "大王", "小贝", "小太阳", "小米", "小蚂蚁",
                "小珍珠", "胖胖", "多多", "小瑜", "小丹", "小坤",
                "小辉", "小妮", "小艺", "小萌", "瑶瑶", "小菲",
                "小凡"};

        final String[] THREE_LETTERS = {"小草莓", "小蜜蜂", "小蚂蚁", "小熊猫", "小白兔", "小乖乖",
                "小可爱", "小甜甜", "小糖果", "小霸王", "小跳跳", "小小虎",
                "小土豆", "小豆豆", "小蘑菇", "小仙女", "小妞妞", "小宝贝",
                "小喵喵", "小汪汪", "小姐姐", "小妹妹", "小斑点", "小狮子",
                "小飞花", "小精灵", "小天使", "小懒虫", "小香蕉", "小葡萄",
                "小奶瓶", "小火腿", "小书虫", "小蛋糕", "小铃铛", "小卷毛",
                "小蜗牛", "小小猪", "小甜心", "小苹果", "小陀螺", "小海豚",
                "小小雅", "小呆呆", "小寿司", "小饼干", "小雪糕", "小水蜜桃",
                "小可爱茶", "小甜甜梦"};
        final Random rand = new Random();
        int type = rand.nextInt(2);
        if (type == 0) {
            return TWO_LETTERS[rand.nextInt(TWO_LETTERS.length)];
        } else {
            return THREE_LETTERS[rand.nextInt(THREE_LETTERS.length)];
        }
    }

    public static String SexRandom(){
        final Random rand = new Random();
        int type = rand.nextInt(2);

        return type == 0 ? "男" : "女";
    }

    public String updatePic(String oldPic) {
        Pattern pattern = Pattern.compile(".*http.*");
        Matcher matcher = pattern.matcher(oldPic);
        String savePath = "http://"+Ip.getIp()+":8081/school/upload/";
        //String savePath = "http://123.207.51.104:8081/school/upload/";
        if (!matcher.matches()) {
            return savePath+oldPic;
        }
        return oldPic;
    }



    public static String ImageSearch(String query) throws Exception {
        String url = "https://pic.sogou.com/pics?query=" + URLEncoder.encode(query, "utf-8");
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }

        String pattern = "\"thumbUrl\":\"(.*?)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result.toString());

        ArrayList<String> imageUrlList = new ArrayList<>();
        while (m.find()) {
            String imageUrl = m.group(1);
            imageUrlList.add(imageUrl);
        }
        Random random = new Random();
        int index = random.nextInt(imageUrlList.size());
        return imageUrlList.get(index).replaceAll("\\\\u002F", "/");
    }

    @NotNull
    public String getFileName(MultipartFile file) {
        String savePath = PathUtils.getUploadPath();
        String fileName = UUID.randomUUID() + ".jpg";
        try {
            File dest = new File(savePath + fileName);
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败：" + e.getMessage();
        }
    }

    public static String encryptPwd(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        String salt = BCrypt.gensalt(WORK_FACTOR);

        return BCrypt.hashpw(plainPassword, salt);
    }


    public static boolean verifyPwd(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        return BCrypt.checkpw(plainPassword, hashedPassword);
    }


    public static String registerUserToProvider(String uid, String nickname, String avatarUrl) {
        try {
            return staticRongCloudApi.getToken(uid, nickname, avatarUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
