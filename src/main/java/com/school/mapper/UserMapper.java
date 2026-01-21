package com.school.mapper;

import com.school.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper {
    /**
     * 判断手机号是否注册
     * 1 手机号未注册，注册一个账户
     * 2 手机号已经注册，返回user id
     */
    Integer findUserByPhone(@Param("phone") String phone);

    //通过手机号注册用户
    void register(@Param("phone") String phone, @Param("user") User user);

    //根据id获取用户信息
    User userInfo(@Param("id") int id);

    //获取密码
    String login(@Param("phone") String phone);

    //重置密码
    boolean resetPwd(@Param("phone")String phone,@Param("password")String pwd);

    //修改用户头像
    boolean updatePhoto(@Param("photo") String photo,@Param("id")int id);

    //修改用户资料
    boolean updateAc(@Param("nickname")String nickname,@Param("sex")String sex,@Param("id")int id);

//获取所有用户信息
    List<User>getalll();
    // 更新用户信息
    int updateUserInfo(User user);
    // 根据ID获取用户
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Integer id);
    /**
     * 批量修改用户状态
     * @param ids 用户ID集合
     * @param state 目标状态 (1:正常, 0:禁用)
     * @return 影响行数
     */
    int updateUserStatus(@Param("ids") List<Integer> ids, @Param("state") Integer state);

}
