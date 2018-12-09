package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantCard;

/**
 * Created by Captain on 2017/4/14.
 */
@Implement(MerchantCardPromotionLoader.class)
public interface IMerchantCardPromotionLoader<T> {

    void loadMerchantCardPromotion(String mid,int cardType);

    interface LoadMerchantCardPromotion<T> extends DataBinder{

        void onMerchantCardPromotionResponse(RespDto<MerchantCard> responce);
        void onMerchantCardPromotionFailure(String msg);

    }
}
