package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.school.entity.LostFound;
import com.school.entity.LostFoundType;
import com.school.entity.PointHistory;
import com.school.mapper.LostFoundMapper;
import com.school.mapper.LostFoundTypeMapper;
import com.school.mapper.PointHistoryMapper;
import com.school.mapper.UserMapper;
import com.school.services.api.LostFoundService;
import com.school.services.api.LostFoundTypeService;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class LostFoundServiceImpl implements LostFoundService {
    @Autowired
    private LostFoundMapper lostFoundMapper;
    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Util util;
    @Autowired
    private LostFoundTypeMapper lostFoundTypeMapper;
    @Autowired
    private LostFoundTypeService lostFoundTypeService;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;


    @Override
    public ServerResponse getLostFoundDetail(int lostfoundtypeId,String type) {

        List<LostFound> lostFoundList = lostFoundMapper.selectByTypeId(lostfoundtypeId,type);
        

        if (lostFoundList == null) {
            return ServerResponse.createServerResponseByFail( "数据为空");
        }
        

        for (LostFound lostFound : lostFoundList) {

            Integer userId = lostFound.getUserId();
            

            String userNameId = lostFoundMapper.searchUserNameId(userId);
            

            lostFound.setNickname(userNameId);
            

            String updatePic = util.updatePic(lostFound.getImg());
            lostFound.setImg(updatePic);
        }

        return ServerResponse.createServerResponseBySuccess(lostFoundList);
    }


    @Override
    public ServerResponse getUserName(int id) {
        String userName = lostFoundMapper.searchUserNameId(id);
        if (userName == null) {
            return ServerResponse.createServerResponseByFail( "数据为空");
        }
        return ServerResponse.createServerResponseBySuccess(userName);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse addLostFound(String lostfoundJson) {

        LostFound lostFound = JSON.parseObject(lostfoundJson, LostFound.class);

        if(sensitiveWordUtil.contains(lostFound.getContent())||sensitiveWordUtil.contains(lostFound.getTitle())||sensitiveWordUtil.contains(lostFound.getPlace())){
            lostFound.setState("已驳回");
            Integer userId = lostFound.getUserId();
            lostFoundMapper.addLostFound(lostFound);

            userMapper.deductPoints(lostFound.getUserId(),50 );

            PointHistory history = new PointHistory();
            history.setUser_id(userId);
            history.setType(4);
            history.setPoints_changed(-50);
            history.setDescription("发布违规信息");
            pointHistoryMapper.insert(history);
            return ServerResponse.createServerResponseByFail("内容包含敏感词，扣除50积分，请重新发布内容");
        }


        if (lostFoundMapper.addLostFound(lostFound)) {
            String targetType = lostFound.getType().equals("失物") ? "招领" : "失物";

            String keyword = lostFound.getTitle();
            List<LostFound> matchedList = lostFoundMapper.smartMatch(targetType, lostFound.getLostfoundtypeId(), keyword);
            if (matchedList != null && !matchedList.isEmpty()) {

                for (LostFound lostFoundData : matchedList) {
                    lostFoundData.setImg(util.updatePic(lostFoundData.getImg()));
                }
                return ServerResponse.createServerResponseBySuccess(matchedList,"发布成功，发现疑似匹配物品！");
            }
            return ServerResponse.createServerResponseBySuccess("信息发布成功");
        } else {
            return ServerResponse.createServerResponseByFail("信息发布失败");
        }
    }



    @Override
    public ServerResponse getLostFoundByUserId(int user_id) {

        List<LostFound> allByIdLostFoundList = lostFoundMapper.getLostFoundListById(user_id);
        

        if (allByIdLostFoundList.size() == 0) {
            return ServerResponse.createServerResponseBySuccess("还未发布任何信息");
        } else {

            for (LostFound lostFound : allByIdLostFoundList) {

                Integer userId = lostFound.getUserId();
                

                String userNameId = lostFoundMapper.searchUserNameId(userId);
                

                lostFound.setNickname(userNameId);
                

                String updatePic = util.updatePic(lostFound.getImg());
                lostFound.setImg(updatePic);
            }
        }
        return ServerResponse.createServerResponseBySuccess(allByIdLostFoundList, "获取失物信息列表成功");
    }


    @Override
    public ServerResponse updateState(int id, String state, int user_id) {
        if (lostFoundMapper.updateState(id, state)) {
            return ServerResponse.createServerResponseBySuccess( "状态已更改");
        }
        return ServerResponse.createServerResponseByFail("状态更改失败");
    }


    @Override
    public ServerResponse showTopList(Integer stick) {
        List<LostFound> lists = lostFoundMapper.showTopList(stick);
        if (lists.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess("无置顶信息");
        }

        for (LostFound lostFound : lists) {

            Integer userId = lostFound.getUserId();

            Integer lostfoundtypeId = lostFound.getLostfoundtypeId();

            String userNameId = lostFoundMapper.searchUserNameId(userId);

            lostFound.setNickname(userNameId);

            List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
            for (LostFoundType type : lostFoundTypes) {
                if (Objects.equals(type.getId(), lostfoundtypeId)) {
                    lostFound.setLostfoundtype(type);
                }
            }

            String img = lostFound.getImg();
            String updatePic = util.updatePic(img);
            lostFound.setImg(updatePic);
        }
        return ServerResponse.createServerResponseBySuccess(lists, "置顶信息");
    }


    @Override
    public ServerResponse getInfoByKey(String keyword) {
        List<com.school.entity.LostFound> list = lostFoundMapper.selectAllInfo(keyword);
        return ServerResponse.createServerResponseBySuccess(list);
    }


    @Override
    public ServerResponse getLostFoundById(Integer id) {
        LostFound lostFound = lostFoundMapper.selectByPrimaryKey(id);
        if (lostFound == null) return ServerResponse.createServerResponseByFail("信息不存在");
        return ServerResponse.createServerResponseBySuccess(lostFound);
    }


    @Override
    public ServerResponse deleteLostFoundById(Integer id) {

        LostFound lostFound = lostFoundMapper.selectByPrimaryKey(id);
        if (lostFound.getType().equals("招领")&lostFound.getState().equals("待认领"))
        {userMapper.addPoints(lostFound.getUserId(), 10);
        recordPointHistory(lostFound.getUserId(), 2, 10, "招领帖子被删除，退还积分");
        }

        int result = lostFoundMapper.deleteByPrimaryKey(id);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse updateLostFoundStatus(Integer lostFoundId, String state) {

        LostFound lostFound = lostFoundMapper.selectByPrimaryKey(lostFoundId);
        if (lostFound == null) {
            return ServerResponse.createServerResponseByFail("记录不存在");
        }

        String oldState = lostFound.getState();


        if (state != null && state.equals(oldState)) {
            return ServerResponse.createServerResponseBySuccess("操作成功");
        }

        String type = lostFound.getType();
        Integer userId = lostFound.getUserId();


        lostFoundMapper.updateState(lostFoundId, state);


        if (userId != null) {
            int pointsChange = 0;
            int historyType = 0;
            String historyDesc = null;


            if ("已驳回".equals(state) && !"已驳回".equals(oldState)) {
                pointsChange = -50;
                historyType = 4;
                historyDesc = "发布违规信息被驳回扣除";
            }


            else if ("已驳回".equals(oldState) && !"已驳回".equals(state)) {
                pointsChange = 50;
                historyType = 2;
                historyDesc = "驳回撤销，退还积分";
            }


            else if ("待审核".equals(oldState) && "待认领".equals(state) && "招领".equals(type)) {
                pointsChange = 10;
                historyType = 1;
                historyDesc = "招领帖子审核通过奖励";
            }


            if (pointsChange != 0) {
                userMapper.addPoints(userId, pointsChange);
                recordPointHistory(userId, historyType, pointsChange, historyDesc);
            }
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
    public ServerResponse getDetailByTitle(String title,String type) {
        int id = (int) lostFoundTypeService.getIdByName(title).getData();
        return ServerResponse.createServerResponseBySuccess(getLostFoundDetail(id,type).getData());
    }


}
