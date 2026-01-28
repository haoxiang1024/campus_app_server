package com.school.services.api;

import com.school.utils.ServerResponse;
import org.springframework.stereotype.Service;
@Service
public interface AdminService {
    //获取用户 失物招领信息数量
    ServerResponse getAllUser();
    ServerResponse getAllLostFoundCount(String type);
    //获取所有用户详细信息
    ServerResponse getAllUserInfo(int page,int size);
    //模糊查询用户信息
    ServerResponse searchUsers(String keyword);
    //批量重置密码
    ServerResponse resetPassword(String ids);
    //分页查询信息
    ServerResponse getLostFoundByPage(int page, int pageSize, String keyword, String type, String state);
    //置顶信息
    ServerResponse updateStickStatus(int id,int stick);
}

