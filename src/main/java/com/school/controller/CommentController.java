package com.school.controller;

import com.school.services.api.CommentService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;


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


    @ResponseBody
    @RequestMapping("/getCommentsByLostFoundId")
    public ServerResponse getCommentsByLostFoundId(@RequestParam("lostfound_id") int lostfound_id)
    {

        return commentService.getCommentsByLostFoundId(lostfound_id);
    }


    @ResponseBody
    @RequestMapping("/getReceivedComments")
    public ServerResponse getReceivedComments(@RequestParam("user_id") int user_id) {
        return commentService.getReceivedComments(user_id);
    }


    @ResponseBody
    @RequestMapping("/getComments")
    public ServerResponse getComments(@RequestParam("user_id") int user_id) {
        return commentService.getComments(user_id);
    }
    @ResponseBody
    @RequestMapping("/delComment")
    public ServerResponse delComment(@RequestParam("commentId") Integer commentId) {
        return commentService.deleteCommentById(commentId);
    }

}
