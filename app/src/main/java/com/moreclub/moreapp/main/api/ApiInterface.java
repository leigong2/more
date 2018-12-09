package com.moreclub.moreapp.main.api;


import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.main.model.AddSignInter;
import com.moreclub.moreapp.main.model.AddUGCParam;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.model.CorrectSignin;
import com.moreclub.moreapp.main.model.HeadlineCount;
import com.moreclub.moreapp.main.model.HomeRecomMerchants;
import com.moreclub.moreapp.main.model.HotMerchant;
import com.moreclub.moreapp.main.model.InterInvites;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.main.model.MerchantCard;
import com.moreclub.moreapp.main.model.MerchantCount;
import com.moreclub.moreapp.main.model.MerchantCouponDetails;
import com.moreclub.moreapp.main.model.MerchantDetails;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.MerchantSupportCoupon;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.MorePlate;
import com.moreclub.moreapp.main.model.PackageResp;
import com.moreclub.moreapp.main.model.ShareParam;
import com.moreclub.moreapp.main.model.ShareRequestResult;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.SignSpaceList;
import com.moreclub.moreapp.main.model.SplashImage;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.main.model.UpLoadCityInfo;
import com.moreclub.moreapp.main.model.UpdateBody;
import com.moreclub.moreapp.main.model.UpdateResp;
import com.moreclub.moreapp.main.model.UserSignInterList;
import com.moreclub.moreapp.main.model.UserSignParam;
import com.moreclub.moreapp.main.model.UserSignResult;
import com.moreclub.moreapp.main.model.UserSignSetting;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;
import com.moreclub.moreapp.ucenter.model.UploadedUGCChannel;
import com.moreclub.moreapp.ucenter.model.UserCoupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    String BASE_URL = Constants.BASE_URL;

    //main
    @GET("/v1/merchant/open/busareas")
    Call<RespDto<List<BizArea>>> onLoadBizArea(@Query("city") String city);

    @GET("/v1/dict/open/merchant/searchtags")
    Call<RespDto<Map<Integer, TagDict>>> onLoadBarFeature();

    @GET("/v1/hpg/open/advertisement")
    Call<RespDto<List<BannerItem>>> onLoadBanner(@Query("cityId") int cityId,
                                                 @Query("pos") String pos,
                                                 @Query("type") int type);

    @GET("/v1/merchant/open/mchantlist/{city}")
    Call<RespDto<List<MerchantItem>>> onLoadMainMerchants(@Path("city") String city,
                                                          @Query("pn") int pageNum,
                                                          @Query("ps") int pageSize,
                                                          @Query("uid") Long uid);

    @GET("/v1/merchant/open/mchantlist/{city}")
    Call<RespDto<List<MerchantItem>>> onLoadMainMerchantsNoUid(@Path("city") String city,
                                                               @Query("pn") int pageNum,
                                                               @Query("ps") int pageSize);

    @GET("/v3/merchant/open/fullsearch")
    Call<RespDto<List<MerchantItem>>> onLoadSearchMerchants(@Query("query") String query,
                                                            @Query("city") String city,
                                                            @Query("page") int pageNum,
                                                            @Query("size") int pageSize,
                                                            @Query("uid") long uid);

    @GET("/v3/merchant/open/fullsearch")
    Call<RespDto<List<MerchantItem>>> onLoadSearchMerchantsNoUid(@Query("query") String query,
                                                                 @Query("city") String city,
                                                                 @Query("page") int pageNum,
                                                                 @Query("size") int pageSize
    );

    @GET("/v1/merchant/open/couponmerchants/{city}")
    Call<RespDto<List<MerchantItem>>> onLoadCouponMerchants(
            @Path("city") String city,
            @Query("modal") int modal,
            @Query("pn") int pn,
            @Query("ps") int ps,
            @Query("location") String location);

    @GET("/v1/merchant/open/featuresearch")
    Call<RespDto<List<MerchantItem>>> onLoadMerchantList(@Query("query") String query,
                                                         @Query("city") String city,
                                                         @Query("type") int type,
                                                         @Query("page") int pageNum,
                                                         @Query("size") int pageSize,
                                                         @Query("uid") long uid);

    @GET("/v1/merchant/open/featuresearch")
    Call<RespDto<List<MerchantItem>>> onLoadMerchantListNoUid(@Query("query") String query,
                                                              @Query("city") String city,
                                                              @Query("type") int type,
                                                              @Query("page") int pageNum,
                                                              @Query("size") int pageSize);

    @GET("/v1/merchant/open/detail/{mid}")
    Call<RespDto<MerchantDetails>> onLoadMerchantDetails(@Path("mid") String mid,
                                                         @Query("uid") String uid);

    @GET("/v1/merchant/open/richtext/{mid}")
    Call<RespDto<String>> onLoadMerchantDetailsWebContent(@Path("mid") String mid);

    @GET("/v1/hpg/open/merchtags")
    Call<RespDto<List<TagDict>>> onLoadSearchTag();

    @GET("/v1/merchant/open/pluslist")
    Call<RespDto<List<MerchantItem>>> onLoadBizAreaMerchants(@Query("bid") int bid,
                                                             @Query("pn") int pageNum,
                                                             @Query("ps") int pageSize,
                                                             @Query("uid") long uid);

    @GET("/v1/merchant/open/pluslist")
    Call<RespDto<List<MerchantItem>>> onLoadBizAreaMerchantsNoUid(@Query("bid") int bid,
                                                                  @Query("pn") int pageNum,
                                                                  @Query("ps") int pageSize);

    @GET("/v1/city/open/all")
    Call<RespDto<List<City>>> onLoadCity();

    @GET("/v1/splash/open/image")
    Call<RespDto<SplashImage>> onLoadSplashImageOld();

    @GET("/v2/splash/open/image/{city}")
    Call<RespDto<SplashImage>> onLoadSplashImage(@Path("city") String city);


    @GET("v2/mpackage/open/recentlist/{city}")
    Call<RespDto<PackageResp>> onLoadRecentPackages(@Path("city") String city,
                                                    @Query("uid") long uid,
                                                    @Query("pn") int pageNum,
                                                    @Query("ps") int pageSize);

    @GET("/v1/promotion/open/{mid}")
    Call<RespDto<MerchantCard>> onMerchantCardPromotion(@Path("mid") String mid, @Query("cardType") int cardType);

    @GET("/v1/systemmessage/getPushs")
    Call<RespDto<MessageCenterPush>> onLoadMessageFromPush(@Query("uid") String uid, @Query("platform") String platform);

    @POST("/v1/share")
    Call<RespDto<ShareRequestResult>> onShare(@Body ShareParam parm);

    @GET("/v1/signin/open/merchants/{mid}")
    Call<RespDto<SignList>> onSignList(@Path("mid") String mid, @Query("uid") String uid);

    @GET("/v1/signin/open/merchants/morespace/{mid}")
    Call<RespDto<SignSpaceList>> onSignMoreList(@Path("mid") String mid, @Query("mid") String mid2, @Query("uid") String uid, @Query("pn") String pn, @Query("ps") String ps);

    @POST("/v1/signin/manual")
    Call<RespDto<UserSignResult>> onSign(@Body UserSignParam param);

    @GET("/v1/signin/settings/{uid}")
    Call<RespDto<UserSignSetting>> onAutoSignSetting(@Path("uid") String uid);

    @POST("/v1/signin/auto")
    Call<RespDto<UserSignResult>> onSignAuto(@Body UserSignParam param);

    @GET("/v1/merchant/open/nearby/{city}")
    Call<RespDto<List<MerchantItem>>> onLoadNearbyMerchants(@Path("city") String city,
                                                            @Query("pn") Integer pn,
                                                            @Query("ps") Integer ps,
                                                            @Query("city") String city1,
                                                            @Query("location") String pageNum,
                                                            @Query("uid") long uid);

    @GET("/v1/merchant/open/nearby/{city}")
    Call<RespDto<List<MerchantItem>>> onLoadNearbyMerchantsNoUid(@Path("city") String city,
                                                                 @Query("pn") Integer pn,
                                                                 @Query("ps") Integer ps,
                                                                 @Query("city") String city1,
                                                                 @Query("location") String pageNum);

    @POST("/v1/signin/correct")
    Call<RespDto<Boolean>> onCorrectSignin(@Body CorrectSignin param);

    @POST("/v2/signinter/add")
    Call<RespDto<MerchantsSignInters>> onAddSignInter(@Body AddSignInter body);

    @GET("/v1/hpg/open/infocount")
    Call<RespDto<HeadlineCount>> onLoadHeadlineCount();

    @GET("/v1/hpg/open/merchantcount")
    Call<RespDto<MerchantCount>> onLoadMerchantCount(@Query("cityCode") String cityCode,
                                                     @Query("cityId") Integer cityId);

    @POST("/v1/signin/settings/{uid}")
    Call<RespDto<String>> onSecretSign(@Path("uid") String uid, @Body AutoSigninSettings param);

    @GET("/v1/merchant/open/acts/{mid}")
    Call<RespDto<List<MerchantActivity>>> onLoadMerchantActivity(@Path("mid") Integer mid,
                                                                 @Query("uid") Long uid,
                                                                 @Query("pn") Integer pn,
                                                                 @Query("ps") Integer ps);

    @GET("/v1/hpg/open/merchants")
    Call<RespDto<HomeRecomMerchants>> onLoadHomeMerchants(@Query("city") String city);

    @GET("/v2/coupon/open/merchantsupport")
    Call<RespDto<ArrayList<MerchantSupportCoupon>>> onLoadMerchantsupport(@Query("mid") String mid);

    @GET("/bgd/coupon/couponmodal/{modal}")
    Call<RespDto<MerchantCouponDetails>> onLoadMerchantCouponDetailsOld(@Path("modal") String modal);

    @GET("/v2/coupon/open/couponmodal/{modal}")
    Call<RespDto<MerchantCouponDetails>> onLoadMerchantCouponDetails(@Path("modal") String modal);

    @GET("/v2/coupon/couponinfo/{rid}")
    Call<RespDto<UserCoupon>> onLoadUserCouponDetails(@Path("rid") String rid);

    @GET("/v2/signinter/merchantInters")
    Call<List<MerchantsSignInters>> onLoadMerchantInters(@Query("mid") Integer mid,
                                                         @Query("pn") Integer pn);

    @GET("/v2/signinter/open/merchantInters")
    Call<List<MerchantsSignInters>> onLoadOpenMerchantInters(@Query("mid") Integer mid,
                                                             @Query("pn") Integer pn);

    @GET("/v2/signinter/signinteraction")
    Call<RespDto<MerchantsSignInters>> onLoadSignInterDetail(@Query("sid") Integer sid);

    @GET("/v2/signinter/interInvites")
    Call<RespDto<List<InterInvites>>> onLoadInterInvitesDetail(@Query("uid") Long uid,
                                                               @Query("fromUid") Long fromUid);

    @GET("/v1/systemmessage/clearCount")
    Call<Void> clearRedPointPush(@Query("type") String type,
                                 @Query("uid") Long uid);

    @DELETE("/v2/signinter/close")
    Call<RespDto<Boolean>> onEndSignInterDetail(@Query("sid") Integer sid);

    @DELETE("/v2/signinter/delete")
    Call<RespDto<Object>> onDeleteSignInterDetail(@Query("sid") Integer sid);

    @POST("/v2/signinter/inter")
    Call<RespDto<Boolean>> onPostSignInter(@Body SignInter body);

    @PUT("/v2/signinter/passOrJoin/{vid}")
    Call<RespDto<Boolean>> onAllowInteraction(@Path("vid") Integer vid);

    @PUT("/v2/user/mileage/modiflyMileage{uid1}")
    Call<RespDto<Boolean>> onModiflyMileage(@Path("uid1") String uid1);

    @GET("/v2/coupon/h5/coupons")
    Call<RespDto<Boolean>> onH5Coupon(@Query("uid") String uid);

    @GET("/v3/hpg/open/merchants")
    Call<RespDto<HotMerchant>> onLoadHotMerchant(@Query("uid") Long uid,
                                                 @Query("city") String city,
                                                 @Query("location") String location,
                                                 @Query("different") Boolean different);

    @POST("/v1/client/open/update")
    Call<RespDto<UpdateResp>> onLoadUpdate(@Body UpdateBody body);

    @GET("/v1/signin/open/morespace/{city}")
    Call<RespDto<SignSpaceList>> onLoadSignInterTotalOld(@Path("city") String city,
                                                         @Query("pn") Integer pn,
                                                         @Query("ps") Integer ps);

    @GET("/v3/signin/open/morespace/{city}")
    Call<RespDto<SignSpaceList>> onLoadSignInterTotal(@Path("city") String city,
                                                      @Query("pn") Integer pn,
                                                      @Query("ps") Integer ps);

    @GET("/v2/signinter/open/cityInters")
    Call<RespDto<List<MerchantsSignInters>>> onLoadSignIntersTotal(@Query("city") String city,
                                                                   @Query("pn") Integer pn);

    @POST("/v3/prefeer/follow")
    Call<RespDto<ChannelAttentionResult>> onChannelAttention(@Body ChannelAttentionParam parm);

    @GET("/v1/information/aordeliketime")
    Call<RespDto<Boolean>> onLikeChannel(@Query("fid") Integer fid,
                                         @Query("uid") long uid,
                                         @Query("type") String type);

    @POST("/v1/user/open/cityInfo")
    Call<RespDto<String>> onUpLoadCityInfo(@Body UpLoadCityInfo body);

    @GET("/v3/prefeer/open/topics")
    Call<RespDto<ArrayList<Topic>>> onLoadtopics(@Query("city") int city);

    @POST("/v2/ugc/add")
    Call<RespDto<UploadedUGCChannel>> onUploadUGC(@Body AddUGCParam ugc);

    @GET("/v2/ugc/open/lists")
    Call<RespDto<ArrayList<Topic>>> onLoadUserUGCList(@Query("uid") int uid, @Query("page") int page, @Query("page") int pageSize);

    @GET("/v2/user/pushsetting")
    Call<RespDto<Boolean>> onClosePush(@Query("colsePush") Boolean colsePush,
                                       @Query("uid") Long uid);

    @GET("/v3/hpg/open/moreplate")
    Call<RespDto<MorePlate>> onloadMorePlate(@Query("city") String city,
                                             @Query("uid") Long uid);

    @GET("/v2/signinter/myInters")
    Call<RespDto<UserSignInterList>> onloadUserInterList(@Query("uid") Long uid,
                                                         @Query("pn") Integer pn,
                                                         @Query("pageSize") Integer pageSize);

    class ApiFactory {
        public static ApiInterface createApi() {
            return RestApi.getInstance().create(ApiInterface.class);
        }

        public static ApiInterface createApi(String token) {
            return RestApi.getInstance().create(ApiInterface.class, token);
        }
    }
}
