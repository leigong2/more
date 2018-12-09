package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.TotalBarsActivity;
import com.moreclub.moreapp.main.ui.adapter.MerchantCouponSupportAdater;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.presenter.IUserCouponDetailsLoader;
import com.moreclub.moreapp.ucenter.presenter.UserCouponDetailsLoader;
import com.moreclub.moreapp.ucenter.ui.view.CouponCardView;
import com.moreclub.moreapp.util.ArithmeticUtils;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/6/22.
 */

public class UserCouponDetailsActivity extends BaseActivity implements IUserCouponDetailsLoader.LoadUserCouponDetailsDataBinder {

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.cardView)
    CouponCardView couponCardView;
    @BindView(R.id.coupon_name_tv)
    TextView couponName;
    @BindView(R.id.user_coupon_des)
    TextView couponDes;
    @BindView(R.id.user_coupon_time)
    TextView userCouponTime;
    @BindView(R.id.lock_lay)
    RelativeLayout lockLay;

    private String cid;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_coupon_details_activity;
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
        loadData();
        setupView();
    }

    private void setupView() {

        activityTitle.setText("我的礼券");
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
    }

    private void initData() {
        cid = getIntent().getStringExtra("cid");
    }

    public void loadData() {
        ((UserCouponDetailsLoader) mPresenter).onLoadUserCouponDetails(cid);
    }

    @Override
    protected Class getLogicClazz() {
        return IUserCouponDetailsLoader.class;
    }

    @Override
    public void onUserCouponDetailsResponse(RespDto response) {
        final UserCoupon userCoupon = (UserCoupon) response.getData();

        if (userCoupon == null) {
            Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
            return;
        }
        couponCardView.useButton.setVisibility(View.VISIBLE);
        couponCardView.priceTag.setVisibility(View.VISIBLE);
        if (userCoupon.getPromotionType() ==
                MerchantCouponSupportAdater.MorePromotionType.MorePromotionTypeCut.ordinal()) {

            couponCardView
                    .promotionTextView.setText("· 直减 ·");
            couponCardView
                    .priceTag.setText("元");
            couponCardView
                    .priceTextView.setText("" + ArithmeticUtils.round(userCoupon.getParValue(), 2));
        } else if (userCoupon.getPromotionType() ==
                MerchantCouponSupportAdater.MorePromotionType.MorePrmotionTypeFullCut.ordinal()) {

            couponCardView
                    .promotionTextView.setText("· 满减 ·");
            couponCardView
                    .priceTag.setText("元");
            couponCardView
                    .priceTextView.setText("" + ArithmeticUtils.round(userCoupon.getParValue(), 2));
        } else {

            couponCardView
                    .promotionTextView.setText("· 折扣 ·");
            couponCardView
                    .priceTag.setText("折");
            couponCardView
                    .priceTextView.setText("" + ArithmeticUtils.
                    round(userCoupon.getParValue() * 10.0, 1));
        }

        couponCardView.couponNameTextView.
                setText(userCoupon.getModalName());

        couponCardView.lockedImageView.setVisibility(View.GONE);

        if (userCoupon.getLocked()==null){
            userCoupon.setLocked(false);
        }

        if (userCoupon.getStatus() == MerchantCouponSupportAdater
                .MoreCouponStatus.MoreCouponStatusUse.ordinal() &&
                 !userCoupon.getLocked()) {
            couponCardView.cardBgView.setAlpha(0.85f);
            couponCardView.cardBackView.
                    setBackgroundColor(ContextCompat.getColor(this, R.color.coupon_bg_use));
            couponCardView.lockedImageView.setVisibility(View.GONE);
            couponCardView.useButton.setText("  去使用  ");
            couponCardView.useButton.setTextColor
                    (ContextCompat.getColor(this, R.color.regButton));
            couponCardView.useButton.
                    setBackgroundResource(R.drawable.merchant_coupon_use_btn);
            couponCardView.priceTextView.
                    setTextColor(ContextCompat.getColor(this, R.color.regButton));
            couponCardView.priceTag.
                    setTextColor(ContextCompat.getColor(this, R.color.regButton));
            couponCardView.useButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.
                            makeCustomAnimation(UserCouponDetailsActivity.this,
                                    R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(UserCouponDetailsActivity.this,
                            TotalBarsActivity.class);
                    intent.putExtra("keyword", "iscoupon");
                    intent.putExtra("couponMoal", userCoupon.getCouponModal());
                    ActivityCompat.startActivity(UserCouponDetailsActivity.this, intent, compat.toBundle());
                }
            });
        } else {
            couponCardView.cardBgView.setAlpha(0.6f);
            couponCardView.cardBackView.
                    setBackgroundColor(ContextCompat.getColor(this, R.color.coupon_bg_nouse));
            if (userCoupon.getStatus() ==
                    MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal()) {
                couponCardView.useButton.setText("  已过期  ");
                couponCardView.lockedImageView.setVisibility(View.GONE);
            } else if (userCoupon.getLocked()) {
                couponCardView.useButton.setText("  已锁定  ");
                couponCardView.lockedImageView.setVisibility(View.VISIBLE);
                lockLay.setVisibility(View.VISIBLE);
            } else {
                couponCardView.useButton.setText("  不可用  ");
                couponCardView.lockedImageView.setVisibility(View.GONE);
            }

            couponCardView.useButton.setTextColor
                    (ContextCompat.getColor(this, R.color.white));
            couponCardView.useButton.
                    setBackgroundResource(R.drawable.merchant_coupon_use_btn_locked);
            couponCardView.priceTextView.
                    setTextColor(ContextCompat.getColor(this, R.color.white));
            couponCardView.priceTag.
                    setTextColor(ContextCompat.getColor(this, R.color.white));

        }


        couponCardView
                .couponTimeTextView.setText("有效期至" +
                TimeUtils.formatChatDate2(userCoupon.getAvlEndDateTime() * 1000));

        userCouponTime.setText(TimeUtils.formatChatDate2(userCoupon.getAvlStartDateTime() * 1000)
                + "-" + TimeUtils.formatChatDate2(userCoupon.getAvlEndDateTime() * 1000));

        couponCardView.
                couponDesTextView.setText(userCoupon.getName());

        couponName.setText(userCoupon.getName());
        couponDes.setText(userCoupon.getRemark());

    }

    @Override
    public void onUserCouponDetailsFailure(String msg) {


    }
}
