package com.school.controller;

import com.school.entity.ShopItem;
import com.school.services.api.ShopService;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private Util util;
    /**
     * 获取可兑换商品列表
     */
    @ResponseBody
    @RequestMapping("/items")
    public ServerResponse getItems() {
        return shopService.getActiveShopItems();
    }
    /**
     * 获取所有商品列表
     */
    @ResponseBody
    @RequestMapping("/items/all")
    public ServerResponse allItems() {
        return shopService.getAllItems();
    }

    @PostMapping("/item/save")
    public ServerResponse saveItem(ShopItem item, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        // 处理图片上传
        if(imageFile != null && !imageFile.isEmpty()){
            String fileName = util.getFileName(imageFile);
            // 数据库只存储文件名
            item.setImage_url(fileName);
        }
        return shopService.saveOrUpdateItem(item);
    }

    // 修改商品上架状态
    @PostMapping("/updateStatus")
    public ServerResponse updateStatus(@RequestParam Integer id, @RequestParam Integer status) {
       return shopService.updateStatus(id, status);
    }

    @ResponseBody
    @RequestMapping("/orders")
    public ServerResponse listOrders() {
        return shopService.getAllOrders();
    }
    /**
     * 发起积分兑换
     */
    @ResponseBody
    @RequestMapping("/exchange")
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
    @ResponseBody
    @RequestMapping("/history")
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
    @ResponseBody
    @RequestMapping("/myOrders")
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