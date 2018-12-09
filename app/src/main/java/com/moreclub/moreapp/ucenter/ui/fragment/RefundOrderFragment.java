package com.moreclub.moreapp.ucenter.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.common.ui.view.alertview.AlertView;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.ConsumeCode;
import com.moreclub.moreapp.ucenter.model.Order;
import com.moreclub.moreapp.ucenter.model.OrderResp;
import com.moreclub.moreapp.ucenter.presenter.IRefundOrderLoader;
import com.moreclub.moreapp.ucenter.presenter.MyOrderLoader;
import com.moreclub.moreapp.ucenter.presenter.RefoundOrderLoader;
import com.moreclub.moreapp.ucenter.ui.activity.PkgOrderDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.adapter.RefundOrderAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/27.
 */

public class RefundOrderFragment extends BaseFragment implements
        OnItemClickListener,OnDismissListener,IRefundOrderLoader.LoaderOrderDataBinder{

    private final static int PAGE_START = 1;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;

    @BindView(R.id.myorder_all_rv) XRecyclerView recyclerView;

    private RefundOrderAdapter adapter;
    private List<Order> orderList;
    private AlertView delAlertView;
    private int delOrderPos;
    @Override
    protected int getLayoutResource() {
        return R.layout.my_order_all_fragment;
    }

    @Override
    protected Class getLogicClazz() {
        return IRefundOrderLoader.class;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        orderList = new ArrayList<>();

        if (delAlertView==null) {
            delAlertView = new AlertView(getString(R.string.order_del_title),
                    getString(R.string.order_del_des),
                    getString(R.string.dialog_confirm_cancel),
                    new String[]{getString(R.string.dialog_confirm_ok)},
                    null, getActivity(), AlertView.Style.Alert, this)
                    .setCancelable(true).setOnDismissListener(this);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new RefundOrderAdapter(getActivity(), R.layout.my_order_item, orderList);
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Order order = (Order) o;
//                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
//                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
//                Intent intent = new Intent(mContext, RefundAuditActivity.class);
//                intent.putExtra("count", getTypeCount(order.getCodes(), 2));
//                intent.putExtra("price", order.getActualPrice().intValue());
//                intent.putExtra("payMethod", order.getPayMethod());
//
//                ActivityCompat.startActivity( mContext, intent, compat.toBundle());

                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, PkgOrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrderId());

                ActivityCompat.startActivity( mContext, intent, compat.toBundle());
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                delOrderPos = position;
                if (!delAlertView.isShowing()&&!getActivity().isFinishing())
                    delAlertView.show();
                return true;
            }
        });

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
                onLoadOrders();
            }

            @Override
            public void onLoadMore() {
                if (page < pages){
                    page++;
                    onLoadOrders();
                } else {
                    recyclerView.loadMoreComplete();
                }
            }
        });

    }

//    @Override
//    protected void onInitData2Remote() {
//        super.onInitData2Remote();
//        //onLoadOrders();
//
//    }

    public void onLoadData() {
        recyclerView.refresh();
    }

    @Override
    public void onOrderResponse(Response response) {
        onLoadComplete(page);

        if (response.code() == 401) {
            AppUtils.pushLeftActivity(getActivity(), UseLoginActivity.class);
            return;
        }

        RespDto<OrderResp> respDto = (RespDto<OrderResp>) response.body();

        if (respDto != null) {
            pages = respDto.getData().getPages();
            List<Order> orders = respDto.getData().getList();
            if (orders != null && !orders.isEmpty()) {
                orderList.addAll(orders);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onOrderFailure(String msg) {
        Logger.d("error" + msg);

        onLoadComplete(page);
    }

    private  void onLoadOrders() {
        long uid = MoreUser.getInstance().getUid();
        ((RefoundOrderLoader)mPresenter).getAllOrderList("refund", uid, page, PAGE_SIZE);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            orderList.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    private int getTypeCount(List<ConsumeCode> codes, int type)  {
        int result = 0;
        if (codes != null && !codes.isEmpty()) {
            for (ConsumeCode consumeCode : codes) {
                if (consumeCode.getStatus() == type) {
                    result ++ ;
                }
            }
        }

        return result;
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        if (position==0) {
            try {
                Order delOrder = orderList.get(delOrderPos);
                if (delOrder != null)
                    ((MyOrderLoader) mPresenter).delOrder("" + delOrder.getOrderId());
            }catch ( Exception e){

            }
        }
    }

    @Override
    public void onDelOrderFailure(String msg) {
        orderList.remove(delOrderPos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDelOrderResponse(RespDto response) {
        orderList.remove(delOrderPos);
        adapter.notifyDataSetChanged();
    }
}