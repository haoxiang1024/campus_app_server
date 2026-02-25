package com.school.mapper;

import com.school.entity.LostFoundType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 失物招领类型数据访问接口
 * 提供失物招领类型相关的数据库操作方法
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
@Mapper
public interface LostFoundTypeMapper {
    /**
     * 获取所有失物招领类型
     * @return 失物招领类型列表
     */
    List<LostFoundType> GetAll();

    /**
     * 获取所有类型名称
     * @return 类型名称列表
     */
    List<String> getAllType();

    /**
     * 根据类型名称获取类型ID
     * @param name 类型名称
     * @return 类型ID
     */
    int typeIdByName(@Param("name") String name);




}
