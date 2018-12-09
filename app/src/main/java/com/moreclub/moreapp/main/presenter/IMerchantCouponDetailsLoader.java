package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantCouponDetails;

/**
 * Created by Captain on 2017/6/15.
 */
@Implement(MerchantCouponDetailsLoader.class)
public interface IMerchantCouponDetailsLoader<T> {

    void onLoadMerchantCouponDetails(String cid);

    interface LoadMerchantCouponDetailsDataBinder<T> extends DataBinder{

        void onMerchantCouponDetailsResponse(RespDto<MerchantCouponDetails> response);
        void onMerchantCouponDetailsFailure(String msg);
    }
}
