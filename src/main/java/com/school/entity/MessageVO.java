package com.school.entity;

import java.util.Date;
import java.util.List;

/**
 * 留言展示视图对象 (包含用户头像和昵称)
 */
public class MessageVO {
    private Integer id;
    private Integer userId;
    private String content;
    private Date createTime;
    private String nickname;
    private String photo;
    private Integer parentId; // 标识是否是子评论
    private List<MessageVO> replies; // 用于存放该留言下的所有二级评论

    @Override
    public String toString() {
        return "MessageVO{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", parentId=" + parentId +
                ", replies=" + replies +
                '}';
    }

    public List<MessageVO> getReplies() {
        return replies;
    }

    public void setReplies(List<MessageVO> replies) {
        this.replies = replies;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
