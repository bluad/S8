package com.mantoo.yican.model;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ExpressStatus {

    // 任务单号
    private String missionNo;
    // 日期
    private String expressDate;
    // 时间
    private String expressTime;
    // 线路
    private String line;
    // 快递状态
    private String way;
    // 数量
    private String number;
    // 目的地
    private String distination;
    // 佣金
    private String pay;
    // 件数
    private String jianShu;
    // 体积
    private String tiJi;
    // 重量
    private String weight;
    // 完成状态
    private String status;

    private String complayName;

    private String sendAddr;

    private String receiveAddr;

    private String missionStatus;

    public String getMissionNo() {
        return missionNo;
    }

    public void setMissionNo(String missionNo) {
        this.missionNo = missionNo;
    }

    public String getExpressDate() {
        return expressDate;
    }

    public void setExpressDate(String expressDate) {
        this.expressDate = expressDate;
    }

    public String getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(String expressTime) {
        this.expressTime = expressTime;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistination() {
        return distination;
    }

    public void setDistination(String distination) {
        this.distination = distination;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getJianShu() {
        return jianShu;
    }

    public void setJianShu(String jianShu) {
        this.jianShu = jianShu;
    }

    public String getTiJi() {
        return tiJi;
    }

    public void setTiJi(String tiJi) {
        this.tiJi = tiJi;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComplayName() {
        return complayName;
    }

    public void setComplayName(String complayName) {
        this.complayName = complayName;
    }

    public String getSendAddr() {
        return sendAddr;
    }

    public void setSendAddr(String sendAddr) {
        this.sendAddr = sendAddr;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public String getMissionStatus() {
        return missionStatus;
    }

    public void setMissionStatus(String missionStatus) {
        this.missionStatus = missionStatus;
    }
}
