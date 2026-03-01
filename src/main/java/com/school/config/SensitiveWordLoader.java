package com.school.config;

import com.school.mapper.SensitiveWordMapper;
import com.school.utils.SensitiveWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;

//加载敏感词
@Component
public class SensitiveWordLoader {
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    // 项目启动后自动执行
    @PostConstruct
    public void init() {
        refreshWordCount();
    }

    public void refreshWordCount() {
        List<String> words = sensitiveWordMapper.getWords();
        sensitiveWordUtil.initKeyWord(new HashSet<>(words));
        System.out.println("DFA敏感词库加载完成，词数：" + words.size());
    }
}
