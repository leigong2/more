package com.moreclub.moreapp.ucenter.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.ucenter.model.UserUgc;
import com.moreclub.moreapp.ucenter.presenter.IMyUgcsLoader;
import com.moreclub.moreapp.ucenter.presenter.MyUgcsLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.MyChannelAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserUgcListActivity extends BaseActivity implements IMyUgcsLoader.LoaderMyUgcsDataBinder {

    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    private int pn;
    private int ps = 20;
    private List<UserUgc> dataList;
    public MyChannelAdapter adapter;
    private String friendUid;
    public boolean hasFollow;
    public boolean from_user_detail;
    public boolean ischannel;
    private int type;

    @Override
    protected int getLayoutResource() {
        return R.layout.my_ugc_channel_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IMyUgcsLoader.class;
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
        activityTitle.setText("动态");
        friendUid = getIntent().getStringExtra("friendUid");
        ischannel = getIntent().getBooleanExtra("isChannel",false);
        hasFollow = getIntent().getBooleanExtra("hasFollow",false);
        type = getIntent().getIntExtra("type", -1);
        if (TextUtils.isEmpty(friendUid))
            friendUid = "0";
        dataList = new ArrayList<>();
        adapter = new MyChannelAdapter(this, R.layout.main_channel_item, dataList,hasFollow);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(lm);
        adapter.setHasHeader(true);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        long uid = MoreUser.getInstance().getUid();
        ((MyUgcsLoader) mPresenter).onLoadMyUgcs(uid, pn, ps, Long.parseLong(friendUid), type);
    }

    @Override
    public void onloadMyUgcsResponse(RespDto<List<UserUgc>> responce) {
        onLoadComplete(pn);
        if (responce == null || responce.getData() == null) return;
        List<UserUgc> data = responce.getData();
        if (data != null)
            dataList.addAll(data);
        if (data == null || data.size() == 0) {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onloadMyUgcsFailure(String msg) {
        onLoadComplete(pn);
    }

    private void onLoadComplete(Integer index) {
        if (index == 0) {
            dataList.clear();
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
    }

    @OnClick({R.id.nav_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (this.from_user_detail) {
            Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
            SignInteractSquareAdapter signInteractSquareAdapter = adapters.get("0");
            if (signInteractSquareAdapter != null
                    && signInteractSquareAdapter.mUserPopupWindow != null
                    && signInteractSquareAdapter.mUserPopupWindow.isShowing()) {
                signInteractSquareAdapter.upDatePopupWindow();
            } else {
                AppUtils.pushLeftActivity(this, UserDetailV2Activity.class);
            }
        }
        super.onDestroy();
    }
}
