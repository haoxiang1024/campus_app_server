package com.school.services.api;

import com.school.utils.ServerResponse;

public interface MessageService {

    ServerResponse addMessage(Integer userId, String content, Integer parentId, Integer replyUserId);

    ServerResponse getMessageList();

    ServerResponse getAdminMessagePage(int page, int pageSize, String keyword, Integer state);


    ServerResponse updateCommentStatus(Integer commentId, Integer state);


    ServerResponse deleteCommentById(Integer commentId);

    ServerResponse deleteComment(Integer commentId);

    ServerResponse getMessagesByUserId(Integer userId);
}
