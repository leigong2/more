package com.moreclub.moreapp.information.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.presenter.IUserPartyListLoader;
import com.moreclub.moreapp.information.presenter.UserPartyListLoader;
import com.moreclub.moreapp.information.ui.activity.ActivitiesActivity;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.main.model.MerchantActivity;
import com.moreclub.moreapp.ucenter.model.BaseUserDetails;
import com.moreclub.moreapp.ucenter.ui.adapter.UserDetailsListAdapter;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017-05-18.
 */

public class UserPartyFragment extends BaseFragment implements IUserPartyListLoader.LoadUserPartyListBinder {

    @BindView(R.id.xrv_activities_fragment)
    XRecyclerView xrvActivitiesFragment;
    @BindView(R.id.no_data_view)
    View no_data_view;
    Unbinder unbinder;
    private ArrayList<BaseUserDetails> mData;
    private Integer mPn = 0;
    private Integer mPs = 20;
    private Long mUid = MoreUser.getInstance().getUid();
    private UserDetailsListAdapter mPartyAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activities_fragment;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, rootView);
        setupView();
        mPresenter = LogicProxy.getInstance().bind(IUserPartyListLoader.class, UserPartyFragment.this);
        ((UserPartyListLoader) mPresenter).onLoadPartyList(((ActivitiesActivity) getActivity()).mid, mUid, mPn, mPs);
    }

    private void setupView() {
        mData = new ArrayList<>();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        xrvActivitiesFragment.setLayoutManager(lm);
        xrvActivitiesFragment.setPullRefreshEnabled(true);
        xrvActivitiesFragment.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvActivitiesFragment.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        mPartyAdapter = new UserDetailsListAdapter(getContext(), mData);
        xrvActivitiesFragment.setAdapter(mPartyAdapter);
        xrvActivitiesFragment.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPn = 0;
                ((UserPartyListLoader) mPresenter).onLoadPartyList(((ActivitiesActivity) getActivity()).mid, mUid, mPn, mPs);
            }

            @Override
            public void onLoadMore() {
                mPn++;
                ((UserPartyListLoader) mPresenter).onLoadPartyList(((ActivitiesActivity) getActivity()).mid, mUid, mPn, mPs);
            }
        });
    }

    private void onLoadComplete(Integer index) {
        if (index == 0) {
            mData.clear();
            xrvActivitiesFragment.refreshComplete();
        } else {
            xrvActivitiesFragment.loadMoreComplete();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onGetPartyListResponse(RespDto<List<MerchantActivity>> response) {
        onLoadComplete(mPn);
        List<MerchantActivity> data = response.getData();
        if (data != null && data.size() > 0) {
            if (mPn == 0) {
                mData.clear();
            }
            for (int i = 0; i < data.size(); i++) {
                MerchantActivity item = data.get(i);
                if (item.getType() == 2 || item.getType() == 6)
                    mData.add(item);
                item.setListType(2);
            }
        } else {
            xrvActivitiesFragment.loadMoreComplete();
        }
        if (mData.isEmpty()) {
            no_data_view.setVisibility(View.VISIBLE);
            xrvActivitiesFragment.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.GONE);
            xrvActivitiesFragment.setVisibility(View.VISIBLE);
        }
        mPartyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPartyListFaiure(String msg) {
        onLoadComplete(mPn);
    }
}
