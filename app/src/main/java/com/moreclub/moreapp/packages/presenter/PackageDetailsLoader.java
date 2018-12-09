package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.PackageDetailsInfo;
import com.moreclub.moreapp.packages.model.PackageList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageDetailsLoader extends BasePresenter<IPackageDetailsLoader.loadPackageDetailsDataBinder> implements IPackageDetailsLoader {
    @Override
    public void onLoadPackageDetails(String pid, String uid) {
        Callback callback = new Callback<RespDto<PackageDetailsInfo>>() {
            @Override
            public void onResponse(Call<RespDto<PackageDetailsInfo>> call,
                                   Response<RespDto<PackageDetailsInfo>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onPackageDetailsFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onPackageDetailsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<PackageDetailsInfo>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPackageDetailsFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadPackageDetails(pid,uid).enqueue(callback);

    }
}
