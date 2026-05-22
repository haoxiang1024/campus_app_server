package com.school.mapper;

import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper {

    Integer findUserByPhone(@Param("phone") String phone);


    boolean register(@Param("user") User user);


    User userInfo(@Param("id") int id);


    String login(@Param("phone") String phone);


    boolean resetPwd(@Param("phone")String phone,@Param("password")String pwd);


    boolean updatePhoto(@Param("photo") String photo,@Param("id")int id);


    boolean updateAc(@Param("nickname")String nickname,@Param("sex")String sex,@Param("id")int id);


    List<User>getalll();

    int updateUserInfo(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Integer id);

    int updateUserStatus(@Param("ids") List<Integer> ids, @Param("state") Integer state);

    List<User> getUserPage(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
    

    int getUserCount(@Param("keyword") String keyword);


    int deleteUserById(@Param("id") int id);

    int deductPoints(@Param("userId") Integer userId, @Param("points") Integer points);

    int addPoints(@Param("userId") Integer userId, @Param("points") Integer points);
}
