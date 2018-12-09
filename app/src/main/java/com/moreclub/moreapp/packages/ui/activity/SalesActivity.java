package com.moreclub.moreapp.packages.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.common.util.ScreenUtil;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.SalesInfo;
import com.moreclub.moreapp.packages.presenter.ISalesLoader;
import com.moreclub.moreapp.packages.presenter.SalesLoader;
import com.moreclub.moreapp.packages.ui.adapter.SalesAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.CooperationMerchantActivity;
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
import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * zune:接口
 * textview.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
 **/
public class SalesActivity extends BaseListActivity implements ISalesLoader.LoadSalesDataBinder {

    private static final String ORANGE = "1";
    private static final String BLACK = "2";
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_sales)
    XRecyclerView xrvSales;
    @BindView(R.id.no_data_view)
    View no_data_view;
    private List<SalesInfo.ListBean> dataList;
    private SalesAdapter adapter;
    private String city;
    private Long uid;
    private Integer pn;
    private Integer ps;
    private boolean isLoaded;
    private View header;
    private View.OnTouchListener saleListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX();
                    int screenWidth = ScreenUtil.getScreenWidth(SalesActivity.this);
                    if (x < screenWidth * 2 / 5) {
                        //Todo 橙卡合作酒吧
                        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                            AppUtils.pushLeftActivity(SalesActivity.this, UseLoginActivity.class);
                            return true;
                        }
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                SalesActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(SalesActivity.this, CooperationMerchantActivity.class);
                        intent.putExtra("cardtype", 1);
                        ActivityCompat.startActivity(SalesActivity.this, intent, compat.toBundle());
                    } else if (x > screenWidth * 3 / 5) {
                        //Todo 黑卡合作酒吧
                        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid().equals(0L)) {
                            AppUtils.pushLeftActivity(SalesActivity.this, UseLoginActivity.class);
                            return true;
                        }
                        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                                SalesActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                        Intent intent = new Intent(SalesActivity.this, CooperationMerchantActivity.class);
                        intent.putExtra("cardtype", 2);
                        ActivityCompat.startActivity(SalesActivity.this, intent, compat.toBundle());
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.sales_activity;
    }

    @Override
    public Class getLogicClazz() {
        return ISalesLoader.class;
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
        dataList = new ArrayList<>();
        setupView();
        city = PrefsUtils.getString(this, KEY_CITY_CODE, "cd");
        uid = MoreUser.getInstance().getUid();
        ps = 10;
        pn = 0;
        activityTitle.setText("SALE");
        ((SalesLoader) mPresenter).onLoadSales(city, uid, pn, ps);
        /**zune:cardType = 0普通会员 1橙卡会员 2黑卡会员**/
        ((SalesLoader) mPresenter).loadPrivilege(ORANGE, city);
        ((SalesLoader) mPresenter).loadPrivilege(BLACK, city);
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        ((SalesLoader) mPresenter).onLoadSales(city, uid, pn, ps);
        /**zune:cardType = 0普通会员 1橙卡会员 2黑卡会员**/
        ((SalesLoader) mPresenter).loadPrivilege(ORANGE, city);
        ((SalesLoader) mPresenter).loadPrivilege(BLACK, city);
    }

    private void setupView() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(SalesActivity.this);
        xrvSales.setLayoutManager(lm);
        header = LayoutInflater.from(this).inflate(R.layout.sales_header, null);
        xrvSales.addHeaderView(header);
        xrvSales.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvSales.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xrvSales.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        adapter = new SalesAdapter(SalesActivity.this, R.layout.sales_item, dataList);
        xrvSales.setAdapter(adapter);
        xrvSales.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pn = 0;
                ((SalesLoader) mPresenter).onLoadSales(city, uid, pn, ps);
            }

            @Override
            public void onLoadMore() {
                ++pn;
                isLoaded = true;
                ((SalesLoader) mPresenter).onLoadSales(city, uid, pn, ps);
            }
        });
    }

    private void initHeader(RespDto<List<Integer>> data) {
        List<Integer> result = data.getData();
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_white);
        drawable.setBounds(0, 2, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        TextView allCityOranges = (TextView) header.findViewById(R.id.tv_orange_desc);
        allCityOranges.setCompoundDrawables(null, null, drawable, null);
        TextView allCityBlacks = (TextView) header.findViewById(R.id.tv_black_desc);
        allCityBlacks.setCompoundDrawables(null, null, drawable, null);
        allCityOranges.setText("全城 " + result.get(0) + " 家酒吧");
        allCityBlacks.setText("全城 " + result.get(1) + " 家酒吧");
        if (result.get(0) > 0 || result.get(1) > 0) {
            ImageView ivSalesTop = (ImageView) header.findViewById(R.id.iv_sales_top);
            ivSalesTop.setOnTouchListener(saleListener);
        }
        adapter.notifyDataSetChanged();
    }

    private void onLoadComplete(Integer index) {
        if (index == 0) {
            dataList.clear();
            xrvSales.refreshComplete();
        } else {
            xrvSales.loadMoreComplete();
        }
    }

    @Override
    public void onSalesResponse(RespDto<SalesInfo> body) {
        if (body == null || body.getData() == null || body.getData().getList().size() == 0) {
            if (isLoaded)
                Toast.makeText(SalesActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
            onLoadComplete(pn);
        } else {
            onLoadComplete(pn);
            if (body != null && body.getData() != null && body.getData().getList() != null) {
                SalesInfo data = body.getData();
                dataList.addAll(data.getList());
                adapter.notifyDataSetChanged();
            }
        }
        if (dataList.isEmpty()) {
            no_data_view.setVisibility(View.VISIBLE);
            xrvSales.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.GONE);
            xrvSales.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSalesFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
        onLoadComplete(pn);
    }

    @Override
    public void onUserPrivilegeResponse(RespDto<List<Integer>> response) {
        initHeader(response);
    }

    @Override
    public void onUserPrivilegeFailure(String msg) {
        if (msg.equals("401")) {
            //Todo 登录
            AppUtils.pushLeftActivity(SalesActivity.this, UseLoginActivity.class);
            finish();
        }
    }


    @OnClick({R.id.nav_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                SalesActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
        }
    }
}
