package com.moreclub.moreapp.packages.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class PkgRefundReq {

    private String appVersion;
    private String deviceId;
    private String machine;
    private String machineStyle;
    private String opSystem;
    private String opVersion;
    private int operate;
    private String operateCity;
    private String operateIp;
    private String operateLocation;
    private String operateRemark;
    private String orderId;
    private String uid;
    private String reason;
    private List<Integer> couponCodes;
    private Integer payMethod;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getMachineStyle() {
        return machineStyle;
    }

    public void setMachineStyle(String machineStyle) {
        this.machineStyle = machineStyle;
    }

    public String getOpSystem() {
        return opSystem;
    }

    public void setOpSystem(String opSystem) {
        this.opSystem = opSystem;
    }

    public String getOpVersion() {
        return opVersion;
    }

    public void setOpVersion(String opVersion) {
        this.opVersion = opVersion;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getOperateCity() {
        return operateCity;
    }

    public void setOperateCity(String operateCity) {
        this.operateCity = operateCity;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    public String getOperateLocation() {
        return operateLocation;
    }

    public void setOperateLocation(String operateLocation) {
        this.operateLocation = operateLocation;
    }

    public String getOperateRemark() {
        return operateRemark;
    }

    public void setOperateRemark(String operateRemark) {
        this.operateRemark = operateRemark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Integer> getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(List<Integer> couponCodes) {
        this.couponCodes = couponCodes;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }
}
