package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Captain on 2017/6/15.
 */

public class UserCoupon {

//    {"originate":1,"name":"more福利！回馈广大用户！","modalName":"节日礼遇券","promotionType":1,
//            "type":5,"parValue":50.00,"mustConsumption":0.00,"superposition":true,
//            "remark":"嗨起来吧，more来给您送福利！","modalstatus":1,"cid":21,"couponModal":21,
//            "avlStartDateTime":1498113480,"avlEndDateTime":1498286340,"status":4,
//            "createAt":null,"locked":false,"rid":4874}

    public int getModalstatus() {
        return modalstatus;
    }

    public void setModalstatus(int modalstatus) {
        this.modalstatus = modalstatus;
    }

    private int modalstatus;

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }
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

    public int getCouponModal() {
        return couponModal;
    }

    public void setCouponModal(int couponModal) {
        this.couponModal = couponModal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAvlStartDateTime() {
        return avlStartDateTime;
    }

    public void setAvlStartDateTime(long avlStartDateTime) {
        this.avlStartDateTime = avlStartDateTime;
    }

    public long getAvlEndDateTime() {
        return avlEndDateTime;
    }

    public void setAvlEndDateTime(long avlEndDateTime) {
        this.avlEndDateTime = avlEndDateTime;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getReceiveforMerchant() {
        return receiveforMerchant;
    }

    public void setReceiveforMerchant(String receiveforMerchant) {
        this.receiveforMerchant = receiveforMerchant;
    }

    public String getLockedTips() {
        return lockedTips;
    }

    public void setLockedTips(String lockedTips) {
        this.lockedTips = lockedTips;
    }

    private int cid;
    private int originate;
    private String name;
    private String modalName;
    private int promotionType;
    private int type;
    private float parValue;
    private float mustConsumption;
    private Boolean superposition;
    private int couponModal;
    private int status;
    private long avlStartDateTime;
    private long avlEndDateTime;
    private int rid;
    private int balance;
    private String remark;
    private long publisher;
    private long createAt;
    private String receiveforMerchant;
    private Boolean locked;
    private String lockedTips;

}
