package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.MerchantPackage;
import com.moreclub.moreapp.packages.model.PackageBaseInfo;
import com.moreclub.moreapp.packages.model.PackageDetailsInfo;
import com.moreclub.moreapp.packages.model.PackageList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageMainListLoader extends BasePresenter<IPackageMainListLoader.LoaderPackageMainListDataBinder> implements IPackageMainListLoader {
    @Override
    public void packageListLoad(String chooseCity, String uid,String currentPage,String pageNumber) {

        Callback callback = new Callback<RespDto<PackageList>>() {
            @Override
            public void onResponse(Call<RespDto<PackageList>> call,
                                   Response<RespDto<PackageList>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onPackageListFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onPackageListResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<PackageList>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPackageListFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadSuperMainPackages(chooseCity,uid,
                currentPage, pageNumber).enqueue(callback);
    }

    @Override
    public void loadMerchantPackage(String mid, String pn, String ps, String uid) {

        Callback callback = new Callback<RespDto<MerchantPackage>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantPackage>> call,
                                   Response<RespDto<MerchantPackage>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onMerchantPackageFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onMerchantPackageResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<MerchantPackage>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantPackageFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMerchantPackage(mid,pn,ps,uid).enqueue(callback);
    }

}
