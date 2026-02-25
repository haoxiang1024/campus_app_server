package com.school.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * 评论实体类
 * 用于表示失物招领系统中的评论信息，支持评论回复功能
 * 包含评论的基本信息、用户信息以及层级关系
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
public class Comment {
    /** 评论唯一标识ID */
    private Integer id;
    
    /** 关联的失物招领信息ID */
    private Integer lostfound_id;
    
    /** 评论用户的ID */
    private Integer user_id;
    
    /** 评论用户的昵称 */
    private String nickname;
    
    /** 评论用户的头像URL */
    private String photo;
    
    /** 评论内容 */
    private String content;
    
    /** 评论状态：0-正常，1-删除 */
    private int state;
    
    /** 父评论ID，用于构建评论层级关系 */
    private int parentId;
    
    /** 被回复用户的ID */
    private int replyUserId;
    
    /** 被回复用户的昵称 */
    private String replyNickname;

    /** 子评论列表，用于存储回复该评论的所有子评论 */
    private List<Comment> replies;

    /**
     * 获取父评论ID
     * @return 父评论ID
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * 设置父评论ID
     * @param parentId 父评论ID
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取被回复者用户ID
     * @return 被回复者用户ID
     */
    public int getReplyUserId() {
        return replyUserId;
    }

    /**
     * 设置被回复者用户ID
     * @param replyUserId 被回复者用户ID
     */
    public void setReplyUserId(int replyUserId) {
        this.replyUserId = replyUserId;
    }

    /**
     * 获取被回复者昵称
     * @return 被回复者昵称
     */
    public String getReplyNickname() {
        return replyNickname;
    }

    /**
     * 设置被回复者昵称
     * @param replyNickname 被回复者昵称
     */
    public void setReplyNickname(String replyNickname) {
        this.replyNickname = replyNickname;
    }

    /**
     * 获取子评论列表
     * @return 子评论列表
     */
    public List<Comment> getReplies() {
        return replies;
    }

    /**
     * 设置子评论列表
     * @param replies 子评论列表
     */
    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", lostfound_id=" + lostfound_id +
                ", user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", content='" + content + '\'' +
                ", state=" + state +
                ", parentId=" + parentId +
                ", replyUserId=" + replyUserId +
                ", replyNickname='" + replyNickname + '\'' +
                ", replies=" + replies +
                ", create_time=" + create_time +
                '}';
    }

    /**
     * 获取评论状态
     * @return 评论状态
     */
    public int getState() {
        return state;
    }

    /**
     * 设置评论状态
     * @param state 评论状态
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * 获取评论用户ID
     * @return 评论用户ID
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * 设置评论用户ID
     * @param user_id 评论用户ID
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /**
     * 获取评论创建时间
     * @return 评论创建时间
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * 设置评论创建时间
     * @param create_time 评论创建时间
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取关联的失物招领ID
     * @return 失物招领ID
     */
    public Integer getLostfound_id() {
        return lostfound_id;
    }

    /**
     * 设置关联的失物招领ID
     * @param lostfound_id 失物招领ID
     */
    public void setLostfound_id(Integer lostfound_id) {
        this.lostfound_id = lostfound_id;
    }

    /** 评论创建时间，格式化为 yyyy-MM-dd HH:mm */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date create_time;

    /**
     * 默认构造函数
     */
    public Comment() {
    }

    /**
     * 获取评论ID
     * @return 评论ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置评论ID
     * @param id 评论ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取评论用户昵称
     * @return 评论用户昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置评论用户昵称
     * @param nickname 评论用户昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取评论用户头像
     * @return 评论用户头像
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 设置评论用户头像
     * @param photo 评论用户头像
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取评论内容
     * @return 评论内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置评论内容
     * @param content 评论内容
     */
    public void setContent(String content) {
        this.content = content;
    }

}
