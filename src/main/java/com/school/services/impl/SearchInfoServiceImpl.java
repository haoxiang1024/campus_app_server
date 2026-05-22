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

            for (SearchInfo searchInfo : searchInfos) {

                Integer userId = searchInfo.getUser_id();
                

                String userNameId = userMapper.userInfo(userId).getNickname();
                

                searchInfo.setNickname(userNameId);
                

                String updatePic = util.updatePic(searchInfo.getImg());
                searchInfo.setImg(updatePic);
                

                List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
                for (LostFoundType type : lostFoundTypes) {

                    if (Objects.equals(type.getId(), searchInfo.getLostfoundtype_id())) {
                        searchInfo.setLostfoundtype(type);
                    }
                }
            }
        }
        

        return ServerResponse.createServerResponseBySuccess(searchInfos, "查找成功");
    }
}
