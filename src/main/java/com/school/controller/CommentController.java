package com.school.controller;

import com.school.services.api.CommentService;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 评论控制器类
 * 处理评论相关的请求，包括添加评论、获取评论列表等功能
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 添加评论接口
     * @param lostfound_id 失物招领ID
     * @param user_id 用户ID
     * @param content 评论内容
     * @param parent_id 父评论ID（用于回复功能）
     * @param reply_user_id 被回复用户ID
     * @return 返回ServerResponse对象，包含添加评论的结果信息
     */
    @ResponseBody
    @RequestMapping("/addComment")
    public ServerResponse addComment(@RequestParam("lostfound_id") int lostfound_id,
                                     @RequestParam("user_id") int user_id,
                                     @RequestParam("content") String content,
                                     @RequestParam("parent_id") int parent_id,
                                     @RequestParam("reply_user_id") int reply_user_id)
    {

        return commentService.addComment(lostfound_id,user_id,content, parent_id,  reply_user_id);
    }

    /**
     * 根据失物招领ID获取评论列表
     * @param lostfound_id 失物招领ID
     * @return 返回ServerResponse对象，包含该失物招领的所有评论信息
     */
    @ResponseBody
    @RequestMapping("/getCommentsByLostFoundId")
    public ServerResponse getCommentsByLostFoundId(@RequestParam("lostfound_id") int lostfound_id)
    {

        return commentService.getCommentsByLostFoundId(lostfound_id);
    }

    /**
     * 获取用户收到的评论列表
     * @param user_id 用户ID
     * @return 返回ServerResponse对象，包含该用户收到的所有评论信息
     */
    @ResponseBody
    @RequestMapping("/getReceivedComments")
    public ServerResponse getReceivedComments(@RequestParam("user_id") int user_id) {
        return commentService.getReceivedComments(user_id);
    }

}
