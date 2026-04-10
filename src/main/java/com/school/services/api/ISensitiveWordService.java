package com.school.services.api;



import java.util.Map;

public interface ISensitiveWordService {
    // 获取敏感词分页列表
    Map<String, Object> getSensitiveWordList(int page, int pageSize, String keyword);

    // 添加敏感词
    boolean addSensitiveWord(String word);

    // 删除单个敏感词
    boolean deleteSensitiveWord(String word);

    // 批量删除敏感词
    boolean batchDeleteSensitiveWords(String words);
}
