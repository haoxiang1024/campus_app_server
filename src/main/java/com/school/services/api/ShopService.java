package com.school.services.api;

import com.school.entity.ExchangeOrder;
import com.school.entity.PointHistory;
import com.school.entity.ShopItem;
import com.school.utils.ServerResponse;
import java.util.List;

public interface ShopService {
    /**
     * 获取所有活跃的商城物品
     *
     * @return ServerResponse 包含活跃商城物品列表的响应对象
     */
    ServerResponse getActiveShopItems();
    /**
     * 管理员根据ID直接删除订单
     */
    ServerResponse deleteOrderById(Integer id);
    /**
     * 兑换物品
     *
     * @param userId 用户 ID
     * @param itemId 要兑换的物品 ID
     * @return ServerResponse 包含兑换结果的响应对象
     */
    ServerResponse exchangeItem(Integer userId, Integer itemId);

    /**
     * 获取用户的积分历史记录
     *
     * @param userId 用户 ID
     * @return ServerResponse 包含积分历史记录的响应对象
     */
    ServerResponse getPointHistory(Integer userId);

    /**
     * 验证订单
     *
     * @param verifyCode 订单验证码
     * @param adminId 管理员 ID
     * @return ServerResponse 包含订单验证结果的响应对象
     */
    ServerResponse verifyOrder(String verifyCode, Integer adminId);

    /**
     * 获取我的订单列表
     *
     * @param userId 用户 ID
     * @param keyword 搜索关键词（可为 null）
     * @return ServerResponse 包含订单列表的响应对象
     */
    ServerResponse getMyOrders(Integer userId, String keyword);

    /**
     * 删除订单
     *
     * @param id 订单 ID
     * @param userId 用户 ID（用于权限验证）
     * @return ServerResponse 包含删除结果的响应对象
     */
    ServerResponse deleteOrder(Integer id, Integer userId);

    /**
     * 分页获取所有商城物品，支持名称模糊查询
     *
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param keyword 搜索关键词（商品名称）
     * @return ServerResponse 包含分页数据的响应对象
     */
    ServerResponse getAllItems(int page, int pageSize, String keyword, Integer status);
    /**
     * 保存或更新商城物品
     *
     * @param item 要保存或更新的商城物品对象
     * @return ServerResponse 包含保存或更新结果的响应对象
     */
    ServerResponse saveOrUpdateItem(ShopItem item);



    /**
     * 获取所有订单（管理员功能），支持分页和条件查询
     *
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param keyword 搜索关键词（支持订单号、商品名、用户名等）
     * @param status 订单状态 (0-待核销, 1-已领取, 2-已取消) 可为空
     * @return ServerResponse 包含封装好的分页数据对象的响应
     */
    ServerResponse getAllOrders(int page, int pageSize, String keyword, Integer status);

    // 修改商品上架状态
    ServerResponse updateStatus(Integer id, Integer status);
}