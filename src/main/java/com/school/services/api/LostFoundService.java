package com.school.services.api;

import com.school.utils.ServerResponse;
import org.apache.ibatis.annotations.Param;


/**
 * 失物招领服务接口
 * 提供失物招领信息管理相关功能
 */
public interface LostFoundService {
    /**
     * 获取物品的详细信息
     * @param lostfoundtypeId 物品分类ID
     * @param type 信息类型（失物/招领）
     * @return ServerResponse 包含物品详细信息列表的响应对象
     */
    ServerResponse getLostFoundDetail(int lostfoundtypeId,String type);

    /**
     * 根据用户ID获取用户名
     * @param id 用户ID
     * @return ServerResponse 包含用户名的响应对象
     */
    ServerResponse getUserName(int id);

    /**
     * 添加失物招领信息（JSON格式）
     * @param lostfoundtJson 失物招领信息JSON字符串
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse addLostFound(String lostfoundtJson);

    /**
     * 根据用户ID获取用户发布的失物招领信息
     * @param user_id 用户ID
     * @return ServerResponse 包含用户发布信息列表的响应对象
     */
    ServerResponse getLostFoundByUserId(int user_id);

    /**
     * 用户自己修改信息发布状态
     * @param id 信息ID
     * @param state 目标状态
     * @param user_id 用户ID
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse updateState(int id,String state,int user_id);

    /**
     * 显示置顶信息列表
     * @param stick 置顶标识（1表示置顶）
     * @return ServerResponse 包含置顶信息列表的响应对象
     */
    ServerResponse showTopList(@Param("stick")int stick);
    
    /**
     * 根据关键字模糊查询失物招领信息
     * @param keyword 搜索关键字
     * @return ServerResponse 包含匹配信息列表的响应对象
     */
    ServerResponse getInfoByKey(String keyword);
    
    /**
     * 根据ID获取失物招领信息详情
     * @param id 信息ID
     * @return ServerResponse 包含信息详情的响应对象
     */
    ServerResponse getLostFoundById(Integer id);
    
    /**
     * 根据ID删除失物招领信息
     * @param id 信息ID
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse deleteLostFoundById(Integer id);
    
    /**
     * 管理员审核失物招领信息（修改状态）
     * @param id 信息ID
     * @param state 目标状态
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse updateLostFoundStatus(Integer id, String state);
    
    /**
     * 分页查询失物招领信息列表
     * @param page 页码
     * @param size 每页大小
     * @return ServerResponse 包含信息列表和分页信息的响应对象
     */
    ServerResponse getLostListByPage(int page, int size);
    
    /**
     * 根据分类标题获取该分类下的信息
     * @param title 分类标题
     * @param type 信息类型
     * @return ServerResponse 包含分类信息列表的响应对象
     */
    ServerResponse getDetailByTitle(String title,String type);
}
