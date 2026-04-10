package com.school.services.impl;


import com.school.config.SensitiveWordLoader;
import com.school.services.api.ISensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensitiveWordServiceImpl implements ISensitiveWordService {

    @Autowired
    private SensitiveWordLoader sensitiveWordLoader;

    private final String FILE_PATH = System.getProperty("user.dir") + File.separator + "upload" + File.separator + "sensitive_words.txt";

    // 读取所有敏感词
    private synchronized List<String> readAllWords() {
        List<String> words = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return words;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    words.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    // 写入敏感词到文件
    private synchronized void writeAllWords(List<String> words) {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            for (String word : words) {
                bw.write(word);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getSensitiveWordList(int page, int pageSize, String keyword) {
        List<String> allWords = readAllWords();

        if (keyword != null && !keyword.trim().isEmpty()) {
            allWords = allWords.stream()
                    .filter(w -> w.contains(keyword.trim()))
                    .collect(Collectors.toList());
        }

        int total = allWords.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<Map<String, String>> list = new ArrayList<>();
        if (fromIndex < total) {
            List<String> pageWords = allWords.subList(fromIndex, toIndex);
            for (String w : pageWords) {
                Map<String, String> map = new HashMap<>();
                map.put("word", w);
                list.add(map);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("totalPages", totalPages);
        return result;
    }

    @Override
    public boolean addSensitiveWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        List<String> words = readAllWords();
        if (words.contains(word.trim())) {
            return false;
        }
        words.add(word.trim());
        writeAllWords(words);
        sensitiveWordLoader.refreshWordCount();
        return true;
    }

    @Override
    public boolean deleteSensitiveWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        List<String> words = readAllWords();
        boolean removed = words.remove(word.trim());
        if (removed) {
            writeAllWords(words);
            sensitiveWordLoader.refreshWordCount();
        }
        return removed;
    }

    @Override
    public boolean batchDeleteSensitiveWords(String words) {
        if (words == null || words.trim().isEmpty()) {
            return false;
        }
        List<String> wordList = readAllWords();
        Set<String> toDelete = new HashSet<>(Arrays.asList(words.split(",")));

        boolean removed = wordList.removeIf(w -> toDelete.contains(w.trim()));
        if (removed) {
            writeAllWords(wordList);
            sensitiveWordLoader.refreshWordCount();
        }
        return removed;
    }
}
