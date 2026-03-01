package com.school.utils;
//敏感词工具类

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Service
public class SensitiveWordUtil {
    // 敏感词库树
    private static Map<Object, Object> sensitiveWordMap;
    // 最小匹配规则（如：匹配“敏感”，文本为“敏感词”，匹配到“敏感”即返回）
    public static final int MIN_MATCH_TYPE = 1;
    // 最大匹配规则（如：匹配“敏感词”，文本为“敏感词”，匹配到“敏感词”才返回）
    public static final int MAX_MATCH_TYPE = 2;

    /**
     * 初始化敏感词库，构建DFA算法模型
     */
    public static void initKeyWord(Set<String> wordSet) {
        sensitiveWordMap = new HashMap<>(wordSet.size());
        for (String key : wordSet) {
            Map nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    Map<String, String> newPinnerMap = new HashMap<>();
                    newPinnerMap.put("isEnd", "0");
                    nowMap.put(keyChar, newPinnerMap);
                    nowMap = newPinnerMap;
                }
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 检索内容是否包含敏感词
     */
    public static boolean contains(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, MAX_MATCH_TYPE);
            if (matchFlag > 0) return true;
        }
        return false;
    }

    /**
     * 检查敏感词数量
     */
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        Map nowMap = sensitiveWordMap;
        int matchFlag = 0;
        int actualMatchLength = 0; // 记录真正完整匹配到的长度
        for (int i = beginIndex; i < txt.length(); i++) {
            char word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    actualMatchLength = matchFlag; // 只有结尾了才算数
                    if (MIN_MATCH_TYPE == matchType) break;
                }
            } else {
                break;
            }
        }
        return actualMatchLength; // 返回真正匹配成功的长度
    }

    /**
     * 替换敏感词
     * @param content 待检测的文本
     * @param replaceChar 替换字符，例如 "*"
     * @return 替换后的文本
     */
    public String replaceSensitiveWord(String content, String replaceChar) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }
        StringBuilder result = new StringBuilder(content);

        for (int i = 0; i < result.length(); i++) {
            int length = checkSensitiveWord(result.toString(), i, 1);

            if (length > 0) {
                // 找到敏感词，创建对应长度的替换字符串
                String stars = getReplaceChars(replaceChar, length);
                // 替换从 i 到 i+length 的内容
                result.replace(i, i + length, stars);
                // 指针跳过已替换的词
                i = i + length - 1;
            }
        }

        return result.toString();
    }

    /**
     * 生成指定长度的替换符
     */
    private String getReplaceChars(String replaceChar, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(replaceChar);
        }
        return sb.toString();
    }
}