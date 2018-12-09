package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.PayBillOrder;
import com.moreclub.moreapp.packages.model.PayBillOrderParam;
import com.moreclub.moreapp.packages.model.UserMileage;
import com.moreclub.moreapp.ucenter.model.RateCardInfo;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/4/13.
 */

public class PromotionConditionLoader extends BasePresenter<IPromotionConditionLoader.LoaderPromotionConditionDataBinder> implements IPromotionConditionLoader {

    @Override
    public void loadUserPromotionCondition(String mid, String uid) {

        Callback callback = new Callback<RespDto<RateCardInfo>>() {
            @Override
            public void onResponse(Call<RespDto<RateCardInfo>> call,
                                   Response<RespDto<RateCardInfo>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onPromotionConditionFailure("401");
                    return;
                }
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onPromotionConditionResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<RateCardInfo>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPromotionConditionFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadPromotionCondition(mid, uid).enqueue(callback);

    }

    @Override
    public void loadBillOrder(PayBillOrderParam orderParam) {

        Callback callback = new Callback<RespDto<PayBillOrder>>() {
            @Override
            public void onResponse(Call<RespDto<PayBillOrder>> call,
                                   Response<RespDto<PayBillOrder>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onPayBillOrderFailure("401");
                    return;
                }
                if (response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onPayBillOrderResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onPayBillOrderFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<PayBillOrder>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPayBillOrderFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onPayBillOrder(orderParam).enqueue(callback);

    }

    @Override
    public void loadUserSupportCoupons(String uid, String mid) {
        Callback callback = new Callback<RespDto<ArrayList<UserCoupon>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<UserCoupon>>> call,
                                   Response<RespDto<ArrayList<UserCoupon>>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onUserSupportCouponsFailure("401");
                    return;
                }
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onUserSupportCouponsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<UserCoupon>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserSupportCouponsFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadUserSupportCoupons(uid, mid).enqueue(callback);


    }

    @Override
    public void loadMileage() {
        Callback callback = new Callback<RespDto<UserMileage>>() {
            @Override
            public void onResponse(Call<RespDto<UserMileage>> call,
                                   Response<RespDto<UserMileage>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onMileageFailure("401");
                    return;
                }
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onMileageResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserMileage>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMileageFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onMileage("" + MoreUser.getInstance().getUid(),
                "" + MoreUser.getInstance().getUid()).enqueue(callback);
    }
}
