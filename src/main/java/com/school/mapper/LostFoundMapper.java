package com.school.mapper;

import com.school.entity.LostFound;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface LostFoundMapper {
    //获取一个分类下的物品信息
    List<LostFound> selectByTypeId(@Param("lostfoundtypeId") int lostfoundtypeId);

    //根据userid获取用户名称
    String searchUserNameId(@Param("id") int id);

    //添加物品信息
    boolean addLostFound(@Param("lostFound") LostFound lostFound);

    //根据用户id获取用户发布的信息
    List<LostFound> getLostFoundListById(@Param("user_id") int user_id);

    //修改状态
    boolean updateState(@Param("id")int id,@Param("state")String state);

    //显示置顶信息
    List<LostFound> showTopList(@Param("stick")int stick);

    // 获取所有信息（支持搜索关键字）
    @Select("SELECT title,place,pub_date,state,phone,content FROM lostfound " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "   OR title LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR content LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR place LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY pub_date DESC") // 建议加上按发布时间降序排序
    List<LostFound> selectAllInfo(@Param("keyword") String keyword);
    // 根据ID删除
    @Delete("DELETE FROM lostfound WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);
    // 根据ID查询详情
    @Select("select * from lostfound where id=#{id}")
    LostFound selectByPrimaryKey(Integer id);
    //分页查询所有信息
    @Select("SELECT * FROM lostfound ORDER BY pub_date DESC")
    List<LostFound> selectByPage();
}
