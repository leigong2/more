package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.SalesInfo;
import com.moreclub.moreapp.packages.presenter.ISalesLoader.LoadSalesDataBinder;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-14.
 */

public class SalesLoader extends BasePresenter<LoadSalesDataBinder> implements ISalesLoader{
    @Override
    public void onLoadSales(String city,Long uid,Integer pn,Integer ps) {
        Callback<RespDto<SalesInfo>> callback = new Callback<RespDto<SalesInfo>>() {
            @Override
            public void onResponse(Call<RespDto<SalesInfo>> call, Response<RespDto<SalesInfo>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onSalesResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onSalesFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onSalesFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<SalesInfo>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSalesFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadSales(city,uid,pn,ps).enqueue(callback);
    }

    @Override
    public void loadPrivilege(String cardType, String city) {

        Callback callback = new Callback<RespDto<List<Integer>>>() {
            @Override
            public void onResponse(Call<RespDto<List<Integer>>> call,
                                   Response<RespDto<List<Integer>>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onUserPrivilegeFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onUserPrivilegeResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<Integer>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserPrivilegeFailure(t.getMessage());
            }
        };
        Long uid = MoreUser.getInstance().getUid();
        com.moreclub.moreapp.ucenter.api.ApiInterface.ApiFactory.createApi().onGetOpenPrivilege(
                cardType, city, uid).enqueue(callback);
    }
}
