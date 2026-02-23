package com.school.services.api;

import com.school.entity.Comment;
import com.school.utils.ServerResponse;

public interface CommentService {
    ServerResponse addComment(int lostfound_id,int user_id,String content);
}
