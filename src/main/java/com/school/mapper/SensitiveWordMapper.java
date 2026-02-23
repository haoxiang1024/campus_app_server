package com.school.mapper;

import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SensitiveWordMapper {
    //获取所以敏感词
    @Select("SELECT word FROM sensitive_word")
    List<String> getWords();
}
