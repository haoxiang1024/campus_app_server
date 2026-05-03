package com.school.services.impl;

import com.school.entity.Message;
import com.school.entity.MessageVO;
import com.school.entity.PointHistory;
import com.school.mapper.MessageMapper;
import com.school.mapper.PointHistoryMapper;
import com.school.mapper.UserMapper;
import com.school.services.api.MessageService;
import com.school.utils.DateUtil;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private Util util;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse addMessage(Integer userId, String content, Integer parentId, Integer replyUserId) {
        if (content == null || content.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("留言内容不能为空");
        }

        Date now = DateUtil.getTime();
        Integer pId = (parentId == null) ? 0 : parentId;

        // 传入 replyUserId
        //敏感词检测
        if(sensitiveWordUtil.contains(content)){
            Message msg = new Message(userId, content, now, 2, pId, replyUserId);//驳回
            messageMapper.insertMessage(msg);
            //扣除积分
            userMapper.deductPoints(msg.getUserId(),50 );
            //记录积分流水
            PointHistory history = new PointHistory();
            history.setUser_id(userId);
            history.setType(4); //系统扣除
            history.setPoints_changed(-50);
            history.setDescription("发布违规留言");
            pointHistoryMapper.insert(history);

            return ServerResponse.createServerResponseByFail("留言包含敏感词，扣除50积分，请重新发布内容");
        }
        Message msg = new Message(userId, content, now, 1, pId, replyUserId);//审核
        int result = messageMapper.insertMessage(msg);
        if (result > 0) {
            return ServerResponse.createServerResponseBySuccess("留言发布成功");
        }
        return ServerResponse.createServerResponseByFail("留言发布失败，请稍后重试");
    }

    @Override
    public ServerResponse getMessageList() {
        List<MessageVO> allList = messageMapper.getMessageList();
        if (allList == null || allList.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(new ArrayList<>(), "获取留言成功");
        }

        List<MessageVO> rootMessages = new ArrayList<>();
        List<MessageVO> replyMessages = new ArrayList<>();

        for (MessageVO vo : allList) {
            if (vo.getPhoto() != null && !vo.getPhoto().isEmpty()) {
                vo.setPhoto(util.updatePic(vo.getPhoto()));
            }

            if (vo.getParentId() == null || vo.getParentId() == 0) {
                rootMessages.add(vo); // 收集主楼
            } else {
                replyMessages.add(vo); // 收集评论
            }
        }

        Map<Integer, List<MessageVO>> replyMap = replyMessages.stream()
                .collect(Collectors.groupingBy(MessageVO::getParentId));

        for (MessageVO root : rootMessages) {
            List<MessageVO> replies = replyMap.getOrDefault(root.getId(), new ArrayList<>());
            root.setReplies(replies);
        }

        return ServerResponse.createServerResponseBySuccess(rootMessages, "获取留言成功");
    }

    @Override
    public ServerResponse getAdminMessagePage(int page, int pageSize, String keyword, Integer state) {
        int offset = (page - 1) * pageSize;

        // 查询总数
        int total = messageMapper.countAdminMessages(keyword, state);

        // 查询当前页数据
        List<MessageVO> list = messageMapper.getAdminMessageList(offset, pageSize, keyword, state);

        // 计算总页数
        int totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages == 0) totalPages = 1;
        // 封装前端需要的分页对象
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("totalPages", totalPages);

        return ServerResponse.createServerResponseBySuccess(data, "获取成功");
    }
    // 提取积分流水记录方法
    private void recordPointHistory(Integer userId, int historyType, int pointsChange, String description) {
        PointHistory history = new PointHistory();
        history.setUser_id(userId);
        history.setType(historyType);
        history.setPoints_changed(pointsChange);
        history.setDescription(description);
        pointHistoryMapper.insert(history);
    }
    @Override
    public ServerResponse updateCommentStatus(Integer commentId, Integer state) {
        Message message = messageMapper.getMessageById(commentId);
        int oldState = message.getState();  // 获取当前状态
        int newState = state;  // 要更新的目标状态
        int row = messageMapper.updateMessageState(commentId, state);
        if (row > 0) {
            // 处理积分
            if (newState == 2 && oldState != 2) {
                // 驳回，扣除50积分
                userMapper.addPoints(message.getUserId(), -50);
                recordPointHistory(message.getUserId(), 4, 50, "留言被驳回，扣除用户积分");
            } else if (newState != 2 && oldState == 2) {
                userMapper.addPoints(message.getUserId(), 50);
                recordPointHistory(message.getUserId(), 2, 50, "留言被撤销驳回，归还用户积分");
            }
            return ServerResponse.createServerResponseBySuccess("状态更新成功");
        }
        return ServerResponse.createServerResponseByFail("更新失败");
    }

    @Override
    public ServerResponse deleteCommentById(Integer commentId) {
        int row = messageMapper.deleteMessageById(commentId);
        if (row > 0) {
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        return ServerResponse.createServerResponseByFail("删除失败");
    }

    @Override
    public ServerResponse getMessagesByUserId(Integer userId) {
        if (userId == null) {
            return ServerResponse.createServerResponseByFail("用户ID不能为空");
        }

        List<MessageVO> list = messageMapper.getMessagesByUserId(userId);

        if (list == null || list.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess(new ArrayList<>(), "暂无留言");
        }

        // 处理头像图片路径
        for (MessageVO vo : list) {
            if (vo.getPhoto() != null && !vo.getPhoto().isEmpty()) {
                vo.setPhoto(util.updatePic(vo.getPhoto()));
            }
        }

        return ServerResponse.createServerResponseBySuccess(list, "获取留言成功");
    }
}