package com.school.controller;

import com.school.entity.User;
import com.school.services.impl.UserService;
import com.school.services.interfaces.Admin;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private Admin admin;
    @Autowired
    private UserService userService;
    @Autowired
    private Util util;
    //管理员登录
    @ResponseBody
    @RequestMapping("/login")
    public ServerResponse login(String username, String password){
        return admin.getUser(username,password);
    }
    //获取用户数据 失物招领数量
    @ResponseBody
    @RequestMapping("/getAllUser")
    public ServerResponse getAllUser(){
        return admin.getAllUser();
    }
    @ResponseBody
    @RequestMapping("/getAllLost")
    public ServerResponse getAllLost(){
        return admin.getAllLost();
    }
    @ResponseBody
    @RequestMapping("/getAllFound")
    public ServerResponse getAllFound(){
        return admin.getAllFound();
    }
    //获取用户详细信息
    @ResponseBody
    @RequestMapping("/getAllUserInfo")
    public ServerResponse getAllUserInfo(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return admin.getAllUserInfo(page, size);
    }
    //查询用户信息
    @ResponseBody
    @GetMapping("/searchUsers")
    public ServerResponse searchUsers(@RequestParam("keyword") String keyword) {
        return admin.searchUsers(keyword);
    }
    //修改用户信息
    @ResponseBody
    @PostMapping("/updateUserInfo")
    public ServerResponse updateUserInfo(
            @RequestParam("id") Integer id,
            @RequestParam("nickname") String nickname,
            @RequestParam("sex") String sex,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {


            //  获取现有用户信息
            User user = userService.getUserById(id);
            if (user == null) {
                return ServerResponse.createServerResponseByFail(500, "用户不存在");
            }

            // 更新字段
            user.setNickname(nickname);
            user.setSex(sex);
            user.setPhone(phone);
            user.setEmail(email);
            //上传头像 判断有没有更新头像 如果上传文件为空 说明不需要修改头像
            if(photoFile != null && !photoFile.isEmpty()){
                String fileName = util.getFileName(photoFile);
                // 数据库只存文件名
                user.setPhoto(fileName);
            }
        return userService.updateUserInfo(user);
    }}
