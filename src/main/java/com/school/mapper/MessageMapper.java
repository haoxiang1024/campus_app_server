package com.school.mapper;

import com.school.entity.Message;
import com.school.entity.MessageVO;
import com.school.entity.Msg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface MessageMapper {


    int insertMessage(@Param("msg") Message message);


    List<MessageVO> getMessageList();

    int countAdminMessages(@Param("keyword") String keyword, @Param("state") Integer state);


    List<MessageVO> getAdminMessageList(@Param("offset") int offset,
                                        @Param("pageSize") int pageSize,
                                        @Param("keyword") String keyword,
                                        @Param("state") Integer state);


    int updateMessageState(@Param("id") Integer id, @Param("state") Integer state);


    int deleteMessageById(@Param("id") Integer id);


    List<MessageVO> getMessagesByUserId(@Param("userId") Integer userId);

    @Select("select * from message where id = #{id}")
    Msg getMessageById(@Param("id") Integer id);
}