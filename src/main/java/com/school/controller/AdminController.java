package com.school.controller;

import com.school.entity.User;
import com.school.services.interfaces.Admin;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private Admin admin;
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



}
