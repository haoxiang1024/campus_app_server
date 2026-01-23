package com.school.services.api;

import com.school.utils.ServerResponse;
import org.springframework.stereotype.Service;

@Service
public interface LostFoundTypeService {
    //获取所有分类
    ServerResponse getAllType();



    //获取分类id
    ServerResponse getIdByName(String name);


}

