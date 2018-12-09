package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.model.UserSignInterList;
import com.moreclub.moreapp.main.presenter.IMySignInterDataLoader;
import com.moreclub.moreapp.main.presenter.MySignInterDataLoader;
import com.moreclub.moreapp.main.ui.adapter.SignInterLiveAdapter;
import com.moreclub.moreapp.main.utils.ReflectionUtil;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_CODE;

public class MySignInterListActivity extends BaseActivity implements IMySignInterDataLoader.LoadMySignInter {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.iv_addpinzuo)
    ImageView iv_addpinzuo;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightBtn;
    private List<MerchantsSignInters> dataList;
    private SignInterLiveAdapter adapter;
    private String city;
    private Integer mPn;
    private Integer mPs;
    private int totalPage;

    @Override
    protected Class getLogicClazz() {
        return IMySignInterDataLoader.class;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_inter_total_activity;
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
        activityTitle.setText("我的约玩");
        navRightBtn.setVisibility(View.GONE);
        mPn = 0;
        mPs = 20;
        navRightBtn.setImageResource(R.drawable.user_details_menu);
        city = PrefsUtils.getString(this, KEY_CITY_CODE, "cd");
        dataList = new ArrayList<>();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(lm);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        adapter = new SignInterLiveAdapter(this, R.layout.sign_inter_live_item, dataList, "");
        adapter.setHasHeader(true);
        try {
            ArrowRefreshHeader mRefreshHeader = (ArrowRefreshHeader) ReflectionUtil.getValue(xRecyclerView, "mRefreshHeader");
            mRefreshHeader.setArrowImageView(R.drawable.abc_icon_down_arrow);
            LinearLayout first = (LinearLayout) mRefreshHeader.getChildAt(0);
            RelativeLayout second = (RelativeLayout) first.getChildAt(0);
            LinearLayout third = (LinearLayout) second.getChildAt(0);
            TextView forth = (TextView) third.getChildAt(0);
            forth.setTextColor(Color.WHITE);
            xRecyclerView.setRefreshHeader(mRefreshHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPn = 0;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPn++;
                loadData();
            }
        });
    }

    private void loadData() {
        ((MySignInterDataLoader) mPresenter).onLoadUserInter(MoreUser.getInstance().getUid(), mPn, mPs);
    }

    @OnClick({R.id.nav_back, R.id.nav_right_btn,R.id.iv_addpinzuo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                finish();
                break;
            case R.id.iv_addpinzuo:
                if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                    AppUtils.pushLeftActivity(MySignInterListActivity.this, UseLoginActivity.class);
                    return;
                }
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        MySignInterListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(MySignInterListActivity.this, AddSignInterActivity.class);
                intent.putExtra("from","MySignInterListActivity");
                ActivityCompat.startActivity(MySignInterListActivity.this, intent, compat.toBundle());
                break;
            case R.id.nav_right_btn:
                break;
        }
    }

    public void onLoadComplete(Integer index) {
        if (xRecyclerView == null)
            return;
        if (index == 0) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onUserInterResponse(RespDto response) {
        onLoadComplete(mPn);
        if (mPn == 0) {
            dataList.clear();
        }
        if (response == null || response.getData() == null) return;
        UserSignInterList data = (UserSignInterList) response.getData();
        totalPage = data.getPages();
        List<MerchantsSignInters> list = data.getList();
        if (mPn >= totalPage) {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
        } else {
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }
        if (dataList.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            xRecyclerView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            emptyText.setVisibility(View.GONE);
            xRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserInterFailure(String msg) {
        if (dataList.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            xRecyclerView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            emptyText.setVisibility(View.GONE);
            xRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPn = 0;
        loadData();
    }
}
