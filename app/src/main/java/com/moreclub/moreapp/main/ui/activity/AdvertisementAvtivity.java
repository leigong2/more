package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.main.model.SplashImage;
import com.moreclub.moreapp.main.presenter.ISplashDataLoader;
import com.moreclub.moreapp.main.presenter.SplashDataLoader;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.ui.activity.PlayMoreActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.information.constant.Constants.KEY_ACT_ID;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_EXPIRE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_IMAGE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_LINK;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_LOADED;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_TITLE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_TYPE;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AdvertisementAvtivity extends BaseActivity implements
        ISplashDataLoader.SplashDataBinder<SplashImage> {
    Handler handler = new Handler();
    @BindView(R.id.advertise_bg)
    ImageView imageView;
    private boolean isClick = false;

    @Override
    protected Class getLogicClazz() {
        return ISplashDataLoader.class;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.advertisement_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupViews();
        loadData();
    }

    private void loadData() {
        ((SplashDataLoader) mPresenter).onLoadSplash(this);
    }

    private void setupViews() {
        long time = System.currentTimeMillis() / 1000;
        long expire = PrefsUtils.getLong(this, KEY_SPLASH_EXPIRE, 0);

        if (!checkVersion() || TextUtils.isEmpty(PrefsUtils.getString(this, KEY_SPLASH_IMAGE, ""))) {
            AppUtils.pushLeftActivity(AdvertisementAvtivity.this, SplashActivity.class);
            finish();
        } else {
            startFlash();
            if (time > expire) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AdvertisementAvtivity.this, R.anim.fade_in, R.anim.fade_out);
                Intent intent = new Intent(AdvertisementAvtivity.this, SuperMainActivity.class);
                intent.putExtra("isClickTo", false);
                ActivityCompat.startActivity(AdvertisementAvtivity.this, intent, compat.toBundle());
                finish();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //animate(imageView);
                        if (!isClick) {
                            Long lastExpire = PrefsUtils.getLong(getApplicationContext(),
                                    KEY_SPLASH_EXPIRE, 0);
                            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                    AdvertisementAvtivity.this, R.anim.fade_in, R.anim.fade_out);
                            Intent intent = new Intent(AdvertisementAvtivity.this, SuperMainActivity.class);
                            intent.putExtra("isClickTo", false);
                            ActivityCompat.startActivity(AdvertisementAvtivity.this, intent, compat.toBundle());
                            finish();
                        }
                    }
                }, 3000);
            }
        }

        String url = PrefsUtils.getString(this, KEY_SPLASH_IMAGE, "");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .dontAnimate()
                    .override(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }
    }

    private void startFlash() {
        MoreUser.getInstance().setUid(PrefsUtils.getLong(this, Constants.KEY_UID, 0));
        MoreUser.getInstance().setAccess_token(PrefsUtils.getString(this, Constants.KEY_ACCESS_TOKEN, ""));
        MoreUser.getInstance().setRefresh_token(PrefsUtils.getString(this, Constants.KEY_REFRESH_TOKEN, ""));
        MoreUser.getInstance().setThdThumb(PrefsUtils.getString(this, Constants.KEY_THIRD_AVATAR, ""));
        MoreUser.getInstance().setThumb(PrefsUtils.getString(this, Constants.KEY_AVATAR, ""));
        MoreUser.getInstance().setNickname(PrefsUtils.getString(this, Constants.KEY_NICKNAME, ""));
        MoreUser.getInstance().setBirthday(PrefsUtils.getString(this, Constants.KEY_BIRTHDAY, ""));
        MoreUser.getInstance().setSex(PrefsUtils.getInt(this, Constants.KEY_GENDER, -1));
        MoreUser.getInstance().setFellowerCount(PrefsUtils.getInt(this, Constants.KEY_UCENTER_FELLOWER, 0));
        MoreUser.getInstance().setFollowCount(PrefsUtils.getInt(this, Constants.KEY_UCENTER_FOLLOW, 0));
    }

    @OnClick(R.id.advertise_bg)
    void onClickImage() {
//        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
//                AdvertisementAvtivity.this, R.anim.fade_in, R.anim.fade_out);
//        Intent intent = new Intent(AdvertisementAvtivity.this, HomeStarActivity.class);
//        intent.putExtra("isClickTo",true);
//        ActivityCompat.startActivity(AdvertisementAvtivity.this, intent, compat.toBundle());
//        finish();
        isClick = true;
        String linkUrl = PrefsUtils.getString(this, KEY_SPLASH_LINK, "");
        String title = PrefsUtils.getString(this, KEY_SPLASH_TITLE, "");
        int clickType = PrefsUtils.getInt(this, KEY_SPLASH_TYPE, 0);
        //1活动，2资讯，3跳内部浏览器，4跳外部浏览器,5发现
        if (clickType == 1) {
            try {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(this, LiveActivity.class);
                intent.putExtra("isFromAdert", true);
                intent.putExtra(KEY_ACT_ID, Integer.valueOf(linkUrl));
                ActivityCompat.startActivity(this, intent, compat.toBundle());
                finish();
            } catch (Exception e) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AdvertisementAvtivity.this, R.anim.fade_in, R.anim.fade_out);
                Intent intent = new Intent(AdvertisementAvtivity.this, SuperMainActivity.class);
                intent.putExtra("isClickTo", false);
                ActivityCompat.startActivity(AdvertisementAvtivity.this, intent, compat.toBundle());
                finish();
            }

        } else if (clickType == 2) {
            try {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(this, MChannelDetailsAcitivy.class);
                intent.putExtra("sid", Integer.valueOf(linkUrl));
                intent.putExtra("isFromAdert", true);
                ActivityCompat.startActivity(this, intent, compat.toBundle());
                finish();
            } catch (Exception e) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        AdvertisementAvtivity.this, R.anim.fade_in, R.anim.fade_out);
                Intent intent = new Intent(AdvertisementAvtivity.this, SuperMainActivity.class);
                intent.putExtra("isClickTo", false);
                ActivityCompat.startActivity(AdvertisementAvtivity.this, intent, compat.toBundle());
                finish();
            }
        } else if (clickType == 3 || clickType == 0 || clickType == 4) {
            if (!TextUtils.isEmpty(linkUrl)) {
                Intent intent = new Intent(this, PlayMoreActivity.class);
                intent.putExtra("webUrl", linkUrl);
                intent.putExtra("webTitle", title);
                intent.putExtra("isFromAdert", true);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        this, R.anim.slide_right_in, R.anim.slide_right_out);
                ActivityCompat.startActivity(this, intent, compat.toBundle());
                finish();
            }
        } else if (clickType == 5) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("isFromAdert", true);
            ActivityCompat.startActivity(this, intent, compat.toBundle());
            finish();
        } else if (clickType == 6) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(this, MerchantDetailsViewActivity.class);
            intent.putExtra("mid", linkUrl);
            intent.putExtra("isFromAdert", true);
            ActivityCompat.startActivity(this, intent, compat.toBundle());
            finish();
        } else {
            isClick = false;
        }
    }

    @OnClick(R.id.splash_step_tv)
    void onClickStepTv() {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                AdvertisementAvtivity.this, R.anim.fade_in, R.anim.fade_out);
        Intent intent = new Intent(AdvertisementAvtivity.this, SuperMainActivity.class);
        ActivityCompat.startActivity(AdvertisementAvtivity.this, intent, compat.toBundle());
        finish();
    }

    @Override
    public void onSplashResponse(SplashImage response) {

    }

    @Override
    public void onSplashFailure(String msg) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }
}
