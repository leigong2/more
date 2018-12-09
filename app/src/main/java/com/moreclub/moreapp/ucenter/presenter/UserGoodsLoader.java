package com.moreclub.moreapp.ucenter.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.UserLikesItem;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserGoodLike;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-08-25-0025.
 */

public class UserGoodsLoader extends BasePresenter<IUserGoodsLoader.LoaderUserGoodsDataBinder> implements IUserGoodsLoader {
    @Override
    public void onLoadUserGoods(String token, Long uid, int pn, int ps) {
        Callback<RespDto<UserGoodLike>> callback = new Callback<RespDto<UserGoodLike>>() {
            @Override
            public void onResponse(Call<RespDto<UserGoodLike>> call, Response<RespDto<UserGoodLike>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onLoadGoodsResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onLoadGoodsFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<UserGoodLike>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onLoadGoodsFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onLoadLikeList(uid, pn, ps).enqueue(callback);
    }
}
