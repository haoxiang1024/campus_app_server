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

/**
 * 失物招领服务实现类
 * 实现失物招领信息管理的核心业务逻辑
 */
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

    /**
     * 获取物品的详细信息
     * 根据分类ID和类型获取对应的失物招领信息列表，并补充用户信息和图片路径
     * @param lostfoundtypeId 物品分类ID
     * @param type 信息类型（失物/招领）
     * @return ServerResponse 包含物品详细信息列表的响应对象
     */
    @Override
    public ServerResponse getLostFoundDetail(int lostfoundtypeId,String type) {
        // 根据分类ID和类型查询失物招领信息列表
        List<LostFound> lostFoundList = lostFoundMapper.selectByTypeId(lostfoundtypeId,type);
        
        // 检查查询结果是否为空
        if (lostFoundList == null) {
            return ServerResponse.createServerResponseByFail( "数据为空");
        }
        
        // 为每个失物招领信息补充关联的用户信息和完整图片路径
        for (LostFound lostFound : lostFoundList) {
            // 获取发布该信息的用户ID
            Integer userId = lostFound.getUserId();
            
            // 根据用户ID查询用户名
            String userNameId = lostFoundMapper.searchUserNameId(userId);
            
            // 设置用户名到信息对象中
            lostFound.setNickname(userNameId);
            
            // 处理图片路径，转换为完整的URL
            String updatePic = util.updatePic(lostFound.getImg());
            lostFound.setImg(updatePic);
        }

        return ServerResponse.createServerResponseBySuccess(lostFoundList);
    }

    /**
     * 根据用户ID获取用户名
     * @param id 用户ID
     * @return ServerResponse 包含用户名的响应对象
     */
    @Override
    public ServerResponse getUserName(int id) {
        String userName = lostFoundMapper.searchUserNameId(id);
        if (userName == null) {
            return ServerResponse.createServerResponseByFail( "数据为空");
        }
        return ServerResponse.createServerResponseBySuccess(userName);
    }

    /**
     * 添加失物招领信息（JSON格式）
     * 将JSON字符串解析为LostFound对象并保存到数据库
     * @param lostfoundJson 失物招领信息JSON字符串
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse addLostFound(String lostfoundJson) {
        // 将JSON字符串解析为LostFound实体对象
        LostFound lostFound = JSON.parseObject(lostfoundJson, LostFound.class);
        //敏感词检测
        if(sensitiveWordUtil.contains(lostFound.getContent())||sensitiveWordUtil.contains(lostFound.getTitle())||sensitiveWordUtil.contains(lostFound.getPlace())){
            lostFound.setState("已驳回");
            Integer userId = lostFound.getUserId();
            lostFoundMapper.addLostFound(lostFound);
            //扣除积分
            userMapper.deductPoints(lostFound.getUserId(),50 );
            //记录积分流水
            PointHistory history = new PointHistory();
            history.setUser_id(userId);
            history.setType(4); //系统扣除
            history.setPoints_changed(-50);
            history.setDescription("发布违规信息");
            pointHistoryMapper.insert(history);
            return ServerResponse.createServerResponseByFail("内容包含敏感词，扣除50积分，请重新发布内容");
        }

        // 调用数据访问层保存失物招领信息
        if (lostFoundMapper.addLostFound(lostFound)) {
            String targetType = lostFound.getType().equals("失物") ? "招领" : "失物";
            // 提取标题作为关键字
            String keyword = lostFound.getTitle();
            List<LostFound> matchedList = lostFoundMapper.smartMatch(targetType, lostFound.getLostfoundtypeId(), keyword);
            if (matchedList != null && !matchedList.isEmpty()) {
                // 如果匹配到了，把列表放进 data 字段返回给前端
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


    /**
     * 根据用户ID获取用户发布的失物招领信息
     * 补充用户昵称和图片完整路径信息
     * @param user_id 用户ID
     * @return ServerResponse 包含用户发布信息列表的响应对象
     */
    @Override
    public ServerResponse getLostFoundByUserId(int user_id) {
        // 查询指定用户发布的所有失物招领信息
        List<LostFound> allByIdLostFoundList = lostFoundMapper.getLostFoundListById(user_id);
        
        // 检查用户是否发布过信息
        if (allByIdLostFoundList.size() == 0) {
            return ServerResponse.createServerResponseBySuccess("还未发布任何信息");
        } else {
            // 为每条信息补充完整的用户信息和图片路径
            for (LostFound lostFound : allByIdLostFoundList) {
                // 获取发布信息的用户ID
                Integer userId = lostFound.getUserId();
                
                // 查询用户名
                String userNameId = lostFoundMapper.searchUserNameId(userId);
                
                // 设置用户名
                lostFound.setNickname(userNameId);
                
                // 处理图片路径，转换为完整URL
                String updatePic = util.updatePic(lostFound.getImg());
                lostFound.setImg(updatePic);
            }
        }
        return ServerResponse.createServerResponseBySuccess(allByIdLostFoundList, "获取失物信息列表成功");
    }

    /**
     * 用户自己修改信息发布状态
     * @param id 信息ID
     * @param state 目标状态
     * @param user_id 用户ID
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse updateState(int id, String state, int user_id) {
        if (lostFoundMapper.updateState(id, state)) {
            return ServerResponse.createServerResponseBySuccess( "状态已更改");
        }
        return ServerResponse.createServerResponseByFail("状态更改失败");
    }

    /**
     * 显示置顶信息列表
     * 获取所有置顶的失物招领信息，并补充完整的用户和分类信息
     * @param stick 置顶标识（1表示置顶）
     * @return ServerResponse 包含置顶信息列表的响应对象
     */
    @Override
    public ServerResponse showTopList(Integer stick) {
        List<LostFound> lists = lostFoundMapper.showTopList(stick);
        if (lists.isEmpty()) {
            return ServerResponse.createServerResponseBySuccess("无置顶信息");
        }
        //设置用户名
        for (LostFound lostFound : lists) {
            //获取用户id
            Integer userId = lostFound.getUserId();
            //获取分类id
            Integer lostfoundtypeId = lostFound.getLostfoundtypeId();
            //根据用户id获取用户名
            String userNameId = lostFoundMapper.searchUserNameId(userId);
            //设置用户名
            lostFound.setNickname(userNameId);
            //设置分类
            List<LostFoundType> lostFoundTypes = lostFoundTypeMapper.GetAll();
            for (LostFoundType type : lostFoundTypes) {
                if (Objects.equals(type.getId(), lostfoundtypeId)) {
                    lostFound.setLostfoundtype(type);
                }
            }
            //设置图片
            String img = lostFound.getImg();
            String updatePic = util.updatePic(img);
            lostFound.setImg(updatePic);
        }
        return ServerResponse.createServerResponseBySuccess(lists, "置顶信息");
    }

    /**
     * 根据关键字模糊查询失物招领信息
     * 在标题、描述等字段中进行模糊匹配
     * @param keyword 搜索关键字
     * @return ServerResponse 包含匹配信息列表的响应对象
     */
    @Override
    public ServerResponse getInfoByKey(String keyword) {
        List<com.school.entity.LostFound> list = lostFoundMapper.selectAllInfo(keyword);
        return ServerResponse.createServerResponseBySuccess(list);
    }

    /**
     * 根据ID获取失物招领信息详情
     * @param id 信息ID
     * @return ServerResponse 包含信息详情的响应对象
     */
    @Override
    public ServerResponse getLostFoundById(Integer id) {
        LostFound lostFound = lostFoundMapper.selectByPrimaryKey(id);
        if (lostFound == null) return ServerResponse.createServerResponseByFail("信息不存在");
        return ServerResponse.createServerResponseBySuccess(lostFound);
    }

    /**
     * 根据ID删除失物招领信息
     * @param id 信息ID
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse deleteLostFoundById(Integer id) {
        //退还用户积分
        LostFound lostFound = lostFoundMapper.selectByPrimaryKey(id);
        if (lostFound.getType().equals("招领")&lostFound.getState().equals("待认领"))
        {
            userMapper.addPoints(lostFound.getUserId(), 10);
        recordPointHistory(lostFound.getUserId(), 2, 10, "招领帖子被删除，退还积分");
        }
        //执行删除
        int result = lostFoundMapper.deleteByPrimaryKey(id);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");
    }

    /**
     * 更新失物招领状态（包含审核通过加积分逻辑）
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务
    public ServerResponse updateLostFoundStatus(Integer lostFoundId, String state) {
        // 查出数据库中原本的记录
        LostFound lostFound = lostFoundMapper.selectByPrimaryKey(lostFoundId);
        if (lostFound == null) {
            return ServerResponse.createServerResponseByFail("记录不存在");
        }

        String oldState = lostFound.getState();

        // 状态未改变直接返回
        if (state != null && state.equals(oldState)) {
            return ServerResponse.createServerResponseBySuccess("操作成功");
        }

        String type = lostFound.getType();
        Integer userId = lostFound.getUserId();

        // 更新帖子状态
        lostFoundMapper.updateState(lostFoundId, state);

        // 只有当存在发布人时，才进行积分结算
        if (userId != null) {
            int pointsChange = 0;
            int historyType = 0;
            String historyDesc = null;

            //  驳回操作（扣除50分）
            if ("已驳回".equals(state) && !"已驳回".equals(oldState)) {
                pointsChange = -50;
                historyType = 4; // 系统扣除
                historyDesc = "发布违规信息被驳回扣除";
            }

            // 撤销驳回（退还50分）
            else if ("已驳回".equals(oldState) && !"已驳回".equals(state)) {
                pointsChange = 50;
                historyType = 2; // 退还积分
                historyDesc = "驳回撤销，退还积分";
            }

            // 首次通过招领帖子（奖励10分）
            else if ("待审核".equals(oldState) && "待认领".equals(state) && "招领".equals(type)) {
                pointsChange = 10;
                historyType = 1; // 招领奖励
                historyDesc = "招领帖子审核通过奖励";
            }

            // 若积分发生变动，统一执行加扣和流水入库
            if (pointsChange != 0) {
                userMapper.addPoints(userId, pointsChange);
                recordPointHistory(userId, historyType, pointsChange, historyDesc);
            }
        }

        return ServerResponse.createServerResponseBySuccess("操作成功");
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


    /**
     * 根据分类标题获取该分类下的信息
     * 先通过标题获取分类ID，再查询对应分类的信息
     * @param title 分类标题
     * @param type 信息类型
     * @return ServerResponse 包含分类信息列表的响应对象
     */
    @Override
    public ServerResponse getDetailByTitle(String title,String type) {
        int id = (int) lostFoundTypeService.getIdByName(title).getData();
        return ServerResponse.createServerResponseBySuccess(getLostFoundDetail(id,type).getData());
    }


}
