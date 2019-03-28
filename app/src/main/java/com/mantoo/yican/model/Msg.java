package com.mantoo.yican.model;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * Created by Administrator on 2018/6/22.
 */

public class Msg {
    public static final int TYPE_BLE = 0;
    public static final int TYPE_PHONE = 1;

    private String content;
    private int type;
    private String time;
    private String name;
    public Msg(String name, String time, String content, int type) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.type = type;
    }
    public Msg() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "name=" + name +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", time='" + time + '\'' +
                '}';
    }
}
