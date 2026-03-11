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
    //  查询总数（用于分页）
    int countAdminMessages(@Param("keyword") String keyword, @Param("state") Integer state);

    //  分页获取留言列表
    List<MessageVO> getAdminMessageList(@Param("offset") int offset,
                                        @Param("pageSize") int pageSize,
                                        @Param("keyword") String keyword,
                                        @Param("state") Integer state);

    //  更新留言状态与原因
    int updateMessageState(@Param("id") Integer id, @Param("state") Integer state, @Param("reason") String reason);

    //  删除留言
    int deleteMessageById(@Param("id") Integer id);

    /**
     * 根据用户ID获取留言列表
     * @param userId 用户ID
     * @return 留言视图对象列表
     */
    List<MessageVO> getMessagesByUserId(@Param("userId") Integer userId);
}