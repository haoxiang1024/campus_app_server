package com.school.mapper;

import com.school.entity.ShopItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopItemMapper {
    List<ShopItem> selectAllActive();
    List<ShopItem> selectAll();
    ShopItem selectByPrimaryKey(Integer id);
    // 利用数据库乐观锁机制，防止超卖并发问题
    int deductStock(Integer id);
    // 插入
    int insert(ShopItem item);
    // 更新
    int update(ShopItem item);
    // 修改商品上架状态
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}
