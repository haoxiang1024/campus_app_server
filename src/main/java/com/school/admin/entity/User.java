package com.school.admin.entity;

import lombok.Data;
import java.util.Date;


public class User {
    private String id;
    private String username;
    private String password;
    private String activateCode;
    private String email;
    private String phoneNumber;
    private String realName;
    private Integer gender;
    private String icon;
    private String schoolId;
    private String campusId;
    private Date createTime;
    private Date lastLogin;
    private Integer kind;
    private Integer status;
    private Integer recordStatus;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", activateCode='" + activateCode + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", icon='" + icon + '\'' +
                ", schoolId='" + schoolId + '\'' +
                ", campusId='" + campusId + '\'' +
                ", createTime=" + createTime +
                ", lastLogin=" + lastLogin +
                ", kind=" + kind +
                ", status=" + status +
                ", recordStatus=" + recordStatus +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public User(String id, String username, String password, String activateCode, String email, String phoneNumber, String realName, Integer gender, String icon, String schoolId, String campusId, Date createTime, Date lastLogin, Integer kind, Integer status, Integer recordStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.activateCode = activateCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.realName = realName;
        this.gender = gender;
        this.icon = icon;
        this.schoolId = schoolId;
        this.campusId = campusId;
        this.createTime = createTime;
        this.lastLogin = lastLogin;
        this.kind = kind;
        this.status = status;
        this.recordStatus = recordStatus;
    }
}
