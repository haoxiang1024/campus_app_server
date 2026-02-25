package com.school.entity;

import java.util.Date;

/**
 * 用户实体类
 * 用于表示校园应用系统中的用户信息，包含用户的基本资料、账户状态和权限信息
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
public class User {
    /** 用户唯一标识ID */
    private Integer id;
    
    /** 用户登录密码 */
    private String password;
    
    /** 用户昵称 */
    private String nickname;
    
    /** 用户头像URL */
    private String photo;
    
    /** 用户性别 */
    private String sex;
    
    /** 用户手机号码 */
    private String phone;
    
    /** 用户账户余额 */
    private Integer balance;
    
    /** 用户声望值 */
    private Integer prestige;
    
    /** 用户注册时间 */
    private Date reg_date;
    
    /** 用户邮箱地址 */
    private String email;
    
    /** 用户账户状态：0-正常，1-禁用 */
    private int state;
    
    /** 用户角色权限：0-普通用户，1-管理员 */
    private int role;
    
    /** 即时通讯token */
    private String im_token;

    /**
     * 获取即时通讯token
     * @return 即时通讯token
     */
    public String getIm_token() {
        return im_token;
    }

    /**
     * 设置即时通讯token
     * @param im_token 即时通讯token
     */
    public void setIm_token(String im_token) {
        this.im_token = im_token;
    }

    /**
     * 获取用户角色
     * @return 用户角色权限：0-普通用户，1-管理员
     */
    public int getRole() {
        return role;
    }

    /**
     * 设置用户角色
     * @param role 用户角色权限：0-普通用户，1-管理员
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * 获取用户状态
     * @return 用户账户状态：0-正常，1-禁用
     */
    public int getstate() {
        return state;
    }

    /**
     * 设置用户状态
     * @param state 用户账户状态：0-正常，1-禁用
     */
    public void setstate(int state) {
        this.state = state;
    }

    public User(String phone, String password, String email) {
        this.phone = phone;
        this.password = password;
        this.email = email;
    }

    /**
     * 获取用户邮箱
     * @return 用户邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置用户邮箱
     * @param email 用户邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public User(String photo, String phone, String sex, Integer balance, Integer prestige, Date reg_date, String email, int state, int role, String nickname, String password) {
        this.photo = photo;
        this.phone = phone;
        this.sex = sex;
        this.balance = balance;
        this.prestige = prestige;
        this.reg_date = reg_date;
        this.email = email;
        this.state = state;
        this.role = role;
        this.nickname = nickname;
        this.password = password;
    }

    public User() {
    }

    /**
     * 获取用户ID
     * @return 用户唯一标识ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户ID
     * @param id 用户唯一标识ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户密码
     * @return 用户登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码
     * @param password 用户登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户昵称
     * @return 用户昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置用户昵称
     * @param nickname 用户昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取用户头像
     * @return 用户头像URL
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 设置用户头像
     * @param photo 用户头像URL
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取用户性别
     * @return 用户性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置用户性别
     * @param sex 用户性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取用户手机号
     * @return 用户手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置用户手机号
     * @param phone 用户手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取用户余额
     * @return 用户账户余额
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * 设置用户余额
     * @param balance 用户账户余额
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * 获取用户声望值
     * @return 用户声望值
     */
    public Integer getPrestige() {
        return prestige;
    }

    /**
     * 设置用户声望值
     * @param prestige 用户声望值
     */
    public void setPrestige(Integer prestige) {
        this.prestige = prestige;
    }

    /**
     * 获取用户注册时间
     * @return 用户注册时间
     */
    public Date getReg_date() {
        return reg_date;
    }

    /**
     * 设置用户注册时间
     * @param reg_date 用户注册时间
     */
    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                ", prestige=" + prestige +
                ", reg_date=" + reg_date +
                ", email='" + email + '\'' +
                ", state=" + state +
                ", role=" + role +
                ", im_token='" + im_token + '\'' +
                '}';
    }
}