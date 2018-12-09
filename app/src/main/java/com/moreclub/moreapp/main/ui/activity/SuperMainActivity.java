package com.moreclub.moreapp.main.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.constant.DemoHelper;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.RedPointerEvent;
import com.moreclub.moreapp.main.model.UpdateBody;
import com.moreclub.moreapp.main.model.UpdateResp;
import com.moreclub.moreapp.main.presenter.ISuperMainDataLoader;
import com.moreclub.moreapp.main.presenter.SuperMainDataLoader;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.ui.fragment.HomeStarFragment;
import com.moreclub.moreapp.main.ui.fragment.MChannelFragment;
import com.moreclub.moreapp.main.ui.fragment.MessageCenterFragment;
import com.moreclub.moreapp.main.ui.fragment.SpaceFragment;
import com.moreclub.moreapp.main.ui.fragment.UserCenterFragment;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.event.UserLoginEvent;
import com.moreclub.moreapp.ucenter.model.event.UserLogoutEvent;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.TabFragmentHost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

import static com.moreclub.moreapp.main.constant.Constants.IM_RED;
import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_ID;
import static com.moreclub.moreapp.main.constant.Constants.PUSH_RED;
import static com.moreclub.moreapp.main.ui.view.BaseTabView.PAGE_START;

public class SuperMainActivity extends AppCompatActivity implements OnClickListener
        , ISuperMainDataLoader.LoadSuperMainDataBinder {
    private static final int UPDATE_RED_POINT = 1001;
    private Class[] clas = new Class[]{HomeStarFragment.class, MChannelFragment.class, SpaceFragment.class, MessageCenterFragment.class,
            UserCenterFragment.class};

    private int images[] = new int[]{R.drawable.page_home_btn_selector, R.drawable.page_channel_btn_selector, 1, R.drawable.page_message_btn_selector,
            R.drawable.page_my_btn_selector};
    private ImageView main_image_center;
    private ImageView temp_bottom;
    private TabFragmentHost mTabHost;
    private int tabOldIndex;
    private BasePresenter mPresenter;
    private CircleImageView messageRedPointer;
    private boolean publishViewIsShow = false;
    private PopupWindow mPublishWindow;
    private View mPublishView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_RED_POINT:
                    messageRedPointer.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            List<Object> blockList = PrefsUtils.getDataList(SuperMainActivity.this, "chat_block");
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
    private String shouldGo;
    private int backCount;
    private HomeStarFragment fragment0;
    private MChannelFragment fragment1;
    private SpaceFragment fragment2;
    private MessageCenterFragment fragment3;
    private UserCenterFragment fragment4;
    private int versioncode;
    private int width;
    private int height;
    private LayoutInflater inflater;
    private OnClickListener publishSeatClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid() != null
                    && MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(SuperMainActivity.this, UseLoginActivity.class);
                return;
            }
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    SuperMainActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(SuperMainActivity.this, AddSignInterActivity.class);
            intent.putExtra("from", "SuperMainActivity");
            ActivityCompat.startActivity(SuperMainActivity.this, intent, compat.toBundle());
            closeCouponPopuWindow();
        }
    };
    private OnClickListener publishChannelClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (MoreUser.getInstance().getUid() != null
                    && MoreUser.getInstance().getUid().equals(0L)) {
                AppUtils.pushLeftActivity(SuperMainActivity.this, UseLoginActivity.class);
                return;
            }
            ActivityOptionsCompat compat5 = ActivityOptionsCompat.makeCustomAnimation(
                    SuperMainActivity.this, R.anim.push_down_in, R.anim.push_down_out);
            Intent intent5 = new Intent(SuperMainActivity.this, PublishMChannelActivity.class);
            intent5.putExtra("from", "SpaceFragment");
            ActivityCompat.startActivity(SuperMainActivity.this, intent5, compat5.toBundle());
            closeCouponPopuWindow();
        }
    };
    private OnClickListener closeWindowListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            closeCouponPopuWindow();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.super_main_activity);
        mPresenter = LogicProxy.getInstance().bind(ISuperMainDataLoader.class, this);
        initUI();
