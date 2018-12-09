package com.moreclub.moreapp.message.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.message.presenter.IMessageFollowListLoader;
import com.moreclub.moreapp.message.ui.view.MerchantFollowFragment;
import com.moreclub.moreapp.message.ui.view.UserFollowFragment;
import com.moreclub.moreapp.ucenter.ui.fragment.CollectPageApdater;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/6/1.
 */

public class MessageFollowListActivity extends BaseListActivity{

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.mycollect_strip) NavigationTabStrip tabStrip;
    @BindView(R.id.mycollect_container) ViewPager viewPager;

    private MerchantFollowFragment merchantFollowFragment;
    private boolean isPackageFirstSelect = false;
    private CollectPageApdater pagerAdapter;

    private int followType;
    @Override
    protected int getLayoutResource() {
        return R.layout.message_follow_list_activity;
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

    }

    @Override
    protected Class getLogicClazz() {
        return IMessageFollowListLoader.class;
    }

    private void initData() {
        followType = getIntent().getIntExtra("followType",0);
    }

    private void setupViews() {
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        if (followType==0){
            activityTitle.setText("粉丝");
        } else {
            activityTitle.setText("关注");
        }
        List<Fragment> fragmentList = new ArrayList<>();
        UserFollowFragment firstFragment = new UserFollowFragment();
        firstFragment.followType=followType;
        fragmentList.add(firstFragment);

        merchantFollowFragment = new MerchantFollowFragment();
        merchantFollowFragment.followType=followType;
        fragmentList.add(merchantFollowFragment);

        pagerAdapter = new CollectPageApdater(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabStrip.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 1:
                        if (!isPackageFirstSelect) {
                            isPackageFirstSelect = true;
                            merchantFollowFragment.onLoadData();
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
