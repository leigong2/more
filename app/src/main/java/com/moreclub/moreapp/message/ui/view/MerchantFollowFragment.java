package com.moreclub.moreapp.message.ui.view;

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
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.FollowsUser;
import com.moreclub.moreapp.message.presenter.IMessageFollowMerchantListLoader;
import com.moreclub.moreapp.message.presenter.MessageFollowMerchantListLoader;
import com.moreclub.moreapp.message.ui.adapter.MerchantFollowAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/10.
 */

public class MerchantFollowFragment extends BaseFragment implements IMessageFollowMerchantListLoader.LoaderMessageFollowMerchantDataBinder {

    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;
    public  int followType;
    @BindView(R.id.myorder_all_rv) XRecyclerView recyclerView;
    private MerchantFollowAdapter merchantFollowAdapter;
    private List<FollowsUser> followList;

    @Override
    protected int getLayoutResource() {
        return R.layout.my_order_all_fragment;
    }

    @Override
    protected Class getLogicClazz() {
        return IMessageFollowMerchantListLoader.class;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        followList = new ArrayList<>();
        mPresenter = LogicProxy.getInstance().bind(IMessageFollowMerchantListLoader.class, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        merchantFollowAdapter = new MerchantFollowAdapter(getActivity(), R.layout.follow_list_user_item, followList);
        merchantFollowAdapter.setHasHeader(true);

        merchantFollowAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                FollowsUser collect = (FollowsUser) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, UserDetailV2Activity.class);
                intent.putExtra("toUid", "" + collect.getUid());
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
        recyclerView.setAdapter(merchantFollowAdapter);
        onLoadCollects();
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
//        recyclerView.setRefreshing(true);
    }

    private void onLoadCollects() {
        String uid = "" + MoreUser.getInstance().getUid();
        ((MessageFollowMerchantListLoader) mPresenter).loadFollowsMerchant(uid, "1", "" + page,followType);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            if (!followList.isEmpty()) {
                followList.clear();
                merchantFollowAdapter.notifyDataSetChanged();
            }
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    @Override
    public void onMessageFollowMerchantResponse(RespDto response) {
        onLoadComplete(page);

        ArrayList<FollowsUser> list = (ArrayList<FollowsUser>) response.getData();

        if (list != null) {
            for(int i=0;i<list.size();i++){
                FollowsUser item = list.get(i);
                item.setFollowType(followType);
            }
            followList.addAll(list);
            merchantFollowAdapter.notifyDataSetChanged();
        } else {
            if (page>0)
                recyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onMessageFollowMerchantFailure(String msg) {
        onLoadComplete(page);
    }
}
