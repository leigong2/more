package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.api.ApiInterface;
import com.moreclub.moreapp.message.model.FollowsUser;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;
import com.moreclub.moreapp.util.MoreUser;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/6/1.
 */

public class MessageFollowListLoader extends BasePresenter<IMessageFollowListLoader.LoaderMessageFollowDataBinder> implements IMessageFollowListLoader {
    @Override
    public void loadFollowsUser(String uid,String type,String pn,int followType) {
        Callback callback = new Callback<RespDto<ArrayList<FollowsUser>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<FollowsUser>>> call,
                                   Response<RespDto<ArrayList<FollowsUser>>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onFollowsUserFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onFollowsUserResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<FollowsUser>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onFollowsUserFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        if (followType==0){
            ApiInterface.ApiFactory.createApi(token).loadFollowers(uid,type,pn).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi(token).loadFollowMerchants(uid,type,pn).enqueue(callback);
        }
    }
}
