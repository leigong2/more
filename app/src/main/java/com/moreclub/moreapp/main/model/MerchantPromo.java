package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/2/28.
 */

public class MerchantPromo {
    public float getCardRate() {
        return cardRate;
    }

    public void setCardRate(float cardRate) {
        this.cardRate = cardRate;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    private float cardRate;
    private int cardType;
}
