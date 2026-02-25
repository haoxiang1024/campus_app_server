package com.school.mapper;

import com.school.entity.LostFound;
import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 管理员数据访问接口
 * 提供管理员后台管理相关的数据库操作方法
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
@Mapper
public interface AdminMapper {

    /**
     * 获取系统总用户数
     * @return 用户总数
     */
    @Select("SELECT COUNT(*) FROM user")
    int getAllUserCount();
    /**
     * 获取失物招领信息总数
     * @param type 类型筛选："lost"-丢失物品，"found"-拾到物品，null-全部
     * @return 符合条件的记录总数
     */
    int getAllLostFoundCount(@Param("type") String type);
    /**
     * 获取所有用户基本信息
     * @return 用户信息列表（不包含密码等敏感信息）
     */
    @Select("select id,nickname,photo,phone,sex,reg_date,email,state from user")
    List<User> getAllUserInfo();
    /**
     * 根据关键字模糊查询用户
     * 搜索字段包括：昵称、手机号、邮箱
     * @param keyword 搜索关键字
     * @return 符合条件的用户列表
     */
    @Select("SELECT id,nickname,photo,phone,sex,reg_date,email FROM user WHERE " +
            "nickname LIKE CONCAT('%', #{keyword}, '%') OR " +
            "phone LIKE CONCAT('%', #{keyword}, '%') OR " +
            "email LIKE CONCAT('%', #{keyword}, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);

    /**
     * 批量重置用户密码
     * @param idList 用户ID列表
     * @param password 新密码
     * @return 更新成功的记录数
     */
    int batchResetPassword(@Param("idList") List<Integer> idList, @Param("password") String password);
    /**
     * 分页查询失物招领信息
     * 支持关键字、类型、状态等多条件筛选
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @param type 类型筛选
     * @param state 状态筛选
     * @return 失物招领信息列表
     */
    List<LostFound> getLostFoundByPage(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("state") String state
    );
    /**
     * 根据条件统计失物招领信息总数
     * @param keyword 搜索关键字
     * @param type 类型筛选
     * @param state 状态筛选
     * @return 符合条件的记录总数
     */
    int getLostFoundCountByCondition(
            @Param("keyword") String keyword,
            @Param("type") String type,
            @Param("state") String state
    );
    /**
     * 更新失物招领信息的置顶状态
     * @param id 失物招领ID
     * @param stick 置顶状态：1-置顶，0-取消置顶
     * @return 更新成功返回true，失败返回false
     */
    @Update("update lostFound set stick=#{stick} where id = #{id}")
    boolean updateStickStatus(@Param("id")int id,@Param("stick")int stick);
}
