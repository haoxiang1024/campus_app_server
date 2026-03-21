package com.school.entity;

import java.util.Date;

public class PointHistory {
    private Integer id;
    private Integer user_id;
    private Integer type; // 变动类型：1-发布招领奖励, 2-退还积分, 3-兑换商品消耗, 4-系统扣除
    private Integer points_changed; // 变动数值(正负)
    private String description;
    private Date create_time;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getPoints_changed() {
        return points_changed;
    }

    public void setPoints_changed(Integer points_changed) {
        this.points_changed = points_changed;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", type=" + type +
                ", points_changed=" + points_changed +
                ", description='" + description + '\'' +
                ", create_time=" + create_time +
                '}';
    }

    public PointHistory() {
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
