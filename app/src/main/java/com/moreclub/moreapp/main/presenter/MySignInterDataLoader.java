package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.UserSignInterList;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-09-14-0014.
 */

public class MySignInterDataLoader extends BasePresenter<IMySignInterDataLoader.LoadMySignInter> implements IMySignInterDataLoader {

    @Override
    public void onLoadUserInter(Long uid,Integer pn,Integer ps) {
        Callback<RespDto<UserSignInterList>> callback = new Callback<RespDto<UserSignInterList>>() {
            @Override
            public void onResponse(Call<RespDto<UserSignInterList>> call, Response<RespDto<UserSignInterList>> response) {
                if (getBinder() != null && response != null && response.body() != null) {
                    getBinder().onUserInterResponse(response.body());
                } else if (getBinder() != null) {
                    getBinder().onUserInterFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserSignInterList>> call, Throwable t) {
                if (getBinder() != null) {
                    if ("401".equals(t.getMessage())) {
                        AppUtils.pushLeftActivity(MainApp.getContext(), UseLoginActivity.class);
                        return;
                    }
                    getBinder().onUserInterFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi(MoreUser.getInstance().getAccess_token())
                .onloadUserInterList(uid,pn,ps).enqueue(callback);
    }
}
