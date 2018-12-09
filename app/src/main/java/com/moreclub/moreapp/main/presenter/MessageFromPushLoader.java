package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/4/14.
 */

public class MessageFromPushLoader extends BasePresenter<IMessageFromPushLoader.LoadMessageFromPushDataBinder> implements IMessageFromPushLoader {
    @Override
    public void loadMessageFromPush(String uid, String platform) {
        Callback callback = new Callback<RespDto<MessageCenterPush>>() {
            @Override
            public void onResponse(Call<RespDto<MessageCenterPush>> call,
                                   Response<RespDto<MessageCenterPush>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onMessageFromPushFailure("401");
                    return;
                }
                if (response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onMessageFromPushResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onMessageFromPushFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<MessageCenterPush>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMessageFromPushFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadMessageFromPush(uid, platform).enqueue(callback);
    }

    @Override
    public void clearRedPointPush(String token, String type, Long uid) {
        Callback<Void> callback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (getBinder() != null)
                    getBinder().clearRedPointPushResponse();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().clearRedPointPushFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).clearRedPointPush(type, uid).enqueue(callback);
    }

    @Override
    public void closePush(String token, Boolean colsePush, Long uid) {

        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null) {
                    getBinder().closePushResponse(response.body());
                } else if (getBinder() != null) {
                    getBinder().closePushFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().closePushFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi(token).onClosePush(colsePush, uid).enqueue(callback);
    }
}