//        int widgetPx = getTabWidgetHeight();
//        if (widgetPx>ScreenUtil.dp2px(this, 60)) {
//            main_image_center.getLayoutParams().width = widgetPx;
//            main_image_center.getLayoutParams().height = widgetPx;
//        }
    }

    private void initFragment() {
        fragment0 = (HomeStarFragment) getSupportFragmentManager().findFragmentByTag("tab0");
        fragment1 = (MChannelFragment) getSupportFragmentManager().findFragmentByTag("tab1");
        fragment2 = (SpaceFragment) getSupportFragmentManager().findFragmentByTag("tab2");
        fragment3 = (MessageCenterFragment) getSupportFragmentManager().findFragmentByTag("tab3");
        fragment4 = (UserCenterFragment) getSupportFragmentManager().findFragmentByTag("tab4");
    }

    @Override
    protected void onResume() {
        mTabHost.setCurrentTab(tabOldIndex);
        super.onResume();
    }

    private void initUI() {
        //底部中间按钮控件
        width = ScreenUtil.getScreenWidth(this);
        height = ScreenUtil.getScreenHeight(this);
        initFragment();
        main_image_center = (ImageView) findViewById(R.id.main_image_center);
        main_image_center.setOnClickListener(this);
        temp_bottom = (ImageView) findViewById(R.id.temp_bottom);

        String[] tabIndicatorArray = getResources().getStringArray(R.array.arr_tab_indicator);
        mTabHost = (TabFragmentHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        inflater = getLayoutInflater();
        for (int i = 0; i < images.length; i++) {
            final View indicatorView = inflater.inflate(R.layout.list_item_viewpagerindicator, null);
            TextView tvIndicator = (TextView) indicatorView.findViewById(R.id.tv_title_indicator);
            tvIndicator.setText(tabIndicatorArray[i]);
            ImageView imageView = (ImageView) indicatorView.findViewById(R.id.ima_indicator);
            if (images[i] == 1) {

            } else {
                imageView.setImageResource(images[i]);
            }
            if (i == 3) {
                messageRedPointer = (CircleImageView) indicatorView.findViewById(R.id.civ_red_point);
            }
            //Typist添加tab切换事件
            mTabHost.addTab(mTabHost.newTabSpec("tab" + i).setIndicator(indicatorView), clas[i], null);
            mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

                @Override
                public void onTabChanged(String tabId) {
                    if (!"tab2".equals(tabId)) {
                        main_image_center.setImageResource(R.drawable.main_together_btn);
                        //创建ListView从Visible到Gone的动画
                        ObjectAnimator visibleToInVisable = ObjectAnimator.ofFloat(main_image_center, "rotation", 0.0f, 0.0f);
                        //设置插值器
                        visibleToInVisable.setInterpolator(new AccelerateInterpolator());
                        visibleToInVisable.setDuration(200);
                        visibleToInVisable.start();
                        publishViewIsShow = false;
                    }
                    switch (tabId) {
                        case "tab1":
                            tabOldIndex = 1;
                            break;
                        case "tab2":
                            main_image_center.setImageResource(R.drawable.main_publish_btn);
                            tabOldIndex = 2;
                            break;
                        case "tab3":
                            tabOldIndex = 3;
                            break;
                        case "tab4":
                            tabOldIndex = 4;
                            if (fragment4 != null && fragment4.uid != MoreUser.getInstance().getUid()) {
                                fragment4.loadData();
                                PrefsUtils.setBoolean(SuperMainActivity.this, Constants.KEY_UCENTER_FIRST, true);
                                fragment4.setupViews();
                            }
                            break;
                        default:
                            tabOldIndex = 0;
                            break;
                    }
                }
            });
        }
        shouldGo = getIntent().getStringExtra("shouldGo");
        if (!TextUtils.isEmpty(shouldGo) && shouldGo.equals("MessageCenterFragment")) {
            mTabHost.setCurrentTab(3);
        } else if (!TextUtils.isEmpty(shouldGo) && shouldGo.equals("UserCenterFragment")) {
            mTabHost.setCurrentTab(4);
        } else {
            mTabHost.setCurrentTab(0);
        }
        loadData();
        if (MoreUser.getInstance().getUid() != null && !MoreUser.getInstance().getUid().equals(0L)) {
            ((SuperMainDataLoader) mPresenter).onLoadRedPointer("" + MoreUser.getInstance().getUid(), "android");
            DemoHelper.hxLogin("" + MoreUser.getInstance().getUid(), "m" + MoreUser.getInstance().getUid());
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    private void loadData() {
        try {
            UpdateBody body = new UpdateBody();
            body.setPlatform(0);
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versioncode = pi.versionCode;
            body.setVersionCode(versioncode);
            ((SuperMainDataLoader) mPresenter).onUpdateMore(body);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initFragment();
        shouldGo = intent.getStringExtra("shouldGo");
        String from = intent.getStringExtra("from");
        if (!TextUtils.isEmpty(from) && from.equals("SelectCityActivity")) {
            mTabHost.setCurrentTab(0);
            if (fragment0 != null)
                fragment0.activityComeBack();
            if (fragment1 != null) {
                fragment1.cityID = PrefsUtils.getInt(this, KEY_CITY_ID, 2);
                fragment1.reLoadData();
            }
            if (fragment2 != null && fragment2.mTotalCitySpaceV3View != null
                    && fragment2.mTotalCitySpaceV3View.dataList != null) {
                fragment2.mTotalCitySpaceV3View.mPn = 0;
                fragment2.mTotalCitySpaceV3View.dataList.clear();
                fragment2.mTotalCitySpaceV3View.loadData();
            }
            return;
        }
        if (!TextUtils.isEmpty(shouldGo) && shouldGo.equals("MessageCenterFragment")) {
            mTabHost.setCurrentTab(3);
        } else if (!TextUtils.isEmpty(shouldGo) && shouldGo.equals("UserCenterFragment")) {
            mTabHost.setCurrentTab(4);
            if (fragment4 != null) {
                fragment4.loadData();
            }
        } else if (!TextUtils.isEmpty(shouldGo) && shouldGo.equals("MChannelFragment")) {
            mTabHost.setCurrentTab(1);
            if (!TextUtils.isEmpty(from) && from.equals("PublishMChannelActivity")
                    && !TextUtils.isEmpty(shouldGo) && shouldGo.equals("MChannelFragment")) {
                if (fragment1 != null && fragment1.viewPager != null
                        && fragment1.viewList != null && fragment1.viewList.size() > 0) {
                    fragment1.cityID = PrefsUtils.getInt(this, KEY_CITY_ID, 2);
                    fragment1.viewPager.setCurrentItem(2);
                    fragment1.mChannelSameCityView.page = PAGE_START;
                    if (fragment1.mChannelSameCityView.dataList != null)
                        fragment1.mChannelSameCityView.dataList.clear();
                    fragment1.mChannelSameCityView.loadData();
                }

            }
        } else if (!TextUtils.isEmpty(shouldGo) && shouldGo.equals("SpaceFragment")) {
            mTabHost.setCurrentTab(2);
            if (fragment2 != null) {
                fragment2.mTotalCitySpaceV3View.mPn = 0;
                fragment2.mTotalCitySpaceV3View.loadData();
            }
        } else {
            mTabHost.setCurrentTab(0);
        }
        EventBus.getDefault().post(new RedPointerEvent(false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_image_center:
                if (tabOldIndex == 2 && !publishViewIsShow) {
                    //创建ListView从Visible到Gone的动画
                    ObjectAnimator visibleToInVisable = ObjectAnimator.ofFloat(main_image_center, "rotation", 0.0f, -135.0f);
                    //设置插值器
                    visibleToInVisable.setInterpolator(new AccelerateInterpolator());
                    visibleToInVisable.setDuration(200);
                    visibleToInVisable.start();
                    publishViewIsShow = true;
                    setupCouponSupportWindow();
                } else {
                    //创建ListView从Visible到Gone的动画
                    ObjectAnimator visibleToInVisable = ObjectAnimator.ofFloat(main_image_center, "rotation", 0.0f, 0.0f);
                    //设置插值器
                    visibleToInVisable.setInterpolator(new AccelerateInterpolator());
                    visibleToInVisable.setDuration(200);
                    visibleToInVisable.start();
                    publishViewIsShow = false;
                    mTabHost.setCurrentTab(2);
                    tabOldIndex = 2;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRedPointerResponse(RespDto response) {
        MessageCenterPush result = (MessageCenterPush) response.getData();
        int upPushCount = 0;
        upPushCount += result.getSystemCount();
        upPushCount += result.getFollowCount();
        upPushCount += result.getWallCount();
        if (upPushCount > 0 || PrefsUtils.getInt(this, PUSH_RED, 0) + PrefsUtils.getInt(this, IM_RED, 0) > 0) {
            messageRedPointer.setVisibility(View.VISIBLE);
        } else {
            messageRedPointer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRedPointerFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @Override
    public void onUpdateResponse(RespDto<UpdateResp> response) {
        Log.i("zune:", "response = " + response);
    }

    @Override
    public void onUpdateFailure(String msg) {
        Log.i("zune:", "onUpdateFailuremsg = " + msg);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
//        Glide.with(mContext)
//                .load(R.drawable.profilephoto_small)
//                .dontAnimate()
//                .into(mainUserAvatar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserUpdateEvent(UserUpdateEvent event) {
//        Glide.with(mContext)
//                .load(event.getUri())
//                .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
//                .skipMemoryCache(true)
//                .placeholder(R.drawable.profilephoto_small)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .dontAnimate()
//                .into(mainUserAvatar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLoginEvent(UserLoginEvent event) {
//        if (!TextUtils.isEmpty(event.getUserAvatar()))
//            Glide.with(mContext)
//                    .load(event.getUserAvatar())
//                    .transform(new GlideCircleTransform(mContext, 2, 0xF0F0F0))
//                    .dontAnimate()
//                    .placeholder(R.drawable.profilephoto_small)
//                    .into(mainUserAvatar);
//        else
//            mainUserAvatar.setImageResource(R.drawable.profilephoto_small);
    }

    @Override
    public void onBackPressed() {
        List<PopupWindow> popupWindows = MainApp.getInstance().totalPops.get(tabOldIndex);
        if (popupWindows != null) {
            for (int i = 0; i < popupWindows.size(); i++) {
                if (popupWindows.get(i) != null && popupWindows.get(i).isShowing()) {
                    if (popupWindows.get(i) == mPublishWindow) {
                        closeCouponPopuWindow();
                    } else {
                        popupWindows.get(i).dismiss();
                    }
                    return;
                }
            }
        }
        backCount++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backCount = 0;
            }
        }, 2000);
        if (backCount == 2) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按退出", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCouponSupportWindow() {

        int width = ScreenUtil.getScreenWidth(this);
        int height = ScreenUtil.getScreenHeight(this);
        if (null == mPublishWindow) {
            mPublishView = LayoutInflater.from(this).inflate(
                    R.layout.super_main_publish_window, null);

            LinearLayout closeView = (LinearLayout) mPublishView.findViewById(R.id.closeview);
            ImageView centerBottom = (ImageView) mPublishView.findViewById(R.id.center_bottom);
            LinearLayout publishSeatBtn = (LinearLayout) mPublishView.findViewById(R.id.publish_seat_btn);
            LinearLayout publishChannelBtn = (LinearLayout) mPublishView.findViewById(R.id.publish_channel_btn);

            //创建ListView从Visible到Gone的动画
            ObjectAnimator visibleToInVisable = ObjectAnimator.ofFloat(centerBottom, "rotation", 0.0f, -135.0f);
            //设置插值器
            visibleToInVisable.setInterpolator(new AccelerateInterpolator());
            visibleToInVisable.setDuration(200);
            visibleToInVisable.start();

            int widgetPx = getTabWidgetHeight();
            if (widgetPx > ScreenUtil.dp2px(this, 60)) {
                centerBottom.getLayoutParams().width = widgetPx;
                centerBottom.getLayoutParams().height = widgetPx;
            }

            closeView.setOnClickListener(closeWindowListener);
            publishSeatBtn.setOnClickListener(publishSeatClick);
            publishChannelBtn.setOnClickListener(publishChannelClick);
            centerBottom.setOnClickListener(closeWindowListener);

            //设置弹出部分和面积大小
            mPublishWindow = new PopupWindow(mPublishView, width, height, true);
            mPublishWindow.setClippingEnabled(false);
            //设置动画弹出效果
            mPublishWindow.setAnimationStyle(R.style.PopupAnimation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mPublishWindow.setBackgroundDrawable(dw);
            mPublishWindow.setFocusable(false);
            mPublishWindow.setTouchable(true);
        }
        mPublishWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY,
                0, 0);
        MainApp.getInstance().spacePops.add(mPublishWindow);

    }

    /**
     * 获取状态通知栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public int getTabWidgetHeight() {
        final TabWidget tabWidget = mTabHost.getTabWidget();
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tabWidget.measure(spec, spec);
        int h = tabWidget.getMeasuredHeight();
        return h;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
                if (adapters != null && adapters.get("0") != null) {
                    SignInteractSquareAdapter adapter = adapters.get("0");
                    adapter.refreshAdpater(photos);
                }
            }
        }
    }


    private void closeCouponPopuWindow() {
        if (mPublishWindow != null && mPublishWindow.isShowing()) {
            //创建ListView从Visible到Gone的动画
            ObjectAnimator visibleToInVisable = ObjectAnimator.ofFloat(main_image_center, "rotation", 0.0f, 0.0f);
            //设置插值器
            visibleToInVisable.setInterpolator(new AccelerateInterpolator());
            visibleToInVisable.setDuration(200);
            visibleToInVisable.start();
            publishViewIsShow = false;
            if (MainApp.getInstance().spacePops != null
                    && MainApp.getInstance().spacePops.contains(mPublishWindow))
                MainApp.getInstance().spacePops.remove(mPublishWindow);
            mPublishWindow.dismiss();
            mPublishWindow = null;
        }
    }
}
