package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.RongCloudApi;
import com.school.services.api.UserService;
import com.school.utils.DateUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 实现用户管理相关的核心业务功能，包括注册、登录、IM集成等
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private RongCloudApi rongCloudApi;
    @Autowired
    private Util util;
    /**
     * 用户注册
     * 创建新用户账户，包含随机昵称、头像生成和IM系统集成
     * @param phone 手机号码
     * @param email 邮箱地址
     * @param password 密码
     * @return ServerResponse 包含注册用户信息和操作结果的响应对象
     */
    @Override
    public ServerResponse register(String phone,String email,String password) {
        // 初始化用户基本属性
        Date time = DateUtil.getTime();//获取时间
        Integer balance = 1000;  // 初始余额
        Integer prestige = 1000; // 初始声望值
        String sex=Util.SexRandom(); // 随机性别
        String nickname = Util.NickNameRandom();//随机获取昵称
        String photo; // 头像URL
        int state=1;//状态：1启用 0禁用
        int role=0;//0：普通用户 1：管理员用户
        
        // 密码加密处理
        String hashedPwd=Util.encryptPwd(password);
        
        // 获取随机头像
        try {
            photo = Util.ImageSearch("头像");//随机获取头像
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        // 创建用户对象
        User user = new User(photo,phone,sex,balance,prestige,time,email,state,role,nickname,hashedPwd);
        
        // 验证手机号是否已经注册过
        List<User> userList = userMapper.getalll();
        for(User u:userList){
            if(u.getPhone().equals(phone)){
                return ServerResponse.createServerResponseByFail( "该用户已注册！");
            }
        }
        
        // 执行数据库注册操作
        if(userMapper.register(user)) {
            // IM系统注册 - 同步创建IM账户
            String response = Util.registerUserToProvider(user.getId().toString(), user.getNickname(), user.getPhoto());
            JSONObject jsonObject = JSON.parseObject(response);
            String token = jsonObject.getString("token");
            
            // 验证IM注册是否成功
            if(token != null && !token.isEmpty()){
                return ServerResponse.createServerResponseBySuccess(user, "注册成功");
            }
        }
        return ServerResponse.createServerResponseByFail( "注册失败");
    }


    /**
     * 用户登录
     * 验证用户身份并返回完整的用户信息
     * @param phone 手机号码
     * @param pwd 密码
     * @return ServerResponse 包含用户信息和登录结果的响应对象
     */
    @Override
    public ServerResponse login(String phone, String pwd) {
        // 根据手机号查找用户ID
        Integer userid = userMapper.findUserByPhone(phone);
        if (userid == null) {
            // 如果查询不到用户ID，说明该账号未注册
            return ServerResponse.createServerResponseByFail("登录失败，该手机号尚未注册");
        }
        
        // 获取数据库中存储的加密密码
        String hashedPwd = userMapper.login(phone);
        
        // 验证密码是否匹配
        boolean isMatch = Util.verifyPwd(pwd, hashedPwd);
        if (isMatch) {
            // 密码正确，获取详细用户信息
            User userInfo = userMapper.userInfo(userid);
            
            // 检查用户是否被禁用
             if (userInfo.getstate() == 0) {
                 return ServerResponse.createServerResponseByFail("该账号已被禁用");
             }
            
            // 更新头像路径为完整URL
            String pic = util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            
            return ServerResponse.createServerResponseBySuccess(userInfo, "登录成功");
        } else {
            // 密码错误
            return ServerResponse.createServerResponseByFail("登录失败，密码错误");
        }

    }

    /**
     * 重置用户密码
     * 用于用户忘记密码时的安全重置功能
     * @param phone 手机号码
     * @param newPwd 新密码
     * @return ServerResponse 包含用户信息和重置结果的响应对象
     */
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

    /**
     * 更新用户头像
     * 同步更新数据库和IM系统的用户头像信息
     * @param photo 头像URL
     * @param id 用户ID
     * @return ServerResponse 包含更新后用户信息的响应对象
     */
    @Override
    public ServerResponse updatePhoto(String photo, int id) {
        if (userMapper.updatePhoto(photo, id)) {
            User userInfo = userMapper.userInfo(id);//获取用户信息
            //设置头像
            String pic = util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            //IM用户资料同步修改
            String res=rongCloudApi.refresh(String.valueOf(id),userInfo.getNickname(),pic);
            JSONObject jsonObject = JSON.parseObject(res);
            String code = jsonObject.getString("code");
            if(code.equals("200")){
                return ServerResponse.createServerResponseBySuccess(userInfo,"修改资料成功!");
            }else {
                return ServerResponse.createServerResponseBySuccess("IM错误，修改失败!");
            }
        }
        return ServerResponse.createServerResponseBySuccess("修改失败!");

    }

    /**
     * 更新用户基本信息
     * 同步更新数据库和IM系统的用户基本信息
     * @param nickname 昵称
     * @param sex 性别
     * @param id 用户ID
     * @return ServerResponse 包含更新后用户信息的响应对象
     */
    @Override
    public ServerResponse updateAc(String nickname, String sex,int id) {
        if (userMapper.updateAc(nickname,sex,id)) {
            User userInfo = userMapper.userInfo(id);
            //设置头像
            String pic = util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);
            //IM用户资料同步修改
            String res=rongCloudApi.refresh(String.valueOf(id),nickname,pic);
            JSONObject jsonObject = JSON.parseObject(res);
            String code = jsonObject.getString("code");
            if(code.equals("200")){
                return ServerResponse.createServerResponseBySuccess(userInfo,"修改资料成功!");
            }else {
                return ServerResponse.createServerResponseBySuccess("IM错误，修改失败!");
            }
        }
        return ServerResponse.createServerResponseBySuccess("修改失败!");
    }

    /**
     * 根据ID查询用户信息
     * @param id 用户ID
     * @return ServerResponse 包含用户信息的响应对象
     */
    @Override
    public ServerResponse getUser(int id) {
        User userInfo = userMapper.userInfo(id);
        return ServerResponse.createServerResponseBySuccess(userInfo, "查询成功");
    }

    /**
     * 管理员更新用户信息
     * 支持批量更新用户资料并同步到IM系统
     * @param user 用户对象
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse updateUserInfo(User user) {
        int resultCount = userMapper.updateUserInfo(user);
        if (resultCount > 0) {
            //IM用户资料同步修改
            String res=rongCloudApi.refresh(String.valueOf(user.getId()),user.getNickname(),user.getPhoto());
            JSONObject jsonObject = JSON.parseObject(res);
            String code = jsonObject.getString("code");
            if(code.equals("200")){
                return ServerResponse.createServerResponseBySuccess("用户信息更新成功");
            }else {
                return ServerResponse.createServerResponseBySuccess("IM错误，修改失败!");
            }
        } else {
            return ServerResponse.createServerResponseByFail(500,"用户信息更新失败");
        }
    }

    /**
     * 根据ID获取单个用户信息
     * @param id 用户ID
     * @return User 用户实体对象
     */
    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    /**
     * 修改用户状态（启用/禁用）
     * 支持批量操作用户账户状态
     * @param ids 逗号分隔的ID字符串，如 "1,2,3"
     * @param status 目标状态（1-启用，0-禁用）
     * @return ServerResponse 操作结果响应对象
     */
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

    /**
     * 分页查询用户列表
     * 支持关键字搜索和分页展示
     * @param page 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @return ServerResponse 包含用户列表和分页信息的响应对象
     */
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

    /**
     * 获取IM用户Token
     * 处理用户首次连接和重复连接的不同场景
     * @param uid 用户ID
     * @param nickname 用户昵称
     * @return ServerResponse 包含IM Token的响应对象
     */
    @Override
    public ServerResponse getIMUserToken(int uid, String nickname) {
        User user = userMapper.userInfo(uid);
        //判断是否是非首次连接（数据库已有 Token）
        if (user.getIm_token() != null && !user.getIm_token().isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(user.getIm_token(),"token获取成功");
        }
        //首次连接
        String response=Util.registerUserToProvider(String.valueOf(uid),nickname,user.getPhoto());
        JSONObject jsonObject = JSON.parseObject(response);
        String token = jsonObject.getString("token");
        if (token != null && !token.isEmpty()){
            user.setIm_token(token);
            userMapper.updateUserInfo(user);
            return ServerResponse.createServerResponseBySuccess(token,"token获取成功");
        }
        return ServerResponse.createServerResponseByFail("聊天服务连接失败，请稍后再试");
    }
}






