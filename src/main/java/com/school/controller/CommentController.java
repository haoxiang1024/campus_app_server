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

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @ResponseBody
    @RequestMapping("/addComment")
    public ServerResponse addComment(@RequestParam("lostfound_id") int lostfound_id,
                                     @RequestParam("user_id") int user_id,
                                     @RequestParam("content") String content)
    {

        return commentService.addComment(lostfound_id,user_id,content);
    }
}
