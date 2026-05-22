package com.school.utils;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Service
public class SensitiveWordUtil {

    private static Map<Object, Object> sensitiveWordMap;

    public static final int MIN_MATCH_TYPE = 1;

    public static final int MAX_MATCH_TYPE = 2;


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


    public static boolean contains(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, MAX_MATCH_TYPE);
            if (matchFlag > 0) return true;
        }
        return false;
    }


    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        Map nowMap = sensitiveWordMap;
        int matchFlag = 0;
        int actualMatchLength = 0;
        for (int i = beginIndex; i < txt.length(); i++) {
            char word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    actualMatchLength = matchFlag;
                    if (MIN_MATCH_TYPE == matchType) break;
                }
            } else {
                break;
            }
        }
        return actualMatchLength;
    }


    public String replaceSensitiveWord(String content, String replaceChar) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }
        StringBuilder result = new StringBuilder(content);

        for (int i = 0; i < result.length(); i++) {
            int length = checkSensitiveWord(result.toString(), i, 1);

            if (length > 0) {

                String stars = getReplaceChars(replaceChar, length);

                result.replace(i, i + length, stars);

                i = i + length - 1;
            }
        }

        return result.toString();
    }


    private String getReplaceChars(String replaceChar, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(replaceChar);
        }
        return sb.toString();
    }
}