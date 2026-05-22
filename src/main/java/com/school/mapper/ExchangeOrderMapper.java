package com.school.mapper;

import com.school.entity.ExchangeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExchangeOrderMapper {

    int insertOrder(ExchangeOrder order);


    @Select("SELECT * FROM exchange_order WHERE verify_code = #{verifyCode}")
    ExchangeOrder selectByVerifyCode(@Param("verifyCode") String verifyCode);


    @Update("UPDATE exchange_order SET status = 1, verify_time = NOW(), verify_admin_id = #{adminId} WHERE verify_code = #{verifyCode} AND status = 0")
    int verifyOrder(@Param("verifyCode") String verifyCode, @Param("adminId") Integer adminId);

    List<ExchangeOrder> selectByUserIdAndKeyword(@Param("userId") Integer userId, @Param("keyword") String keyword);

    int deleteOrder(@Param("id") Integer id, @Param("userId") Integer userId);


    int updateStatus(ExchangeOrder order);

    int deleteOrderById(@Param("id") Integer id);

    List<ExchangeOrder> selectAllWithUserInfo(@Param("keyword") String keyword, @Param("status") Integer status);

    ExchangeOrder selectByOrderId(Integer id);
}
