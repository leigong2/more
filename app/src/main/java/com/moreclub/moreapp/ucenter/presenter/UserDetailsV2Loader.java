package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.information.model.SignInterResult;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.LikeUser;
import com.moreclub.moreapp.ucenter.model.UploadUserImage;
import com.moreclub.moreapp.ucenter.model.UserDetails;
import com.moreclub.moreapp.ucenter.model.UserDetailsV2;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;
import com.moreclub.moreapp.ucenter.model.UserLoadImage;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/2.
 */

public class UserDetailsV2Loader extends BasePresenter<IUserDetailsV2Loader.LoaderUserDetailsV2DataBinder> implements IUserDetailsV2Loader {
    @Override
    public void loadUserDetails(long uid, long suid) {

        Callback callback = new Callback<RespDto<UserDetailsV2>>() {
            @Override
            public void onResponse(Call<RespDto<UserDetailsV2>> call,
                                   Response<RespDto<UserDetailsV2>> response) {
                if (getBinder() != null && response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onUserDetailsFailure("401");
                    return;
                }
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onUserDetailsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserDetailsV2>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserDetailsFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadUserV2Details(uid, suid).enqueue(callback);
    }

    @Override
    public void onFollowAdd(String uid, String friendID) {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onFollowAddFailure("401");
                    return;
                }
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onFollowAddResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onFollowAddFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        UserFollowParam param = new UserFollowParam();
        param.setFriendId(friendID);
        param.setOwnerId(uid);
        ApiInterface.ApiFactory.createApi(token).onFollowAdd(param).enqueue(callback);

    }

    @Override
    public void onFollowDel(String uid, String friendID) {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onFollowDelFailure("401");
                    return;
                }
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onFollowDelResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onFollowDelFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        UserFollowParam param = new UserFollowParam();
        param.setUid(uid);
        param.setFriend(friendID);
        ApiInterface.ApiFactory.createApi(token).onFollowDel(uid, friendID, param).enqueue(callback);
    }

    @Override
    public void onLoadMerchantActivity(Integer mid, Long uid, Integer pn, Integer ps) {
        Callback<RespDto<List<MerchantActivity>>> callback = new Callback<RespDto<List<MerchantActivity>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantActivity>>> call, Response<RespDto<List<MerchantActivity>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMerchantActivityResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMerchantActivityFailure(response.body().getErrorDesc());
                        } else {
                            if (getBinder() != null)
                                getBinder().onMerchantActivityFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantActivity>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantActivityFailure(t.getMessage());
            }
        };
        com.moreclub.moreapp.main.api.ApiInterface.ApiFactory.createApi().onLoadMerchantActivity(mid, uid, pn, ps).enqueue(callback);
    }

    @Override
    public void onReplyInvite(String token, SignInterResult params) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onReplyInviteResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onReplyInviteFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onReplyInvite(params).enqueue(callback);
    }

    @Override
    public void onUserActiveDetails(long selfUid, long toUid) {

        Callback callback = new Callback<RespDto<UserDetails>>() {
            @Override
            public void onResponse(Call<RespDto<UserDetails>> call,
                                   Response<RespDto<UserDetails>> response) {
                if (getBinder() != null && response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onUserActiveDetailsFailure("401");
                    return;
                }
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onUserActiveDetailsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<UserDetails>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserActiveDetailsFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onLoadActiveUserDetails(toUid, selfUid).enqueue(callback);

    }

    @Override
    public void loadUserActiveList(long uid, long publisher, int page, int pageSize) {

        Callback callback = new Callback<RespDto<ArrayList<MainChannelItem>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MainChannelItem>>> call,
                                   Response<RespDto<ArrayList<MainChannelItem>>> response) {
                if (getBinder() != null && response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onUserActiveListFailure("401");
                    return;
                }
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onUserActiveListResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MainChannelItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUserActiveListFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onLoadActiveUserList(uid, publisher, page, pageSize).enqueue(callback);

    }

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
        com.moreclub.moreapp.main.api.ApiInterface.ApiFactory.createApi(token).onChannelAttention(param).enqueue(callback);
    }

    @Override
    public void onUploadImages(String token, UploadUserImage images) {

        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onUploadImageResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onUploadImageFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onUploadImageFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onUpLoadImages(images).enqueue(callback);
    }

    @Override
    public void onLikeGood(String token, LikeUser user) {
        Callback<RespDto<LikeUser>> callback = new Callback<RespDto<LikeUser>>() {
            @Override
            public void onResponse(Call<RespDto<LikeUser>> call, Response<RespDto<LikeUser>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onLikeUserResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onLikeUserFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<LikeUser>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onLikeUserFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onAddLikeGood(user).enqueue(callback);
    }

    @Override
    public void onLoadState(String token, long uid, long suid) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onLoadStateResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onLoadStateFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onLoadStateFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onLoadLikeState(uid, suid).enqueue(callback);
    }

    @Override
    public void onPostSignInter(String token, SignInter inter) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onPostSignInterResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPostSignInterFailure(t.getMessage());
            }

        };
        com.moreclub.moreapp.main.api.ApiInterface.ApiFactory.createApi(token).onPostSignInter(inter).enqueue(callback);
    }
}
