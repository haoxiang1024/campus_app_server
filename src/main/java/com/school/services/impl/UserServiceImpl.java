package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.school.entity.LoginResponseDTO;
import com.school.entity.User;
import com.school.mapper.UserMapper;
import com.school.services.api.RongCloudApi;
import com.school.services.api.UserService;
import com.school.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private RongCloudApi rongCloudApi;
    @Autowired
    private Util util;
    @Autowired
    private TokenUtils serverTokenUtils;

    @Override
    public ServerResponse register(String phone,String email,String password,int role) {

        Date time = DateUtil.getTime();
        int points=100;
        String sex=Util.SexRandom();
        String nickname = Util.NickNameRandom();
        String photo;
        String fileName;
        int state=1;

        String hashedPwd=Util.encryptPwd(password);
        

        try {
            photo = Util.ImageSearch("头像");

            String saveDirectory = "upload/";

            fileName = UUID.randomUUID() + ".jpg";

            Path targetPath = Paths.get(saveDirectory, fileName);

            Files.createDirectories(targetPath.getParent());

            URL url = new URI(photo).toURL();

            try (InputStream in = url.openStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        

        User user = new User(fileName,phone,sex,points,time,email,state,role,nickname,hashedPwd);
        

        List<User> userList = userMapper.getalll();
        for(User u:userList){
            if(u.getPhone().equals(phone)){
                return ServerResponse.createServerResponseByFail( "该用户已注册！");
            }
        }
        

        if(userMapper.register(user)) {

            String response = Util.registerUserToProvider(user.getId().toString(), user.getNickname(), user.getPhoto());
            JSONObject jsonObject = JSON.parseObject(response);

            String token = jsonObject.getString("token");

            user.setIm_token(token);
            userMapper.updateUserInfo(user);

            if(token != null && !token.isEmpty()){

                String bearerToken = serverTokenUtils.generateToken(user.getId());

                LoginResponseDTO loginResponseDTO=new LoginResponseDTO(user,bearerToken);
                return ServerResponse.createServerResponseBySuccess(loginResponseDTO, "注册成功");
            }
        }
        return ServerResponse.createServerResponseByFail( "注册失败");
    }



    @Override
    public ServerResponse login(String phone, String pwd) {

        Integer userid = userMapper.findUserByPhone(phone);
        if (userid == null) {

            return ServerResponse.createServerResponseByFail("登录失败，该手机号尚未注册");
        }
        

        String hashedPwd = userMapper.login(phone);
        

        boolean isMatch = Util.verifyPwd(pwd, hashedPwd);
        if (isMatch) {

            User userInfo = userMapper.userInfo(userid);

             if (userInfo.getstate() == 0) {
                 return ServerResponse.createServerResponseByFail("该账号已被禁用");
             }
            

            String pic = util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);

            String bearerToken = serverTokenUtils.generateToken(userInfo.getId());

            LoginResponseDTO loginResponseDTO=new LoginResponseDTO(userInfo,bearerToken);
            
            return ServerResponse.createServerResponseBySuccess(loginResponseDTO, "登录成功");
        } else {

            return ServerResponse.createServerResponseByFail("登录失败，密码错误");
        }

    }


    @Override
    public ServerResponse resetPwd(String phone, String newPwd) {

        Integer userId = userMapper.findUserByPhone(phone);
        if (userId == null) {
            return ServerResponse.createServerResponseBySuccess("用户不存在");
        }

                String hashedPwd =  Util.encryptPwd(newPwd);
                if (userMapper.resetPwd(phone, hashedPwd)) {
                    User userInfo = userMapper.userInfo(userId);

                    return ServerResponse.createServerResponseBySuccess(userInfo, "重置成功");
                }else {
                    return ServerResponse.createServerResponseBySuccess("重置失败");
                }


    }


    @Override
    public ServerResponse updatePhoto(String photo, int id) {
        if (userMapper.updatePhoto(photo, id)) {
            User userInfo = userMapper.userInfo(id);

            String pic = util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);

            String res=rongCloudApi.refresh(String.valueOf(id),userInfo.getNickname(),photo);
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


    @Override
    public ServerResponse updateAc(String nickname, String sex,int id) {
        if (userMapper.updateAc(nickname,sex,id)) {
            User userInfo = userMapper.userInfo(id);

            String pic = util.updatePic(userInfo.getPhoto());
            userInfo.setPhoto(pic);

            String res=rongCloudApi.refresh(String.valueOf(id),nickname,userInfo.getPhoto());
            JSONObject jsonObject = JSON.parseObject(res);
            String code = jsonObject.getString("code");
            if(code.equals("200")){
                return ServerResponse.createServerResponseBySuccess(userInfo,"状态更新成功");

            }else {
                return ServerResponse.createServerResponseBySuccess("IM错误，修改失败!");
            }
        }
        return ServerResponse.createServerResponseBySuccess("修改失败!");
    }




    @Override
    public ServerResponse updateUserInfo(User user) {
        int resultCount = userMapper.updateUserInfo(user);
        if (resultCount > 0) {

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
    public ServerResponse getIMUserToken(int uid, String nickname) {
        User user = userMapper.userInfo(uid);

        if (user.getIm_token() != null && !user.getIm_token().isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(user.getIm_token(),"token获取成功");
        }

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


    @Override
    public ServerResponse updatePhone(int id, String newPhone, String code) {

        User user = userMapper.userInfo(id);
        if (user == null) {
            return ServerResponse.createServerResponseByFail("用户不存在");
        }
        String oldEmail = user.getEmail();
        if (StringUtils.isEmpty(oldEmail)) {
            return ServerResponse.createServerResponseByFail("原账号未绑定邮箱，无法进行安全验证");
        }


        boolean isValid = EmailVerificationUtils.verifyCode(oldEmail, code);
        if (!isValid) {
            return ServerResponse.createServerResponseByFail("验证码错误或已过期");
        }


        Integer existId = userMapper.findUserByPhone(newPhone);
        if (existId != null && !existId.equals(id)) {
            return ServerResponse.createServerResponseByFail("该手机号已被其他账号绑定");
        }


        User updateParam = new User();
        updateParam.setId(id);
        updateParam.setPhone(newPhone);

        int result = userMapper.updateUserInfo(updateParam);
        if (result > 0) {

            User updatedUser = userMapper.userInfo(id);
            updatedUser.setPhoto(util.updatePic(updatedUser.getPhoto()));

            String res=rongCloudApi.refresh(String.valueOf(updatedUser.getId()),updatedUser.getNickname(),updatedUser.getPhoto());
            JSONObject jsonObject = JSON.parseObject(res);
            String imcode = jsonObject.getString("code");
            if(imcode.equals("200")){
                return ServerResponse.createServerResponseBySuccess(updatedUser, "手机号修改成功");
            }

        }
        return ServerResponse.createServerResponseByFail("修改失败，请稍后重试");
    }


    @Override
    public ServerResponse updateEmail(int id, String newEmail, String code) {

        User user = userMapper.userInfo(id);
        if (user == null) {
            return ServerResponse.createServerResponseByFail("用户不存在");
        }
        String oldEmail = user.getEmail();
        if (StringUtils.isEmpty(oldEmail)) {
            return ServerResponse.createServerResponseByFail("原账号未绑定邮箱，无法进行安全验证");
        }


        boolean isValid = EmailVerificationUtils.verifyCode(oldEmail, code);
        if (!isValid) {
            return ServerResponse.createServerResponseByFail("验证码错误或已过期");
        }


        User updateParam = new User();
        updateParam.setId(id);
        updateParam.setEmail(newEmail);

        int result = userMapper.updateUserInfo(updateParam);
        if (result > 0) {

            User updatedUser = userMapper.userInfo(id);
            updatedUser.setPhoto(util.updatePic(updatedUser.getPhoto()));
            return ServerResponse.createServerResponseBySuccess(updatedUser, "邮箱修改成功");
        }
        return ServerResponse.createServerResponseByFail("修改失败，请稍后重试");
    }


    @Override
    public ServerResponse deleteAccount(int id) {

        int result = userMapper.deleteUserById(id);
        if (result > 0) {
            return ServerResponse.createServerResponseBySuccess("账号已注销");
        }
        return ServerResponse.createServerResponseByFail("账号注销失败，请稍后重试");
    }
}






