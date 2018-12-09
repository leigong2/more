package com.moreclub.moreapp.information.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.presenter.IPartyListLoader;
import com.moreclub.moreapp.information.presenter.PartyListLoader;
import com.moreclub.moreapp.information.ui.adapter.ActivitiesAdapter;
import com.moreclub.moreapp.information.ui.view.XRecyclerView;
import com.moreclub.moreapp.util.MoreUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017-05-18.
 */

public class PartyFragment extends BaseFragment implements IPartyListLoader.LoadPartyListBinder {

    @BindView(R.id.xrv_activities_fragment)
    XRecyclerView xrvActivitiesFragment;
    @BindView(R.id.no_data_view)
    View no_data_view;
    private ActivityData mData;
    Unbinder unbinder;
    private int mType = 2;
    private Integer mPn = 1;
    private Integer mPs = 20;
    private Long mUid = MoreUser.getInstance().getUid();
    private String mCityCode;
    private static final String KEY_CITY_CODE = "city.code";
    private RecyclerView.Adapter mPartyAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activities_fragment;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, rootView);
        mCityCode = PrefsUtils.getString(getActivity(), KEY_CITY_CODE, "cd");
        mPresenter = LogicProxy.getInstance().bind(IPartyListLoader.class, PartyFragment.this);
        ((PartyListLoader) mPresenter).onLoadPartyList(mCityCode, mPn, mPs, mType, mUid);
    }

    private void setupView() {
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        xrvActivitiesFragment.setLayoutManager(lm);
        xrvActivitiesFragment.setPullRefreshEnabled(true);
        xrvActivitiesFragment.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvActivitiesFragment.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        if(mData!=null) {
            mPartyAdapter = new ActivitiesAdapter(getContext(), R.layout.activities_item, mData.getList());
            xrvActivitiesFragment.setAdapter(mPartyAdapter);
        }
        xrvActivitiesFragment.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPn = 1;
                ((PartyListLoader) mPresenter).onLoadPartyList(mCityCode, mPn, mPs, mType, mUid);
            }

            @Override
            public void onLoadMore() {
                mPn++;
                if (mPn < mData.getTotal() / mPs + 1) {
                    ((PartyListLoader) mPresenter).onLoadPartyList(mCityCode, mPn, mPs, mType, mUid);
                } else if (mPn == mData.getTotal() / mPs + 1) {
                    int pageSize = mData.getTotal() % mPs;
                    ((PartyListLoader) mPresenter).onLoadPartyList(mCityCode, mPn, pageSize, mType, mUid);
                } else {
                    Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                    onLoadComplete(mPn);
                }
            }
        });
    }

    private void onLoadComplete(Integer index) {
        if (index == 1) {
            mData = null;
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
    public void onGetPartyListResponse(RespDto<ActivityData> response) {
        onLoadComplete(mPn);
        ActivityData data = response.getData();
        if (mData == null) {
            mData = data;
            setupView();
        } else {
            mData.getList().addAll(data.getList());
            mPartyAdapter.notifyDataSetChanged();
        }
        if (mData == null || mData.getList() == null || mData.getList().isEmpty()) {
            no_data_view.setVisibility(View.VISIBLE);
            xrvActivitiesFragment.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.GONE);
            xrvActivitiesFragment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPartyListFaiure(String msg) {
        onLoadComplete(mPn);
        if (mData == null || mData.getList() == null || mData.getList().isEmpty()) {
            no_data_view.setVisibility(View.VISIBLE);
            xrvActivitiesFragment.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.GONE);
            xrvActivitiesFragment.setVisibility(View.VISIBLE);
        }
    }
}
