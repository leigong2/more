package com.moreclub.moreapp.message.ui.activity;

import android.content.Intent;
import android.net.Uri;
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
import com.moreclub.moreapp.information.ui.activity.HeadlineActivity;
import com.moreclub.moreapp.main.ui.activity.MChannelDetailsAcitivy;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.main.ui.activity.SearchEntryActivity;
import com.moreclub.moreapp.main.ui.activity.SuperMainActivity;
import com.moreclub.moreapp.main.ui.activity.TotalBarsActivity;
import com.moreclub.moreapp.message.model.MessageSystem;
import com.moreclub.moreapp.message.model.MessageSystemExtras;
import com.moreclub.moreapp.message.presenter.IMessageSystemListLoader;
import com.moreclub.moreapp.message.presenter.MessageSystemListLoader;
import com.moreclub.moreapp.message.ui.adapter.MessageSystemListAdapter;
import com.moreclub.moreapp.ucenter.ui.activity.PlayMoreActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserCouponListActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moreclub.moreapp.util.AppUtils.isNetworkAvailable;

/**
 * Created by Captain on 2017/5/4.
 */

public class MessageSystemListActivity extends BaseListActivity implements IMessageSystemListLoader.LoaderMessageSystemListDataBinder{
    private final static int PAGE_START = 1;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;

    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.merchant_rv) XRecyclerView xRecyclerView;

    ArrayList<MessageSystem> dataList;
    MessageSystemListAdapter adapter;
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
        return IMessageSystemListLoader.class;
    }

    private void initData() {
        dataList = new ArrayList<>();

        loadData();
    }

    private void loadData(){

        ((MessageSystemListLoader)mPresenter).loadMessageSystem(""+MoreUser.getInstance().getUid(),"android","system",""+page,""+PAGE_SIZE);

    }

    private void setupViews() {
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
        activityTitle.setText(getString(R.string.message_system_sub_title));


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
                page ++ ;
                loadData();
            }
        });
        adapter = new MessageSystemListAdapter(this,R.layout.message_system_list_item,dataList);
        xRecyclerView.setAdapter(adapter);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                MessageSystem item = (MessageSystem)o;
                if (item==null)
                    return;
                String template= item.getTemplate();
                //星空首页
                if ("index".equals(template)){
                    Intent intent = new Intent(MessageSystemListActivity.this, SuperMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MessageSystemListActivity.this.startActivity(intent);
                    MessageSystemListActivity.this.finish();
                } else if ("search".equals(template)){
                    AppUtils.pushLeftActivity(MessageSystemListActivity.this, SearchEntryActivity.class);
                } else if ("merchant_info".equals(template)){
                    MessageSystemExtras extra = item.getMap();
                    if (extra==null||extra.getLinktext()==null)
                        return;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            MessageSystemListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(MessageSystemListActivity.this, MerchantDetailsViewActivity.class);
                    intent.putExtra("mid",extra.getLinktext());
                    ActivityCompat.startActivity( MessageSystemListActivity.this, intent, compat.toBundle());
                } else if ("merchant_list".equals(template)){
                    AppUtils.pushAnimActivity(MessageSystemListActivity.this, TotalBarsActivity.class ,
                            R.anim.slide_right_in, R.anim.fade_out);
                } else if ("information".equals(template)){
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            MessageSystemListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(MessageSystemListActivity.this, MChannelDetailsAcitivy.class);
                    intent.putExtra("sid", Integer.parseInt(item.getMap().getLinktext()));
                    ActivityCompat.startActivity(MessageSystemListActivity.this, intent, compat.toBundle());
                } else if ("information_list".equals(template)){
                    AppUtils.pushLeftActivity(MessageSystemListActivity.this, HeadlineActivity.class);

                } else if ("user_info".equals(template)){
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            MessageSystemListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(MessageSystemListActivity.this, SuperMainActivity.class);
                    intent.putExtra("shouldGo","UserCenterFragment");
                    ActivityCompat.startActivity( MessageSystemListActivity.this, intent, compat.toBundle());
                } else if ("thd_url".equals(template)){
                    MessageSystemExtras extra = item.getMap();
                    if (extra==null||extra.getLinktext()==null)
                        return;

                    Intent intent = new Intent(MessageSystemListActivity.this,PlayMoreActivity.class);
                    intent.putExtra("webUrl",extra.getLinktext());
                    intent.putExtra("webTitle",extra.getPushTitle());

                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            MessageSystemListActivity.this, R.anim.slide_right_in, R.anim.slide_right_out);
                    ActivityCompat.startActivity( MessageSystemListActivity.this, intent, compat.toBundle());

                } else if ("more_update".equals(template)){
                    //Todo 更新下载
                    String url = "https://www.moreclub.cn/web/open/loadapk"; // web address
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else if ("coupon".equals(template)){
                    AppUtils.pushLeftActivity(MessageSystemListActivity.this,UserCouponListActivity.class);
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
    public void onMessageSystemListResponse(RespDto response) {
        onLoadComplete(page);
        ArrayList<MessageSystem> result = (ArrayList<MessageSystem>) response.getData();
        if (result!=null&&result.size()>0){
            dataList.addAll(result);
            adapter.notifyDataSetChanged();
        }
        if (result!=null&&result.size()<PAGE_SIZE){
            xRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onMessageSystemListFailure(String msg) {
        onLoadComplete(page);
    }
}
