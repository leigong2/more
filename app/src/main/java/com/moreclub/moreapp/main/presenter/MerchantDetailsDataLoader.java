package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.main.model.MerchantDetails;
import com.moreclub.moreapp.main.model.MerchantSupportCoupon;
import com.moreclub.moreapp.main.model.MerchantSupportCouponResult;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.UserSignParam;
import com.moreclub.moreapp.main.model.UserSignResult;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.util.MoreUser;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/2/28.
 */

public class MerchantDetailsDataLoader extends BasePresenter<IMerchantDetailsDataLoader.MerchantDetailsDataBinder> implements IMerchantDetailsDataLoader {
    @Override
    public void onLoadMerchantDetails(String mid, String userID) {

        Callback<RespDto<MerchantDetails>> callback = new Callback<RespDto<MerchantDetails>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantDetails>> call, Response<RespDto<MerchantDetails>> response) {

                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onMerchantDetailsResponse(response.body());

            }

            @Override
            public void onFailure(Call<RespDto<MerchantDetails>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantDetailsFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onLoadMerchantDetails(mid, userID).enqueue(callback);

    }

    @Override
    public void onLoadMerchantRichText(String mid) {
        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {

                if (response != null && response.body() != null && response.body().isSuccess())
                    if (getBinder() != null)
                        getBinder().onMerchantDetailsRichTextResponse(response.body());

            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantDetailsRichTextFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onLoadMerchantDetailsWebContent(mid).enqueue(callback);
    }

    @Override
    public void onLoadSignList(String mid, String uid) {
        Callback<RespDto<SignList>> callback = new Callback<RespDto<SignList>>() {
            @Override
            public void onResponse(Call<RespDto<SignList>> call, Response<RespDto<SignList>> response) {

                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onSignListFailure("401");
                    return;
                }
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onSignListResponse(response.body());
                }

            }

            @Override
            public void onFailure(Call<RespDto<SignList>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSignListFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onSignList(mid, uid).enqueue(callback);
    }

    @Override
    public void userSign(UserSignParam param) {

        Callback<RespDto<UserSignResult>> callback = new Callback<RespDto<UserSignResult>>() {
            @Override
            public void onResponse(Call<RespDto<UserSignResult>> call, Response<RespDto<UserSignResult>> response) {

                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onSignFailure("401");
                    return;
                }
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onSignResponse(response.body());
                }

            }

            @Override
            public void onFailure(Call<RespDto<UserSignResult>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSignFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onSign(param).enqueue(callback);
    }

    @Override
    public void onSecretSign(String uid, AutoSigninSettings param) {

        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onSecretSignFailure("401");
                    return;
                }
                if (getBinder() != null)
                    getBinder().onSecretSignResponse(response.body());

            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSecretSignFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onSecretSign(
                uid, param).enqueue(callback);
    }

    @Override
    public void onLoadMerchantActivity(Integer mid, Long uid, Integer pn, Integer ps) {
        Callback<RespDto<List<MerchantActivity>>> callback = new Callback<RespDto<List<MerchantActivity>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantActivity>>> call, Response<RespDto<List<MerchantActivity>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMerchantActivityResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMerchantActivityFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onMerchantActivityFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantActivity>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantActivityFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMerchantActivity(mid, uid, pn, ps).enqueue(callback);
    }

    @Override
    public void onLoadMerchantCouponSupport(String mid) {

        Callback<RespDto<ArrayList<MerchantSupportCoupon>>> callback = new Callback<RespDto<ArrayList<MerchantSupportCoupon>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MerchantSupportCoupon>>> call, Response<RespDto<ArrayList<MerchantSupportCoupon>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMerchantCouponSupportResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMerchantCouponSupportFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onMerchantCouponSupportFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MerchantSupportCoupon>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantCouponSupportFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMerchantsupport(mid).enqueue(callback);
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
                if (response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onUserSupportCouponsResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onUserSupportCouponsFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<UserCoupon>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserSupportCouponsFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.packages.api.ApiInterface.ApiFactory.createApi(token).onLoadUserSupportCoupons(uid, mid).enqueue(callback);

    }
}
