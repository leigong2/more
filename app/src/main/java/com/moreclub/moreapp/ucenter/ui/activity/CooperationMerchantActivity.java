package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.ui.activity.BizAreaBarsActivity;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.ucenter.model.CooperationMerchant;
import com.moreclub.moreapp.ucenter.presenter.CooperationMerchantLoader;
import com.moreclub.moreapp.ucenter.presenter.ICooperationMerchantLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.CooperationMerchantAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/4/11.
 */

public class CooperationMerchantActivity extends BaseListActivity implements ICooperationMerchantLoader.LoadCooperationMerchantDataBinder {
    private final static String KEY_CITY_CODE = "city.code";
    private final static int PAGE_START = 1;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.privilege_list)
    XRecyclerView recyclerView;

    private int type;
    private ArrayList<MerchantItem> dataList = new ArrayList<>();
    CooperationMerchantAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.about_privilege;
    }

    @Override
    protected Class getLogicClazz() {
        return ICooperationMerchantLoader.class;
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
        loadData();
    }

    private void initData() {

        Intent intent = getIntent();
        type = intent.getIntExtra("cardtype", 0);

        loadData();
    }

    private void setupViews() {
        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(getString(R.string.cooperation_merchant_title));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new CooperationMerchantAdapter(this,
                R.layout.cooperation_merchant_item, dataList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                MerchantItem item = dataList.get(position);
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(CooperationMerchantActivity.this,
                                R.anim.slide_right_in, R.anim.fade_out);
                Intent intent = new Intent(CooperationMerchantActivity.this, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", "" + item.getMid());
                ActivityCompat.startActivity(CooperationMerchantActivity.this, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                recyclerView.reset();
                loadData();
            }

            @Override
            public void onLoadMore() {
                if (page < pages) {
                    page++;
                    loadData();
                } else {
                    recyclerView.loadMoreComplete();
                }
            }
        });
    }

    private void loadData() {
        ((CooperationMerchantLoader) mPresenter).loadCooperationMerchant("" + type, PrefsUtils.getString(this, KEY_CITY_CODE, "cd"), "" + page, "" + PAGE_SIZE);
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CooperationMerchantActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

//    @Override
//    public void onUserPrivilegeResponse(RespDto response) {
//
//        UserPrivilege result = (UserPrivilege) response.getData();
//        Log.d("ddd","success");
//        CooperationMerchantAdapter adapter = new CooperationMerchantAdapter(this,
//                R.layout.cooperation_merchant_item, result.getMerchants());
//
//        privilegeList.setAdapter(adapter);
//    }
//
//    @Override
//    public void onUserPrivilegeFailure(String msg) {
//        if ("401".equals(msg)) {
//            AppUtils.pushLeftActivity(CooperationMerchantActivity.this, UseLoginActivity.class);
//            return;
//        }
//        Toast.makeText(CooperationMerchantActivity.this,msg,Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onCooperationMerchantResponse(RespDto response) {
        onLoadComplete(page);
        CooperationMerchant result = (CooperationMerchant) response.getData();
        ArrayList<MerchantItem> list = result.getList();
        if (page == PAGE_START) {
            if (result.getTotal() > 10)
                pages = result.getTotal() / result.getSize() + 1;
            else
                pages = 1;
        }
        if (list != null) {
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCooperationMerchantFailure(String msg) {
        onLoadComplete(page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }
}
