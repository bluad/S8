package com.mantoo.yican.model;

import com.mantoo.yican.widget.SlideView;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/12.
 */

public class AddresseeData  implements Serializable{

    private int ID;
    private String sender;
    private String sendphone;
    private String province;
    private String city;
    private String town;
    private String address;
    private String rename;
    private String rephone;
    private String reprovince;
    private String recity;
    private String retown;
    private String sendaddress;
    private String receivephone;
    private String remark;
    public String getSendaddress() {
        return sendaddress;
    }

    public void setSendaddress(String sendaddress) {
        this.sendaddress = sendaddress;
    }

    public String getReceivephone() {
        return receivephone;
    }

    public void setReceivephone(String receivephone) {
        this.receivephone = receivephone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String readdress;
    private String huopin;
    private String note;
    private String origin;
    private String express;
    private String courier;
    private String couphone;
    private String orderId;
    private String classify;
    private String date;
    private String receiver;
    private String receiveaddress;
    private String receiveprovince;
    private String receivecity;
    private String receivetown;
    private String signdate;

    public String getSigndate() {
        return signdate;
    }

    public void setSigndate(String signdate) {
        this.signdate = signdate;
    }

    public String getReceiveprovince() {
        return receiveprovince;
    }

    public void setReceiveprovince(String receiveprovince) {
        this.receiveprovince = receiveprovince;
    }

    public String getReceivecity() {
        return receivecity;
    }

    public void setReceivecity(String receivecity) {
        this.receivecity = receivecity;
    }

    public String getReceivetown() {
        return receivetown;
    }

    public void setReceivetown(String receivetown) {
        this.receivetown = receivetown;
    }


    public String getReceiveaddress() {
        return receiveaddress;
    }

    public void setReceiveaddress(String receiveaddress) {
        this.receiveaddress = receiveaddress;
    }

    private String frightfee;
    private String receivepot;
    private String receivepotid;
    private String invoice, selat, selng;
    public  transient SlideView slideView;
    public String getReceivepotid() {
        return receivepotid;
    }

    public void setReceivepotid(String receivepotid) {
        this.receivepotid = receivepotid;
    }



    public String getReceiver() {
        return receiver;
    }

    public String getFrightfee() {
        return frightfee;
    }

    public void setFrightfee(String frightfee) {
        this.frightfee = frightfee;
    }

    public String getReceivepot() {
        return receivepot;
    }

    public void setReceivepot(String receivepot) {
        this.receivepot = receivepot;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getSender() {
        return sender;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public String getRephone() {
        return rephone;
    }

    public void setRephone(String rephone) {
        this.rephone = rephone;
    }

    public String getReprovince() {
        return reprovince;
    }

    public void setReprovince(String reprovince) {
        this.reprovince = reprovince;
    }

    public String getRecity() {
        return recity;
    }

    public void setRecity(String recity) {
        this.recity = recity;
    }

    public String getRetown() {
        return retown;
    }

    public void setRetown(String retown) {
        this.retown = retown;
    }

    public String getReaddress() {
        return readdress;
    }

    public void setReaddress(String readdress) {
        this.readdress = readdress;
    }

    public String getHuopin() {
        return huopin;
    }

    public void setHuopin(String huopin) {
        this.huopin = huopin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getCouphone() {
        return couphone;
    }

    public void setCouphone(String couphone) {
        this.couphone = couphone;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }


}
