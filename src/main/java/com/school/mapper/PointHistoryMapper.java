package com.school.mapper;

import com.school.entity.PointHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface PointHistoryMapper {
    int insert(PointHistory record);
    List<PointHistory> selectByUserId(Integer userId);

    List<PointHistory> selectAllWithSearch(@Param("keyword") String keyword, @Param("type") Integer type);


    int deleteById(@Param("id") Integer id);
}