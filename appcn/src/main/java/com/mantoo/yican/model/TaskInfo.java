package com.mantoo.yican.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/14.
 */

public class TaskInfo implements Serializable {

    private String waybillNo;

    private String sendAddress;

    private String receviceAddress;

    private String missionAddress;

    private String number;

    private String volumn;

    private String weight;

    private String distence;

    private String waybillStatus;

    private String isHandle;

    private String startLat;

    private String startLng;

    private String endLng;

    private String endLat;

    private String daishouAmount;

    private String sendDate;

    private String paytype;

    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getWaybillStatus() {
        return waybillStatus;
    }

    public void setWaybillStatus(String waybillStatus) {
        this.waybillStatus = waybillStatus;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getReceviceAddress() {
        return receviceAddress;
    }

    public void setReceviceAddress(String receviceAddress) {
        this.receviceAddress = receviceAddress;
    }

    public String getMissionAddress() {
        return missionAddress;
    }

    public void setMissionAddress(String missionAddress) {
        this.missionAddress = missionAddress;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVolumn() {
        return volumn;
    }

    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDistence() {
        return distence;
    }

    public void setDistence(String distence) {
        this.distence = distence;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLng() {
        return startLng;
    }

    public void setStartLng(String startLng) {
        this.startLng = startLng;
    }

    public String getEndLng() {
        return endLng;
    }

    public void setEndLng(String endLng) {
        this.endLng = endLng;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getDaishouAmount() {
        return daishouAmount;
    }

    public void setDaishouAmount(String daishouAmount) {
        this.daishouAmount = daishouAmount;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
