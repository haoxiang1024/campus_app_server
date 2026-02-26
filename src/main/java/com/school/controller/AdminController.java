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

/**
 * 管理员控制器类
 * 处理管理员相关的请求，包括用户管理、失物招领管理等
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    // 注入服务层依赖
    @Autowired
    private AdminService adminService;    // 管理员服务
    @Autowired
    private UserService userService;      // 用户服务
    @Autowired
    private Util util;                   // 工具类
    @Autowired
    private LostFoundService lostFoundService;  // 失物招领服务
    
    /**
     * 获取所有用户数据
     * @return 返回ServerResponse对象，包含所有用户信息
     */
    @ResponseBody
    @RequestMapping("/getAllUser")
    public ServerResponse getAllUser(){
        return adminService.getAllUser();
    }
    
    /**
     * 获取所有失物招领的数量统计
     * @param type 可选参数，用于指定失物招领的类型
     * @return 返回ServerResponse对象，包含失物招领的数量统计信息
     */
    @ResponseBody
    @RequestMapping("/getAllLostFoundCount")
    public ServerResponse getAllLostFoundCount(@RequestParam(value = "type", required = false) String type) {
        return adminService.getAllLostFoundCount(type);
    }
    
    /**
     * 分页获取用户详细信息
     * @param page 页码，默认为1
     * @param size 每页大小，默认为10
     * @return 返回ServerResponse对象，包含分页的用户详细信息
     */
    @ResponseBody
    @RequestMapping("/getAllUserInfo")
    public ServerResponse getAllUserInfo(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllUserInfo(page, size);
    }
    
    /**
     * 根据关键字搜索用户信息
     * @param keyword 搜索关键字
     * @return 返回ServerResponse对象，包含匹配的用户信息列表
     */
    @ResponseBody
    @GetMapping("/searchUsers")
    public ServerResponse searchUsers(@RequestParam("keyword") String keyword) {
        return adminService.searchUsers(keyword);
    }
    
    /**
     * 更新用户信息
     * @param id 用户ID
     * @param nickname 用户昵称
     * @param sex 用户性别
     * @param phone 用户手机号
     * @param email 用户邮箱
     * @param photoFile 用户头像文件（可选）
     * @return 返回ServerResponse对象，包含更新结果信息
     */
    @ResponseBody
    @PostMapping("/updateUserInfo")
    public ServerResponse updateUserInfo(
            @RequestParam("id") Integer id,
            @RequestParam("nickname") String nickname,
            @RequestParam("sex") String sex,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "photoFile", required = false) MultipartFile photoFile) {

            // 获取现有用户信息
            User user = userService.getUserById(id);
            if (user == null) {
                return ServerResponse.createServerResponseByFail(500, "用户不存在");
            }

            // 更新用户基本信息字段
            user.setNickname(nickname);
            user.setSex(sex);
            user.setPhone(phone);
            user.setEmail(email);
            
            // 处理用户头像上传
            if(photoFile != null && !photoFile.isEmpty()){
                String fileName = util.getFileName(photoFile);
                // 数据库只存储文件名
                user.setPhoto(fileName);
            }
        return userService.updateUserInfo(user);
    }
    
    /**
     * 更新用户状态接口
     * @param ids 需要更新的用户ID字符串，可能包含多个ID，用逗号分隔
     * @param state 要更新的用户状态值
     * @return 返回ServerResponse对象，包含操作结果信息
     */
    @ResponseBody
    @PostMapping("/updateUserStatus")
    public ServerResponse updateUserStatus(@RequestParam("ids") String ids,
                                           @RequestParam("state") Integer state) {
        return userService.updateUserStatus(ids, state);
    }

    /**
     * 重置用户密码的接口方法
     * @param ids 需要重置密码的用户ID，多个ID用逗号分隔
     * @return 返回ServerResponse对象，包含操作结果信息
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public ServerResponse resetPassword(String ids) {
        return adminService.resetPassword(ids);
    }
    
    /**
     * 根据关键字搜索失物招领信息
     * @param keyword 搜索关键字
     * @return 返回ServerResponse对象，包含匹配的失物招领信息列表
     */
    @ResponseBody
    @GetMapping("/getInfoByKey")
    public ServerResponse getInfoByKey(String keyword) {
        return lostFoundService.getInfoByKey(keyword);
    }
    
    /**
     * 根据ID获取失物招领详情
     * @param lostFoundId 失物招领ID
     * @return 返回ServerResponse对象，包含失物招领详细信息
     */
    @ResponseBody
    @GetMapping("/getLostFoundById")
    public ServerResponse getLostFoundById(Integer lostFoundId) {
        return lostFoundService.getLostFoundById(lostFoundId);
    }
    
    /**
     * 根据ID删除失物招领信息
     * @param lostFoundId 失物招领ID
     * @return 返回ServerResponse对象，包含删除操作结果
     */
    @ResponseBody
    @PostMapping("/deleteLostFoundById")
    public ServerResponse deleteLostFoundById(Integer lostFoundId) {
        return lostFoundService.deleteLostFoundById(lostFoundId);
    }
    
    /**
     * 更新失物招领状态
     * @param lostFoundId 失物招领ID
     * @param state 新的状态值
     * @return 返回ServerResponse对象，包含更新操作结果
     */
    @ResponseBody
    @PostMapping("/updateLostFoundStatus")
    public ServerResponse updateLostFoundStatus(Integer lostFoundId, String state) {
        return lostFoundService.updateLostFoundStatus(lostFoundId, state);
    }
    
    /**
     * 分页获取失物招领信息列表
     * @param page 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param keyword 搜索关键字（可选）
     * @param type 失物招领类型（可选）
     * @param state 状态筛选（可选）
     * @return 返回ServerResponse对象，包含分页的失物招领信息列表
     */
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
    
    /**
     * 分页搜索用户列表
     * @param page 页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param keyword 搜索关键字（可选）
     * @return 返回ServerResponse对象，包含分页的用户搜索结果
     */
    @ResponseBody
    @GetMapping("/searchList")
    public ServerResponse searchList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(required = false) String keyword) {
        return userService.getUserList(page, pageSize, keyword);
    }
    
    /**
     * 更新失物招领置顶状态
     * @param id 失物招领ID
     * @param stick 置顶状态值
     * @return 返回ServerResponse对象，包含更新操作结果
     */
    @ResponseBody
    @PostMapping("/updateStickStatus")
    public ServerResponse updateStickStatus(int id,int stick){
        return adminService.updateStickStatus(id, stick);
    }

    /**
     * 分页获取评论列表
     * @param page 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键字（可选）
     * @param state 评论状态（可选）
     */
    @ResponseBody
    @GetMapping("/getCommentsByPage")
    public ServerResponse getCommentsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String state) {
        return adminService.getCommentsByPage(page, pageSize, keyword, state);
    }

    /**
     * 更新评论状态 (通过/驳回)
     * @param commentId 评论ID
     * @param state 新的状态值
     * @param reason 驳回原因（可选）
     */
    @ResponseBody
    @PostMapping("/updateCommentStatus")
    public ServerResponse updateCommentStatus(
            @RequestParam("commentId") Integer commentId,
            @RequestParam("state") int state,
            @RequestParam(value = "reason", required = false) String reason) {
        return adminService.updateCommentStatus(commentId, state, reason);
    }

    /**
     * 根据ID删除评论信息
     * @param commentId 评论ID
     */
    @ResponseBody
    @PostMapping("/deleteCommentById")
    public ServerResponse deleteCommentById(@RequestParam("commentId") Integer commentId) {
        return adminService.deleteCommentById(commentId);
    }
}
