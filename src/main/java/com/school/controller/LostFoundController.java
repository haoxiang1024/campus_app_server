package com.school.controller;


import com.school.entity.LostFoundType;
import com.school.mapper.LostFoundTypeMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class LostFoundController {
    @Autowired
    private LostFoundTypeService lostFoundTypeService;
    @Autowired
    private LostFoundService lostFoundService;
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;
    @Autowired
    private Util util;

    @ResponseBody
    @RequestMapping("/getAllType")
    public ServerResponse getAllType() {
        return lostFoundTypeService.getAllType();
    }

    @ResponseBody
    //获取分类下的内容
    @RequestMapping("/DetailByTitle")
    public ServerResponse getDetailByTitle(HttpSession session, String title) {
        //获取标题id
        int id = 0;
        List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
        //中英文转换
        String[] en = {"Digital Devices", "Certificates", "Daily Necessities", "Clothing and Apparel", "Other"};
        String[] cn = {"数码设备", "证件", "日用品", "服饰", "其他"};
        Map<String, String> map = new HashMap<>();//建立关系
        for (int i = 0; i < en.length; i++) {
            map.put(en[i], cn[i]);
        }
        for (LostFoundType lostfoundtype : lostFoundTypes) {
            if (lostfoundtype.getName().equals(title)) {
                //中文
                id = lostfoundtype.getId();
            } else {
                //英文
                String cnValue = map.get(title);
                if (lostfoundtype.getName().equals(cnValue)) {
                    id = lostfoundtype.getId();
                }
            }
       }
        ServerResponse lostDetailList = lostFoundService.getLostFoundDetail(id);
        //更新图片地址
        List<com.school.entity.LostFound> data = (List<com.school.entity.LostFound>) lostDetailList.getData();
        String updatePic = "";
        for (com.school.entity.LostFound lostFound : data) {
            if (lostFound.getImg() == null) {
                break;
            } else {
                updatePic = Util.updatePic(lostFound.getImg());
                lostFound.setImg(updatePic);
            }

        }
        //更新数据
        lostDetailList.setData(data);
        if (lostDetailList.isSuccess()) {
            session.setAttribute("detailList", lostDetailList.getData());
        }
        return lostDetailList;
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



    //获取类型id
    @ResponseBody
    @RequestMapping("/getIdByName")
    public ServerResponse getIdByName(String name) {
        return lostFoundTypeService.getIdByName(name);
    }

    //获取发布信息
    @ResponseBody
    @RequestMapping("/getLostFoundByUserId")
    public ServerResponse getLostFoundByUserId(int user_id) {
        return lostFoundService.getLostFoundByUserId(user_id);
    }

    //更改状态
    @ResponseBody
    @RequestMapping("/updateState")
    public ServerResponse updateState(int id, String state, int user_id) {
        return lostFoundService.updateState(id, state, user_id);
    }

    //置顶信息
    @ResponseBody
    @RequestMapping("/showTopList")
    public ServerResponse showTopList(int stick) {
        return lostFoundService.showTopList(stick);
    }

    // 用于处理安卓客户端传过来的文件
    /**
     * @param upload_file 上传的文件
     * @param lostJson    丢失物品json数据
     * @param foundJson   招领物品json数据
     * @param op          此操作代表是上传丢失物品or招领物品
     */
    @ResponseBody
    @PostMapping("/addLostFound")
    public ServerResponse upload(MultipartFile upload_file, String lostJson, String foundJson, String op) {
        String filename = util.getFileName(upload_file);
        //判断是丢失or招领
        if (op != null && !op.equals("")) {
            if (op.equals("丢失")) {
                //失物
                //添加物品信息,调用json-update设置图片名称
                String newLostJson = Json.updateByKey(lostJson, "img", filename);
                lostFoundService.addLostFound(newLostJson);
            } else {
                //招领
                String newFoundJson = Json.updateByKey(foundJson, "img", filename);
                lostFoundService.addLostFound(newFoundJson);
            }
        } else {
            return ServerResponse.createServerResponseBySuccess("未选择失物or招领");
        }
        return ServerResponse.createServerResponseBySuccess("发布成功!");
    }

}
