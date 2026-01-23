package com.school.services.impl;

import com.school.entity.LostFoundType;
import com.school.mapper.LostFoundTypeMapper;
import com.school.services.api.LostFoundTypeService;
import com.school.utils.ResponseCode;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LostFoundTypeServiceImpl implements LostFoundTypeService {
    String key = "typeList";//key值用于redsi存储
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;


    @Override
    public ServerResponse getAllType() {

        List<String> lostfoundtypeList = lostFoundTypeMapper.getAllType();
        if (lostfoundtypeList == null) {
            //数据为空
            return ServerResponse.createServerResponseByFail(ResponseCode.DATA_EMPTY.getCode(), ResponseCode.DATA_EMPTY.getMsg());
        } else {
            //返回数据
            return ServerResponse.createServerResponseBySuccess(lostfoundtypeList);
        }

    }


    @Override
    public ServerResponse getIdByName(String name) {
        //中英文转换
        String[] en = {"Digital Devices", "Certificates", "Daily Necessities", "Clothing and Apparel", "Other"};
        String[] cn = {"数码设备", "证件", "日用品", "服饰", "其他"};
        Map<String, String> map = new HashMap<>();//建立关系
        for (int i = 0; i < en.length; i++) {
            map.put(en[i], cn[i]);
        }
        //获取标题id
        int id = 0;
        List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
        for (LostFoundType lostfoundtype : lostFoundTypes) {
            if (lostfoundtype.getName().equals(name)) {
                //中文
                id = lostfoundtype.getId();
            } else {
                //英文
                String cnValue = map.get(name);
                if (lostfoundtype.getName().equals(cnValue)) {
                    id = lostfoundtype.getId();
                }
            }
        }
        return ServerResponse.createServerResponseBySuccess(id, ResponseCode.GET_DATA_SUCCESS.getMsg());
    }
}
