package com.school.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface CommentMapper {
    @Insert("INSERT INTO comment (lostfound_id, user_id, content, state, create_time) " +
            "VALUES (#{lostfound_id}, #{user_id}, #{content}, #{state}, NOW())")
    int addComment(@Param("lostfound_id") int lostfound_id,
                   @Param("user_id") int user_id,
                   @Param("content") String content,
                   @Param("state") int state);
}
