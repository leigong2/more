package com.moreclub.moreapp.information.api;


import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.model.ActivityDetail;
import com.moreclub.moreapp.information.model.ActivityIntros;
import com.moreclub.moreapp.information.model.ChatGroupAdd;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.information.model.HxRoomDetails;
import com.moreclub.moreapp.information.model.MessageWall;
import com.moreclub.moreapp.information.model.MessageWallReply;
import com.moreclub.moreapp.information.model.NewStore;
import com.moreclub.moreapp.information.model.UserWriteMessageWall;
import com.moreclub.moreapp.main.model.MChannelGood;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.SignInterUser;
import com.moreclub.moreapp.main.model.UGCChannel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    String BASE_URL = Constants.BASE_URL;

    //information

    @GET("/v1/information/open/list")
    Call<RespDto<Headline>> onLoadHeadlineList(@Query("cityId") Integer cityId,
                                               @Query("pageIndex") Integer pageIndex,
                                               @Query("pageSize") Integer pageSize);

    @GET("/v1/merchant/open/latest")
    Call<RespDto<NewStore>> onLoadNewStoreList(@Query("cityCode") String cityCode,
                                               @Query("pageNum") Integer pageNum,
                                               @Query("pageSize") Integer pageSize);

    @GET("/v1/information/open/app/{sid}")
    Call<RespDto<Headline.HeadlineDetail>> onLoadHeadlineDetail(@Path("sid") Integer sid,
                                                                @Query("uid") Long uid);

    @GET("/v1/comment/open/list/{sid}")
    Call<RespDto<List<HeadlineComment>>> onLoadCommentList(@Path("sid") Integer sid,
                                                           @Query("pageIndex") Integer pageIndex,
                                                           @Query("pageSize") Integer pageSize,
                                                           @Query("uid") Long uid);

    @GET("/v2/activity/open/recentlist//{city}")
    Call<RespDto<ActivityData>> onLoadActivitiesList(@Path("city") String cityCode,
                                                     @Query("pn") Integer pn,
                                                     @Query("ps") Integer ps,
                                                     @Query("type") Integer type,
                                                     @Query("uid") Long uid);

    @POST("/v1/comment/addCommentByPostOrToUser")
    Call<RespDto<String>> onSendComment(@Body HeadlineSendComment comment);

    @GET("/v1/activity/open/detail")
    Call<RespDto<ActivityDetail>> onLoadActivitiesDetail(@Query("actId") Integer actId,
                                                         @Query("uid") Long uid);

    @GET("/v1/activity/open/intros")
    Call<RespDto<ActivityIntros>> onLoadActivitiesIntrosOld(@Query("activityId") Integer actId,
                                                         @Query("uid") Long uid);

    @GET("/v2/activity/open/intros")
    Call<RespDto<ActivityIntros>> onLoadActivitiesIntros(@Query("activityId") Integer actId,
                                                         @Query("uid") Long uid);

    @GET("/v1/activity/open/commswall")
    Call<RespDto<List<MessageWall>>> onLoadMessageWall(@Query("actId") Integer actId,
                                                       @Query("pn") Integer pn,
                                                       @Query("ps") Integer ps);

    @GET("/v1/activity/open/commsreply")
    Call<RespDto<MessageWallReply>> onLoadMessageWallReply(@Query("cid") Long cid,
                                                           @Query("pn") Integer pn,
                                                           @Query("ps") Integer ps);

    @POST("/v1/activity/addcomm")
    Call<RespDto<String>> onPostMessageWall(@Body UserWriteMessageWall user);

    @POST("/v1/activity/replycomm")
    Call<RespDto<String>> onPostMessageWallReply(@Body UserWriteMessageWall user);


    @GET("/v1/systemmessage/list")
    Call<RespDto<List<com.moreclub.moreapp.message.model.MessageWall>>> onLoadSystemMessages(@Query("platform") String platform,
                                                                                             @Query("uid") Long uid,
                                                                                             @Query("type") String type,
                                                                                             @Query("pn") Integer pn,
                                                                                             @Query("ps") Integer ps);

    @POST("/v1/group/addSingleUserToChatGroup")
    Call<RespDto<Boolean>> onPostAddChatGroup(@Body ChatGroupAdd param);

    @GET("/v1/group/open/getGroupInfo/{roomid}")
    Call<RespDto<HxRoomDetails>> onLoadRoomHxDetails(@Path("roomid") String roomid);

    @GET("/v3/prefeer/open/listbyCity")
    Call<RespDto<ArrayList<MainChannelItem>>> onLoadChannelSameCityList(@Query("uid") Long uid, @Query("city") Integer city,
                                                                        @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    @GET("/v3/prefeer/open/follws")
    Call<RespDto<ArrayList<MainChannelItem>>> onLoadChannelFollwList(@Query("uid") Long uid,
                                                                     @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    @GET("/v3/prefeer/open/list")
    Call<RespDto<ArrayList<MainChannelItem>>> onLoadChannelRecommList(@Query("uid") Long uid, @Query("city") Integer city,
                                                                      @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    @GET("/v3/prefeer/open/list")
    Call<RespDto<ArrayList<MainChannelItem>>> onLoadChannelList(@Query("uid") Long uid,
                                                                @Query("city") Integer city,
                                                                @Query("page") Integer page,
                                                                @Query("pageSize") Integer pageSize);

    @GET("/v1/information/aordeliketime")
    Call<RespDto<Boolean>> onSetGood(@Query("uid") Long uid,
                                     @Query("fid") Integer fid,
                                     @Query("type") String type);

    @GET("/v1/comment/aordeliketime")
    Call<RespDto<Boolean>> onSetPersonGood(@Query("uid") Long uid,
                                           @Query("cid") Integer cid,
                                           @Query("pid") Integer sid);

    @GET("/v1/information/open/userlikelist")
    Call<RespDto<MChannelGood>> onLoadGoodList(@Query("fid") Integer fid,
                                               @Query("pageIndex") Integer pageIndex,
                                               @Query("pageSize") Integer pageSize);

    @GET("/v1/signinter/open/users/{city}")
    Call<RespDto<ArrayList<SignInterUser>>> onSignInter(@Path("city") String city);

    @GET("/v2/ugc/open/app/{sid}")
    Call<RespDto<UGCChannel>> onLoadUserUgcDetail(@Path("sid") Integer sid,
                                                  @Query("uid") Long uid);
    class ApiFactory {
        public static ApiInterface createApi() {
            return RestApi.getInstance().create(ApiInterface.class);
        }

        public static ApiInterface createApi(String token) {
            return RestApi.getInstance().create(ApiInterface.class, token);
        }
    }
}
