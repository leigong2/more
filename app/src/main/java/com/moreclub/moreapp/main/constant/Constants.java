package com.moreclub.moreapp.main.constant;


/**
 * Created by Administrator on 2017/2/23.
 */

public class Constants implements com.moreclub.moreapp.app.Constants {
    //public static final String BASE_URL = com.moreclub.moreapp.app.Constants.BASE_URL;

    public final static int MERCHANT_DETAILS_WINE = 1;
    public final static int MERCHANT_DETAILS_MUSIC = MERCHANT_DETAILS_WINE + 1;
    public final static int MERCHANT_DETAILS_SCENE = MERCHANT_DETAILS_MUSIC + 1;
    public final static int MERCHANT_DETAILS_FACILITIES = MERCHANT_DETAILS_SCENE + 1;
    public final static int SIGNINTERACTIVITY = 1001;
    public final static int MESSAGECENTERACTIVITY = 1002;
    public final static int SIGNINTERTOTALACTIVITY = 1003;
    public final static int MYSIGNINTERLISTACTIVITY = 1004;
    public final static int SUPERMAINACTIVITY = 1005;

    public final static String KEY_CITY_ID = "city.id";
    public final static String KEY_CITY_CODE = "city.code";
    public final static String KEY_CITY_NAME = "city.name";
    public final static String KEY_SPLASH_TITLE = "splash.title";
    public final static String KEY_SPLASH_IMAGE = "splash.image";
    public final static String KEY_SPLASH_TYPE = "splash.type";
    public final static String KEY_SPLASH_LINK = "splash.link";
    public final static String KEY_SPLASH_EXPIRE = "splash.expire";
    public final static String KEY_SPLASH_LOADED = "splash.loaded";
    public final static String KEY_CANCEL_REDPOINT = "cancel.redpoint";

    public static final String HEADLINE = ".headline";
    public static final String NEW_STORE = ".newstore";

    public static final String LEFT = ".left";
    public static final String RIGHT = ".right";
    public static final String ROOT = ".root";
    public static final String TITLE_INTEREST = "对方已收到您的拼座消息\n打个招呼更容易打动对方哟";
    public static final String TITLE_REPLY = "已发送拼座邀请给对方\n打个招呼更容易打动对方哟";
    public static final String TITLE_ANSWER_REPLY_SELF = "已通过邀请，拼座成功";
    public static final String TITLE_ANSWER_REPLY = "对方已经通过了您的拼座邀请";
    public static final String TITLE_ANSWER_INTEREST_SELF = "已接受邀请，拼座成功";
    public static final String TITLE_ANSWER_INTEREST = "对方已经接受了您的拼座邀请";
    public static final String TITLE_SEND_INTEREST = "您好，我对您的拼座感兴趣哟~";
    public static final String TITLE_SEND_REPLY = "您好，我想邀请您参与我的拼座";
    public static final String TITLE_ALLOW = "我感兴趣";
    public static final String TITLE_ANSWER = "我邀请TA";
    public static final String HAS_READ_INTERACTION = "has.read.interaction";
    public static final String ALLOW = "allow";
    public static final String ANSWER = "answer";
    public static final int NEARBY_MERCHANT = 1003;

    public final static String KEY_LOCATION_CITY_ID = "location.city.id";
    public final static String KEY_LOCATION_CITY_CODE = "location.city.code";
    public final static String KEY_LOCATION_CITY_NAME = "location.city.name";

    public final static String IS_UPDATE_MORE = "is_update_more";
    public final static String PUSH_RED = "push_red";
    public final static String IM_RED = "im_red";
    public static String downloadUrl;
    public static String updateDesc;
}
