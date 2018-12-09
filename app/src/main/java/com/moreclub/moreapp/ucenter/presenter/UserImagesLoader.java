package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserLoadImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-08-24-0024.
 */

public class UserImagesLoader extends BasePresenter<IUserImagesLoader.LoaderUserImagesDataBinder> implements IUserImagesLoader {
    @Override
    public void onloadImages(Long uid,int pn,int ps) {
        Callback<RespDto<List<UserLoadImage>>> callback = new Callback<RespDto<List<UserLoadImage>>>() {
            @Override
            public void onResponse(Call<RespDto<List<UserLoadImage>>> call, Response<RespDto<List<UserLoadImage>>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onloadImagesResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onloadImagesFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<List<UserLoadImage>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onloadImagesFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadImages(uid,pn,ps).enqueue(callback);
    }

    @Override
    public void onDeleteImages(String token,Long id, Long uid) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onDeleteImagesResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onDeleteImagesFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder()!=null)
                    getBinder().onDeleteImagesFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onDeleteImages(id,uid).enqueue(callback);
    }
}
