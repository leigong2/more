package com.moreclub.moreapp.main.ui.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.ui.view.scrollview.ObservableScrollViewCallbacks;
import com.moreclub.common.ui.view.scrollview.ScrollState;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.constant.DemoHelper;
import com.moreclub.moreapp.information.ui.activity.ActivitiesActivity;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.model.HotMerchant;
import com.moreclub.moreapp.main.model.RedPointerEvent;
import com.moreclub.moreapp.main.model.UpLoadCityInfo;
import com.moreclub.moreapp.main.model.UpdateBody;
import com.moreclub.moreapp.main.model.UpdateResp;
import com.moreclub.moreapp.main.model.UserSignParam;
import com.moreclub.moreapp.main.model.UserSignResult;
import com.moreclub.moreapp.main.model.UserSignSetting;
import com.moreclub.moreapp.main.presenter.HomeDataLoader;
import com.moreclub.moreapp.main.presenter.IHomeDataLoader;
import com.moreclub.moreapp.main.ui.service.UpdateService;
import com.moreclub.moreapp.main.ui.view.AutoSplitTextView;
import com.moreclub.moreapp.main.ui.view.LinesView;
import com.moreclub.moreapp.main.ui.view.LoadView;
import com.moreclub.moreapp.main.ui.view.ParticleView;
import com.moreclub.moreapp.main.ui.view.WaveLayout;
import com.moreclub.moreapp.main.ui.view.WaveView;
import com.moreclub.moreapp.packages.ui.activity.SalesActivity;
import com.moreclub.moreapp.ucenter.model.event.UserLoginEvent;
import com.moreclub.moreapp.ucenter.model.event.UserLogoutEvent;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserCouponListActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MapLocation;
import com.moreclub.moreapp.util.MoreUser;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION_CODES.M;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.moreclub.moreapp.app.MainApp.locationService;
import static com.moreclub.moreapp.information.constant.Constants.KEY_ACT_ID;
import static com.moreclub.moreapp.main.constant.Constants.IM_RED;
import static com.moreclub.moreapp.main.constant.Constants.IS_UPDATE_MORE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_CODE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_NAME;
import static com.moreclub.moreapp.main.constant.Constants.KEY_LOCATION_CITY_CODE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_LOCATION_CITY_ID;
import static com.moreclub.moreapp.main.constant.Constants.KEY_LOCATION_CITY_NAME;
import static com.moreclub.moreapp.main.constant.Constants.PUSH_RED;
import static com.moreclub.moreapp.main.constant.Constants.ROOT;
import static com.moreclub.moreapp.main.constant.Constants.updateDesc;
import static com.moreclub.moreapp.ucenter.constant.Constants.KEY_BIND_NUMBER;
import static com.moreclub.moreapp.ucenter.constant.Constants.KEY_PHONE_USER;

/**
 * Created by Administrator on 2017/5/25.
 */

