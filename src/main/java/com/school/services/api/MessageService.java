package com.school.services.api;

import com.school.utils.ServerResponse;

public interface MessageService {
    /**
     * 发布留言
     * @param userId 用户ID
     * @param content 留言内容
     * @return 操作结果
     */
    ServerResponse addMessage(Integer userId, String content, Integer parentId);

    /**
     * 获取留言板列表
     * @return 留言列表结果
     */
    ServerResponse getMessageList();
}
