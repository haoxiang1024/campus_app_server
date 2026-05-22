package com.school.mapper;

import com.school.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface CommentMapper {

    @Insert("INSERT INTO comment (lostfound_id, user_id, content, state, create_time,parent_id,reply_user_id) " +
            "VALUES (#{lostfound_id}, #{user_id}, #{content}, #{state}, NOW(),#{parent_id},#{reply_user_id})")
    int addComment(@Param("lostfound_id") int lostfound_id,
                   @Param("user_id") int user_id,
                   @Param("content") String content,
                   @Param("state") int state,
                   @Param("parent_id") int parent_id,
                   @Param("reply_user_id") int reply_user_id);

    @Select("SELECT " +
            "c.id, c.lostfound_id, c.user_id, c.content, c.state, c.create_time, " +
            "c.parent_id, " +
            "c.reply_user_id, " +
            "u1.nickname AS nickname, " +
            "u1.photo AS photo, " +
            "u2.nickname AS reply_nickname " +
            "FROM comment c " +
            "LEFT JOIN user u1 ON c.user_id = u1.id " +
            "LEFT JOIN user u2 ON c.reply_user_id = u2.id " +
            "WHERE c.lostfound_id = #{lostfound_id}  and c.state = 1 " +
            "ORDER BY c.create_time ASC")
    List<Comment> getCommentsByLostFoundId(int lostfound_id);


    List<Comment> getReceivedComments(@Param("userId") int userId);


    @Select("SELECT c.id, c.lostfound_id, c.user_id, c.content, c.state, c.parent_id AS parentId, c.reply_user_id AS replyUserId, c.create_time, u.nickname, u.photo " +
            "FROM comment c LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.state = 1 AND c.user_id = #{user_id} " +
            "ORDER BY c.create_time DESC")
    List<Comment> getComments(@Param("user_id") int userId);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment getCommentById(@Param("id") int id);

}
