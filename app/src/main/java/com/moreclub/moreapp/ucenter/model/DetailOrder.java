package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017/4/1.
 */

public class DetailOrder extends Order {
    private String merchantName;
    private String merchantPhone;
    private String address;
    private String location;
    private String drawbackRemark;
    private String appointmentRemark;
    private String avlRemark;
    private String avlTime;
    private String invoiceRemark;
    private String useRules;
    private String avlStartDate;
    private String avlEndDate;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDrawbackRemark() {
        return drawbackRemark;
    }

    public void setDrawbackRemark(String drawbackRemark) {
        this.drawbackRemark = drawbackRemark;
    }

    public String getAppointmentRemark() {
        return appointmentRemark;
    }

    public void setAppointmentRemark(String appointmentRemark) {
        this.appointmentRemark = appointmentRemark;
    }

    public String getAvlRemark() {
        return avlRemark;
    }

    public void setAvlRemark(String avlRemark) {
        this.avlRemark = avlRemark;
    }

    public String getAvlTime() {
        return avlTime;
    }

    public void setAvlTime(String avlTime) {
        this.avlTime = avlTime;
    }

    public String getInvoiceRemark() {
        return invoiceRemark;
    }

    public void setInvoiceRemark(String invoiceRemark) {
        this.invoiceRemark = invoiceRemark;
    }

    public String getUseRules() {
        return useRules;
    }

    public void setUseRules(String useRules) {
        this.useRules = useRules;
    }

    public String getAvlStartDate() {
        return avlStartDate;
    }

    public void setAvlStartDate(String avlStartDate) {
        this.avlStartDate = avlStartDate;
    }

    public String getAvlEndDate() {
        return avlEndDate;
    }

    public void setAvlEndDate(String avlEndDate) {
        this.avlEndDate = avlEndDate;
    }
}
