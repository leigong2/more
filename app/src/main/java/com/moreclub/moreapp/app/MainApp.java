package com.moreclub.moreapp.app;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.PopupWindow;

import com.baidu.mapapi.SDKInitializer;
import com.liulishuo.filedownloader.FileDownloader;
import com.moreclub.common.MainApplication;
import com.moreclub.common.api.RestApi;
import com.moreclub.moreapp.chat.constant.DemoHelper;
import com.moreclub.moreapp.push.PushWorkServer;
import com.moreclub.moreapp.util.LocationService;
import com.moreclub.moreapp.util.MoreUser;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.moreclub.moreapp.ucenter.constant.Constants.WB_APPKEY;

/**
 */

public class MainApp extends MainApplication {
    public static String APP_ID = "wxac068b915ea16f30";
    public static IWXAPI api;
    public static AuthInfo mAuthInfo;
    public static IWeiboShareAPI mWeiboShareAPI;
    public static SsoHandler mSsoHandler;
    public static final String TAG = "com.xiaomi.mipushdemo";

    public static final String PUSH_APP_ID = "2882303761517566170";
    public static final String PUSH_APP_KEY = "5121756688170";
    public static final String PUSH_TAG = "com.moreclub.moreapp";
    private static MainApp instance;
    public static LocationService locationService;
    public String mVersion;

    public static HashMap<String,String> cityMap = new HashMap<>();
    public List<PopupWindow> homeStartPops;
    public List<PopupWindow> channelPops;
    public List<PopupWindow> spacePops;
    public List<PopupWindow> messagePops;
    public List<PopupWindow> userCenterPops;
    public List<List<PopupWindow>> totalPops;

    @Override
    public void onCreate() {
        RestApi.getInstance().bug(Constants.Config.DEVELOPER_MODE);
        super.onCreate();
        instance = this;
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
        configure();
        MoreUser.getInstance().initAccount(this);


        mAuthInfo = new AuthInfo(this,
                WB_APPKEY,
                "https://api.weibo.com/oauth2/default.html", "");

        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, WB_APPKEY);
        mWeiboShareAPI.registerApp();

        if (shouldInit()) {
            MiPushClient.registerPush(this, PUSH_APP_ID, PUSH_APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);


        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,
                "58d0fade8f4a9d7a8a0004c6", "More", MobclickAgent.EScenarioType.E_UM_NORMAL, true));
        MobclickAgent.setDebugMode(false);

        mVersion = getVersion();

        //环信初始化
        try {
            DemoHelper.getInstance().init(instance);
        } catch (Exception ex) {

        }
        /**
         * 初始化下载,仅仅是缓存Application的Context，不耗时
         */
        FileDownloader.setup(getApplicationContext());

        initCityMap();

        //我们现在需要服务运行, 将标志位重置为 false
        startService(new Intent(this, PushWorkServer.class));
        initPopWindowGroup();
    }

    private void initPopWindowGroup() {
        homeStartPops = new ArrayList<>();
        channelPops = new ArrayList<>();
        spacePops = new ArrayList<>();
        messagePops = new ArrayList<>();
        userCenterPops = new ArrayList<>();
        totalPops = new ArrayList<>();
        totalPops.add(homeStartPops);
        totalPops.add(channelPops);
        totalPops.add(spacePops);
        totalPops.add(messagePops);
        totalPops.add(userCenterPops);
    }

    private void initCityMap(){
        cityMap.put("cd","成都");
        cityMap.put("bj","北京");
        cityMap.put("sh","上海");
        cityMap.put("sz","深圳");
        cityMap.put("gz","广州");
        cityMap.put("km","昆明");
    }

    private void configure() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    public static MainApp getInstance() {
        if (instance==null){
            instance = (MainApp) MainApplication.getInstance();
        }
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String temp = info.versionName;
            String substring = temp.substring(0, 5);
            return "v" + substring;
        } catch (Exception e) {
            e.printStackTrace();
            return "v2.0.0";
        }
    }
}
