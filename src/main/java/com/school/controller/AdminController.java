package com.school.controller;

import com.school.entity.User;
import com.school.services.api.AdminService;
import com.school.services.api.LostFoundService;
import com.school.services.api.UserService;
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
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private Util util;
    @Autowired
    private LostFoundService lostFoundService;
    //获取用户数据 失物招领数量
    @ResponseBody
    @RequestMapping("/getAllUser")
    public ServerResponse getAllUser(){
        return adminService.getAllUser();
    }
    @ResponseBody
    @RequestMapping("/getAllLostFoundCount")
    public ServerResponse getAllLostFoundCount(@RequestParam(value = "type", required = false) String type) {
        return adminService.getAllLostFoundCount(type);
    }
    //获取用户详细信息
    @ResponseBody
    @RequestMapping("/getAllUserInfo")
    public ServerResponse getAllUserInfo(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllUserInfo(page, size);
    }
    //查询用户信息
    @ResponseBody
    @GetMapping("/searchUsers")
    public ServerResponse searchUsers(@RequestParam("keyword") String keyword) {
        return adminService.searchUsers(keyword);
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
    }
    @ResponseBody
    @PostMapping("/updateUserStatus")
    public ServerResponse updateUserStatus(@RequestParam("ids") String ids,
                                           @RequestParam("state") Integer state) {
        return userService.updateUserStatus(ids, state);
    }

    @PostMapping("/resetPassword")
    @ResponseBody
    public ServerResponse resetPassword(String ids) {
        return adminService.resetPassword(ids);
    }
    @ResponseBody
    @GetMapping("/getInfoByKey")
    public ServerResponse getInfoByKey(String keyword) {
        return lostFoundService.getInfoByKey(keyword);
    }
    @ResponseBody
    @GetMapping("/getLostFoundById")
    public ServerResponse getLostFoundById(Integer lostFoundId) {
        return lostFoundService.getLostFoundById(lostFoundId);
    }
    @ResponseBody
    @PostMapping("/deleteLostFoundById")
    public ServerResponse deleteLostFoundById(Integer lostFoundId) {
        return lostFoundService.deleteLostFoundById(lostFoundId);
    }
    @ResponseBody
    @PostMapping("/updateLostFoundStatus")
    public ServerResponse updateLostFoundStatus(Integer lostFoundId, String state) {
        return lostFoundService.updateLostFoundStatus(lostFoundId, state);
    }
    @ResponseBody
    @GetMapping("/getLostFoundByPage")
    public ServerResponse getLostFoundByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String state) {
        return adminService.getLostFoundByPage(page, pageSize, keyword, type, state);
    }
    @ResponseBody
    @GetMapping("/searchList")
    public ServerResponse searchList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(required = false) String keyword) {
        return userService.getUserList(page, pageSize, keyword);
    }
    @ResponseBody
    @PostMapping("/updateStickStatus")
    public ServerResponse updateStickStatus(int id,int stick){
        return adminService.updateStickStatus(id, stick);
    }
}
