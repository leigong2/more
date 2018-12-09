package com.moreclub.moreapp.main.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantCouponDetails;
import com.moreclub.moreapp.main.presenter.IMerchantCouponDetailsLoader;
import com.moreclub.moreapp.main.presenter.MerchantCouponDetailsLoader;
import com.moreclub.moreapp.main.ui.adapter.MerchantCouponSupportAdater;
import com.moreclub.moreapp.main.ui.view.MerchantCouponModalView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/6/15.
 */

public class MerchantCouponDetailsActivity extends BaseActivity
        implements IMerchantCouponDetailsLoader.LoadMerchantCouponDetailsDataBinder{

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.cardView) MerchantCouponModalView couponCardView;
    @BindView(R.id.coupon_name_tv) TextView couponName;
    @BindView(R.id.user_coupon_des) TextView couponDes;
    private String cid;

    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_coupon_details;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupView();
        initData();
        loadData();
    }

    private void initData() {
        cid = getIntent().getStringExtra("cid");
    }

    @Override
    protected Class getLogicClazz() {
        return IMerchantCouponDetailsLoader.class;
    }

    private void loadData() {
        ((MerchantCouponDetailsLoader)mPresenter).onLoadMerchantCouponDetails(cid);
    }

    private void setupView() {
        activityTitle.setText("礼券说明");
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

    }

    /**
     *
     * @param response
     */
    @Override
    public void onMerchantCouponDetailsResponse(RespDto response) {

        MerchantCouponDetails merchantSupportCoupon = (MerchantCouponDetails) response.getData();

        if (merchantSupportCoupon.getPromotionType()==
                MerchantCouponSupportAdater.MorePromotionType.MorePromotionTypeCut.ordinal()){
            couponCardView.promotionTextView.setText("· 直减 ·");
        } else if (merchantSupportCoupon.getPromotionType()==
                MerchantCouponSupportAdater.MorePromotionType.MorePrmotionTypeFullCut.ordinal()){
            couponCardView.promotionTextView.setText("· 满减 ·");
        } else{
            couponCardView.promotionTextView.setText("· 折扣 ·");
        }
        couponCardView.couponDesTextView.setText(merchantSupportCoupon.getName());


        if (merchantSupportCoupon.getType()== MerchantCouponSupportAdater.MoreCouponType.MoreCouponTypeMore.ordinal()){
            couponCardView.couponNameTextView.setText("more劵");
        } else {
            couponCardView.couponNameTextView.setText("more欢乐劵");
        }
        couponCardView.priceTextView.setText(""+merchantSupportCoupon.getParValue());

        if (merchantSupportCoupon.getStatus()==
                MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusUse.ordinal()){
            couponCardView.cardBgView.setBackgroundColor(
                    ContextCompat.getColor(MerchantCouponDetailsActivity.this,R.color.black));
        } else if(merchantSupportCoupon.getStatus()==
                MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal()){
            couponCardView.cardBgView.setBackgroundColor(
                    ContextCompat.getColor(MerchantCouponDetailsActivity.this,R.color.merchant_address));
        } else {
            couponCardView.cardBgView.setBackgroundColor(
                    ContextCompat.getColor(MerchantCouponDetailsActivity.this,R.color.black));
        }

        couponCardView.couponDesTextView.setText(merchantSupportCoupon.getName());
        couponName.setText(merchantSupportCoupon.getName());
        couponDes.setText(merchantSupportCoupon.getRemark());

    }

    @Override
    public void onMerchantCouponDetailsFailure(String msg) {



    }
}
