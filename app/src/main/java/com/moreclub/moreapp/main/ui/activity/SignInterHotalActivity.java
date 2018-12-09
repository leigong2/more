package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.SignSpaceList;
import com.moreclub.moreapp.main.presenter.ISignInterTotalLoader;
import com.moreclub.moreapp.main.presenter.SignInterTotalLoader;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.main.utils.ReflectionUtil;
import com.moreclub.moreapp.main.utils.UpdateUser;
import com.moreclub.moreapp.util.DataCleanManager;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_CODE;
import static com.moreclub.moreapp.util.FileSaveUtils.SAVE_REAL_PATH_TEMP;

public class SignInterHotalActivity extends BaseListActivity
        implements ISignInterTotalLoader.LoadSignInterTotalDataBinder<SignSpaceList> {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.nav_right_btn)
    ImageButton navRightBtn;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    private ArrayList<SignSpaceList.SininMoreSpacesBean> dataList;
    public SignInteractSquareAdapter adapter;
    private Integer mPn = 0;
    private Integer mPs = 20;
    private List<SignSpaceList.SininMoreSpacesBean.SignInteractionDtoBean> newSignInterses;

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_square_fragment;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);
        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        setupView();
    }

    @Override
    protected Class getLogicClazz() {
        return ISignInterTotalLoader.class;
    }

    private void setupView() {
        rlRoot.setBackgroundResource(R.drawable.home_star_bg);
        activityTitle.setText("M-space");
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
        ((SignInterTotalLoader) mPresenter).onLoadSignInterTotal(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"), mPn, mPs);
        adapter = new SignInteractSquareAdapter(this, dataList, newSignInterses, "");
        xRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPn = 0;
                onReloadData();
            }

            @Override
            public void onLoadMore() {
                mPn++;
                onReloadData();
            }
        });
    }

    @Override
    protected void onReloadData() {
        ((SignInterTotalLoader) mPresenter).onLoadSignInterTotal(PrefsUtils.getString(this, KEY_CITY_CODE, "cd"), mPn, mPs);
    }


    @Override
    public void onSignInterTotalResponse(RespDto<SignSpaceList> respDto) {
        if (respDto == null)
            return;
        onLoadComplete(mPn);
        SignSpaceList result = respDto.getData();
        if (result == null || result.getSininMoreSpaces() == null || result.getSininMoreSpaces().size() == 0) {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
        } else {
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

    @Override
    public void onSignInterTotalFailure(String msg) {
        Logger.i("zune:", "msg = " + msg);
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

    @OnClick({R.id.nav_back, R.id.nav_right_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                SignInterHotalActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onReloadData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, "获取图片出错,请确保您的权限已经开启", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        if (adapter.mUserPopupWindow != null && adapter.mUserPopupWindow.isShowing()) {
            adapter.mUserPopupWindow.dismiss();
            DataCleanManager.cleanCustomCache(SAVE_REAL_PATH_TEMP);
            Map<String, SignInteractSquareAdapter> adapters = UpdateUser.getInstance().getAdapters();
            if (adapters != null && adapters.size() > 0) {
                adapters.clear();
            }
            adapter.hasGone = false;
        } else {
            super.onBackPressed();
            UpdateUser.getInstance().setAdapters(null);
        }
    }
}
