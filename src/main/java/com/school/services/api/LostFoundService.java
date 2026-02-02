package com.school.services.api;

import com.school.utils.ServerResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface LostFoundService {
    //获取物品的详细信息
    ServerResponse getLostFoundDetail(int lostfoundtypeId,String type);

    //获取用户名称
    ServerResponse getUserName(int id);

    //添加物品信息(json格式)
    ServerResponse addLostFound(String lostfoundtJson);

    //根据用户id获取用户发布的信息
    ServerResponse getLostFoundByUserId(int user_id);

    //用户自己修改状态
    ServerResponse updateState(int id,String state,int user_id);

    //显示置顶信息
    ServerResponse showTopList(@Param("stick")int stick);
    //模糊查询
    ServerResponse getInfoByKey(String keyword);
    //根据id获取信息
    ServerResponse getLostFoundById(Integer id);
    //根据id删除信息
    ServerResponse deleteLostFoundById(Integer id);
    //管理员审核(修改状态)
    ServerResponse updateLostFoundStatus(Integer id, String state);
    //分页查询
    ServerResponse getLostListByPage(int page, int size);
    //获取某分类下的信息
    ServerResponse getDetailByTitle(String title,String type);
}
