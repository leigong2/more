package com.moreclub.moreapp.message.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseListActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.message.model.MessageSignin;
import com.moreclub.moreapp.message.presenter.IMessageSigninListLoader;
import com.moreclub.moreapp.message.presenter.MessageSigninListLoader;
import com.moreclub.moreapp.message.ui.adapter.MessageSigninListAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.UserSecretActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/5/26.
 */

public class MessageSigninListActivity extends BaseListActivity implements IMessageSigninListLoader.LoaderMessageSigninList {

    private final static int PAGE_START = 1;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    @BindView(R.id.nav_back)
    ImageButton naBack;
    @BindView(R.id.activity_title)
    TextView activityTitle;
    @BindView(R.id.merchant_rv)
    XRecyclerView xRecyclerView;

    ArrayList<MessageSignin> dataList;
    MessageSigninListAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.merchant_listview_activity;
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
        loadData();
    }

    @Override
    protected Class getLogicClazz() {
        return IMessageSigninListLoader.class;
    }

    private void initData() {
        dataList = new ArrayList<>();
        loadData();
    }

    private void loadData() {

        ((MessageSigninListLoader) mPresenter).loadSigninList("" + MoreUser.getInstance().getUid(), "" + page);

    }

    private void setupViews() {

        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
        activityTitle.setText(getString(R.string.message_sign_title));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                loadData();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
        adapter = new MessageSigninListAdapter(this, R.layout.message_signin_list_item, dataList);
        xRecyclerView.setAdapter(adapter);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                MessageSignin item = (MessageSignin) o;
                if (item != null && item.getMid() > 0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            MessageSigninListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(MessageSigninListActivity.this, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid", "" + item.getMid());
                    ActivityCompat.startActivity(MessageSigninListActivity.this, intent, compat.toBundle());
                } else {
                    AppUtils.pushLeftActivity(MessageSigninListActivity.this, UserSecretActivity.class);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            xRecyclerView.refreshComplete();
        } else
            xRecyclerView.loadMoreComplete();
    }

    @Override
    public void onMessageSigninResponse(RespDto response) {
        onLoadComplete(page);
        ArrayList<MessageSignin> result = (ArrayList<MessageSignin>) response.getData();
        if (result != null && result.size() > 0) {
            dataList.addAll(result);
            adapter.notifyDataSetChanged();
        }
        if (result == null || result.size() == 0) {
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onMessageSigninListFailure(String msg) {
        onLoadComplete(page);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                MessageSigninListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
        Intent intent = new Intent(MessageSigninListActivity.this, SuperMainActivity.class);
        intent.putExtra("shouldGo","MessageCenterFragment");
        ActivityCompat.startActivity( MessageSigninListActivity.this, intent, compat.toBundle());
    }
}
