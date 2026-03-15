package com.school.controller;

import com.school.entity.ExchangeOrder;
import com.school.services.api.ShopService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 获取可兑换商品列表
     */
    @GetMapping("/items")
    public ServerResponse getItems() {
        return shopService.getActiveShopItems();
    }

    /**
     * 发起积分兑换
     */
    @PostMapping("/exchange")
    public ServerResponse exchange(Integer userId, Integer itemId) {

        if (userId == null) {
            return ServerResponse.createServerResponseByFail("未登录，请先登录");
        }

        if (itemId == null) {
            return ServerResponse.createServerResponseByFail("参数错误：商品ID不能为空");
        }

        //  处理兑换逻辑
        try {
            return shopService.exchangeItem(userId, itemId);
        } catch (RuntimeException e) {
            // 捕获 Service 层抛出的积分不足、库存不足等异常，友好提示
            return ServerResponse.createServerResponseByFail(e.getMessage());
        }
    }

    /**
     * 获取我的积分明细
     */
    @GetMapping("/history")
    public ServerResponse getMyPointHistory(Integer userId) {

        if (userId == null) {
            return ServerResponse.createServerResponseByFail("未登录，请先登录");
        }

        try {
            // 解析 UserId
            return shopService.getPointHistory(userId);
        } catch (Exception e) {
            return ServerResponse.createServerResponseByFail("查询失败，请稍后再试");
        }
    }
    // 获取我的订单
    @GetMapping("/myOrders")
    public ServerResponse getMyOrders(@RequestParam(required = false) Integer  userId, @RequestParam(required = false) String keyword) {
        return shopService.getMyOrders(userId, keyword);
    }
    // 删除订单
    @ResponseBody
    @RequestMapping("/deleteOrder")
    public ServerResponse deleteOrder(int id, int userId) {
        return shopService.deleteOrder(id,userId);
    }
}