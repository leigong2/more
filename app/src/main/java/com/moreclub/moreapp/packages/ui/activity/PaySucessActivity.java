package com.moreclub.moreapp.packages.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.common.util.PhoneUtil;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.packages.presenter.IOrderDetailsLoader;
import com.moreclub.moreapp.packages.presenter.OrderDetailsLoader;
import com.moreclub.moreapp.packages.ui.adapter.PaySuccessAdapter;
import com.moreclub.moreapp.packages.ui.view.CustomPayAlertView;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;
import com.moreclub.moreapp.ucenter.model.event.OrderUpdateEvent;
import com.moreclub.moreapp.ucenter.ui.activity.MyOrderActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/3/29.
 */

public class PaySucessActivity extends BaseListActivity implements
        IOrderDetailsLoader.LoaderOrderDetailsDataBinder,OnDismissListener,OnItemClickListener {

    public static final int MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE = 1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_MERCHANT_PHONE = 2;
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.pay_success_rv) XRecyclerView xRecyclerView;
    @BindView(R.id.un_pay_layout) LinearLayout un_pay_layout;
    @BindView(R.id.cancelOrderBtn) TextView cancelOrderBtn;
    @BindView(R.id.payOrderBtn) TextView payOrderBtn;

    View header;
    LinearLayout paySuccessHeaderHeader;
    TextView lookOrderButton;
    ArrayList<HashMap<String, String>> dataResoure = new ArrayList<HashMap<String, String>>();
    String pid;
    String mid;
    String payMethod;
    String payMerchant;
    long orderID;
    String orderTime;
    String packageName;
    float payOldPrice;
    float payActPrice;
    float cardRate;
    float cardRatePrice;
    float couponPrice;
    float noRatePriceValue;
    int payType;
    int enterFrom;
    int cardLevel;
    int orderStatus;
    int orderCount;
    boolean paySucess;

    CustomPayAlertView mAlertView;
    @Override
    protected int getLayoutResource() {
        return R.layout.pay_success_activity;
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
    }

    @Override
    protected Class getLogicClazz() {
        return IOrderDetailsLoader.class;
    }

    private void initData() {
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        mid = intent.getStringExtra("mid");
        packageName = intent.getStringExtra("packageName");
        payMethod = intent.getStringExtra("payMethod");
        payMerchant = intent.getStringExtra("payMerchant");
        orderID = intent.getLongExtra("orderID",0);
        orderTime = intent.getStringExtra("orderTime");
        payOldPrice = intent.getFloatExtra("totalPrice",0);
        payActPrice = intent.getFloatExtra("actPrice",0);
        payType = intent.getIntExtra("payType", 0);
        enterFrom = intent.getIntExtra("enterFrom", 0);
        paySucess = intent.getBooleanExtra("paySucess",false);
        cardRate = intent.getFloatExtra("cardRate",0);
        couponPrice = intent.getFloatExtra("couponPrice",0);
        noRatePriceValue = intent.getFloatExtra("noRatePriceValue",0);
        orderStatus = intent.getIntExtra("orderStatus", 0);
        cardLevel = intent.getIntExtra("cardLevel", 0);
        cardRatePrice = intent.getFloatExtra("cardRatePrice", 0);
        orderCount= intent.getIntExtra("orderCount", 0);

        if (cardRate==0){
            cardRate=1;
        }
        if (payType == 1) {
            HashMap<String, String> item1 = new HashMap<>();
            item1.put("Tag", getString(R.string.pay_price));
            item1.put("Value", "¥"+payOldPrice);
            item1.put("colorType", "3");
            dataResoure.add(item1);

            HashMap<String, String> item2 = new HashMap<>();
            item2.put("Tag", "不参与优惠的消费金额");
            item2.put("Value", "¥ "+noRatePriceValue);//to do
            item2.put("colorType", "1");
            dataResoure.add(item2);

            HashMap<String, String> devide = new HashMap<>();
            devide.put("Tag", "devide");
            devide.put("Value", "");
            devide.put("colorType", "1");
            dataResoure.add(devide);

            HashMap<String, String> item3 = new HashMap<>();
            item3.put("Tag", getString(R.string.act_price));
            item3.put("Value", "¥" + payActPrice);
            item3.put("colorType", "2");
            dataResoure.add(item3);

            HashMap<String, String> item4 = new HashMap<>();
            if (cardLevel==0){
                item4.put("Tag", "普通会员");//to do
                item4.put("Value", "¥ "+0);// to do
            } else if (cardLevel==1){
                if (cardRate==1){
                    item4.put("Tag", "橙卡会员");//to do
                } else {
                    item4.put("Tag", "橙卡会员"+(cardRate*10)+"折");//to do
                }
                item4.put("Value", "-¥ "+cardRatePrice);// to do
            } else  if (cardLevel ==2){
                if (cardRate==1){
                    item4.put("Tag", "黑卡会员");//to do
                } else {
                    item4.put("Tag", "黑卡会员"+(cardRate*10)+"折");//to do
                }

                item4.put("Value", "-¥ "+cardRatePrice);// to do
            }
            item4.put("colorType", "1");
            dataResoure.add(item4);

            if (couponPrice>0) {
                HashMap<String, String> item5 = new HashMap<>();
                item5.put("Tag", "优惠劵");
                item5.put("Value", "-¥ " + couponPrice);//to do
                item5.put("colorType", "1");
                dataResoure.add(item5);
            }

            HashMap<String, String> devide1 = new HashMap<>();
            devide1.put("Tag", "devide");
            devide1.put("Value", "");
            devide1.put("colorType", "1");
            dataResoure.add(devide1);

            HashMap<String, String> item6 = new HashMap<>();
            item6.put("Tag", "消费时间:");
            if (paySucess) {
                if (orderTime.length() > 11) {
                    item6.put("Value", TimeUtils.timestempToDate(orderTime.substring(0, orderTime.length() - 3)));//to do
                } else {
                    item6.put("Value", TimeUtils.timestempToDate(orderTime));//to do
                }
            } else {
                item6.put("Value", "待支付");//to do
            }
            item6.put("colorType", "1");
            item6.put("conent_type","pay_success_item2");
            dataResoure.add(item6);

            HashMap<String, String> item7 = new HashMap<>();
            item7.put("Tag", "订 单 号:");
            item7.put("Value", ""+orderID);
            item7.put("colorType", "1");
            item7.put("conent_type","pay_success_item2");
            dataResoure.add(item7);

            HashMap<String, String> item8 = new HashMap<>();
            item8.put("Tag", "支付方式:");
            if (paySucess) {
                if (payActPrice == 0) {
                    item8.put("Value", "0 元支付");
                } else {
                    item8.put("Value", payMethod);
                }
            } else {
                item8.put("Value", "待支付");
            }
            item8.put("colorType", "1");
            item8.put("conent_type","pay_success_item2");
            dataResoure.add(item8);

            HashMap<String, String> bottom = new HashMap<>();
            bottom.put("Tag", "bottom");
            bottom.put("Value", "");
            bottom.put("colorType", "1");
            dataResoure.add(bottom);
        } else {

            HashMap<String, String> item1 = new HashMap<>();
            item1.put("Tag", "支付商家");
            item1.put("Value", payMerchant);
            item1.put("colorType", "3");
            dataResoure.add(item1);

            HashMap<String, String> item2 = new HashMap<>();
            item2.put("Tag", getString(R.string.pay_price));
            item2.put("Value", "¥"+payOldPrice);
            item2.put("colorType", "3");
            dataResoure.add(item2);

            HashMap<String, String> devide = new HashMap<>();
            devide.put("Tag", "devide");
            devide.put("Value", "");
            devide.put("colorType", "1");
            dataResoure.add(devide);

            HashMap<String, String> item3 = new HashMap<>();
            item3.put("Tag", getString(R.string.act_price));
            item3.put("Value", "¥" + payActPrice);
            item3.put("colorType", "2");
            dataResoure.add(item3);

            if (couponPrice>0) {
                HashMap<String, String> item4 = new HashMap<>();
                item4.put("Tag", "优惠劵");
                item4.put("Value", "-¥ " + couponPrice);//to do
                item4.put("colorType", "1");
                dataResoure.add(item4);
            }


            HashMap<String, String> devide1 = new HashMap<>();
            devide1.put("Tag", "devide");
            devide1.put("Value", "");
            devide1.put("colorType", "1");
            dataResoure.add(devide1);

            HashMap<String, String> item6 = new HashMap<>();
            item6.put("Tag", "消费时间:");
            if (paySucess) {
                if (orderTime.length() > 11) {
                    item6.put("Value", TimeUtils.timestempToDate(orderTime.substring(0, orderTime.length() - 3)));//to do
                } else {
                    item6.put("Value", TimeUtils.timestempToDate(orderTime));//to do
                }
            } else {
                item6.put("Value", "待支付");//to do
            }
            item6.put("colorType", "1");
            item6.put("conent_type","pay_success_item2");
            dataResoure.add(item6);

            HashMap<String, String> item7 = new HashMap<>();
            item7.put("Tag", "订 单 号:");
            item7.put("Value", ""+orderID);//to do
            item7.put("colorType", "1");
            item7.put("conent_type","pay_success_item2");
            dataResoure.add(item7);

            HashMap<String, String> item8 = new HashMap<>();
            item8.put("Tag", "支付方式:");
            if (paySucess) {
                if (payActPrice == 0) {
                    item8.put("Value", "0 元支付");
                } else {
                    item8.put("Value", payMethod);
                }
            } else {
                item8.put("Value", "待支付");
            }
            item8.put("colorType", "1");
            item8.put("conent_type","pay_success_item2");
            dataResoure.add(item8);

            HashMap<String, String> bottom = new HashMap<>();
            bottom.put("Tag", "bottom");
            bottom.put("Value", "");
            bottom.put("colorType", "1");
            dataResoure.add(bottom);

        }
    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        payOrderBtn.setOnClickListener(payOrderListener);
        cancelOrderBtn.setOnClickListener(cancelOrderListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        header = LayoutInflater.from(this).inflate(R.layout.pay_success_header,
                (ViewGroup) findViewById(android.R.id.content), false);
        xRecyclerView.addHeaderView(header);
        paySuccessHeaderHeader = ButterKnife.findById(header, R.id.pay_success_header_header);
        TextView merchantName = ButterKnife.findById(header, R.id.merchant_name);
        merchantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType == 1) {
                    Intent intent = new Intent(PaySucessActivity.this, MerchantDetailsViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("mid", mid);
                    PaySucessActivity.this.startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(PaySucessActivity.this, PackageDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("mid", mid);
                    intent.putExtra("pid", pid);
                    PaySucessActivity.this.startActivity(intent);
                    finish();
                }
            }
        });
        if (paySucess) {
            un_pay_layout.setVisibility(View.GONE);
            activityTitle.setText(getString(R.string.pay_sucess));
            paySuccessHeaderHeader.setVisibility(View.VISIBLE);
            lookOrderButton = ButterKnife.findById(header, R.id.look_order_button);
            TextView privilegeLay = ButterKnife.findById(header, R.id.privilege_tag);
            privilegeLay.setOnClickListener(goCenterListener);
            TextView payTitle = ButterKnife.findById(header, R.id.pay_title);
            privilegeLay.setVisibility(View.VISIBLE);


            if (payActPrice==0){
                privilegeLay.setText("查看我的里程  ");
            } else {
                if (payType == 1) {
                    privilegeLay.setText(payActPrice + "里程已到账，查看我的里程  ");
                } else {
                    privilegeLay.setText("使用后可获得" + payActPrice + "里程，查看我的里程  ");
                }
            }
            if (payType == 1) {
                payTitle.setText("买单成功");
                merchantName.setText(payMerchant+"  ");
                lookOrderButton.setVisibility(View.GONE);
            } else {
                payTitle.setText("支付成功");
                merchantName.setText(packageName+"  ");
                lookOrderButton.setVisibility(View.VISIBLE);
            }
            lookOrderButton.setOnClickListener(lookOrderListener);
        } else {
            activityTitle.setText("订单详情");
            un_pay_layout.setVisibility(View.VISIBLE);
            paySuccessHeaderHeader.setVisibility(View.GONE);
            if (payType == 1) {
                merchantName.setText(payMerchant+"  ");
            } else {
                merchantName.setText(packageName+"  ");
            }
        }

        xRecyclerView.setPullRefreshEnabled(false);
        PaySuccessAdapter adapter = new PaySuccessAdapter(PaySucessActivity.this, dataResoure);
        xRecyclerView.setAdapter(adapter);

        mAlertView = new CustomPayAlertView(getString(R.string.order_cancel_des),
                "",
                getString(R.string.order_cancel_false),
                new String[]{getString(R.string.order_cancel_ok)},
                null, this, CustomPayAlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
    }

    View.OnClickListener cancelOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mAlertView.isShowing() && !PaySucessActivity.this.isFinishing())
                mAlertView.show();
        }
    };

    View.OnClickListener payOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    PaySucessActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(PaySucessActivity.this, PayActivity.class);
            intent.putExtra("pid", pid);
            intent.putExtra("mid", mid);
            intent.putExtra("payTitle", payMerchant);
            intent.putExtra("payType", payType);
            intent.putExtra("orderID", orderID);
            intent.putExtra("orderCount", orderCount);
            intent.putExtra("cardLevel", cardLevel);
            intent.putExtra("cardRatePrice", cardRatePrice);
            intent.putExtra("cardRate", cardRate);
            intent.putExtra("couponPrice", couponPrice);
            intent.putExtra("noRatePriceValue", noRatePriceValue);
            if (payType == 0) {
                intent.putExtra("singlePrice", ((payActPrice) / orderCount));
            } else {
                intent.putExtra("singlePrice", 0f);
            }
            intent.putExtra("actPrice", payActPrice);
            intent.putExtra("totalPrice", payOldPrice);
            intent.putExtra("merchantName", payMerchant);
