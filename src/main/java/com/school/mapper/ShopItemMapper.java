package com.school.mapper;

import com.school.entity.ShopItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopItemMapper {
    List<ShopItem> selectAllActive();
    ShopItem selectByPrimaryKey(Integer id);
    // 利用数据库乐观锁机制，防止超卖并发问题
    int deductStock(Integer id);
}
