package com.moreclub.moreapp.main.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.moreclub.common.magicindicator.MagicIndicator;
import com.moreclub.common.magicindicator.ViewPagerHelper;
import com.moreclub.common.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.moreclub.common.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.moreclub.common.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.moreclub.common.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.presenter.IMChannelFragmentDataLoader;
import com.moreclub.moreapp.main.presenter.ISpaceV3DataLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelPagerAdapter;
import com.moreclub.moreapp.main.ui.view.BaseTabView;
import com.moreclub.moreapp.main.ui.view.TotalCitySpaceV3View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-08-0008.
 */

public class SpaceFragment extends BaseFragment implements ISpaceV3DataLoader.LoadSpaceViewV3DataBinder {

    private FragmentActivity mContext;
    private ArrayList<Object> dataList;
    private TextView activityTitle;
    private NavigationTabStrip mychannelStrip;
    private MagicIndicator channelTab;
    private ViewPager viewPager;
    private ArrayList<BaseTabView> viewList;
    private ArrayList<String> tabTitles;
    private MChannelPagerAdapter pagerAdapter;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    public TotalCitySpaceV3View mTotalCitySpaceV3View;
    private ArrayList<View> pagerViews;


    @Override
    protected int getLayoutResource() {
        return R.layout.space_v3_layout;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mContext = getActivity();
        dataList = new ArrayList<>();
        mPresenter = LogicProxy.getInstance().bind(IMChannelFragmentDataLoader.class, this);
        setupViews(rootView);
    }

    private void setupViews(View view) {
        viewList = new ArrayList<>();
        tabTitles = new ArrayList<>();
        activityTitle = (TextView) view.findViewById(R.id.activity_title);
        mychannelStrip = (NavigationTabStrip) view.findViewById(R.id.mychannel_strip);
        channelTab = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        viewPager = (ViewPager) view.findViewById(R.id.mychannel_container);
        pagerViews = new ArrayList<>();
        pagerAdapter = new MChannelPagerAdapter(mContext, pagerViews);
        activityTitle.setText("M-Space");
        viewPager.setAdapter(pagerAdapter);
        loadTab();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        channelTab.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setText(tabTitles.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                linePagerIndicator.setColors(Color.BLACK);
                return linePagerIndicator;
            }
        };
        commonNavigator.setAdapter(commonNavigatorAdapter);
        channelTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(channelTab, viewPager);
    }

    private void loadTab() {
        mTotalCitySpaceV3View = new TotalCitySpaceV3View(mContext);
        viewList.add(mTotalCitySpaceV3View);
        tabTitles.add("   同城   ");
        for (int i = 0; i < viewList.size(); i++) {
            View temp = viewList.get(i).createView();
            pagerViews.add(temp);
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadSpaceV3TabsResponse(RespDto respDto) {

    }

    @Override
    public void onLoadSpaceV3TabsFailure(String msg) {

    }

}
