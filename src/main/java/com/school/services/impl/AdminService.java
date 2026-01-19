package com.school.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.User;
import com.school.mapper.AdminMapper;
import com.school.services.interfaces.Admin;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public ServerResponse resetPassword(String ids) {
        if (ids == null || ids.isEmpty()) {
            return ServerResponse.createServerResponseByFail(500,"ID不能为空");
        }

        // 将字符串 "1,2,3" 转为 List<Integer>
        List<Integer> idList = Stream.of(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        String pwd="a12345678";
         String hashedPassword = Util.encryptPwd(pwd);
         int result = adminMapper.batchResetPassword(idList, hashedPassword);

        if (result > 0) {
            return ServerResponse.createServerResponseBySuccess("成功重置 " + result + " 个账号的密码");
        }
        return ServerResponse.createServerResponseByFail(500,"重置密码失败");
    }
}
