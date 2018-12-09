package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.MessageWallReply;
import com.moreclub.moreapp.information.model.UserWriteMessageWall;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-06.
 */

public class MessageWallReplyLoader extends BasePresenter<IMessageWallReplyLoader.LoadMessageWallReplyBinder> implements IMessageWallReplyLoader {

    @Override
    public void onLoadMessageWallReply(Long cid, Integer pn, Integer ps) {
        Callback<RespDto<MessageWallReply>> callback = new Callback<RespDto<MessageWallReply>>() {
            @Override
            public void onResponse(Call<RespDto<MessageWallReply>> call, Response<RespDto<MessageWallReply>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onLoadReplyResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onLoadReplyFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onLoadReplyFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<MessageWallReply>> call, Throwable t) {
                getBinder().onLoadReplyFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMessageWallReply(cid, pn, ps).enqueue(callback);
    }

    @Override
    public void onPostMessageWallReply(String token, UserWriteMessageWall user) {
        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onPostReplyResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onPostReplyFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onPostReplyFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                getBinder().onPostReplyFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onPostMessageWallReply(user).enqueue(callback);
    }
}
