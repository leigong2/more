package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.information.model.SignInterResult;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.ucenter.model.LikeUser;
import com.moreclub.moreapp.ucenter.model.UploadUserImage;
import com.moreclub.moreapp.ucenter.model.UserDetailsV2;
import com.moreclub.moreapp.ucenter.model.UserLoadImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Captain on 2017/5/2.
 */

@Implement(UserDetailsV2Loader.class)
public interface IUserDetailsV2Loader <T>{

    void loadUserDetails(long selfUid,long toUid);
    void onFollowAdd(String uid,String friendID);
    void onFollowDel(String uid,String friendID);
    void onLoadMerchantActivity(Integer mid,Long uid,Integer pn,Integer ps);
    void onReplyInvite(String token,SignInterResult params);
    void onUserActiveDetails(long selfUid,long toUid);
    void loadUserActiveList(long uid,long publisher,int page,int pageSize);
    void onAttentionChannel(ChannelAttentionParam param);
    void onUploadImages(String token, UploadUserImage images);
    void onLikeGood(String token,LikeUser user);
    void onLoadState(String token,long uid, long suid);
    void onPostSignInter(String token,SignInter inter);
    interface LoaderUserDetailsV2DataBinder<T> extends DataBinder{

        void onUserDetailsResponse(RespDto<UserDetailsV2> response);
        void onUserDetailsFailure(String msg);

        void onFollowAddResponse(RespDto<Boolean> response);
        void onFollowAddFailure(String msg);

        void onFollowDelResponse(RespDto<Boolean> response);
        void onFollowDelFailure(String msg);

        void onMerchantActivityResponse(RespDto<List<MerchantActivity>> body);
        void onMerchantActivityFailure(String msg);

        void onReplyInviteResponse(RespDto<Boolean> body);
        void onReplyInviteFailure(String msg);

        void onUserActiveDetailsResponse(RespDto<Boolean> body);
        void onUserActiveDetailsFailure(String msg);

        void onUserActiveListResponse(RespDto<ArrayList<MainChannelItem>> body);
        void onUserActiveListFailure(String msg);

        void onAttentionChannelResponse(RespDto<ChannelAttentionResult> responce);
        void onAttentionChannelFailure(String msg);

        void onUploadImageResponse(RespDto<String> responce);
        void onUploadImageFailure(String msg);

        void onLikeUserResponse(RespDto<Boolean> responce);
        void onLikeUserFailure(String msg);

        void onLoadStateResponse(RespDto<Boolean> responce);
        void onLoadStateFailure(String msg);

        void onPostSignInterResponse(RespDto<Boolean> body);
        void onPostSignInterFailure(String msg);
    }
}
