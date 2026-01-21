package com.school.mapper;

import com.school.entity.Lost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface LostMapper {

    List<Lost> selectByTypeId(@Param("lostfoundtypeId") int lostfoundtypeId);

    //根据丢失物品中的userid获取用户名称
    String searchUserNameId(@Param("id") int id);

    //添加丢失物品信息
    boolean addLost(@Param("lost") Lost lost);

    //根据用户id获取用户发布的信息
    List<Lost> getAllByIdLostList(@Param("user_id") int user_id);

    //修改状态
    boolean updateState(@Param("id")int id,@Param("state")String state);

    //显示置顶信息
    List<Lost>showLostList(@Param("stick")int stick);
    //添加数据用的
    //更新失物图片
    void updateImg(@Param("phone") String phone, @Param("user_id") int user_id, @Param("id") int id);

    void updateImgs(@Param("img") String img,@Param("id") int id);

    //添加信息
    void addBatch(@Param("user_id") int user_id);

    //所有信息
    List<Lost> getAllList();

    // 获取所有失物信息（支持搜索关键字）
    @Select("SELECT title,place,pub_date,state,phone,content FROM lost " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "   OR title LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR content LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR place LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY pub_date DESC") // 建议加上按发布时间降序排序
    List<Lost> selectAllLost(@Param("keyword") String keyword);
    // 根据ID删除
    @Delete("DELETE FROM lost WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);
    // 根据ID查询详情
    @Select("select * from lost where id=#{id}")
    Lost selectByPrimaryKey(Integer id);
    //分页查询所有信息
    @Select("SELECT * FROM lost ORDER BY pub_date DESC")
    List<Lost> selectLostByPage();
}
