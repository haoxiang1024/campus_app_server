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

    int deductStock(Integer id);

    int insert(ShopItem item);

    int update(ShopItem item);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    List<ShopItem> selectAllWithSearch(@Param("keyword") String keyword, @Param("status") Integer status);
    void addStock(Integer id, int stock);
}
