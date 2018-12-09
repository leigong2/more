package com.moreclub.moreapp.main.model.event;

import com.moreclub.moreapp.main.model.MerchantItem;

/**
 * Created by Captain on 2017/8/26.
 */

public class ChannelAddressEvent {

    public ChannelAddressEvent(MerchantItem item){
        merchantItem = item;
    }


    public MerchantItem getMerchantItem() {
        return merchantItem;
    }

    public void setMerchantItem(MerchantItem merchantItem) {
        this.merchantItem = merchantItem;
    }

    private MerchantItem merchantItem;

}
