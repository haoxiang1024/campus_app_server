package com.school.mapper;

import com.school.entity.ExchangeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExchangeOrderMapper {
    // 插入订单
    int insertOrder(ExchangeOrder order);

    // 根据核销码查询订单
    @Select("SELECT * FROM exchange_order WHERE verify_code = #{verifyCode}")
    ExchangeOrder selectByVerifyCode(@Param("verifyCode") String verifyCode);

    // 更新订单状态（核销操作），防重复核销
    @Update("UPDATE exchange_order SET status = 1, verify_time = NOW(), verify_admin_id = #{adminId} WHERE verify_code = #{verifyCode} AND status = 0")
    int verifyOrder(@Param("verifyCode") String verifyCode, @Param("adminId") Integer adminId);
    // 查询订单
    List<ExchangeOrder> selectByUserIdAndKeyword(@Param("userId") Integer userId, @Param("keyword") String keyword);
    // 删除订单
    int deleteOrder(@Param("id") Integer id, @Param("userId") Integer userId);
}
