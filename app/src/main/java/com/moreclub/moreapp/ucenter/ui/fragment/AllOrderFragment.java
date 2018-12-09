package com.moreclub.moreapp.ucenter.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.fragment.BaseFragment;
import com.moreclub.common.ui.view.alertview.OnDismissListener;
import com.moreclub.common.ui.view.alertview.OnItemClickListener;
import com.moreclub.common.util.PhoneUtil;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.packages.ui.activity.OrderCloseActivity;
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.packages.ui.activity.PayActivity;
import com.moreclub.moreapp.packages.ui.activity.PaySucessActivity;
import com.moreclub.moreapp.packages.ui.view.CustomPayAlertView;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.Order;
import com.moreclub.moreapp.ucenter.model.OrderRepayValidateResult;
import com.moreclub.moreapp.ucenter.model.OrderResp;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;
import com.moreclub.moreapp.ucenter.presenter.AllOrderLoader;
import com.moreclub.moreapp.ucenter.presenter.IAllOrderLoader;
import com.moreclub.moreapp.ucenter.ui.activity.PkgOrderDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.ucenter.ui.adapter.AllOrderAdapter;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.ClickUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/27.
 */

public class AllOrderFragment extends BaseFragment implements
        OnItemClickListener, OnDismissListener, IAllOrderLoader.LoaderAllOrderDataBinder {

    private final static int PAGE_START = 1;
    private final static int PAGE_SIZE = 10;
    private int page = PAGE_START;
    private int pages = 0;

    @BindView(R.id.myorder_all_rv)
    XRecyclerView recyclerView;

    private AllOrderAdapter adapter;
    private List<Order> orderList;
    private CustomPayAlertView delAlertView;
    private CustomPayAlertView cancelAlertView;
    private int delOrderPos;
    private Order clickOrder;
    private Order cancelOrder;

    @Override
    protected int getLayoutResource() {
        return R.layout.my_order_all_fragment;
    }

    @Override
    protected Class getLogicClazz() {
        return IAllOrderLoader.class;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        ButterKnife.bind(this, rootView);
        orderList = new ArrayList<>();
        mContext = getContext();

        if (delAlertView == null) {
            delAlertView = new CustomPayAlertView(getString(R.string.order_del_des),
                    "",
                    getString(R.string.order_cancel_false),
                    new String[]{getString(R.string.dialog_confirm_ok_delorder)},
                    null, getActivity(), CustomPayAlertView.Style.Alert, this)
                    .setCancelable(true).setOnDismissListener(this);
        }

        if (cancelAlertView == null) {
            cancelAlertView = new CustomPayAlertView(getString(R.string.order_cancel_des),
                    "",
                    getString(R.string.order_cancel_false),
                    new String[]{getString(R.string.order_cancel_ok)},
                    null, getActivity(), CustomPayAlertView.Style.Alert, this)
                    .setCancelable(true).setOnDismissListener(this);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AllOrderAdapter(getActivity(), R.layout.my_order_item, orderList, new AllOrderAdapter.CancelOrderListener() {

            @Override
            public void cancelOrder(Order order) {
                cancelOrder = order;
                if (!cancelAlertView.isShowing() && !getActivity().isFinishing())
                    cancelAlertView.show();
            }
        }, new AllOrderAdapter.ButtonClickListener() {

            @Override
            public void btnClick(Order order) {
                clickOrder = order;
                clickOrder.setGotoType(1);
                if (order.getStatus() == 0 || order.getStatus() == 1) {
                    String time = TimeUtils.getTimestampSecond();
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("appkey", Constants.MORE_APPKEY);
                    map.put("timestamp", time);
                    map.put("orderId", "" + order.getOrderId());
                    map.put("uid", "" + MoreUser.getInstance().getUid());

                    RepayValidateParam param = new RepayValidateParam();
                    param.setTimestamp(time);
                    param.setOrderId("" + order.getOrderId());
                    param.setUid("" + MoreUser.getInstance().getUid());
                    param.setAppkey(Constants.MORE_APPKEY);
                    String sig = SecurityUtils.createSign(map, Constants.MORE_SECRET);
                    param.setSign(sig);

                    if (ClickUtils.isPayClick()) {
                        return;
                    } else {
                        ((AllOrderLoader) mPresenter).onRepayValidate(param);
                    }
                }
            }
        });
        adapter.setHasHeader(true);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Order order = (Order) o;
                clickOrder = order;
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);

                if (order.getStatus() == 0 || order.getStatus() == 1 || order.getStatus() == 2) {

                    if (order.getPrdType() == 0&&order.getStatus() == 2){
                        Intent intent = new Intent(mContext, PkgOrderDetailActivity.class);
                        intent.putExtra("orderId", order.getOrderId());
                        ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                        return;
                    } else {
                        if (order.getStatus() == 2) {
                            clickOrder.setGotoType(3);
                        } else {
                            clickOrder.setGotoType(2);
                        }
                    }
                    String time = TimeUtils.getTimestampSecond();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("appkey", Constants.MORE_APPKEY);
                    map.put("timestamp", time);
                    map.put("orderId", "" + order.getOrderId());
                    map.put("uid", "" + MoreUser.getInstance().getUid());

                    RepayValidateParam param = new RepayValidateParam();
                    param.setTimestamp(time);
                    param.setOrderId("" + order.getOrderId());
                    param.setUid("" + MoreUser.getInstance().getUid());
                    param.setAppkey(Constants.MORE_APPKEY);
                    String sig = SecurityUtils.createSign(map, Constants.MORE_SECRET);
                    param.setSign(sig);

                    if (ClickUtils.isPayClick()) {
                        return;
                    } else {
                        ((AllOrderLoader) mPresenter).onRepayValidate(param);
                    }
                    return;
                }else if (order.getStatus() == 3 || order.getStatus() == 4 || order.getStatus() == 5) {
                    Intent intent = new Intent(mContext, OrderCloseActivity.class);
                    intent.putExtra("orderPackageName", order.getTitle());
                    intent.putExtra("orderNumber", "" + order.getOrderId());
                    intent.putExtra("orderStatus", order.getStatus());
                    intent.putExtra("orderTime", "" + order.getCreateTime());
                    intent.putExtra("orderPrice", "¥" + order.getTotalPrice().intValue());
                    intent.putExtra("orderPhone", order.getContactPhone());
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                    return;
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                delOrderPos = position;
                if (!delAlertView.isShowing() && !getActivity().isFinishing())
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
                if (page < pages) {
                    page++;
                    onLoadOrders();
                } else {
                    recyclerView.loadMoreComplete();
                }
            }
        });

    }

    public void onLoadData() {
        onInitData2Remote();
    }

    @Override
    protected void onInitData2Remote() {
        super.onInitData2Remote();
        //onLoadOrders();
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

    private void onLoadOrders() {
        long uid = MoreUser.getInstance().getUid();
        ((AllOrderLoader) mPresenter).getAllOrderList("all", uid, page, PAGE_SIZE);
    }

    private void onLoadComplete(int page) {
        if (page == PAGE_START) {
            orderList.clear();
            recyclerView.refreshComplete();
        } else
            recyclerView.loadMoreComplete();
    }

    /**
     * @param o
     * @param position
     */
    @Override
    public void onItemClick(Object o, int position) {
        if (position == 0) {
            if (o == delAlertView) {
                try {
                    Order delOrder = orderList.get(delOrderPos);
                    if (delOrder != null)
                        ((AllOrderLoader) mPresenter).delOrder("" + delOrder.getOrderId());
                } catch (Exception e) {
                }
            } else if (o == cancelAlertView) {
                try {
                    if (cancelOrder != null) {
                        UserOrderCancelParam cancelParam = new UserOrderCancelParam();
                        cancelParam.setUid("" + MoreUser.getInstance().getUid());
                        cancelParam.setOrderId("" + cancelOrder.getOrderId());
                        cancelParam.setAppVersion(MainApp.getInstance().mVersion);
                        cancelParam.setDeviceId(PhoneUtil.getImsi2(mContext));
                        cancelParam.setMachine("android");
                        cancelParam.setMachineStyle("android");
                        cancelParam.setOpSystem("android");
                        cancelParam.setOpVersion("5.0");
                        ((AllOrderLoader) mPresenter).cancelOrder(cancelParam);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onDelOrderFailure(String msg) {
        orderList.remove(delOrderPos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelOrderFailure(String msg) {

    }

    @Override
    public void onCancelOrderResponse(RespDto response) {
        for (int i = 0; i < orderList.size(); i++) {
            Order item = orderList.get(i);
            if (item == cancelOrder) {
                item.setStatus(5);
                break;
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRepayValidateFailure(String msg) {
        if ("C10110".equals(msg)) {
            clickOrder.setStatus(4);
            adapter.notifyDataSetChanged();
            Toast.makeText(mContext, "你的订单已经过期,请重新下单", Toast.LENGTH_LONG).show();
            if (clickOrder.getPrdType() == 0) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.
                        makeCustomAnimation(mContext,
                                R.anim.slide_right_in,
                                R.anim.fade_out);

                Intent intent = new Intent(mContext, PackageDetailsActivity.class);
                intent.putExtra("pid", "" + clickOrder.getPid());
                intent.putExtra("mid", "" + clickOrder.getMerchantId());
                mContext.startActivity(intent);
            } else {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
                intent.putExtra("mid", "" + clickOrder.getMerchantId());
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        } else {
            Toast.makeText(mContext, "网络超时", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRepayValidateResponse(RespDto response) {

        OrderRepayValidateResult result = (OrderRepayValidateResult) response.getData();
        if (result != null) {
            if (clickOrder == null) {
                return;
            }

            //status 状态 0-创建，1-已下单未支付，2-已支付,3-已过期(15min内未支付)，4-订单关闭，5-取消
            if (result.getOrderStatus() == 0 || result.getOrderStatus() == 1 || result.getOrderStatus() == 2) {
                if (clickOrder.getGotoType() == 1) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, PayActivity.class);
                    intent.putExtra("pid", "" + result.getPackageId());
                    intent.putExtra("mid", "" + result.getMid());
                    intent.putExtra("payTitle", clickOrder.getTitle());
                    intent.putExtra("payType", result.getOrderType());
                    intent.putExtra("orderID", clickOrder.getOrderId());
                    intent.putExtra("orderCount", clickOrder.getItemNum());
                    intent.putExtra("cardLevel", result.getCardLevel());
                    intent.putExtra("cardRatePrice", result.getCardPrice());
                    intent.putExtra("cardRate", result.getCardRate());
                    intent.putExtra("couponPrice", result.getCouponPrice());
                    intent.putExtra("noRatePriceValue", result.getExPrice());
                    if (clickOrder.getPrdType() == 0) {
                        intent.putExtra("singlePrice", ((clickOrder.getTotalPrice().setScale(2,
                                BigDecimal.ROUND_HALF_UP).floatValue()) / clickOrder.getItemNum()));
                    } else {
                        intent.putExtra("singlePrice", 0f);
                    }
                    intent.putExtra("actPrice", (clickOrder.getActualPrice().
                            setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()));
                    intent.putExtra("totalPrice", clickOrder.getTotalPrice().setScale(2,
                            BigDecimal.ROUND_HALF_UP).floatValue());
                    intent.putExtra("merchantName", clickOrder.getTitle());
                    intent.putExtra("orderStatus", result.getOrderStatus());
                    intent.putExtra("payMethod", clickOrder.getPayMethod());
                    intent.putExtra("isSelectPayMethod", true);
                    intent.putExtra("enterFrom", 3);
                    if (clickOrder.getActualPrice().
                            setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() == 0) {
                        intent.putExtra("zeroPay", "1");
                    } else {
                        intent.putExtra("zeroPay", "0");
                    }
                    ActivityCompat.startActivity(mContext, intent, compat.toBundle());
                } else if (clickOrder.getGotoType() == 2) {

                    Intent intent = new Intent(mContext, PaySucessActivity.class);
                    intent.putExtra("mid", "" + result.getMid());
                    intent.putExtra("pid", "" + result.getPackageId());
                    if (result.getPayMethod() == 0) {
                        intent.putExtra("payMethod", "微信");
                    } else {
                        intent.putExtra("payMethod", "支付宝");
                    }
                    intent.putExtra("paySucess", false);
                    intent.putExtra("payMerchant", result.getMarchantName());
                    intent.putExtra("totalPrice", result.getTotalPrice());
                    intent.putExtra("actPrice", result.getActualPrice());
                    intent.putExtra("cardRate", result.getCardRate());
                    intent.putExtra("couponPrice", result.getCouponPrice());
                    String time = TimeUtils.getTimestampSecond();
                    intent.putExtra("orderTime", time);
                    intent.putExtra("orderID", result.getOrderId());
                    intent.putExtra("noRatePriceValue", result.getExPrice());
                    intent.putExtra("enterFrom", 1);
                    intent.putExtra("payType", result.getOrderType());
                    intent.putExtra("cardRatePrice", result.getCardPrice());
                    intent.putExtra("cardLevel", result.getCardLevel());
                    intent.putExtra("orderStatus", result.getOrderStatus());
                    intent.putExtra("orderCount", clickOrder.getItemNum());
                    if (result.getOrderType() == 0) {
                        intent.putExtra("packageName", result.getPackageName());
                    }
                    mContext.startActivity(intent);

                } else if (clickOrder.getGotoType() == 3) {
                    Intent intent = new Intent(mContext, PaySucessActivity.class);
                    intent.putExtra("mid", "" + result.getMid());
                    intent.putExtra("pid", "" + result.getPackageId());
                    if (result.getPayMethod() == 0) {
                        intent.putExtra("payMethod", "微信");
                    } else {
                        intent.putExtra("payMethod", "支付宝");
                    }
                    intent.putExtra("paySucess", true);
                    intent.putExtra("payMerchant", result.getMarchantName());
                    intent.putExtra("totalPrice", result.getTotalPrice());
                    intent.putExtra("actPrice", result.getActualPrice());
                    intent.putExtra("cardRate", result.getCardRate());
                    intent.putExtra("couponPrice", result.getCouponPrice());
                    String time = TimeUtils.getTimestampSecond();
                    intent.putExtra("orderTime", time);
                    intent.putExtra("orderID", result.getOrderId());
                    intent.putExtra("noRatePriceValue", result.getExPrice());
                    intent.putExtra("enterFrom", 1);
                    intent.putExtra("payType", result.getOrderType());
                    intent.putExtra("cardLevel", result.getCardLevel());
                    intent.putExtra("cardRatePrice", result.getCardPrice());
                    intent.putExtra("orderStatus", result.getOrderStatus());
                    intent.putExtra("orderCount", clickOrder.getItemNum());
                    if (result.getOrderType() == 0) {
                        intent.putExtra("packageName", result.getPackageName());
                    }
                    mContext.startActivity(intent);
                }
            } else {
                clickOrder.setStatus(4);
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "你的订单已经过期,请重新下单", Toast.LENGTH_LONG).show();
            }
        } else {
            clickOrder.setStatus(4);
            adapter.notifyDataSetChanged();
            Toast.makeText(mContext, "你的订单已经过期,请重新下单", Toast.LENGTH_LONG).show();
//            if (clickOrder.getPrdType() == 0) {
//                ActivityOptionsCompat compat = ActivityOptionsCompat.
//                        makeCustomAnimation(mContext,
//                                R.anim.slide_right_in,
//                                R.anim.fade_out);
//
//                Intent intent = new Intent(mContext, PackageDetailsActivity.class);
//                intent.putExtra("pid", "" + clickOrder.getPid());
//                intent.putExtra("mid", "" + clickOrder.getMerchantId());
//                mContext.startActivity(intent);
//            } else {
//                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
//                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
//                Intent intent = new Intent(mContext, MerchantDetailsViewActivity.class);
//                intent.putExtra("mid", "" + clickOrder.getMerchantId());
//                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
//            }
        }
    }

    @Override
    public void onDelOrderResponse(RespDto response) {
        orderList.remove(delOrderPos);
        adapter.notifyDataSetChanged();
    }

    public void updateData(){
        clickOrder.setStatus(5);
        adapter.notifyDataSetChanged();
    }

}
