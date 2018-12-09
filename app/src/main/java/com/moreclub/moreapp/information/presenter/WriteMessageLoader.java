package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.MessageWall;
import com.moreclub.moreapp.information.model.UserWriteMessageWall;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-01.
 */

public class WriteMessageLoader extends BasePresenter<IWriteMessageLoader.WriteMessageBinder> implements IWriteMessageLoader {
    @Override
    public void onLoadMessageWall(Integer actId, Integer pn, Integer ps) {
        Callback<RespDto<List<MessageWall>>> callback = new Callback<RespDto<List<MessageWall>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MessageWall>>> call, Response<RespDto<List<MessageWall>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMessageWallResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMessageWallFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onMessageWallFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MessageWall>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMessageWallFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMessageWall(actId, pn, ps).enqueue(callback);
    }

    @Override
    public void onPostMessageWall(String token,UserWriteMessageWall user) {

        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().writeMessageResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().writeMessageFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().writeMessageFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().writeMessageFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onPostMessageWall(user).enqueue(callback);
    }
}
