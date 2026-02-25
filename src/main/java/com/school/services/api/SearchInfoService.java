package com.school.services.api;

import com.school.utils.ServerResponse;


/**
 * 搜索信息服务接口
 * 提供信息搜索相关功能
 */
public interface SearchInfoService {
    /**
     * 根据关键字搜索失物招领信息
     * @param value 搜索关键字
     * @return ServerResponse 包含搜索结果列表的响应对象
     */
    ServerResponse searchInfo(String value);

}
