package com.mantoo.yican.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/12.
 */

public class RobExpress implements Serializable {


    String sender,sendphone,selat,selng,huopin,state,express,ID,date,receiver;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getSender() {
        return sender;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendphone() {
        return sendphone;
    }

    public void setSendphone(String sendphone) {
        this.sendphone = sendphone;
    }

    public String getSelat() {
        return selat;
    }

    public void setSelat(String selat) {
        this.selat = selat;
    }

    public String getSelng() {
        return selng;
    }

    public void setSelng(String selng) {
        this.selng = selng;
    }

    public String getHuopin() {
        return huopin;
    }

    public void setHuopin(String huopin) {
        this.huopin = huopin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }





}
