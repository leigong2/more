package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantCouponDetails;
import com.moreclub.moreapp.main.model.MerchantSupportCoupon;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/6/15.
 */

public class MerchantCouponDetailsLoader extends BasePresenter<IMerchantCouponDetailsLoader.LoadMerchantCouponDetailsDataBinder> implements IMerchantCouponDetailsLoader {
    @Override
    public void onLoadMerchantCouponDetails(String cid) {

        Callback<RespDto<MerchantCouponDetails>> callback = new Callback<RespDto<MerchantCouponDetails>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantCouponDetails>> call, Response<RespDto<MerchantCouponDetails>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onMerchantCouponDetailsResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onMerchantCouponDetailsFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<MerchantCouponDetails>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantCouponDetailsFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onLoadMerchantCouponDetails(cid).enqueue(callback);
    }
}
