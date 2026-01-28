package com.school.services.impl;

import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.UserService;
import com.school.utils.DateUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;


    @Override
    public ServerResponse register(String phone) {
        Integer userid = userMapper.findUserByPhone(phone);
        Date time = DateUtil.getTime();//获取时间
        Integer bigDecimal = 1000;
        String nickname = Util.NickNameRandom();//随机获取昵称
        String photo;
        try {
            photo = Util.ImageSearch("头像");//随机获取头像
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = new User(nickname, phone, photo, "男", bigDecimal, 100, time);
        if (userid == null) {
            //注册，第一次用户信息为系统默认，后续用户可以自行修改
            //注册成功后返回user对象
            userMapper.register(phone, user);
        } else {
            //已经有账户,返回userInfo
            User userInfo = userMapper.userInfo(userid);
            //设置头像
            String pic = Util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            return ServerResponse.createServerResponseBySuccess(userInfo, "已有账户，立即登录");
        }
        return ServerResponse.createServerResponseBySuccess(user, "注册成功");
    }

    @Override
    public ServerResponse login(String phone, String pwd) {
        Integer userid = userMapper.findUserByPhone(phone);
        if (userid == null) {
            // 如果查询不到用户ID，说明该账号未注册
            return ServerResponse.createServerResponseByFail("登录失败，该手机号尚未注册");
        }
        //获取密码
        String hashedPwd = userMapper.login(phone);
        boolean isMatch = Util.verifyPwd(pwd, hashedPwd);
        if (isMatch) {
            //密码正确，获取详细用户信息
            User userInfo = userMapper.userInfo(userid);
            // 检查用户是否被禁用
             if (userInfo.getstate() == 0) {
                 return ServerResponse.createServerResponseByFail("该账号已被禁用");
             }
            String pic = Util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            return ServerResponse.createServerResponseBySuccess(userInfo, "登录成功");
        } else {
            // 密码错误
            return ServerResponse.createServerResponseByFail("登录失败，密码错误");
        }

    }

    @Override
    public ServerResponse resetPwd(String phone, String newPwd) {
        //判断新密码与旧密码是否相同如果相同则提示用户，不同则修改密码
        //获取用户信息
        Integer userId = userMapper.findUserByPhone(phone);
        if (userId == null) {
            return ServerResponse.createServerResponseBySuccess("用户不存在");
        }
            //密码重置
                String hashedPwd =  Util.encryptPwd(newPwd);//加密存储密码
                if (userMapper.resetPwd(phone, hashedPwd)) {
                    User userInfo = userMapper.userInfo(userId);
                    //重置成功
                    return ServerResponse.createServerResponseBySuccess(userInfo, "重置成功");
                }else {
                    return ServerResponse.createServerResponseBySuccess("重置失败");
                }


    }

    @Override
    public ServerResponse updatePhoto(String photo, int id) {
        if (userMapper.updatePhoto(photo, id)) {
            User userInfo = userMapper.userInfo(id);//获取用户信息
            //设置头像
            String pic = Util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            return ServerResponse.createServerResponseBySuccess(userInfo, "修改头像成功!");
        }
        return ServerResponse.createServerResponseBySuccess("修改失败!");

    }

    @Override
    public ServerResponse updateAc(String nickname, String sex,int id) {
        if (userMapper.updateAc(nickname,sex,id)) {
            User userInfo = userMapper.userInfo(id);
            //设置头像
            String pic = Util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            return ServerResponse.createServerResponseBySuccess(userInfo,"修改资料成功!");
        }
        return ServerResponse.createServerResponseBySuccess("修改失败!");
    }

    @Override
    public ServerResponse getUser(int id) {
        User userInfo = userMapper.userInfo(id);
        return ServerResponse.createServerResponseBySuccess(userInfo, "查询成功");
    }

    @Override
    public ServerResponse updateUserInfo(User user) {
        int resultCount = userMapper.updateUserInfo(user);

        if (resultCount > 0) {
            return ServerResponse.createServerResponseBySuccess("用户信息更新成功");
        } else {
            return ServerResponse.createServerResponseByFail(500,"用户信息更新失败");
        }
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    public ServerResponse updateUserStatus(String ids, Integer status) {
        if (StringUtils.isEmpty(ids)) {
            return ServerResponse.createServerResponseByFail(500,"参数错误：ID不能为空");
        }

        try {
            // 将前端传来的 "1,2,3" 字符串转为 List<Integer>
            List<Integer> idList = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            int result = userMapper.updateUserStatus(idList, status);

            if (result > 0) {
                return ServerResponse.createServerResponseBySuccess("状态更新成功");
            } else {
                return ServerResponse.createServerResponseByFail(500,"更新失败，未找到指定用户");
            }
        } catch (Exception e) {
            return ServerResponse.createServerResponseByFail(500,"服务器内部错误：" + e.getMessage());
        }
    }

    @Override
    public ServerResponse getUserList(int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        List<User> list = userMapper.getUserPage(offset, pageSize, keyword);
        int total = userMapper.getUserCount(keyword);
        int listSize=userMapper.getUserCount(null);//获取所有用户数量

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", (int) Math.ceil((double) total / pageSize));
        result.put("listSize", listSize);
        result.put("total_if", total);//查询了之后的所有记录
        return ServerResponse.createServerResponseBySuccess(result);
    }
}






