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

    @ResponseBody
    @RequestMapping("/items")
    public ServerResponse getItems() {
        return shopService.getActiveShopItems();
    }

    @ResponseBody
    @RequestMapping("/items/all")
    public ServerResponse allItems(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) Integer status) {
        return shopService.getAllItems(page, pageSize, keyword, status);
    }

    @PostMapping("/item/save")
    public ServerResponse saveItem(ShopItem item, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        if(imageFile != null && !imageFile.isEmpty()){
            String fileName = util.getFileName(imageFile);

            item.setImage_url(fileName);
        }
        return shopService.saveOrUpdateItem(item);
    }


    @PostMapping("/updateStatus")
    public ServerResponse updateStatus(@RequestParam Integer id, @RequestParam Integer status) {
       return shopService.updateStatus(id, status);
    }

    @ResponseBody
    @RequestMapping("/orders")
    public ServerResponse listOrders(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int pageSize,
                                     @RequestParam(value = "keyword", required = false) String keyword,
                                     @RequestParam(value = "status", required = false) Integer status) {
        return shopService.getAllOrders(page, pageSize, keyword, status);
    }

    @PostMapping("/deleteOrderById")
    public ServerResponse deleteOrderById(Integer id) {
        return shopService.deleteOrderById(id);
    }

    @ResponseBody
    @RequestMapping("/exchange")
    public ServerResponse exchange(Integer userId, Integer itemId) {

        if (userId == null) {
            return ServerResponse.createServerResponseByFail("未登录，请先登录");
        }

        if (itemId == null) {
            return ServerResponse.createServerResponseByFail("参数错误：商品ID不能为空");
        }


        try {
            return shopService.exchangeItem(userId, itemId);
        } catch (RuntimeException e) {
            return ServerResponse.createServerResponseByFail(e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping("/history")
    public ServerResponse getMyPointHistory(Integer userId) {

        if (userId == null) {
            return ServerResponse.createServerResponseByFail("未登录，请先登录");
        }

        try {
            return shopService.getPointHistory(userId);
        } catch (Exception e) {
            return ServerResponse.createServerResponseByFail("查询失败，请稍后再试");
        }
    }

    @ResponseBody
    @RequestMapping("/myOrders")
    public ServerResponse getMyOrders(@RequestParam(required = false) Integer  userId, @RequestParam(required = false) String keyword) {
        return shopService.getMyOrders(userId, keyword);
    }

    @ResponseBody
    @RequestMapping("/deleteOrder")
    public ServerResponse deleteOrder(int id, int userId) {
        return shopService.deleteOrder(id,userId);
    }
}