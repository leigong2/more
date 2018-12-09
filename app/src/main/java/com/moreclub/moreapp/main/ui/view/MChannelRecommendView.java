package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.util.Log;

import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.presenter.IMChannelRecommListLoader;
import com.moreclub.moreapp.main.presenter.MChannelRecommListLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelBaseAdapter;
import com.moreclub.moreapp.main.ui.fragment.MChannelFragment;
import com.moreclub.moreapp.ucenter.model.Topics;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelRecommendView extends BaseChannelView implements IMChannelRecommListLoader.LoadMChannelRecommListDataBinder {

    public MChannelRecommendView(BaseFragment fragment, Context cxt) {
        mContext = cxt;
        this.fragment = (MChannelFragment) fragment;
        mPresenter = LogicProxy.getInstance().bind(IMChannelRecommListLoader.class, this);
        dataList = new ArrayList<>();
        adapter = new MChannelBaseAdapter(mContext, R.layout.main_channel_item, dataList,null);
    }

    public void loadData() {
        ((MChannelRecommListLoader) mPresenter).loadMChannelRecomm(MoreUser.getInstance().getUid(), fragment.cityID, page, PAGE_SIZE);
        Log.d("dddd", "MChannelAttentionView");
    }

    @Override
    public void onMChannelRecommResponse(RespDto<ArrayList<MainChannelItem>> body) {
        onLoadComplete(page);
        ArrayList<MainChannelItem> result = body.getData();
        setDataToView(result);
    }

    @Override
    public void onMChannelRecommFailure(String message) {
        if ("401".equals(message)) {
            AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
            return;
        }
        onLoadComplete(page);
    }

    @Override
    public void onLoadTopicListResponse(RespDto<Topics> body) {

    }

    @Override
    public void onLoadTopicListFailure(String message) {

    }

    @Override
    protected void initHeader() {

    }
}
