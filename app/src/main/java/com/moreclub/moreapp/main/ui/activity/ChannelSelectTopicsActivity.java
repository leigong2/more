package com.moreclub.moreapp.main.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.main.model.event.ChannelTopicEvent;
import com.moreclub.moreapp.main.presenter.ChannelTopicLoader;
import com.moreclub.moreapp.main.presenter.IChannelTopicLoader;
import com.moreclub.moreapp.main.ui.adapter.SelectTopicAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Captain on 2017/8/26.
 */

public class ChannelSelectTopicsActivity extends BaseActivity implements IChannelTopicLoader.LoaderChannelTopicDataBinder {
    private final static String KEY_CITY_ID = "city.id";
    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    @BindView(R.id.recyclerView) XRecyclerView recyclerView;
    @BindView(R.id.activity_title)
    TextView activity_title;
    private SelectTopicAdapter adapter;
    private ArrayList<Topic> dataList;
    private int cityID;
    @Override
    protected int getLayoutResource() {
        return R.layout.channel_select_topics_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);

        initData();
        loadData();
        setupViews();
    }

    @Override
    protected Class getLogicClazz() {
        return IChannelTopicLoader.class;
    }

    private void loadData() {
        ((ChannelTopicLoader)mPresenter).loadTopic(cityID);
    }

    private void initData() {
        cityID = PrefsUtils.getInt(this, KEY_CITY_ID, 2);
        dataList = new ArrayList<>();
    }

    @OnClick(R.id.nav_back)
    void onClickBack() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    private void setupViews() {
        activity_title.setText("添加话题");
        adapter = new SelectTopicAdapter(this, R.layout.modify_auto_sign_item, dataList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Topic item = (Topic) o;
                EventBus.getDefault().post(new ChannelTopicEvent(item));
                finish();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        recyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
    }

    @Override
    public void onTopicListResponse(RespDto<List<Topic>> response) {
        onLoadComplete(page);
        if (page==0) {
            Topic first = new Topic();
            first.setName("不使用话题");
            first.setTid(-1);
            dataList.add(first);
        }
        StringBuilder topics = new StringBuilder();
        ArrayList<Topic> data = (ArrayList<Topic>) response.getData();
        if (data!=null&&data.size()>0){
            dataList.addAll(data);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onTopicListFailure(String msg) {
        onLoadComplete(page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }
}
