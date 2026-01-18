package com.school.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.User;
import com.school.mapper.AdminMapper;
import com.school.services.interfaces.Admin;
import com.school.utils.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService implements Admin {
    @Autowired
    AdminMapper adminMapper;
    @Override
    public ServerResponse getUser(String username, String password) {
        com.school.entity.Admin admin = adminMapper.getUser(username, password);
        if (admin!= null) {
            return ServerResponse.createServerResponseBySuccess(admin,"登录成功");
        }else {
            return ServerResponse.createServerResponseByFail(500);
        }

}

    @Override
    public ServerResponse getAllUser() {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllUserCount());
    }

    @Override
    public ServerResponse getAllLost() {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllLostCount());
    }

    @Override
    public ServerResponse getAllFound() {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllFoundCount());
    }

    // AdminServiceImpl.java
    @Override
    public ServerResponse getAllUserInfo(int page, int size) {
        // 开启分页
        PageHelper.startPage(page, size);

        // 执行查询
        List<User> list = adminMapper.getAllUserInfo();

        //  将结果封装进 PageInfo 对象，它包含了 total (总条数)
        PageInfo<User> pageInfo = new PageInfo<>(list);

        //  返回封装好的数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList()); // 当前页数据
        resultMap.put("total", pageInfo.getTotal()); // 总记录数，例如 102

        return ServerResponse.createServerResponseBySuccess(resultMap);
    }

    @Override
    public ServerResponse searchUsers(String keyword) {
        // 参数校验
        if (StringUtils.isBlank(keyword)) {
            return ServerResponse.createServerResponseByFail(500,"搜索关键字不能为空");
        }

        // 执行模糊查询
        List<User> list = adminMapper.searchUsers(keyword);

        // 返回成功响应
        return ServerResponse.createServerResponseBySuccess(list);
    }
}