//            if (result.getPayMethod() == 0) {
//                intent.putExtra("payMethod", "微信");
//            } else {
//                intent.putExtra("payMethod", "支付宝");
//            }
            if ("微信".equals(payMethod)){
                intent.putExtra("payMethod", 0);
            } else {
                intent.putExtra("payMethod", 1);
            }

            intent.putExtra("isSelectPayMethod", true);
            intent.putExtra("enterFrom", 3);
            if (payActPrice == 0) {
                intent.putExtra("zeroPay", "1");
            } else {
                intent.putExtra("zeroPay", "0");
            }
            intent.putExtra("orderStatus",orderStatus);
            ActivityCompat.startActivity(PaySucessActivity.this, intent, compat.toBundle());
        }
    };

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
//            if (enterFrom == 1) {
//                Intent intent = new Intent(PaySucessActivity.this, PackageDetailsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("mid", mid);
//                intent.putExtra("pid", pid);
//                PaySucessActivity.this.startActivity(intent);
//                finish();
//            } else if (payType == 1 || enterFrom == 2) {
//                Intent intent = new Intent(PaySucessActivity.this, MerchantDetailsViewActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("mid", mid);
//                PaySucessActivity.this.startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(PaySucessActivity.this, MyOrderActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("from","PaySucessActivity");
//                PaySucessActivity.this.startActivity(intent);
//                finish();
//            }
        }
    };

    View.OnClickListener goCenterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            AppUtils.pushLeftActivity(PaySucessActivity.this, UserCenterFragment.class);
