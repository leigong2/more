package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.presenter.CityDataLoader;
import com.moreclub.moreapp.main.presenter.ICityDataLoader;
import com.moreclub.moreapp.main.ui.adapter.SelectCityAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Administrator on 2017/3/2.
 */

public class SelectCityActivity extends BaseListActivity implements
        ICityDataLoader.CityDataBinder<City>{
    private final static String KEY_CITY_ID = "city.id";
    private final static String KEY_CITY_CODE = "city.code";
    private final static String KEY_CITY_NAME = "city.name";

    @BindView(R.id.selectcity_rv)
    RecyclerView xRecyclerView;
    @BindView(R.id.selectcity_close)
    ImageView close;

    private SelectCityAdapter adapter;
    private List<City> cityList;

    @Override
    protected int getLayoutResource() {
        return R.layout.selectcity_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return ICityDataLoader.class;
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
        boolean fromSplash = getIntent().getBooleanExtra("FROM_SPLASH", false);
        if (fromSplash) {
            close.setVisibility(View.GONE);
        }
        cityList = new ArrayList<>();
        setupViews();
        ((CityDataLoader)mPresenter).onLoadCity();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this)) {
            hideNoData();
            ((CityDataLoader)mPresenter).onLoadCity();
        }
        else
            showNoData(true);
    }

    private void setupViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new SelectCityAdapter(this, R.layout.selectcity_item, cityList);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                City city = (City) o;
                PrefsUtils.getEditor(SelectCityActivity.this)
                        .putInt(KEY_CITY_ID, city.getCid())
                        .putString(KEY_CITY_CODE, city.getCityCode())
                        .putString(KEY_CITY_NAME, city.getCityName())
                        .commit();

                Intent intent = new Intent(SelectCityActivity.this, SuperMainActivity.class);
                intent.putExtra("from", "SelectCityActivity");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setHasFixedSize(true);
        xRecyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.selectcity_close, R.id.selectcity_nav})
    void onClickClose() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCityResponse(List<City> response) {
        if (response != null && !response.isEmpty()) {
            int cid = PrefsUtils.getInt(SelectCityActivity.this,
                    KEY_CITY_ID, 2);
            for (City city : response){
                if (city.getCid() == cid) {
                    city.setSelected(true);
                    break;
                }
            }

            cityList.addAll(response);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCityFailure(String msg) {

    }
}
