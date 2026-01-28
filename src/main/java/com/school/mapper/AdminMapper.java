package com.school.mapper;

import com.school.entity.LostFound;
import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {

    //获取所有用户数
    @Select("SELECT COUNT(*) FROM user")
    int getAllUserCount();
    //获取所有物品数量
    /**
     * 根据类型获取数量
     * @param type 传入 "lost", "found" 或 null (查询全部)
     */
    int getAllLostFoundCount(@Param("type") String type);
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
    List<LostFound> getLostFoundByPage(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("state") String state
    );
    //查询总记录条数(含过滤条件)
    int getLostFoundCountByCondition(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("state") String state
    );
    //置顶信息
    @Update("update lostFound set stick=#{stick} where id = #{id}")
    boolean updateStickStatus(@Param("id")int id,@Param("stick")int stick);
}
