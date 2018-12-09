package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.view.BubbleTextView;
import com.moreclub.common.ui.view.scrollview.ObservableScrollView;
import com.moreclub.common.ui.view.scrollview.ObservableScrollViewCallbacks;
import com.moreclub.common.ui.view.scrollview.ScrollState;
import com.moreclub.common.ui.view.scrollview.ScrollUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.ui.activity.MessageFollowListActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserCenter;
import com.moreclub.moreapp.ucenter.model.UserCouponResult;
import com.moreclub.moreapp.ucenter.model.event.UserBindPhoneEvent;
import com.moreclub.moreapp.ucenter.model.event.UserUpdateEvent;
import com.moreclub.moreapp.ucenter.presenter.IUserCenterLoader;
import com.moreclub.moreapp.ucenter.presenter.UserCenterLoader;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.ShareHandler;
import com.moreclub.moreapp.util.ShareUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/3/13.
 */

public class UserCenterActivity extends BaseActivity implements ObservableScrollViewCallbacks,
        IUserCenterLoader.LoaderUserCenterDataBinder {
    private final static int ORANGE_CARD_LIMIT = 999;
    private final static int BLACK_CARD_LIMIT = 9999;
    private final static int ORANGE_CARD_LIMIT_MIN = 333;
    private final static int BLACK_CARD_LIMIT_MIN = 3333;
    private final static int ORANGE_CARD_LIMIT_MAX = 666;
    private final static int BLACK_CARD_LIMIT_MAX = 6666;

    private final static int MSG_MILEAGE = 0x01;
    private final static int MSG_MAX_STEP = 30;

    @BindString(R.string.ucenter_title)
    String title;
    @BindString(R.string.ucenter_normal_member)
    String normalMemberText;
    @BindString(R.string.ucenter_orange_member)
    String orangeMemberText;
    @BindString(R.string.ucenter_black_member)
    String blackMemberText;
    @BindString(R.string.ucenter_orange_discount)
    String orangeDicountText;
    @BindString(R.string.ucenter_black_discount)
    String blackDicountText;


    @BindDimen(R.dimen.ucenter_avatar_boder_width)
    int borderWidth;
    @BindDimen(R.dimen.ucenter_username_scroll_height)
    int userNameTranDp;
    @BindDimen(R.dimen.ucenter_username_scroll_x)
    int userNameTranX;
    @BindDimen(R.dimen.ucenter_fan_scroll_x)
    int fanTranX;

    @BindDimen(R.dimen.ucenter_orange_scroll_x)
    int orangeScrollX;
    @BindDimen(R.dimen.ucenter_fan_scroll_height)
    int fanScrollY;

    @BindView(R.id.ucenter_toolbar)
    LinearLayout toolbar;
    @BindView(R.id.ucenter_avatar)
    CircleImageView avatar;
    @BindView(R.id.ucenter_username)
    TextView tvUserName;
    @BindView(R.id.ucenterScrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.ucenter_hs)
    HorizontalScrollView hScrollView;

    @BindView(R.id.ucenter_fan_layout)
    LinearLayout fanLayout;
    @BindView(R.id.ucenter_card_discount_layout)
    LinearLayout discountLayout;
    @BindView(R.id.ucenter_playmore_layout)
    LinearLayout playMoreLayout;
    @BindView(R.id.ucenter_day_layout)
    LinearLayout dayLayout;
    @BindView(R.id.ucenter_left_ball)
    ImageView leftBall;
    @BindView(R.id.ucenter_right_ball)
    ImageView rightBall;
    @BindView(R.id.ucenter_orange_lock)
    ImageView orangeLock;
    @BindView(R.id.ucenter_black_lock)
    ImageView blackLock;
    @BindView(R.id.ucenter_scroll_point)
    ImageView scrollPoint;
    @BindView(R.id.ucenter_normal_point)
    ImageView normalPoint;

    @BindView(R.id.ucenter_orange_lock_text)
    TextView orangeLockText;
    @BindView(R.id.ucenter_black_lock_text)
    TextView blackLockText;
    @BindView(R.id.ucenter_normal_text)
    TextView normalText;
    @BindView(R.id.ucenter_orange_text)
    TextView orangeText;
    @BindView(R.id.ucenter_black_text)
    TextView blackText;
    @BindView(R.id.ucenter_reg_day)
    TextView tvRegDay;
    @BindView(R.id.ucenter_user_mileage)
    TextView tvMileAge;
    @BindView(R.id.ucenter_mileage_info)
    BubbleTextView tvMileAgeInfo;
    @BindView(R.id.ucenter_follower_count)
    TextView tvFollowerCount;
    @BindView(R.id.ucenter_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.ucenter_discount_text)
    TextView tvDiscount;

    @BindView(R.id.ucenter_normal_progress)
    ProgressBar normalProgress;
    @BindView(R.id.ucenter_orange_progress)
    ProgressBar orangeProgress;
    @BindView(R.id.ucenter_black_progress)
    ProgressBar blackProgress;
    @BindView(R.id.ucenter_lock_top_progress)
    ProgressBar topProgress;
    @BindView(R.id.ucenter_point_line)
    ImageView lineView;
    @BindView(R.id.user_center_coupon_red)
    TextView couponRed;

    @BindView(R.id.share_layout)
    LinearLayout shareLayout;
    @BindView(R.id.couponLay)
    LinearLayout couponLay;
    @BindView(R.id.coupon_name)
    TextView couponName;
    @BindView(R.id.coupon_count)
    TextView couponCount;
    @BindView(R.id.check_layout)
    LinearLayout checkLayout;
    @BindView(R.id.check_line)
    TextView checkLine;
    @BindColor(R.color.black)
    int baseColor;
    @BindColor(R.color.commonThinTextColor)
    int borderColor;
    @BindColor(R.color.white)
    int titleColor;
    @BindColor(R.color.orange)
    int orangeColor;

    private int toolbarHeight;
    private TextView tvTitle;

    @BindDimen(R.dimen.ucenter_scroll_height)
    int totalTranY;

    @BindDimen(R.dimen.ucenter_avatar_scroll_height)
    int avatarTranY;
    private int userNameTranY;
    private int totalTranX;

    private int nameWidth;
    private int fanWidth;
    private boolean firstEnter;
    private Boolean noReadCoupon;
    private UserCenter userInfo;

    @Override
    protected int getLayoutResource() {
        return R.layout.ucenter_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        initData();
        setupViews();
    }

    private void initData() {
        noReadCoupon = PrefsUtils.getBoolean(this, Constants.KEY_NO_READ_COUPON, false);
        loadData();
    }

    private void loadData() {
        ((UserCenterLoader) mPresenter).loadUserCenter("" + MoreUser.getInstance().getUid());
        ((UserCenterLoader) mPresenter).loadUserCoupon("" + MoreUser.getInstance().getUid());
    }

    @Override
    protected Class getLogicClazz() {
        return IUserCenterLoader.class;
    }

    private void setupViews() {

        if (noReadCoupon) {
            couponRed.setVisibility(View.VISIBLE);
        } else {
            couponRed.setVisibility(View.GONE);
        }

        firstEnter = PrefsUtils.getBoolean(this, Constants.KEY_UCENTER_FIRST, true);
        toolbarHeight = ScreenUtil.dp2px(this, 28);
        //avatarTranY = ScreenUtil.dp2px(this, (317-110-32));
        totalTranX = ScreenUtil.getScreenWidth(this) / 2 - ScreenUtil.dp2px(this, 56);

        Logger.d("===userNameTranDp==" + userNameTranDp);

        userNameTranY = userNameTranDp;

        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        tvTitle = ButterKnife.findById(toolbar, R.id.activity_title);
        tvTitle.setText(title);
        scrollView.setScrollViewCallbacks(this);
        //scrollView.setVisibility(View.INVISIBLE);

        ImageButton ivSetting = ButterKnife.findById(toolbar, R.id.nav_right_btn);
        ivSetting.setVisibility(View.VISIBLE);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        UserCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserCenterActivity.this, SettingActivity.class);
                ActivityCompat.startActivity(UserCenterActivity.this, intent, compat.toBundle());
                if (!MoreUser.getInstance().getToken_type().equals(Constants.TOKEN_MOBILE) && userInfo != null && userInfo.getPhone() != null)
                    EventBus.getDefault().post(new UserBindPhoneEvent(userInfo.getPhone()));
            }
        });

        ImageView ivBack = ButterKnife.findById(toolbar, R.id.nav_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        couponLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = UserCenterActivity.this;
                String str = "ucenter_coupon_click";
                Map<String, String> map = new HashMap<>();
                map.put("uid", MoreUser.getInstance().getUid() + "");
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                AppUtils.pushLeftActivity(UserCenterActivity.this, UserCouponListActivity.class);
            }
        });

        tvUserName.setText(MoreUser.getInstance().getNickname());

        final String avatarUrl = TextUtils.isEmpty(PrefsUtils.getString(this, Constants.KEY_AVATAR, "")) ?
                PrefsUtils.getString(this, Constants.KEY_THIRD_AVATAR, "") : PrefsUtils.getString(this, Constants.KEY_AVATAR, "");
        if (!TextUtils.isEmpty(avatarUrl))
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.profilephoto)
                    .dontAnimate()
                    .into(avatar);
        else
            avatar.setImageResource(R.drawable.profilephoto);

        leftBall.setAlpha(0.0f);
        rightBall.setAlpha(0.0f);
        orangeLockText.setAlpha(0.0f);
        blackLockText.setAlpha(0.0f);
        topProgress.setAlpha(0.0f);
        tvMileAgeInfo.setAlpha(0.0f);
        scrollPoint.setAlpha(0.0f);
        //blackLock.setAlpha(0.0f);

        normalProgress.setMax(ORANGE_CARD_LIMIT);
        orangeProgress.setMax(BLACK_CARD_LIMIT);
        blackProgress.setMax(BLACK_CARD_LIMIT);

        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tvUserName.measure(spec, spec);
        nameWidth = tvUserName.getMeasuredWidth();

        tvFollowerCount.setText("粉丝 " + MoreUser.getInstance().getFellowerCount());
        tvFollowCount.setText("关注 " + MoreUser.getInstance().getFollowCount());

        fanLayout.measure(spec, spec);
        fanWidth = fanLayout.getMeasuredWidth();

        if (firstEnter) {
            PrefsUtils.setBoolean(this, Constants.KEY_UCENTER_FIRST, false);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollBy(0, totalTranY);
                }
            }, 50);
        }

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = UserCenterActivity.this;
                String str = "ucenter_shareapp_click";
                Map<String, String> map = new HashMap<>();
                map.put("uid", MoreUser.getInstance().getUid() + "");
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }
                /**
                 * @param context
                 * @param shareType 0-朋友圈，微博,1-微信好友
                 * @param channel   0-app,1-微信,2,微博,3-qq
                 * @param module    0-活动,1-资讯,2-app,3-商家,4-套餐,5-拼座,6-黑卡,7-橙卡
                 * @param moduleID
                 */
                ShareHandler shareHandler = new ShareHandler(UserCenterActivity.this, 2, "0");
                shareHandler.showShareView();
                shareHandler.shareTitle.setText("分享1次／天可得20里程，每日坚持，累计可得500里程，积累里程升级身份，享特权。");
                float textWidth = getTextWidth(shareHandler.shareTitle
                        , "分享1次／天可得20里程，每日坚持，累计可得500里程，积累里程升级身份，享特权。");
                if (textWidth + ScreenUtil.dp2px(UserCenterActivity.this, 32)
                        > ScreenUtil.getScreenWidth(UserCenterActivity.this)) {
                    shareHandler.shareTitle.setGravity(View.SCROLL_INDICATOR_START);
                }
                shareHandler.setOnShareCallBack(new ShareUtils.IShareCallBackLinstener() {
                    @Override
                    public void shareComlete(boolean sucess) {
                        ((UserCenterLoader) mPresenter).onShareCallBack("" + MoreUser.getInstance().getUid());

                    }
                });
            }
        });
    }

    @Override
    public void onShareCallBackFailure(String msg) {

    }

    @Override
    public void onShareCallBackResponse(RespDto response) {

    }

    @OnClick(R.id.ucenter_follower_count)
    void onClickFollowerCount() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                UserCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(UserCenterActivity.this, MessageFollowListActivity.class);
        intent.putExtra("followType", 0);
        ActivityCompat.startActivity(UserCenterActivity.this, intent, compat.toBundle());
    }

    @OnClick(R.id.ucenter_follow_count)
    void onClickFollowCount() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                UserCenterActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(UserCenterActivity.this, MessageFollowListActivity.class);
        intent.putExtra("followType", 1);
        ActivityCompat.startActivity(UserCenterActivity.this, intent, compat.toBundle());
    }

    @OnClick(R.id.ucenter_playmore)
    void onClickPlayMore() {
        Context context = UserCenterActivity.this;
        String str = "ucenter_playmore_click";
        Map<String, String> map = new HashMap<>();
        map.put("uid", MoreUser.getInstance().getUid() + "");
        if (Build.VERSION.SDK_INT > 22) {
            int du = 2000000000;
            MobclickAgent.onEventValue(context, str, map, du);
        } else {
            MobclickAgent.onEvent(context, str, map);
        }
        AppUtils.pushLeftActivity(this, PlayMoreActivity.class);
    }

    @OnClick(R.id.ucenter_avatar)
    void onClickModifyUserInfo() {
        AppUtils.pushLeftActivity(this, UserModifyDetailActivity.class);
//        AppUtils.pushLeftActivity(this, UserModifyActivity.class);
    }

    @OnClick(R.id.ucenter_order_btn_layout)
    void onClickMyOrderLayout() {
        Context context = UserCenterActivity.this;
        String str = "ucenter_order_click";
        Map<String, String> map = new HashMap<>();
        map.put("uid", MoreUser.getInstance().getUid() + "");
        if (Build.VERSION.SDK_INT > 22) {
            int du = 2000000000;
            MobclickAgent.onEventValue(context, str, map, du);
        } else {
            MobclickAgent.onEvent(context, str, map);
        }
        AppUtils.pushLeftActivity(this, MyOrderActivity.class);
    }

    @OnClick(R.id.ucenter_collect_btn_layout)
    void onClickMyCollectLayout() {
        Context context = UserCenterActivity.this;
        String str = "ucenter_user_like";
        Map<String, String> map = new HashMap<>();
        map.put("uid", MoreUser.getInstance().getUid() + "");
        if (Build.VERSION.SDK_INT > 22) {
            int du = 2000000000;
            MobclickAgent.onEventValue(context, str, map, du);
        } else {
            MobclickAgent.onEvent(context, str, map);
        }
        AppUtils.pushLeftActivity(this, UserCollectionActivity.class);
    }

    @OnClick({R.id.ucenter_privilege, R.id.ucenter_card_discount_layout})
    void onClickMyPrivilegeLayout() {
        Context context = UserCenterActivity.this;
        String str = "ucenter_privilege_click";
        Map<String, String> map = new HashMap<>();
        map.put("uid", MoreUser.getInstance().getUid() + "");
        if (Build.VERSION.SDK_INT > 22) {
            int du = 2000000000;
            MobclickAgent.onEventValue(context, str, map, du);
        } else {
            MobclickAgent.onEvent(context, str, map);
        }
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(this, UserPrivilegeActivity.class);
        if (userInfo != null) {
            intent.putExtra("cardLevel", userInfo.getCardLevel());
            intent.putExtra("curMiles", userInfo.getCurMiles());
            intent.putExtra("totalMiles", userInfo.getCardLimit());
            intent.putExtra("cardType", userInfo.getCardLevel());
        } else {
            intent.putExtra("cardLevel", 0);
            intent.putExtra("curMiles", 0);
            intent.putExtra("totalMiles", 0);
            intent.putExtra("cardType", 0);
            Toast.makeText(this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.startActivity(this, intent, compat.toBundle());
    }

    @OnClick(R.id.check_layout)
    void onClickCheckLayout() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(this, PackageCheckActivity.class);
        if (userInfo != null)
            intent.putExtra("mid", userInfo.getMerchantId());
        ActivityCompat.startActivity(this, intent, compat.toBundle());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
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

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float alpha = Math.min(1, (float) scrollY / totalTranY);
        //banner.setTranslationY((scrollY - toolbarHeight) / 2);
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        tvTitle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, titleColor));

        Logger.d("--scrollY---" + scrollY + "---totalTranY" + totalTranY);
        float scaleRate = (float) scrollY / totalTranY;
        Logger.d("--scaleRate---" + scaleRate);
        if (scaleRate >= 1.0) {
            scaleRate = 1.0f;
        }

        discountLayout.setAlpha(1 - scaleRate);
        dayLayout.setAlpha(1 - scaleRate);
        orangeLock.setAlpha(1 - scaleRate);
        lineView.setAlpha(1 - scaleRate);
        normalText.setAlpha(1 - scaleRate);
        orangeText.setAlpha(1 - scaleRate);
        blackText.setAlpha(1 - scaleRate);
        normalProgress.setAlpha(1 - scaleRate);
        orangeProgress.setAlpha(1 - scaleRate);
        blackProgress.setAlpha(1 - scaleRate);
        blackLock.setAlpha(1 - scaleRate);
        normalPoint.setAlpha(1 - scaleRate);

        leftBall.setAlpha(scaleRate);
        rightBall.setAlpha(scaleRate);
        orangeLockText.setAlpha(scaleRate);
        blackLockText.setAlpha(scaleRate);
        topProgress.setAlpha(scaleRate);
        tvMileAgeInfo.setAlpha(scaleRate);
        scrollPoint.setAlpha(scaleRate);

        avatar.setScaleX(1 - scaleRate * (7f / 15));
        avatar.setScaleY(1 - scaleRate * (7f / 15));

        avatar.setTranslationY(avatarTranY * scaleRate);
        avatar.setTranslationX(-totalTranX * scaleRate);
        tvUserName.setTranslationY(userNameTranDp * scaleRate);
        tvUserName.setTranslationX(-(userNameTranX - nameWidth / 2) * scaleRate);
        fanLayout.setTranslationY(fanScrollY * scaleRate);
        fanLayout.setTranslationX(-(fanTranX - fanWidth / 2) * scaleRate);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserUpdateEvent(UserUpdateEvent event) {
        tvUserName.setText(MoreUser.getInstance().getNickname());
        Glide.with(this)
                .load(event.getUri())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.transform(new GlideCircleTransform(this, 2, 0xF0F0F0))
                .dontAnimate()
                .into(avatar);
    }

    @Override
    public void onUserCenterResponse(RespDto response) {
        userInfo = (UserCenter) response.getData();
        if (userInfo.getMerchantId() != null && userInfo.getMerchantId().length() > 0) {
            checkLayout.setVisibility(View.VISIBLE);
            checkLine.setVisibility(View.VISIBLE);
        } else {
            checkLayout.setVisibility(View.GONE);
            checkLine.setVisibility(View.GONE);
        }
        setupStickViews();
        setupHScrollView();
        setupMileAge();

        MoreUser.getInstance().setCardLevel(userInfo.getCardLevel());
    }

    @Override
    public void onUserCenterFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupHScrollView() {
        //scrollView.setVisibility(View.VISIBLE);

        if (userInfo != null) {

            if (userInfo.getCardLevel() == Constants.CARD_TYPE_NORMAL) {

            } else if (userInfo.getCardLevel() == Constants.CARD_TYPE_ORANGE) {
                hScrollView.scrollBy(orangeScrollX, 0);
            } else if (userInfo.getCardLevel() == Constants.CARD_TYPE_BLACK) {
                hScrollView.scrollBy(orangeScrollX * 2 + ScreenUtil.dp2px(this, 30), 0);
            }
        }

        Logger.d("===playmore: " + playMoreLayout.getWidth() + ", " + playMoreLayout.getHeight());
    }

    private void setupMileAge() {
        if (userInfo != null) {
            tvRegDay.setText(userInfo.getRegDays() + "");
            tvMileAge.setText(userInfo.getCurMiles() + "");

            boolean first = true;
            if (first) {
                //blackLock.setAlpha(1.0f);

                if (userInfo.getCurMiles() <= ORANGE_CARD_LIMIT) {
                    normalProgress.setProgress(userInfo.getCurMiles());
                } else if (userInfo.getCurMiles() <= BLACK_CARD_LIMIT) {
                    normalProgress.setProgress(ORANGE_CARD_LIMIT);
                    orangeProgress.setProgress(userInfo.getCurMiles());
                } else {
                    normalProgress.setProgress(ORANGE_CARD_LIMIT);
                    orangeProgress.setProgress(BLACK_CARD_LIMIT);
                    blackProgress.setProgress(BLACK_CARD_LIMIT);
                }
            } else {

            }

            setupTopProgress();

            if (userInfo.getCardLevel() == Constants.CARD_TYPE_NORMAL) {
            } else if (userInfo.getCardLevel() == Constants.CARD_TYPE_ORANGE) {
                normalPoint.setVisibility(View.VISIBLE);
                orangeLock.setVisibility(View.INVISIBLE);
                orangeText.setTextColor(orangeColor);

                dayLayout.setTranslationX(orangeScrollX);
                discountLayout.setTranslationX(orangeScrollX);
                lineView.setTranslationX(orangeScrollX);
            } else if (userInfo.getCardLevel() == Constants.CARD_TYPE_BLACK) {
                normalPoint.setVisibility(View.VISIBLE);
                blackLock.setVisibility(View.INVISIBLE);
                orangeLock.setImageResource(R.drawable.ucenter_orange_ball);
                orangeText.setTextColor(orangeColor);
                blackText.setTextColor(orangeColor);

                int scrollX = orangeScrollX * 2 + ScreenUtil.dp2px(this, 30);
                dayLayout.setTranslationX(scrollX);
                discountLayout.setTranslationX(scrollX);
                lineView.setTranslationX(scrollX);
            }

            dayLayout.setVisibility(View.VISIBLE);
            discountLayout.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            scrollPoint.setVisibility(View.VISIBLE);
            tvMileAgeInfo.setVisibility(View.VISIBLE);
        }
    }

    private void setupTopProgress() {
        if (userInfo.getCurMiles() <= ORANGE_CARD_LIMIT) {
            topProgress.setMax(ORANGE_CARD_LIMIT);
            int progress = userInfo.getCurMiles() < ORANGE_CARD_LIMIT_MIN
                    ? ORANGE_CARD_LIMIT_MIN : userInfo.getCurMiles();
            topProgress.setProgress(progress);

            if (progress >= ORANGE_CARD_LIMIT_MAX) {
                progress = ORANGE_CARD_LIMIT_MAX;
            }

            int tranX = ScreenUtil.getScreenWidth(this) * progress / ORANGE_CARD_LIMIT;
            scrollPoint.setTranslationX(tranX);

            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            tvMileAgeInfo.measure(spec, spec);
            int measuredWidthTicketNum = tvMileAgeInfo.getMeasuredWidth();

            tvMileAgeInfo.setTranslationX(tranX - measuredWidthTicketNum / 2 + ScreenUtil.dp2px(this, 6));
        } else if (userInfo.getCurMiles() < BLACK_CARD_LIMIT) {
            topProgress.setMax(BLACK_CARD_LIMIT);
            int progress = userInfo.getCurMiles() <= BLACK_CARD_LIMIT_MIN
                    ? BLACK_CARD_LIMIT_MIN : userInfo.getCurMiles();
            topProgress.setProgress(progress);

            if (progress >= BLACK_CARD_LIMIT_MAX) {
                progress = BLACK_CARD_LIMIT_MAX;
            }

            int tranX = ScreenUtil.getScreenWidth(this) * progress / BLACK_CARD_LIMIT;
            scrollPoint.setTranslationX(tranX);

            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            tvMileAgeInfo.measure(spec, spec);
            int measuredWidthTicketNum = tvMileAgeInfo.getMeasuredWidth();

            tvMileAgeInfo.setTranslationX(tranX - measuredWidthTicketNum / 2 + ScreenUtil.dp2px(this, 6));
        } else {
            rightBall.setVisibility(View.GONE);
            topProgress.setMax(BLACK_CARD_LIMIT);
            topProgress.setProgress(BLACK_CARD_LIMIT);

            int tranX = ScreenUtil.getScreenWidth(this) * BLACK_CARD_LIMIT_MIN / BLACK_CARD_LIMIT;
            int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            tvMileAgeInfo.measure(spec, spec);
            int measuredWidthTicketNum = tvMileAgeInfo.getMeasuredWidth();

            scrollPoint.setTranslationX(tranX);
            tvMileAgeInfo.setTranslationX(tranX - measuredWidthTicketNum / 2 + ScreenUtil.dp2px(this, 6));
        }
    }

    private void setupStickViews() {
        if (userInfo != null) {
            if (userInfo.getCardLevel() == Constants.CARD_TYPE_NORMAL) {
                orangeLockText.setText(normalMemberText);
                blackLockText.setText(orangeMemberText);
                tvDiscount.setText(orangeDicountText);
            } else if (userInfo.getCardLevel() == Constants.CARD_TYPE_ORANGE) {
                orangeLockText.setText(orangeMemberText);
                blackLockText.setText(blackMemberText);
                tvDiscount.setText(orangeDicountText);
            } else if (userInfo.getCardLevel() == Constants.CARD_TYPE_BLACK) {
                orangeLockText.setText(blackMemberText);
                blackLockText.setVisibility(View.INVISIBLE);
                tvDiscount.setText(blackDicountText);
            }


            PrefsUtils.setInt(this, Constants.KEY_UCENTER_FELLOWER, userInfo.getFollower());
            PrefsUtils.setInt(this, Constants.KEY_UCENTER_FOLLOW, userInfo.getFollow());
            MoreUser.getInstance().setFellowerCount(userInfo.getFollower());
            MoreUser.getInstance().setFollowCount(userInfo.getFollow());

            if (userInfo.getCurMiles() == 0) {
                tvMileAgeInfo.setText("第" + userInfo.getRegDays() + "天，" + userInfo.getCurMiles() + "里程");
                return;
            }

            final Handler mileAgeHanlder = new Handler() {
                private int msgValue = 0;

                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_MILEAGE:
                            Logger.d("---msg.arg1---" + msg.arg1);
                            tvMileAgeInfo.setText("第" + userInfo.getRegDays() + "天，" + msg.arg1 + "里程");
                            break;
                    }
                }
            };

            //userInfo.setCurMiles(999); for test

            final int len = String.valueOf(userInfo.getCurMiles()).length();
            switch (len) {
                case 1:
                    sendMileageMsg(mileAgeHanlder, 1);
                    break;
                case 2:
                    sendMileageMsg(mileAgeHanlder, 10);
                    break;
                case 3:
                    sendMileageMsg(mileAgeHanlder, 100);
                    break;
                case 4:
                    sendMileageMsg(mileAgeHanlder, 1000);
                    break;
                case 5:
                    sendMileageMsg(mileAgeHanlder, 10000);
                    break;
                case 6:
                    sendMileageMsg(mileAgeHanlder, 100000);
                    break;
            }

            tvFollowerCount.setText("粉丝 " + userInfo.getFollower());
            tvFollowCount.setText("关注 " + userInfo.getFollow());

            //int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            //fanLayout.measure(spec,spec);
            //fanWidth = fanLayout.getMeasuredWidth();
        }
    }

    private void sendMileageMsg(final Handler mileAgeHanlder, int stepValue) {
        int times;
        int value;

        tvMileAgeInfo.setText("第" + userInfo.getRegDays() + "天，" + stepValue + "里程");

        if (stepValue < 10) {
            times = userInfo.getCurMiles();
            stepValue = 1;
            value = 1;
        } else {
            int m = (userInfo.getCurMiles() - stepValue) % MSG_MAX_STEP;
            value = (userInfo.getCurMiles() - stepValue) / MSG_MAX_STEP;

            if (value == 0) {
                times = m;
                //stepValue = 1;
                value = 1;
            } else {
                times = (MSG_MAX_STEP * value + m) / value;
            }
        }

        Logger.d("=######==times== : " + times);

        for (int t = 1; t <= times; t++) {
            Message msg = mileAgeHanlder.obtainMessage();
            msg.what = MSG_MILEAGE;
            msg.arg1 = t == times ? userInfo.getCurMiles() : (stepValue + t * value);
            mileAgeHanlder.sendMessageDelayed(msg, 20 * t);
        }
    }

    @Override
    public void onUserCouponFailure(String msg) {
        couponLay.setVisibility(View.VISIBLE);
        couponCount.setText("0张   ");
    }

    @Override
    public void onUserCouponResponse(RespDto response) {
        UserCouponResult result = (UserCouponResult) response.getData();
        couponLay.setVisibility(View.VISIBLE);
        if (result != null) {
            couponCount.setText(result.getUsable() + "张   ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public float getTextWidth(TextView view, String text) {
        TextPaint paint = view.getPaint();
        if (text == null)
            return -1;
        return paint.measureText(text);
    }
}
