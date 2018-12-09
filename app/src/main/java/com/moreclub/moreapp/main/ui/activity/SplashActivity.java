package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.main.ui.adapter.WelcomeAdapter;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.util.MoreUser;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_LOADED;

/**
 * Created by Administrator on 2017/3/7.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.splash_bg)
    ImageView imageView;

    @BindView(R.id.splash_title)
    TextView tvTitle;

    @BindView(R.id.splash_company_title)
    TextView tcCompanyTtitle;

    @BindView(R.id.splash_start)
    TextView splashStart;

    @BindView(R.id.splash_banner)
    Banner splashBanner;

    @BindView(R.id.vp_welcome)
    ViewPager vpWelcome;

    @BindView(R.id.welcome_indicator)
    View indicator;

    @BindView(R.id.ll_indicators)
    LinearLayout llIndicators;

    private ArrayList<View> views;
    Handler handler = new Handler();

    ViewPager.OnPageChangeListener vpListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            float translationX = ScreenUtil.dp2px(SplashActivity.this, 24) * (positionOffset + position);
            indicator.setTranslationX(translationX);
            if (position == views.size() - 1) {
                splashStart.setVisibility(View.VISIBLE);
                llIndicators.setVisibility(View.GONE);
                indicator.setVisibility(View.GONE);
            } else {
                splashStart.setVisibility(View.GONE);
                llIndicators.setVisibility(View.VISIBLE);
                indicator.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.splash_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        setupViews();
    }

    private void setupViews() {
        startFlash();
        splashStart.setVisibility(View.GONE);
        indicator.setVisibility(View.GONE);
    }

    private void startWelcome() {
        initPictures();
        PagerAdapter adapter = new WelcomeAdapter(views);
        vpWelcome.addOnPageChangeListener(vpListener);
        vpWelcome.setAdapter(adapter);
    }

    private boolean checkVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return PrefsUtils.getBoolean(this, KEY_SPLASH_LOADED, false);
//        return true;
    }


    private void initPictures() {
        indicator.setVisibility(View.VISIBLE);
        views = new ArrayList<>();
        int[] lls = {R.drawable.splash1, R.drawable.splash2, R.drawable.splash3, R.drawable.splash4 ,R.drawable.splash5};
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageDrawable(getResources().getDrawable(lls[i]));
            views.add(imageView);
        }
        for (int i = 0; i < 5; i++) {
            View smallCircle = new View(this);
            smallCircle.setBackgroundResource(R.drawable.indicater_normal_circle);
            int sp = ScreenUtil.dp2px(this, 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sp, sp);
            layoutParams.setMargins(sp, sp, sp, sp);
            smallCircle.setLayoutParams(layoutParams);
            llIndicators.addView(smallCircle);
        }
    }

    private void startFlash() {
        MoreUser.getInstance().setUid(PrefsUtils.getLong(this, Constants.KEY_UID, 0));
        MoreUser.getInstance().setAccess_token(PrefsUtils.getString(this, Constants.KEY_ACCESS_TOKEN, ""));
        MoreUser.getInstance().setRefresh_token(PrefsUtils.getString(this, Constants.KEY_REFRESH_TOKEN, ""));
        MoreUser.getInstance().setThdThumb(PrefsUtils.getString(this, Constants.KEY_THIRD_AVATAR, ""));
        MoreUser.getInstance().setThumb(PrefsUtils.getString(this, Constants.KEY_AVATAR, ""));
        //MoreUser.getInstance().setExpires_in(item.getExpires_in());
        MoreUser.getInstance().setNickname(PrefsUtils.getString(this, Constants.KEY_NICKNAME, ""));
        MoreUser.getInstance().setBirthday(PrefsUtils.getString(this, Constants.KEY_BIRTHDAY, ""));
        MoreUser.getInstance().setSex(PrefsUtils.getInt(this, Constants.KEY_GENDER, -1));
        //MoreUser.getInstance().setScope(item.getScope());
        //MoreUser.getInstance().setRegistTime(item.getRegistTime());
        MoreUser.getInstance().setFellowerCount(PrefsUtils.getInt(this, Constants.KEY_UCENTER_FELLOWER, 0));
        MoreUser.getInstance().setFollowCount(PrefsUtils.getInt(this, Constants.KEY_UCENTER_FOLLOW, 0));
        if (!checkVersion()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startWelcome();
                }
            }, 1000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            SplashActivity.this, R.anim.fade_in, R.anim.fade_out);
                    Intent intent = new Intent(SplashActivity.this, SuperMainActivity.class);
                    intent.putExtra("isClickTo", false);
                    ActivityCompat.startActivity(SplashActivity.this, intent, compat.toBundle());
                    finish();
                }
            }, 1000);
        }
    }


    @OnClick(R.id.splash_start)
    public void onViewClicked() {
        PrefsUtils.setBoolean(this, KEY_SPLASH_LOADED, true);
//        startFlash();
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(this, SelectCityActivity.class);
        intent.putExtra("FROM_SPLASH", true);
        ActivityCompat.startActivity(this, intent, compat.toBundle());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }
}
