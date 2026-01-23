package com.school.entity;

import java.util.Date;

public class Admin {
    private Integer id;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String nickname;
    private String photo;
    private String phone;
    private Date reg_date;

    public String toString() {
        return "AdminService{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", phone='" + phone + '\'' +
                ", reg_date=" + reg_date +
                '}';
    }

    public Admin(Integer id, String username, String password, String nickname, String photo, String phone, Date reg_date) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.photo = photo;
        this.phone = phone;
        this.reg_date = reg_date;
    }

    public Admin() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }
}
