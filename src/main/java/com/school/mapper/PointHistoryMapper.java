package com.school.mapper;

import com.school.entity.PointHistory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PointHistoryMapper {
    int insert(PointHistory record);
    List<PointHistory> selectByUserId(Integer userId);
}