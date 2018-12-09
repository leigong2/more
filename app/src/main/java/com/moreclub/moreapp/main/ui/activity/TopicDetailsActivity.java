package com.moreclub.moreapp.main.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.presenter.IMChannelRecommListLoader;
import com.moreclub.moreapp.main.presenter.MChannelRecommListLoader;
import com.moreclub.moreapp.main.ui.adapter.MChannelAdapter;
import com.moreclub.moreapp.ucenter.model.Topics;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/8/28.
 */

public class TopicDetailsActivity extends BaseListActivity implements IMChannelRecommListLoader.LoadMChannelRecommListDataBinder {
    protected final static int PAGE_START = 0;
    protected final static int PAGE_SIZE = 10;
    protected int page = PAGE_START;
    protected int pages = 0;
    public final static String KEY_CITY_ID = "city.id";
    @BindView(R.id.nav_back)
    ImageButton nav_back;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.recyclerView)
    XRecyclerView xRecyclerView;

    public TextView topicDes;
    public TextView topicName;

    public MChannelAdapter adapter;
    public ArrayList<MainChannelItem> dataList;
    private int cityID;
    private String topicTitle;
    private int topicId;
    private int pn;
    private int ps = 10;

    @Override
    protected int getLayoutResource() {
        return R.layout.topic_details_activity;
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
    protected void onReloadData() {

    }

    @Override
    protected Class getLogicClazz() {
        return IMChannelRecommListLoader.class;
    }

    private void initData() {
        dataList = new ArrayList<>();
        cityID = PrefsUtils.getInt(this, KEY_CITY_ID, 2);
        topicTitle = getIntent().getStringExtra("topicTitle");
        topicId = getIntent().getIntExtra("topicId", -1);
    }

    private void setupViews() {

        if (topicTitle == null) {
            activityTitle.setText("");
        } else {
            activityTitle.setText(topicTitle);
        }
        View header = LayoutInflater.from(this).inflate(R.layout.topic_details_header,
                (ViewGroup) findViewById(android.R.id.content), false);

        topicDes = (TextView) header.findViewById(R.id.topic_des);
        topicName = (TextView) header.findViewById(R.id.topic_name);
        topicName.setText("#" + topicTitle + "#");
        xRecyclerView.addHeaderView(header);
        adapter = new MChannelAdapter(this, R.layout.main_channel_item, dataList,null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter.setHasHeader(true);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                xRecyclerView.reset();
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });

        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadData() {
        ((MChannelRecommListLoader) mPresenter).loadMChannelRecomm(MoreUser.getInstance().getUid(), cityID, page, PAGE_SIZE);
        ((MChannelRecommListLoader) mPresenter).onLoadTopicList(pn, ps, cityID, topicId, MoreUser.getInstance().getUid());
    }


    public void reloadData() {
        page = PAGE_START;
        xRecyclerView.reset();
        loadData();
    }


    @Override
    public void onMChannelRecommResponse(RespDto<ArrayList<MainChannelItem>> body) {

    }

    @Override
    public void onMChannelRecommFailure(String message) {
        if ("401".equals(message)) {
            AppUtils.pushLeftActivity(this, UseLoginActivity.class);
            return;
        }
        onLoadComplete(page);
    }

    @Override
    public void onLoadTopicListResponse(RespDto<Topics> response) {
        if (response == null || response.getData() == null) return;
        onLoadComplete(page);
        Topics result = response.getData();
        topicDes.setText(result.getTopicRemark());
        List<MainChannelItem> lists = result.getLists();
        if (result != null && lists.size() > 0) {
            dataList.addAll(lists);
            adapter.notifyDataSetChanged();
            if (lists.size() < PAGE_SIZE) {
                xRecyclerView.loadMoreComplete();
            }
        } else {
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onLoadTopicListFailure(String message) {
        Log.i("zune:", "msg = " + message);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            xRecyclerView.refreshComplete();
        } else
            xRecyclerView.loadMoreComplete();
    }
}
