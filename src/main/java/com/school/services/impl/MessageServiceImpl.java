package com.school.services.impl;

import com.school.entity.Message;
import com.school.entity.MessageVO;
import com.school.mapper.MessageMapper;
import com.school.services.api.MessageService;
import com.school.utils.DateUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private Util util;

    public ServerResponse addMessage(Integer userId, String content, Integer parentId) {
        if (content == null || content.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("留言内容不能为空");
        }

        Date now = DateUtil.getTime();
        // 如果 parentId 为空，设为 0 作为主楼标识
        Integer pId = (parentId == null) ? 0 : parentId;
        Message msg = new Message(userId, content, now, 1, pId);

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
}