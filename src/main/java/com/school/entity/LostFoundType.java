package com.school.entity;

import java.io.Serializable;

/**
 * 失物招领类型实体类
 * 用于表示失物招领信息的分类类型，如书籍、电子产品、衣物等
 * 
 * @author campus_app_server
 * @version 1.0
 * @since 2026-02-25
 */
public class LostFoundType implements Serializable {
    /** 序列化版本UID */
    private static final long serialVersionUID = 112447865142901463L;

    /** 类型唯一标识ID */
    private Integer id;

    /** 类型名称 */
    private String name;

    /**
     * 默认构造函数
     */
    public LostFoundType() {
    }

    /**
     * 全参数构造函数
     * 
     * @param id 类型ID
     * @param name 类型名称
     */
    public LostFoundType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 获取类型ID
     * @return 类型唯一标识ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置类型ID
     * @param id 类型唯一标识ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取类型名称
     * @return 类型名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置类型名称
     * @param name 类型名称
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LostFoundType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

