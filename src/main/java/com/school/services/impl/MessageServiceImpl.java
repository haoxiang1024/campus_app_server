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

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private Util util;

    @Override
    public ServerResponse addMessage(Integer userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            return ServerResponse.createServerResponseByFail("留言内容不能为空");
        }

        // 创建留言对象，默认状态 1 (正常)
        Date now = DateUtil.getTime();
        Message msg = new Message(userId, content, now, 1);

        int result = messageMapper.insertMessage(msg);
        if (result > 0) {
            return ServerResponse.createServerResponseBySuccess("留言发布成功");
        }
        return ServerResponse.createServerResponseByFail("留言发布失败，请稍后重试");
    }

    @Override
    public ServerResponse getMessageList() {
        List<MessageVO> list = messageMapper.getMessageList();
        if (list != null && !list.isEmpty()) {
            for (MessageVO vo : list) {
                if (vo.getPhoto() != null && !vo.getPhoto().isEmpty()) {
                    String fullPhotoUrl = util.updatePic(vo.getPhoto());
                    vo.setPhoto(fullPhotoUrl);
                }
            }
        }

        return ServerResponse.createServerResponseBySuccess(list, "获取留言成功");
    }
}