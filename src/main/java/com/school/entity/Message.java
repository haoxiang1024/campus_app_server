package com.school.entity;

import java.util.Date;

/**
 * 留言实体类
 */
public class Message {
    private Integer id;
    private Integer userId;
    private String content;
    private Date createTime;
    private Integer state;

    public Message() {}

    public Message(Integer userId, String content, Date createTime, Integer state) {
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
