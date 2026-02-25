package com.school.entity;

/**
 * 邮件表单数据实体类
 * 用于封装发送邮件所需的各种表单数据
 * 包含发件人信息、收件人信息以及邮件内容
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
public class EmailFormData {
    /** 发件人邮箱地址 */
    private String name;
    
    /** 邮箱授权码 */
    private String password;
    
    /** 收件人邮箱地址 */
    private String email;
    
    /** 邮件主题 */
    private String subject;
    
    /** 邮件正文内容 */
    private String message;

    /**
     * 默认构造函数
     */
    public EmailFormData() {
    }

    /**
     * 全参数构造函数
     * 
     * @param name 发件人邮箱
     * @param password 邮箱授权码
     * @param email 收件人邮箱
     * @param subject 邮件主题
     * @param message 邮件内容
     */
    public EmailFormData(String name, String password, String email, String subject, String message) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    /**
     * 获取发件人邮箱
     * @return 发件人邮箱地址
     */
    public String getName() {
        return name;
    }

    /**
     * 设置发件人邮箱
     * @param name 发件人邮箱地址
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取邮箱授权码
     * @return 邮箱授权码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置邮箱授权码
     * @param password 邮箱授权码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取收件人邮箱
     * @return 收件人邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置收件人邮箱
     * @param email 收件人邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取邮件主题
     * @return 邮件主题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置邮件主题
     * @param subject 邮件主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取邮件内容
     * @return 邮件正文内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置邮件内容
     * @param message 邮件正文内容
     */
    public void setMessage(String message) {
        this.message = message;
    }
}

