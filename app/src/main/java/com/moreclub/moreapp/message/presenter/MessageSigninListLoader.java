package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.api.ApiInterface;
import com.moreclub.moreapp.message.model.MessageSignin;
import com.moreclub.moreapp.message.model.MessageSystem;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/26.
 */

public class MessageSigninListLoader extends BasePresenter<IMessageSigninListLoader.LoaderMessageSigninList> implements IMessageSigninListLoader {
    @Override
    public void loadSigninList(String uid,String page) {

        Callback callback = new Callback<RespDto<ArrayList<MessageSignin>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MessageSignin>>> call,
                                   Response<RespDto<ArrayList<MessageSignin>>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onMessageSigninListFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onMessageSigninResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MessageSignin>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMessageSigninListFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onSigninList(uid,page).enqueue(callback);

    }
}
