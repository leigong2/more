package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.CheckPackageParam;
import com.moreclub.moreapp.ucenter.model.PackageCheck;
import com.moreclub.moreapp.ucenter.model.PackageCheckItem;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/7/18.
 */

public class PackageCheckListLoader extends
        BasePresenter<IPackageCheckListLoader.LoaderPackageCheckListDataBinder>
        implements IPackageCheckListLoader{


    @Override
    public void packageCheckList(String mid, String status, String pageNum, String pageSize) {

        Callback callback = new Callback<RespDto<PackageCheck>>() {
            @Override
            public void onResponse(Call<RespDto<PackageCheck>> call,
                                   Response<RespDto<PackageCheck>> response) {
                if (getBinder() != null && response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onPackageCheckFailure("401");
                    return;
                }
                if (response.body()!=null&&response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onPackageCheckResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onPackageCheckFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<PackageCheck>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPackageCheckFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).
                onPackageCheckCallback(mid, status, pageNum, pageSize).enqueue(callback);
    }

    @Override
    public void onCheckPackage(CheckPackageParam param) {

        Callback callback = new Callback<RespDto<PackageCheckItem>>() {
            @Override
            public void onResponse(Call<RespDto<PackageCheckItem>> call,
                                   Response<RespDto<PackageCheckItem>> response) {
                if (getBinder() != null && response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onCheckFailure("401");
                    return;
                }
                if (response.body()!=null&&response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onCheckResponse(response.body());
                } else {
                    if (getBinder() != null&&response.body()!=null&&response.body().getErrorDesc()!=null) {
                        getBinder().onCheckFailure(response.body().getErrorDesc());
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<PackageCheckItem>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPackageCheckFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).
                onCheckPackage(param).enqueue(callback);
    }
}
