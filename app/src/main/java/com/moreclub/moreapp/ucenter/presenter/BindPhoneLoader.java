package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.BindedUser;
import com.moreclub.moreapp.ucenter.model.UserBindPhone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-12.
 */

public class BindPhoneLoader extends BasePresenter<IBindPhoneLoader.LoadCheckCodeBinder> implements IBindPhoneLoader {

    @Override
    public void userGetSMSCodeBindPhone(String phone, String appkey, String sign, String fp) {
        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onUseGetSMSCodeResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onUseGetSMSCodeFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onUseGetSMSCodeFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUseGetSMSCodeFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onGetSMSCodeBindPhone(phone, appkey, sign, fp).enqueue(callback);
    }

    @Override
    public void userCheckSMSCode(String phone, String vcode, String appkey, String sign) {
        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onUseCheckSMSCodeResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onUseCheckSMSCodeFailure(response.body().getErrorDesc());
                    } else if (getBinder() != null) {
                        if (getBinder() != null)
                            getBinder().onUseCheckSMSCodeFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUseCheckSMSCodeFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onCheckSMSCode(phone, vcode, appkey, sign).enqueue(callback);
    }

    @Override
    public void userCheckBindedPhone(String phone) {
        Callback<RespDto<BindedUser>> callback = new Callback<RespDto<BindedUser>>() {
            @Override
            public void onResponse(Call<RespDto<BindedUser>> call, Response<RespDto<BindedUser>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onUseCheckBindedPhoneResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onUseCheckBindedPhoneCodeFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<BindedUser>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUseCheckBindedPhoneCodeFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onCheckBindedPhone(phone).enqueue(callback);
    }

    @Override
    public void onBindPhone(String token, UserBindPhone regParam) {
        Callback<RespDto<String>> callBack = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onBindResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onBindFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onBindFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onBindPhoneOpen(regParam).enqueue(callBack);
    }
}
