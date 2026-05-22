package com.school.controller;

import com.school.entity.User;
import com.school.services.api.*;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


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
    @Autowired
    private MessageService messageService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ISensitiveWordService sensitiveWordService;

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
    

    @ResponseBody
    @RequestMapping("/getAllUserInfo")
    public ServerResponse getAllUserInfo(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllUserInfo(page, size);
    }
    

    @ResponseBody
    @GetMapping("/searchUsers")
    public ServerResponse searchUsers(@RequestParam("keyword") String keyword) {
        return adminService.searchUsers(keyword);
    }
    

    @ResponseBody
    @PostMapping("/updateUserInfo")
    public ServerResponse updateUserInfo(
            @RequestParam("id") Integer id,
            @RequestParam("nickname") String nickname,
            @RequestParam("sex") String sex,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
            @RequestParam("points") int points,
            @RequestParam("role") Integer role) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ServerResponse.createServerResponseByFail(500, "用户不存在");
        }

        user.setNickname(nickname);
        user.setSex(sex);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPoints(points);
        user.setRole(role);

        if(photoFile != null && !photoFile.isEmpty()){
            String fileName = util.getFileName(photoFile);
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
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) Integer state,
                                     @RequestParam(required = false) Integer role) {

        return adminService.getUserListByPage(page, pageSize, keyword, state, role);
    }

    @ResponseBody
    @PostMapping("/updateStickStatus")
    public ServerResponse updateStickStatus(int id,int stick){
        return adminService.updateStickStatus(id, stick);
    }


    @ResponseBody
    @GetMapping("/getCommentsByPage")
    public ServerResponse getCommentsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String state) {
        return adminService.getCommentsByPage(page, pageSize, keyword, state);
    }


    @ResponseBody
    @RequestMapping ("/updateCommentStatus")
    public ServerResponse updateCommentStatus(
            @RequestParam("commentId") Integer commentId,
            @RequestParam("state") int state
            ) {
        return adminService.updateCommentStatus(commentId, state);
    }


    @ResponseBody
    @RequestMapping("/deleteCommentById")
    public ServerResponse deleteCommentById(@RequestParam("commentId") Integer commentId) {
        return adminService.deleteCommentById(commentId);
    }


    @ResponseBody
    @GetMapping("/getTypeByPage")
    public ServerResponse getTypeByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        return adminService.getTypeByPage(page, pageSize, keyword);
    }


    @ResponseBody
    @PostMapping("/addType")
    public ServerResponse addType(@RequestParam("name") String name) {
        return adminService.addType(name);
    }


    @ResponseBody
    @PostMapping("/updateType")
    public ServerResponse updateType(@RequestParam("id") Integer id, @RequestParam("name") String name) {
        return adminService.updateType(id, name);
    }


    @ResponseBody
    @PostMapping("/deleteTypeById")
    public ServerResponse deleteTypeById(@RequestParam("typeId") Integer typeId) {
        return adminService.deleteTypeById(typeId);
    }


    @ResponseBody
    @PostMapping("/deleteTypeBatch")
    public ServerResponse deleteTypeBatch(@RequestParam("ids") String ids) {
        return adminService.deleteTypeBatch(ids);
    }




    @ResponseBody
    @GetMapping("/getMessageByPage")
    public ServerResponse getMessageByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer state) {
        return messageService.getAdminMessagePage(page, pageSize, keyword, state);
    }


    @ResponseBody
    @PostMapping("/updateMessageStatus")
    public ServerResponse updateMessageStatus(
            @RequestParam Integer messageId,
            @RequestParam Integer state
           ) {
        return messageService.updateCommentStatus(messageId, state);
    }


    @ResponseBody
    @PostMapping("/deleteMessage")
    public ServerResponse deleteMessage(@RequestParam Integer messageId) {
        return messageService.deleteCommentById(messageId);
    }
    @ResponseBody
    @PostMapping("/verifyOrder")
    public ServerResponse verifyOrder(@RequestParam("verifyCode") String verifyCode,
                                      @RequestParam("adminId") Integer adminId) {
       return shopService.verifyOrder(verifyCode, adminId);
    }




    @ResponseBody
    @GetMapping("/pointHistory/getByPage")
    public ServerResponse getByPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) Integer type) {
        return shopService.getAllPointHistories(page, pageSize, keyword, type);
    }


    @ResponseBody
    @PostMapping("/pointHistory/delete")
    public ServerResponse delete(@RequestParam("id") Integer id) {
        return shopService.deletePointHistory(id);
    }



    @ResponseBody
    @GetMapping("/sensitive/list")
    public ServerResponse getList(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(required = false) String keyword) {
        Map<String, Object> data = sensitiveWordService.getSensitiveWordList(page, pageSize, keyword);
        return ServerResponse.createServerResponseBySuccess( data);
    }


    @ResponseBody
    @PostMapping("/sensitive/add")
    public ServerResponse addWord(@RequestParam String word) {
        boolean success = sensitiveWordService.addSensitiveWord(word);
        return success ? ServerResponse.createServerResponseBySuccess("添加敏感词成功") : ServerResponse.createServerResponseByFail("添加失败或敏感词已存在");
    }


    @ResponseBody
    @PostMapping("/sensitive/delete")
    public ServerResponse deleteWord(@RequestParam String word) {
        boolean success = sensitiveWordService.deleteSensitiveWord(word);
        return success ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");
    }


    @ResponseBody
    @PostMapping("/sensitive/batchDelete")
    public ServerResponse batchDelete(@RequestParam String words) {
        boolean success = sensitiveWordService.batchDeleteSensitiveWords(words);
        return success ? ServerResponse.createServerResponseBySuccess("批量删除成功") : ServerResponse.createServerResponseByFail("批量删除失败");
    }
}
