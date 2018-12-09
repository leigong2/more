package com.moreclub.moreapp.ucenter.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class Order {
    private Long orderId;
    private Integer itemNum;
    private BigDecimal totalPrice;
    private BigDecimal actualPrice;
    private Integer pid;
    private Integer merchantId;
    private Integer status;
    private Integer prdType;
    private Integer payMethod;
    private String title;
    private String thumb;
    private long createTime;
    private long payTime;
    private Boolean comment;//是否评价过
    private String contactName;
    private String contactPhone;
    private List<ConsumeCode> codes;

    public String getMarchantName() {
        return marchantName;
    }

    public void setMarchantName(String marchantName) {
        this.marchantName = marchantName;
    }

    private String marchantName;

    public int getGotoType() {
        return gotoType;
    }

    public void setGotoType(int gotoType) {
        this.gotoType = gotoType;
    }

    private int gotoType;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPrdType() {
        return prdType;
    }

    public void setPrdType(Integer prdType) {
        this.prdType = prdType;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<ConsumeCode> getCodes() {
        return codes;
    }

    public void setCodes(List<ConsumeCode> codes) {
        this.codes = codes;
    }
}
