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


    @PostMapping("/add")
    public ServerResponse addMessage(@RequestBody java.util.Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        String content = (String) params.get("content");
        Integer parentId = (Integer) params.get("parentId");
        Integer replyUserId = (Integer) params.get("replyUserId");

        return messageService.addMessage(userId, content, parentId, replyUserId);
    }


    @GetMapping("/list")
    public ServerResponse getMessageList() {
        return messageService.getMessageList();
    }


    @GetMapping("/userList")
    public ServerResponse getMessagesByUserId(@RequestParam("userId") Integer userId) {
        return messageService.getMessagesByUserId(userId);
    }


    @ResponseBody
    @PostMapping("/deleteMessage")
    public ServerResponse deleteMessage(@RequestParam("id") Integer id) {
        return messageService.deleteComment(id);
    }
}
