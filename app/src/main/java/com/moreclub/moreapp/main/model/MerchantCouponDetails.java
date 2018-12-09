package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/6/15.
 */

public class MerchantCouponDetails {

//    {"cid":4,"originate":1,"name":"测试优惠券","promotionType":1,
//            "type":1,"parValue":30.50,"mustConsumption":0.00,
//            "superposition":true,"remark":"修改的",
//            "status":1,"publisher":3005765457822721,"createAt":1496996535000,
//            "modiflyAt":1497436513000,"forMerchantType":2,"receiveforMerchant":"33,38"}

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getOriginate() {
        return originate;
    }

    public void setOriginate(int originate) {
        this.originate = originate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getParValue() {
        return parValue;
    }

    public void setParValue(float parValue) {
        this.parValue = parValue;
    }

    public float getMustConsumption() {
        return mustConsumption;
    }

    public void setMustConsumption(float mustConsumption) {
        this.mustConsumption = mustConsumption;
    }

    public Boolean getSuperposition() {
        return superposition;
    }

    public void setSuperposition(Boolean superposition) {
        this.superposition = superposition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPublisher() {
        return publisher;
    }

    public void setPublisher(long publisher) {
        this.publisher = publisher;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getModiflyAt() {
        return modiflyAt;
    }

    public void setModiflyAt(long modiflyAt) {
        this.modiflyAt = modiflyAt;
    }

    public int getForMerchantType() {
        return forMerchantType;
    }

    public void setForMerchantType(int forMerchantType) {
        this.forMerchantType = forMerchantType;
    }

    public String getReceiveforMerchant() {
        return receiveforMerchant;
    }

    public void setReceiveforMerchant(String receiveforMerchant) {
        this.receiveforMerchant = receiveforMerchant;
    }

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    private int cid;
    private int originate;
    private String name;
    private int promotionType;
    private int type;
    private float parValue;
    private float mustConsumption;
    private Boolean superposition;
    private String remark;
    private int status;
    private long publisher;
    private long createAt;
    private long modiflyAt;
    private int forMerchantType;
    private String receiveforMerchant;
    private String modalName;
}
