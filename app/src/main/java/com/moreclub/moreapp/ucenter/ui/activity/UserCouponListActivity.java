package com.moreclub.moreapp.ucenter.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.BaseActivity;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.TotalBarsActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.model.UserCouponResult;
import com.moreclub.moreapp.ucenter.presenter.IUserCouponListLoader;
import com.moreclub.moreapp.ucenter.presenter.UserCouponListLoader;
import com.moreclub.moreapp.ucenter.ui.adapter.UserCouponAdapter;
import com.moreclub.moreapp.ucenter.ui.view.MyCouponAdapter;
import com.moreclub.moreapp.util.MoreUser;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Captain on 2017/6/15.
 */

public class UserCouponListActivity extends BaseActivity implements IUserCouponListLoader.LoadUserCouponListDataBinder{

    private final static String KEY_CITY_CODE = "city.code";
    private final static int PAGE_START = 0;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;

    @BindView(R.id.user_coupon_rv) XRecyclerView recyclerView;
    @BindView(R.id.nav_back) ImageButton naBack;
    @BindView(R.id.activity_title) TextView activityTitle;
    @BindView(R.id.noncoupon_lay) RelativeLayout noncouponLay;
    private ArrayList<UserCoupon> dataList;
    private MyCouponAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.user_coupon_list_activity;
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
        loadData();

    }

    @Override
    protected Class getLogicClazz() {
        return IUserCouponListLoader.class;
    }

    private void initData() {

        try {
            PrefsUtils.getEditor(this).putBoolean(Constants.KEY_NO_READ_COUPON, false)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setupViews() {
        activityTitle.setText("我的礼券");
        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });

        dataList = new ArrayList<>();
        adapter = new MyCouponAdapter(this, R.layout.user_coupon_support_item, dataList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                UserCoupon item = (UserCoupon) o;

                Context context = UserCouponListActivity.this;
                String str = "coupon_item_touse_btn_click";
                Map<String, String> map = new HashMap<>();
                map.put("cid",""+item.getRid());
                if (Build.VERSION.SDK_INT > 22) {
                    int du = 2000000000;
                    MobclickAgent.onEventValue(context, str, map, du);
                } else {
                    MobclickAgent.onEvent(context, str, map);
                }

                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(UserCouponListActivity.this,
                                R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserCouponListActivity.this,
                        UserCouponDetailsActivity.class);
                intent.putExtra("cid",""+item.getRid());
                ActivityCompat.startActivity(UserCouponListActivity.this, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        adapter.setOnUseListener(new UserCouponAdapter.UseListener() {
            @Override
            public void useClick(UserCoupon userCoupon) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(UserCouponListActivity.this,
                        R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(UserCouponListActivity.this,
                        TotalBarsActivity.class);
                intent.putExtra("keyword", "iscoupon");
                intent.putExtra("couponMoal",userCoupon.getCouponModal());
                ActivityCompat.startActivity(UserCouponListActivity.this, intent, compat.toBundle());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = PAGE_START;
                recyclerView.reset();
                loadData();
            }

            @Override
            public void onLoadMore() {
                    page++;
                    loadData();
            }
        });

        naBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out);
            }
        });
        recyclerView.refresh();

    }

    private void loadData(){
        ((UserCouponListLoader)mPresenter).loadUserCoupon(""+ MoreUser.getInstance().getUid(),""+page);

    }

    /**
     *
     * @param response
     */
    @Override
    public void onUserCouponResponse(RespDto response) {
        noncouponLay.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        onLoadComplete(page);
        UserCouponResult result = (UserCouponResult) response.getData();
        if (result!=null&&result.getCoupons()!=null&&result.getCoupons().size()>0){
            dataList.addAll(result.getCoupons());
            adapter.notifyDataSetChanged();
        } else if (page>0) {
            recyclerView.loadMoreComplete();
        }

        if (page==0&&(result==null||result.getCoupons()==null||result.getCoupons().size()==0)){
            noncouponLay.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserCouponFailure(String msg) {
        noncouponLay.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        onLoadComplete(page);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            dataList.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
