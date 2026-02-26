package com.school.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.Comment;
import com.school.entity.CommentVO;
import com.school.entity.LostFound;
import com.school.entity.User;
import com.school.mapper.AdminMapper;
import com.school.services.api.AdminService;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 管理员服务实现类
 * 实现管理员后台管理相关功能
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    /**
     * 获取所有用户数量
     * @return ServerResponse 包含用户总数的响应对象
     */
    @Override
    public ServerResponse getAllUser() {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllUserCount());
    }

    /**
     * 获取失物招领信息数量
     * @param type 信息类型（失物/招领）
     * @return ServerResponse 包含信息数量的响应对象
     */
    @Override
    public ServerResponse getAllLostFoundCount(String type) {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllLostFoundCount(type));
    }

    /**
     * 分页获取所有用户详细信息
     * 使用PageHelper实现分页功能
     * @param page 页码
     * @param size 每页大小
     * @return ServerResponse 包含用户列表和分页信息的响应对象
     */
    @Override
    public ServerResponse getAllUserInfo(int page, int size) {
        // 开启分页插件，设置当前页码和每页大小
        PageHelper.startPage(page, size);
        
        // 执行用户信息查询
        List<User> list = adminMapper.getAllUserInfo();
        
        // 将查询结果封装进 PageInfo 对象，它自动计算总条数等分页信息
        PageInfo<User> pageInfo = new PageInfo<>(list);
        
        // 构建返回结果Map，包含分页数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList()); // 当前页数据
        resultMap.put("total", pageInfo.getTotal()); // 总记录数，例如 102

        return ServerResponse.createServerResponseBySuccess(resultMap);
    }

    /**
     * 模糊查询用户信息
     * 根据关键字搜索用户昵称、手机号等字段
     * @param keyword 搜索关键字
     * @return ServerResponse 包含匹配用户列表的响应对象
     */
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

    /**
     * 批量重置用户密码
     * 将指定用户的密码重置为默认密码
     * @param ids 用户ID字符串，多个ID用逗号分隔
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse resetPassword(String ids) {
        // 参数有效性检查
        if (ids == null || ids.isEmpty()) {
            return ServerResponse.createServerResponseByFail(500,"ID不能为空");
        }

        // 将逗号分隔的ID字符串转换为Integer列表
        List<Integer> idList = Stream.of(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        
        // 设置默认密码并进行加密
        String pwd="a12345678";
        String hashedPassword = Util.encryptPwd(pwd);
        
        // 执行批量密码重置操作
        int result = adminMapper.batchResetPassword(idList, hashedPassword);

        // 根据操作结果返回相应响应
        if (result > 0) {
            return ServerResponse.createServerResponseBySuccess("成功重置 " + result + " 个账号的密码");
        }
        return ServerResponse.createServerResponseByFail(500,"重置密码失败");
    }

    /**
     * 分页查询失物招领信息
     * 支持关键字、类型、状态等多条件筛选
     * @param page 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @param type 信息类型
     * @param state 信息状态
     * @return ServerResponse 包含信息列表和分页信息的响应对象
     */
    @Override
    public ServerResponse getLostFoundByPage(int page, int pageSize, String keyword, String type, String state){
        // 计算分页偏移量
        int offset = (page - 1) * pageSize;
        
        // 执行分页查询，支持多条件筛选
        List<LostFound> list = adminMapper.getLostFoundByPage(offset, pageSize, keyword, type, state);
        
        // 获取符合条件的总记录数
        int total = adminMapper.getLostFoundCountByCondition(keyword,type,state);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);           // 当前页数据列表
        result.put("total", total);         // 总记录数
        result.put("totalPages", (int) Math.ceil((double) total / pageSize)); // 总页数
        
        return ServerResponse.createServerResponseBySuccess(result);
    }

    /**
     * 更新信息置顶状态
     * 控制失物招领信息在首页的置顶显示
     * @param id 信息ID
     * @param stick 置顶状态（1-置顶，0-取消置顶）
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse updateStickStatus(int id,int stick) {
        if(adminMapper.updateStickStatus(id, stick)){
            return ServerResponse.createServerResponseBySuccess("置顶成功");
        }
        return ServerResponse.createServerResponseByFail("置顶失败");
    }

    /**
     * 分页查询评论信息
     */
    @Override
    public ServerResponse getCommentsByPage(int page, int pageSize, String keyword, String state) {
        // 使用 PageHelper 开启分页
        PageHelper.startPage(page, pageSize);

        // 执行联表多条件查询
        List<CommentVO> list = adminMapper.getCommentsByCondition(keyword, state);

        // 封装分页结果
        PageInfo<CommentVO> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("totalPages", pageInfo.getPages());

        return ServerResponse.createServerResponseBySuccess(result);
    }

    /**
     * 更新评论状态
     */
    @Override
    public ServerResponse updateCommentStatus(Integer commentId, int state, String reason) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setState(state);

        // 记录或清空驳回原因
        if ("已驳回".equals(state)) {
            comment.setRejectReason(reason);
        } else if ("已通过".equals(state)) {
            comment.setRejectReason("");
        }

        int rows = adminMapper.updateCommentSelective(comment);
        if (rows > 0) {
            return ServerResponse.createServerResponseBySuccess("操作成功");
        }
        return ServerResponse.createServerResponseByFail(500, "操作失败，可能数据不存在");
    }

    /**
     * 删除评论
     */
    @Override
    public ServerResponse deleteCommentById(Integer commentId) {
        int rows = adminMapper.deleteCommentById(commentId);
        if (rows > 0) {
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        return ServerResponse.createServerResponseByFail(500, "删除失败，可能数据不存在");
    }
}
