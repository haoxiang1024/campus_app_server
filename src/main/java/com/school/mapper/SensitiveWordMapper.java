package com.school.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 敏感词数据访问接口
 * 提供敏感词相关的数据库操作方法
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
@Mapper
public interface SensitiveWordMapper {
    /**
     * 获取所有敏感词
     * @return 敏感词列表
     */
    @Select("SELECT word FROM sensitive_word")
    List<String> getWords();
}
