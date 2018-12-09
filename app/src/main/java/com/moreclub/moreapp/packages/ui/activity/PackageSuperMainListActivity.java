package com.moreclub.moreapp.packages.ui.activity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.Package;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.packages.model.MerchantPackage;
import com.moreclub.moreapp.packages.model.PackageBaseInfo;
import com.moreclub.moreapp.packages.model.PackageList;
import com.moreclub.moreapp.packages.model.event.PkgCollectEvent;
import com.moreclub.moreapp.packages.presenter.IPackageMainListLoader;
import com.moreclub.moreapp.packages.presenter.PackageMainListLoader;
import com.moreclub.moreapp.packages.ui.adapter.PackageListAdapter;
import com.moreclub.moreapp.ucenter.model.event.UserLogoutEvent;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageSuperMainListActivity extends BaseListActivity implements IPackageMainListLoader.LoaderPackageMainListDataBinder {
    private final static String KEY_CITY_CODE = "city.code";
    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int totalPage = 1;

    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.packages_rv)
    XRecyclerView xRecyclerView;

    private PackageListAdapter adapter;
    private ArrayList<PackageBaseInfo> dataList;

    private String loadType;
    private String merchantID;

    @Override
    protected int getLayoutResource() {
        return R.layout.package_super_main_list;
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
        loadData();

    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this)) {
            hideNoData();
            loadData();
        }
        else
            showNoData(true);
    }

    private void setupViews() {

        naBack.setOnClickListener(goBackListener);
        activityTitle.setText(getString(R.string.package_list_title));


        dataList = new ArrayList<>();
        adapter = new PackageListAdapter(this, R.layout.package_super_main_list_item, dataList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                PackageBaseInfo itemValue = dataList.get(position);
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(PackageSuperMainListActivity.this,
                                R.anim.slide_right_in,
                                R.anim.fade_out);

                Intent intent = new Intent(PackageSuperMainListActivity.this,
                        PackageDetailsActivity.class);
                intent.putExtra("pid", "" + itemValue.getPid());
                intent.putExtra("mid", "" + itemValue.getMerchantId());
                intent.putExtra("position", position);
                ActivityCompat.startActivity(PackageSuperMainListActivity.this, intent, compat.toBundle());
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
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                xRecyclerView.reset();
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                if (page <= totalPage) {
                    loadData();
                } else {
                    xRecyclerView.loadMoreComplete();
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        loadType = intent.getStringExtra("loadType");
        merchantID = intent.getStringExtra("merchantID");
    }

    private void loadData() {

        if ("merchant_package".equals(loadType)) {
            if (MoreUser.getInstance().getUid() > 0) {
                ((PackageMainListLoader) mPresenter).loadMerchantPackage(merchantID, "" + page, "" + PAGE_SIZE, "" + MoreUser.getInstance().getUid());
            } else {
                ((PackageMainListLoader) mPresenter).loadMerchantPackage(merchantID, "" + page, "" + PAGE_SIZE, "");
            }

        } else {
            if (MoreUser.getInstance().getUid() > 0) {
                ((PackageMainListLoader) mPresenter).packageListLoad(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"), "" + MoreUser.getInstance().getUid(), "" + page, "" + PAGE_SIZE);
            } else {
                ((PackageMainListLoader) mPresenter).packageListLoad(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"), "", "" + page, "" + PAGE_SIZE);
            }

        }
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PackageSuperMainListActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
        }
    };

    @Override
    protected Class getLogicClazz() {
        return IPackageMainListLoader.class;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            xRecyclerView.refreshComplete();
        } else
            xRecyclerView.loadMoreComplete();
    }

    @Override
    public void onPackageListResponse(RespDto<PackageList> response) {
        onLoadComplete(page);
        PackageList items = (PackageList) response.getData();
        if (items != null && items.getList() != null && items.getList().size() > 0) {
            dataList.addAll(items.getList());
            totalPage = items.getPages();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPackageListFailure(String msg) {
        onLoadComplete(page);
        if ("401".equals(msg)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        Toast.makeText(PackageSuperMainListActivity.this, msg, Toast.LENGTH_SHORT).show();
        //onLoadComplete(page);
    }

    @Override
    public void onMerchantPackageResponse(RespDto<MerchantPackage> response) {
        onLoadComplete(page);
        MerchantPackage result = response.getData();
        if (result.getList() != null && result.getList().size() > 0) {
            dataList.addAll(result.getList());
            totalPage = result.getPages();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMerchantPackageFailure(String msg) {
        onLoadComplete(page);
    }

    /**
     * 回传处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("", "");

//        if (requestCode == 102 && data != null) {
//
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPkgCollectEvent(PkgCollectEvent event) {
        boolean collectStatus = event.ismIsCollect();
        int pos = event.getmPostion();
        PackageBaseInfo updateItem = dataList.get(pos);
        updateItem.setCollected(collectStatus);
//            adapter.notifyItemChanged(pos);
        adapter.notifyDataSetChanged();
    }

    ;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
