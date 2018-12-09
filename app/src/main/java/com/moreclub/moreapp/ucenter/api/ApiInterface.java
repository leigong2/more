package com.moreclub.moreapp.ucenter.api;


import android.content.Context;

import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.SignInterResult;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantDetails;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.main.model.UserSignSetting;
import com.moreclub.moreapp.ucenter.model.AliYunAuth;
import com.moreclub.moreapp.ucenter.model.AliYunAuthParm;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;
import com.moreclub.moreapp.ucenter.model.BindedUser;
import com.moreclub.moreapp.ucenter.model.CheckPackageParam;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;
import com.moreclub.moreapp.ucenter.model.CooperationMerchant;
import com.moreclub.moreapp.ucenter.model.DetailOrder;
import com.moreclub.moreapp.ucenter.model.LikeUser;
import com.moreclub.moreapp.ucenter.model.OrderRepayValidateResult;
import com.moreclub.moreapp.ucenter.model.OrderResp;
import com.moreclub.moreapp.ucenter.model.PackageCheck;
import com.moreclub.moreapp.ucenter.model.PackageCheckItem;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.ucenter.model.ReportParam;
import com.moreclub.moreapp.ucenter.model.ReportResult;
import com.moreclub.moreapp.ucenter.model.SecurityQuestions;
import com.moreclub.moreapp.ucenter.model.Topics;
import com.moreclub.moreapp.ucenter.model.UploadUserImage;
import com.moreclub.moreapp.ucenter.model.UserAnswer;
import com.moreclub.moreapp.ucenter.model.UserBindPhone;
import com.moreclub.moreapp.ucenter.model.UserCenter;
import com.moreclub.moreapp.ucenter.model.UserCheckAnswer;
import com.moreclub.moreapp.ucenter.model.UserCouponResult;
import com.moreclub.moreapp.ucenter.model.UserDetails;
import com.moreclub.moreapp.ucenter.model.UserDetailsV2;
import com.moreclub.moreapp.ucenter.model.UserFeedback;
import com.moreclub.moreapp.ucenter.model.UserFollowParam;
import com.moreclub.moreapp.ucenter.model.UserGoodLike;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserLoadImage;
import com.moreclub.moreapp.ucenter.model.UserLogin;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;
import com.moreclub.moreapp.ucenter.model.UserModify;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;
import com.moreclub.moreapp.ucenter.model.UserOrderDelParam;
import com.moreclub.moreapp.ucenter.model.UserPackageEvaluate;
import com.moreclub.moreapp.ucenter.model.UserPassword;
import com.moreclub.moreapp.ucenter.model.UserPrivilege;
import com.moreclub.moreapp.ucenter.model.UserQueryQuestion;
import com.moreclub.moreapp.ucenter.model.UserSecurityQuestion;
import com.moreclub.moreapp.ucenter.model.UserTag;
import com.moreclub.moreapp.ucenter.model.UserUgc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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
                                                         @Query("userID") String userID);

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


    @POST("/v1/user/bindphone")
    Call<RespDto<String>> onBindPhone(@Body UserBindPhone user);

    @POST("/v2/user/open/bindphone")
    Call<RespDto<String>> onBindPhoneOpen(@Body UserBindPhone user);

    @POST("/v1/user/open/wxlogin")
    Call<RespDto<UserLoginInfo>> onLoadWXLoginOld(@Body UserLogin userLogin);

    @POST("/v2/user/open/wxlogin")
    Call<RespDto<UserLoginInfo>> onLoadWXLogin(@Body UserLogin userLogin);

    @POST("/v1/user/open/mblogin")
    Call<RespDto<UserLoginInfo>> onLoadPassLoginOld(@Body UserLogin userLogin);

    @POST("/v2/user/open/mblogin")
    Call<RespDto<UserLoginInfo>> onLoadPassLogin(@Body UserLogin userLogin);

    @POST("/v1/user/open/wblogin")
    Call<RespDto<UserLoginInfo>> onLoadWBLoginOld(@Body UserLogin userLogin);

    @POST("/v2/user/open/wblogin")
    Call<RespDto<UserLoginInfo>> onLoadWBLogin(@Body UserLogin userLogin);

    @GET("/v1/user/open/authcode")
    Call<RespDto<String>> onGetSMSCodeOld(@Query("phone") String phone,
                                          @Query("appkey") String appkey,
                                          @Query("sign") String sig);

    @GET("/v2/user/open/authcode")
    Call<RespDto<String>> onGetSMSCode(@Query("phone") String phone,
                                       @Query("appkey") String appkey,
                                       @Query("sign") String sig);

    @GET("/v1/user/open/authcode")
    Call<RespDto<String>> onGetSMSCodeFp(@Query("phone") String phone,
                                         @Query("appkey") String appkey,
                                         @Query("sign") String sig,
                                         @Query("fp") String fp);

    @GET("/bgd/user/open/authcode")
    Call<RespDto<String>> onGetSMSCodeFp1Old(@Query("phone") String phone,
                                             @Query("appkey") String appkey,
                                             @Query("sign") String sig,
                                             @Query("fp") String fp);

    @GET("/v2/user/open/authcode")
    Call<RespDto<String>> onGetSMSCodeFp1(@Query("phone") String phone,
                                          @Query("appkey") String appkey,
                                          @Query("sign") String sig,
                                          @Query("fp") String fp);

    @GET("/v2/user/open/bindcode")
    Call<RespDto<String>> onGetSMSCodeBindPhone(@Query("phone") String phone,
                                                @Query("appkey") String appkey,
                                                @Query("sign") String sig,
                                                @Query("fp") String fp);

    @GET("/v1/user/open/checkcode")
    Call<RespDto<String>> onCheckSMSCode(@Query("phone") String phone,
                                         @Query("vcode") String vcode,
                                         @Query("appkey") String appkey,
                                         @Query("sign") String sig);

    @GET("/bgd/user/open/verifycode")
    Call<RespDto<String>> onCheckSMSCodeFpOld(@Query("phone") String phone,
                                              @Query("vcode") String vcode,
                                              @Query("appkey") String appkey,
                                              @Query("sign") String sig,
                                              @Query("fp") String fp);

    @GET("/v2/user/open/verifycode")
    Call<RespDto<String>> onCheckSMSCodeFp(@Query("phone") String phone,
                                           @Query("vcode") String vcode,
                                           @Query("appkey") String appkey,
                                           @Query("sign") String sig,
                                           @Query("fp") String fp);

    @POST("/v1/user/{open}/changepw")
    Call<RespDto<String>> onRenamePwdOld(@Body UserPassword password,
                                         @Path("open") String open);

    @POST("/v2/user/inner/changepw")
    Call<RespDto<String>> onRenamePwd(@Body UserPassword password);

    @POST("/v1/user/securityset")
    Call<RespDto<String>> onPostSecurityAnswer(@Body UserAnswer answer);

    @POST("/v1/user/open/register")
    Call<RespDto<UserLoginInfo>> onLoadRegisterOld(@Body UserLogin userLogin);

    @POST("/v2/user/open/register")
    Call<RespDto<UserLoginInfo>> onLoadRegister(@Body UserLogin userLogin);

    @POST("/v1/feedback/add")
    Call<RespDto<Boolean>> onFeedback(@Body UserFeedback feedback);

    @POST("/v1/user/0/changepw")
    Call<RespDto<String>> onModifyPassword(@Body UserPassword password);

    @POST("/bgd/user/open/changepw")
    Call<RespDto<String>> onForgetModifyPasswordOld(@Body UserPassword password);

    @POST("/v2/user/open/changepw")
    Call<RespDto<String>> onForgetModifyPassword(@Body UserPassword password);

    @POST("/v1/aliyunimg/auth")
    Call<RespDto<AliYunAuth>> onAliYunAuth(@Body AliYunAuthParm parm);

    @POST("/v1/aliyunimg/open/auth")
    Call<RespDto<AliYunAuth>> onAliYunAuthOpen(@Body AliYunAuthParm parm);

    @POST("/v1/user/update")
    Call<RespDto<String>> onUserModify(@Body UserModify parm);

    @GET("/v1/user/userinfo/{uid}")
    Call<RespDto<UserInfo>> onLoadUserInfo(@Path("uid") String uid);

    @GET("/v2/mpackage/order/list/{part}")
    Call<RespDto<OrderResp>> onLoadOrder(@Path("part") String part,
                                         @Query("uid") long uid,
                                         @Query("pn") int pageNum,
                                         @Query("ps") int pageSize);

    @GET("/v2/mpackage/order/detail/{orderid}")
    Call<RespDto<DetailOrder>> onLoadOrderDetail(
            @Path("orderid") long orderid,
            @Query("uid") long uid);

    @POST("/v2/mpackage/order/comment/add")
    Call<RespDto<String>> onUserEvaluatePackage(@Body UserPackageEvaluate parm);

    @GET("/v1/promotion/privilege")
    Call<RespDto<UserPrivilege>> onGetPrivilege(@Query("cardType") String cardType,
                                                @Query("city") String city,
                                                @Query("uid") Long uid);

    @GET("/v1/promotion/open/merchantcounts")
    Call<RespDto<List<Integer>>> onGetOpenPrivilege(@Query("cardType") String cardType,
                                                @Query("city") String city,
                                                @Query("uid") Long uid);

    @GET("/v1/collection/list/{uid}")
    Call<RespDto<ArrayList<CollectlistInfo>>> onLoadCollect(@Path("uid") String uid,
                                                            @Query("type") String type,
                                                            @Query("pageIndex") String pageIndex);

    @GET("/v1/user/mycenter/{uid}")
    Call<RespDto<UserCenter>> onLoadMyCenter(@Path("uid") String uid);

    @GET("/v1/promotion/merchants/{type}")
    Call<RespDto<CooperationMerchant>> onLoadCooperationMerchant(@Path("type") String type,
                                                                 @Query("city") String city,
                                                                 @Query("pageNum") String pageNum,
                                                                 @Query("pageSize") String pageSize);

    @GET("/v1/user/view")
    Call<RespDto<UserDetails>> onLoadUserDetails(@Query("oppsuser") long oppsuser,
                                                 @Query("uid") long uid);

    @GET("/v2/user/profile/{uid}")
    Call<RespDto<UserDetailsV2>> onLoadUserV2Details(@Path("uid") long uid,
                                                     @Query("suid") long suid);

    @GET("/bgd/prefeer/open/subTypeInfo")
    Call<RespDto<UserDetails>> onLoadActiveUserDetailsOld(@Query("sid") long sid,
                                                          @Query("uid") long uid);

    @GET("/v3/prefeer/open/subTypeInfo")
    Call<RespDto<UserDetails>> onLoadActiveUserDetails(@Query("sid") long sid,
                                                       @Query("uid") long uid);

    @GET("/v3/prefeer/open/listByPublisher")
    Call<RespDto<ArrayList<MainChannelItem>>> onLoadActiveUserList(
            @Query("uid") long uid,
            @Query("publisher") long publisher,
            @Query("page") long page,
            @Query("pageSize") long pageSize

    );

    @GET("/v1/dict/open/secursub")
    Call<RespDto<SecurityQuestions>> onGetQuestions();

    @POST("/v1/user/{open}/securityget")
    Call<RespDto<UserSecurityQuestion>> onGetSecurityQuestionOld(@Body UserQueryQuestion user,
                                                                 @Path("open") String open);

    @POST("/v2/user/{open}/securityget")
    Call<RespDto<UserSecurityQuestion.DataBean>> onGetSecurityQuestion(@Body UserQueryQuestion user,
                                                                       @Path("open") String open);

    @POST("/bgd/user/{open}/checkanswer")
    Call<RespDto<String>> onCheckSecurityAnswerOld(@Body UserCheckAnswer answer,
                                                   @Path("open") String open);

    @POST("/v2/user/{open}/checkanswer")
    Call<RespDto<String>> onCheckSecurityAnswer(@Body UserCheckAnswer answer,
                                                @Path("open") String open);

    @POST("/v1/signin/settings/{uid}")
    Call<RespDto<String>> onSecretSign(@Path("uid") String uid, @Body AutoSigninSettings param);

    @GET("/v1/signin/settings/{uid}")
    Call<RespDto<UserSignSetting>> onAutoSignSetting(@Path("uid") String uid);

    @POST("/v1/follow/add")
    Call<RespDto<Boolean>> onFollowAdd(@Body UserFollowParam param);

    @HTTP(method = "DELETE", path = "/v1/follow/del/{uid}/{friend}", hasBody = true)
    Call<RespDto<Boolean>> onFollowDel(@Path("uid") String uid, @Path("friend") String friend, @Body UserFollowParam param);

    @POST("/v1/report/add")
    Call<RespDto<ReportResult>> onReportAdd(@Body ReportParam param);

    @GET("/v2/coupon/userallcoupons")
    Call<RespDto<UserCouponResult>> onUserAllCoupons(@Query("uid") String uid, @Query("pn") String pn);

    @POST("/v2/signinter/invite")
    Call<RespDto<Boolean>> onReplyInvite(@Body SignInterResult param);

    @POST("/v2/pay/repayvalidate")
    Call<RespDto<OrderRepayValidateResult>> onRepayValidate(@Body RepayValidateParam param);

    @POST("/v2/mpackage/order/del/{orderId}")
    Call<RespDto<Boolean>> onOrderDel(@Path("orderId") String orderId, @Body UserOrderDelParam param);

    @POST("/v2/mpackage/order/cancel")
    Call<RespDto<Boolean>> onOrderCancel(@Body UserOrderCancelParam param);

    @GET("/v2/user/mileage/shareApp{uid1}")
    Call<RespDto<Boolean>> onShareAppCallback(@Path("uid1") String uid1, @Query("uid") String uid);

    @GET("/v1/dict/open/usertags")
    Call<RespDto<Map<String, List<UserTag>>>> onLoadUserTags(@Query("modal") String modal);

    @GET("/v2/mpackage/couponcodes/{mid}")
    Call<RespDto<PackageCheck>> onPackageCheckCallback(@Path("mid") String mid,
                                                       @Query("status") String status,
                                                       @Query("pageNum") String pageNum,
                                                       @Query("pageSize") String pageSize);

    @POST("/bgd/mpackage/order/writeoff2")
    Call<RespDto<PackageCheckItem>> onCheckPackage(@Body CheckPackageParam param);

    @GET("v1/user/open/phoneinfo")
    Call<RespDto<BindedUser>> onCheckBindedPhone(@Query("phone") String phone);

    @POST("/v1/album/add")
    Call<RespDto<Boolean>> onUpLoadImages(@Body UploadUserImage param);

    @GET("/v1/album/open/list/{uid}")
    Call<RespDto<List<UserLoadImage>>> onLoadImages(@Path("uid") Long uid,
                                                    @Query("pn") Integer pn,
                                                    @Query("ps") Integer ps);

    @DELETE("/v1/album/delete/{id}")
    Call<RespDto<Boolean>> onDeleteImages(@Path("id") Long id,
                                          @Query("uid") Long uid);

    @POST("/v1/like/add")
    Call<RespDto<LikeUser>> onAddLikeGood(@Body LikeUser userLikeReq);

    @GET("/v1/like/state/{uid}")
    Call<RespDto<Boolean>> onLoadLikeState(@Path("uid") Long uid,
                                           @Query("suid") Long suid);

    @GET("/v1/like/open/list/{uid}")
    Call<RespDto<UserGoodLike>> onLoadLikeList(@Path("uid") Long uid,
                                               @Query("pn") Integer pn,
                                               @Query("ps") Integer ps);

    @GET("/v2/ugc/open/lists")
    Call<RespDto<List<UserUgc>>> onLoadMyUgcList(@Query("uid") Long uid,
                                                 @Query("page") Integer page,
                                                 @Query("pageSize") Integer pageSize,
                                                 @Query("friendUid") Long friendUid,
                                                 @Query("type") Integer type);

    @DELETE("/v2/ugc/delete/{id}")
    Call<RespDto<Boolean>> onDeleteMyUgcList(@Path("id") Long id);

    @GET("/v3/prefeer/open/listByTopic")
    Call<RespDto<Topics>> onLoadTopicList(@Query("page") Integer page,
                                          @Query("pageSize") Integer pageSize,
                                          @Query("city") Integer city,
                                          @Query("topic") Integer topic,
                                          @Query("uid") Long uid);

    class ApiFactory {
        public static ApiInterface createApi() {
            return RestApi.getInstance().create(ApiInterface.class);
        }

        public static ApiInterface createApi(Context context) {
            return RestApi.getInstance().create(context, ApiInterface.class);
        }

        public static ApiInterface createApi(String token) {
            return RestApi.getInstance().create(ApiInterface.class, token);
        }
    }
}
