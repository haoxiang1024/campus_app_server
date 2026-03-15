package com.school.services.impl;

import com.school.entity.ExchangeOrder;
import com.school.entity.PointHistory;
import com.school.entity.ShopItem;
import com.school.mapper.ExchangeOrderMapper;
import com.school.mapper.PointHistoryMapper;
import com.school.mapper.ShopItemMapper;
import com.school.mapper.UserMapper;
import com.school.services.api.ShopService;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Util util;

    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    @Autowired
    private ExchangeOrderMapper exchangeOrderMapper;

    @Override
    public ServerResponse getActiveShopItems() {
        List<ShopItem> items = shopItemMapper.selectAllActive();
        for (ShopItem shopItem : items) {
            shopItem.setImage_url(util.updatePic(shopItem.getImage_url()));
        }
        return ServerResponse.createServerResponseBySuccess(items);
    }

    /**
     * 核心积分兑换逻辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse exchangeItem(Integer userId, Integer itemId) {
        //  查询商品是否存在以及状态
        ShopItem item = shopItemMapper.selectByPrimaryKey(itemId);
        if (item == null || item.getStatus() == 0) {
            return ServerResponse.createServerResponseByFail("该商品不存在或已下架");
        }
        if (item.getStock() <= 0) {
            return ServerResponse.createServerResponseByFail("商品库存不足");
        }

        //  扣减商品库存 (带有并发防超卖判断)
        int stockRow = shopItemMapper.deductStock(itemId);
        if (stockRow == 0) {
            return ServerResponse.createServerResponseByFail("哎呀，手慢了，商品刚刚被抢光啦！");
        }

        //  扣除用户积分 (带有余额足够判断)
        int pointsRow = userMapper.deductPoints(userId, item.getRequired_points());
        if (pointsRow == 0) {
            // 积分不够扣减失败，抛出异常以回滚上方扣掉的库存
            throw new RuntimeException("您的积分不足，无法兑换");
        }
        //生成提货码
        String verifyCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String orderNo = "EX" + System.currentTimeMillis();
        //  生成订单记录
        ExchangeOrder order = new ExchangeOrder();
        order.setOrder_no(orderNo);
        order.setUser_id(userId);
        order.setItem_id(itemId);
        order.setItem_name(item.getName());
        order.setPoints_cost(item.getRequired_points());
        order.setVerify_code(verifyCode);
        exchangeOrderMapper.insertOrder(order);
        //  插入积分变动流水
        PointHistory history = new PointHistory();
        history.setUser_id(userId);
        history.setType(3); // 假设 3 代表消耗兑换
        history.setPoints_changed(-item.getRequired_points());
        history.setDescription("兑换了商品：" + item.getName());
        pointHistoryMapper.insert(history);

        return ServerResponse.createServerResponseBySuccess( verifyCode,"兑换成功！");
    }

    @Override
    public ServerResponse getPointHistory(Integer userId) {
        List<PointHistory> histories = pointHistoryMapper.selectByUserId(userId);
        return ServerResponse.createServerResponseBySuccess(histories);
    }

    /**
     * 管理员核销提货码
     */
    @Override
    public ServerResponse verifyOrder(String verifyCode, Integer adminId) {
        ExchangeOrder order = exchangeOrderMapper.selectByVerifyCode(verifyCode);
        if (order == null) {
            return ServerResponse.createServerResponseByFail("无效的提货码");
        }
        if (order.getStatus() != 0) {
            return ServerResponse.createServerResponseByFail("该提货码已被核销或作废！");
        }

        // 执行核销
        int updateCount = exchangeOrderMapper.verifyOrder(verifyCode, adminId);
        if (updateCount > 0) {
            return ServerResponse.createServerResponseBySuccess("核销成功！商品：【" + order.getItem_name() + "】");
        }
        return ServerResponse.createServerResponseByFail("核销失败，请重试");
    }

    @Override
    public ServerResponse getMyOrders(Integer userId, String keyword) {
        List<ExchangeOrder> orders = exchangeOrderMapper.selectByUserIdAndKeyword(userId, keyword);
        if (orders != null) {
            return ServerResponse.createServerResponseBySuccess(orders);
        }
        return ServerResponse.createServerResponseByFail("没有找到相关订单");
    }

    @Override
    public ServerResponse deleteOrder(Integer id, Integer userId) {
        int deleteCount = exchangeOrderMapper.deleteOrder(id, userId);
        if (deleteCount > 0) {
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        return ServerResponse.createServerResponseByFail("删除失败");
    }
}