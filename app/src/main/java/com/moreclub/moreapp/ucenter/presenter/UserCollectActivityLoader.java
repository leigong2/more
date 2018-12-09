package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-26.
 */

public class UserCollectActivityLoader extends BasePresenter<IUserCollectActivityLoader.LoadUserCollectActivityDataBinder> implements IUserCollectActivityLoader {
    @Override
    public void loadActivity(String uid, String type, String pageIndex) {
        Callback<RespDto<ArrayList<CollectlistInfo>>> callback = new Callback<RespDto<ArrayList<CollectlistInfo>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<CollectlistInfo>>> call, Response<RespDto<ArrayList<CollectlistInfo>>> response) {
                if (getBinder() != null && response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onUserCollectActivityFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onUserCollectActivityResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<CollectlistInfo>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserCollectActivityFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadCollect(uid,type,pageIndex).enqueue(callback);
    }
}
