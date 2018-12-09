package com.moreclub.moreapp.ucenter.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;
import com.moreclub.moreapp.ucenter.presenter.IUserCollectHeadlineLoader;
import com.moreclub.moreapp.ucenter.presenter.UserCollectHeadlineLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.CollectHeadlineAdapter;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-05-26.
 */

public class HeadlineCollectFragment extends BaseFragment implements IUserCollectHeadlineLoader.LoadUserCollectHeadlineDataBinder {


    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;
    private ArrayList<CollectlistInfo> collectList;
    private CollectHeadlineAdapter headlineAdapter;

    @BindView(R.id.myorder_all_rv)
    XRecyclerView recyclerView;
    @BindView(R.id.no_data_view)
    View no_data_view;


    @Override
    protected int getLayoutResource() {
        return R.layout.my_order_all_fragment;
    }

    @Override
    protected Class getLogicClazz() {
        return IUserCollectHeadlineLoader.class;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        collectList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        headlineAdapter = new CollectHeadlineAdapter(getActivity(), R.layout.headline_collect_item, collectList);
        headlineAdapter.setHasHeader(true);
        headlineAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                CollectlistInfo collect = (CollectlistInfo) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, MChannelDetailsAcitivy.class);
                intent.putExtra("sid", collect.getRelationId());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        recyclerView.setAdapter(headlineAdapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                recyclerView.reset();
                onLoadCollects();
            }

            @Override
            public void onLoadMore() {
                page++;
                onLoadCollects();
            }
        });
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            if (!collectList.isEmpty()) {
                collectList.clear();
                headlineAdapter.notifyDataSetChanged();
            }
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    @Override
    protected void onInitData2Remote() {
        super.onInitData2Remote();
        onLoadData();
    }

    private void onLoadCollects() {
        String uid = "" + MoreUser.getInstance().getUid();
        /**zune:type = 1资讯,2活动,3商家**/
        ((UserCollectHeadlineLoader) mPresenter).loadHeadline(uid, "1", "" + page);
    }

    public void onLoadData() {
        if (recyclerView != null)
            recyclerView.refresh();
    }

    @Override
    public void onUserCollectHeadlineResponse(RespDto response) {
        onLoadComplete(page);

        ArrayList<CollectlistInfo> list = (ArrayList<CollectlistInfo>) response.getData();
        if (list != null) {
            collectList.addAll(list);
            headlineAdapter.notifyDataSetChanged();
        }
        if (collectList.isEmpty()) {
            no_data_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUserCollectHeadlineFailure(String msg) {
        Logger.d("error" + msg);
        onLoadComplete(page);
    }
}