public class HomeStarActivity extends BaseActivity implements IHomeDataLoader.HomeDataBinder {
    @BindView(R.id.home_star_scroll)
    ObservableScrollView scrollView;
    @BindView(R.id.fl_root)
    FrameLayout fl_root;
    @BindView(R.id.home_start_parent)
    LinearLayout linearLayout;
    @BindView(R.id.home_star_city_name)
    TextView homeStarCityName;
    @BindView(R.id.home_star_divider)
    TextView homeStarDivider;
    @BindView(R.id.home_btn_search)
    ImageView homeBtnSearch;
    @BindView(R.id.bar_bg)
    ImageView barBg;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rl_refresh)
    RelativeLayout llRefresh;
    @BindView(R.id.main_btn_msg)
    ImageView mainBtnMsg;
    @BindView(R.id.message_red_pointer)
    TextView messageRedPointer;
    @BindView(R.id.main_user_avatar)
    CircleImageView mainUserAvatar;
    @BindView(R.id.main_toolbar)
    RelativeLayout mainToolbar;
    @BindView(R.id.home_star_entry_btn)
    Button homeStarEntryBtn;
    @BindView(R.id.home_activities)
    ImageView homeActivities;
    @BindView(R.id.home_sales)
    ImageView homeSales;
    @BindView(R.id.iv_btn_bg)
    ImageView ivBtnBg;
    @BindView(R.id.iv_errer)
    ImageView ivErrer;
    @BindView(R.id.load_bar1)
    LoadView load_bar1;
    @BindView(R.id.load_bar2)
    LoadView load_bar2;
    @BindView(R.id.load_bar3)
    LoadView load_bar3;
    @BindView(R.id.load_bar4)
    LoadView load_bar4;
    @BindView(R.id.wave_layout)
    WaveLayout wave_layout;
    @BindView(R.id.particle)
    ParticleView particle;

    private PopupWindow mNewCouponWindow;
    private View mNewCouponView;

    @BindDimen(R.dimen.home_item_tpadding)
    int itemPaddingTop;

    @BindDimen(R.dimen.home_item_tmargin)
    int itemMarginTop;

    @BindDimen(R.dimen.home_first_item_tmargin)
    int firstItemMarginTop;

    @BindDimen(R.dimen.home_merchant_circle_width)
    int merchantCircleWidth;

    @BindDimen(R.dimen.home_merchant_circle_width_init)
    int merchantCircleWidthInit;

    @BindColor(R.color.orange)
    int merchantCircleColor;
    private static final int UPDATE_SELECTOR = 1002;
    private static final int UPDATE_RED_POINT = 1003;
    private static final int REQUEST_CODE_LOCATION = 321;
    private static final int INSTALL_CODE = 1005;


    private LayoutInflater inflater;
    private int width;
    private int height;
    private Float[] circleXPotionRate1 = {0.0f};
    private Float[] circleXPotionRate2 = {0.1f, 0.1f};
    private Float[] circleXPotionRate3 = {-0.1f, 0.125f, -0.1f};
    private Float[] circleXPotionRate4 = {-0.1f, 0.105f, 0.125f, 0.0f};
    private Integer[] circleTopArr1 = new Integer[1];
    private Integer[] circleTopArr2 = new Integer[2];
    private Integer[] circleTopArr3 = new Integer[3];
    private Integer[] circleTopArr4 = new Integer[4];
    private boolean isFirstAdd = true;
    private int[] merchantViewWidthAndHeight;
    private List<float[]> dispatchNearLoacations;
    private List<float[]> rootLeftLocations;
    private List<float[]> rootRightLocations;
    private int nowY = -1;
    private PopupWindow mSignWindow;
    private View popupSignView;
    private SystemBarTintManager tintManager;
    private String cityCode;
    private String cityName;
    private boolean isShowed = true;
    private boolean hasLoad;
    private int GPScount;
    private int merchantNameHeight;
    private float moveY;
    private float downY;
    private float firstDownY;
    private boolean down = true;
    //获取status_bar_height资源的ID
    int resourceId;
    int statusBarHeight = -1;
    private int highApi;
    private int lowApi;
    private boolean isUpdateView;
    private boolean hasLoadView;
    private boolean hasBack;
    private boolean isShowLocationPop;
    private MapLocation.MapCallback mapCallback;
    private View mLocationPopView;
    private PopupWindow mLocationWindow;
    private String cityLocation;
    private Boolean differet = false;
    private int GPSResCount;
    private boolean canScroll;
    private List<Integer> hots;
    private List<HotMerchant.RespListBean> mMerchant;
    private List<String> areaNames;
    private List<float[]> areaLocations;
    private Timer timer;
    private View mUpdatePopView;
    private ImageView close;
    private TextView cancel;
    private TextView makeSure;
    private PopupWindow mUpdatePopWindow;
    private TextView tvTitle;
    private int versioncode;
    private View gray_layout;
    private RelativeLayout bottom_layout;
    private RelativeLayout top_layout;
    private int totalCount;

    private EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            List<Object> blockList = PrefsUtils.getDataList(HomeStarActivity.this, "chat_block");
            for (EMMessage message : messages) {
                if (blockList != null) {
                    for (int i = 0; i < blockList.size(); i++) {
                        if (blockList.get(i).equals(message.getFrom())) {
                            return;
                        }
                    }
                }
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }

            Message msg = handler.obtainMessage();
            msg.what = UPDATE_RED_POINT; //消息标识
            handler.sendMessage(msg); //发送消息

            Intent intent = new Intent();
            intent.setAction("Message_Center_IM_Message");
            intent.putExtra("type", "hx");
            sendBroadcast(intent);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SELECTOR:
                    homeActivities.setSelected(false);
                    homeSales.setSelected(false);
                    removeCallbacksAndMessages(null);
                    break;
                case UPDATE_RED_POINT:
                    messageRedPointer.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    // 要申请的权限
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ObservableScrollViewCallbacks callBack = new ObservableScrollViewCallbacks() {

        @Override
        public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            //Todo 滚动事件监听
            if (scrollY > nowY && scrollY > 0) {
                hideBtn();
            } else if (scrollY < nowY) {
                showBtn();
            }
            if (scrollY <= 0) {
                nowY = -1;
            } else
                nowY = scrollY;
        }

        @Override
        public void onDownMotionEvent() {

        }

        @Override
        public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            fl_root.setTranslationY(0);
            if (!down && moveY - firstDownY > 200 && wave_layout.getVisibility() == View.GONE) {
                if (timer != null)
                    timer.cancel();
                canScroll = false;
                moveY = 0;
                llRefresh.setTranslationY(0);
                llRefresh.setAlpha(0);
                tvRefresh.setText("正在刷新");
                down = true;
                activityComeBack();
            } else {
                llRefresh.setTranslationY(0);
                llRefresh.setAlpha(0);
                tvRefresh.setText("下拉刷新");
                down = true;
            }
            downY = 0;
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (nowY <= 0 && downY < height / 4 * 3
                    && moveY - firstDownY < height / 3
                    && canScroll && wave_layout.getVisibility() == View.GONE) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveY = event.getRawY();
                        if (down && moveY - downY > 0) {
                            firstDownY = moveY;
                            downY = moveY;
                            down = false;
                        }
                        fl_root.setTranslationY(moveY - downY);
                        llRefresh.setTranslationY((moveY - downY) / 2);
                        llRefresh.setAlpha((moveY - downY) / width * 2);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            } else if (wave_layout.getVisibility() == View.VISIBLE || !canScroll) {
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.home_star_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IHomeDataLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        isShowLocationPop = false;
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#00000000"));
        inflater = LayoutInflater.from(this);
        scrollView.setOnTouchListener(touchListener);
        scrollView.addScrollViewCallbacks(callBack);
        width = ScreenUtil.getScreenWidth(this);
        height = ScreenUtil.getScreenHeight(this);
        if (PrefsUtils.getInt(this, PUSH_RED, 0) + PrefsUtils.getInt(this, IM_RED, 0) > 0)
            messageRedPointer.setVisibility(View.VISIBLE);
        else
            messageRedPointer.setVisibility(View.GONE);
        hots = new ArrayList<>();
        areaNames = new ArrayList<>();
        areaLocations = new ArrayList<>();
        circleTopArr4[0] = (int) (3 * ((0.85 * width) / 100));
        circleTopArr4[1] = (int) (18 * ((0.85 * width) / 100));
        circleTopArr4[2] = (int) (48 * ((0.85 * width) / 100));
        circleTopArr4[3] = (int) (78 * ((0.85 * width) / 100));
        circleTopArr3[0] = (int) (3 * ((0.85 * width) / 100));
        circleTopArr3[1] = (int) (38 * ((0.85 * width) / 100));
        circleTopArr3[2] = (int) (78 * ((0.85 * width) / 100));
        circleTopArr2[0] = (int) (8 * ((0.85 * width) / 100));
        circleTopArr2[1] = (int) (73 * ((0.85 * width) / 100));
        circleTopArr1[0] = (int) (33 * ((0.85 * width) / 100));
        firstItemMarginTop = (int) (width * 2.0f / 3.0f);
        if (TextUtils.isEmpty(PrefsUtils.getString(this, KEY_BIND_NUMBER, ""))
                && TextUtils.isEmpty(PrefsUtils.getString(this, KEY_PHONE_USER, ""))) {
            MoreUser.getInstance().clearAccount(this);
            EventBus.getDefault().post(new UserLogoutEvent());
        }
        try {
            UpdateBody body = new UpdateBody();
            body.setPlatform(0);
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versioncode = pi.versionCode;
            body.setVersionCode(versioncode);
            ((HomeDataLoader) mPresenter).onUpdateMore(body);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        dispatchGPS();
//        loadDatas();
        activityComeBack();
        homeActivities.setSelected(false);
        homeSales.setSelected(false);
    }

    /**
     * zune:处理定位
     **/
    private void dispatchGPS() {
        GPScount = 0;
        GPSResCount = 0;
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GPScount++;
                GPSResCount++;
                if (GPScount == 1 && GPSResCount == 1) {
                    cityLocation = "";
                    Long uid;
                    if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                        uid = MoreUser.getInstance().getUid();
                    } else {
                        uid = null;
                    }
                    ((HomeDataLoader) mPresenter).onLoadHotMerchant(uid, cityCode, 0 + "," + 0, true);
                    hasLoad = true;
                }
            }
        };
        timer.schedule(task, 2000);
        MoreUser.getInstance().setShowCouponDialog(false);
        Log.i("zune:", "处理定位");
        mapCallback = new MapLocation.MapCallback() {
            @Override
            public void dbMapCallback(double lat, double lng, String city, String province, String country) {
                GPSResCount++;
                if (GPSResCount == 1) {
                    if (MoreUser.getInstance().getCity() == null) {
                        cityLocation = "成都";
                    } else {
                        cityLocation = MoreUser.getInstance().getCity().split("市")[0];
                        if (TextUtils.isEmpty(PrefsUtils.getString(HomeStarActivity.this, KEY_LOCATION_CITY_NAME, ""))) {
                            PrefsUtils.setString(HomeStarActivity.this, KEY_LOCATION_CITY_NAME, cityLocation);
                        }
                        if (TextUtils.equals(cityLocation, cityName)) {
                            differet = false;
                        } else {
                            differet = true;
                        }
                        if (!TextUtils.equals(cityLocation, cityName)
                                && !TextUtils.equals(cityLocation, PrefsUtils.getString(HomeStarActivity.this, KEY_LOCATION_CITY_NAME, ""))) {
                            isShowLocationPop = true;
                        } else {
                            isShowLocationPop = false;
                        }
                        if (!TextUtils.equals(cityLocation, "成都")
                                && !TextUtils.equals(cityLocation, "北京")
                                && !TextUtils.equals(cityLocation, "上海")
                                && !TextUtils.equals(cityLocation, "昆明"))
                            isShowLocationPop = false;
                    }
                    UpLoadCityInfo cityInfo = new UpLoadCityInfo();
                    cityInfo.setUid(MoreUser.getInstance().getUid());
                    cityInfo.setCity(city);
                    if (!TextUtils.isEmpty(province))
                        cityInfo.setRegion(province);
                    if (!TextUtils.isEmpty(country))
                        cityInfo.setCountry(country);
                    Long uid;
                    if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                        uid = MoreUser.getInstance().getUid();
                        ((HomeDataLoader) mPresenter).onUpLoadCityInfo(cityInfo);
                    } else {
                        uid = null;
                    }
                    ((HomeDataLoader) mPresenter).onLoadHotMerchant(uid, cityCode, lng + "," + lat, differet);
                }
                Log.i("zune:", "GPSCountRes = " + GPScount);
            }

            @Override
            public void dbMapCallbackFails() {
                GPScount++;
                if (GPScount == 1) {
                    cityLocation = "";
                    Long uid;
                    if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
                        uid = MoreUser.getInstance().getUid();
                    } else {
                        uid = null;
                    }
                    ((HomeDataLoader) mPresenter).onLoadHotMerchant(uid, cityCode, 0 + "," + 0, true);
                }
                Log.i("zune:", "GPSCountFail = " + GPScount);
            }
        };
        MapLocation.getLocation(this.getApplicationContext(), mapCallback);
    }

    /**
     * zune:弹出异地城市切换
     */
    private void showLocationPop() {
        if (mLocationPopView == null) {
            mLocationPopView = inflater.inflate(
                    R.layout.star_city_change, null);
            final View grayLayout = mLocationPopView.findViewById(R.id.gray_layout);
            ImageView closeW = (ImageView) mLocationPopView.findViewById(R.id.closeW);
            ImageView centerIcon = (ImageView) mLocationPopView.findViewById(R.id.center_icon);
            centerIcon.setImageResource(R.drawable.location_changed);
            TextView tvTitle = (TextView) mLocationPopView.findViewById(R.id.tv_title);
            tvTitle.setText(R.string.home_star_location_change);
            TextView lookNewCoupon = (TextView) mLocationPopView.findViewById(R.id.lookNewCoupon);
            lookNewCoupon.setVisibility(View.GONE);
            TextView makeSure = (TextView) mLocationPopView.findViewById(R.id.btn_make_sure);
            makeSure.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            makeSure.setText("打开" + cityLocation);
            makeSure.setVisibility(View.VISIBLE);
            TextView cancel = (TextView) mLocationPopView.findViewById(R.id.cancel);
            cancel.setVisibility(View.VISIBLE);
            grayLayout.setVisibility(View.VISIBLE);
            grayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grayLayout.setVisibility(View.GONE);
                    closeLocationPop();
                    PrefsUtils.getEditor(HomeStarActivity.this)
                            .putString(KEY_LOCATION_CITY_NAME, cityLocation)
                            .commit();
                }
            });

            closeW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grayLayout.setVisibility(View.GONE);
                    closeLocationPop();
                    PrefsUtils.getEditor(HomeStarActivity.this)
                            .putString(KEY_LOCATION_CITY_NAME, cityLocation)
                            .commit();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grayLayout.setVisibility(View.GONE);
                    closeLocationPop();
                    PrefsUtils.getEditor(HomeStarActivity.this)
                            .putString(KEY_LOCATION_CITY_NAME, cityLocation)
                            .commit();
                }
            });
            makeSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GPSResCount = 0;
                    GPScount = 0;
                    grayLayout.setVisibility(View.GONE);
                    closeLocationPop();
                    ((HomeDataLoader) mPresenter).onLoadCity();
                }
            });
        } else {
            TextView makeSure = (TextView) mLocationPopView.findViewById(R.id.btn_make_sure);
            makeSure.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            makeSure.setText("打开" + cityLocation);
        }

        mLocationWindow = new PopupWindow(mLocationPopView, width, height, true);
        //设置动画弹出效果
        mLocationWindow.setAnimationStyle(R.style.coupon_popup_animation);
        // 设置半透明灰色
        mLocationWindow.setFocusable(false);
        mLocationWindow.setTouchable(true);
        int[] pos = new int[2];

        if (mLocationWindow != null)
            mLocationWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                    pos[0], 0);
    }

    private void closeLocationPop() {
        if (mLocationWindow != null && mLocationWindow.isShowing()) {
            mLocationWindow.dismiss();
            mLocationWindow = null;
        }
    }

    /**
     * zune:弹出优惠券
     **/
    private void showNewCoupon() {
        if (null == mNewCouponWindow) {
            mNewCouponView = LayoutInflater.from(this).inflate(
                    R.layout.star_new_coupon, null);

            final View grayLayout = mNewCouponView.findViewById(R.id.gray_layout);
            ImageView closeW = (ImageView) mNewCouponView.findViewById(R.id.closeW);
            TextView lookNewCoupon = (TextView) mNewCouponView.findViewById(R.id.lookNewCoupon);

            grayLayout.setVisibility(View.VISIBLE);
            grayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grayLayout.setVisibility(View.GONE);
                    closeNewCouponWindow();
                }
            });

            closeW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grayLayout.setVisibility(View.GONE);
                    closeNewCouponWindow();
                }
            });

            lookNewCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grayLayout.setVisibility(View.GONE);
                    closeNewCouponWindow();

                    AppUtils.pushLeftActivity(HomeStarActivity.this, UserCouponListActivity.class);
                }
            });

            mNewCouponWindow = new PopupWindow(mNewCouponView, width, height, true);
            //设置动画弹出效果
            mNewCouponWindow.setAnimationStyle(R.style.coupon_popup_animation);
            // 设置半透明灰色
            mNewCouponWindow.setFocusable(false);
            mNewCouponWindow.setTouchable(true);
        }
        int[] pos = new int[2];

        mNewCouponWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], 0);

    }

    private void closeNewCouponWindow() {
        if (mNewCouponWindow != null && mNewCouponWindow.isShowing()) {
            mNewCouponWindow.dismiss();
            mNewCouponWindow = null;
        }
    }

    /**
     * zune:弹出更新对话框
     *
     * @param updateDesc
     */
    private void showUpdateWindow(String updateDesc) {
        if (mUpdatePopView == null) {
            mUpdatePopView = inflater.inflate(
                    R.layout.update_app, null);
            close = (ImageView) mUpdatePopView.findViewById(R.id.closeW);
            cancel = (TextView) mUpdatePopView.findViewById(R.id.cancel);
            makeSure = (TextView) mUpdatePopView.findViewById(R.id.btn_make_sure);
            tvTitle = (TextView) mUpdatePopView.findViewById(R.id.tv_title);
            gray_layout = mUpdatePopView.findViewById(R.id.gray_layout);
            bottom_layout = (RelativeLayout) mUpdatePopView.findViewById(R.id.bottom_layout);
            top_layout = (RelativeLayout) mUpdatePopView.findViewById(R.id.top_layout);
            View.OnClickListener noCloseListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
            if (!TextUtils.isEmpty(updateDesc))
                tvTitle.setText(updateDesc);
            makeSure.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            View.OnClickListener closeClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = HomeStarActivity.this;
                    String str = "btn_refuse_update";
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", MoreUser.getInstance().getUid() + "");
                    if (Build.VERSION.SDK_INT > 22) {
                        int du = 2000000000;
                        MobclickAgent.onEventValue(context, str, map, du);
                    } else {
                        MobclickAgent.onEvent(context, str, map);
                    }
                    PrefsUtils.setBoolean(HomeStarActivity.this, IS_UPDATE_MORE, false);
                    closeUpdateWindow();
                }
            };
            View.OnClickListener closeListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeUpdateWindow();
                }
            };
            top_layout.setOnClickListener(noCloseListener);
            bottom_layout.setOnClickListener(noCloseListener);
            close.setOnClickListener(closeListener);
            gray_layout.setOnClickListener(closeListener);
            cancel.setOnClickListener(closeClickListener);
            View.OnClickListener sureClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = HomeStarActivity.this;
                    String str = "btn_accept_update";
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", MoreUser.getInstance().getUid() + "");
                    if (Build.VERSION.SDK_INT > 22) {
                        int du = 2000000000;
                        MobclickAgent.onEventValue(context, str, map, du);
                    } else {
                        MobclickAgent.onEvent(context, str, map);
                    }
                    PrefsUtils.setBoolean(HomeStarActivity.this, IS_UPDATE_MORE, true);
                    downLoadApp();
                    closeUpdateWindow();
                }
            };
            makeSure.setOnClickListener(sureClickListener);
        } else {

        }
        mUpdatePopWindow = new PopupWindow(mUpdatePopView, ScreenUtil.getScreenWidth(this)
                , ScreenUtil.getScreenHeight(this), true);
        //设置动画弹出效果
        mUpdatePopWindow.setAnimationStyle(R.style.coupon_popup_animation);
        mUpdatePopWindow.setFocusable(false);
        int[] pos = new int[2];

        if (mUpdatePopWindow != null)
            mUpdatePopWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                    pos[0], 0);
    }

    private void closeUpdateWindow() {
        if (mUpdatePopWindow != null && mUpdatePopWindow.isShowing()) {
            mUpdatePopWindow.dismiss();
            mUpdatePopWindow = null;
        }
    }

    /**
     * zune:弹出自动签到
     **/
    private void setupSignPopuWindow(final String autoSignMid, final String merchantName) {

        if (null == mSignWindow) {
            popupSignView = LayoutInflater.from(this).inflate(
                    R.layout.main_sign_popu_window, null);
            LinearLayout sign_error_layout = (LinearLayout) popupSignView.findViewById(R.id.sign_error_layout);
            TextView merchantNameTV = (TextView) popupSignView.findViewById(R.id.merchantNameTV);
            merchantNameTV.setText(merchantName);
            merchantNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 查看酒吧详情
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            HomeStarActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(HomeStarActivity.this, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", autoSignMid);
                    ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
                }
            });

            sign_error_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            HomeStarActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(HomeStarActivity.this, ModifyAutoSignAddress.class);
                    intent.putExtra("mid", autoSignMid);
                    intent.putExtra("merchantName", merchantName);
                    ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
                    mSignWindow.dismiss();
                    mSignWindow = null;
                }
            });
            int width = ScreenUtil.getScreenWidth(HomeStarActivity.this);
            int height = ScreenUtil.getScreenHeight(HomeStarActivity.this);

            //设置弹出部分和面积大小
            mSignWindow = new PopupWindow(popupSignView, width, height - 56, true);
            //设置动画弹出效果
            mSignWindow.setAnimationStyle(R.style.PopupAnimation);

