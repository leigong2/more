package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.presenter.IMerchantListDataLoader;
import com.moreclub.moreapp.main.presenter.MerchantListDataLoader;
import com.moreclub.moreapp.main.ui.adapter.TotalBarsAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/2/27.
 */

public class MerchantListViewActivity extends BaseListActivity implements
        IMerchantListDataLoader.LoadMerchantListDataBinder{
    private final static String KEY_CITY_CODE = "city.code";
    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;

    @BindView(R.id.merchant_rv)
    XRecyclerView xRecyclerView;
    private TotalBarsAdapter adapter;
    private ArrayList<MerchantItem> dataList;

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;

    private int searchType;
    private String searchKey;
    private String cityCode;

    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_listview_activity;
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

        initData();
        setupViews();
        loadData();
    }

    @Override
    protected void onReloadData() {
        if (page ==PAGE_START) {
            loadData();
        }
    }

    private  void loadData(){
        ((MerchantListDataLoader)mPresenter).onLoadMerchantListData(
                searchKey, cityCode, searchType, page, PAGE_SIZE);
    }

    private void initData(){
        searchType = getIntent().getIntExtra("searchType", 0);
        searchKey = getIntent().getStringExtra("searchKey");
    }

    private void setupViews() {
        activityTitle.setText(searchKey);

        dataList = new ArrayList<>();
        adapter = new TotalBarsAdapter(this, R.layout.main_bar_item, dataList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                MerchantItem item = (MerchantItem) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(MerchantListViewActivity.this,
                                R.anim.slide_right_in, R.anim.fade_out);
                Intent intent = new Intent(MerchantListViewActivity.this, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", "" + item.getMid());
                ActivityCompat.startActivity(MerchantListViewActivity.this, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                loadData();
            }

            @Override
            public void onLoadMore() {
                page ++ ;
                loadData();
            }
        });

        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected Class getLogicClazz() {
        return IMerchantListDataLoader.class;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMerchantListResponse(RespDto responce) {

        onLoadComplete(page);
        List<MerchantItem> items = (List<MerchantItem>)responce.getData();
        if ( items != null && !items.isEmpty()) {
            hideNoData();
            dataList.addAll(items);
        } else {
            if (page ==PAGE_START) {
                showNoData(false);
            }
        }

        Logger.d("load BusinessList success -> " + dataList);

        new DistanceTask(dataList).execute();

    }

    @Override
    public void onMerchantFailure(String msg) {
        onLoadComplete(page);
        showNoData(true);
        Logger.d("错误"+msg);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            xRecyclerView.refreshComplete();
        } else
            xRecyclerView.loadMoreComplete();
    }

   final class DistanceTask extends AsyncTask<Void,Void,String>{

        ArrayList<MerchantItem> oldData;
        public DistanceTask(ArrayList<MerchantItem> data){
            oldData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            for(MerchantItem item : oldData){
                String dist = AppUtils.getDistance(MoreUser.getInstance().
                        getUserLocationLat(),MoreUser.getInstance().
                        getUserLocationLng(),item.getLat(),item.getLng());
                item.setDistanceResult(dist);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.notifyDataSetChanged();
        }
    }
}
