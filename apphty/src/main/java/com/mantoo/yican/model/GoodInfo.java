package com.mantoo.yican.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/23.
 */

public class GoodInfo implements Serializable{
    private String missonNumber;
    private String orderNumber;
    private String customerName;
    private String alertNumber;
    private String waybillNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAlertNumber() {
        return alertNumber;
    }

    public void setAlertNumber(String alertNumber) {
        this.alertNumber = alertNumber;
    }

    public String getMissonNumber() {
        return missonNumber;
    }

    public void setMissonNumber(String missonNumber) {
        this.missonNumber = missonNumber;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }
}
