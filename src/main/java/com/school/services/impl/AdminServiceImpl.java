package com.school.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.*;
import com.school.mapper.*;
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


@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;
    @Autowired
    private LostFoundMapper lostFoundMapper;
    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public ServerResponse getAllUser() {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllUserCount());
    }


    @Override
    public ServerResponse getAllLostFoundCount(String type) {
        return ServerResponse.createServerResponseBySuccess(adminMapper.getAllLostFoundCount(type));
    }


    @Override
    public ServerResponse getAllUserInfo(int page, int size) {

        PageHelper.startPage(page, size);
        

        List<User> list = adminMapper.getAllUserInfo();
        

        PageInfo<User> pageInfo = new PageInfo<>(list);
        

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());

        return ServerResponse.createServerResponseBySuccess(resultMap);
    }


    @Override
    public ServerResponse searchUsers(String keyword) {

        if (StringUtils.isBlank(keyword)) {
            return ServerResponse.createServerResponseByFail(500,"搜索关键字不能为空");
        }


        List<User> list = adminMapper.searchUsers(keyword);


        return ServerResponse.createServerResponseBySuccess(list);
    }


    @Override
    public ServerResponse resetPassword(String ids) {

        if (ids == null || ids.isEmpty()) {
            return ServerResponse.createServerResponseByFail(500,"ID不能为空");
        }


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


    @Override
    public ServerResponse getLostFoundByPage(int page, int pageSize, String keyword, String type, String state){

        int offset = (page - 1) * pageSize;
        

        List<LostFound> list = adminMapper.getLostFoundByPage(offset, pageSize, keyword, type, state);
        

        int total = adminMapper.getLostFoundCountByCondition(keyword,type,state);
        

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("totalPages", (int) Math.ceil((double) total / pageSize));
        
        return ServerResponse.createServerResponseBySuccess(result);
    }


    @Override
    public ServerResponse updateStickStatus(int id,int stick) {
        if(adminMapper.updateStickStatus(id, stick)){
            return ServerResponse.createServerResponseBySuccess("置顶成功");
        }
        return ServerResponse.createServerResponseByFail("置顶失败");
    }


    @Override
    public ServerResponse getCommentsByPage(int page, int pageSize, String keyword, String state) {

        PageHelper.startPage(page, pageSize);


        List<CommentVO> list = adminMapper.getCommentsByCondition(keyword, state);


        PageInfo<CommentVO> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("totalPages", pageInfo.getPages());

        return ServerResponse.createServerResponseBySuccess(result);
    }


    @Override
    public ServerResponse updateCommentStatus(Integer commentId, int state) {

        Comment comment = commentMapper.getCommentById(commentId);
        if (comment == null) {
            return ServerResponse.createServerResponseByFail(404, "评论不存在");
        }

        Integer userId = comment.getUser_id();
        Integer oldState = comment.getState();


        comment.setState(state);
        int rows = adminMapper.updateCommentSelective(comment);

        if (rows <= 0) {
            return ServerResponse.createServerResponseByFail(500, "操作失败，可能数据不存在");
        }



        if ((oldState == 0 || oldState == 1) && state == 2) {

            userMapper.addPoints(userId, -50);
            recordPointHistory(userId, 4, -50, "发布违规评论被驳回");
            return ServerResponse.createServerResponseBySuccess("操作成功");
        }


        if (oldState == 2 && state == 1) {

            userMapper.addPoints(userId, 50);
            recordPointHistory(userId, 2, 50, "撤销误驳回，返还积分");
            return ServerResponse.createServerResponseBySuccess("操作成功");
        }

        return ServerResponse.createServerResponseBySuccess("操作成功");
    }

    private void recordPointHistory(Integer userId, int historyType, int pointsChange, String description) {
        PointHistory history = new PointHistory();
        history.setUser_id(userId);
        history.setType(historyType);
        history.setPoints_changed(pointsChange);
        history.setDescription(description);
        pointHistoryMapper.insert(history);
    }

    @Override
    public ServerResponse deleteCommentById(Integer commentId) {

        Comment comment = commentMapper.getCommentById(commentId);
        userMapper.addPoints(comment.getUser_id(), -50);
        recordPointHistory(comment.getUser_id(), 4, -50, "删除违规评论");
        int rows = adminMapper.deleteCommentById(commentId);
        if (rows > 0) {
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        return ServerResponse.createServerResponseByFail(500, "删除失败，可能数据不存在");
    }



    @Override
    public ServerResponse getTypeByPage(int page, int pageSize, String keyword) {

        PageHelper.startPage(page, pageSize);

        List<LostFoundType> list = lostFoundTypeMapper.selectTypeByPage(keyword);

        PageInfo<LostFoundType> pageInfo = new PageInfo<>(list);
    

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("totalPages", pageInfo.getPages());
    
        return ServerResponse.createServerResponseBySuccess(resultMap);
    }
    

    @Override
    public ServerResponse addType(String name) {

        if (name == null || name.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("分类名称不能为空");
        }

        LostFoundType type = new LostFoundType();
        type.setName(name);

        int result = lostFoundTypeMapper.insertType(type);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("新增成功") : ServerResponse.createServerResponseByFail("新增失败");
    }
    

    @Override
    public ServerResponse updateType(Integer id, String name) {

        if (id == null || name == null || name.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("参数错误");
        }

        LostFoundType type = new LostFoundType();
        type.setId(id);
        type.setName(name);

        int result = lostFoundTypeMapper.updateType(type);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("更新成功") : ServerResponse.createServerResponseByFail("更新失败");
    }
    

    @Override
    public ServerResponse deleteTypeById(Integer typeId) {
        if (typeId == null) {
            return ServerResponse.createServerResponseByFail("分类ID不能为空");
        }


        int count = lostFoundMapper.countByTypeId(typeId);
        if (count > 0) {
            return ServerResponse.createServerResponseByFail("删除被拦截：当前有 " + count + " 条物品信息属于该分类，请先转移或删除相关物品！");
        }

        int result = lostFoundTypeMapper.deleteTypeById(typeId);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");
    }


    @Override
    public ServerResponse deleteTypeBatch(String ids) {

        if (ids == null || ids.isEmpty()) {
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }
    

        String[] idArray = ids.split(",");
        List<Integer> idList = new ArrayList<>();
    

        for (String idStr : idArray) {
            Integer typeId = Integer.parseInt(idStr);

            int count = lostFoundMapper.countByTypeId(typeId);
    

            if (count > 0) {

                return ServerResponse.createServerResponseByFail("批量删除被拦截：分类 ID [" + typeId + "] 下仍有 " + count + " 条关联的物品信息，请先处理！");
            }
            idList.add(typeId);
        }
    

        int result = lostFoundTypeMapper.deleteTypeBatch(idList);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("批量删除成功") : ServerResponse.createServerResponseByFail("批量删除失败");
    }


    @Override
    public ServerResponse getUserListByPage(int page, int pageSize, String keyword, Integer state, Integer role) {

        PageHelper.startPage(page, pageSize);


        List<User> list = adminMapper.getUserListByCondition(keyword, state, role);


        PageInfo<User> pageInfo = new PageInfo<>(list);


        Map<String, Object> result = new HashMap<>();
        result.put("list", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("listSize", pageInfo.getList().size());
        result.put("total_if", pageInfo.getTotal());

        return ServerResponse.createServerResponseBySuccess(result);
    }
}
