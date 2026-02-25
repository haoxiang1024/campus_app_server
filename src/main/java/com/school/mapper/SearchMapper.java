package com.school.mapper;

import com.school.entity.SearchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 搜索数据访问接口
 * 提供搜索相关的数据库操作方法
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
@Mapper
public interface SearchMapper {
    /**
     * 根据搜索值查询相关信息
     * @param value 搜索关键字
     * @return 搜索结果列表
     */
    List<SearchInfo>
    searchInfoByValue(@Param("value")String value);


}
