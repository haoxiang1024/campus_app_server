package com.school.entity;

import java.util.Date;
import java.util.Objects;

/**
 * 搜索信息实体类
 * 用于表示搜索结果中的失物招领信息，继承自LostFound的部分属性
 * 主要用于搜索功能的数据封装和传输
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
public class SearchInfo {

    /** 搜索信息唯一标识ID */
    private Integer id;

    /** 搜索信息标题 */
    private String title;

    /** 搜索信息图片URL */
    private String img;

    /** 发布时间 */
    private Date pub_date;

    /** 搜索信息详细描述 */
    private String content;

    /** 丢失/拾到地点 */
    private String place;

    /** 联系电话 */
    private String phone;

    /** 状态：0-未解决，1-已解决 */
    private String state;

    /** 是否置顶：0-不置顶，1-置顶 */
    private Integer stick;

    /** 失物招领类型ID */
    private Integer lostfoundtype_id;

    /** 失物招领类型对象 */
    private LostFoundType lostfoundtype;

    /** 发布用户ID */
    private Integer user_id;

    /** 发布用户昵称 */
    private String nickname;

    /**
     * 默认构造函数
     */
    public SearchInfo() {
    }

    /**
     * 全参数构造函数
     * 
     * @param id 搜索信息ID
     * @param title 标题
     * @param img 图片URL
     * @param pub_date 发布时间
     * @param content 内容描述
     * @param place 地点
     * @param phone 联系电话
     * @param state 状态
     * @param stick 是否置顶
     * @param lostfoundtype_id 类型ID
     * @param lostfoundtype 类型对象
     * @param user_id 用户ID
     * @param nickname 用户昵称
     */
    public SearchInfo(Integer id, String title, String img, Date pub_date, String content, String place, String phone, String state, Integer stick, Integer lostfoundtype_id, LostFoundType lostfoundtype, Integer user_id, String nickname) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.pub_date = pub_date;
        this.content = content;
        this.place = place;
        this.phone = phone;
        this.state = state;
        this.stick = stick;
        this.lostfoundtype_id = lostfoundtype_id;
        this.lostfoundtype = lostfoundtype;
        this.user_id = user_id;
        this.nickname = nickname;
    }

    /**
     * 获取搜索信息ID
     * @return 搜索信息唯一标识ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置搜索信息ID
     * @param id 搜索信息唯一标识ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取标题
     * @return 搜索信息标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     * @param title 搜索信息标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取图片URL
     * @return 搜索信息图片URL
     */
    public String getImg() {
        return img;
    }

    /**
     * 设置图片URL
     * @param img 搜索信息图片URL
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取发布时间
     * @return 发布时间
     */
    public Date getPub_date() {
        return pub_date;
    }

    /**
     * 设置发布时间
     * @param pub_date 发布时间
     */
    public void setPub_date(Date pub_date) {
        this.pub_date = pub_date;
    }

    /**
     * 获取内容描述
     * @return 搜索信息详细描述
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容描述
     * @param content 搜索信息详细描述
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取地点
     * @return 丢失/拾到地点
     */
    public String getPlace() {
        return place;
    }

    /**
     * 设置地点
     * @param place 丢失/拾到地点
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * 获取联系电话
     * @return 联系电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置联系电话
     * @param phone 联系电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取状态
     * @return 状态：0-未解决，1-已解决
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态
     * @param state 状态：0-未解决，1-已解决
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取是否置顶
     * @return 是否置顶：0-不置顶，1-置顶
     */
    public Integer getStick() {
        return stick;
    }

    /**
     * 设置是否置顶
     * @param stick 是否置顶：0-不置顶，1-置顶
     */
    public void setStick(Integer stick) {
        this.stick = stick;
    }

    /**
     * 获取类型ID
     * @return 失物招领类型ID
     */
    public Integer getLostfoundtype_id() {
        return lostfoundtype_id;
    }

    /**
     * 设置类型ID
     * @param lostfoundtype_id 失物招领类型ID
     */
    public void setLostfoundtype_id(Integer lostfoundtype_id) {
        this.lostfoundtype_id = lostfoundtype_id;
    }

    /**
     * 获取类型对象
     * @return 失物招领类型对象
     */
    public LostFoundType getLostfoundtype() {
        return lostfoundtype;
    }

    /**
     * 设置类型对象
     * @param lostfoundtype 失物招领类型对象
     */
    public void setLostfoundtype(LostFoundType lostfoundtype) {
        this.lostfoundtype = lostfoundtype;
    }

    /**
     * 获取用户ID
     * @return 发布用户ID
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * 设置用户ID
     * @param user_id 发布用户ID
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /**
     * 获取用户昵称
     * @return 发布用户昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置用户昵称
     * @param nickname 发布用户昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchInfo that = (SearchInfo) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(img, that.img) && Objects.equals(pub_date, that.pub_date) && Objects.equals(content, that.content) && Objects.equals(place, that.place) && Objects.equals(phone, that.phone) && Objects.equals(state, that.state) && Objects.equals(stick, that.stick) && Objects.equals(lostfoundtype_id, that.lostfoundtype_id) && Objects.equals(lostfoundtype, that.lostfoundtype) && Objects.equals(user_id, that.user_id) && Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, img, pub_date, content, place, phone, state, stick, lostfoundtype_id, lostfoundtype, user_id, nickname);
    }

    @Override
    public String toString() {
        return "SearchInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", pub_date=" + pub_date +
                ", content='" + content + '\'' +
                ", place='" + place + '\'' +
                ", phone='" + phone + '\'' +
                ", state='" + state + '\'' +
                ", stick=" + stick +
                ", lostfoundtype_id=" + lostfoundtype_id +
                ", lostfoundtype=" + lostfoundtype +
                ", user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
