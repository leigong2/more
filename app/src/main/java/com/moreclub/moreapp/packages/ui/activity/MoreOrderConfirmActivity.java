package com.moreclub.moreapp.packages.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PhoneUtil;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.adapter.MerchantCouponSupportAdater;
import com.moreclub.moreapp.packages.model.PackageOrder;
import com.moreclub.moreapp.packages.model.PackageOrderResult;
import com.moreclub.moreapp.packages.presenter.IPackageConfirmOrderLoader;
import com.moreclub.moreapp.packages.presenter.PackageConfirmOrderLoader;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.adapter.UserCouponAdapter;
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

/**
 * Created by Captain on 2017/3/27.
 */

public class MoreOrderConfirmActivity extends BaseActivity implements IPackageConfirmOrderLoader.LoaderPackageConfirmOrder{
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.package_title) TextView packageTileText;
    @BindView(R.id.package_des) TextView packageDesText;
    @BindView(R.id.more_details) LinearLayout moreDetails;
    @BindView(R.id.orderNumberEditText) EditText orderNumberEditText;
    @BindView(R.id.phone_value) EditText phoneValueEditText;
    @BindView(R.id.reduceButton) ImageButton reduceButton;
    @BindView(R.id.addButton) ImageButton addButton;
    @BindView(R.id.total_privce) TextView totalPrivceTextView;
    @BindView(R.id.submit_order_button) Button submitOrderButton;
    @BindView(R.id.openDesIV) ImageView openDesIV;
    @BindView(R.id.old_price) TextView oldPriceTV;
    @BindView(R.id.coupon_name) TextView couponName;
    @BindView(R.id.enble_coupon) TextView enbledCoupon;
    @BindView(R.id.coupon_devide) TextView couponDevide;
    @BindView(R.id.coupon_icon) ImageView couponIcon;
    @BindView(R.id.coupon_layout) LinearLayout couponLayout;
    @BindView(R.id.gray_layout) View grayLayout;
    /**
     * 礼券
     */
    private PopupWindow mCouponSupportWindow;
    private View couponSupportView;
    private RecyclerView supportList;
    private UserCouponAdapter couponSupportAdater;
    private ArrayList<UserCoupon> userCouponList;
    private int couponType;
    private float couponValue=1;
    private double couponPrice;
    private double couponMustPrice;
    private String couponID;
    private Boolean couponLocked;
    private Boolean superPosition;
    private Boolean noUseCoupon;
    private Boolean noEnbled;
    private String initCouponValue;
    private int couponCount;
    String packageTitle;
    String packageDes;
    double actPrice;
    double optionPrice;
    String merchantName;
    String mid;
    String pid;
    double price;
    double oldPrice;
    int totalNumber = 1;
    boolean isShowDetails = false;
    Matrix matrix = new Matrix();
    @Override
    protected int getLayoutResource() {
        return R.layout.package_confirm;
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

    @Override
    protected Class getLogicClazz() {
        return IPackageConfirmOrderLoader.class;
    }

    private void initData() {
        couponLocked = false;
        noUseCoupon = false;
        noEnbled= false;
        userCouponList = new ArrayList<>();
        initCouponValue = "无可用";

        Intent intent = getIntent();
        packageTitle = intent.getStringExtra("packageTitle");
        packageDes = intent.getStringExtra("packageDes");
        mid = intent.getStringExtra("mid");
        pid = intent.getStringExtra("pid");
        price = intent.getDoubleExtra("price",0);
        merchantName = intent.getStringExtra("merchantName");
        oldPrice = intent.getDoubleExtra("oldPrice",0);

        ((PackageConfirmOrderLoader)mPresenter).loadUserSupportCoupons(""+MoreUser.getInstance().getUid(),mid);
    }

    private void setupViews() {
        packageTileText.setText(packageTitle);
        packageDesText.setText(packageDes);
        activityTitle.setText(getString(R.string.package_order_title));
        naBack.setOnClickListener(goBackListener);
        grayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCouponSupportWindow();
            }
        });

        orderNumberEditText.addTextChangedListener(new EditChangedListener());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalNumber++;
                orderNumberEditText.setText(""+totalNumber);
                optionPrice = ArithmeticUtils.mul(totalNumber,price,2);
                actPrice = optionPrice;
                totalPrivceTextView.setText("¥ " + optionPrice);
                computeTotal();

                if (oldPrice>0) {
                    oldPriceTV.setText("¥ " + ArithmeticUtils.mul(totalNumber, oldPrice, 2));
                }
            }
        });

        reduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalNumber--;
                if (totalNumber<0){
                    totalNumber= 0;
                    orderNumberEditText.setText(""+totalNumber);
                    enbledCoupon.setText(initCouponValue);
                    actPrice=0;
                    optionPrice=0;
                    return;
                }
                orderNumberEditText.setText(""+totalNumber);
                optionPrice = ArithmeticUtils.mul(totalNumber,price,2);
                actPrice = optionPrice;
                totalPrivceTextView.setText("¥ " + optionPrice);
                computeTotal();
                if (oldPrice>0) {
                    oldPriceTV.setText("¥ " + ArithmeticUtils.mul(totalNumber, oldPrice, 2));
                }
            }
        });

        optionPrice = ArithmeticUtils.mul(totalNumber,price,2);
        actPrice = optionPrice;
        totalPrivceTextView.setText("¥ " +optionPrice);
        submitOrderButton.setOnClickListener(submitOrderListener);
        moreDetails.setOnClickListener(openDesListener);
        phoneValueEditText.setText(MoreUser.getInstance().getUserPhone());
        if (oldPrice>0) {
            oldPriceTV.setVisibility(View.VISIBLE);
            oldPriceTV.setText("¥ " + ArithmeticUtils.mul(totalNumber, oldPrice, 2));
            oldPriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        } else {
            oldPriceTV.setVisibility(View.GONE);
        }
    }

    private void submitData() {

        String number = orderNumberEditText.getText().toString();
        if (TextUtils.isEmpty(number)||number.equals("0")){
            Toast.makeText(MoreOrderConfirmActivity.this,getString(R.string.select_package_number),Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = phoneValueEditText.getText().toString();
        if (TextUtils.isEmpty(phone)||phone.length()!=11){
            Toast.makeText(MoreOrderConfirmActivity.this,getString(R.string.selelct_phone),Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            MoreUser.getInstance().setUserPhone(phone);
            PrefsUtils.getEditor(this)
                    .putString(Constants.KEY_PHONE_USER, phone)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }

        PackageOrder param = new PackageOrder();
        param.setActualPrice(""+actPrice);
        param.setContactName(MoreUser.getInstance().getNickname());
        param.setContactPhone(phone);
        param.setAppVersion("1.0");
        param.setItemNum(totalNumber);
        param.setDeviceId(PhoneUtil.getImsi2(MoreOrderConfirmActivity.this));
        param.setMachine("android");
        param.setMachineStyle("android");
        param.setOpSystem("android");
        param.setOpVersion("5.0");
        param.setMerchantId(mid);
        param.setTotalPrice(""+optionPrice);
        param.setOperateLocation(""+MoreUser.getInstance().getUserLocationLat()+","+MoreUser.getInstance().getUserLocationLng());
        param.setPid(pid);
        param.setUid(""+MoreUser.getInstance().getUid());
        if (noUseCoupon||noEnbled||couponLocked){

        } else {
            if (!TextUtils.isEmpty(couponID)) {
                param.setCouponId(couponID);
            }
        }
        ((PackageConfirmOrderLoader)mPresenter).packageOrder(param);
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MoreOrderConfirmActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    View.OnClickListener submitOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitData();
        }
    };

    View.OnClickListener openDesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isShowDetails = !isShowDetails;
            if (isShowDetails){

                Bitmap bitmap =((BitmapDrawable) ContextCompat.getDrawable(MoreOrderConfirmActivity.this,R.drawable.triangle_down)).getBitmap();
                // 设置旋转角度
                matrix.setRotate(180);
                // 重新绘制Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);
                openDesIV.setImageBitmap(bitmap);
                packageDesText.setVisibility(View.VISIBLE);
            } else {
                openDesIV.setImageResource(R.drawable.triangle_down);
                packageDesText.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onPackageOrderResponse(RespDto response) {
        PackageOrderResult result = (PackageOrderResult) response.getData();

        if (result.getOrderId()>0){
            for (int i=0;i<userCouponList.size();i++){
                UserCoupon item = userCouponList.get(i);
                if (couponID!=null&&couponID.equals(item.getRid()+"")){
                    item.setLocked(true);
                }
            }

            ActivityOptionsCompat compat = ActivityOptionsCompat.
                    makeCustomAnimation(MoreOrderConfirmActivity.this,
                            R.anim.slide_right_in,
                            R.anim.fade_out);

            Intent intent = new Intent(MoreOrderConfirmActivity.this,PayActivity.class);
            intent.putExtra("pid",pid);
            intent.putExtra("mid", mid);
            intent.putExtra("payTitle", packageTitle);
            intent.putExtra("singlePrice",(float) price);
            intent.putExtra("actPrice",(float)actPrice);
            intent.putExtra("totalPrice",(float) optionPrice);
            intent.putExtra("payType",0);
            intent.putExtra("orderCount",totalNumber);
            intent.putExtra("leftSecond",result.getLeftSecond());
            intent.putExtra("orderID",result.getOrderId());
            intent.putExtra("merchantName",merchantName);
            intent.putExtra("enterFrom",1);
            intent.putExtra("cardRatePrice",(float) 0);
            intent.putExtra("couponPrice",couponPrice);
            intent.putExtra("noRatePriceValue",0);
            intent.putExtra("cardLevel",0);
            intent.putExtra("cardRate",1);
            if (actPrice==0) {
                intent.putExtra("zeroPay", "1");
            } else {
                intent.putExtra("zeroPay", "0");
            }
            MoreOrderConfirmActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onPackageOrderFailure(String msg) {
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(MoreOrderConfirmActivity.this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(MoreOrderConfirmActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserSupportCouponsFailure(String msg) {
        couponDevide.setVisibility(View.VISIBLE);
        couponLayout.setVisibility(View.VISIBLE);
        enbledCoupon.setText("无可用");
        initCouponValue = "无可用";
    }

    @Override
    public void onUserSupportCouponsResponse(RespDto response) {
        ArrayList<UserCoupon> result = (ArrayList<UserCoupon>) response.getData();
        couponDevide.setVisibility(View.VISIBLE);
        couponLayout.setVisibility(View.VISIBLE);

        couponName.setText("礼券");
        if (result!=null&&result.size()>0){
            couponCount = result.size();
            initCouponValue = result.size()+" 张";
            enbledCoupon.setText(initCouponValue);
            for(int i=0;i<result.size();i++){
                UserCoupon item = (UserCoupon) result.get(i);
                if (item.getPromotionType()==
                        MerchantCouponSupportAdater.MorePromotionType.
                                MorePromotionTypeCut.ordinal()||item.getPromotionType()==
                        MerchantCouponSupportAdater.
                                MorePromotionType.MorePrmotionTypeFullCut.ordinal()){
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
                    computeTotal();
                    break;
                } else {
                    continue;
                }
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
        } else {
            couponCount=0;
            enbledCoupon.setText("无可用");
            initCouponValue = "无可用";
        }
    }

    class EditChangedListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String numberS = s.toString();
            if (!TextUtils.isEmpty(numberS)) {
                totalNumber = Integer.valueOf(numberS);
                optionPrice = ArithmeticUtils.mul(totalNumber,price,2);
                actPrice = optionPrice;
                totalPrivceTextView.setText("¥ " + optionPrice);
                computeTotal();

                if (oldPrice>0) {
                    oldPriceTV.setText("¥ " + ArithmeticUtils.mul(totalNumber, oldPrice, 2));
                }
            } else {
                totalNumber =0;
                optionPrice = ArithmeticUtils.mul(totalNumber,price,2);
                actPrice = optionPrice;
                totalPrivceTextView.setText("¥ " + optionPrice);
                computeTotal();

                if (oldPrice>0) {
                    oldPriceTV.setText("¥ " + ArithmeticUtils.mul(totalNumber, oldPrice, 2));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (InputMethodUtils.isShouldHideKeyboard(v, ev)) {
                InputMethodUtils.hideKeyboard(MoreOrderConfirmActivity.this,
                        v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @OnClick(R.id.coupon_layout)
    void couponLayoutClick(){
        setupCouponSupportWindow();
    }

    private void setupCouponSupportWindow() {

        int width = ScreenUtil.getScreenWidth(MoreOrderConfirmActivity.this);
        int height = ScreenUtil.getScreenHeight(MoreOrderConfirmActivity.this);
        if (null == mCouponSupportWindow) {
            couponSupportView = LayoutInflater.from(this).inflate(
                    R.layout.merchant_support_coupon_list, null);

            ImageButton closeView = (ImageButton) couponSupportView.findViewById(R.id.close);
            supportList = (RecyclerView) couponSupportView.findViewById(R.id.support_list);
            TextView dialogTitle = (TextView) couponSupportView.findViewById(R.id.dialog_title);
            TextView noUse = (TextView) couponSupportView.findViewById(R.id.no_use_coupon);
            dialogTitle.setText(getString(R.string.user_coupon_title,couponCount));

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
                    computeTotal();
                    closeCouponSupportWindow();
                }
            });

            WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            supportList.setLayoutManager(layoutManager);

            couponSupportAdater = new UserCouponAdapter(MoreOrderConfirmActivity.this, R.layout.user_coupon_support_item, userCouponList);
            supportList.setAdapter(couponSupportAdater);
            couponSupportAdater.setOnItemClickListener(new UserCouponAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(UserCoupon item) {
                    if (item.getPromotionType()==
                            MerchantCouponSupportAdater.MorePromotionType.
                                    MorePromotionTypeCut.ordinal()||item.getPromotionType()==
                            MerchantCouponSupportAdater.
                                    MorePromotionType.MorePrmotionTypeFullCut.ordinal()){
                        noEnbled = false;
                    } else {
                        Toast.makeText(MoreOrderConfirmActivity.this,
                                MoreOrderConfirmActivity.this.getString(R.string.coupon_no_support),
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
            grayLayout.setVisibility(View.VISIBLE);
            mCouponSupportWindow.setFocusable(false);
            mCouponSupportWindow.setTouchable(true);
        }
        int[] pos = new int[2];

        mCouponSupportWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
                pos[0], 0);

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
                if ((actPrice)<couponMustPrice){
                    couponPrice = 0;
                    enbledCoupon.setText("不可用");
                    noEnbled = true;
                } else {
                    enbledCoupon.setText("¥ -"+ArithmeticUtils.round(couponPrice,2));
                    noEnbled = false;
                    noUseCoupon = false;
                }
            } else {
                if (noUseCoupon){
                    couponPrice = 0;
                    enbledCoupon.setText("不使用");
                } else if (noEnbled){
                    couponPrice = 0;
                    enbledCoupon.setText("不可用");
                } else {
                    enbledCoupon.setText("¥ -"+ArithmeticUtils.round(couponPrice,2));
                }
            }

        } else {
            noEnbled= true;
            couponPrice = 0;
            enbledCoupon.setText("不可用");
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
                couponPrice = 0;
                enbledCoupon.setTextColor(ContextCompat.getColor(this,R.color.merchant_item_distance));
                couponName.setTextColor(ContextCompat.getColor(this,R.color.merchant_item_distance));
                enbledCoupon.setText(initCouponValue);
            } else {
                enbledCoupon.setTextColor(ContextCompat.getColor(this, R.color.bill_total_price));
                couponName.setTextColor(ContextCompat.getColor(this, R.color.black));
                couponIcon.setImageResource(R.drawable.payment_coupon);
            }
        }

        double tempP = ArithmeticUtils.round(optionPrice-couponPrice,2);
        if (tempP<=0){
            tempP = 0;
        }
        submitOrderButton.setText("¥ "+tempP+"  "
                +this.getString(R.string.bill_confirm));
        actPrice = tempP;
    }


    private void closeCouponSupportWindow(){
        if (mCouponSupportWindow!=null&&mCouponSupportWindow.isShowing()){
            mCouponSupportWindow.dismiss();
            mCouponSupportWindow = null;
            grayLayout.setVisibility(View.GONE);
        }
    }
}
