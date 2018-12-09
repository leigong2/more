package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.PackageDetailsInfo;
import com.moreclub.moreapp.packages.model.PackageOrder;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/27.
 */

public class PackageConfirmOrderLoader extends BasePresenter<IPackageConfirmOrderLoader.LoaderPackageConfirmOrder> implements IPackageConfirmOrderLoader{

    @Override
    public void packageOrder(PackageOrder param) {

        Callback callback = new Callback<RespDto<PackageDetailsInfo>>() {
            @Override
            public void onResponse(Call<RespDto<PackageDetailsInfo>> call,
                                   Response<RespDto<PackageDetailsInfo>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onPackageOrderFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onPackageOrderResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onPackageOrderFailure(response.body().getErrorDesc());
                }
            }

            @Override
            public void onFailure(Call<RespDto<PackageDetailsInfo>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPackageOrderFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onPackageOrder(param).enqueue(callback);

    }

    @Override
    public void loadUserSupportCoupons(String uid, String mid) {
        Callback callback = new Callback<RespDto<ArrayList<UserCoupon>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<UserCoupon>>> call,
                                   Response<RespDto<ArrayList<UserCoupon>>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onUserSupportCouponsFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
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
        ApiInterface.ApiFactory.createApi(token).onLoadUserSupportCoupons(uid,mid).enqueue(callback);

    }
}
