package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.model.MChannelGood;
import com.moreclub.moreapp.main.model.UGCChannel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-07-28-0028.
 */

public class MChannelDetailsLoader extends BasePresenter<IMChannelDetailsLoader.LoadMChannelDetailsBinder> implements IMChannelDetailsLoader {

    @Override
    public void onLoadMChannelDetails(Integer sid, Long uid) {
        Callback<RespDto<Headline.HeadlineDetail>> callback = new Callback<RespDto<Headline.HeadlineDetail>>() {
            @Override
            public void onResponse(Call<RespDto<Headline.HeadlineDetail>> call, Response<RespDto<Headline.HeadlineDetail>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onGetMChannelDetailsResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onGetMChannelDetailsFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onGetMChannelDetailsFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<Headline.HeadlineDetail>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetMChannelDetailsFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadHeadlineDetail(sid, uid).enqueue(callback);
    }

    @Override
    public void onLoadUgcDetails(Integer sid, Long uid) {
        Callback<RespDto<UGCChannel>> callback = new Callback<RespDto<UGCChannel>>() {
            @Override
            public void onResponse(Call<RespDto<UGCChannel>> call, Response<RespDto<UGCChannel>> response) {
                if (getBinder()!=null&&response!=null&&response.body()!=null) {
                    getBinder().onGetUgcDetailsResponse(response.body());
                } else if (getBinder()!=null)
                    getBinder().onGetUgcDetailsFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<UGCChannel>> call, Throwable t) {
                if (getBinder()!=null)
                    getBinder().onGetUgcDetailsFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadUserUgcDetail(sid, uid).enqueue(callback);
    }

    @Override
    public void onLoadCommentList(Integer sid, Integer pageIndex, Integer pageSize, Long uid) {


        Callback<RespDto<List<HeadlineComment>>> callback = new Callback<RespDto<List<HeadlineComment>>>() {
            @Override
            public void onResponse(Call<RespDto<List<HeadlineComment>>> call, Response<RespDto<List<HeadlineComment>>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onGetCommentListResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onGetCommentListFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onGetCommentListFailure("");
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<HeadlineComment>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetCommentListFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadCommentList(sid, pageIndex, pageSize, uid).enqueue(callback);
    }

    @Override
    public void onLoadGoodList(Integer fid, Integer pageIndex, Integer pageSize) {
        Callback<RespDto<MChannelGood>> callback = new Callback<RespDto<MChannelGood>>() {
            @Override
            public void onResponse(Call<RespDto<MChannelGood>> call, Response<RespDto<MChannelGood>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onGetGoodListResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<MChannelGood>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetGoodListFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadGoodList(fid, pageIndex, pageSize).enqueue(callback);
    }

    @Override
    public void onSetGood(String token, Long uid, Integer fid, String type) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onSetResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onSetFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSetFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onSetGood(uid, fid, type).enqueue(callback);
    }

    @Override
    public void onSetPersonGood(String token, Long uid, Integer cid) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onSetPersonGoodResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onSetPersonGoodFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSetPersonGoodFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onSetPersonGood(uid, cid,-1).enqueue(callback);
    }

    @Override
    public void onSendComment(String token, HeadlineSendComment comment) {
        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (getBinder() != null && response != null) {
                    if (response.body() != null && response.body().isSuccess()) {
                        getBinder().onSendCommentResponse(response.body());
                    } else {
                        if (response != null && response.body() != null) {
                            getBinder().onSendCommentFailure(response.body().getErrorDesc());
                        } else {
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
        ApiInterface.ApiFactory.createApi(token).onSendComment(comment).enqueue(callback);
    }
}
