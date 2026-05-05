package com.school.controller;

import com.school.services.api.MessageService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 发布留言
     *
     */
    @PostMapping("/add")
    public ServerResponse addMessage(@RequestBody java.util.Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        String content = (String) params.get("content");
        Integer parentId = (Integer) params.get("parentId");
        Integer replyUserId = (Integer) params.get("replyUserId");

        return messageService.addMessage(userId, content, parentId, replyUserId);
    }

    /**
     * 获取留言列表
     */
    @GetMapping("/list")
    public ServerResponse getMessageList() {
        return messageService.getMessageList();
    }

    /**
     * 根据用户ID获取留言列表
     *
     */
    @GetMapping("/userList")
    public ServerResponse getMessagesByUserId(@RequestParam("userId") Integer userId) {
        return messageService.getMessagesByUserId(userId);
    }

    /**
     * 删除留言
     *
     */
    @ResponseBody
    @PostMapping("/deleteMessage")
    public ServerResponse deleteMessage(@RequestParam("id") Integer id) {
        return messageService.deleteComment(id);
    }
}
