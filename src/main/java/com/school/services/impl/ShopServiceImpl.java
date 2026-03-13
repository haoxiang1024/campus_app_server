package com.school.services.impl;

import com.school.entity.PointHistory;
import com.school.entity.ShopItem;
import com.school.mapper.PointHistoryMapper;
import com.school.mapper.ShopItemMapper;
import com.school.mapper.UserMapper;
import com.school.services.api.ShopService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointHistoryMapper pointHistoryMapper;

    @Override
    public ServerResponse getActiveShopItems() {
        List<ShopItem> items = shopItemMapper.selectAllActive();
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

        //  插入积分变动流水
        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setType(3); // 假设 3 代表消耗兑换
        history.setPointsChanged(-item.getRequired_points());
        history.setDescription("兑换了商品：" + item.getName());
        pointHistoryMapper.insert(history);

        return ServerResponse.createServerResponseBySuccess("兑换成功！可以在个人中心查看");
    }

    @Override
    public ServerResponse getPointHistory(Integer userId) {
        List<PointHistory> histories = pointHistoryMapper.selectByUserId(userId);
        return ServerResponse.createServerResponseBySuccess(histories);
    }
}