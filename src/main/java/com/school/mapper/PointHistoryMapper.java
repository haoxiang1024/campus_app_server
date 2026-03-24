package com.school.mapper;

import com.school.entity.PointHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface PointHistoryMapper {
    int insert(PointHistory record);
    List<PointHistory> selectByUserId(Integer userId);
    // 后台管理：多条件搜索与分页查询
    List<PointHistory> selectAllWithSearch(@Param("keyword") String keyword, @Param("type") Integer type);

    // 后台管理：根据ID删除流水
    int deleteById(@Param("id") Integer id);
}