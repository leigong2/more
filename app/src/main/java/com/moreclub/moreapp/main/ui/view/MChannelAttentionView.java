package com.moreclub.moreapp.main.ui.view;

import android.content.Context;

import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.presenter.IMChannelAttentionListLoader;
import com.moreclub.moreapp.main.presenter.MChannelAttentionListLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelBaseAdapter;
import com.moreclub.moreapp.main.ui.fragment.MChannelFragment;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/25.
 */

public class MChannelAttentionView extends BaseChannelView implements IMChannelAttentionListLoader.LoadMChannelAttentionListDataBinder{

    public MChannelAttentionView(BaseFragment fragment,Context cxt){
        mContext = cxt;
        this.fragment = (MChannelFragment) fragment;
        mPresenter = LogicProxy.getInstance().bind(IMChannelAttentionListLoader.class, this);
        dataList = new ArrayList<>();
        adapter = new MChannelBaseAdapter(mContext,R.layout.main_channel_item,dataList,null);
    }

    public void loadData(){
        ((MChannelAttentionListLoader)mPresenter).loadMChannelAttention(MoreUser.getInstance().getUid(),page,PAGE_SIZE);
    }

    @Override
    public void onMChannelAttentionResponse(RespDto<ArrayList<MainChannelItem>> body) {
        onLoadComplete(page);
        ArrayList<MainChannelItem> result = body.getData();
        setDataToView(result);
    }

    @Override
    public void onMChannelAttentionFailure(String message) {
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
