package com.moreclub.moreapp.packages.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PhoneUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.DictionaryName;
import com.moreclub.moreapp.packages.model.PkgRefundReq;
import com.moreclub.moreapp.packages.presenter.IRefundResonDataLoader;
import com.moreclub.moreapp.packages.presenter.RefundReasonDataLoader;
import com.moreclub.moreapp.packages.ui.adapter.RefundResonAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.sdsmdg.tastytoast.TastyToast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/5.
 */

public class ApplyFefundActivity extends BaseActivity implements
        IRefundResonDataLoader.LoaderReasonDataBinder{
    @BindString(R.string.applyrefund_title) String title;

    @BindView(R.id.activity_title) TextView tvTitle;

    @BindView(R.id.apply_fefund_rv) XRecyclerView xRecyclerView;

    private TextView tvPkgTitle;
    private TextView tvPrdCount;
    private TextView tvPrdAmount;

    private List<DictionaryName> dictionaryNames;
    private RefundResonAdapter adapter;
    private DictionaryName selectReason;
    private ArrayList<Integer> codes;

    private long orderId;
    private int payMethod;
    private int notUsedCount;
    private int amount;

    @Override
    protected int getLayoutResource() {
        return R.layout.apply_refund_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IRefundResonDataLoader.class;
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
        dictionaryNames = new ArrayList<>();
        String pkgTitle = getIntent().getStringExtra("pkgTitle");
        notUsedCount = getIntent().getIntExtra("notUsedCount", 0);
        amount = getIntent().getIntExtra("amount", 0);
        orderId = getIntent().getLongExtra("orderId", 0);
        payMethod = getIntent().getIntExtra("payMethod", 0);
        codes = getIntent().getIntegerArrayListExtra("codes");

        tvTitle.setText(title);

        adapter = new RefundResonAdapter(this, R.layout.apply_refund_item, dictionaryNames);
        adapter.setAddHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            private CheckBox lastChecked = null;
            private int lastCheckedPos = -1;

            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                selectReason = (DictionaryName) o;

                CheckBox cb = (CheckBox) view.findViewById(R.id.refund_item_cb);
                int clickedPos = position;

                if (lastCheckedPos == clickedPos ) {
                    return;
                }

                if(lastChecked != null) {
                    lastChecked.setVisibility(View.GONE);
                    dictionaryNames.get(lastCheckedPos).setSelect(false);
                }

                cb.setVisibility(View.VISIBLE);

                lastChecked = cb;
                lastCheckedPos = clickedPos;
                dictionaryNames.get(clickedPos).setSelect(true);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        View header = LayoutInflater.from(this).inflate(R.layout.apply_refund_header,
                (ViewGroup) findViewById(android.R.id.content), false);

        tvPkgTitle = ButterKnife.findById(header, R.id.apply_refund_prd_title);
        tvPkgTitle.setText(pkgTitle);

        tvPrdCount = ButterKnife.findById(header, R.id.apply_refund_number);
        tvPrdAmount = ButterKnife.findById(header, R.id.apply_refund_total_amount);

        tvPrdCount.setText(notUsedCount+"份");
        tvPrdAmount.setText("¥ "+amount);

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.addHeaderView(header);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                ((RefundReasonDataLoader)mPresenter).getRefundReson();
            }

            @Override
            public void onLoadMore() {

            }
        });

        xRecyclerView.refresh();
    }

    @OnClick(R.id.nav_back)
    void onClickBack() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @OnClick(R.id.apply_refund_submit)
    void onClickSubmit() {
        if (selectReason == null) {
                TastyToast.makeText(getApplicationContext(), "请选择退款原因",
                    TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            return;
        }


        PkgRefundReq refundReq = new PkgRefundReq();
        refundReq.setAppVersion("1.0");
        refundReq.setDeviceId(PhoneUtil.getImsi2(this));
        refundReq.setMachine("android");
        refundReq.setMachineStyle("android");
        refundReq.setOpSystem("android");
        refundReq.setOpVersion("5.0");
        refundReq.setOperateLocation(
                MoreUser.getInstance().getUserLocationLat()+","
                +MoreUser.getInstance().getUserLocationLng());
        refundReq.setOrderId(""+ orderId);
        refundReq.setUid(""+MoreUser.getInstance().getUid());
        refundReq.setOperateIp("");
        refundReq.setOperateCity(MoreUser.getInstance().getCity());
        refundReq.setReason(selectReason.getName());
        refundReq.setPayMethod(payMethod);
        refundReq.setOperateRemark("客户端退款");
        refundReq.setCouponCodes(codes);

        ((RefundReasonDataLoader)mPresenter).doPkgRefund(refundReq);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onResponse(RespDto response) {
        xRecyclerView.refreshComplete();
        dictionaryNames.clear();

        List<DictionaryName> dicts = (List<DictionaryName>) response.getData();
        if (dicts!= null && !dicts.isEmpty()) {
            dictionaryNames.addAll(dicts);
            if (xRecyclerView.getAdapter() == null) {
                xRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFailure(String msg) {
        xRecyclerView.refreshComplete();
    }

    @Override
    public void onPkgRefundResponse(Response response) {
        if (response.code() == 401) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }

        Response<RespDto<Long>> respDtoResponse = response;

        if (respDtoResponse != null && respDtoResponse.body().isSuccess()) {
            TastyToast.makeText(getApplicationContext(), "退款成功",
                    TastyToast.LENGTH_SHORT, TastyToast.INFO);

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                    ApplyFefundActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
            Intent intent = new Intent(ApplyFefundActivity.this, RefundAuditActivity.class);
            intent.putExtra("count", notUsedCount);
            intent.putExtra("price", amount);
            intent.putExtra("payMethod", payMethod);

            ActivityCompat.startActivity( ApplyFefundActivity.this, intent, compat.toBundle());

            finish();
        } else {
            TastyToast.makeText(getApplicationContext(), respDtoResponse.body().getErrorDesc(),
                    TastyToast.LENGTH_SHORT, TastyToast.INFO);
        }
    }

    @Override
    public void onPkgRefundFailure(String msg) {
        TastyToast.makeText(getApplicationContext(), "退款失败",
                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }
}
