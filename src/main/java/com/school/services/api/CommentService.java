package com.school.services.api;

import com.school.entity.Comment;
import com.school.utils.ServerResponse;

/**
 * 评论服务接口
 * 提供评论相关的业务功能
 */
public interface CommentService {
    /**
     * 添加评论
     * @param lostfound_id 失物招领信息ID
     * @param user_id 用户ID
     * @param content 评论内容
     * @param parent_id 父评论ID（0表示一级评论）
     * @param reply_user_id 被回复用户ID
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse addComment(int lostfound_id,int user_id,String content, int parent_id, int reply_user_id);
    
    /**
     * 根据失物招领ID获取评论列表
     * @param lostfound_id 失物招领信息ID
     * @return ServerResponse 包含评论树结构的响应对象
     */
    ServerResponse getCommentsByLostFoundId(int lostfound_id);
    
    /**
     * 获取某用户收到的所有评论/回复
     * @param userId 用户ID
     * @return ServerResponse 包含用户收到的评论列表
     */
    ServerResponse getReceivedComments(int userId);

}
