package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantSupportCoupon;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.model.UserCouponResult;
import com.moreclub.moreapp.ucenter.model.UserDetails;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/6/15.
 */

public class UserCouponListLoader extends BasePresenter<IUserCouponListLoader.LoadUserCouponListDataBinder> implements IUserCouponListLoader {
    @Override
    public void loadUserCoupon(String uid,String pn) {

        Callback callback = new Callback<RespDto<UserCouponResult>>() {
            @Override
            public void onResponse(Call<RespDto<UserCouponResult>> call,
                                   Response<RespDto<UserCouponResult>> response) {
                if (getBinder() != null && response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onUserCouponFailure("401");
                    return;
                }
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onUserCouponResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserCouponResult>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserCouponFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onUserAllCoupons(uid,pn).enqueue(callback);

    }
}
