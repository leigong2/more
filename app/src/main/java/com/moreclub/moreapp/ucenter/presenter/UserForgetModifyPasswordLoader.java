package com.moreclub.moreapp.ucenter.presenter;

import android.content.Context;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/17.
 */

public class UserForgetModifyPasswordLoader extends BasePresenter<IUserForgetModifyPasswordLoader.LoaderUserModifyPassword> implements IUserForgetModifyPasswordLoader {

    @Override
    public void onModifyPassword(UserPassword param) {
        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onUserModifyPasswordResponse(response.body());
                } else {
                    if (getBinder()!=null && response != null && response.body() != null) {
                        getBinder().onUserModifyPasswordFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onUserModifyPasswordFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserModifyPasswordFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onForgetModifyPassword(param).enqueue(callback);
    }
}
