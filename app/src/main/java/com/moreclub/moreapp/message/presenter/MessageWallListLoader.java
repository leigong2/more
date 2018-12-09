package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.message.model.MessageWall;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-07.
 */

public class MessageWallListLoader extends BasePresenter<IMessageWallListLoader.LoaderMessageWallListDataBinder> implements IMessageWallListLoader {

    @Override
    public void loadSystemMessage(String token,String platform, Long uid, String type, Integer pageIndex, Integer pageSize) {

        Callback<RespDto<List<MessageWall>>> callback =new Callback<RespDto<List<MessageWall>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MessageWall>>> call, Response<RespDto<List<MessageWall>>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onLoadWallResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onLoadWallFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onLoadWallFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MessageWall>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onLoadWallFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onLoadSystemMessages(platform,uid,type,pageIndex,pageSize).enqueue(callback);

    }
}
