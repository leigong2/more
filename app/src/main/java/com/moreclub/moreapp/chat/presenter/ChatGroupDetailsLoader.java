package com.moreclub.moreapp.chat.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.api.ApiInterface;
import com.moreclub.moreapp.chat.model.ChatGroupUser;
import com.moreclub.moreapp.information.model.HxRoomDetails;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/6/9.
 */

public class ChatGroupDetailsLoader extends BasePresenter<IChatGroupDetailsLoader.LoaderChatGroupDetailsDataBinder> implements IChatGroupDetailsLoader {
    @Override
    public void loadChatGroupUser(String roomID,String pageIndex,String pageSize) {

        Callback<RespDto<ArrayList<ChatGroupUser>>> callback = new Callback<RespDto<ArrayList<ChatGroupUser>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<ChatGroupUser>>> call, Response<RespDto<ArrayList<ChatGroupUser>>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onChatGroupUserResponse(response.body());
                } else {
                    if (response != null && response.body() != null) {
                        getBinder().onChatGroupUserFailure(response.body().getErrorDesc());
                    } else {
                        getBinder().onChatGroupUserFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<ChatGroupUser>>> call, Throwable t) {
                getBinder().onChatGroupUserFailure(t.getMessage());
            }
        };

        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadChatGroupUser(roomID, pageIndex,pageSize).enqueue(callback);

    }

    @Override
    public void loadRoomHxDetails(String roomId) {

        Callback callback = new Callback<RespDto<HxRoomDetails>>() {
            @Override
            public void onResponse(Call<RespDto<HxRoomDetails>> call,
                                   Response<RespDto<HxRoomDetails>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onRoomHxDetailsFaiure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onRoomHxDetailsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<HxRoomDetails>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onRoomHxDetailsFaiure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadRoomHxDetails(roomId).enqueue(callback);

    }

    @Override
    public void onExitChatGroup(String uid, String roomId) {

        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onExitChatGroupFaiure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onExitChatGroupResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onExitChatGroupFaiure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onExitChatGroup(uid,roomId).enqueue(callback);

    }
}
