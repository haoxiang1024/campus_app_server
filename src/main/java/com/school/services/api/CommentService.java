package com.school.services.api;

import com.school.entity.Comment;
import com.school.utils.ServerResponse;

public interface CommentService {
    //发布评论
    ServerResponse addComment(int lostfound_id,int user_id,String content, int parent_id, int reply_user_id);
    //获取评论
    ServerResponse getCommentsByLostFoundId(int lostfound_id);
    // 获取某用户收到的所有评论/回复
    ServerResponse getReceivedComments(int userId);

}
