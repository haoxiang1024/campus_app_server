package com.school.mapper;

import com.school.entity.LostFoundType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface LostFoundTypeMapper {

    List<LostFoundType> GetAll();


    List<String> getAllType();


    int typeIdByName(@Param("name") String name);


    List<LostFoundType> selectTypeByPage(@Param("keyword") String keyword);


    int insertType(LostFoundType type);


    int updateType(LostFoundType type);


    int deleteTypeById(@Param("id") Integer id);


    int deleteTypeBatch(@Param("ids") List<Integer> ids);


}
