package com.school.services.api;

import com.school.utils.ServerResponse;


/**
 * 失物招领分类服务接口
 * 提供失物招领分类管理相关功能
 */
public interface LostFoundTypeService {
    /**
     * 获取所有失物招领分类
     * @return ServerResponse 包含分类名称列表的响应对象
     */
    ServerResponse getAllType();

    /**
     * 根据分类名称获取分类ID
     * @param name 分类名称（支持中英文）
     * @return ServerResponse 包含分类ID的响应对象
     */
    ServerResponse getIdByName(String name);


}

