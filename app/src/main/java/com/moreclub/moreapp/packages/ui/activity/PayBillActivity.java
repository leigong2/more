package com.moreclub.moreapp.packages.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PhoneUtil;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.adapter.MerchantCouponSupportAdater;
import com.moreclub.moreapp.packages.model.PayBillOrder;
import com.moreclub.moreapp.packages.model.PayBillOrderParam;
import com.moreclub.moreapp.packages.model.UserMileage;
import com.moreclub.moreapp.packages.presenter.IPromotionConditionLoader;
import com.moreclub.moreapp.packages.presenter.PromotionConditionLoader;
import com.moreclub.moreapp.ucenter.model.RateCardInfo;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.adapter.UserCouponAdapter;
import com.moreclub.moreapp.ucenter.ui.view.CouponCardView;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.ArithmeticUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.WrapContentLinearLayoutManager;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/4/13.
 */

public class PayBillActivity extends BaseListActivity implements IPromotionConditionLoader.LoaderPromotionConditionDataBinder,View.OnTouchListener{
    private final static String KEY_CITY_CODE = "city.code";
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.card_lay) RelativeLayout cardLay;
    @BindView(R.id.card_image) ImageView cardImage;
    @BindView(R.id.card_logo_iv) ImageView cardLogoIv;
    @BindView(R.id.card_name) TextView cardName;
    @BindView(R.id.en_card_name) TextView enCardName;
    @BindView(R.id.disrate_tag) TextView disrateTag;
    @BindView(R.id.disrate) TextView disrate;
    @BindView(R.id.input_price) EditText inputPrice;
    @BindView(R.id.rate_Lay) LinearLayout rateLay;
    @BindView(R.id.no_rate_lay) RelativeLayout noRateLay;
    @BindView(R.id.no_rate_price) EditText noRatePrice;
    @BindView(R.id.payButton) TextView payButton;
    @BindView(R.id.coupon_icon) ImageView couponIcon;
    @BindView(R.id.coupon_name) TextView couponName;
    @BindView(R.id.enble_coupon) TextView enbledCoupon;
    @BindView(R.id.price_devide) TextView priceDevide;
    @BindView(R.id.coupon_layout) LinearLayout couponLayout;
    @BindView(R.id.vip_layout) LinearLayout vipLayout;
    @BindView(R.id.vip_name) TextView vipName;
    @BindView(R.id.vip_des) TextView vipDes;
    @BindView(R.id.vip_icon) ImageView vipIcon;
    @BindView(R.id.no_support_text) TextView noSupportText;
    @BindView(R.id.money_tag) TextView moneyTag;
    @BindView(R.id.empt) TextView empt;
    @BindView(R.id.noempt) TextView noempt;
    @BindView(R.id.no_money_tag) TextView noMoneyTag;
    @BindView(R.id.gray_layout) View grayLayout;

    private PopupWindow mCouponSupportWindow;
    private View couponSupportView;
    private RecyclerView supportList;
    private UserCouponAdapter couponSupportAdater;
    private ArrayList<UserCoupon> userCouponList;

    private String titleString;
    private int cardLevel;
    private int merchantSupportCardLevel;
    private String mid;
    private double inputPriceValue=0;
    private double noRatePriceValue=0;
    private double actPrice=0;
    private float ratef = 0;
    private float rate = 1;

    private int couponType;
    private float couponValue=1;
    private double couponPrice;
    private double couponMustPrice;
    private String couponID;
    private Boolean couponLocked;
    private Boolean superPosition;
    private Boolean noUseCoupon;
    private Boolean noEnbled;
    private int couponCount;
    private String initCouponValue;
    private String initVipValue;

    StringBuilder noSupportValue = new StringBuilder();
    @Override
    protected int getLayoutResource() {
        return R.layout.pay_bill_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {

        ButterKnife.bind(this);

        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        initData();
        setupViews();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        loadData();
    }

    @Override
    protected Class getLogicClazz() {
        return IPromotionConditionLoader.class;
    }

    private void initData() {
        userCouponList = new ArrayList<>();
        noUseCoupon = false;
        couponLocked = false;
        noEnbled = false;
        superPosition = true;
        initVipValue = "无折扣";
        initCouponValue = "无可用";

        Intent intent = getIntent();
        titleString = intent.getStringExtra("merchantName");
        mid = intent.getStringExtra("merchantID");
        merchantSupportCardLevel = intent.getIntExtra("merchantSupportCardLevel",0);
        loadData();
    }

    private void loadData(){
        ((PromotionConditionLoader) mPresenter).loadUserPromotionCondition(mid, ""+MoreUser.getInstance().getUid());
        ((PromotionConditionLoader) mPresenter).loadUserSupportCoupons(""+MoreUser.getInstance().getUid(),mid);
    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(titleString);
        rateLay.setOnClickListener(showNoRateLayListener);
        grayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCouponSupportWindow();
            }
        });
        inputPrice.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence!=null&&charSequence.length()>0){
                    String tempString = charSequence.toString();
                    if (tempString!=null&&tempString.startsWith(".")){
                        inputPrice.setText("");
                        empt.setText("");
                        inputPriceValue = 0;
                        initPrice();
                        moneyTag.setVisibility(View.GONE);
                        return;
                    }
                    moneyTag.setVisibility(View.VISIBLE);
                    inputPriceValue = Double.valueOf(charSequence.toString());
                    empt.setText(charSequence.toString());
                    computeTotal();
                } else{
                    inputPriceValue = 0;
                    empt.setText("");
                    initPrice();
                    moneyTag.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        noRatePrice.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence!=null&&charSequence.length()>0){
                    String tempString = charSequence.toString();
                    if (tempString!=null&&tempString.startsWith(".")){
                        noRatePriceValue=0;
                        noRatePrice.setText("");
                        noempt.setText("");
                        noMoneyTag.setVisibility(View.GONE);
                        return;
                    }
                    noRatePriceValue = Double.valueOf(charSequence.toString());
                    noempt.setText(""+noRatePriceValue);
                    noMoneyTag.setVisibility(View.VISIBLE);
                    if (inputPriceValue<noRatePriceValue){
                        if (charSequence.length()==1) {
                            noRatePrice.setText("");
                            noempt.setText("");
                            noMoneyTag.setVisibility(View.GONE);
                        } else {
                            noRatePrice.setText(charSequence.subSequence(0,charSequence.length()-1));
                            noRatePrice.setSelection(noRatePrice.getText().length());
                            noempt.setText(charSequence.subSequence(0,charSequence.length()-1));
                            noMoneyTag.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(PayBillActivity.this,"不打折的金额不能大于原价",Toast.LENGTH_SHORT).show();
                    }
                    computeTotal();
                } else{
                    noempt.setText("");
                    noMoneyTag.setVisibility(View.GONE);
                    noRatePriceValue = 0;
                    computeTotal();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initPrice(){

        enbledCoupon.setText(initCouponValue);
        vipDes.setText(initVipValue);
        couponPrice = 0;
        payButton.setText("¥ 0"+"  "
                +PayBillActivity.this.getString(R.string.bill_confirm));
        actPrice = 0;

    }

    private void computeTotal(){
        couponPrice = couponValue;
        if (couponType==
                MerchantCouponSupportAdater.
                        MorePromotionType.MorePromotionTypeCut.ordinal()||couponType==
                MerchantCouponSupportAdater.
                        MorePromotionType.MorePrmotionTypeFullCut.ordinal()){
            if (couponType==
                    MerchantCouponSupportAdater.
                            MorePromotionType.MorePrmotionTypeFullCut.ordinal()){
                if ((inputPriceValue-noRatePriceValue)<couponMustPrice){
                    enbledCoupon.setText("不可用");
                    noEnbled = true;
                    couponPrice = 0;
                } else {
                    enbledCoupon.setText("¥ -" + ArithmeticUtils.round(couponPrice, 2));
                    noUseCoupon = false;
                    noEnbled = false;
                }
            } else {
                if (noUseCoupon) {
                    couponPrice = 0;
                    enbledCoupon.setText("不使用");
                } else if (noEnbled) {
                    couponPrice = 0;
                    enbledCoupon.setText("不可用");
                } else {
                    enbledCoupon.setText("¥ -" + ArithmeticUtils.round(couponPrice, 2));
                }
            }
        } else  {
            enbledCoupon.setText("不可用");
            couponPrice = 0;
        }

        if (couponLocked||noUseCoupon||noEnbled){
            couponPrice = 0;
            enbledCoupon.setTextColor(ContextCompat.getColor(this,R.color.merchant_item_distance));
            couponName.setTextColor(ContextCompat.getColor(this,R.color.merchant_item_distance));
            if (couponLocked) {
                couponIcon.setImageResource(R.drawable.payment_coupon_lock);
            }
        } else {
            if (couponCount==0){
                enbledCoupon.setTextColor(ContextCompat.getColor(this,R.color.merchant_item_distance));
                couponName.setTextColor(ContextCompat.getColor(this,R.color.merchant_item_distance));
                enbledCoupon.setText(initCouponValue);
                couponPrice = 0;
            } else {
                enbledCoupon.setTextColor(ContextCompat.getColor(this, R.color.bill_total_price));
                couponName.setTextColor(ContextCompat.getColor(this, R.color.black));
                couponIcon.setImageResource(R.drawable.payment_coupon);
            }
        }

        double cardPrice =0;

        if (superPosition){
            cardPrice = (inputPriceValue-noRatePriceValue-couponPrice)*rate;
        } else {
            if ((couponLocked || noUseCoupon||noEnbled)) {
                couponPrice = 0;
                cardPrice = (inputPriceValue - noRatePriceValue - couponPrice) * rate;
            } else {
                cardPrice = (inputPriceValue - noRatePriceValue - couponPrice);
            }
        }

        if (cardPrice<0)
            cardPrice=0;

        double vipShow = ArithmeticUtils.round((inputPriceValue-
                noRatePriceValue-couponPrice)*(1-rate),2);
        if (vipShow<=0){
            vipShow=0;
        }

        if (cardLevel==0||merchantSupportCardLevel==0){
            vipDes.setText("无折扣");
            vipDes.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
            vipName.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
        } else {
            if (superPosition) {
                vipDes.setText("¥ -" + vipShow);
                vipDes.setTextColor(ContextCompat.getColor(this, R.color.bill_total_price));
                vipName.setTextColor(ContextCompat.getColor(this, R.color.black));
            } else {
                if ((couponLocked || noUseCoupon||noEnbled)) {
                    vipDes.setText("¥ -" + vipShow);
                    vipDes.setTextColor(ContextCompat.getColor(this, R.color.bill_total_price));
                    vipName.setTextColor(ContextCompat.getColor(this, R.color.black));
                } else {
                    vipDes.setText("不可叠加");
                    vipDes.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
                    vipName.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
                }
            }
        }
        double tempP = ArithmeticUtils.round(noRatePriceValue+cardPrice,2);
        if (tempP<=0){
            tempP = 0;
        }
        String stemp = "¥ "+tempP+"  "
                +PayBillActivity.this.getString(R.string.bill_confirm);

        SpannableStringBuilder spannable = new SpannableStringBuilder(stemp);
        int startIndex = 0;
        int endIndex = stemp.length()-4;
        int color = Color.parseColor("#fe6e0e");
        spannable.setSpan(new ForegroundColorSpan(color),startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        payButton.setText(spannable);
        actPrice = tempP;

    }

    View.OnClickListener showNoRateLayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            noRateLay.setVisibility(View.VISIBLE);
            rateLay.setVisibility(View.GONE);
        }
    };

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PayBillActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @OnClick(R.id.coupon_layout)
    void couponLayoutClick(){
        setupCouponSupportWindow();
    }


    /**
     * 优惠礼券
     * @param msg
     */
    @Override
    public void onUserSupportCouponsFailure(String msg) {
        couponLayout.setVisibility(View.VISIBLE);
        enbledCoupon.setText("无可用");
        initCouponValue = "无可用";
    }


    @Override
    public void onUserSupportCouponsResponse(RespDto response) {
        priceDevide.setVisibility(View.VISIBLE);
        couponLayout.setVisibility(View.VISIBLE);
        ArrayList<UserCoupon> result = (ArrayList<UserCoupon>) response.getData();

        couponName.setText("礼券");
        if (result!=null&&result.size()>0){
            couponCount = result.size();
            enbledCoupon.setText(result.size()+" 张");
            initCouponValue = result.size()+" 张";
            for(int i=0;i<result.size();i++){
                UserCoupon item = (UserCoupon) result.get(i);
                if (item.getPromotionType()==
                        MerchantCouponSupportAdater.MorePromotionType.
                                MorePromotionTypeCut.ordinal()||item.getPromotionType()==
                        MerchantCouponSupportAdater.MorePromotionType.
                                MorePrmotionTypeFullCut.ordinal()){
                    couponName.setText(item.getModalName());
                    couponType = item.getPromotionType();
                    couponValue = item.getParValue();
                    couponMustPrice = item.getMustConsumption();
                    couponID =""+ item.getRid();
                    superPosition = item.getSuperposition();
                    couponLocked = item.getLocked();

                    noEnbled = false;
                    if (item.getStatus()==
                            MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal()){
                        noEnbled = true;
                    }

                    if (couponLocked!=null&&couponLocked) {
                        enbledCoupon.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
                        couponName.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
                        couponIcon.setImageResource(R.drawable.payment_coupon_lock);
                    }
                    if (inputPriceValue>0) {
                        computeTotal();
                    } else {
                        initPrice();
                    }
                    break;
                } else {
                    continue;
                }
            }
        } else {
            enbledCoupon.setText("无可用");
            initCouponValue = "无可用";
            couponCount = 0;
        }

        if (result!=null&&result.size()>0) {
            for (int i = 0; i < result.size(); i++) {
                UserCoupon item = (UserCoupon) result.get(i);
                if (item.getLocked()) {
                    userCouponList.add(item);
                    UserCoupon tipItem = new UserCoupon();
                    tipItem.setLockedTips(this.getString(R.string.coupon_lock_tip));
                    userCouponList.add(tipItem);
                } else {
                    userCouponList.add(item);
                }
            }
        }
    }

    /**
     * 卡信息返回
     * @param response
     */
    @Override
    public void onPromotionConditionResponse(RespDto response) {
        RateCardInfo result = (RateCardInfo) response.getData();
        cardLevel = result.getCardLevel();
        priceDevide.setVisibility(View.VISIBLE);
        vipLayout.setVisibility(View.VISIBLE);

        if (result.getDiscountRate()!=null){
            ratef = Float.valueOf(result.getDiscountRate());
            rate = ratef;
            initVipValue=(ratef*10)+"折";
        } else {
            initVipValue = "无折扣";
        }

//        cardLevel = 2;
//        merchantSupportCardLevel = 1;
//        ratef= 0.8f;
//        rate = ratef;
        vipLayout.setVisibility(View.VISIBLE);
        cardLay.setVisibility(View.VISIBLE);
        /**zune:橙卡和黑卡显示背景图为普通会员**/
        if (cardLevel==0||merchantSupportCardLevel==0){
            cardImage.setImageResource(R.drawable.plaincard);
            vipIcon.setImageResource(R.drawable.payment_vip_plain);
            disrateTag.setVisibility(View.GONE);
            disrate.setVisibility(View.GONE);
            rateLay.setVisibility(View.GONE);
            noRateLay.setVisibility(View.GONE);
            initVipValue = "无折扣";
            vipDes.setText(initVipValue);
            noSupportValue.append("暂无折扣，再多积累里程，就可享橙卡优惠");
            vipDes.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
            vipName.setTextColor(ContextCompat.getColor(this, R.color.merchant_item_distance));
            if (merchantSupportCardLevel==0) {
                vipLayout.setVisibility(View.GONE);
                cardLay.setVisibility(View.GONE);
            }
        } else if (cardLevel==1){
            cardImage.setImageResource(R.drawable.orangecard);
            vipIcon.setImageResource(R.drawable.payment_morecard);
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
            disrate.setText(""+(ratef*10));
            rateLay.setVisibility(View.VISIBLE);
            initVipValue = (ratef*10)+"折";
            vipDes.setText(initVipValue);
            vipDes.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipName.setTextColor(ContextCompat.getColor(this, R.color.black));
            if (merchantSupportCardLevel >= 1) {
                noSupportValue.append("橙卡会员-可享");
                noSupportValue.append(initVipValue);
                noSupportValue.append("优惠");
            }else{
                noSupportValue.append("当前商家不打折");
            }
        } else if (cardLevel==2){
            cardImage.setImageResource(R.drawable.blackcard);
            vipIcon.setImageResource(R.drawable.payment_morecard_black);
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
            rateLay.setVisibility(View.VISIBLE);
            disrate.setText(""+(ratef*10));
            initVipValue=(ratef*10)+"折";
            vipDes.setText(initVipValue);
            vipDes.setTextColor(ContextCompat.getColor(this, R.color.black));
            vipName.setTextColor(ContextCompat.getColor(this, R.color.black));
            if (merchantSupportCardLevel==2){
                noSupportValue.append("黑卡会员-可享");
                noSupportValue.append(initVipValue);
                noSupportValue.append("优惠");
            } else {
                noSupportValue.append("黑卡会员-可享当前商家最大优惠9折");
            }
        }

        noSupportText.setText(noSupportValue.toString());

        if (ratef==0||ratef==1){
            disrateTag.setVisibility(View.GONE);
            disrate.setVisibility(View.GONE);
            rateLay.setVisibility(View.GONE);
            noRateLay.setVisibility(View.GONE);
        } else {
            rateLay.setVisibility(View.VISIBLE);
            disrateTag.setVisibility(View.VISIBLE);
            disrate.setVisibility(View.VISIBLE);
        }
        cardName.setText(result.getCardName());
        enCardName.setText(result.getEnName());
        vipName.setText(result.getCardName());

        ((PromotionConditionLoader) mPresenter).loadMileage();
    }

    @Override
    public void onPromotionConditionFailure(String msg) {
        initVipValue = "无折扣";
        vipDes.setText(initVipValue);
        noSupportValue.append("普通会员，无折扣");
        noSupportText.setText(noSupportValue.toString());
        vipLayout.setVisibility(View.GONE);
        cardLay.setVisibility(View.GONE);
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }else {
            finish();
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 里程返回
     * @param msg
     */
    @Override
    public void onMileageFailure(String msg) {

    }

    @Override
    public void onMileageResponse(RespDto response) {

        UserMileage result = (UserMileage) response.getData();
        int cardValue = result.getCardType();
        int mileageValue = result.getTotalMileage();

        noSupportValue = null;
        noSupportValue = new StringBuilder();
        /**zune:橙卡和黑卡显示背景图为普通会员**/
        if (cardValue==0||merchantSupportCardLevel==0){
            initVipValue = "无折扣";
            noSupportValue.append("暂无折扣，再积累");
            noSupportValue.append(999-mileageValue);
            noSupportValue.append("里程，可享橙卡优惠");
        } else if (cardValue==1){
            initVipValue = (ratef*10)+"折";
            if (merchantSupportCardLevel == 1) {
                noSupportValue.append("橙卡会员-可享");
                noSupportValue.append(initVipValue);
                noSupportValue.append("优惠");
            }else{
                noSupportValue.append("可享");
                noSupportValue.append(initVipValue);
                noSupportValue.append("优惠，还差");
                noSupportValue.append(9999-mileageValue);
                noSupportValue.append("里程，可享黑卡优惠");
            }
        } else if (cardValue==2){
            initVipValue=(ratef*10)+"折";
            if (merchantSupportCardLevel==2){
                noSupportValue.append("黑卡会员-可享");
                noSupportValue.append(initVipValue);
                noSupportValue.append("优惠");
            } else {
                noSupportValue.append("黑卡会员-可享当前商家最大优惠9折");
            }
        }

        noSupportText.setText(noSupportValue.toString());
    }

    /**
     * 买单订单
     * @param
     */
    @OnClick(R.id.payButton)
    void payBillOrderClick(){

        if (inputPriceValue==0){
            Toast.makeText(this,"请输入买单的金额",Toast.LENGTH_SHORT).show();
            return;
        }

        PayBillOrderParam param = new PayBillOrderParam();
        param.setActualPrice(actPrice);
        param.setAppVersion("1.0");
        param.setContactName(MoreUser.getInstance().getNickname());
        param.setDeviceId(PhoneUtil.getImsi2(this));
        param.setExPrice(noRatePriceValue);
        param.setMachine("android");
        param.setMachineStyle("android");
        param.setMerchantId(mid);
        param.setOpSystem("android");
        param.setOpVersion("5.0");
        param.setOperateCity(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"));
        param.setOperateLocation(MoreUser.getInstance().getUserLocationLat()+","+MoreUser.getInstance().getUserLocationLng());
        param.setOperateRemark("直接买单");
        param.setTotalPrice(inputPriceValue);
        param.setUid(""+MoreUser.getInstance().getUid());
        if ((couponLocked!=null&&couponLocked)||noUseCoupon||noEnbled) {

        } else {
            if (!TextUtils.isEmpty(couponID)) {
                param.setCouponId(couponID);
            }
        }

        ((PromotionConditionLoader) mPresenter).loadBillOrder(param);

    }

    @Override
    public void onPayBillOrderFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayBillOrderResponse(RespDto response) {

        for (int i=0;i<userCouponList.size();i++){
            UserCoupon item = userCouponList.get(i);
            if (couponID!=null&&couponID.equals(item.getRid()+"")){
                item.setLocked(true);
                couponLocked = true;
            }
        }


        PayBillOrder result = (PayBillOrder) response.getData();

        ActivityOptionsCompat compat = ActivityOptionsCompat.
                makeCustomAnimation(this,
                        R.anim.slide_right_in,
                        R.anim.fade_out);

        Intent intent = new Intent(this,PayActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("payTitle", titleString+" 买单");
        intent.putExtra("singlePrice",0f);
        intent.putExtra("actPrice",(float) actPrice);
        intent.putExtra("totalPrice",(float)inputPriceValue);
        intent.putExtra("payType",1);
        intent.putExtra("orderID",result.getOrderId());
        intent.putExtra("merchantName",titleString);
        intent.putExtra("cardRate",ratef);
        intent.putExtra("couponPrice",(float) couponPrice);
        intent.putExtra("noRatePriceValue",(float) noRatePriceValue);
        intent.putExtra("cardLevel",cardLevel);
        double vipShow = ArithmeticUtils.round((inputPriceValue-
                noRatePriceValue-couponPrice)*(1-rate),2);
        if (vipShow<=0){
            vipShow=0;
        }
        intent.putExtra("cardRatePrice",(float) vipShow);
        intent.putExtra("enterFrom",2);
        if (actPrice==0) {
            intent.putExtra("zeroPay", "1");
        } else {
            intent.putExtra("zeroPay", "0");
        }
        startActivity(intent);
//        computeTotal();
        finish();
    }

    private void setupCouponSupportWindow() {

        int width = ScreenUtil.getScreenWidth(PayBillActivity.this);
        int height = ScreenUtil.getScreenHeight(PayBillActivity.this);
        if (null == mCouponSupportWindow) {
            couponSupportView = LayoutInflater.from(this).inflate(
                    R.layout.merchant_support_coupon_list, null);

            ImageButton closeView = (ImageButton) couponSupportView.findViewById(R.id.close);
            supportList = (RecyclerView) couponSupportView.findViewById(R.id.support_list);
            TextView dialogTitle = (TextView) couponSupportView.findViewById(R.id.dialog_title);
            dialogTitle.setText(getString(R.string.user_coupon_title,couponCount));
            TextView noUse = (TextView) couponSupportView.findViewById(R.id.no_use_coupon);
            LinearLayout noCouponLayout = (LinearLayout) couponSupportView.
                    findViewById(R.id.no_coupon_layout);
            if (couponCount==0){
                noCouponLayout.setVisibility(View.VISIBLE);
                supportList.setVisibility(View.GONE);
                noUse.setVisibility(View.GONE);
            } else {
                noCouponLayout.setVisibility(View.GONE);
                supportList.setVisibility(View.VISIBLE);
                noUse.setVisibility(View.VISIBLE);
            }

            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeCouponSupportWindow();
                }
            });


            noUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noUseCoupon = true;
                    couponType = MerchantCouponSupportAdater.
                            MorePromotionType.MorePromotionTypeCut.ordinal();
                    computeTotal();
                    closeCouponSupportWindow();
                }
            });
            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            supportList.setLayoutManager(layoutManager);
            couponSupportAdater = new UserCouponAdapter(PayBillActivity.this,
                    R.layout.user_coupon_support_item, userCouponList);
            supportList.setAdapter(couponSupportAdater);
            couponSupportAdater.setOnItemClickListener(new UserCouponAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(UserCoupon item) {
                    if (item.getPromotionType()==
                            MerchantCouponSupportAdater.MorePromotionType.
                                    MorePromotionTypeCut.ordinal()||item.getPromotionType()==
                            MerchantCouponSupportAdater.MorePromotionType.
                                    MorePrmotionTypeFullCut.ordinal()){
                        noEnbled = false;
                    } else {
                        Toast.makeText(PayBillActivity.this,
                                PayBillActivity.this.getString(R.string.coupon_no_support),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (item.getStatus()==
                            MerchantCouponSupportAdater.MoreCouponStatus.MoreCouponStatusExpire.ordinal()){
                        noEnbled = true;
                    }

                    noUseCoupon = false;
                    couponName.setText(item.getModalName());
                    couponType = item.getPromotionType();
                    couponValue = item.getParValue();
                    couponMustPrice = item.getMustConsumption();
                    couponID =""+ item.getRid();
                    couponLocked = item.getLocked();
                    superPosition = item.getSuperposition();
                    couponPrice = couponValue;
                    computeTotal();
                    closeCouponSupportWindow();
                }
            });

            supportList.setFocusable(true);
            //设置弹出部分和面积大小
            int heightt = 0;
            if (couponCount==0){
                heightt = ScreenUtil.dp2px(this,260);
            } else if (couponCount==1) {
                heightt = ScreenUtil.dp2px(this,360);
            } else {
                heightt = height-ScreenUtil.dp2px(this,219);
            }

            mCouponSupportWindow = new PopupWindow(couponSupportView, width, heightt, true);
            //设置动画弹出效果
            mCouponSupportWindow.setAnimationStyle(R.style.coupon_popup_animation);
            // 设置半透明灰色
            ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
            mCouponSupportWindow.setBackgroundDrawable(dw);
            mCouponSupportWindow.setFocusable(false);
            mCouponSupportWindow.setTouchable(true);
            grayLayout.setVisibility(View.VISIBLE);
        }
        int[] pos = new int[2];

        mCouponSupportWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCouponSupportWindow();
    }

    private void closeCouponSupportWindow(){
        if (mCouponSupportWindow!=null&&mCouponSupportWindow.isShowing()){
            mCouponSupportWindow.dismiss();
            mCouponSupportWindow = null;
            grayLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(PayBillActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
