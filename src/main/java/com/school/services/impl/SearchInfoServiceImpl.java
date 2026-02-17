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
    @Override
    public ServerResponse searchInfo(String value) {
        List<SearchInfo> searchInfos = searchMapper.searchInfoByValue(value);
        if (searchInfos.size() == 0) {
            return ServerResponse.createServerResponseBySuccess("没有找到相关信息");
        } else {
            //设置用户名
            for (SearchInfo searchInfo : searchInfos) {
                //获取用户id
                Integer userId = searchInfo.getUser_id();
                //根据用户id获取用户名
                String userNameId = userMapper.userInfo(userId).getNickname();
                //设置用户名
                searchInfo.setNickname(userNameId);
                //设置图片
                String updatePic = util.updatePic(searchInfo.getImg());
                searchInfo.setImg(updatePic);
                //设置分类
                List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
                for (LostFoundType type : lostFoundTypes
                ) {
                    if (Objects.equals(type.getId(), searchInfo.getLostfoundtype_id())) {
                        searchInfo.setLostfoundtype(type);
                    }

                }

            }
        }
        return ServerResponse.createServerResponseBySuccess(searchInfos, "查找成功");
    }
}
