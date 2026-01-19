package com.school.controller;

import com.school.services.interfaces.UserInterface;
import com.school.utils.EmailVerificationUtils;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;


@SuppressWarnings("rawtypes")
@Controller
public class UserController {
    @Autowired
    UserInterface userInterface;
    @Autowired
    private Util util;
    @ResponseBody
    @RequestMapping("/login")
    public ServerResponse login(String phone) {
        return userInterface.loginRegister(phone);
    }
    @ResponseBody
    @RequestMapping("/loginByPwd")
    public ServerResponse loginByPwd(String phone,String pwd){
       return userInterface.loginByPwd(phone,pwd);
    }
    @ResponseBody
    @RequestMapping("/resetPwd")
    public ServerResponse resetPwd(String phone,String newPwd,String email,String email_code){
        return userInterface.resetPwd(phone, newPwd);
    }
    @ResponseBody
    @RequestMapping("/updatePic")
    public ServerResponse updatePic(MultipartFile upload_file,int id) {
        //更新头像
        String fileName = util.getFileName(upload_file);
        return userInterface.updatePhoto(fileName, id);
    }
    @ResponseBody
    @RequestMapping("/updateAc")
    public ServerResponse updateAc(String nickname,String sex,int id){
        return userInterface.updateAc(nickname, sex, id);
    }
    /**
     * 发送验证码接口
     * @param email 用户邮箱
     * @return 操作结果
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

}
