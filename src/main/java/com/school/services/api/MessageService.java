package com.school.services.api;

import com.school.utils.ServerResponse;

public interface MessageService {
    /**
     * 发布留言
     * @param userId 用户ID
     * @param content 留言内容
     * @return 操作结果
     */
    ServerResponse addMessage(Integer userId, String content, Integer parentId, Integer replyUserId);
    /**
     * 获取留言板列表
     * @return 留言列表结果
     */
    ServerResponse getMessageList();
    /**
     * 分页获取留言列表 (后台管理使用，支持多条件检索)
     * @param page 当前页码
     * @param pageSize 每页显示数量
     * @param keyword 搜索关键字 (匹配内容或发布人昵称)
     * @param state 留言状态 (0:待审核, 1:已通过, 2:已驳回)
     * @return 包含分页信息和留言列表的操作结果
     */
    ServerResponse getAdminMessagePage(int page, int pageSize, String keyword, Integer state);

    /**
     * 更新留言状态 (审核通过/驳回)
     * @param commentId 留言ID
     * @param state 目标状态 (1:已通过, 2:已驳回)
     * @param reason 驳回原因 (当状态为驳回时填写)
     * @return 操作结果
     */
    ServerResponse updateCommentStatus(Integer commentId, Integer state, String reason);

    /**
     * 根据ID物理删除留言
     * @param commentId 留言ID
     * @return 操作结果
     */
    ServerResponse deleteCommentById(Integer commentId);

    /**
     * 根据用户ID获取留言列表
     * @param userId 用户ID
     * @return 留言列表结果
     */
    ServerResponse getMessagesByUserId(Integer userId);
}