//            mPopupWindow.setClippingEnabled(true);
            mSignWindow.setFocusable(true);
            mSignWindow.setTouchable(true);
            popupSignView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSignWindow.dismiss();
                    mSignWindow = null;
                }
            });
        }
        int[] pos = new int[2];
        mSignWindow.showAsDropDown(mainToolbar, 0, 0);

    }

    /**
     * zune:4.4.4以上版本状态栏沉浸
     **/
    @TargetApi(19)
    private void setTranslucentStatus() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = -1;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            mainToolbar.setTranslationY(statusBarHeight);
            barBg.setTranslationY(statusBarHeight);
        }
    }

    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("定位权限不可用")
                .setMessage("由于本应用有地图导航功能，请开启定位权限；\n否则，您将无法正常使用地图导航")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATION);
    }

    private void loadDatas() {
//        Log.i("zune:","token = "+MoreUser.getInstance().getAccess_token());
        resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        highApi = ScreenUtil.dp2px(HomeStarActivity.this, 10) + statusBarHeight;
        lowApi = ScreenUtil.dp2px(HomeStarActivity.this, 10);
        rootLeftLocations = new ArrayList<>();
        rootRightLocations = new ArrayList<>();
        cityCode = PrefsUtils.getString(this, KEY_CITY_CODE, "cd");
        cityName = PrefsUtils.getString(this, KEY_CITY_NAME, "成都");
        homeStarCityName.setText(cityName);
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
            ((HomeDataLoader) mPresenter).onModiflyMileage();
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
//                ((HomeDataLoader) mPresenter).onLoadRedPointer("" + MoreUser.getInstance().getUid(), "android");
            }
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0)
                DemoHelper.hxLogin("" + MoreUser.getInstance().getUid(), "m" + MoreUser.getInstance().getUid());
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        new Thread() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    // 加载所有会话到内存
                    EMClient.getInstance().chatManager().loadAllConversations();
                    // 加载所有群组到内存，如果使用了群组的话
                    EMClient.getInstance().groupManager().loadAllGroups();
                } catch (Exception e) {

                }
                // 登录成功跳转界面
            }
        }.start();
        DemoHelper.getInstance().getDataFromServer();
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
//                showDialogTipUserRequestPermission();
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
        final String avatarUrl = TextUtils.isEmpty(MoreUser.getInstance().getThumb()) ?
                MoreUser.getInstance().getThdThumb() : MoreUser.getInstance().getThumb();
        if (!TextUtils.isEmpty(avatarUrl))
            Glide.with(this)
                    .load(avatarUrl)
                    .dontAnimate()
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .placeholder(R.drawable.profilephoto_small)
                    .into(mainUserAvatar);
        else {
            mainUserAvatar.setImageResource(R.drawable.profilephoto_small);
        }
        load_bar1.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load_bar2.start();
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load_bar3.start();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                load_bar4.start();
            }
        }, 1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    //用户同意授权
                    activityComeBack();
                } else {
                    //用户拒绝授权
//                    dispatchGPS();
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= M) {
                    showDialogTipUserRequestPermission();
                }
                break;
        }
    }

    /**
     * zune:数据响应
     **/

    @Override
    public void onHotMerchantResponse(RespDto response) {
        ivErrer.setVisibility(View.GONE);
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
            ((HomeDataLoader) mPresenter).onLoadSignSetting();
        } else {
            if (isShowLocationPop) {
                showLocationPop();
            }
        }
        if (hots != null)
            hots.clear();
        if (areaNames != null)
            areaNames.clear();
        if (areaLocations != null)
            areaLocations.clear();
        wave_layout.setVisibility(View.GONE);
        fl_root.setVisibility(View.VISIBLE);
        llRefresh.setTranslationY(0);
        llRefresh.setAlpha(0);
        tvRefresh.setText("下拉刷新");
        HotMerchant hotMerchantRsp = (HotMerchant) response.getData();
        mMerchant = hotMerchantRsp.getRespList();
        for (int i = 0; i < mMerchant.size(); i++) {
            int hot = mMerchant.get(i).getHot();
            areaNames.add(mMerchant.get(i).getBusName());
            hots.add(hot);
        }
        if (mMerchant != null)
            addHotBusCircle(mMerchant);
        if (locationService.getClient() != null)
            locationService.getClient().stop();
    }

    @Override
    public void onHotMerchantFailure(String msg) {
        if (locationService.getClient() != null)
            locationService.getClient().stop();
        Log.i("zune:", "msgHot = " + msg);
        ivErrer.setVisibility(View.VISIBLE);
        wave_layout.setVisibility(View.GONE);
    }

    @Override
    public void onUpdateResponse(RespDto respDto) {
        if (respDto == null || !respDto.isSuccess() || respDto.getData() == null
                || ((UpdateResp) respDto.getData()).getVersionCode() <= versioncode) return;
        UpdateResp response = (UpdateResp) respDto.getData();
        Constants.downloadUrl = response.getDownloadUrl();
        updateDesc = response.getUpdateDesc();
        if (response.isForceUpdate()) {
            showUpdateWindow(updateDesc);
        } else if (response.isUpdate() && PrefsUtils.getBoolean(HomeStarActivity.this, IS_UPDATE_MORE, true)) {
            showUpdateWindow(updateDesc);
        }
    }

    private void deleteApk(File cacheDir) {
        if (cacheDir != null && cacheDir.exists() && cacheDir.isDirectory()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    deleteApk(files[i]);
                }
                cacheDir.delete();
            }
        }
    }

    @Override
    public void onUpdateFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onUpLoadCityInfoFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onLoadMorePlateFailure(String msg) {

    }

    @Override
    public void onLoadMorePlateResponse(RespDto response) {

    }

    @Override
    public void onUpLoadCityInfoResponse(RespDto response) {
        String data = (String) response.getData();
        Log.i("zune:", "uploadCityInfo = " + data);
    }

    @Override
    public void onHomeResponse(RespDto response) {
    }

    @Override
    public void onHomeFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onNearbyMerchantsResponse(RespDto response) {
    }

    @Override
    public void onNearbyMerchantsFailure(String msg) {
    }

    @Override
    public void onModiflyMileageFailure(String msg) {

    }

    @Override
    public void onModiflyMileageResponse(RespDto response) {

    }

    /**
     * H5优惠劵
     */
    @Override
    public void onH5CouponResponse(RespDto response) {

        Boolean result = (Boolean) response.getData();
        if (result) {
            MoreUser.getInstance().setShowCouponDialog(false);
            showNewCoupon();
        }
    }

    @Override
    public void onH5CouponFailure(String msg) {
        MoreUser.getInstance().setShowCouponDialog(false);
    }

    /**
     * zune:城市切换
     **/
    @Override
    public void onCityResponse(List response) {
        if (response != null && !response.isEmpty()) {
            differet = false;
            for (int i = 0; i < response.size(); i++) {
                if (((City) response.get(i)).getCityName().equals(cityLocation)) {
                    PrefsUtils.getEditor(HomeStarActivity.this)
                            .putInt(KEY_LOCATION_CITY_ID, ((City) response.get(i)).getCid())
                            .putString(KEY_LOCATION_CITY_CODE, ((City) response.get(i)).getCityCode())
                            .putString(KEY_LOCATION_CITY_NAME, ((City) response.get(i)).getCityName())
                            .commit();
                    PrefsUtils.setString(this, KEY_CITY_CODE, ((City) response.get(i)).getCityCode());
                    PrefsUtils.setString(this, KEY_CITY_NAME, ((City) response.get(i)).getCityName());
                    activityComeBack();
                    break;
                }
            }
        }
    }

    @Override
    public void onCityFailure(String msg) {
        Logger.i("zune:", "msgCity = " + msg);
    }

