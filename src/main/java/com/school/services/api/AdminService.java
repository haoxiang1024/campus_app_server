package com.school.services.api;

import com.school.utils.ServerResponse;

/**
 * 管理员服务接口
 * 提供管理员后台管理相关功能
 */
public interface AdminService {
    /**
     * 获取所有用户数量
     * @return ServerResponse 包含用户总数的响应对象
     */
    ServerResponse getAllUser();
    
    /**
     * 获取失物招领信息数量
     * @param type 信息类型（失物/招领）
     * @return ServerResponse 包含信息数量的响应对象
     */
    ServerResponse getAllLostFoundCount(String type);
    
    /**
     * 分页获取所有用户详细信息
     * @param page 页码
     * @param size 每页大小
     * @return ServerResponse 包含用户列表和分页信息的响应对象
     */
    ServerResponse getAllUserInfo(int page,int size);
    
    /**
     * 模糊查询用户信息
     * @param keyword 搜索关键字
     * @return ServerResponse 包含匹配用户列表的响应对象
     */
    ServerResponse searchUsers(String keyword);
    
    /**
     * 批量重置用户密码
     * @param ids 用户ID字符串，多个ID用逗号分隔
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse resetPassword(String ids);
    
    /**
     * 分页查询失物招领信息
     * @param page 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @param type 信息类型
     * @param state 信息状态
     * @return ServerResponse 包含信息列表和分页信息的响应对象
     */
    ServerResponse getLostFoundByPage(int page, int pageSize, String keyword, String type, String state);
    
    /**
     * 更新信息置顶状态
     * @param id 信息ID
     * @param stick 置顶状态（1-置顶，0-取消置顶）
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse updateStickStatus(int id,int stick);

    /**
     * 分页获取评论列表
     */
    ServerResponse getCommentsByPage(int page, int pageSize, String keyword, String state);

    /**
     * 更新评论状态
     */
    ServerResponse updateCommentStatus(Integer commentId, int state, String reason);

    /**
     * 删除评论
     */
    ServerResponse deleteCommentById(Integer commentId);
}

