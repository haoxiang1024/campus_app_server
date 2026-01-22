package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.Lostfoundtype;
import com.school.mapper.LostFoundTypeMapper;
import com.school.mapper.LostFoundMapper;
import com.school.services.interfaces.LostFound;
import com.school.utils.ResponseCode;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LostFoundService implements LostFound {
    @Autowired
    private LostFoundMapper lostFoundMapper;

    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;


    @Override
    public ServerResponse getLostDetailList(int lostfoundtypeId) {
        List<com.school.entity.LostFound> lostFoundDetailList = lostFoundMapper.selectByTypeId(lostfoundtypeId);
        if (lostFoundDetailList == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.DATA_EMPTY.getCode(), ResponseCode.DATA_EMPTY.getMsg());
        }
        //设置用户名
        for (com.school.entity.LostFound lostFound : lostFoundDetailList) {
            //获取用户id
            Integer userId = lostFound.getUserId();
            //根据用户id获取用户名
            String userNameId = lostFoundMapper.searchUserNameId(userId);
            //设置用户名
            lostFound.setNickname(userNameId);

        }

        return ServerResponse.createServerResponseBySuccess(lostFoundDetailList);
    }

    @Override
    public ServerResponse getUserName(int id) {
        String userName = lostFoundMapper.searchUserNameId(id);
        if (userName == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.DATA_EMPTY.getCode(), ResponseCode.DATA_EMPTY.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess(userName);
    }

    @Override
    public ServerResponse addLost(String lostJson) {
        //json字符串转java对象
        com.school.entity.LostFound lostFound = JSON.parseObject(lostJson, com.school.entity.LostFound.class);
        //添加信息并存入redis
        if (lostFoundMapper.addLost(lostFound)) {
            return ServerResponse.createServerResponseBySuccess(ResponseCode.ADD_LOST_SUCCESS.getMsg());
        } else {
            return ServerResponse.createServerResponseByFail(ResponseCode.ADD_LOST_FAIL.getCode(), ResponseCode.ADD_LOST_FAIL.getMsg());
        }
    }



    @Override
    public ServerResponse getAllByIdLostList(int user_id) {
        List<com.school.entity.LostFound> allByIdLostFoundList = lostFoundMapper.getAllByIdLostList(user_id);
        if (allByIdLostFoundList.size() == 0) {
            return ServerResponse.createServerResponseBySuccess("还未发布任何信息");
        } else {
            //设置用户名
            for (com.school.entity.LostFound lostFound : allByIdLostFoundList) {
                //获取用户id
                Integer userId = lostFound.getUserId();
                //根据用户id获取用户名
                String userNameId = lostFoundMapper.searchUserNameId(userId);
                //设置用户名
                lostFound.setNickname(userNameId);
                //设置图片
                String updatePic = Util.updatePic(lostFound.getImg());
                lostFound.setImg(updatePic);
            }
        }
        return ServerResponse.createServerResponseBySuccess(allByIdLostFoundList, "获取失物信息列表成功");
    }

    @Override
    public ServerResponse updateState(int id, String state, int user_id) {
        if (lostFoundMapper.updateState(id, state)) {
            List<com.school.entity.LostFound> allByIdLostFoundList = lostFoundMapper.getAllByIdLostList(user_id);
            return ServerResponse.createServerResponseBySuccess(allByIdLostFoundList, "状态已更改");
        }
        return ServerResponse.createServerResponseBySuccess("状态更改失败");
    }

    @Override
    public ServerResponse showLostList(int stick) {
        List<com.school.entity.LostFound> lists = lostFoundMapper.showLostList(stick);
        if (lists.size() == 0) {
            return ServerResponse.createServerResponseBySuccess("无置顶信息");
        }
        //设置用户名
        for (com.school.entity.LostFound lostFound : lists) {
            //获取用户id
            Integer userId = lostFound.getUserId();
            //获取分类id
            Integer lostfoundtypeId = lostFound.getLostfoundtypeId();
            //根据用户id获取用户名
            String userNameId = lostFoundMapper.searchUserNameId(userId);
            //设置用户名
            lostFound.setNickname(userNameId);
            //设置分类
            List<Lostfoundtype> lostfoundtypes = lostFoundTypeMapper.GetAll();
            for (Lostfoundtype type : lostfoundtypes) {
                if(Objects.equals(type.getId(), lostfoundtypeId)){
                    lostFound.setLostfoundtype(type);
                }
            }
            //设置图片
            String img = lostFound.getImg();
            String updatePic = Util.updatePic(img);
            lostFound.setImg(updatePic);
        }
        return ServerResponse.createServerResponseBySuccess(lists, "置顶信息");
    }

    @Override
    public ServerResponse getAllLost(String keyword) {
        List<com.school.entity.LostFound> list = lostFoundMapper.selectAllLost(keyword);
        return ServerResponse.createServerResponseBySuccess(list);
    }

    @Override
    public ServerResponse getLostById(Integer id) {
        com.school.entity.LostFound lostFound = lostFoundMapper.selectByPrimaryKey(id);
        if (lostFound == null) return ServerResponse.createServerResponseByFail("信息不存在");
        return ServerResponse.createServerResponseBySuccess(lostFound);    }

    @Override
    public ServerResponse deleteLost(Integer id) {
        int result = lostFoundMapper.deleteByPrimaryKey(id);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");    }

    @Override
    public ServerResponse updateLostStatus(Integer id, String state) {
        boolean isSuccess= lostFoundMapper.updateState(id, state);
        if(isSuccess){
            return ServerResponse.createServerResponseBySuccess("修改成功");
        }
        return ServerResponse.createServerResponseByFail("修改失败");
    }

    @Override
    public ServerResponse getLostListByPage(int page, int size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 执行查询
        List<com.school.entity.LostFound> list = lostFoundMapper.selectLostByPage();
        //  封装 PageInfo
        PageInfo<com.school.entity.LostFound> pageInfo = new PageInfo<>(list);
        // 封装返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        return ServerResponse.createServerResponseBySuccess(resultMap);
    }


}
