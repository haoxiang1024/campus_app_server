package com.school.controller;


import com.school.services.api.LostFoundService;
import com.school.services.api.LostFoundTypeService;
import com.school.utils.Json;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


/**
 * 失物招领控制器类
 * 处理失物招领相关的请求，包括物品发布、信息查询、状态更新等功能
 */
@Controller
public class LostFoundController {
    @Autowired
    private LostFoundTypeService lostFoundTypeService;
    @Autowired
    private LostFoundService lostFoundService;
    @Autowired
    private Util util;

    /**
     * 获取所有失物招领分类
     * @return 返回ServerResponse对象，包含所有分类信息
     */
    @ResponseBody
    @RequestMapping("/getAllType")
    public ServerResponse getAllType() {
        return lostFoundTypeService.getAllType();
    }

    /**
     * 根据标题和类型获取详细信息
     * @param title 标题
     * @param type 类型
     * @return 返回ServerResponse对象，包含符合条件的详细信息
     */
    @ResponseBody
    @RequestMapping("/DetailByTitle")
    public ServerResponse getDetailByTitle(String title,String type) {
        return lostFoundService.getDetailByTitle(title,type);
    }

    /**
     * 根据ID获取用户名并存储到session中
     * @param session HTTP会话对象
     * @param id 用户ID
     * @return 返回ServerResponse对象，包含用户名信息
     */
    @ResponseBody
    @RequestMapping("/getUname")
    public ServerResponse getUname(HttpSession session, int id) {
        ServerResponse lostDetailUserName = lostFoundService.getUserName(id);
        if (lostDetailUserName.isSuccess()) {
            session.setAttribute("nickname", lostDetailUserName.getData());
        }
        return lostDetailUserName;
    }

    /**
     * 根据名称获取类型ID
     * @param name 类型名称
     * @return 返回ServerResponse对象，包含对应的类型ID
     */
    @ResponseBody
    @RequestMapping("/getIdByName")
    public ServerResponse getIdByName(String name) {
        return lostFoundTypeService.getIdByName(name);
    }

    /**
     * 根据用户ID获取发布的失物招领信息
     * @param user_id 用户ID
     * @return 返回ServerResponse对象，包含该用户发布的所有失物招领信息
     */
    @ResponseBody
    @RequestMapping("/getLostFoundByUserId")
    public ServerResponse getLostFoundByUserId(int user_id) {
        return lostFoundService.getLostFoundByUserId(user_id);
    }

    /**
     * 更新失物招领信息状态
     * @param id 失物招领信息ID
     * @param state 新状态值
     * @param user_id 用户ID
     * @return 返回ServerResponse对象，包含更新结果信息
     */
    @ResponseBody
    @RequestMapping("/updateState")
    public ServerResponse updateState(int id, String state, int user_id) {
        return lostFoundService.updateState(id, state, user_id);
    }

    /**
     * 获取置顶列表信息
     * @param stick 置顶标识
     * @return 返回ServerResponse对象，包含置顶的失物招领信息列表
     */
    @ResponseBody
    @RequestMapping("/showTopList")
    public ServerResponse showTopList(int stick) {
        return lostFoundService.showTopList(stick);
    }

    /**
     * 发布失物或招领信息
     * @param upload_file 上传的文件
     * @param lostJson 丢失物品json数据
     * @param foundJson 招领物品json数据
     * @param op 操作类型，"失物"表示发布丢失物品，其他值表示发布招领物品
     * @return 返回ServerResponse对象，包含发布结果信息
     */
    @ResponseBody
    @PostMapping("/addLostFound")
    public ServerResponse addLostFound(MultipartFile upload_file, String lostJson, String foundJson, String op) {
        String filename = util.getFileName(upload_file);
        // 判断是发布丢失物品还是招领物品
        if (op != null && !op.equals("")) {
            ServerResponse serviceResponse = null;
            if (op.equals("失物")) {
                // 处理丢失物品发布
                String newLostJson = Json.updateByKey(lostJson, "img", filename);
                serviceResponse = lostFoundService.addLostFound(newLostJson);            }
            else {
                // 处理招领物品发布
                String newFoundJson = Json.updateByKey(foundJson, "img", filename);
                serviceResponse = lostFoundService.addLostFound(newFoundJson);
            }
                return serviceResponse;

        } else {
            return ServerResponse.createServerResponseBySuccess("未选择失物或者招领");
        }
    }

}
