package com.mantoo.yican.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/11.
 */

public class Vehicle implements Serializable {
    private String Title; //每条item的数据
    private boolean isChecked; //每条item的状态

    public Vehicle(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
