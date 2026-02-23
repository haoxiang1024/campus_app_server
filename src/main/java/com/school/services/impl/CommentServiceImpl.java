package com.school.services.impl;

import com.school.entity.Comment;
import com.school.mapper.CommentMapper;
import com.school.services.api.CommentService;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;

    @Override
    public ServerResponse addComment(int lostfound_id, int user_id, String content) {
        int status;
        String finalContent = content;
        if (sensitiveWordUtil.contains(content)) {
            // 命中敏感词：脱敏处理并设为待审核 (status = 0)
            finalContent = sensitiveWordUtil.replaceSensitiveWord(content, "*");
            status = 0;
        } else {
            // 正常内容：直接发布 (status = 1)
            status = 1;
        }
        int row = commentMapper.addComment(lostfound_id, user_id, finalContent, status);
        if (row > 0) {
            String msg = (status == 0) ? "评论已提交审核" : "评论发布成功";
            return ServerResponse.createServerResponseBySuccess(msg);
        }
        return ServerResponse.createServerResponseByFail("发布失败");
    }
}
