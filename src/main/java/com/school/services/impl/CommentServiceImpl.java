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

/**
 * 评论服务实现类
 * 实现评论相关的业务功能，包括敏感词过滤和评论结构组织
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private Util util;
    /**
     * 添加评论
     * 包含敏感词检测和过滤功能，自动识别并处理敏感内容
     * @param lostfound_id 失物招领信息ID
     * @param user_id 用户ID
     * @param content 评论内容
     * @param parent_id 父评论ID（0表示一级评论）
     * @param reply_user_id 被回复用户ID
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse addComment(int lostfound_id, int user_id, String content, int parent_id, int reply_user_id) {
        // 初始化评论状态变量
        int status;
        String finalContent = content;
        
        // 敏感词检测逻辑
        if (sensitiveWordUtil.contains(content)) {
            // 命中敏感词：直接驳回
            //finalContent = sensitiveWordUtil.replaceSensitiveWord(content, "*");
            status = 2; // 驳回状态
            commentMapper.addComment(lostfound_id, user_id, finalContent, status,parent_id,reply_user_id);
            return ServerResponse.createServerResponseByFail("内容包含敏感词，请修改后重试");
        } else {
            // 正常内容：直接发布 (status = 1)
            status = 1; // 已发布状态
        }
        
        // 调用数据访问层保存评论
        int row = commentMapper.addComment(lostfound_id, user_id, finalContent, status,parent_id,reply_user_id);
        
        // 根据保存结果返回相应响应
        if (row > 0) {
            // 根据状态构建不同的成功消息
            String msg = (status == 0) ? "评论已提交审核" : "评论发布成功";
            return ServerResponse.createServerResponseBySuccess(msg);
        }
        return ServerResponse.createServerResponseByFail("发布失败");
    }

    /**
     * 根据失物招领ID获取评论列表
     * 构建评论的树状结构，将一级评论和二级回复进行分组组织
     * @param lostfound_id 失物招领信息ID
     * @return ServerResponse 包含评论树结构的响应对象
     */
    @Override
    public ServerResponse getCommentsByLostFoundId(int lostfound_id) {
        // 从数据库获取指定失物招领信息的所有评论
        List<Comment> allComments = commentMapper.getCommentsByLostFoundId(lostfound_id);
        
        // 如果没有评论数据，返回空列表
        if (allComments == null || allComments.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(new ArrayList<>());
        }
        
        // 为所有评论设置完整的头像URL路径
        for (Comment comment : allComments) {
            comment.setPhoto(util.updatePic(comment.getPhoto()));
        }
        
        // 初始化数据结构：主评论列表和回复映射表
        List<Comment> mainComments = new ArrayList<>(); // 存放一楼的主评论
        Map<Integer, List<Comment>> replyMap = new HashMap<>(); // 用于存放各主评论的回复列表
        
        // 第一次遍历：区分主评论和回复评论
        for (Comment comment : allComments) {
            if (comment.getParentId() == 0) {
                // 如果为 0，说明它是独立的一楼主评论
                comment.setReplies(new ArrayList<>()); // 初始化回复列表
                mainComments.add(comment);
            } else {
                // 否则它是二楼回复，根据 parentId 放进对应的分组里
                Integer parentId = comment.getParentId();
                // 使用computeIfAbsent确保父评论ID对应的列表存在
                replyMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(comment);
            }
        }
        
        // 第二次遍历：将分组好的子评论塞入对应的主评论里
        for (Comment mainComment : mainComments) {
            List<Comment> replies = replyMap.get(mainComment.getId());
            if (replies != null) {
                // 将该主评论的所有回复设置到其replies属性中
                mainComment.setReplies(replies);
            }
        }

        // 返回构建好的评论树结构
        return ServerResponse.createServerResponseBySuccess(mainComments);

    }

    /**
     * 获取某用户收到的所有评论/回复
     * 用于用户消息中心展示收到的评论通知
     * @param userId 用户ID
     * @return ServerResponse 包含用户收到的评论列表
     */
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

        // 直接返回平铺的列表给前端 RecyclerView 渲染即可。
        return ServerResponse.createServerResponseBySuccess(receivedComments);
    }
}
