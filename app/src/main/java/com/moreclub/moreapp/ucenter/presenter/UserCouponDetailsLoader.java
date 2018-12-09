package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantCouponDetails;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/6/22.
 */

public class UserCouponDetailsLoader extends BasePresenter<IUserCouponDetailsLoader
        .LoadUserCouponDetailsDataBinder> implements IUserCouponDetailsLoader{

    @Override
    public void onLoadUserCouponDetails(String cid) {

        Callback<RespDto<UserCoupon>> callback = new Callback<RespDto<UserCoupon>>() {
            @Override
            public void onResponse(Call<RespDto<UserCoupon>> call, Response<RespDto<UserCoupon>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onUserCouponDetailsResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onUserCouponDetailsFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onUserCouponDetailsFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserCoupon>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserCouponDetailsFailure(t.getMessage());
            }
        };

        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadUserCouponDetails(cid).enqueue(callback);

    }
}
