package com.school.controller;

import com.school.services.api.LostFoundService;
import com.school.utils.Json;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    // 用于处理安卓客户端传过来的文件
    @Autowired
    private LostFoundService lostFoundService;
    @Autowired
    private Util util;
    /**
     * @param upload_file 上传的文件
     * @param lostJson    丢失物品json数据
     * @param foundJson   招领物品json数据
     * @param op          此操作代表是上传丢失物品or招领物品
     */
    @ResponseBody
    @PostMapping("/upload")
    public ServerResponse upload(MultipartFile upload_file, String lostJson, String foundJson, String op) {
        String filename = util.getFileName(upload_file);
        //判断是丢失or招领
        if (op != null && !op.equals("")) {
            if (op.equals("丢失")) {
                //失物
                //添加物品信息,调用json-update设置图片名称
                String newLostJson = Json.updateByKey(lostJson, "img", filename);
                //System.out.println(newLostJson);
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
