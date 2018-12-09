package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.Topics;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/7/28.
 */

public class MChannelRecommListLoader extends BasePresenter<IMChannelRecommListLoader.LoadMChannelRecommListDataBinder> implements IMChannelRecommListLoader {
    @Override
    public void onLoadTopicList(int pn, int ps, int city, int topic, long uid) {
        Callback<RespDto<Topics>> callback = new Callback<RespDto<Topics>>() {
            @Override
            public void onResponse(Call<RespDto<Topics>> call, Response<RespDto<Topics>> response) {
                if (getBinder() != null && response != null && response.body() != null) {
                    getBinder().onLoadTopicListResponse(response.body());
                } else if (getBinder() != null) {
                    getBinder().onLoadTopicListFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<Topics>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onLoadTopicListFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi().
                onLoadTopicList(pn, ps, city, topic, uid).enqueue(callback);
    }

    @Override
    public void loadMChannelRecomm(long uid, int city, int page, int pageSize) {
        Callback<RespDto<ArrayList<MainChannelItem>>> callback = new Callback<RespDto<ArrayList<MainChannelItem>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MainChannelItem>>> call, Response<RespDto<ArrayList<MainChannelItem>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMChannelRecommResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMChannelRecommFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onMChannelRecommFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MainChannelItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMChannelRecommFailure(t.getMessage());
            }
        };
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi().
                onLoadChannelRecommList(uid, city, page, pageSize).enqueue(callback);


    }


}
