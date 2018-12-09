package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.presenter.HeadlineLoader;
import com.moreclub.moreapp.information.ui.activity.HeadlineActivity;
import com.moreclub.moreapp.main.model.MerchantActivities;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.main.presenter.IMerchantActivityDataLoader;
import com.moreclub.moreapp.main.presenter.MerchantActivityDataLoader;
import com.moreclub.moreapp.main.ui.adapter.MerchantActivitiesAdapter;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MerchantActivitiesActivity extends BaseActivity implements
        IMerchantActivityDataLoader.LoadMerchantActivityDataBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_merchant_activities)
    XRecyclerView xrvMerchantActivities;
    private ArrayList<MerchantActivity> activities;
    private MerchantActivitiesAdapter mAdapter;
    private Integer mid;
    private int pn = 0;
    private int ps = 10;
    private int total;
    private SlidrInterface attach;

    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_activities_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IMerchantActivityDataLoader.class;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        attach = Slidr.attach(this, config);
        activityTitle.setText("商家活动");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        activities = (ArrayList<MerchantActivity>) extras.getSerializable("activities");
        mid = extras.getInt("mid");
        setupView();
    }

    private void setupView() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        xrvMerchantActivities.setLayoutManager(lm);
        xrvMerchantActivities.setPullRefreshEnabled(true);
        xrvMerchantActivities.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvMerchantActivities.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        if (activities == null) {
            ((MerchantActivityDataLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), pn, ps);
        } else {
            mAdapter = new MerchantActivitiesAdapter(this, R.layout.activities_item, activities);
            xrvMerchantActivities.setAdapter(mAdapter);
        }
        xrvMerchantActivities.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pn = 0;
                ((MerchantActivityDataLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), pn, ps);
            }

            @Override
            public void onLoadMore() {
                ++pn;
                if (pn < total / ps) {
                    ((MerchantActivityDataLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), pn, ps);
                } else if (pn == total / ps) {
                    int pageSize = total % ps;
                    ((MerchantActivityDataLoader) mPresenter).onLoadMerchantActivity(mid, MoreUser.getInstance().getUid(), pn, pageSize);
                } else {
                    Toast.makeText(MerchantActivitiesActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    onLoadComplete(pn);
                }
            }
        });
    }

    private void onLoadComplete(Integer index) {
        if (index == 0) {
            if (activities != null)
                activities.clear();
            if (xrvMerchantActivities != null)
                xrvMerchantActivities.refreshComplete();
        } else {
            if (xrvMerchantActivities != null)
                xrvMerchantActivities.loadMoreComplete();
        }
    }

    @OnClick(R.id.nav_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onMerchantActivityResponse(RespDto<List<MerchantActivity>> body) {
        onLoadComplete(pn);
        if (body != null && body.getData() != null && body.isSuccess()) {
            List<MerchantActivity> datas = body.getData();
            total = datas.size();
            activities = (ArrayList<MerchantActivity>) datas;
            mAdapter = new MerchantActivitiesAdapter(this, R.layout.activities_item, activities);
            xrvMerchantActivities.setAdapter(mAdapter);
        }
    }

    @Override
    public void onMerchantActivityFailure(String message) {
        onLoadComplete(pn);
        Log.i("zune:", "msg = " + message);
    }
}
