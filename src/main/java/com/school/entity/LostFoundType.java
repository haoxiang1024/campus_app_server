package com.school.entity;

import java.io.Serializable;

/**
 * (LostFoundTypeService)实体类
 *
 * @author makejava
 * @since 2023-05-13 13:45:40
 */
public class LostFoundType implements Serializable {
    private static final long serialVersionUID = 112447865142901463L;

    private Integer id;

    private String name;

    public LostFoundType() {
    }

    public LostFoundType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LostFoundTypeService{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

