package com.moreclub.moreapp.information.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.moreclub.common.log.Logger;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.presenter.HeadlineLoader;
import com.moreclub.moreapp.information.presenter.IHeadlineLoader;
import com.moreclub.moreapp.information.ui.adapter.HeadlineAdapter;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeadlineActivity extends BaseActivity implements IHeadlineLoader.LoadHeadlineBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_headline_list)
    XRecyclerView xrvHeadlineList;
    private int total;
    private List<Headline.HeadlineDetail> item = new ArrayList<>();
    private RecyclerView.Adapter mHeadlineAdapter;
    private LinearLayoutManager lm;
    private Integer cityId;
    private Integer pageIndex;
    private Integer pageSize;
    private boolean isFirstLoad = true;
    @Override
    protected int getLayoutResource() {
        return R.layout.new_store_activity;
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
    }

    @Override
    public Class getLogicClazz() {
        return IHeadlineLoader.class;
    }

    private void setupView() {
        activityTitle.setText("头条");
        lm = new LinearLayoutManager(HeadlineActivity.this);
        xrvHeadlineList.setLayoutManager(lm);
        xrvHeadlineList.setPullRefreshEnabled(true);
        xrvHeadlineList.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvHeadlineList.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xrvHeadlineList.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        cityId = 2;
        pageIndex = 1;
        pageSize = 20;
        mHeadlineAdapter = new HeadlineAdapter(HeadlineActivity.this, R.layout.headline_item, item);
        xrvHeadlineList.setAdapter(mHeadlineAdapter);
        ((HeadlineLoader) mPresenter).onLoadHeadlineList(cityId, pageIndex, pageSize);
        xrvHeadlineList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                ((HeadlineLoader) mPresenter).onLoadHeadlineList(cityId, pageIndex, pageSize);
            }

            @Override
            public void onLoadMore() {
                ++pageIndex;
                if (pageIndex < total / pageSize + 1) {
                    ((HeadlineLoader) mPresenter).onLoadHeadlineList(cityId, pageIndex, pageSize);
                } else if (pageIndex == total / pageSize + 1) {
                    int pageSize = total % HeadlineActivity.this.pageSize;
                    ((HeadlineLoader) mPresenter).onLoadHeadlineList(cityId, pageIndex, pageSize);
                } else {
                    Toast.makeText(HeadlineActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    onLoadComplete(pageIndex);
                }
            }
        });
    }

    @Override
    public void onGetHeadlineResponse(RespDto<Headline> response) {
        onLoadComplete(pageIndex);
        if (response.isSuccess()) {
            Headline data = response.getData();
            total = data.getTotal();
            item.addAll(data.getList());
            if (isFirstLoad) {
                isFirstLoad = false;
                mHeadlineAdapter.notifyDataSetChanged();
            } else {
                mHeadlineAdapter.notifyDataSetChanged();
            }
        }
    }

    private void onLoadComplete(Integer index) {
        if (index == 1) {
            item.clear();
            xrvHeadlineList.refreshComplete();
        } else {
            xrvHeadlineList.loadMoreComplete();
        }
    }

    @Override
    public void onGetHeadlineFailure(String msg) {
        onLoadComplete(pageIndex);
        Logger.i("zune:", "msg = " + msg);
    }

    @OnClick(R.id.nav_back)
    public void onViewClicked() {
        HeadlineActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }
}
