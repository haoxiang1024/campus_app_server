package com.school.mapper;

import com.school.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论数据访问接口
 * 提供评论相关的数据库操作方法，支持评论发布和查询功能
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
public interface CommentMapper {
    /**
     * 发布新评论
     * @param lostfound_id 关联的失物招领ID
     * @param user_id 评论用户ID
     * @param content 评论内容
     * @param state 评论状态
     * @param parent_id 父评论ID（用于回复功能）
     * @param reply_user_id 被回复用户ID
     * @return 插入成功的记录数
     */
    @Insert("INSERT INTO comment (lostfound_id, user_id, content, state, create_time,parent_id,reply_user_id) " +
            "VALUES (#{lostfound_id}, #{user_id}, #{content}, #{state}, NOW(),#{parent_id},#{reply_user_id})")
    int addComment(@Param("lostfound_id") int lostfound_id,
                   @Param("user_id") int user_id,
                   @Param("content") String content,
                   @Param("state") int state,
                   @Param("parent_id") int parent_id,
                   @Param("reply_user_id") int reply_user_id);
    /**
     * 根据失物招领ID获取相关评论列表
     * 包含评论用户信息和被回复用户信息
     * @param lostfound_id 失物招领ID
     * @return 评论列表，按创建时间升序排列
     */
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

    /**
     * 查询用户收到的评论
     * @param userId 用户ID
     * @return 该用户收到的评论列表
     */
    List<Comment> getReceivedComments(@Param("userId") int userId);

    /**
     * 根据用户ID查询该用户发表的所有评论
     * 
     * @param userId 用户ID
     * @return 该用户发表的评论列表
     */
    @Select("SELECT c.id, c.lostfound_id, c.user_id, c.content, c.state, c.parent_id AS parentId, c.reply_user_id AS replyUserId, c.create_time, u.nickname, u.photo " +
            "FROM comment c LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.state = 1 AND c.user_id = #{user_id} " +
            "ORDER BY c.create_time DESC")
    List<Comment> getComments(@Param("user_id") int userId);

}
