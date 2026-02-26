package com.school.entity;

import java.util.Date;

public class CommentVO {
    private Integer id;
    private String content;
    private Date create_time;
    private String state;
    private String rejectReason;

    // 联表查询出的额外字段
    private String nickname; // 评论人昵称 (来自 user 表)
    private String title;  // 帖子标题 (来自 lost_found 表)

    public CommentVO() {
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                ", state='" + state + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", nickname='" + nickname + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
