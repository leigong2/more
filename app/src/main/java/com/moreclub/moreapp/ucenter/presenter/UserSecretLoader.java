package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.UserSignSetting;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/26.
 */

public class UserSecretLoader extends BasePresenter<IUserSecretLoader.LoaderUserSecretDataBinder> implements IUserSecretLoader {
    @Override
    public void onSecretSign(String uid,AutoSigninSettings param) {
        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onSecretSignFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onSecretSignResponse(response.body());
                }
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
    public void loadSignSetting(String uid) {
        Callback callback = new Callback<RespDto<UserSignSetting>>() {
            @Override
            public void onResponse(Call<RespDto<UserSignSetting>> call,
                                   Response<RespDto<UserSignSetting>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onSignSettingFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onSignSettingResponse(response.body());
                }
            }
            @Override
            public void onFailure(Call<RespDto<UserSignSetting>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSignSettingFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onAutoSignSetting(
                uid).enqueue(callback);
    }
}
