package com.moreclub.moreapp.ucenter.model;

import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/9.
 */

public class UserPrivilege {
    private float discountRate;
    private ArrayList<MerchantItem> merchants;
    private int mileageLimit;
    private String name;
    private ArrayList<String> remarks;
    private int totalMerchants;

    private CardDivision blackDivision;
    private CardDivision orangeDivision;

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public ArrayList<MerchantItem> getMerchants() {
        return merchants;
    }

    public void setMerchants(ArrayList<MerchantItem> merchants) {
        this.merchants = merchants;
    }

    public int getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(int mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalMerchants() {
        return totalMerchants;
    }

    public void setTotalMerchants(int totalMerchants) {
        this.totalMerchants = totalMerchants;
    }

    public ArrayList<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(ArrayList<String> remarks) {
        this.remarks = remarks;
    }

    public CardDivision getBlackDivision() {
        return blackDivision;
    }

    public void setBlackDivision(CardDivision blackDivision) {
        this.blackDivision = blackDivision;
    }

    public CardDivision getOrangeDivision() {
        return orangeDivision;
    }

    public void setOrangeDivision(CardDivision orangeDivision) {
        this.orangeDivision = orangeDivision;
    }

    @Override
    public String toString() {
        return "UserPrivilege{" +
                "discountRate=" + discountRate +
                ", merchants=" + merchants +
                ", mileageLimit=" + mileageLimit +
                ", name='" + name + '\'' +
                ", remarks=" + remarks +
                ", totalMerchants=" + totalMerchants +
                ", blackDivision=" + blackDivision +
                ", orangeDivision=" + orangeDivision +
                '}';
    }

    //    {
//        discountRate = 1;
//        merchants =     (
//                {
//                        address = "\U53f7";
//        lat = "30.6301";
//        lng = "104.083906";
//        mid = 45;
//        name = "\U5f25\U6e21\U9152\U5427";
//        sellingPoint = "\U684\U7a7a\U95f4";
//        thumb = "http:/dpnZLh.png";
//        },
//
//        {
//            address = "\U6850\U6893\U6797\U5357\U8def7\U53f7\U964424\U53f7";
//            lat = "30.62";
//            lng = "104.072";
//            mid = 47;
//            name = "\U53e4\U5df4\U70ed\U6d6a\U7ea2\U9152\U4ff1\U4e50\U90e8";
//            sellingPoint = "\U8ba901";
//            thumb = "http://more-activity.oss-cn-beijing.aliyuncs.com/2016/11/10/AicDe4o7IYFMxEkaxc0QcWnNs.jpg";
//        }
//        );
//        mileageLimit = 0;
//        name = "\U666e\U901a\U4f1a\U5458";
//        remarks =     (
//                "\U4ea4\U636"
//        );
//        totalMerchants = 28;
//    }
}
