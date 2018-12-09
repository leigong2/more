package com.moreclub.moreapp.packages.api;



import android.content.Context;

import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.model.CollectParam;
import com.moreclub.moreapp.main.model.MerchantDetails;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.packages.model.DictionaryName;
import com.moreclub.moreapp.packages.model.MerchantPackage;
import com.moreclub.moreapp.packages.model.OrderStatus;
import com.moreclub.moreapp.packages.model.PackageDetailsInfo;
import com.moreclub.moreapp.packages.model.PackageList;
import com.moreclub.moreapp.packages.model.PackageOrder;
import com.moreclub.moreapp.packages.model.PackageOrderResult;
import com.moreclub.moreapp.packages.model.PayBillOrder;
import com.moreclub.moreapp.packages.model.PayBillOrderParam;
import com.moreclub.moreapp.packages.model.PayParam;
import com.moreclub.moreapp.packages.model.AppPayResult;
import com.moreclub.moreapp.packages.model.PkgRefundReq;
import com.moreclub.moreapp.packages.model.SalesInfo;
import com.moreclub.moreapp.packages.model.UserMileage;
import com.moreclub.moreapp.packages.model.ZeroPayParam;
import com.moreclub.moreapp.ucenter.model.AliYunAuth;
import com.moreclub.moreapp.ucenter.model.AliYunAuthParm;
import com.moreclub.moreapp.ucenter.model.RateCardInfo;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.model.UserFeedback;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserLogin;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;
import com.moreclub.moreapp.ucenter.model.UserModify;
import com.moreclub.moreapp.ucenter.model.UserPassword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("/v1/user/open/wxlogin")
    Call<RespDto<UserLoginInfo>> onLoadWXLogin(@Body UserLogin userLogin);

    @POST("/v1/user/open/mblogin")
    Call<RespDto<UserLoginInfo>> onLoadPassLogin(@Body UserLogin userLogin);

    @POST("/v1/user/open/wblogin")
    Call<RespDto<UserLoginInfo>> onLoadWBLogin(@Body UserLogin userLogin);

    @GET("/v1/user/open/authcode")
    Call<RespDto<String>> onGetSMSCode(@Query("phone") String phone,
                                       @Query("appkey") String appkey,
                                       @Query("sign") String sig);

    @GET("/v1/user/open/checkcode")
    Call<RespDto<String>> onCheckSMSCode(@Query("phone") String phone,
                                         @Query("vcode") String vcode,
                                         @Query("appkey") String appkey,
                                         @Query("sign") String sig);

    @POST("/v1/user/open/register")
    Call<RespDto<UserLoginInfo>> onLoadRegister(@Body UserLogin userLogin);
    @POST("/v1/feedback/add")
    Call<RespDto<Boolean>> onFeedback(@Body UserFeedback feedback);
    @POST("/v1/user/0/changepw")
    Call<RespDto<String>> onModifyPassword(@Body UserPassword password);

    @POST("/bgd/user/open/changepw")
    Call<RespDto<String>> onForgetModifyPassword(@Body UserPassword password);

    @POST("/v1/aliyunimg/auth")
    Call<RespDto<AliYunAuth>> onAliYunAuth(@Body AliYunAuthParm parm);

    @POST("/v1/user/update")
    Call<RespDto<String>> onUserModify(@Body UserModify parm);

    @GET("/v1/user/userinfo/{uid}")
    Call<RespDto<UserInfo>> onLoadUserInfo(@Path("uid") String uid);

    @GET("/v2/mpackage/open/recentlist/{chooseCity}")
    Call<RespDto<PackageList>> onLoadSuperMainPackages(@Path("chooseCity") String chooseCity, @Query("uid") String uid, @Query("pn") String pn, @Query("ps") String ps);

    @GET("/v2/mpackage/open/detail/{pid}")
    Call<RespDto<PackageDetailsInfo>> onLoadPackageDetails(@Path("pid") String pid, @Query("uid") String uid);

    @POST("/v2/mpackage/order")
    Call<RespDto<PackageOrderResult>> onPackageOrder(@Body PackageOrder parm);

    @POST("/v2/mpackage/order/refund")
    Call<RespDto<Long>> onPkgOrderRefund(@Body PkgRefundReq parm);

    @POST("/v2/pay/weix/{payTag}")
    Call<RespDto<AppPayResult>> onWeixinPay(@Path("payTag") String payTag, @Body PayParam parm);

    @POST("/v2/pay/ali/{payTag}")
    Call<RespDto<AppPayResult>> onAliPay(@Path("payTag") String payTag, @Body PayParam parm);

    @POST("/v2/pay/ali/sync")
    Call<RespDto<String>> onNotifyServerAliPay(@Query("orderId") String orderId);

    @PUT("/v2/pay/wxpayback/{orderId}")
    Call<RespDto<String>> onNotifyServerWeixinPay(@Path("orderId") String orderId);

    @GET("/v2/mpackage/order/left/{orderid}")
    Call<RespDto<OrderStatus>> onLoaderOrderStatus(@Path("orderid") String orderid);

    @PUT("/v2/mpackage/order/expire/{orderid}")
    Call<RespDto<String>> onOrderExpire(@Path("orderid") String orderid);

    @GET("/v1/dict/open/common/list/refund_reason")
    Call<RespDto<List<DictionaryName>>> onLoadRefundReason();

    @POST("/v1/collection/addcancel")
    Call<RespDto<String>> onCollectData(@Body CollectParam collectParam);

    @GET("/v2/mpackage/open/list/{mid}")
    Call<RespDto<MerchantPackage>> onLoadMerchantPackage(@Path("mid") String mid, @Query("pn") String pn, @Query("ps") String ps, @Query("uid") String uid);

    @GET("/v1/promotion/condition")
    Call<RespDto<RateCardInfo>> onLoadPromotionCondition(@Query("mid") String mid, @Query("uid") String uid);

    @GET("/v2/mpackage/open/recentlist/{city}")
    Call<RespDto<SalesInfo>> onLoadSales(@Path("city") String city,
                                         @Query("uid") Long uid,
                                         @Query("pn") Integer pn,
                                         @Query("ps") Integer ps);

    @POST("/v2/mbill/paybill")
    Call<RespDto<PayBillOrder>> onPayBillOrder(@Body PayBillOrderParam orderParam);

    @GET("/v2/coupon/usersupportcoupons")
    Call<RespDto<ArrayList<UserCoupon>>> onLoadUserSupportCoupons(
            @Query("uid") String uid,@Query("mid") String mid);

    @POST("/v1/pay/zeropay")
    Call<RespDto<Boolean>> onZeroPay(@Body ZeroPayParam collectParam);

    @GET("/v2/user/mileage/getMileage{uidd}")
    Call<RespDto<UserMileage>> onMileage(@Path("uidd") String uidd,
                                         @Query("uid") String uid);

//    NSString *urlString = [NSString stringWithFormat:@"/v2/mpackage/open/detail/%ld",(long)_pid];
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
