package com.moreclub.moreapp.information.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.information.ui.adapter.ActivityPagerAdapter;
import com.moreclub.moreapp.information.ui.fragment.LiveFragment;
import com.moreclub.moreapp.information.ui.fragment.PartyFragment;
import com.moreclub.moreapp.information.ui.fragment.UserLiveFragment;
import com.moreclub.moreapp.information.ui.fragment.UserPartyFragment;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

public class ActivitiesActivity extends BaseListActivity {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.mycollect_container)
    ViewPager viewPager;
    @BindView(R.id.mycollect_strip)
    NavigationTabStrip tabStrip;
    private PartyFragment mPartyFragment;
    private LiveFragment mLiveFragment;
    private UserLiveFragment mUserLiveFragment;
    private UserPartyFragment mUserPartyFragment;
    private PagerAdapter mPagerAdapter;
    public int mid;

    @Override
    protected int getLayoutResource() {
        return R.layout.activities_activity;
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
        setupView();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        setupView();
    }

    private void setupView() {
        mid = getIntent().getIntExtra("mid", -1);
        List<Fragment> fragments = new ArrayList<>();
        activityTitle.setText("热点Live");
        if (mid > 0) {
            mUserLiveFragment = new UserLiveFragment();
            fragments.add(mUserLiveFragment);
            mUserPartyFragment = new UserPartyFragment();
            fragments.add(mUserPartyFragment);
        } else {
            mLiveFragment = new LiveFragment();
            fragments.add(mLiveFragment);
            mPartyFragment = new PartyFragment();
            fragments.add(mPartyFragment);
        }
        mPagerAdapter = new ActivityPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabStrip.setViewPager(viewPager);
    }

    @OnClick({R.id.nav_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                ActivitiesActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
        }
    }
}
