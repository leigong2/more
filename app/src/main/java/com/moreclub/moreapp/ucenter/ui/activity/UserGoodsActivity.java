package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.main.ui.adapter.UserGoodAdapter;
import com.moreclub.moreapp.ucenter.model.UserGoodLike;
import com.moreclub.moreapp.ucenter.presenter.IUserGoodsLoader;
import com.moreclub.moreapp.ucenter.presenter.UserGoodsLoader;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserGoodsActivity extends BaseActivity implements IUserGoodsLoader.LoaderUserGoodsDataBinder {

    @BindView(R.id.rv_goods)
    XRecyclerView rvGoods;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    private String toUid;
    private int pn;
    private int ps = 10;
    private String from;
    private List<UserGoodLike.UserLikeRespsBean> dataLists;
    private UserGoodAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_goods_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IUserGoodsLoader.class;
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
        loadData();
    }

    private void setupView() {
        activityTitle.setText("点赞");
        dataLists = new ArrayList<>();
        toUid = getIntent().getStringExtra("toUid");
        from = getIntent().getStringExtra("from");
        adapter = new UserGoodAdapter(this, R.layout.mchannel_good_item, dataLists);
        adapter.setHasHeader(false);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvGoods.setLayoutManager(lm);
        rvGoods.setAdapter(adapter);
        rvGoods.setLoadingMoreEnabled(true);
        rvGoods.setPullRefreshEnabled(true);
        rvGoods.setRefreshProgressStyle(ProgressStyle.SysProgress);
        rvGoods.setArrowImageView(R.drawable.abc_icon_down_arrow);
        rvGoods.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        rvGoods.setLoadingListener(new com.jcodecraeer.xrecyclerview.XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pn = 0;
                loadData();
            }

            @Override
            public void onLoadMore() {
                pn++;
                loadData();
            }
        });
    }

    private void loadData() {
        if (TextUtils.isEmpty(toUid))
            toUid = "0";
        ((UserGoodsLoader) mPresenter).onLoadUserGoods(MoreUser.getInstance().getAccess_token(), Long.parseLong(toUid), pn, ps);
    }

    @Override
    public void onLoadGoodsResponse(RespDto respDto) {
        onLoadComplete(pn);
        if (respDto == null || respDto.getData() == null
                || ((UserGoodLike) respDto.getData()).getUserLikeResps() == null) return;
        UserGoodLike userLikesItem = (UserGoodLike) respDto.getData();
        List<UserGoodLike.UserLikeRespsBean> data = userLikesItem.getUserLikeResps();
        if (pn >= userLikesItem.getPages()) {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
            return;
        }
        dataLists.addAll(data);
        activityTitle.setText(dataLists.size() + "人点赞");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadGoodsFailure(String msg) {
        onLoadComplete(pn);
    }

    private void onLoadComplete(Integer index) {
        if (index == 0) {
            dataLists.clear();
            rvGoods.refreshComplete();
        } else {
            rvGoods.loadMoreComplete();
        }
    }

    @OnClick({R.id.nav_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                from = "";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        from = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from.equals("UserDetailV2Activity")) {
            AppUtils.pushLeftActivity(this, UserDetailV2Activity.class);
        }
    }
}
