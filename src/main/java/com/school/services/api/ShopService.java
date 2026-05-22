package com.school.services.api;

import com.school.entity.ShopItem;
import com.school.utils.ServerResponse;

public interface ShopService {

    ServerResponse getActiveShopItems();

    ServerResponse deleteOrderById(Integer id);

    ServerResponse exchangeItem(Integer userId, Integer itemId);


    ServerResponse getPointHistory(Integer userId);


    ServerResponse verifyOrder(String verifyCode, Integer adminId);


    ServerResponse getMyOrders(Integer userId, String keyword);


    ServerResponse deleteOrder(Integer id, Integer userId);


    ServerResponse getAllItems(int page, int pageSize, String keyword, Integer status);

    ServerResponse saveOrUpdateItem(ShopItem item);




    ServerResponse getAllOrders(int page, int pageSize, String keyword, Integer status);


    ServerResponse updateStatus(Integer id, Integer status);


    ServerResponse getAllPointHistories(int page, int pageSize, String keyword, Integer type);


    ServerResponse deletePointHistory(Integer id);
}