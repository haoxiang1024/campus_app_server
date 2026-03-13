package com.school.entity;

import java.util.Date;

public class PointHistory {
    private Integer id;
    private Integer userId;
    private Integer type; // 1-获取, 3-兑换消耗等
    private Integer pointsChanged; // 变动数值(正负)
    private String description;
    private Date createTime;

    public PointHistory() {
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", type=" + type +
                ", pointsChanged=" + pointsChanged +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPointsChanged() {
        return pointsChanged;
    }

    public void setPointsChanged(Integer pointsChanged) {
        this.pointsChanged = pointsChanged;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
