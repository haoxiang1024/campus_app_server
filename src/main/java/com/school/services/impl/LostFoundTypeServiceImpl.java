package com.school.services.impl;

import com.school.entity.LostFoundType;
import com.school.mapper.LostFoundTypeMapper;
import com.school.services.api.LostFoundTypeService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LostFoundTypeServiceImpl implements LostFoundTypeService {
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;



    @Override
    public ServerResponse getAllType() {

        List<LostFoundType> lostfoundtypeList = lostFoundTypeMapper.GetAll();
        if (lostfoundtypeList == null) {

            return ServerResponse.createServerResponseByFail("数据为空");
        } else {

            return ServerResponse.createServerResponseBySuccess(lostfoundtypeList);
        }

    }



    @Override
    public ServerResponse getIdByName(String name) {

        List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
        int id = 0;
        for (LostFoundType lostfoundtype : lostFoundTypes) {
            if (lostfoundtype.getName().equals(name)) {
                id = lostfoundtype.getId();
                break;
            }
        }
        return ServerResponse.createServerResponseBySuccess(0, id, "获取数据成功");
    }
}
