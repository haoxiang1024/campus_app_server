package com.school.mapper;

import com.school.entity.LostFound;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 失物招领数据访问接口
 * 提供失物招领信息的增删改查操作
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
@Mapper
public interface LostFoundMapper {
    /**
     * 根据类型ID和类型获取失物招领信息列表
     * @param lostfoundtypeId 失物招领类型ID
     * @param type 类型标识（lost/found）
     * @return 失物招领信息列表
     */
    List<LostFound> selectByTypeId(@Param("lostfoundtypeId") int lostfoundtypeId,@Param("type") String type);

    /**
     * 根据用户ID获取用户名
     * @param id 用户ID
     * @return 用户名
     */
    String searchUserNameId(@Param("id") int id);

    /**
     * 添加新的失物招领信息
     * @param lostFound 失物招领对象
     * @return 添加成功返回true，失败返回false
     */
    boolean addLostFound(@Param("lostFound") LostFound lostFound);

    /**
     * 根据用户ID获取该用户发布的失物招领信息
     * @param user_id 用户ID
     * @return 失物招领信息列表
     */
    List<LostFound> getLostFoundListById(@Param("user_id") int user_id);

    /**
     * 更新失物招领信息状态
     * @param id 失物招领ID
     * @param state 新状态
     * @return 更新成功返回true，失败返回false
     */
    boolean updateState(@Param("id")int id,@Param("state")String state);

    /**
     * 显示置顶的失物招领信息
     * @param stick 置顶标识（1为置顶）
     * @return 置顶的失物招领信息列表
     */
    List<LostFound> showTopList(@Param("stick")int stick);

    /**
     * 获取所有失物招领信息（支持关键字搜索）
     * 搜索字段包括：标题、内容、地点、电话
     * @param keyword 搜索关键字
     * @return 失物招领信息列表，按发布时间降序排列
     */
    @Select("SELECT title,place,pub_date,state,phone,content FROM lostfound " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "   OR title LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR content LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR place LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY pub_date DESC") // 建议加上按发布时间降序排序
    List<LostFound> selectAllInfo(@Param("keyword") String keyword);
    /**
     * 根据ID删除失物招领信息
     * @param id 失物招领ID
     * @return 删除成功的记录数
     */
    @Delete("DELETE FROM lostfound WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);
    /**
     * 根据ID查询失物招领详细信息
     * @param id 失物招领ID
     * @return 失物招领对象
     */
    @Select("select * from lostfound where id=#{id}")
    LostFound selectByPrimaryKey(Integer id);
    /**
     * 分页查询所有失物招领信息
     * @return 失物招领信息列表，按发布时间降序排列
     */
    @Select("SELECT * FROM lostfound ORDER BY pub_date DESC")
    List<LostFound> selectByPage();
}
