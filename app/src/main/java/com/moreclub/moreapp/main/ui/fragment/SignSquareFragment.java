package com.moreclub.moreapp.main.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.SignSpaceList;
import com.moreclub.moreapp.main.presenter.IMerchantSignInteractSquareLoader;
import com.moreclub.moreapp.main.presenter.MerchantSignInteractSquareLoader;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.utils.ReflectionUtil;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-06-19.
 */

public class SignSquareFragment extends BaseFragment implements IMerchantSignInteractSquareLoader.LoadMerchantSignInteractSquareDataBinder {

    @BindView(R.id.xRecyclerView)
    public XRecyclerView xRecyclerView;
    Unbinder unbinder;

    private ArrayList<SignSpaceList.SininMoreSpacesBean> dataList;
    public BasePresenter mPresenter;

    public SignInteractSquareAdapter adapter;
    private String mid;
    private String logo;
    private String desc;
    private String name;
    private int mPs = 20;
    private int mPn = 0;
    private View header;
    public List<SignSpaceList.SininMoreSpacesBean.SignInteractionDtoBean> newSignInterses;

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_square_fragment;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, rootView);
        Bundle intent = getArguments();
        if (intent!=null) {
            mid = intent.getString("mid");
            logo = intent.getString("logo");
            desc = intent.getString("desc");
            name = intent.getString("name");
        }
        newSignInterses = new ArrayList<>();
        header = View.inflate(getContext(), R.layout.sign_square_header, null);
        xRecyclerView.addHeaderView(header);
        try {
            ArrowRefreshHeader mRefreshHeader = (ArrowRefreshHeader) ReflectionUtil.getValue(xRecyclerView, "mRefreshHeader");
            mRefreshHeader.setArrowImageView(R.drawable.abc_icon_down_arrow);
            LinearLayout first = (LinearLayout) mRefreshHeader.getChildAt(0);
            RelativeLayout second = (RelativeLayout) first.getChildAt(0);
            LinearLayout third = (LinearLayout) second.getChildAt(0);
            TextView forth = (TextView) third.getChildAt(0);
            forth.setTextColor(Color.WHITE);
            xRecyclerView.setRefreshHeader(mRefreshHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        initData();
        mPresenter = LogicProxy.getInstance().bind(IMerchantSignInteractSquareLoader.class, SignSquareFragment.this);
        loadData();
    }

    private void initData() {
        TextView merchant_name = (TextView) header.findViewById(R.id.merchant_name);
        CircleImageView civ_sign_space_icon = (CircleImageView) header.findViewById(R.id.civ_sign_space_icon);
        TextView tv_desc = (TextView) header.findViewById(R.id.tv_desc);
        merchant_name.setText(name);
        tv_desc.setText(desc);
        Glide.with(getContext())
                .load(logo)
                .dontAnimate()
                .into(civ_sign_space_icon);
        dataList = new ArrayList<>();
        adapter = new SignInteractSquareAdapter(getContext(), dataList, newSignInterses, mid);
        xRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPn = 0;
                newSignInterses.clear();
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPn++;
                loadData();
            }
        });
    }

    public void loadData() {
        if (mid == null)
            return;
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() > 0) {
            ((MerchantSignInteractSquareLoader) mPresenter).onLoadSignList(mid, "" + MoreUser.getInstance().getUid(), mPn + "", mPs + "");
        } else {
            ((MerchantSignInteractSquareLoader) mPresenter).onLoadSignList(mid, "0", mPn + "", mPs + "");
        }
    }

    private void onLoadComplete(Integer index) {
        if (xRecyclerView != null)
            if (index == 0) {
                dataList.clear();
                xRecyclerView.refreshComplete();
            } else {
                xRecyclerView.loadMoreComplete();
            }
    }

    @Override
    public void onSignListResponse(RespDto response) {
        if (response == null)
            return;
        onLoadComplete(mPn);
        SignSpaceList result = (SignSpaceList) response.getData();
        if (result == null || result.getSininMoreSpaces() == null || result.getSininMoreSpaces().size() == 0){
            Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
        } else {
            dataList.addAll(result.getSininMoreSpaces());
            int size = result.getSininMoreSpaces().size();
            for (int i = 0; i < size; i++) {
                newSignInterses.add(result.getSininMoreSpaces().get(i).getSignInteractionDto());
                for (int i1 = 0; i1 < i; i1++) {
                    if (dataList.get(i1).getUid() == dataList.get(i).getUid())
                        dataList.remove(i);
                }
            }
            if (dataList.size() > 1)
                dataList.add(1, dataList.get(0));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSignListFailure(String msg) {
        try {
            onLoadComplete(mPn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("zune:", "msg = " + msg);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
