package com.moreclub.moreapp.message.api;


import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.model.MerchantCard;
import com.moreclub.moreapp.main.model.MerchantDetails;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.PackageResp;
import com.moreclub.moreapp.main.model.ShareParam;
import com.moreclub.moreapp.main.model.ShareRequestResult;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.message.model.FollowsUser;
import com.moreclub.moreapp.message.model.MessageInteraction;
import com.moreclub.moreapp.message.model.MessageSignin;
import com.moreclub.moreapp.message.model.MessageSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
                                                          @Query("ps") int pageSize);

    @GET("/v1/merchant/open/fullsearch")
    Call<RespDto<List<MerchantItem>>> onLoadSearchMerchants(@Query("query") String query,
                                                            @Query("city") String city,
                                                            @Query("page") int pageNum,
                                                            @Query("size") int pageSize);

    @GET("/v1/merchant/open/featuresearch")
    Call<RespDto<List<MerchantItem>>> onLoadMerchantList(@Query("query") String query,
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
                                                             @Query("ps") int pageSize);

    @GET("/v1/city/open/all")
    Call<RespDto<List<City>>> onLoadCity();

    @GET("v2/mpackage/open/recentlist/{city}")
    Call<RespDto<PackageResp>> onLoadRecentPackages(@Path("city") String city,
                                                    @Query("uid") long uid,
                                                    @Query("pn") int pageNum,
                                                    @Query("ps") int pageSize);

    @GET("/v1/promotion/{mid}")
    Call<RespDto<MerchantCard>> onMerchantCardPromotion(@Path("mid") String mid,
                                                        @Query("cardType") int cardType);

    @GET("/v1/systemmessage/getPushs")
    Call<RespDto<MessageCenterPush>> onLoadMessageFromPush(@Query("uid") String uid,
                                                           @Query("platform") String platform);

    @POST("/v1/share")
    Call<RespDto<ShareRequestResult>> onShare(@Body ShareParam parm);

    @GET("/v1/signin/open/merchants/{mid}")
    Call<RespDto<SignList>> onSignList(@Path("mid") String mid, @Query("uid") String uid);

    @GET("/v1/signin/open/merchants/more/{mid}")
    Call<RespDto<SignList>> onSignMoreList(@Path("mid") String mid, @Query("mid") String mid2,
                                           @Query("uid") String uid,
                                           @Query("pn") String pn, @Query("ps") String ps);

    @GET("/v1/systemmessage/list")
    Call<RespDto<ArrayList<MessageSystem>>> onLoadMessageList(@Query("uid") String uid,
                                                              @Query("platform") String platform,
                                                              @Query("type") String type,
                                                              @Query("pageIndex") String pageIndex,
                                                              @Query("pageSize") String pageSize);

    @GET("/v2/sign/signpushs")
    Call<RespDto<ArrayList<MessageSignin>>> onSigninList(@Query("uid") String uid,
                                                         @Query("page") String page);

    @GET("/v1/user/followers")
    Call<RespDto<ArrayList<FollowsUser>>> loadFollowers(@Query("uid") String uid,
                                                        @Query("ftype") String type,
                                                        @Query("pn") String pn);

    @GET("/v1/user/follows")
    Call<RespDto<ArrayList<FollowsUser>>> loadFollowMerchants(@Query("uid") String uid,
                                                              @Query("ftype") String type,
                                                              @Query("pn") String pn);

    @GET("/v2/signinter/ownIIs")
    Call<RespDto<List<MessageInteraction>>> onLoadInteractionMessages(@Query("uid") Long uid);


    class ApiFactory {
        public static ApiInterface createApi() {
            return RestApi.getInstance().create(ApiInterface.class);
        }

        public static ApiInterface createApi(String token) {
            return RestApi.getInstance().create(ApiInterface.class, token);
        }
    }
}