//    @Override
//    public void onRedPointerResponse(RespDto response) {
//        MessageCenterPush result = (MessageCenterPush) response.getData();
//        int upPushCount = 0;
//        upPushCount += result.getSystemCount();
//        upPushCount += result.getFollowCount();
//        upPushCount += result.getWallCount();
//        if (upPushCount > 0 || PrefsUtils.getInt(this, PUSH_RED, 0) + PrefsUtils.getInt(this, IM_RED, 0) > 0) {
//            messageRedPointer.setVisibility(View.VISIBLE);
//        } else {
//            messageRedPointer.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onRedPointerFailure(String msg) {
//        Log.i("zune:", "msg = " + msg);
//    }

    @Override
    public void onSignSettingResponse(RespDto response) {
        UserSignSetting result = (UserSignSetting) response.getData();
        if (result.isAutoSignin()) {
            UserSignParam param = new UserSignParam();
            param.setUid("" + MoreUser.getInstance().getUid());
            param.setCity(cityCode);
            param.setUserName(MoreUser.getInstance().getNickname());
            param.setUserLat("" + MoreUser.getInstance().getUserLocationLat());
            param.setUserLng("" + MoreUser.getInstance().getUserLocationLng());
            ((HomeDataLoader) mPresenter).onSignAuto(param);
        } else {
            if (isShowLocationPop) {
                showLocationPop();
            } else {
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                    ((HomeDataLoader) mPresenter).onH5Coupon();
                }
            }
        }
    }

    @Override
    public void onSignSettingFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
        if (isShowLocationPop) {
            showLocationPop();
        } else {
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                ((HomeDataLoader) mPresenter).onH5Coupon();
            }
        }
    }

    @Override
    public void onSignAutoResponse(RespDto response) {
//
        UserSignResult result = (UserSignResult) response.getData();
        if (result != null && result.getMerchantName() != null && !MoreUser.getInstance().isAutoSignSuccess()) {
            MoreUser.getInstance().setAutoSignSuccess(true);
            PrefsUtils.setBoolean(this, com.moreclub.moreapp.ucenter.constant.Constants.KEY_AUTO_SIGN_POP, true);
            setupSignPopuWindow(result.getMid(), result.getMerchantName());
        } else {
            if (isShowLocationPop) {
                showLocationPop();
            } else {
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                    ((HomeDataLoader) mPresenter).onH5Coupon();
                }
            }
        }
    }

    @Override
    public void onSignAutoFailure(String msg) {
        if (isShowLocationPop) {
            showLocationPop();
        } else {
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                ((HomeDataLoader) mPresenter).onH5Coupon();
            }
        }

        Log.i("zune:", "msg = " + msg);
    }

    /**
     * zune:获取热门酒吧
     **/
    private void addHotBusCircle(List<HotMerchant.RespListBean> respList) {
        if (respList != null) {
            int size = respList.size();
            for (int position = 0; position < size; position++) {
                HotMerchant.RespListBean respListBean = respList.get(position);
                int merchantNum = respListBean.getMerchantNum();
                totalCount += merchantNum;
                if (position % 2 == 0) {
                    View view = initLeftHotView(position, respListBean, size);
                    initLeftHotData(respList, position, view, size);
                } else {
                    View view = initRightHotView(position, respListBean, size);
                    initRightHotData(respList, position, view, size);
                }
            }
        }
    }

    /**
     * zune: 处理大圈的位置信息
     **/
    private View initLeftHotView(int position, HotMerchant.RespListBean respListBean, int size) {
        if (linearLayout == null) return new View(this);
        View view;
        if (linearLayout.getChildCount() != size) {
            view = inflater.inflate(R.layout.home_area_left_item, null);
            setLeftHotViewParams(position, respListBean, view);
            linearLayout.addView(view);
        } else {
            view = linearLayout.getChildAt(position);
            setLeftHotViewParams(position, respListBean, view);
        }
        return view;
    }

    private void setLeftHotViewParams(int position, HotMerchant.RespListBean respListBean, View view) {
        int hot = respListBean.getHot();
        view.setScaleY((float) (0.6 + 0.1 * hot));
        view.setScaleX((float) (0.6 + 0.1 * hot));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (width * 0.85), (int) (width * 0.85));
        params.setMargins(-(int) (width * (hot - 1) / 20.0f), (int) (10 * ((0.85 * width) / 100)), 0, 0);
        RelativeLayout leftParent = ButterKnife.findById(view, R.id.home_star_left_item_parent);
        LinesView lvLines = new LinesView(HomeStarActivity.this);
        lvLines.setLinesCount(respListBean.getMerchants().size());
        lvLines.setTag(Constants.LEFT);
        RelativeLayout.LayoutParams linesParams = new RelativeLayout.LayoutParams(width, width);
        lvLines.setLayoutParams(linesParams);
        leftParent.addView(lvLines, 0);
        ImageView imageView = ButterKnife.findById(view, R.id.home_circle_bg);
        imageView.setLayoutParams(params);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(MATCH_PARENT,
                WRAP_CONTENT);
        if (position == 0)
            lparams.setMargins(-(int) ((0.08f * (4 - hot)) * width), (int) (firstItemMarginTop - (0.06f * (4 - hot)) * width), 0, 0);
        else if (hot > 1)
            lparams.setMargins(-(int) ((0.08f * (4 - hot)) * width), (int) (-itemMarginTop - (0.06f * (4 - hot)) * width), 0, 0);
        else if (hot == 1)
            lparams.setMargins(-(int) ((0.08f * (4 - hot)) * width), (int) (-itemMarginTop - (0.06f * (4 - hot)) * width), 0
                    , (int) (-(0.01f * (4 - hot)) * width));
        view.setLayoutParams(lparams);
        view.invalidate();
    }

    private View initRightHotView(int position, HotMerchant.RespListBean respListBean, int size) {
        if (linearLayout == null) return new View(this);
        View view;
        if (linearLayout.getChildCount() != size) {
            view = inflater.inflate(R.layout.home_area_right_item, null);
            setRightHotViewParams(respListBean, view);
            linearLayout.addView(view);
        } else {
            view = linearLayout.getChildAt(position);
            setRightHotViewParams(respListBean, view);
        }
        return view;
    }

    private void setRightHotViewParams(HotMerchant.RespListBean respListBean, View view) {
        int hot = respListBean.getHot();
        view.setScaleY((float) (0.6 + 0.1 * hot));
        view.setScaleX((float) (0.6 + 0.1 * hot));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (width * 0.85), (int) (width * 0.85));
        params.setMargins(0, (int) (10 * ((0.85 * width) / 100)), -(int) (width * (hot - 1) / 20.0f), 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout rightParent = ButterKnife.findById(view, R.id.home_star_right_item_parent);
        LinesView lvLines = new LinesView(HomeStarActivity.this);
        lvLines.setLinesCount(respListBean.getMerchants().size());
        lvLines.setTag(Constants.RIGHT);
        RelativeLayout.LayoutParams linesParams = new RelativeLayout.LayoutParams(width, width);
        lvLines.setLayoutParams(linesParams);
        rightParent.addView(lvLines, 0);
        ImageView imageView = ButterKnife.findById(view, R.id.home_circle_bg);
        imageView.setLayoutParams(params);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(MATCH_PARENT,
                WRAP_CONTENT);
        lParams.setMargins(0, (int) (-itemMarginTop - (0.06f * (4 - hot)) * width), -(int) ((0.08f * (4 - hot)) * width), 0);
        if (hot == 1)
            lParams.setMargins(0, (int) (-itemMarginTop - (0.06f * (4 - hot)) * width), -(int) ((0.08f * (4 - hot)) * width)
                    , (int) (-(0.01f * (4 - hot)) * width));
        view.setLayoutParams(lParams);
    }

    /**
     * zune:处理左侧所有数据
     **/
    private void initLeftHotData(final List<HotMerchant.RespListBean> respList, final int position, View view, final int size) {
        int merchantNum = respList.get(position).getMerchants().size();
        int drawSize = Math.min(4, merchantNum);
        if (respList.get(position).getHot() < 3 && drawSize > 2) {
            drawSize = 2;
        }
        final int[] merchantNameHeights = new int[drawSize];
        final View[] parties = new View[drawSize];
        final TextView tvAllCity = ButterKnife.findById(view, R.id.home_all_city_text);
        final RelativeLayout leftParent = getLeftHotParent(respList, position, view, tvAllCity);
        initLeftHotMerchantView(respList, position, drawSize, merchantNameHeights, parties, leftParent);
    }

    private RelativeLayout getLeftHotParent(final List<HotMerchant.RespListBean> busareaDtos, final int position, View view, TextView tvAllCity) {
        float outScale = (float) (1 / (0.6 + 0.1 * busareaDtos.get(position).getHot()));
        RelativeLayout.LayoutParams lpCity = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(HomeStarActivity.this, 40), ScreenUtil.dp2px(HomeStarActivity.this, 40));
        lpCity.setMargins((int) (width * 0.0f), (int) (width * 0.5f), 0, 0);
        tvAllCity.setLayoutParams(lpCity);
        tvAllCity.setText(busareaDtos.get(position).getMerchantNum() + "+");
        tvAllCity.setGravity(CENTER);
        tvAllCity.setTranslationX(width * (0.3f - busareaDtos.get(position).getHot() / 20.0f));
        tvAllCity.setScaleY(outScale);
        tvAllCity.setScaleX(outScale);
        View.OnClickListener areaMerchantsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 查看对应区域的酒吧
                Context context = HomeStarActivity.this;
                String str = "home_all_city_click";
                Map<String, String> map = new HashMap<>();
                map.put("busId", busareaDtos.get(position).getBusId() + "");
                map.put("cityCode", cityCode);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(HomeStarActivity.this,
                                R.anim.slide_right_in,
                                R.anim.fade_out);
                Intent intent = new Intent(HomeStarActivity.this, BizAreaBarsActivity.class);
                intent.putExtra("bid", busareaDtos.get(position).getBusId());
                intent.putExtra("bname", busareaDtos.get(position).getBusName());
                ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
            }
        };
        tvAllCity.setOnClickListener(areaMerchantsListener);
        TextView tvAreaName = ButterKnife.findById(view, R.id.home_star_left_area_name);
        tvAreaName.setAlpha(0);
        RelativeLayout.LayoutParams lpArea = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpArea.addRule(RelativeLayout.RIGHT_OF, R.id.home_circle_bg);
        lpArea.setMargins(ScreenUtil.dp2px(this, 8), (int) (width * 0.45f), 0, 0);
        tvAreaName.setLayoutParams(lpArea);
        if (position == 0 && TextUtils.equals(cityLocation, cityName)) {
            tvAreaName.setText(busareaDtos.get(position).getBusName() + "-附近商圈");
        } else if (position == 0) {
            tvAreaName.setText(busareaDtos.get(position).getBusName() + "-商圈");
        } else
            tvAreaName.setText(busareaDtos.get(position).getBusName());
        tvAreaName.postInvalidateDelayed(10);
        return ButterKnife.findById(view, R.id.home_star_left_item_parent);
    }

    private void initLeftHotMerchantView(final List<HotMerchant.RespListBean> respList, final int position
            , int drawSize, int[] merchantNameHeights, View[] parties, RelativeLayout leftParent) {
        int hot = respList.get(position).getHot();
        float outScale = (float) (1 / (0.6 + 0.1 * hot));
        for (int j = 0; j < drawSize; j++) {
            View merchantView = inflater.inflate(R.layout.home_merchant_item, null);
            HotMerchant.RespListBean.MerchantsBean merchantsBean = respList.get(position).getMerchants().get(j);
            dispatchNewMerchant(merchantsBean, merchantView);
            TextView view_party = (TextView) merchantView.findViewById(R.id.party);
            parties[j] = view_party;
            if ((respList.get(position).getMerchants().get(j).getActivityType() == 2
                    || respList.get(position).getMerchants().get(j).getActivityType() == 6)) {
                view_party.setBackgroundResource(R.drawable.party_starnightpage);
                view_party.setText(respList.get(position).getMerchants().get(j).getActivityTitle());
                view_party.setVisibility(View.VISIBLE);
            } else if ((respList.get(position).getMerchants().get(j).getActivityType() == 1
                    || respList.get(position).getMerchants().get(j).getActivityType() == 5)) {
                view_party.setBackgroundResource(R.drawable.live_starnightpage);
                view_party.setText(respList.get(position).getMerchants().get(j).getActivityTitle());
                view_party.setVisibility(View.VISIBLE);
            } else {
                view_party.setVisibility(View.INVISIBLE);
            }
            final int finalJ = j;
            view_party.setScaleY(outScale);
            view_party.setScaleX(outScale);
            view_party.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            HomeStarActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(HomeStarActivity.this, LiveActivity.class);
                    intent.putExtra(KEY_ACT_ID, respList.get(position).getMerchants().get(finalJ).getActivityId());
                    ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
                }
            });
            View.OnClickListener merchantListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 查看酒吧详情
                    Context context = HomeStarActivity.this;
                    String str = "home_bar_click";
                    Map<String, String> map = new HashMap<>();
                    map.put("mid", respList.get(position).getMerchants().get(finalJ).getMerchantId() + "");
                    map.put("cityCode", cityCode);
                    if (Build.VERSION.SDK_INT > 22) {
                        int du = 2000000000;
                        MobclickAgent.onEventValue(context, str, map, du);
                    } else {
                        MobclickAgent.onEvent(context, str, map);
                    }
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            HomeStarActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(HomeStarActivity.this, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", respList.get(position).getMerchants().get(finalJ).getMerchantId() + "");
                    ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
                }
            };
            merchantView.setOnClickListener(merchantListener);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (int) (width * 0.55), WRAP_CONTENT);
            switch (drawSize) {
                case 1:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins((int) (width * circleXPotionRate1[j]), circleTopArr1[j], 0, 0);
                    }
                    break;
                case 2:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins((int) (width * circleXPotionRate2[j]), circleTopArr2[j], 0, 0);
                    } else if (j == 1) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins((int) (width * circleXPotionRate2[j]), circleTopArr2[j], 0, 0);
                    }
                    break;
                case 3:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins((int) (width * circleXPotionRate3[j]), circleTopArr3[j], 0, 0);
                    } else if (j == 1) {
                        lp.addRule(RelativeLayout.RIGHT_OF, R.id.home_all_city_text);
                        lp.setMargins((int) (width * circleXPotionRate3[j]), circleTopArr3[j], 0, 0);
                    } else if (j == 2) {
                        lp.addRule(RelativeLayout.RIGHT_OF, R.id.home_all_city_text);
                        lp.setMargins((int) (width * circleXPotionRate3[j]), circleTopArr3[j], 0, 0);
                    }
                    break;
                case 4:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins((int) (width * circleXPotionRate4[j]), circleTopArr4[j], 0, 0);
                    } else if (j == 1) {
                        lp.addRule(RelativeLayout.RIGHT_OF, R.id.home_all_city_text);
                        lp.setMargins((int) (width * circleXPotionRate4[j]), circleTopArr4[j], 0, 0);
                    } else if (j == 2) {
                        lp.addRule(RelativeLayout.RIGHT_OF, R.id.home_all_city_text);
                        lp.setMargins((int) (width * circleXPotionRate4[j]), circleTopArr4[j], 0, 0);
                    } else if (j == 3) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins((int) (width * circleXPotionRate4[j]), circleTopArr4[j], 0, 0);
                    }
                    break;
            }
            merchantView.setLayoutParams(lp);
            merchantView.setTranslationX(width * (0.2f - hot / 20.0f));
            AutoSplitTextView merchantName = ButterKnife.findById(merchantView,
                    R.id.home_start_merchant_name);
            view_party.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1));
            if (position == 0 && TextUtils.equals(cityLocation, cityName)) {
                String distance = AppUtils.getDistance(
                        MoreUser.getInstance().getUserLocationLat(),
                        MoreUser.getInstance().getUserLocationLng(),
                        respList.get(position).getMerchants().get(j).getLat(),
                        respList.get(position).getMerchants().get(j).getLng());
                merchantName.setText(respList.get(position).getMerchants().get(j).getMerchantName());
                TextView nearDistance = (TextView) merchantView.findViewById(R.id.near_distance);
                nearDistance.setVisibility(View.VISIBLE);
                nearDistance.setText(distance);
                float textWidth = getTextWidth(merchantName, respList.get(position).getMerchants().get(j).getMerchantName());
                if (textWidth < ScreenUtil.dp2px(this, 100)) {
                    nearDistance.setTranslationY(-ScreenUtil.dp2px(this, 6) - getViewWidthAndHeight(merchantName)[1] * (outScale - 1) / 2);
                } else if (textWidth < ScreenUtil.dp2px(this, 200)) {
                    textWidth %= ScreenUtil.dp2px(this, 100);
                    nearDistance.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1) / 4);
                } else
                    textWidth = ScreenUtil.dp2px(this, 200);
                nearDistance.setTranslationX((ScreenUtil.dp2px(this, 108) + textWidth * outScale) / 2);
                nearDistance.setScaleY(outScale);
                nearDistance.setScaleX(outScale);
            } else {
                merchantName.setText(respList.get(position).getMerchants().get(j).getMerchantName());
            }
            List<HotMerchant.RespListBean.MerchantsBean.CardListBean> cardList = respList.get(position).getMerchants().get(j).getCardList();
            ImageView iv_orange = ButterKnife.findById(merchantView,
                    R.id.iv_orange);
            ImageView iv_black = ButterKnife.findById(merchantView,
                    R.id.iv_black);
            if (position == 0 && TextUtils.equals(cityLocation, cityName)) {
                iv_orange.setVisibility(View.GONE);
                iv_black.setVisibility(View.GONE);
            } else if (cardList != null) {
                hotLayoutView(respList.get(position), j, merchantName, iv_orange, iv_black, cardList, outScale);
                iv_orange.setScaleY(outScale);
                iv_orange.setScaleX(outScale);
                iv_black.setScaleY(outScale);
                iv_black.setScaleX(outScale);
            } else {
                iv_orange.setVisibility(View.GONE);
                iv_black.setVisibility(View.GONE);
            }
            TextPaint textPaint = merchantName.getPaint();
            float textPaintWidth = textPaint.measureText(respList.get(position).getMerchants().get(j).getMerchantName());
            int width = ScreenUtil.dp2px(HomeStarActivity.this, 100);
            int[] ints = getViewWidthAndHeight(merchantName);
            merchantNameHeight = ints[1];
            if (textPaintWidth < width) {
                merchantNameHeights[j] = merchantNameHeight / 2;
            } else
                merchantNameHeights[j] = merchantNameHeight / 2;
            WaveView waveView = ButterKnife.findById(merchantView,
                    R.id.home_start_merchant_circle);
            waveView.setColor(merchantCircleColor);
            if (j == 0) {
                RelativeLayout.LayoutParams waveLp = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(HomeStarActivity.this, 80)
                        , ScreenUtil.dp2px(HomeStarActivity.this, 80));
                waveLp.addRule(RelativeLayout.BELOW, R.id.home_start_merchant_circle_title);
                waveLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                waveLp.setMargins(0, -ScreenUtil.dp2px(HomeStarActivity.this, 20), 0, 0);
                waveView.setLayoutParams(waveLp);
                waveView.setInitialRadius(merchantCircleWidthInit * 4 / 3);
                waveView.setMaxRadius(merchantCircleWidth * 4 / 3);
            } else {
                waveView.setInitialRadius(merchantCircleWidthInit);
                waveView.setMaxRadius(merchantCircleWidth);
            }
            waveView.setDuration(4000);
            waveView.setStyle(Paint.Style.FILL);
            waveView.setInterpolator(new LinearOutSlowInInterpolator());
            waveView.start();
            merchantName.setScaleY(outScale);
            merchantName.setScaleX(outScale);
            merchantName.setTranslationY(-merchantNameHeight * (outScale - 1) / 2);
            leftParent.addView(merchantView, 3);
        }
    }

    /**
     * zune:处理右侧所有数据
     **/
    private void initRightHotData(final List<HotMerchant.RespListBean> respList, final int position, View view, final int size) {
        int merchantNum = respList.get(position).getMerchants().size();
        int drawSize = Math.min(4, merchantNum);
        if (respList.get(position).getHot() < 3 && drawSize > 2) {
            drawSize = 2;
        }
        final int[] merchantNameHeights = new int[drawSize];
        final View[] parties = new View[drawSize];
        final TextView tvAllCity = ButterKnife.findById(view, R.id.home_all_city_text);
        final RelativeLayout rightParent = getRightHotParent(respList, position, view, tvAllCity);
        initRightHotMerchantView(respList.get(position), drawSize, merchantNameHeights, parties, rightParent);
    }

    private RelativeLayout getRightHotParent(final List<HotMerchant.RespListBean> respList, final int position, View view, TextView tvAllCity) {
        float scale = (float) (0.1 * (respList.get(position).getHot()));
        float outScale = (float) (1 / (0.6 + 0.1 * respList.get(position).getHot()));
        tvAllCity.setText(respList.get(position).getMerchantNum() + "+");
        tvAllCity.setTranslationX(width * (1 - scale / 2) - width * (0.2f - respList.get(position).getHot() / 20.0f));
        RelativeLayout.LayoutParams lpCity = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(HomeStarActivity.this, 40), ScreenUtil.dp2px(HomeStarActivity.this, 40));
        lpCity.setMargins((int) (width * 0.0f), (int) (width * 0.5f), 0, 0);
        tvAllCity.setLayoutParams(lpCity);
        tvAllCity.setGravity(CENTER);
        tvAllCity.setScaleY(outScale);
        tvAllCity.setScaleX(outScale);
        View.OnClickListener areaMerchantsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 查看对应区域的酒吧
                Context context = HomeStarActivity.this;
                String str = "home_all_city_click";
                Map<String, String> map = new HashMap<>();
                map.put("busId", respList.get(position).getBusId() + "");
                map.put("cityCode", cityCode);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(HomeStarActivity.this,
                                R.anim.slide_right_in,
                                R.anim.fade_out);
                Intent intent = new Intent(HomeStarActivity.this, BizAreaBarsActivity.class);
                intent.putExtra("bid", respList.get(position).getBusId());
                intent.putExtra("bname", respList.get(position).getBusName());
                ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
            }
        };
        tvAllCity.setOnClickListener(areaMerchantsListener);
        TextView tvAreaName = ButterKnife.findById(view, R.id.home_star_tight_area_name);
        tvAreaName.setAlpha(0);
        RelativeLayout.LayoutParams lpArea = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpArea.addRule(RelativeLayout.LEFT_OF, R.id.home_circle_bg);
        lpArea.setMargins(0, (int) (width * 0.45f), ScreenUtil.dp2px(this, 8), 0);
        tvAreaName.setLayoutParams(lpArea);
        tvAreaName.setText(respList.get(position).getBusName());
        tvAreaName.postInvalidateDelayed(10);
        return ButterKnife.findById(view, R.id.home_star_right_item_parent);
    }

    private void initRightHotMerchantView(final HotMerchant.RespListBean merchantBizArea, int drawSize
            , int[] merchantNameHeights, View[] parties, RelativeLayout rightParent) {
        int hot = merchantBizArea.getHot();
        float outScale = (float) (1 / (0.6 + 0.1 * hot));
        for (int j = 0; j < drawSize; j++) {
            View merchantView = inflater.inflate(R.layout.home_merchant_item, null);
            dispatchNewMerchant(merchantBizArea.getMerchants().get(j), merchantView);
            TextView view_party = (TextView) merchantView.findViewById(R.id.party);
            parties[j] = view_party;
            if ((merchantBizArea.getMerchants().get(j).getActivityType() == 2
                    || merchantBizArea.getMerchants().get(j).getActivityType() == 6)) {
                view_party.setBackgroundResource(R.drawable.party_starnightpage);
                view_party.setText(merchantBizArea.getMerchants().get(j).getActivityTitle());
                view_party.setVisibility(View.VISIBLE);
            } else if ((merchantBizArea.getMerchants().get(j).getActivityType() == 1
                    || merchantBizArea.getMerchants().get(j).getActivityType() == 5)) {
                view_party.setBackgroundResource(R.drawable.live_starnightpage);
                view_party.setText(merchantBizArea.getMerchants().get(j).getActivityTitle());
                view_party.setVisibility(View.VISIBLE);
            } else {
                view_party.setVisibility(View.INVISIBLE);
            }
            final int finalJ = j;
            view_party.setScaleY(outScale);
            view_party.setScaleX(outScale);
            view_party.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            HomeStarActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(HomeStarActivity.this, LiveActivity.class);
                    intent.putExtra(KEY_ACT_ID, merchantBizArea.getMerchants().get(finalJ).getActivityId());
                    ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
                }
            });
            View.OnClickListener merchantListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo 查看酒吧详情
                    Context context = HomeStarActivity.this;
                    String str = "home_bar_click";
                    Map<String, String> map = new HashMap<>();
                    map.put("mid", merchantBizArea.getMerchants().get(finalJ).getMerchantId() + "");
                    map.put("cityCode", cityCode);
                    if (Build.VERSION.SDK_INT > 22) {
                        int du = 2000000000;
                        MobclickAgent.onEventValue(context, str, map, du);
                    } else {
                        MobclickAgent.onEvent(context, str, map);
                    }
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            HomeStarActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(HomeStarActivity.this, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", merchantBizArea.getMerchants().get(finalJ).getMerchantId() + "");
                    ActivityCompat.startActivity(HomeStarActivity.this, intent, compat.toBundle());
                }
            };
            merchantView.setOnClickListener(merchantListener);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    (int) (width * 0.55), WRAP_CONTENT);
            switch (drawSize) {
                case 1:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.setMargins(0, circleTopArr1[j], (int) (width * 0.0f), 0);
                        merchantView.setLayoutParams(lp);
                    }
                    break;
                case 2:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.setMargins(0, circleTopArr2[j], (int) (width * 0.1f), 0);
                        merchantView.setLayoutParams(lp);
                    } else if (j == 1) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.home_all_city_text);
                        lp.setMargins(0, circleTopArr2[j], (int) (width * 0.1f), 0);
                        merchantView.setLayoutParams(lp);
                    }
                    break;
                case 3:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.setMargins(0, circleTopArr3[j], (int) (width * circleXPotionRate3[j]), 0);
                        merchantView.setLayoutParams(lp);
                    } else if (j == 1) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.home_all_city_text);
                        lp.setMargins(0, circleTopArr3[j], (int) (width * 0.2f), 0);
                        merchantView.setLayoutParams(lp);
                    } else if (j == 2) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.home_all_city_text);
                        lp.setMargins(0, circleTopArr3[j], (int) (width * circleXPotionRate3[j]), 0);
                        merchantView.setLayoutParams(lp);
                    }
                    break;
                case 4:
                    if (j == 0) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.setMargins(0, circleTopArr4[j], (int) (width * circleXPotionRate4[j]), 0);
                        merchantView.setLayoutParams(lp);
                    } else if (j == 1) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.home_all_city_text);
                        lp.setMargins(0, circleTopArr4[j], (int) (width * 0.23f), 0);
                        merchantView.setLayoutParams(lp);
                    } else if (j == 2) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.home_all_city_text);
                        lp.setMargins(0, circleTopArr4[j], (int) (width * 0.25f), 0);
                        merchantView.setLayoutParams(lp);
                    } else if (j == 3) {
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.setMargins(0, circleTopArr4[j], (int) (width * circleXPotionRate4[j]), 0);
                        merchantView.setLayoutParams(lp);
                    }
                    break;
            }
            merchantView.setTranslationX(width * (hot / 20.0f - 0.2f));

            AutoSplitTextView merchantName = ButterKnife.findById(merchantView,
                    R.id.home_start_merchant_name);
            ImageView iv_orange = ButterKnife.findById(merchantView,
                    R.id.iv_orange);
            ImageView iv_black = ButterKnife.findById(merchantView,
                    R.id.iv_black);
            merchantName.setText(merchantBizArea.getMerchants().get(j).getMerchantName());
            view_party.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1));
            List<HotMerchant.RespListBean.MerchantsBean.CardListBean> cardList = merchantBizArea.getMerchants().get(j).getCardList();
            if (cardList != null) {
                hotLayoutView(merchantBizArea, j, merchantName, iv_orange, iv_black, cardList, outScale);
                iv_orange.setScaleY(outScale);
                iv_orange.setScaleX(outScale);
                iv_black.setScaleY(outScale);
                iv_black.setScaleX(outScale);
            } else {
                iv_orange.setVisibility(View.GONE);
                iv_black.setVisibility(View.GONE);
            }
            TextPaint textPaint = merchantName.getPaint();
            float textPaintWidth = textPaint.measureText(merchantBizArea.getMerchants().get(j).getMerchantName());
            int width = ScreenUtil.dp2px(HomeStarActivity.this, 100);
            int[] ints = getViewWidthAndHeight(merchantName);
            merchantNameHeight = ints[1];
            if (textPaintWidth < width) {
                merchantNameHeights[j] = merchantNameHeight / 2;
            } else
                merchantNameHeights[j] = merchantNameHeight / 2;
            WaveView waveView = ButterKnife.findById(merchantView,
                    R.id.home_start_merchant_circle);
            waveView.setColor(merchantCircleColor);
            if (j == 0) {
                RelativeLayout.LayoutParams waveLp = new RelativeLayout.LayoutParams(ScreenUtil.dp2px(HomeStarActivity.this, 80)
                        , ScreenUtil.dp2px(HomeStarActivity.this, 80));
                waveLp.addRule(RelativeLayout.BELOW, R.id.home_start_merchant_circle_title);
                waveLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                waveLp.setMargins(0, -ScreenUtil.dp2px(HomeStarActivity.this, 20), 0, 0);
                waveView.setLayoutParams(waveLp);
                waveView.setInitialRadius(merchantCircleWidthInit * 4 / 3);
                waveView.setMaxRadius(merchantCircleWidth * 4 / 3);
            } else {
                waveView.setInitialRadius(merchantCircleWidthInit);
                waveView.setMaxRadius(merchantCircleWidth);
            }
            waveView.setDuration(4000);
            waveView.setStyle(Paint.Style.FILL);
            waveView.setInterpolator(new LinearOutSlowInInterpolator());
            waveView.start();
            merchantName.setScaleY(outScale);
            merchantName.setScaleX(outScale);
            merchantName.setTranslationY(-merchantNameHeight * (outScale - 1) / 2);
            rightParent.addView(merchantView, 3);
        }
    }

    private void hotLayoutView(HotMerchant.RespListBean merchantBizArea, int j, TextView merchantName
            , ImageView iv_orange, ImageView iv_black, List<HotMerchant.RespListBean.MerchantsBean.CardListBean> cardList, float outScale) {
        Drawable drawable = getResources().getDrawable(R.drawable.star_blackcard);
        int drawableMinimumWidth = drawable.getMinimumWidth();
        iv_orange.setVisibility(View.VISIBLE);
        iv_black.setVisibility(View.VISIBLE);
        float textWidth = getTextWidth(merchantName, merchantBizArea.getMerchants().get(j).getMerchantName());
        if (textWidth < ScreenUtil.dp2px(this, 100)) {
            iv_orange.setTranslationY(-ScreenUtil.dp2px(this, 6));
            iv_black.setTranslationY(-ScreenUtil.dp2px(this, 6));
            iv_orange.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale) / 4);
            iv_black.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale) / 4);
        } else if (textWidth < ScreenUtil.dp2px(this, 200)) {
            textWidth %= ScreenUtil.dp2px(this, 100);
            iv_orange.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1) / 2);
            iv_black.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1) / 2);
        } else {
            textWidth = ScreenUtil.dp2px(this, 200);
            iv_orange.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1) / 2);
            iv_black.setTranslationY(-getViewWidthAndHeight(merchantName)[1] * (outScale - 1) / 2);
        }
        if (cardList.size() == 1) {
            iv_black.setVisibility(View.GONE);
            if (outScale == 1) {
                iv_orange.setTranslationX((ScreenUtil.dp2px(this, 108) + textWidth * outScale + drawableMinimumWidth * (outScale - 1)) / 2);
            } else
                iv_orange.setTranslationX((ScreenUtil.dp2px(this, 100) + textWidth * outScale + drawableMinimumWidth * (outScale - 1)) / 2);
        } else if (cardList.size() == 2) {
            if (outScale == 1) {
                iv_orange.setTranslationX((ScreenUtil.dp2px(this, 108) + textWidth * outScale + drawableMinimumWidth * (outScale - 1)) / 2);
                iv_black.setTranslationX((ScreenUtil.dp2px(this, 109) + textWidth * outScale + drawableMinimumWidth * (outScale - 1) * 2) / 2);
            } else {
                iv_orange.setTranslationX((ScreenUtil.dp2px(this, 106) + textWidth * outScale + drawableMinimumWidth * (outScale - 1)) / 2);
                iv_black.setTranslationX((ScreenUtil.dp2px(this, 108) + textWidth * outScale + drawableMinimumWidth * (outScale - 1) * 2) / 2);
            }
        }
    }

    /**
     * zune:处理热门商圈
     **/
    private void dispatchNewMerchant(HotMerchant.RespListBean.MerchantsBean merchantItem, View merchantView) {
        ImageView ivNewMerchant = (ImageView) merchantView.findViewById(R.id.iv_new_merchant);
        String shelveDate = merchantItem.getShelveDate();
        if (shelveDate == null) {
            ivNewMerchant.setVisibility(View.INVISIBLE);
            Log.i("zune:","merchantItem = "+merchantItem.getMerchantName());
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(shelveDate);
            long time = date.getTime();
            if (System.currentTimeMillis() - time < 3 * 24 * 60 * 60 * 1000) {
                ivNewMerchant.setVisibility(View.VISIBLE);
            } else {
                ivNewMerchant.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ivNewMerchant.setVisibility(View.INVISIBLE);
        }
    }

    private int[] getViewWidthAndHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(width, height);

        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    private void drawLineLocation(final List<float[]> sendLocations, final List<RelativeLayout> layouts) {
        final boolean[] hasAllCityLoaded = {false, false, false};
        final boolean[] hasLoaded = {false};
        if (hasAllCityLoaded[0])
            addLinesToFrameLay(sendLocations);
        for (int i = 0; i < layouts.size(); i++) {
            final int finalI = i;
            layouts.get(i).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!hasLoaded[0]) {
                        if (!hasAllCityLoaded[finalI]) {
                            hasAllCityLoaded[finalI] = true;
                        }
                        if (hasAllCityLoaded[0] && hasAllCityLoaded[1] && hasAllCityLoaded[2]) {
                            hasLoaded[0] = true;
                            float[] viewLoacation0 = getViewLoacation(layouts.get(0));
                            float[] viewLoacation1 = getViewLoacation(layouts.get(1));
                            float[] viewLoacation2 = getViewLoacation(layouts.get(2));
                            int[] viewWidthAndHeight0 = getViewWidthAndHeight(layouts.get(0));
                            viewLoacation0[0] += viewWidthAndHeight0[0] / 2;
                            viewLoacation0[1] += viewWidthAndHeight0[1] / 2;
                            sendLocations.add(0, viewLoacation0);
                            int[] viewWidthAndHeight1 = getViewWidthAndHeight(layouts.get(1));
                            viewLoacation1[0] += viewWidthAndHeight1[0] / 2;
                            viewLoacation1[1] += viewWidthAndHeight1[1] / 2;
                            sendLocations.add(1, viewLoacation1);
                            int[] viewWidthAndHeight2 = getViewWidthAndHeight(layouts.get(2));
                            viewLoacation2[0] += viewWidthAndHeight2[0] / 2;
                            viewLoacation2[1] += viewWidthAndHeight2[1] / 2;
                            sendLocations.add(2, viewLoacation2);
                            addLinesToFrameLay(sendLocations);
                        }
                    }
                }
            });
        }
