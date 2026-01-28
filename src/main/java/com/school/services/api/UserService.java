package com.school.services.api;

import com.school.entity.User;
import com.school.utils.ServerResponse;
import org.springframework.stereotype.Service;

@SuppressWarnings("rawtypes")
@Service
public interface UserService {
    //登录注册
    ServerResponse register(String phone);
    //登录
    ServerResponse login(String phone,String pwd);
    //忘记密码
    ServerResponse resetPwd(String phone,String newPwd);
    //修改头像
    ServerResponse updatePhoto(String photo,int id);
    //用户资料修改
    ServerResponse updateAc(String nickname,String sex,int id);
    //根据id查询用户信息
    ServerResponse getUser(int id);
    // 管理员更新用户信息
    ServerResponse updateUserInfo(User user);
    // 获取单个用户信息
    User getUserById(Integer id);
    /**
     * 修改用户状态
     * @param ids 逗号分隔的ID字符串，如 "1,2,3"
     * @param status 目标状态
     */
    ServerResponse updateUserStatus(String ids, Integer status);
    //分页查询
    ServerResponse getUserList(int page, int pageSize, String keyword);
}
