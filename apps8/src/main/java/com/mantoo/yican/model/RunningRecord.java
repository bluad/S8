package com.mantoo.yican.model;

import java.io.Serializable;
/**
 * Created by Administrator on 2017/10/12.
 */

public class RunningRecord implements Serializable{


    private int ID;
    private String signImgs;
    private String time;
    private String context;

    public String getSignImgs() {
        return signImgs;
    }

    public void setSignImgs(String signImgs) {
        this.signImgs = signImgs;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
