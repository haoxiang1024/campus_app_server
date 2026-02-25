package com.school.services.api;

import com.school.entity.User;
import com.school.utils.ServerResponse;


/**
 * 用户服务接口
 * 提供用户管理相关的核心业务功能
 */
public interface UserService {
    /**
     * 用户注册
     * @param phone 手机号码
     * @param email 邮箱地址
     * @param password 密码
     * @return ServerResponse 包含注册用户信息和操作结果的响应对象
     */
    ServerResponse register(String phone,String email,String password);
    
    /**
     * 用户登录
     * @param phone 手机号码
     * @param pwd 密码
     * @return ServerResponse 包含用户信息和登录结果的响应对象
     */
    ServerResponse login(String phone,String pwd);
    
    /**
     * 重置用户密码
     * @param phone 手机号码
     * @param newPwd 新密码
     * @return ServerResponse 包含用户信息和重置结果的响应对象
     */
    ServerResponse resetPwd(String phone,String newPwd);
    
    /**
     * 更新用户头像
     * @param photo 头像URL
     * @param id 用户ID
     * @return ServerResponse 包含更新后用户信息的响应对象
     */
    ServerResponse updatePhoto(String photo,int id);
    
    /**
     * 更新用户基本信息
     * @param nickname 昵称
     * @param sex 性别
     * @param id 用户ID
     * @return ServerResponse 包含更新后用户信息的响应对象
     */
    ServerResponse updateAc(String nickname,String sex,int id);
    
    /**
     * 根据ID查询用户信息
     * @param id 用户ID
     * @return ServerResponse 包含用户信息的响应对象
     */
    ServerResponse getUser(int id);
    
    /**
     * 管理员更新用户信息
     * @param user 用户对象
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse updateUserInfo(User user);
    
    /**
     * 根据ID获取单个用户信息
     * @param id 用户ID
     * @return User 用户实体对象
     */
    User getUserById(Integer id);
    
    /**
     * 修改用户状态（启用/禁用）
     * @param ids 逗号分隔的ID字符串，如 "1,2,3"
     * @param status 目标状态（1-启用，0-禁用）
     * @return ServerResponse 操作结果响应对象
     */
    ServerResponse updateUserStatus(String ids, Integer status);
    
    /**
     * 分页查询用户列表
     * @param page 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @return ServerResponse 包含用户列表和分页信息的响应对象
     */
    ServerResponse getUserList(int page, int pageSize, String keyword);
    
    /**
     * 获取IM用户Token
     * @param uid 用户ID
     * @param nickname 用户昵称
     * @return ServerResponse 包含IM Token的响应对象
     */
    ServerResponse getIMUserToken(int uid, String nickname);
}
