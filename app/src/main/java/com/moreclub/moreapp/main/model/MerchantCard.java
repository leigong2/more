package com.moreclub.moreapp.main.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/14.
 */

public class MerchantCard {

//    discountRate = "0.9";
//    includedAll = 1;
//    promoExtra =     (
//    {
//        remark = "\U9002\U7528\U4e8e\U5355\U676f\U9152\Uff08\U9664\U8461\U8404\U9152\U5916\Uff09";
//        title = "\U4f18\U60e0\U63d0\U793a";
//    }
//    );
//    promoHint = "\U9002\U7528\U4e8e\U5355\U676f\U9152\Uff08\U9664\U8461\U8404\U9152\U5916\Uff09|\U5177\U4f53\U4f18\U60e0\U9152\U6b3e\U5230\U5e97\U54a8\U8be2\U5546\U5bb6";
//    promoWine = "<null>";
//    wineCount = 160;

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public boolean getIncludedAll() {
        return includedAll;
    }

    public void setIncludedAll(boolean includedAll) {
        this.includedAll = includedAll;
    }

    public ArrayList<MerchantCardPromoExtra> getPromoExtra() {
        return promoExtra;
    }

    public void setPromoExtra(ArrayList<MerchantCardPromoExtra> promoExtra) {
        this.promoExtra = promoExtra;
    }

    public ArrayList<MerchantCardPromoExtra> getPromoWine() {
        return promoWine;
    }

    public void setPromoWine(ArrayList<MerchantCardPromoExtra> promoWine) {
        this.promoWine = promoWine;
    }

    public String getPromoHint() {
        return promoHint;
    }

    public void setPromoHint(String promoHint) {
        this.promoHint = promoHint;
    }

    public int getWineCount() {
        return wineCount;
    }

    public void setWineCount(int wineCount) {
        this.wineCount = wineCount;
    }

    private float discountRate;
    private boolean includedAll;
    private ArrayList<MerchantCardPromoExtra> promoExtra;
    private ArrayList<MerchantCardPromoExtra> promoWine;
    private String promoHint;
    private int wineCount;

}
