package com.moreclub.moreapp.message.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.MessageInteraction;
import com.moreclub.moreapp.message.presenter.IMessageInteractionListLoader;
import com.moreclub.moreapp.message.presenter.MessageInteractionListLoader;
import com.moreclub.moreapp.message.ui.adapter.MessageInteractionListAdapter;
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

public class MessageInteractionListActivity extends BaseListActivity implements IMessageInteractionListLoader.LoaderMessageInteractionListDataBinder {

    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.xrv_interaction_list)
    XRecyclerView xrvInteractionList;
    private Long uid;
    private List<MessageInteraction> datas;
    private MessageInteractionListAdapter adapter;
    private LinearLayoutManager lm;

    @Override
    protected int getLayoutResource() {
        return R.layout.message_interaction_list_activity;
    }

    @Override
    protected Class getLogicClazz() {
        return IMessageInteractionListLoader.class;
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

    private void initData() {
        if (MoreUser.getInstance().getUid().equals(0L)) {
            AppUtils.pushLeftActivity(MessageInteractionListActivity.this, UseLoginActivity.class);
        } else {
            uid = MoreUser.getInstance().getUid();
            String access_token = MoreUser.getInstance().getAccess_token();
            ((MessageInteractionListLoader) mPresenter).loadInteractionMessage(access_token, uid);
        }
    }

    private void setupViews() {
        activityTitle.setText("玩拼座");
        xrvInteractionList.setPullRefreshEnabled(true);
        lm = new LinearLayoutManager(MessageInteractionListActivity.this);
        xrvInteractionList.setLayoutManager(lm);
        xrvInteractionList.setPullRefreshEnabled(true);
        xrvInteractionList.setLoadingMoreEnabled(false);
        xrvInteractionList.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xrvInteractionList.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xrvInteractionList.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        datas = new ArrayList<>();
        adapter = new MessageInteractionListAdapter(MessageInteractionListActivity.this, R.layout.message_interaction_list_item, datas);
        adapter.setHasHeader(true);
        xrvInteractionList.setAdapter(adapter);
        xrvInteractionList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xrvInteractionList.refreshComplete();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    public void onLoadInteractionResponse(RespDto<List<MessageInteraction>> body) {
        if (body != null && body.isSuccess()) {
            datas.addAll(body.getData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadInteractionFailure(String msg) {
        Log.i("zune:", "msg = " + msg);
    }

    @OnClick({R.id.nav_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_back:
                MessageInteractionListActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
/*        if (adapter!=null&&datas!=null) {
            datas.clear();
            adapter.notifyDataSetChanged();
            String access_token = MoreUser.getInstance().getAccess_token();
            ((MessageInteractionListLoader) mPresenter).loadInteractionMessage(access_token, uid);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
