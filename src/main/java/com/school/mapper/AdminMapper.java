package com.school.mapper;

import com.school.entity.Admin;
import com.school.entity.Lost;
import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {
    //登录
    @Select("select * from admin where username=#{username} and password=#{password}")
    Admin getUser(@Param("username") String username, @Param("password") String password);
    //获取所有用户数
    @Select("SELECT COUNT(*) FROM user")
    int getAllUserCount();
    //获取所有丢失物品数量
    @Select("SELECT COUNT(*) FROM lost")
    int getAllLostCount();
    //获取所有找回物品数量
    @Select("SELECT COUNT(*) FROM found")
    int getAllFoundCount();
    //获取所有用户信息
    @Select("select id,nickname,photo,phone,sex,reg_date,email,state from user")
    List<User> getAllUserInfo();
    /**
     * 根据关键字模糊查询用户
     * 匹配字段：nickname, phone, email
     */
    @Select("SELECT id,nickname,photo,phone,sex,reg_date,email FROM user WHERE " +
            "nickname LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%') OR " +
            "email LIKE CONCAT('%', #{keyword}, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);

    //批量重置密码
    int batchResetPassword(@Param("idList") List<Integer> idList, @Param("password") String password);
    //分页查询
    List<Lost> getLostPage(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
    int getLostCount(@Param("keyword") String keyword);
}
