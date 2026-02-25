package com.school.mapper;

import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据访问接口
 * 提供用户相关的数据库操作方法
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
@Mapper
public interface UserMapper {
    /**
     * 判断手机号是否注册
     * 1 手机号未注册，注册一个账户
     * 2 手机号已经注册，返回user id
     */
    Integer findUserByPhone(@Param("phone") String phone);

    /**
     * 注册新用户
     * @param user 用户对象，包含注册信息
     * @return 注册成功返回true，失败返回false
     */
    boolean register(@Param("user") User user);

    /**
     * 根据用户ID获取用户详细信息
     * @param id 用户唯一标识ID
     * @return 用户对象，如果不存在返回null
     */
    User userInfo(@Param("id") int id);

    /**
     * 用户登录验证，获取用户密码
     * @param phone 用户手机号
     * @return 用户密码，如果用户不存在返回null
     */
    String login(@Param("phone") String phone);

    /**
     * 重置用户密码
     * @param phone 用户手机号
     * @param pwd 新密码
     * @return 重置成功返回true，失败返回false
     */
    boolean resetPwd(@Param("phone")String phone,@Param("password")String pwd);

    /**
     * 修改用户头像
     * @param photo 新头像URL
     * @param id 用户ID
     * @return 修改成功返回true，失败返回false
     */
    boolean updatePhoto(@Param("photo") String photo,@Param("id")int id);

    /**
     * 修改用户基本资料
     * @param nickname 用户昵称
     * @param sex 用户性别
     * @param id 用户ID
     * @return 修改成功返回true，失败返回false
     */
    boolean updateAc(@Param("nickname")String nickname,@Param("sex")String sex,@Param("id")int id);

    /**
     * 获取所有用户信息
     * @return 用户列表
     */
    List<User>getalll();
    /**
     * 更新用户完整信息
     * @param user 包含更新信息的用户对象
     * @return 影响的行数
     */
    int updateUserInfo(User user);
    /**
     * 根据ID获取用户完整信息
     * @param id 用户ID
     * @return 用户对象，如果不存在返回null
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Integer id);
    /**
     * 批量修改用户状态
     * @param ids 用户ID集合
     * @param state 目标状态 (1:正常, 0:禁用)
     * @return 影响行数
     */
    int updateUserStatus(@Param("ids") List<Integer> ids, @Param("state") Integer state);
    /**
     * 分页获取用户信息
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @param keyword 搜索关键字
     * @return 用户列表
     */
    List<User> getUserPage(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
    
    /**
     * 获取符合条件的用户总数
     * @param keyword 搜索关键字
     * @return 用户总数
     */
    int getUserCount(@Param("keyword") String keyword);
}
