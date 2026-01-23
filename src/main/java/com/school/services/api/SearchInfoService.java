package com.school.services.api;

import com.school.utils.ServerResponse;
import org.springframework.stereotype.Service;

@Service
public interface SearchInfoService {
    //搜索信息
    ServerResponse searchInfo(String value);

}
