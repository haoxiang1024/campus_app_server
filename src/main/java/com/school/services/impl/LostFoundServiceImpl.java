package com.school.services.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.entity.LostFound;
import com.school.entity.LostFoundType;
import com.school.mapper.LostFoundMapper;
import com.school.mapper.LostFoundTypeMapper;
import com.school.services.api.LostFoundService;
import com.school.services.api.LostFoundTypeService;
import com.school.utils.ResponseCode;
import com.school.utils.SensitiveWordUtil;
import com.school.utils.ServerResponse;
import com.school.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            return ServerResponse.createServerResponseByFail(ResponseCode.DATA_EMPTY.getCode(), ResponseCode.DATA_EMPTY.getMsg());
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
            return ServerResponse.createServerResponseByFail(ResponseCode.DATA_EMPTY.getCode(), ResponseCode.DATA_EMPTY.getMsg());
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
    public ServerResponse addLostFound(String lostfoundJson) {
        // 将JSON字符串解析为LostFound实体对象
        LostFound lostFound = JSON.parseObject(lostfoundJson, LostFound.class);
        //敏感词检测
        if(sensitiveWordUtil.contains(lostFound.getContent())||sensitiveWordUtil.contains(lostFound.getTitle())||sensitiveWordUtil.contains(lostFound.getPlace())){
            lostFound.setState("已驳回");
            lostFoundMapper.addLostFound(lostFound);
            return ServerResponse.createServerResponseByFail("内容包含敏感词，请修改后重试");
        }
        
        // 调用数据访问层保存失物招领信息
        if (lostFoundMapper.addLostFound(lostFound)) {
            String targetType = lostFound.getType().equals("失物") ? "招领" : "失物";
            // 提取标题作为关键字
            String keyword = lostFound.getTitle();
            List<LostFound> matchedList = lostFoundMapper.smartMatch(targetType, lostFound.getLostfoundtypeId(), keyword);
            if (matchedList != null && !matchedList.isEmpty()) {
                // 如果匹配到了，把列表放进 data 字段返回给前端
                return ServerResponse.createServerResponseBySuccess( matchedList,"发布成功，发现疑似匹配物品！");
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
           // List<LostFound> allByIdLostFoundList = lostFoundMapper.getLostFoundListById(user_id);
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
    public ServerResponse showTopList(int stick) {
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
        int result = lostFoundMapper.deleteByPrimaryKey(id);
        return result > 0 ? ServerResponse.createServerResponseBySuccess("删除成功") : ServerResponse.createServerResponseByFail("删除失败");
    }

    /**
     * 管理员审核失物招领信息（修改状态）
     * @param id 信息ID
     * @param state 目标状态
     * @return ServerResponse 操作结果响应对象
     */
    @Override
    public ServerResponse updateLostFoundStatus(Integer id, String state) {
        boolean isSuccess = lostFoundMapper.updateState(id, state);
        if (isSuccess) {
            return ServerResponse.createServerResponseBySuccess("修改成功");
        }
        return ServerResponse.createServerResponseByFail("修改失败");
    }

    /**
     * 分页查询失物招领信息列表
     * 使用PageHelper实现分页功能
     * @param page 页码
     * @param size 每页大小
     * @return ServerResponse 包含信息列表和分页信息的响应对象
     */
    @Override
    public ServerResponse getLostListByPage(int page, int size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 执行查询
        List<com.school.entity.LostFound> list = lostFoundMapper.selectByPage();
        //  封装 PageInfo
        PageInfo<com.school.entity.LostFound> pageInfo = new PageInfo<>(list);
        // 封装返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        return ServerResponse.createServerResponseBySuccess(resultMap);
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
