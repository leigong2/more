package com.moreclub.moreapp.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.SignPersion;
import com.moreclub.moreapp.main.presenter.IMerchantSignInteractSquareLoader;
import com.moreclub.moreapp.main.presenter.MerchantSignInteractSquareLoader;
import com.moreclub.moreapp.main.ui.adapter.SignInteractSquareAdapter;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;


/**
 * Created by Captain on 2017/4/28.
 */

public class SignInteractSquareActivity extends BaseActivity implements IMerchantSignInteractSquareLoader.LoadMerchantSignInteractSquareDataBinder {
    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private SignInteractSquareAdapter adapter;
    private ArrayList<SignPersion> dataList;
    private String mid;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.sign_interact_square;
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
        setupViews();
    }

    @Override
    protected Class getLogicClazz() {
        return IMerchantSignInteractSquareLoader.class;
    }

    private void initData() {
        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");
        dataList = new ArrayList<>();
//        adapter = new SignInteractSquareAdapter(this, dataList, null,mid);
        loadData();
    }

    void loadData() {
        if (MoreUser.getInstance().getUid() != null && MoreUser.getInstance().getUid() > 0) {
            ((MerchantSignInteractSquareLoader) mPresenter).onLoadSignList(mid, "" + MoreUser.getInstance().getUid(), "1", "20");
        } else {
            ((MerchantSignInteractSquareLoader) mPresenter).onLoadSignList(mid, "0", "1", "20");
        }
    }

    private void setupViews() {
        activityTitle.setText("M-space");
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        mLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        xRecyclerView.setLayoutManager(mLayoutManager);
//        xRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSignListResponse(RespDto response) {
        SignList result = (SignList) response.getData();
        if (result.getUsers() == null || result.getUsers().size() == 0) {

        } else {
            dataList.addAll(result.getUsers());
            if (dataList.size() > 1) {
                dataList.add(1, dataList.get(1));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSignListFailure(String msg) {

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
}