//        allCityLay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (!hasAllCityLoaded[0]) {
//                    hasAllCityLoaded[0] = true;
//                    TextView merchantCount = (TextView) allCityLay.findViewById(R.id.merchant_count);
//                    merchantCount.setText(totalCount + "+");
//                    merchantCount.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AppUtils.pushAnimActivity(HomeStarActivity.this, TotalBarsActivity.class,
//                                    R.anim.slide_right_in, R.anim.fade_out);
//                        }
//                    });
//                    float[] viewLoacation = getViewLoacation(merchantCount);
//                    int[] viewWidthAndHeight1 = getViewWidthAndHeight(merchantCount);
//                    viewLoacation[0] += viewWidthAndHeight1[0] / 2;
//                    viewLoacation[1] += viewWidthAndHeight1[1] / 2;
//                    sendLocations.add(0, viewLoacation);
//                    addLinesToFrameLay(sendLocations);
//                }
//            }
//        });
    }

    private void addLinesToFrameLay(List<float[]> sendLocations) {
        LinesView linesView = new LinesView(HomeStarActivity.this);
        linesView.setTag(ROOT);
        linesView.setLocations(sendLocations);
        int[] viewWidthAndHeight = getViewWidthAndHeight(linearLayout);
        RelativeLayout.LayoutParams linesParams = new RelativeLayout.LayoutParams(width, viewWidthAndHeight[1]);
        linesView.setLayoutParams(linesParams);
        fl_root.addView(linesView, 0);
        fl_root.postInvalidate();
        canScroll = true;
    }

    private List<RelativeLayout> addBusNames() {
        if (areaNames.size() == 0 || areaLocations.size() == 0 || hots.size() == 0)
            return null;
        deleteBusView();
        for (int i = 0; i < areaNames.size(); i++) {
            if (i == 0 && TextUtils.equals(cityLocation, cityName)) {
                areaNames.set(i, areaNames.get(i) + "-附近商圈");
            } else if (i == 0) {
                areaNames.set(i, areaNames.get(i) + "-商圈");
            }
            TextView busName = new TextView(this);
            busName.setTextSize(18);
            busName.setTextColor(Color.parseColor("#48FFFFFF"));
            busName.setMaxLines(2);
            busName.setText(areaNames.get(i));
            if (i % 2 == 1) {
                final float outScale = (float) (1 / (0.6 + 0.1 * hots.get(i)));
                double width = 0.28 * this.width * outScale;
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) width, (int) width);
                busName.setLayoutParams(lp);
                busName.setTranslationX(areaLocations.get(i)[0] - 0.1f * this.width * (outScale - 1));
            } else {
                final float outScale = (float) (1 / (0.6 + 0.1 * hots.get(i)));
                double width = 0.28 * this.width * outScale;
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) width, (int) width);
                busName.setLayoutParams(lp);
                busName.setTranslationX(areaLocations.get(i)[0]);
            }
            busName.setTranslationY(areaLocations.get(i)[1]);
            fl_root.addView(busName, fl_root.getChildCount() - 1);
        }
