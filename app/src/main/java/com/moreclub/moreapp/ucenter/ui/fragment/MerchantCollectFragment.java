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
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;
import com.moreclub.moreapp.ucenter.presenter.IUserCollectMerchantLoader;
import com.moreclub.moreapp.ucenter.presenter.UserCollectMerchantLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.CollectMerchantAdapter;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/10.
 */

public class MerchantCollectFragment extends BaseFragment implements IUserCollectMerchantLoader.LoadUserCollectMerchantDataBinder {


    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;

    @BindView(R.id.myorder_all_rv)
    XRecyclerView recyclerView;
    @BindView(R.id.no_data_view)
    View no_data_view;

    private CollectMerchantAdapter merchantAdapter;
    private List<CollectlistInfo> collectList;

    @Override
    protected int getLayoutResource() {
        return R.layout.my_order_all_fragment;
    }

    @Override
    protected Class getLogicClazz() {
        return IUserCollectMerchantLoader.class;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        collectList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        merchantAdapter = new CollectMerchantAdapter(getActivity(), R.layout.main_bar_item, collectList);
        merchantAdapter.setHasHeader(true);
        merchantAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                CollectlistInfo collect = (CollectlistInfo) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", "" + collect.getRelationId());
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
        recyclerView.setAdapter(merchantAdapter);
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

    @Override
    protected void onInitData2Remote() {
        super.onInitData2Remote();
        onLoadData();
    }

    public void onLoadData() {
        recyclerView.refresh();
    }

    private void onLoadCollects() {
        String uid = "" + MoreUser.getInstance().getUid();
        ((UserCollectMerchantLoader) mPresenter).loadMerchant(uid, "3", "" + page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            if (!collectList.isEmpty()) {
                collectList.clear();
                merchantAdapter.notifyDataSetChanged();
            }
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    @Override
    public void onUserCollectMerchantFailure(String msg) {
        Logger.d("error" + msg);

        onLoadComplete(page);

    }

    @Override
    public void onUserCollectMerchantResponse(RespDto response) {
        onLoadComplete(page);

        ArrayList<CollectlistInfo> list = (ArrayList<CollectlistInfo>) response.getData();
        if (list != null) {
            collectList.addAll(list);
            merchantAdapter.notifyDataSetChanged();
        }
        if (collectList.isEmpty()) {
            no_data_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
