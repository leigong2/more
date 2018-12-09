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
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;
import com.moreclub.moreapp.ucenter.presenter.IUserCollectPackageLoader;
import com.moreclub.moreapp.ucenter.presenter.UserCollectPackageLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.CollectListAdapter;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/4/10.
 */

public class PackageCollectFragment extends BaseFragment implements IUserCollectPackageLoader.LoadUserCollectPackageDataBinder{

    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;

    @BindView(R.id.myorder_all_rv)
    XRecyclerView recyclerView;
    @BindView(R.id.no_data_view)
    View no_data_view;

    private CollectListAdapter packageAdapter;
    private List<CollectlistInfo> collectList;

    @Override
    protected int getLayoutResource() {
        return R.layout.my_order_all_fragment;
    }


    @Override
    protected void onInitView(Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        collectList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        packageAdapter = new CollectListAdapter(getActivity(), R.layout.package_super_main_list_item, collectList);
        packageAdapter.setHasHeader(true);
        packageAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                CollectlistInfo collect = (CollectlistInfo) o;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, PackageDetailsActivity.class);
                intent.putExtra("pid", ""+collect.getRelationId());
                intent.putExtra("mid", ""+collect.getMid());
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
        recyclerView.setAdapter(packageAdapter);
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
    protected Class getLogicClazz() {
        return IUserCollectPackageLoader.class;
    }

//    @Override
//    protected void onInitData2Remote() {
//        super.onInitData2Remote();
//        //onLoadOrders();
//
//    }

    public void onLoadData() {
        recyclerView.refresh();
    }

    private void onLoadCollects() {
        String uid = ""+MoreUser.getInstance().getUid();
        ((UserCollectPackageLoader)mPresenter).loadPackage(uid,"4",""+page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            if (!collectList.isEmpty()) {
                collectList.clear();
                packageAdapter.notifyDataSetChanged();
            }
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    @Override
    public void onUserCollectPackageFailure(String msg) {
        Logger.d("error" + msg);

        onLoadComplete(page);
    }

    @Override
    public void onUserCollectPackageResponse(RespDto response) {
        onLoadComplete(page);

        ArrayList<CollectlistInfo> list = (ArrayList<CollectlistInfo>) response.getData();
        if (list != null) {
            collectList.addAll(list);
            packageAdapter.notifyDataSetChanged();
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
