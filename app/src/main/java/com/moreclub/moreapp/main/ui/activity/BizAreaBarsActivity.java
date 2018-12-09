package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.common.util.InputMethodUtils;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.presenter.BizAreaBarsDataLoader;
import com.moreclub.moreapp.main.presenter.IBizAreaBarsDataLoader;
import com.moreclub.moreapp.main.ui.adapter.TotalBarsAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.util.SaveSearchTask;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Administrator on 2017/3/1.
 */

public class BizAreaBarsActivity extends BaseListActivity implements
        IBizAreaBarsDataLoader.LoadBarsDataBinder {
    private final static String KEY_CITY_CODE = "city.code";

    @BindView(R.id.totalbars_rv)
    XRecyclerView recyclerView;

    @BindView(R.id.totalbars_et)
    EditText editText;

    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;

    private TotalBarsAdapter adapter;

    private List<MerchantItem> merchants;
    private String areaName;
    private int bizAreaId;
    private String keyword;
    private String cityCode;

    @Override
    protected int getLayoutResource() {
        return R.layout.totalbars_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IBizAreaBarsDataLoader.class;
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
        cityCode = PrefsUtils.getString(this, KEY_CITY_CODE, "cd");

        editText.clearFocus();
        editText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String searchText = editText.getText().toString();
                    if (!TextUtils.isEmpty(searchText) && !TextUtils.isEmpty(searchText.trim())) {
                        InputMethodUtils.closeSoftKeyboard(BizAreaBarsActivity.this);

                        page = PAGE_START;
                        keyword = searchText;
                        ((BizAreaBarsDataLoader) mPresenter).onLoadSeachMerchants(
                                searchText, cityCode, page, PAGE_SIZE);

                        new SaveSearchTask(getApplication()).execute(searchText);
                    } else {
                        //
                    }
                }
                return false;
            }
        });

        areaName = getIntent().getStringExtra("bname");
        bizAreaId = getIntent().getIntExtra("bid", 0);
        if (!TextUtils.isEmpty(areaName)) {
            editText.setText(areaName);
            editText.setSelection(areaName.length());
        }
        merchants = new ArrayList<>();
        adapter = new TotalBarsAdapter(this, R.layout.main_bar_item, merchants);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                MerchantItem item = (MerchantItem) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(BizAreaBarsActivity.this,
                                R.anim.slide_right_in, R.anim.fade_out);
                Intent intent = new Intent(BizAreaBarsActivity.this, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", "" + item.getMid());
                ActivityCompat.startActivity(BizAreaBarsActivity.this, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

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
                onLoadMerchants(bizAreaId);
            }

            @Override
            public void onLoadMore() {
                page++;
                onLoadMerchants(bizAreaId);
            }
        });

        onLoadMerchants(bizAreaId);
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this)) {
            hideNoData();
            onLoadMerchants(bizAreaId);
        }
        else
            showNoData(true);
    }

    @OnClick(R.id.totalbars_back)
    void onClickBack() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    public void onMerchantsResponse(RespDto response) {
        onLoadComplete(page);
        if (response.isSuccess()) {
            List<MerchantItem> items = (List<MerchantItem>) response.getData();
            if (items != null && !items.isEmpty()) {
                if (!TextUtils.isEmpty(areaName))
                    dispatchMerchant(items);
                merchants.addAll(items);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void dispatchMerchant(List<MerchantItem> items) {
        Collections.sort(items, new Comparator<MerchantItem>() {
            @Override
            public int compare(MerchantItem o1, MerchantItem o2) {
                double distanceO1 = DistanceUtil.getDistance(new LatLng(MoreUser.getInstance().getUserLocationLat(), MoreUser.getInstance().getUserLocationLng()),
                        new LatLng(o1.getLat(), o1.getLng()));
                double distanceO2 = DistanceUtil.getDistance(new LatLng(MoreUser.getInstance().getUserLocationLat(), MoreUser.getInstance().getUserLocationLng()),
                        new LatLng(o2.getLat(), o2.getLng()));
                return (int) (distanceO1 - distanceO2);
            }
        });
    }

    @Override
    public void onMerchantsFailure(String msg) {
        Logger.d("load merchants failed -> " + msg);
        onLoadComplete(page);
    }

    @Override
    public void onNearbyMerchantsResponse(RespDto response) {
        onLoadComplete(page);
        if (response.isSuccess()) {
            List<MerchantItem> nearbyMerchants = (List<MerchantItem>) response.getData();
            if (nearbyMerchants != null && !nearbyMerchants.isEmpty()) {
                dispatchMerchant(nearbyMerchants);
                merchants.addAll(nearbyMerchants);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNearbyMerchantsFailure(String msg) {
        Logger.d("load nearbyMerchants failed -> " + msg);
        onLoadComplete(page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            merchants.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    private void onLoadMerchants(int bid) {
        if (TextUtils.isEmpty(keyword)) {
            ((BizAreaBarsDataLoader) mPresenter).onLoadMerchants(bid, page, PAGE_SIZE);
        } else {
            ((BizAreaBarsDataLoader) mPresenter).onLoadSeachMerchants(
                    areaName, cityCode, page, PAGE_SIZE);

            if (page == PAGE_START) {
                new SaveSearchTask(getApplication()).execute(areaName);
            }
        }
    }
}
