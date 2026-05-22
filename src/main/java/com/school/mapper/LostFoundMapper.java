package com.school.mapper;

import com.school.entity.LostFound;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;



@Mapper
public interface LostFoundMapper {

    List<LostFound> selectByTypeId(@Param("lostfoundtypeId") int lostfoundtypeId,@Param("type") String type);


    String searchUserNameId(@Param("id") int id);


    boolean addLostFound(@Param("lostFound") LostFound lostFound);


    List<LostFound> getLostFoundListById(@Param("user_id") int user_id);


    boolean updateState(@Param("id")int id,@Param("state")String state);


    List<LostFound> showTopList(@Param("stick")Integer stick);


    @Select("SELECT title,place,pub_date,state,phone,content FROM lostfound " +
            "WHERE (#{keyword} IS NULL OR #{keyword} = '' " +
            "   OR title LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR content LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR place LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY pub_date DESC")
    List<LostFound> selectAllInfo(@Param("keyword") String keyword);

    @Delete("DELETE FROM lostfound WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);

    @Select("select * from lostfound where id=#{id}")
    LostFound selectByPrimaryKey(Integer id);

    @Select("SELECT * FROM lostfound ORDER BY pub_date DESC")
    List<LostFound> selectByPage();


    List<LostFound>smartMatch(@Param("type")String type,@Param("lostfoundtype_id")int lostfoundtype_id,@Param("keyword")String keyword);


    int countByTypeId(@Param("typeId") Integer typeId);
}
