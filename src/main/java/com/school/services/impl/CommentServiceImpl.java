package com.school.services.impl;

import com.school.entity.Comment;
import com.school.mapper.CommentMapper;
import com.school.services.api.CommentService;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private Util util;
    @Override
    public ServerResponse addComment(int lostfound_id, int user_id, String content, int parent_id, int reply_user_id) {
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
        int row = commentMapper.addComment(lostfound_id, user_id, finalContent, status,parent_id,reply_user_id);
        if (row > 0) {
            String msg = (status == 0) ? "评论已提交审核" : "评论发布成功";
            return ServerResponse.createServerResponseBySuccess(msg);
        }
        return ServerResponse.createServerResponseByFail("发布失败");
    }

    @Override
    public ServerResponse getCommentsByLostFoundId(int lostfound_id) {
        List<Comment> allComments = commentMapper.getCommentsByLostFoundId(lostfound_id);
        if (allComments == null || allComments.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(new ArrayList<>());
        }
        //设置头像
        for (Comment comment : allComments) {
            comment.setPhoto(util.updatePic(comment.getPhoto()));
        }
        List<Comment> mainComments = new ArrayList<>(); // 存放一楼的主评论
        Map<Integer, List<Comment>> replyMap = new HashMap<>();
        for (Comment comment : allComments) {
            if (comment.getParentId() == 0) {
                // 如果为 0，说明它是独立的一楼
                comment.setReplies(new ArrayList<>());
                mainComments.add(comment);
            } else {
                // 否则它是二楼回复，根据 parentId 放进对应的分组里
                Integer parentId = comment.getParentId();
                replyMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(comment);
            }
        }
        //  将分组好的子评论塞入对应的主评论里
        for (Comment mainComment : mainComments) {
            List<Comment> replies = replyMap.get(mainComment.getId());
            if (replies != null) {
                mainComment.setReplies(replies);
            }
        }

        return ServerResponse.createServerResponseBySuccess(mainComments);

    }

    @Override
    public ServerResponse getReceivedComments(int userId) {
        // 查询该用户收到的所有评论 (按时间倒序)
        List<Comment> receivedComments = commentMapper.getReceivedComments(userId);

        if (receivedComments == null || receivedComments.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(new ArrayList<>());
        }

        // 补全头像链接
        for (Comment comment : receivedComments) {
            comment.setPhoto(util.updatePic(comment.getPhoto()));
        }

        // 因为是消息列表展示，通常不需要像详情页那样组装成父子树状结构，
        // 直接返回平铺的列表给前端 RecyclerView 渲染即可。
        return ServerResponse.createServerResponseBySuccess(receivedComments);
    }
}
