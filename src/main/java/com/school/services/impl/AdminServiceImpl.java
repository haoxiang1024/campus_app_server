package com.school.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.*;
import com.school.mapper.AdminMapper;
import com.school.mapper.LostFoundMapper;
import com.school.mapper.LostFoundTypeMapper;
import com.school.services.api.AdminService;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;
    @Autowired
    private LostFoundMapper lostFoundMapper;
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
        resultMap.put("total", pageInfo.getTotal()); // 总记录数

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
    public ServerResponse updateCommentStatus(Integer commentId, int state) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setState(state);
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


    /**
     * 分页获取类型列表
     * @param page 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @return ServerResponse 包含类型列表和分页信息的响应对象
     */
    @Override
    public ServerResponse getTypeByPage(int page, int pageSize, String keyword) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        // 根据关键字查询类型列表
        List<LostFoundType> list = lostFoundTypeMapper.selectTypeByPage(keyword);
        // 构建分页信息对象
        PageInfo<LostFoundType> pageInfo = new PageInfo<>(list);
    
        // 封装返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("totalPages", pageInfo.getPages());
    
        return ServerResponse.createServerResponseBySuccess(resultMap);
    }
    
    /**
     * 添加新类型
     * @param name 类型名称
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse addType(String name) {
        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("分类名称不能为空");
        }
        // 创建类型对象并设置名称
        LostFoundType type = new LostFoundType();
        type.setName(name);
        // 执行插入操作
        int result = lostFoundTypeMapper.insertType(type);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("新增成功") : ServerResponse.createServerResponseByFail("新增失败");
    }
    
    /**
     * 更新类型信息
     * @param id 类型 ID
     * @param name 新的类型名称
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse updateType(Integer id, String name) {
        // 参数校验
        if (id == null || name == null || name.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("参数错误");
        }
        // 创建类型对象并设置 ID 和名称
        LostFoundType type = new LostFoundType();
        type.setId(id);
        type.setName(name);
        // 执行更新操作
        int result = lostFoundTypeMapper.updateType(type);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("更新成功") : ServerResponse.createServerResponseByFail("更新失败");
    }
    
    /**
     * 根据 ID 删除类型
     * @param typeId 类型 ID
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse deleteTypeById(Integer typeId) {
        if (typeId == null) {
            return ServerResponse.createServerResponseByFail("分类ID不能为空");
        }

        // 检查是否还有失物招领信息正在使用该分类
        int count = lostFoundMapper.countByTypeId(typeId);
        if (count > 0) {
            return ServerResponse.createServerResponseByFail("删除被拦截：当前有 " + count + " 条物品信息属于该分类，请先转移或删除相关物品！");
        }

        int result = lostFoundTypeMapper.deleteTypeById(typeId);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");
    }

    /**
     * 批量删除类型
     * @param ids 类型 ID 字符串，多个 ID 用逗号分隔
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse deleteTypeBatch(String ids) {
        // 参数校验
        if (ids == null || ids.isEmpty()) {
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }
    
        // 解析 ID 字符串，转换为整数列表
        String[] idArray = ids.split(",");
        List<Integer> idList = new ArrayList<>();
    
        // 遍历检查每一个准备删除的分类
        for (String idStr : idArray) {
            Integer typeId = Integer.parseInt(idStr);
            // 统计该分类下的物品数量
            int count = lostFoundMapper.countByTypeId(typeId);
    
            // 如果分类仍在使用中，则拦截删除操作
            if (count > 0) {
                // 只要发现其中有一个分类还在被使用，就直接熔断整个批量删除操作
                return ServerResponse.createServerResponseByFail("批量删除被拦截：分类 ID [" + typeId + "] 下仍有 " + count + " 条关联的物品信息，请先处理！");
            }
            idList.add(typeId);
        }
    
        // 执行批量删除操作
        int result = lostFoundTypeMapper.deleteTypeBatch(idList);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("批量删除成功") : ServerResponse.createServerResponseByFail("批量删除失败");
    }

    /**
     * 分页获取用户列表 (包含关键字和状态筛选)
     */
    @Override
    public ServerResponse getUserListByPage(int page, int pageSize, String keyword, Integer state, Integer role) {
        // 开启分页插件
        PageHelper.startPage(page, pageSize);

        // 执行多条件联合查询
        List<User> list = adminMapper.getUserListByCondition(keyword, state, role);

        // 封装分页信息
        PageInfo<User> pageInfo = new PageInfo<>(list);

        // 构建前端要求的数据格式
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());       // 当前页数据
        result.put("total", pageInfo.getTotal());     // 真实总记录数
        result.put("listSize", pageInfo.getList().size()); // 当前页数量
        result.put("total_if", pageInfo.getTotal());  // 搜索后的总记录数

        return ServerResponse.createServerResponseBySuccess(result);
    }
}
