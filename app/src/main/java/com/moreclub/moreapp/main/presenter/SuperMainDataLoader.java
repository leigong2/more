package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.UpdateBody;
import com.moreclub.moreapp.main.model.UpdateResp;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-09-12-0012.
 */

public class SuperMainDataLoader extends BasePresenter<ISuperMainDataLoader.LoadSuperMainDataBinder>
        implements ISuperMainDataLoader{
    @Override
    public void onLoadRedPointer(String uid, String platform) {
        Callback callback = new Callback<RespDto<MessageCenterPush>>() {
            @Override
            public void onResponse(Call<RespDto<MessageCenterPush>> call,
                                   Response<RespDto<MessageCenterPush>> response) {
                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onRedPointerFailure("401");
                    return;
                }
                if (response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onRedPointerResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onRedPointerFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<MessageCenterPush>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onRedPointerFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadMessageFromPush(uid, platform).enqueue(callback);
    }

    @Override
    public void onUpdateMore(UpdateBody body) {
        Callback<RespDto<UpdateResp>> callback = new Callback<RespDto<UpdateResp>>() {
            @Override
            public void onResponse(Call<RespDto<UpdateResp>> call, Response<RespDto<UpdateResp>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onUpdateResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<UpdateResp>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUpdateFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadUpdate(body).enqueue(callback);
    }
}
