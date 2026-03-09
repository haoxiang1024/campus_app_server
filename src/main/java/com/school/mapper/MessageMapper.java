package com.school.mapper;

import com.school.entity.Message;
import com.school.entity.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 留言数据访问接口
 */
@Mapper
public interface MessageMapper {

    /**
     * 新增留言
     * @param message 留言对象
     * @return 影响行数
     */
    int insertMessage(@Param("msg") Message message);

    /**
     * 获取留言列表（关联用户信息，按时间倒序）
     * @return 留言视图对象列表
     */
    List<MessageVO> getMessageList();
}