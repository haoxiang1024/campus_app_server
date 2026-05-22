package com.school.mapper;

import com.school.entity.Comment;
import com.school.entity.CommentVO;
import com.school.entity.LostFound;
import com.school.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AdminMapper {

    List<User> getUserListByCondition(@Param("keyword") String keyword, @Param("state") Integer state,@Param("role")Integer  role);

    @Select("SELECT COUNT(*) FROM user")
    int getAllUserCount();

    int getAllLostFoundCount(@Param("type") String type);

    @Select("select id,nickname,photo,phone,sex,reg_date,email,state from user")
    List<User> getAllUserInfo();

    @Select("SELECT id,nickname,photo,phone,sex,reg_date,email FROM user WHERE " +
            "nickname LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%') OR " +
            "email LIKE CONCAT('%', #{keyword}, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);


    int batchResetPassword(@Param("idList") List<Integer> idList, @Param("password") String password);

    List<LostFound> getLostFoundByPage(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("state") String state
    );

    int getLostFoundCountByCondition(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("state") String state
    );

    @Update("update lostFound set stick=#{stick} where id = #{id}")
    boolean updateStickStatus(@Param("id")int id,@Param("stick")int stick);


    @Delete("DELETE FROM comment WHERE id = #{commentId}")
    int deleteCommentById(@Param("commentId") Integer commentId);


    List<CommentVO> getCommentsByCondition(@Param("keyword") String keyword, @Param("state") String state);


    int updateCommentSelective(Comment comment);
}
