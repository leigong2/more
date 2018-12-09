package com.moreclub.moreapp.ucenter.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/12.
 */

public class UserCenter {

    public int getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(int cardLevel) {
        this.cardLevel = cardLevel;
    }

    public int getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(int cardLimit) {
        this.cardLimit = cardLimit;
    }

    public int getCurMiles() {
        return curMiles;
    }

    public void setCurMiles(int curMiles) {
        this.curMiles = curMiles;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getParties() {
        return parties;
    }

    public void setParties(int parties) {
        this.parties = parties;
    }

    public int getRegDays() {
        return regDays;
    }

    public void setRegDays(int regDays) {
        this.regDays = regDays;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public ArrayList<MileageHistory> getMileageHistoryDtos() {
        return mileageHistoryDtos;
    }

    public void setMileageHistoryDtos(ArrayList<MileageHistory> mileageHistoryDtos) {
        this.mileageHistoryDtos = mileageHistoryDtos;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private int cardLevel;
    private int cardLimit;
    private int curMiles;
    private int favorites;
    private int follow;
    private int follower;
    private String grade;
    private String nickName;
    private int parties;
    private int regDays;
    private int rooms;
    private int sex;
    private String thumb;
    private ArrayList<MileageHistory> mileageHistoryDtos;
    private String phone;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    private String merchantId;

    //    {
//        cardLevel = 0;
//        cardLimit = 999;
//        curMiles = 199;
//        favorites = 0;
//        follow = 1;
//        follower = 0;
//        grade = "<null>";
//        mileageHistoryDtos =     (
//                {
//                        mileType = 0;
//        milestoneDate = "2016-10-31";
//        totalMileage = 10;
//        uid = 3097832504509440;
//        updateTime = 1487681693000;
//        }
//        );
//        nickName = "\U5fd7\U5728\U56db\U65b9";
//        parties = 0;
//        regDays = 163;
//        rooms = 0;
//        sex = 1;
//        thumb = "http://more-user.oss-cn-beijing.aliyuncs.com/2016/11/02/u_3097832504509440_1478073471.jpg";
//    }
}
