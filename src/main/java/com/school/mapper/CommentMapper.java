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
     * @param parentId 父评论ID（用于回复功能）
     * @param replyUserId 被回复用户ID
     * @return 插入成功的记录数
     */
    @Insert("INSERT INTO comment (lostfound_id, user_id, content, state, create_time,parentId,replyUserId) " +
            "VALUES (#{lostfound_id}, #{user_id}, #{content}, #{state}, NOW(),#{parentId},#{replyUserId})")
    int addComment(@Param("lostfound_id") int lostfound_id,
                   @Param("user_id") int user_id,
                   @Param("content") String content,
                   @Param("state") int state,
                   @Param("parentId") int parentId,
                   @Param("replyUserId") int replyUserId);
    /**
     * 根据失物招领ID获取相关评论列表
     * 包含评论用户信息和被回复用户信息
     * @param lostfound_id 失物招领ID
     * @return 评论列表，按创建时间升序排列
     */
    @Select("SELECT " +
            "c.id, c.lostfound_id, c.user_id, c.content, c.state, c.create_time, " +
            "c.parentId, " +          // 数据库叫 parentId
            "c.replyUserId, " +       // 数据库叫 replyUserId
            "u1.nickname AS nickname, " +
            "u1.photo AS photo, " +
            "u2.nickname AS replyNickname " + // 从 u2 表取回被回复人的昵称
            "FROM comment c " +
            "LEFT JOIN user u1 ON c.user_id = u1.id " +
            "LEFT JOIN user u2 ON c.replyUserId = u2.id " + // 这里的字段必须和数据库一模一样
            "WHERE c.lostfound_id = #{lostfound_id} " +
            "ORDER BY c.create_time ASC")
    List<Comment> getCommentsByLostFoundId(int lostfound_id);

    /**
     * 查询用户收到的评论
     * @param userId 用户ID
     * @return 该用户收到的评论列表
     */
    List<Comment> getReceivedComments(@Param("userId") int userId);

}
