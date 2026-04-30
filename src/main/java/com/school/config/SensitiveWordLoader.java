package com.school.config;

import com.school.utils.SensitiveWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Component
public class SensitiveWordLoader {

    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;

    // 初始化加载
    @PostConstruct
    public void init() {
        refreshWordCount();
    }

    // 从TXT文件加载敏感词库
    public void refreshWordCount() {
        Set<String> words = new HashSet<>();
        // 定义文件路径
        String filePath = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "sensitive_words.txt";
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        words.add(line.trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 初始化到工具类
        sensitiveWordUtil.initKeyWord(words);
    }
}