//            finish();
        }
    };

    View.OnClickListener continueSellListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (enterFrom == 1) {
                Intent intent = new Intent(PaySucessActivity.this, PackageDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mid", mid);
                intent.putExtra("pid", pid);
                PaySucessActivity.this.startActivity(intent);
                finish();
            } else if (payType == 1 || enterFrom == 2) {
                Intent intent = new Intent(PaySucessActivity.this, MerchantDetailsViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("mid", mid);
                PaySucessActivity.this.startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(PaySucessActivity.this, MyOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from","PaySucessActivity");
                PaySucessActivity.this.startActivity(intent);
                finish();
            }
        }
    };

    View.OnClickListener lookOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PaySucessActivity.this, MyOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("from","PaySucessActivity");
            PaySucessActivity.this.startActivity(intent);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        finish();
//        if (enterFrom == 1) {
//            Intent intent = new Intent(PaySucessActivity.this, PackageDetailsActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("mid", mid);
//            intent.putExtra("pid", pid);
//            PaySucessActivity.this.startActivity(intent);
//            finish();
//        } else if (payType == 1 || enterFrom == 2) {
//            Intent intent = new Intent(PaySucessActivity.this, MerchantDetailsViewActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("mid", mid);
//            PaySucessActivity.this.startActivity(intent);
//            finish();
//        } else {
//            Intent intent = new Intent(PaySucessActivity.this, MyOrderActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("from","PaySucessActivity");
//            PaySucessActivity.this.startActivity(intent);
//            finish();
//        }
    }

    @Override
    public void onDelOrderResponse(RespDto<String> response) {

    }

    @Override
    public void onDelOrderFailure(String msg) {

    }

    @Override
    public void onCancelOrderResponse(RespDto<String> response) {
        OrderUpdateEvent event = new OrderUpdateEvent();
        event.setUpdate(true);
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void onCancelOrderFailure(String msg) {

    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position==0) {
            UserOrderCancelParam cancelParam = new UserOrderCancelParam();
            cancelParam.setUid("" + MoreUser.getInstance().getUid());
            cancelParam.setOrderId("" + orderID);
            cancelParam.setAppVersion(MainApp.getInstance().mVersion);
            cancelParam.setDeviceId(PhoneUtil.getImsi2(PaySucessActivity.this));
            cancelParam.setMachine("android");
            cancelParam.setMachineStyle("android");
            cancelParam.setOpSystem("android");
            cancelParam.setOpVersion("5.0");
            ((OrderDetailsLoader) mPresenter).cancelOrder(cancelParam);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(Constants.MORE_PHONE);
            } else {
                Toast.makeText(this,
                        "您拒绝了该权限，可能导致你无法使用该功能",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_MERCHANT_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(Constants.MORE_PHONE);
            } else {
                Toast.makeText(this,
                        "您拒绝了该权限，可能导致你无法使用该功能",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void callPhone(String ph) {
        if (ph == null || ph.length() == 0) {
            return;
        }
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + ph));
        startActivity(phoneIntent);
    }
}
