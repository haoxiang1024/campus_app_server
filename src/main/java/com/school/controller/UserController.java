package com.school.controller;

import com.school.entity.User;
import com.school.services.api.UserService;
import com.school.utils.EmailVerificationUtils;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;


/**
 * 用户控制器类
 * 处理用户相关的请求，包括注册、登录、信息更新、验证码等功能
 */
@SuppressWarnings("rawtypes")
@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private Util util;
    
    /**
     * 用户注册接口
     * @param phone 用户手机号
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 返回ServerResponse对象，包含注册结果信息
     */
    @ResponseBody
    @RequestMapping("/register")
    public ServerResponse register(String phone,String email,String password) {
        return userService.register(phone,email,password);
    }
    
    /**
     * 用户登录接口
     * @param phone 用户手机号
     * @param pwd 用户密码
     * @return 返回ServerResponse对象，包含登录结果信息
     */
    @ResponseBody
    @RequestMapping("/login")
    public ServerResponse login(String phone,String pwd){
       return userService.login(phone,pwd);
    }
    
    /**
     * 重置用户密码接口
     * @param phone 用户手机号
     * @param newPwd 新密码
     * @param email 用户邮箱
     * @param email_code 邮箱验证码
     * @return 返回ServerResponse对象，包含密码重置结果信息
     */
    @ResponseBody
    @RequestMapping("/resetPwd")
    public ServerResponse resetPwd(String phone,String newPwd,String email,String email_code){
        return userService.resetPwd(phone, newPwd);
    }
    
    /**
     * 更新用户头像接口
     * @param upload_file 上传的头像文件
     * @param id 用户ID
     * @return 返回ServerResponse对象，包含头像更新结果信息
     */
    @ResponseBody
    @RequestMapping("/updatePic")
    public ServerResponse updatePic(MultipartFile upload_file,int id) {
        // 更新头像
        String fileName = util.getFileName(upload_file);
        return userService.updatePhoto(fileName, id);
    }
    
    /**
     * 更新用户账户信息接口
     * @param nickname 用户昵称
     * @param sex 用户性别
     * @param id 用户ID
     * @return 返回ServerResponse对象，包含账户信息更新结果
     */
    @ResponseBody
    @RequestMapping("/updateAc")
    public ServerResponse updateAc(String nickname,String sex,int id){
        return userService.updateAc(nickname, sex, id);
    }
    
    /**
     * 发送验证码接口
     * @param email 用户邮箱
     * @return 返回ServerResponse对象，包含验证码发送结果信息
     */
    @ResponseBody
    @RequestMapping("/send_code")
    public ServerResponse send_code(String email){
        try {
            EmailVerificationUtils.sendVerificationCodeEmail(email);
            return ServerResponse.createServerResponseBySuccess( "验证码已发送，请注意查收");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ServerResponse.createServerResponseByFail(500,"验证码发送失败");
        }
    }

    /**
     * 验证邮箱验证码接口
     * @param email 用户邮箱
     * @param code 验证码
     * @return 返回ServerResponse对象，包含验证码验证结果信息
     */
    @ResponseBody
    @RequestMapping("/verify_code")
    public ServerResponse verify_code(String email,String code){
        boolean isValid = EmailVerificationUtils.verifyCode(email, code);
        if(isValid){
            return ServerResponse.createServerResponseBySuccess( "验证码验证通过");
        }else {
            return ServerResponse.createServerResponseBySuccess( "验证码验证失败");
        }
    }
    
    /**
     * 获取IM用户令牌接口
     * @param uid 用户ID
     * @param nickname 用户昵称
     * @return 返回ServerResponse对象，包含IM用户令牌信息
     */
    @ResponseBody
    @RequestMapping("/getIMUserToken")
    public ServerResponse getIMUserToken(int uid, String nickname) {
        return userService.getIMUserToken(uid,nickname);
    }
    
    /**
     * 根据ID获取用户信息接口
     * @param id 用户ID
     * @return 返回User对象，包含用户详细信息
     */
    @ResponseBody
    @RequestMapping("/getUserById")
    public User getUserById(int id) {
        return userService.getUserById(id);
    }

}
