package com.moreclub.moreapp.ucenter.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moreclub.common.ui.view.tag.FlowLayout;
import com.moreclub.common.ui.view.tag.TagAdapter;
import com.moreclub.common.ui.view.tag.TagFlowLayout;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.Constants;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.packages.model.event.RefundEvent;
import com.moreclub.moreapp.packages.ui.activity.ApplyFefundActivity;
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.ucenter.model.ConsumeCode;
import com.moreclub.moreapp.ucenter.model.DetailOrder;
import com.moreclub.moreapp.ucenter.presenter.IOrderDetailLoader;
import com.moreclub.moreapp.ucenter.presenter.OrderDetailLoader;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/1.
 */

public class PkgOrderDetailActivity extends BaseActivity implements IOrderDetailLoader.
        LoaderOrderDetailDataBinder<DetailOrder> {
    @BindView(R.id.pkgorder_detail_img_header)
    ImageView headerImage;

    @BindView(R.id.pkgorder_detail_merchantName)
    TextView merchantName;

    @BindView(R.id.pkgorder_detail_pkg_title)
    TextView tvPkgName;

    @BindView(R.id.pkgorder_detail_merchantAddress)
    TextView merchantAddress;

    @BindView(R.id.pkgorder_detail_merchantphone)
    TextView merchantPhone;

    @BindView(R.id.pkgorder_detail_couponcodes)
    LinearLayout couponCodesLayout;

    @BindView(R.id.pkgorder_detail_order_number)
    TextView tvOrderNumber;

    @BindView(R.id.pkgorder_detail_order_time)
    TextView tvOrderTime;

    @BindView(R.id.pkgorder_detail_pay_total)
    TextView tvPayTotal;

    @BindView(R.id.pkgorder_detail_contact_phone)
    TextView tvContactPhone;

    @BindView(R.id.pkgorder_detail_valide_time)
    TextView tvValideTime;

    @BindView(R.id.pkgorder_detail_tips)
    TextView tvTips;

    @BindView(R.id.pkgorder_detail_toolbar)
    LinearLayout toolbar;

    @BindView(R.id.pkgorder_detail_tag_layout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.pkgorder_detail_reply_refund)
    Button btnApplyRefund;

    @BindColor(R.color.pkgorder_detail_status_not_use)
    int notUseColor;

    @BindColor(R.color.pkgorder_detail_status_used)
    int usedColor;

    private LayoutInflater inflater;

    private long orderId;
    private long uid;
    private String pkgTitle;
    private int notUsedCount = 0;
    private int amount = 0;
    private DetailOrder detailOrder;
    private ArrayList<Integer> unusedCodes;
    private boolean isRefund = false;
    private List<View> codeViews = new ArrayList<>();

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    @Override
    protected int getLayoutResource() {
        return R.layout.pkgorder_detail_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IOrderDetailLoader.class;
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

        orderId = getIntent().getLongExtra("orderId", 0);
        uid = MoreUser.getInstance().getUid();

        if (orderId != 0 && uid != 0)
            ((OrderDetailLoader)mPresenter).getPkgOrderDetail(orderId, uid);
    }

    private void setupViews() {
        unusedCodes = new ArrayList<>();
        inflater = LayoutInflater.from(this);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @OnClick(R.id.nav_back)
    void onClickBack() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @OnClick(R.id.pkgorder_detail_merchantName)
    void onClickMerchant() {
        if (detailOrder != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.
                    makeCustomAnimation(this,
                            R.anim.slide_right_in,
                            R.anim.fade_out);

            Intent intent = new Intent(this, MerchantDetailsViewActivity.class);
            //intent.putExtra("pid", "" + detailOrder.getPid());
            intent.putExtra("mid", "" + detailOrder.getMerchantId());
            ActivityCompat.startActivity(this, intent, compat.toBundle());
        }
    }

    @OnClick(R.id.pkgorder_detail_pkg_title)
    void onClicKPkgName() {
        if (detailOrder != null) {
            ActivityOptionsCompat compat = ActivityOptionsCompat.
                    makeCustomAnimation(this,
                            R.anim.slide_right_in,
                            R.anim.fade_out);

            Intent intent = new Intent(this, PackageDetailsActivity.class);
            intent.putExtra("pid", "" + detailOrder.getPid());
            intent.putExtra("mid", "" + detailOrder.getMerchantId());
            ActivityCompat.startActivity(this, intent, compat.toBundle());
        }
    }

    @OnClick(R.id.pkgorder_detail_contact_more)
    void onCliCKContactMore() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
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

    @OnClick(R.id.pkgorder_detail_reply_refund)
    void onCliCKApplyRefund() {
        if (notUsedCount == 0) {
            TastyToast.makeText(getApplicationContext(), "抱歉，您没有可供退款商品",
                    TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        if (detailOrder == null) {
            TastyToast.makeText(getApplicationContext(), "获取数据失败，请查看网络重试",
                    TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        if (detailOrder.getActualPrice().doubleValue()==0){
            TastyToast.makeText(getApplicationContext(), "0元支付，不允许退款",
                    TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return;
        }

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(this, ApplyFefundActivity.class);
        intent.putExtra("pkgTitle", pkgTitle);
        intent.putExtra("orderId", detailOrder.getOrderId());
        intent.putExtra("notUsedCount", notUsedCount);
        intent.putExtra("amount", amount * notUsedCount);
        intent.putExtra("payMethod", detailOrder.getPayMethod());
        intent.putIntegerArrayListExtra("codes", unusedCodes);

        ActivityCompat.startActivity( this, intent, compat.toBundle());
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
    public void onOrderResponse(Response<RespDto<DetailOrder>> response) {
        if (response.code() == 401) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }

        detailOrder = response.body().getData();
        if (detailOrder != null) {
            if (!isRefund) {//非退款
                pkgTitle = detailOrder.getTitle();
                amount = detailOrder.getActualPrice().intValue();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String date = sdf.format(new Date(detailOrder.getCreateTime()));
                merchantName.setText(detailOrder.getMerchantName());
                merchantAddress.setText(detailOrder.getAddress());
                merchantPhone.setText(detailOrder.getMerchantPhone() + "");
                tvTips.setText(detailOrder.getUseRules());
                tvOrderNumber.setText(detailOrder.getOrderId() + "");
                tvContactPhone.setText(detailOrder.getContactPhone() + "");
                tvOrderTime.setText(date);
                tvPayTotal.setText("¥" + detailOrder.getActualPrice().doubleValue());
                tvValideTime.setText(detailOrder.getAvlStartDate() + " 至 " + detailOrder.getAvlEndDate());

                List<String> tags = new ArrayList<>();
                tags.add(detailOrder.getDrawbackRemark());

                tagFlowLayout.setAdapter(new TagAdapter<String>(tags) {

                    @Override
                    public View getView(FlowLayout parent, int position, String tag) {
                        TextView tv = (TextView) inflater.inflate(
                                R.layout.pkgorder_tag_tv,
                                tagFlowLayout, false);
                        tv.setText(tag);
                        return tv;
                    }
                });

                Glide.with(this).
                        load(detailOrder.getThumb())
                        .dontAnimate()
                        .into(headerImage);

                setupCouponCode(detailOrder.getCodes());

                if (notUsedCount == 0) {
                    btnApplyRefund.setVisibility(View.GONE);
                }
            } else {
                btnApplyRefund.setVisibility(View.GONE);
                setupRefundCouponCode(detailOrder.getCodes());
            }

            tvPkgName.setText(detailOrder.getTitle());
        }
    }

    @Override
    public void onOrderFailure(String msg) {

    }

    private void setupCouponCode(List<ConsumeCode> codes) {
        for (ConsumeCode code : codes) {
            View item = inflater.inflate(R.layout.pkgorder_detail_codes_item, null);

            TextView tvStatus = ButterKnife.findById(item,
                    R.id.pkgorder_detail_code_status);
            TextView tvPrefix = ButterKnife.findById(item,
                    R.id.pkgorder_detail_code_prefix);
            TextView tvSufix = ButterKnife.findById(item,
                    R.id.pkgorder_detail_code_suffix);

            String stringCode = String.valueOf(code.getConsumCode());
            int length = stringCode.length();

            if (code.getStatus() == 0) {
                tvStatus.setText("未消费");
                tvStatus.setTextColor(notUseColor);
                notUsedCount ++;
                unusedCodes.add(code.getConsumCode());
            } else if(code.getStatus() == 1) {
                tvStatus.setText("已消费");
                tvStatus.setTextColor(usedColor);
                tvPrefix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                tvSufix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            }else if (code.getStatus() == 2) {
                tvStatus.setText("退款中");
                tvStatus.setTextColor(usedColor);
                tvPrefix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                tvSufix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            }

            tvPrefix.setText(stringCode.substring(0, length/2));
            tvSufix.setText(stringCode.substring(length/2, length));

            couponCodesLayout.addView(item);

            codeViews.add(item);
        }
    }

    private void setupRefundCouponCode(List<ConsumeCode> codes) {
        final int size = codes.size();
        for (int i = 0; i < size; i++) {
            View item = codeViews.get(i);

            TextView tvStatus = ButterKnife.findById(item,
                    R.id.pkgorder_detail_code_status);
            TextView tvPrefix = ButterKnife.findById(item,
                    R.id.pkgorder_detail_code_prefix);
            TextView tvSufix = ButterKnife.findById(item,
                    R.id.pkgorder_detail_code_suffix);

            ConsumeCode code = codes.get(i);
            String stringCode = String.valueOf(code.getConsumCode());
            int length = stringCode.length();

            if (code.getStatus() == 0) {
                tvStatus.setText("未消费");
                tvStatus.setTextColor(notUseColor);
                unusedCodes.add(code.getConsumCode());
            } else if(code.getStatus() == 1) {
                tvStatus.setText("已消费");
                tvStatus.setTextColor(usedColor);
                tvPrefix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                tvSufix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            }else if (code.getStatus() == 2) {
                tvStatus.setText("退款中");
                tvStatus.setTextColor(usedColor);
                tvPrefix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                tvSufix.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            }

            tvPrefix.setText(stringCode.substring(0, length/2));
            tvSufix.setText(stringCode.substring(length/2, length));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPkgRefundEvent(RefundEvent event) {
        isRefund = true;
        ((OrderDetailLoader)mPresenter).getPkgOrderDetail(orderId, uid);
    }
}
