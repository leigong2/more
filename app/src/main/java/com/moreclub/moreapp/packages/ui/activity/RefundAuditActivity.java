package com.moreclub.moreapp.packages.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.packages.model.event.RefundEvent;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/6.
 */

public class RefundAuditActivity extends BaseActivity {
    @BindString(R.string.refundaudit_title) String title;

    @BindView(R.id.refund_audit_refund_number)
    TextView tvNumber;

    @BindView(R.id.refund_audit_refund_amount)
    TextView tvAmount;

    @BindView(R.id.refund_audit_refund_account_type)
    TextView tvPayMethod;

    @BindView(R.id.activity_title) TextView tvTitle;

    private static final int MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.refund_audit_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        setupViews();
    }

    private void setupViews() {
        Intent intent = getIntent();
        int count = intent.getIntExtra("count", 0);
        int price = intent.getIntExtra("price", 0);
        int payMethod = intent.getIntExtra("payMethod", 0);

        tvTitle.setText(title);
        tvNumber.setText(count + " 份");
        tvAmount.setText("¥ " + count * price);
        tvPayMethod.setText(payMethod == 0 ? "微信支付" : "支付宝支付");
    }

    @OnClick(R.id.nav_back)
    void onClickBack() {
        EventBus.getDefault().post(new RefundEvent());

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @OnClick(R.id.refund_audit_contact_more)
    void onCliCKContactMore() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_BOOK_PHONE);
        } else {
            callPhone(Constants.MORE_PHONE);
        }
    }

    private void callPhone(String ph) {
        if (ph==null||ph.length()==0){
            return;
        }
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + ph));
        startActivity(phoneIntent);
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
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        EventBus.getDefault().post(new RefundEvent());

        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

}
