package com.school.controller;


import com.school.entity.LostFound;
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



@Controller
public class LostFoundController {
    @Autowired
    private LostFoundTypeService lostFoundTypeService;
    @Autowired
    private LostFoundService lostFoundService;
    @Autowired
    private Util util;


    @ResponseBody
    @RequestMapping("/getAllType")
    public ServerResponse getAllType() {
        return lostFoundTypeService.getAllType();
    }


    @ResponseBody
    @RequestMapping("/DetailByTitle")
    public ServerResponse getDetailByTitle(String title,String type) {
        return lostFoundService.getDetailByTitle(title,type);
    }


    @ResponseBody
    @RequestMapping("/getUname")
    public ServerResponse getUname(HttpSession session, int id) {
        ServerResponse lostDetailUserName = lostFoundService.getUserName(id);
        if (lostDetailUserName.isSuccess()) {
            session.setAttribute("nickname", lostDetailUserName.getData());
        }
        return lostDetailUserName;
    }


    @ResponseBody
    @RequestMapping("/getIdByName")
    public ServerResponse getIdByName(String name) {
        return lostFoundTypeService.getIdByName(name);
    }


    @ResponseBody
    @RequestMapping("/getLostFoundByUserId")
    public ServerResponse getLostFoundByUserId(int user_id) {
        return lostFoundService.getLostFoundByUserId(user_id);
    }


    @ResponseBody
    @RequestMapping("/updateState")
    public ServerResponse updateState(int id, String state, int user_id) {
        return lostFoundService.updateState(id, state, user_id);
    }


    @ResponseBody
    @RequestMapping("/showTopList")
    public ServerResponse showTopList(Integer stick) {
        return lostFoundService.showTopList(stick);
    }


    @ResponseBody
    @PostMapping("/addLostFound")
    public ServerResponse addLostFound(MultipartFile upload_file, String lostFoundJson) {
        String filename = util.getFileName(upload_file);
        String type = Json.getJsonValueByKey(lostFoundJson, "type").toString();

        if (type != null && !type.isEmpty()) {
            String newLostFoundJson = Json.updateByKey(lostFoundJson, "img", filename);
            return lostFoundService.addLostFound(newLostFoundJson);
        } else {
            return ServerResponse.createServerResponseBySuccess("未选择失物或者招领");
        }
    }

}
