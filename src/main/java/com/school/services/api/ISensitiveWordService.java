package com.school.services.api;



import java.util.Map;

public interface ISensitiveWordService {

    Map<String, Object> getSensitiveWordList(int page, int pageSize, String keyword);


    boolean addSensitiveWord(String word);


    boolean deleteSensitiveWord(String word);


    boolean batchDeleteSensitiveWords(String words);
}
