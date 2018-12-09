package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;
import com.moreclub.moreapp.ucenter.model.UserLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/17.
 */

public class UserRegDataLoader extends BasePresenter<IUserRegDataLoader.UserRegDataBinder> implements IUserRegDataLoader{
    @Override
    public void onReg(UserLogin regParam) {
        Callback callback = new Callback<RespDto<UserLoginInfo>>() {
            @Override
            public void onResponse(Call<RespDto<UserLoginInfo>> call,
                                   Response<RespDto<UserLoginInfo>> response) {
                if (response.body()!=null&&response.body().isSuccess()) {
                    getBinder().onUserRegResponse(response.body());
                } else {
                    if (response!=null&&response.body()!=null){
                        if (getBinder() != null)
                            getBinder().onUserRegFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onUserRegFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserLoginInfo>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserRegFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadRegister(regParam).enqueue(callback);
    }
}
