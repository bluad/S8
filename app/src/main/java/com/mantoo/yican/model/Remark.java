package com.mantoo.yican.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/22.
 */

public class Remark implements Serializable{
    private String createdAt;
    private String note;
    private String name;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
