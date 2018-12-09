package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/7/27.
 */

public class MainChannelAttentionDataBinder extends
        BasePresenter<IMainChannelAttentionLoader.LoadMainDataBinder>
        implements IMainChannelAttentionLoader {


    @Override
    public void onAttentionChannel(ChannelAttentionParam param) {

        Callback<RespDto<ChannelAttentionResult>> callback
                = new Callback<RespDto<ChannelAttentionResult>>() {
            @Override
            public void onResponse(Call<RespDto<ChannelAttentionResult>> call,
                                   Response<RespDto<ChannelAttentionResult>> response) {
                if (response.body() != null && response.body().isSuccess())
                    if (getBinder() != null) {
                        getBinder().onAttentionChannelResponse(response.body());
                    }
            }

            @Override
            public void onFailure(Call<RespDto<ChannelAttentionResult>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onAttentionChannelFailure(t.getMessage());
                }
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onChannelAttention(param).enqueue(callback);
    }

    @Override
    public void onLikeChannel(int fid, long uid, String type) {
        Callback<RespDto<Boolean>> callback
                = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onLikeChannelResponse(response.body());
                else if (getBinder()!=null)
                    getBinder().onLikeChannelFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onLikeChannelFailure(t.getMessage());
                }
            }
        };

        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLikeChannel(fid, uid, type).enqueue(callback);
    }

    @Override
    public void onSendComment(HeadlineSendComment comment) {

        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (getBinder() != null && response != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onSendCommentResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onSendCommentFailure(response.body().getErrorDesc());
                        } else if (getBinder() != null) {
                            getBinder().onSendCommentFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSendCommentFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi(token).onSendComment(comment).enqueue(callback);
    }
}
