package com.moreclub.moreapp.information.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.NewStore;
import com.moreclub.moreapp.information.presenter.INewStoreLoader;
import com.moreclub.moreapp.information.presenter.NewStoreLoader;
import com.moreclub.moreapp.information.ui.adapter.NewStoreAdapter;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewStoreActivity extends BaseActivity implements INewStoreLoader.LoadNewStoreBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_headline_list)
    XRecyclerView xrvHeadlineList;

    private int total;
    private RecyclerView.Adapter mNewStoreAdapter;
    private LinearLayoutManager lm;
    private Integer pageIndexNewStore;
    private Integer pageSizeNewStore;
    private final static String KEY_CITY_CODE = "city.code";
    private String cityCode;
    private List<MerchantItem> merchants = new ArrayList<>();

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
        cityCode = PrefsUtils.getString(this, KEY_CITY_CODE, "cd");
        setupView();
    }

    @Override
    public Class getLogicClazz() {
        return INewStoreLoader.class;
    }

    private void setupView() {
        activityTitle.setText("新店");
        lm = new LinearLayoutManager(NewStoreActivity.this);
        xrvHeadlineList.setLayoutManager(lm);
        xrvHeadlineList.setPullRefreshEnabled(true);
        xrvHeadlineList.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvHeadlineList.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        mNewStoreAdapter = new NewStoreAdapter(NewStoreActivity.this, R.layout.main_bar_item, merchants);
        xrvHeadlineList.setAdapter(mNewStoreAdapter);
        pageIndexNewStore = 1;
        pageSizeNewStore = 20;
        ((NewStoreLoader) mPresenter).onLoadNewStoreList(cityCode, pageIndexNewStore, pageSizeNewStore);
        xrvHeadlineList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageIndexNewStore = 1;
                ((NewStoreLoader) mPresenter).onLoadNewStoreList(cityCode, pageIndexNewStore, pageSizeNewStore);
            }

            @Override
            public void onLoadMore() {
                pageIndexNewStore++;
                Log.i("zune:", "index = " + pageIndexNewStore);
                if (pageIndexNewStore < total / pageSizeNewStore + 1) {
                    ((NewStoreLoader) mPresenter).onLoadNewStoreList(cityCode, pageIndexNewStore, pageSizeNewStore);
                } else if (pageIndexNewStore == total / pageSizeNewStore + 1) {
                    int pageSize = total % NewStoreActivity.this.pageSizeNewStore;
                    ((NewStoreLoader) mPresenter).onLoadNewStoreList(cityCode, pageIndexNewStore, pageSize);
                } else {
                    Toast.makeText(NewStoreActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    onLoadComplete(pageIndexNewStore);
                }
            }
        });
    }

    @Override
    public void onGetNewStoreResponse(RespDto<NewStore> response) {
        onLoadComplete(pageIndexNewStore);
        if (response.isSuccess()) {
            NewStore data = response.getData();
            for (int i = 0; i < data.getList().size(); i++) {
                String shelveDate = data.getList().get(i).getShelveDate();
                Log.i("zune:","date = "+shelveDate);
            }
            total = data.getTotal();
            ArrayList<MerchantItem> item = (ArrayList<MerchantItem>) data.getList();
            new DistanceTask(item).execute();
        }
    }

    @Override
    public void onGetNewStoreFailure(String msg) {
        onLoadComplete(pageIndexNewStore);
        Logger.i("zune:", "msg = " + msg);
    }

    private void onLoadComplete(Integer index) {
        if (index == 1) {
            merchants.clear();
            xrvHeadlineList.refreshComplete();
        } else {
            xrvHeadlineList.loadMoreComplete();
        }
    }

    @OnClick(R.id.nav_back)
    public void onViewClicked() {
        NewStoreActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    final class DistanceTask extends AsyncTask<Void, Void, String> {

        ArrayList<MerchantItem> oldData;

        public DistanceTask(ArrayList<MerchantItem> data) {
            oldData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            for (MerchantItem item : oldData) {
                String dist = AppUtils.getDistance(MoreUser.getInstance().
                        getUserLocationLat(), MoreUser.getInstance().
                        getUserLocationLng(), item.getLat(), item.getLng());
                item.setDistanceResult(dist);
            }
            merchants.addAll(oldData);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mNewStoreAdapter.notifyDataSetChanged();
        }
    }

}
