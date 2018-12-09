package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.ui.adapter.MChannelBaseAdapter;
import com.moreclub.moreapp.main.ui.fragment.MChannelFragment;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.Topics;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/9/7.
 */

public class MChannelTopicDetailsView extends BaseChannelView {

    private View header;

    public MChannelTopicDetailsView(BaseFragment fragment, Context cxt, String title, String des, int tid) {
        mContext = cxt;
        this.fragment = (MChannelFragment) fragment;
        dataList = new ArrayList<>();
        topicTitle = title;
        topicD = des;
        topicId = tid;
        initHeader();
        adapter = new MChannelBaseAdapter(mContext, R.layout.main_channel_item, dataList, header);
    }

    @Override
    protected void initHeader() {
        header = LayoutInflater.from(mContext).inflate(R.layout.topic_details_header,
                null, false);
        topicDes = (TextView) header.findViewById(R.id.topic_des);
        topicName = (TextView) header.findViewById(R.id.topic_name);
        topicDes.setText(topicD);
        topicName.setText(topicTitle);
        if (xRecyclerView instanceof com.jcodecraeer.xrecyclerview.XRecyclerView) {
            ((com.jcodecraeer.xrecyclerview.XRecyclerView) xRecyclerView).addHeaderView(header);
        } else {

        }
    }

    @Override
    public void loadData() {
        Callback<RespDto<Topics>> callback2 = new Callback<RespDto<Topics>>() {
            @Override
            public void onResponse(Call<RespDto<Topics>> call, Response<RespDto<Topics>> response) {
                noDataView.setVisibility(View.GONE);
                if (response != null && response.body() != null && response.body().getData() != null) {
                    Topics topics = response.body().getData();
                    topicDes.setText(topics.getTopicRemark());
                    Log.i("zune:", "topic = ");
                    onLoadComplete(page);
                    Topics result = response.body().getData();
                    List<MainChannelItem> lists = result.getLists();
                    if (xRecyclerView instanceof XRecyclerView) {
                        if (lists != null && lists.size() > 0) {
                            ((com.jcodecraeer.xrecyclerview.XRecyclerView) xRecyclerView).setLoadingMoreEnabled(true);
                            dataList.addAll(lists);
                            adapter.notifyDataSetChanged();
                            if (lists.size() <= PAGE_SIZE) {
                                ((com.jcodecraeer.xrecyclerview.XRecyclerView) xRecyclerView).loadMoreComplete();
                            }
                        } else {
                            ((com.jcodecraeer.xrecyclerview.XRecyclerView) xRecyclerView).loadMoreComplete();
                        }
                    } else {
                        if (lists != null && lists.size() > 0) {
                            if (page == PAGE_START) {
                                dataList.clear();
                            }
                            dataList.addAll(lists);
                            adapter.notifyDataSetChanged();
                            if (lists.size() <= PAGE_SIZE) {
                                ((RecyclerViewUpRefresh) xRecyclerView).loadMoreComplete();
                            }
                        } else {
                            ((RecyclerViewUpRefresh) xRecyclerView).loadMoreComplete();
                        }
                    }
                    if (dataList.isEmpty()) {
                        xRecyclerView.setVisibility(View.GONE);
                        noDataView.setVisibility(View.VISIBLE);
                    } else {
                        xRecyclerView.setVisibility(View.VISIBLE);
                        noDataView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<Topics>> call, Throwable t) {
                noDataView.setVisibility(View.VISIBLE);
                xRecyclerView.setVisibility(View.GONE);
            }
        };
        ApiInterface.ApiFactory.createApi().
                onLoadTopicList(page, PAGE_SIZE, fragment.cityID, topicId, MoreUser.getInstance().getUid()).enqueue(callback2);

    }

}
