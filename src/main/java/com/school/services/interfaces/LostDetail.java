package com.school.services.interfaces;

import com.school.utils.ServerResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface LostDetail {
    //获取丢失物品的详细信息
    ServerResponse getLostDetailList(int lostfoundtypeId);

    //获取用户名称
    ServerResponse getUserName(int id);

    //添加丢失物品信息(json格式)
    ServerResponse addLost(String lostJson);

    //根据用户id获取用户发布的信息
    ServerResponse getAllByIdLostList(int user_id);

    //用户自己修改状态
    ServerResponse updateState(int id,String state,int user_id);

    //显示置顶信息
    ServerResponse showLostList(@Param("stick")int stick);
    //模糊查询
    ServerResponse getAllLost(String keyword);
    //根据id获取信息
    ServerResponse getLostById(Integer id);
    //根据id删除信息
    ServerResponse deleteLost(Integer id);
    //管理员审核lost(修改状态)
    ServerResponse updateLostStatus(Integer id, String state);
    //分页查询
    ServerResponse getLostListByPage(int page, int size);
}
