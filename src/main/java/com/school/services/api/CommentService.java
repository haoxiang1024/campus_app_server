package com.school.services.api;

import com.school.utils.ServerResponse;


public interface CommentService {

    ServerResponse addComment(int lostfound_id,int user_id,String content, int parent_id, int reply_user_id);
    

    ServerResponse getCommentsByLostFoundId(int lostfound_id);
    

    ServerResponse getReceivedComments(int userId);

    ServerResponse getComments(int userId);

    ServerResponse deleteCommentById(Integer commentId);
}
