package com.moreclub.moreapp.main.ui.view;

import android.content.Context;

import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.presenter.IMChannelSameCityListLoader;
import com.moreclub.moreapp.main.presenter.MChannelSameCityListLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelBaseAdapter;
import com.moreclub.moreapp.main.ui.fragment.MChannelFragment;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelSameCityView extends BaseChannelView implements
        IMChannelSameCityListLoader.LoadMChannelSameCityListDataBinder {

    public MChannelSameCityView(BaseFragment fragment, Context cxt) {
        mContext = cxt;
        this.fragment = (MChannelFragment) fragment;
        mPresenter = LogicProxy.getInstance().bind(IMChannelSameCityListLoader.class, this);
        dataList = new ArrayList<>();
        adapter = new MChannelBaseAdapter(mContext, R.layout.main_channel_item, dataList,null);
    }

    public void loadData() {
        ((MChannelSameCityListLoader) mPresenter).loadMChannelSameCity(MoreUser.getInstance().getUid(), fragment.cityID, page, PAGE_SIZE);
    }

    @Override
    public void onMChannelSameCityResponse(RespDto<ArrayList<MainChannelItem>> body) {
        onLoadComplete(page);
        ArrayList<MainChannelItem> result = body.getData();
        setDataToView(result);
    }

    @Override
    public void onMChannelSameCityFailure(String message) {
        if ("401".equals(message)) {
            AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
            return;
        }
        onLoadComplete(page);
    }

    @Override
    protected void initHeader() {

    }
}
