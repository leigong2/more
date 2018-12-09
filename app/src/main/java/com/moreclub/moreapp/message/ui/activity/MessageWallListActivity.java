package com.moreclub.moreapp.message.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.message.model.MessageWall;
import com.moreclub.moreapp.message.presenter.IMessageWallListLoader;
import com.moreclub.moreapp.message.presenter.MessageWallListLoader;
import com.moreclub.moreapp.message.ui.adapter.MessageWallListAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

public class MessageWallListActivity extends BaseListActivity implements IMessageWallListLoader.LoaderMessageWallListDataBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.merchant_rv)
    XRecyclerView merchantRv;
    private String platform;
    private Long uid;
    private String type;
    private Integer pageIndex;
    private Integer pageSize;
    private String token;
    private List<MessageWall> datas;
    private RecyclerView.Adapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.message_wall_list_activity;
    }

    @Override
    protected void onInitialization(Bundle bundle) {
        ButterKnife.bind(this);

        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .edgeSize(0.18f)
                .build();
        Slidr.attach(this, config);
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        initData();
        setupViews();
    }

    @Override
    protected void onReloadData() {
        if (isNetworkAvailable(this))
            hideNoData();
        else
            showNoData(true);
        initData();
    }

    @Override
    protected Class getLogicClazz() {
        return IMessageWallListLoader.class;
    }

    private void setupViews() {
        activityTitle.setText("留言墙消息");
        merchantRv.setPullRefreshEnabled(true);
        LinearLayoutManager lm = new LinearLayoutManager(MessageWallListActivity.this);
        merchantRv.setLayoutManager(lm);
        merchantRv.setPullRefreshEnabled(true);
        merchantRv.setArrowImageView(R.drawable.abc_icon_down_arrow);
        merchantRv.setRefreshProgressStyle(ProgressStyle.SysProgress);
        merchantRv.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        datas = new ArrayList<>();
        adapter = new MessageWallListAdapter(MessageWallListActivity.this, R.layout.message_wall_list_item, datas);
        merchantRv.setAdapter(adapter);
    }

    private void initData() {
        if (MoreUser.getInstance().getUid().equals(0L)) {
            AppUtils.pushLeftActivity(MessageWallListActivity.this, UseLoginActivity.class);
        } else {
            token = MoreUser.getInstance().getAccess_token();
            platform = "android";
            uid = MoreUser.getInstance().getUid();
            type = "wall";
            pageIndex = 1;
            pageSize = 20;
            ((MessageWallListLoader) mPresenter).loadSystemMessage(token, platform, uid, type, pageIndex, pageSize);
        }
    }


    private void loadData(RespDto<List<MessageWall>> body) {
        List<MessageWall> data = body.getData();
        if (datas != null)
            datas.addAll(data);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadWallResponse(RespDto<List<MessageWall>> body) {
        if (body != null) {
            loadData(body);
        }
    }

    @Override
    public void onLoadWallFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @OnClick(R.id.nav_back)
    public void onViewClicked() {
        MessageWallListActivity.this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(this, SuperMainActivity.class);
        intent.putExtra("shouldGo","MessageCenterFragment");
        ActivityCompat.startActivity( this, intent, compat.toBundle());
    }
}
