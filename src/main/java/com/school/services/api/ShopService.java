package com.school.services.api;

import com.school.entity.ExchangeOrder;
import com.school.entity.PointHistory;
import com.school.entity.ShopItem;
import com.school.utils.ServerResponse;
import java.util.List;

public interface ShopService {
    ServerResponse getActiveShopItems();
    ServerResponse exchangeItem(Integer userId, Integer itemId);
    ServerResponse getPointHistory(Integer userId);
    ServerResponse verifyOrder(String verifyCode, Integer adminId);
    ServerResponse getMyOrders(Integer userId, String keyword);
    ServerResponse deleteOrder(Integer id, Integer userId);
}