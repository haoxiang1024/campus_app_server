package com.school.services.impl;

import com.school.entity.LostFoundType;
import com.school.entity.SearchInfo;
import com.school.mapper.LostFoundTypeMapper;
import com.school.mapper.SearchMapper;
import com.school.mapper.UserMapper;
import com.school.services.api.SearchInfoService;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 搜索信息服务实现类
 * 实现信息搜索相关功能，整合用户、分类等关联信息
 */
@Service
public class SearchInfoServiceImpl implements SearchInfoService {
    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;
    @Autowired
    private Util util;
    /**
     * 根据关键字搜索失物招领信息
     * 搜索结果包含完整的用户信息、分类信息和图片路径
     * @param value 搜索关键字
     * @return ServerResponse 包含搜索结果列表的响应对象
     */
    @Override
    public ServerResponse searchInfo(String value) {
        // 执行关键字搜索，获取初步搜索结果
        List<SearchInfo> searchInfos = searchMapper.searchInfoByValue(value);
        
        // 检查搜索结果是否为空
        if (searchInfos.size() == 0) {
            return ServerResponse.createServerResponseBySuccess("没有找到相关信息");
        } else {
            // 为每个搜索结果补充完整的关联信息
            for (SearchInfo searchInfo : searchInfos) {
                // 获取发布信息的用户ID
                Integer userId = searchInfo.getUser_id();
                
                // 根据用户ID查询用户名
                String userNameId = userMapper.userInfo(userId).getNickname();
                
                // 设置用户名
                searchInfo.setNickname(userNameId);
                
                // 处理图片路径，转换为完整URL
                String updatePic = util.updatePic(searchInfo.getImg());
                searchInfo.setImg(updatePic);
                
                // 设置分类信息
                List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
                for (LostFoundType type : lostFoundTypes) {
                    // 匹配分类ID，设置对应的分类对象
                    if (Objects.equals(type.getId(), searchInfo.getLostfoundtype_id())) {
                        searchInfo.setLostfoundtype(type);
                    }
                }
            }
        }
        
        // 返回包含完整信息的搜索结果
        return ServerResponse.createServerResponseBySuccess(searchInfos, "查找成功");
    }
}
