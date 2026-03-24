package com.school.services.impl;

import com.school.entity.LostFoundType;
import com.school.mapper.LostFoundTypeMapper;
import com.school.services.api.LostFoundTypeService;
import com.school.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 失物招领分类服务实现类
 * 实现失物招领分类管理相关功能
 */
@Service
public class LostFoundTypeServiceImpl implements LostFoundTypeService {
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;


    /**
     * 获取所有失物招领分类
     * @return ServerResponse 包含分类名称列表的响应对象
     */
    @Override
    public ServerResponse getAllType() {

        List<LostFoundType> lostfoundtypeList = lostFoundTypeMapper.GetAll();
        if (lostfoundtypeList == null) {
            //数据为空
            return ServerResponse.createServerResponseByFail("数据为空");
        } else {
            //返回数据
            return ServerResponse.createServerResponseBySuccess(lostfoundtypeList);
        }

    }


    /**
     * 根据分类名称获取分类ID
     * 支持中英文分类名称的双向查找
     * @param name 分类名称
     * @return ServerResponse 包含分类ID的响应对象
     */
    @Override
    public ServerResponse getIdByName(String name) {
        // 获取所有分类类型
        List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
        int id = 0;
        for (LostFoundType lostfoundtype : lostFoundTypes) {
            if (lostfoundtype.getName().equals(name)) {
                id = lostfoundtype.getId();
                break; // 匹配到后直接 break，提高效率
            }
        }
        return ServerResponse.createServerResponseBySuccess(0, id, "获取数据成功");
    }
}
