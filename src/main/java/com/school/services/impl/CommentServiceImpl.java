package com.school.services.impl;

import com.school.entity.Comment;
import com.school.entity.PointHistory;
import com.school.mapper.CommentMapper;
import com.school.mapper.PointHistoryMapper;
import com.school.mapper.UserMapper;
import com.school.services.api.CommentService;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse addComment(int lostfound_id, int user_id, String content, int parent_id, int reply_user_id) {
        int status = 1;

        // 敏感词检测与处罚
        if (sensitiveWordUtil.contains(content)) {
            status = 2;
            commentMapper.addComment(lostfound_id, user_id, content, status, parent_id, reply_user_id);
            userMapper.deductPoints(user_id, 50);

            PointHistory history = new PointHistory();
            history.setUser_id(user_id);
            history.setType(4);
            history.setPoints_changed(-50);
            history.setDescription("发布违规评论");
            pointHistoryMapper.insert(history);

            return ServerResponse.createServerResponseByFail("评论包含敏感词，扣除50积分，请重新发布内容");
        }

        // 写入评论数据
        int row = commentMapper.addComment(lostfound_id, user_id, content, status, parent_id, reply_user_id);
        if (row > 0) {
            return ServerResponse.createServerResponseBySuccess(status == 0 ? "评论已提交审核" : "评论发布成功");
        }
        return ServerResponse.createServerResponseByFail("发布失败");
    }

    @Override
    public ServerResponse getCommentsByLostFoundId(int lostfound_id) {
        List<Comment> allComments = commentMapper.getCommentsByLostFoundId(lostfound_id);
        if (allComments == null || allComments.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(new ArrayList<>());
        }

        Map<Integer, Comment> commentMap = new HashMap<>();
        List<Comment> rootComments = new ArrayList<>();

        // 预处理：更新图片路径、初始化回复列表、构建ID映射表
        for (Comment comment : allComments) {
            comment.setPhoto(util.updatePic(comment.getPhoto()));
            comment.setReplies(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
        }

        // 构建多级树状结构
        for (Comment comment : allComments) {
            if (comment.getParent_id() == 0) {
                rootComments.add(comment);
            } else {
                Comment parent = commentMap.get(comment.getParent_id());
                if (parent != null) {
                    parent.getReplies().add(comment);
                }
            }
        }

        return ServerResponse.createServerResponseBySuccess(rootComments);
    }

    @Override
    public ServerResponse getReceivedComments(int userId) {
        List<Comment> receivedComments = commentMapper.getReceivedComments(userId);
        if (receivedComments != null && !receivedComments.isEmpty()) {
            for (Comment comment : receivedComments) {
                comment.setPhoto(util.updatePic(comment.getPhoto()));
            }
            return ServerResponse.createServerResponseBySuccess(receivedComments);
        }
        return ServerResponse.createServerResponseBySuccess(new ArrayList<>());
    }

    @Override
    public ServerResponse getComments(int userId) {
        List<Comment> comments = commentMapper.getComments(userId);
        if (comments != null && !comments.isEmpty()) {
            for (Comment comment : comments) {
                comment.setPhoto(util.updatePic(comment.getPhoto()));
            }
            return ServerResponse.createServerResponseBySuccess(comments);
        }
        return ServerResponse.createServerResponseBySuccess(new ArrayList<>());
    }
}