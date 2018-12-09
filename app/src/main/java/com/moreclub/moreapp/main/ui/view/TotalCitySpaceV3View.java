package com.moreclub.moreapp.main.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.SignSpaceList;
import com.moreclub.moreapp.main.presenter.ISignInterTotalLoader;
import com.moreclub.moreapp.main.presenter.SignInterTotalLoader;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.utils.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_OK;
import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_CODE;

/**
 * Created by Administrator on 2017-09-08-0008.
 */

public class TotalCitySpaceV3View extends BaseTabView implements
        ISignInterTotalLoader.LoadSignInterTotalDataBinder<SignSpaceList> {

    private Context mContext;
    public ArrayList<SignSpaceList.SininMoreSpacesBean> dataList;
    public SignInteractSquareAdapter adapter;
    public Integer mPn = 0;
    private Integer mPs = 20;
    private List<SignSpaceList.SininMoreSpacesBean.SignInteractionDtoBean> newSignInterses;
    private XRecyclerView xRecyclerView;

    public TotalCitySpaceV3View(Context cxt) {
        mContext = cxt;
        dataList = new ArrayList<>();
        mPresenter = LogicProxy.getInstance().bind(ISignInterTotalLoader.class, this);
    }

    @Override
    public View createView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sign_square_fragment,
                null, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.xRecyclerView);
        dataList = new ArrayList<>();
        newSignInterses = new ArrayList<>();
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
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
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        ((SignInterTotalLoader) mPresenter).onLoadSignInterTotal(PrefsUtils.getString(mContext, KEY_CITY_CODE, "cd"), mPn, mPs);
        adapter = new SignInteractSquareAdapter(mContext, dataList, newSignInterses, "");
        xRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPn = 0;
                loadData();
            }

            @Override
            public void onLoadMore() {
                mPn++;
                loadData();
            }
        });
    }

    @Override
    public void loadData() {
        ((SignInterTotalLoader) mPresenter).onLoadSignInterTotal(PrefsUtils.getString(mContext, KEY_CITY_CODE, "cd"), mPn, mPs);
    }

    @Override
    public void onSignInterTotalResponse(RespDto<SignSpaceList> respDto) {
        if (respDto == null)
            return;
        onLoadComplete(mPn);
        SignSpaceList result = respDto.getData();
        if (result == null || result.getSininMoreSpaces() == null || result.getSininMoreSpaces().size() == 0) {
            Toast.makeText(mContext, "没有更多了", Toast.LENGTH_SHORT).show();
        } else {
            if (mPn == 0)
                dataList.clear();
            dataList.addAll(result.getSininMoreSpaces());
            int size1 = result.getSininMoreSpaces().size();
            if (result.getSininMoreSpace()!=null) {
                newSignInterses.add(result.getSininMoreSpace());
            }
            for (int i = 0; i < size1; i++) {
                SignSpaceList.SininMoreSpacesBean.SignInteractionDtoBean signInteractionDto = result.getSininMoreSpaces().get(i).getSignInteractionDto();
                if (signInteractionDto != null)
                    newSignInterses.add(signInteractionDto);
            }
            if (size1 > 1 && mPn == 0)
                dataList.add(1, dataList.get(0));
            adapter.notifyDataSetChanged();
        }
    }

    private void onLoadComplete(Integer index) {
        if (xRecyclerView != null)
            if (index == 0) {
                newSignInterses.clear();
                dataList.clear();
                xRecyclerView.refreshComplete();
            } else {
                xRecyclerView.loadMoreComplete();
            }
    }

    @Override
    public void onSignInterTotalFailure(String msg) {

    }

    public void refreshResultPhoto(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Toast.makeText(mContext, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                adapter.refreshAdpater(photos);
            }
        }
    }
}