//        RelativeLayout allCityLay = addAllCityLay();
        RelativeLayout palyLay = addPlayLay();
        RelativeLayout hotLiveLay = addHotLiveLay();
        RelativeLayout discoveryLay = addDiscoveryLay();
        List<RelativeLayout> layouts = new ArrayList<>();
        layouts.add(palyLay);
        layouts.add(hotLiveLay);
        layouts.add(discoveryLay);
        return layouts;
    }

    private RelativeLayout addAllCityLay() {
        RelativeLayout allCityLay = (RelativeLayout) inflater.inflate(R.layout.all_city_layout, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ScreenUtil.dp2px(this, 128), ScreenUtil.dp2px(this, 128));
        allCityLay.setLayoutParams(lp);
        allCityLay.setTranslationY(statusBarHeight + ScreenUtil.dp2px(this, 48));
        allCityLay.setTranslationX(width - ScreenUtil.dp2px(this, 128 + 16));
        fl_root.addView(allCityLay);
        return allCityLay;
    }

    private RelativeLayout addPlayLay() {
        RelativeLayout addPlayLay = (RelativeLayout) inflater.inflate(R.layout.add_play_layout, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) (width / 2.0f - 20)
                , (int) (width / 2.0f - 20));
        addPlayLay.setLayoutParams(lp);
        ImageView iv_bg_all_city = (ImageView) addPlayLay.findViewById(R.id.iv_bg_all_city);
        RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams((int) (width / 2.0f - 20)
                , (int) (width / 2.0f - 20));
        iv_bg_all_city.setLayoutParams(ivparams);
        addPlayLay.setTranslationY(statusBarHeight + ScreenUtil.dp2px(this, 8));
        addPlayLay.setTranslationX(width / 2.0f);
        fl_root.addView(addPlayLay);
        return addPlayLay;
    }

    private RelativeLayout addHotLiveLay() {
        RelativeLayout addHotLiveLay = (RelativeLayout) inflater.inflate(R.layout.add_hot_layout, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) (width / 3.0f)
                , (int) (width / 3.0f));
        addHotLiveLay.setLayoutParams(lp);
        ImageView iv_bg_all_city = (ImageView) addHotLiveLay.findViewById(R.id.iv_bg_all_city);
        RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams((int) (width / 3.0f)
                , (int) (width / 3.0f));
        iv_bg_all_city.setLayoutParams(ivparams);
        addHotLiveLay.setTranslationY(width / 3.0f);
        addHotLiveLay.setTranslationX(ScreenUtil.dp2px(this, 16));
        fl_root.addView(addHotLiveLay);
        return addHotLiveLay;
    }

    private RelativeLayout addDiscoveryLay() {
        RelativeLayout addDiscoveryLay = (RelativeLayout) inflater.inflate(R.layout.add_discovery_layout, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) (width / 3.0f)
                , (int) (width / 3.0f));
        addDiscoveryLay.setLayoutParams(lp);
        ImageView iv_bg_all_city = (ImageView) addDiscoveryLay.findViewById(R.id.iv_bg_all_city);
        RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams((int) (width / 3.0f)
                , (int) (width / 3.0f));
        iv_bg_all_city.setLayoutParams(ivparams);
        addDiscoveryLay.setTranslationY(width * 2.0f / 3.0f);
        addDiscoveryLay.setTranslationX(width - (int) (width / 3.0f) - ScreenUtil.dp2px(this, 16));
        fl_root.addView(addDiscoveryLay);
        return addDiscoveryLay;
    }

    private void deleteBusView() {
        if (fl_root != null && fl_root.getChildCount() > 0)
            for (int i = 0; i < fl_root.getChildCount(); i++) {
                if (fl_root != null && fl_root.getChildAt(i) instanceof TextView) {
                    fl_root.removeViewAt(i);
                    deleteBusView();
                    break;
                }
                if (fl_root != null && fl_root.getChildAt(i) instanceof RelativeLayout) {
                    if (((RelativeLayout) fl_root.getChildAt(i)).getChildCount() > 0) {
                        ((RelativeLayout) fl_root.getChildAt(i)).removeViewAt(0);
                    } else
                        fl_root.removeViewAt(i);
                    deleteBusView();
                    break;
                }
            }
    }

    private float[] getViewLoacation(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        float[] temp = new float[2];
        temp[0] = location[0];
        temp[1] = location[1];
        return temp;
    }

    /**
     * zune:处理底部btn
     **/
    private void hideBtn() {
        if (isShowed) {
            isShowed = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                tintManager.setTintColor(Color.parseColor("#00000000"));
            }
            ObjectAnimator entryBtn = ObjectAnimator.ofFloat(homeStarEntryBtn, "alpha", 1.0f, 0.0f);
            entryBtn.setDuration(200).start();
            ObjectAnimator activities = ObjectAnimator.ofFloat(homeActivities, "alpha", 1.0f, 0.0f);
            activities.setDuration(200).start();
            ObjectAnimator sales = ObjectAnimator.ofFloat(homeSales, "alpha", 1.0f, 0.0f);
            sales.setDuration(200).start();
            ObjectAnimator bg = ObjectAnimator.ofFloat(ivBtnBg, "alpha", 1.0f, 0.0f);
            bg.setDuration(200).start();
            ObjectAnimator bars = ObjectAnimator.ofFloat(mainToolbar, "alpha", 1.0f, 0.0f);
            bars.setDuration(200).start();
            ObjectAnimator barsBgs = ObjectAnimator.ofFloat(barBg, "alpha", 1.0f, 0.0f);
            barsBgs.setDuration(200).start();
        }
    }

    private void showBtn() {
        if (!isShowed) {
            isShowed = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                tintManager.setTintColor(Color.parseColor("#000000"));
            }
            ObjectAnimator entryBtn = ObjectAnimator.ofFloat(homeStarEntryBtn, "alpha", 0.0f, 1.0f);
            entryBtn.setDuration(200).start();
            ObjectAnimator activities = ObjectAnimator.ofFloat(homeActivities, "alpha", 0.0f, 1.0f);
            activities.setDuration(200).start();
            ObjectAnimator sales = ObjectAnimator.ofFloat(homeSales, "alpha", 0.0f, 1.0f);
            sales.setDuration(200).start();
            ObjectAnimator bg = ObjectAnimator.ofFloat(ivBtnBg, "alpha", 0.0f, 1.0f);
            bg.setDuration(200).start();
            ObjectAnimator bars = ObjectAnimator.ofFloat(mainToolbar, "alpha", 0.0f, 1.0f);
            bars.setDuration(200).start();
            ObjectAnimator barsBgs = ObjectAnimator.ofFloat(barBg, "alpha", 0.0f, 1.0f);
            barsBgs.setDuration(200).start();
        }
    }

    public float getTextWidth(TextView view, String text) {
        TextPaint paint = view.getPaint();
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * 12);
        return paint.measureText(text);
    }

    private void downLoadApp() {
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra("downloadUrl", Constants.downloadUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }

    /**
     * zune:处理下拉刷新逻辑
     **/
    private void activityComeBack() {
        scrollView.scrollVerticallyTo(0);
        totalCount = 0;
        if (wave_layout.handler != null)
            wave_layout.handler.removeCallbacksAndMessages(null);
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        updateCityView();
        cityCode = PrefsUtils.getString(this, KEY_CITY_CODE, "cd");
        cityName = PrefsUtils.getString(this, KEY_CITY_NAME, "成都");
        homeStarCityName.setText(cityName);
        dispatchGPS();
    }

    private void updateCityView() {
        wave_layout.setVisibility(View.VISIBLE);
        fl_root.setVisibility(View.GONE);
        hasLoadView = false;
        hasLoad = false;
        isUpdateView = true;
        isFirstAdd = true;
        deleteLinesView();
        loadDatas();
        rootLeftLocations.clear();
        rootRightLocations.clear();
        removeAllViews(linearLayout);
        final List<float[]> sendLocations = new ArrayList<>();
        if (!hasLoadView)
            fl_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getLinesLocation(sendLocations);
                }
            });
    }

    private void getLinesLocation(final List<float[]> sendLocations) {
        if (!hasLoadView && linearLayout != null && linearLayout.getChildCount() > 0
                && getViewWidthAndHeight((linearLayout.getChildAt(0)))[0] > 0
                && getViewWidthAndHeight((linearLayout.getChildAt(0)))[1] > 0) {
            hasLoadView = true;
            sendLocations.clear();
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                int hot = hots.get(i);
                float scale = (float) (0.6 + 0.1 * hot);
                RelativeLayout merchantParentLayout = (RelativeLayout) linearLayout.getChildAt(i);
                if (merchantParentLayout == null) return;
                TextView areaName = null;
                if (merchantParentLayout.getChildAt(0) instanceof TextView)
                    areaName = (TextView) merchantParentLayout.getChildAt(0);
                else if (merchantParentLayout.getChildAt(1) instanceof TextView)
                    areaName = (TextView) merchantParentLayout.getChildAt(1);
                float[] viewLoacation = getViewLoacation(areaName);
                areaLocations.add(viewLoacation);
                for (int i1 = merchantParentLayout.getChildCount() - 1; i1 >= 0; i1--) {
                    switch (merchantParentLayout.getChildCount() - 1) {
                        case 7:
                            if (merchantParentLayout.getChildAt(i1) instanceof RelativeLayout) {
                                RelativeLayout merchantLayout = (RelativeLayout) merchantParentLayout.getChildAt(i1);
                                for (int i2 = 0; i2 < merchantLayout.getChildCount(); i2++) {
                                    if (merchantLayout.getChildAt(i2) instanceof WaveView) {
                                        float[] waveLocation = getViewLoacation(merchantLayout.getChildAt(i2));
                                        if (i1 == 6) {
                                            waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                            waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                            waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                            waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                        } else {
                                            waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                            waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 30) * (1 - scale);
                                            waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                            waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 30) * (1 - scale);
                                        }
                                        sendLocations.add(waveLocation);
                                    }
                                }
                            }
                            if (i1 == 5) {
                                TextView tvCity = (TextView) merchantParentLayout.getChildAt(7);
                                float[] tvAllCityLocation = getViewLoacation(tvCity);
                                int[] viewWidthAndHeight = getViewWidthAndHeight(tvCity);
                                tvAllCityLocation[0] += viewWidthAndHeight[0] / 2;
                                tvAllCityLocation[1] += viewWidthAndHeight[1] / 2;
                                tvAllCityLocation[1] -= viewWidthAndHeight[1] / 2 * (1 - scale);
                                sendLocations.add(tvAllCityLocation);
                            }
                            break;
                        case 6:
                            if (merchantParentLayout.getChildAt(i1) instanceof RelativeLayout) {
                                RelativeLayout merchantLayout = (RelativeLayout) merchantParentLayout.getChildAt(i1);
                                for (int i2 = 0; i2 < merchantLayout.getChildCount(); i2++) {
                                    if (merchantLayout.getChildAt(i2) instanceof WaveView) {
                                        float[] waveLocation = getViewLoacation(merchantLayout.getChildAt(i2));
                                        if (i1 == 5) {
                                            waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                            waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                            waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                            waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                        } else {
                                            if (i1 == 4 && waveLocation[0] + ScreenUtil.dp2px(HomeStarActivity.this, 25) < width / 2)
                                                waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 10);
                                            else if (i1 == 4)
                                                waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                            if (i1 == 3 && waveLocation[0] + ScreenUtil.dp2px(HomeStarActivity.this, 25) < width / 2)
                                                waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 10);
                                            else if (i1 == 3)
                                                waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                            waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 30) * (1 - scale);
                                            waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 30) * (1 - scale);
                                            waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                        }
                                        sendLocations.add(waveLocation);
                                    }
                                }
                            }
                            merchantParentLayout.getChildAt(6).setVisibility(View.GONE);
                            break;
                        case 5:
                            TextView tvCity = (TextView) merchantParentLayout.getChildAt(5);
                            float[] tvAllCityLocation = getViewLoacation(tvCity);
                            int[] viewWidthAndHeight = getViewWidthAndHeight(tvCity);
                            if (hot < 3 && mMerchant.get(i).getMerchants().size() > 2)
                                tvCity.setVisibility(View.VISIBLE);
                            else
                                tvCity.setVisibility(View.INVISIBLE);
                            if (merchantParentLayout.getChildAt(i1) instanceof RelativeLayout) {
                                RelativeLayout merchantLayout = (RelativeLayout) merchantParentLayout.getChildAt(i1);
                                for (int i2 = 0; i2 < merchantLayout.getChildCount(); i2++) {
                                    if (merchantLayout.getChildAt(i2) instanceof WaveView) {
                                        float[] waveLocation = getViewLoacation(merchantLayout.getChildAt(i2));
                                        if (i1 == 4) {
                                            waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                            waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                            waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                            waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                        } else {
                                            waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                            waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 30) * (1 - scale);
                                            waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 30);
                                            waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 30) * (1 - scale);
                                        }
                                        sendLocations.add(waveLocation);
                                    }
                                }
                            }
                            if (i1 == 4 && mMerchant.get(i).getMerchants().size() > 2) {
                                tvAllCityLocation[0] += viewWidthAndHeight[0] / 2;
                                tvAllCityLocation[1] += viewWidthAndHeight[1] / 2;
                                tvAllCityLocation[1] -= viewWidthAndHeight[1] / 2 * (1 - scale);
                                sendLocations.add(tvAllCityLocation);
                            }
                            break;
                        case 4:
                            if (merchantParentLayout.getChildAt(i1) instanceof RelativeLayout) {
                                RelativeLayout merchantLayout = (RelativeLayout) merchantParentLayout.getChildAt(i1);
                                for (int i2 = 0; i2 < merchantLayout.getChildCount(); i2++) {
                                    if (merchantLayout.getChildAt(i2) instanceof WaveView) {
                                        float[] waveLocation = getViewLoacation(merchantLayout.getChildAt(i2));
                                        waveLocation[0] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                        waveLocation[0] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                        waveLocation[1] += ScreenUtil.dp2px(HomeStarActivity.this, 40);
                                        waveLocation[1] -= ScreenUtil.dp2px(HomeStarActivity.this, 40) * (1 - scale);
                                        sendLocations.add(waveLocation);
                                    }
                                }
                            }
                            merchantParentLayout.getChildAt(4).setVisibility(View.GONE);
                            break;
                    }
                }
            }
            for (int i = 0; i < sendLocations.size(); i++) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                    sendLocations.get(i)[1] -= statusBarHeight;
            }
            List<RelativeLayout> layouts = addBusNames();
            drawLineLocation(sendLocations, layouts);
        }
    }

    /**
     * zune:简单优化viewgroup移除内部view的逻辑
     **/
    private void removeAllViews(ViewGroup linearLayout) {
        if (linearLayout != null) {
            View childAt = linearLayout.getChildAt(0);
            if (childAt != null && childAt instanceof ViewGroup) {
                removeAllViews((ViewGroup) childAt);
                linearLayout.removeView(childAt);
                childAt = null;
                removeAllViews(linearLayout);
            } else if (childAt != null && childAt instanceof TextView) {
                ((TextView) childAt).setText("");
                linearLayout.removeView(childAt);
                childAt = null;
                removeAllViews(linearLayout);
            } else if (childAt != null && childAt instanceof ImageView) {
                Drawable drawable = ((ImageView) childAt).getDrawable();
                drawable = null;
                linearLayout.removeView(childAt);
                childAt = null;
                removeAllViews(linearLayout);
            } else if (childAt != null && childAt instanceof WaveView) {
                ((WaveView) childAt).stopImmediately();
                linearLayout.removeView(childAt);
                childAt = null;
                removeAllViews(linearLayout);
            } else if (childAt != null) {
                linearLayout.removeView(childAt);
                childAt = null;
                removeAllViews(linearLayout);
            }
        }
    }

    private void deleteLinesView() {
        if (fl_root != null && fl_root.getChildCount() > 0)
            for (int j = 0; j < fl_root.getChildCount(); j++) {
                if (fl_root.getChildAt(j) instanceof LinesView) {
                    fl_root.removeViewAt(j);
                    deleteLinesView();
                    break;
                }
            }
    }

    /**
     * zune:安装Apk
     **/
    private void installApk(Activity context, File apkPath) {
        Log.i("zune:", "file name = " + apkPath.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            Uri apkUri = FileProvider.getUriForFile(context, "com.mydomain.fileprovider", apkPath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkPath), "application/vnd.android.package-archive");
        }
        context.startActivityForResult(intent, INSTALL_CODE);
    }

    /**
     * zune:处理点击事件
     **/
    @OnClick({R.id.home_star_city_name, R.id.home_star_divider,
            R.id.main_btn_msg, R.id.main_user_avatar, R.id.home_star_entry_btn, R.id.home_activities
            , R.id.home_sales, R.id.home_btn_search, R.id.iv_errer})
    public void onViewClicked(View view) {
        Context context;
        String str;
        Map<String, String> map;
        switch (view.getId()) {
            case R.id.home_star_city_name:
            case R.id.home_star_divider:
                GPSResCount = 0;
                GPScount = 0;
                if (timer != null)
                    timer.cancel();
                AppUtils.pushAnimActivity(HomeStarActivity.this, SelectCityActivity.class,
                        R.anim.push_down_in, R.anim.fade_out);
                break;
            case R.id.home_btn_search:
                AppUtils.pushAnimActivity(HomeStarActivity.this, SearchEntryActivity.class,
                        R.anim.push_down_in, R.anim.fade_out);
                break;
            case R.id.home_star_entry_btn:
                context = HomeStarActivity.this;
                str = "home_entry_btn";
                map = new HashMap<>();
                map.put("cityCode", cityCode);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                AppUtils.pushLeftActivity(HomeStarActivity.this, MainActivity.class);
                break;
            case R.id.home_activities:
                context = HomeStarActivity.this;
                str = "home_activity_btn";
                map = new HashMap<>();
                map.put("cityCode", cityCode);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                homeActivities.setSelected(true);
                AppUtils.pushLeftActivity(HomeStarActivity.this, ActivitiesActivity.class);
                break;
            case R.id.home_sales:
                context = HomeStarActivity.this;
                str = "home_sale_btn";
                map = new HashMap<>();
                map.put("cityCode", cityCode);
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                homeSales.setSelected(true);
                if (MoreUser.getInstance().getUid().equals(0L))
                    AppUtils.pushLeftActivity(HomeStarActivity.this, UseLoginActivity.class);
                else
                    AppUtils.pushLeftActivity(HomeStarActivity.this, SalesActivity.class);
            case R.id.iv_errer:
                if (ivErrer.getVisibility() == View.VISIBLE) {
                    ivErrer.setVisibility(View.GONE);
                    canScroll = false;
                    down = true;
                    activityComeBack();
                    downY = 0;
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        homeActivities.setSelected(false);
        homeSales.setSelected(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!hasBack && intent.getIntExtra("from", -1) == 1) {
            if (timer != null)
                timer.cancel();
            activityComeBack();
            hasBack = true;
        }
        if (intent.getBooleanExtra("INSTALL_NOW", false)) {
            installApk(this, new File(getExternalCacheDir(), "More" + com.moreclub.moreapp.app.Constants.APP_version + ".apk"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (MoreUser.getInstance().isShowCouponDialog()) {
            if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() != 0) {
                ((HomeDataLoader) mPresenter).onH5Coupon();
                MoreUser.getInstance().setShowCouponDialog(false);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        handler.sendEmptyMessageDelayed(UPDATE_SELECTOR, 1000);
        hasBack = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onDestroy();
        linearLayout.removeAllViews();
        wave_layout.handler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onBackPressed() {
        if (mSignWindow != null) {
            mSignWindow.dismiss();
            mSignWindow = null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRedPointerEvent(RedPointerEvent event) {
        if (event.isShowPointer() || PrefsUtils.getInt(this, PUSH_RED, 0) + PrefsUtils.getInt(this, IM_RED, 0) > 0) {
            messageRedPointer.setVisibility(View.VISIBLE);
        } else {
            messageRedPointer.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLogoutEvent(UserLogoutEvent event) {
        Glide.with(this)
                .load(R.drawable.profilephoto_small)
                .dontAnimate()
                .into(mainUserAvatar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserUpdateEvent(UserUpdateEvent event) {
        Glide.with(this)
                .load(event.getUri())
                .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                .skipMemoryCache(true)
                .placeholder(R.drawable.profilephoto_small)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .into(mainUserAvatar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLoginEvent(UserLoginEvent event) {
        if (!TextUtils.isEmpty(event.getUserAvatar()))
            Glide.with(this)
                    .load(event.getUserAvatar())
                    .transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                    .dontAnimate()
                    .placeholder(R.drawable.profilephoto_small)
                    .into(mainUserAvatar);
        else
            mainUserAvatar.setImageResource(R.drawable.profilephoto_small);
    }

    private class StickerSpan extends ImageSpan {
        private String texts;

        public StickerSpan(Drawable b, int verticalAlignment, String texts) {
            super(b, verticalAlignment);
            this.texts = texts;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();
            int transY = (bottom - b.getBounds().bottom - ScreenUtil.dp2px(HomeStarActivity.this, 2.5));// 减去行间距
            if (mVerticalAlignment == ALIGN_BASELINE) {
                int textLength = 0;
                if (text != null)
                    textLength = text.length();
                for (int i = 0; i < textLength; i++) {
                    if (Character.isLetterOrDigit(text.charAt(i))) {
                        transY -= paint.getFontMetricsInt().descent;
                        break;
                    }
                }
            }
            if (y == bottom) {
                transY += ScreenUtil.dp2px(HomeStarActivity.this, 2.5);
            }
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
            if (texts != null) {
                paint.setColor(Color.BLACK);
                paint.setTextSize(ScreenUtil.dp2px(HomeStarActivity.this, 10));
                canvas.drawText(texts, x, y, paint);
            }
        }
    }
}
