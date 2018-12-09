package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.ActivityDetail;
import com.moreclub.moreapp.information.model.ActivityIntros;
import com.moreclub.moreapp.information.model.ChatGroupAdd;
import com.moreclub.moreapp.information.model.HxRoomDetails;
import com.moreclub.moreapp.information.model.MessageWall;
import com.moreclub.moreapp.message.model.MessageSignin;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-25.
 */

public class LiveDataLoader extends BasePresenter<ILiveDataLoader.LoadLiveDataBinder> implements ILiveDataLoader {
    @Override
    public void onLoadLiveDetail(Integer actId, Long uid) {
        Callback<RespDto<ActivityDetail>> callback = new Callback<RespDto<ActivityDetail>>() {
            @Override
            public void onResponse(Call<RespDto<ActivityDetail>> call, Response<RespDto<ActivityDetail>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onLiveDetailResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onLiveDetailFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onLiveDetailFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ActivityDetail>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onLiveDetailFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadActivitiesDetail(actId,uid).enqueue(callback);
    }

    @Override
    public void onLoadLiveIntros(Integer actId, Long uid) {
        Callback<RespDto<ActivityIntros>> callback = new Callback<RespDto<ActivityIntros>>() {
            @Override
            public void onResponse(Call<RespDto<ActivityIntros>> call, Response<RespDto<ActivityIntros>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onLiveIntrosResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onLiveIntrosFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onLiveIntrosFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ActivityIntros>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onLiveIntrosFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadActivitiesIntros(actId,uid).enqueue(callback);

    }

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
        ApiInterface.ApiFactory.createApi().onLoadMessageWall(actId,pn,ps).enqueue(callback);
    }

    @Override
    public void onAddUserChatGroup(ChatGroupAdd param) {

        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onAddUserChatGroupFaiure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onAddUserChatGroupResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onAddUserChatGroupFaiure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onPostAddChatGroup(param).enqueue(callback);

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
        ApiInterface.ApiFactory.createApi().onLoadRoomHxDetails(roomId).enqueue(callback);

    }

}
