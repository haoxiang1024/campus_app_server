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
     * 假设前端传参格式：{ "userId": 1, "content": "今天天气真好！" }
     */
    @PostMapping("/add")
    public ServerResponse addMessage(@RequestBody java.util.Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        String content = (String) params.get("content");
        // 注意：实际项目中 userId 最好从 token 中解析获取，防止伪造
        return messageService.addMessage(userId, content);
    }

    /**
     * 获取留言列表
     */
    @GetMapping("/list")
    public ServerResponse getMessageList() {
        return messageService.getMessageList();
    }
}
