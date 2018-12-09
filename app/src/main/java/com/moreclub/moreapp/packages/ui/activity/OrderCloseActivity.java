package com.moreclub.moreapp.packages.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/6.
 */

public class OrderCloseActivity extends BaseActivity {
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.order_package_name) TextView orderPackageName;
    @BindView(R.id.order_number) TextView orderNumber;
    @BindView(R.id.order_time) TextView orderTime;
    @BindView(R.id.order_price) TextView orderPrice;
    @BindView(R.id.order_phone) TextView orderPhone;
    @BindView(R.id.order_status_text) TextView order_status_text;
    @BindView(R.id.my_phone_layout) LinearLayout my_phone_layout;
    @BindView(R.id.my_phone_line) TextView my_phone_line;
    private String orderPackageNameStr;
    private String orderNumberStr;
    private String orderTimeStr;
    private String orderPriceStr;
    private String orderPhoneStr;
    private int orderStatus;
    @Override
    protected int getLayoutResource() {
        return R.layout.order_close_activity;
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
        Intent intent = getIntent();
        orderPackageNameStr = intent.getStringExtra("orderPackageName");
        orderNumberStr= intent.getStringExtra("orderNumber");
        orderTimeStr= intent.getStringExtra("orderTime");
        orderPriceStr= intent.getStringExtra("orderPrice");
        orderPhoneStr= intent.getStringExtra("orderPhone");
        orderStatus = intent.getIntExtra("orderStatus",0);
    }

    private void setupViews() {
        if (orderStatus==3){
            activityTitle.setText("已过期");
            order_status_text.setText("已过期");
        } else if (orderStatus==4){
            activityTitle.setText(getString(R.string.package_order_close));
            order_status_text.setText(getString(R.string.package_order_close));
        } else if (orderStatus==5){
            activityTitle.setText("订单取消");
            order_status_text.setText("订单取消");
        } else {
            activityTitle.setText(getString(R.string.package_order_close));
            order_status_text.setText(getString(R.string.package_order_close));
        }

        naBack.setOnClickListener(goBackListener);
        orderPackageName.setText(orderPackageNameStr);
        orderNumber.setText(orderNumberStr);
        if (orderTimeStr.length()>3){
            orderTime.setText(TimeUtils.timestempToDate(orderTimeStr.substring(0,orderTimeStr.length()-3)));
        } else {
            orderTime.setText("");
        }

        orderPrice.setText(orderPriceStr);
        if (orderPhoneStr==null||orderPhoneStr.length()==0){
            my_phone_layout.setVisibility(View.GONE);
            my_phone_line.setVisibility(View.GONE);
        } else {
            my_phone_layout.setVisibility(View.VISIBLE);
            orderPhone.setText(orderPhoneStr);
            my_phone_line.setVisibility(View.VISIBLE);
        }
    }
    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OrderCloseActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @Override
    public void onBackPressed() {
        OrderCloseActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }
}
