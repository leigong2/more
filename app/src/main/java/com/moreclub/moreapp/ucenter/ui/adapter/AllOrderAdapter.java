package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.model.LogicProxy;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.util.SecurityUtils;
import com.moreclub.common.util.TimeUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.ui.activity.MerchantDetailsViewActivity;
import com.moreclub.moreapp.packages.ui.activity.PackageDetailsActivity;
import com.moreclub.moreapp.packages.ui.activity.PayActivity;
import com.moreclub.moreapp.ucenter.constant.Constants;
import com.moreclub.moreapp.ucenter.model.ConsumeCode;
import com.moreclub.moreapp.ucenter.model.Order;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.ucenter.presenter.IRepayValidateLoader;
import com.moreclub.moreapp.ucenter.presenter.RepayValidateLoader;
import com.moreclub.moreapp.util.ClickUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/27.
 */

public class AllOrderAdapter extends RecyclerAdapter<Order>{

    public interface CancelOrderListener{

        void cancelOrder(Order order);

    }

    public interface ButtonClickListener{

        void btnClick(Order order);

    }

    CancelOrderListener cancelListener;
    ButtonClickListener btnListener;

    public AllOrderAdapter(Context context, int layoutId, List<Order> datas,CancelOrderListener cancel
    ,ButtonClickListener btnLis) {
        super(context, layoutId, datas);
        cancelListener = cancel;
        btnListener = btnLis;
    }

    @Override

    public void convert(RecyclerViewHolder holder,final Order order) {
        String itemCount = order.getItemNum() == null ? "" : " x "+order.getItemNum() + " 份";
        String price ="";
        float actualF = order.getActualPrice().floatValue();
        int actualInt =order.getActualPrice().intValue();
        if (actualF>actualInt){
            price = "¥ "+order.getActualPrice().setScale(1, BigDecimal.ROUND_HALF_UP);
        } else {
            price = "¥ "+actualInt;
        }

        TextView orderType = holder.getView(R.id.order_type_title);
        if (order.getPrdType()==0) {
            holder.setText(R.id.myoroder_item_title, "       "+order.getTitle());
            holder.setText(R.id.order_type_title,"套餐");
            holder.setText(R.id.my_order_price, itemCount);
            orderType.setBackgroundResource(R.drawable.order_item_package_title_bg);
            orderType.setTextColor(ContextCompat.getColor(mContext,R.color.black));
        } else {
            holder.setText(R.id.myoroder_item_title, "       "+order.getTitle());
            holder.setText(R.id.order_type_title,"买单");
            holder.setText(R.id.my_order_price, "");
            orderType.setBackgroundResource(R.drawable.order_item_bill_title_bg);
            orderType.setTextColor(ContextCompat.getColor(mContext,R.color.orange));
        }

        holder.setText(R.id.my_order_count, price);
        holder.setText(R.id.my_order_total_text, "");

        holder.getView(R.id.order_operation).setVisibility(View.GONE);
        ((TextView)holder.getView(R.id.my_order_wait_pay_status)).
                setTextColor(ContextCompat.getColor(mContext,R.color.merchant_item_distance));
        //status 状态 0-创建，1-已下单未支付，2-已支付,3-已过期(15min内未支付)，4-订单关闭，5-取消
        if (order.getStatus() == 0 || order.getStatus() == 1) { //待支付
            holder.getView(R.id.order_operation).setVisibility(View.VISIBLE);
            holder.setText(R.id.my_order_wait_pay_status, "待支付");
            holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.VISIBLE);
            holder.setText(R.id.my_order_wait_pay_text, "去支付");
            holder.getView(R.id.my_order_cancel_text).setVisibility(View.VISIBLE);

            holder.getView(R.id.my_order_cancel_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelListener!=null){
                        cancelListener.cancelOrder(order);
                    }
                }
            });

            holder.getView(R.id.my_order_wait_pay_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnListener!=null){
                        btnListener.btnClick(order);
                    }
                }
            });

            ((TextView)holder.getView(R.id.my_order_wait_pay_status)).
                    setTextColor(ContextCompat.getColor(mContext,R.color.order_status_no_pay));
//            holder.setImageResource(R.id.my_order_wait_pay, R.drawable.my_order_wait_pay);
        }else if(order.getStatus() == 2) {
            holder.getView(R.id.my_order_cancel_text).setVisibility(View.GONE);
            //prdtype: 0未消费 1已消费 2退款
            if (order.getPrdType() == 0) {
                if (getTypeCount(order.getCodes(), 2) > 0) {
                    holder.setText(R.id.my_order_wait_pay_status, "已退款");
//                    holder.setImageResource(R.id.my_order_wait_pay, R.drawable.myorder_pay_ok);
                } else {
                    int unUsedCount = getTypeCount(order.getCodes(), 0);
                    if (unUsedCount == order.getCodes().size()) {
                        holder.setText(R.id.my_order_wait_pay_status, "未消费");
                        ((TextView)holder.getView(R.id.my_order_wait_pay_status)).
                                setTextColor(ContextCompat.getColor(mContext,R.color.order_status_use));
//                        holder.setImageResource(R.id.my_order_wait_pay, R.drawable.myorder_pay_ok);
                    } else if (unUsedCount < order.getCodes().size() && unUsedCount > 0) {
                        holder.setText(R.id.my_order_wait_pay_status, "部分消费");
                        ((TextView)holder.getView(R.id.my_order_wait_pay_status)).
                                setTextColor(ContextCompat.getColor(mContext,R.color.order_status_use));
//                        holder.setImageResource(R.id.my_order_wait_pay, R.drawable.my_order_wait_pay);
                    } else {
                        holder.setText(R.id.my_order_wait_pay_status, "已消费");
//                        holder.setImageResource(R.id.my_order_wait_pay, R.drawable.my_order_wait_pay);
                    }
                }
            } else {
                holder.setText(R.id.my_order_wait_pay_status, "已支付");
                ((TextView)holder.getView(R.id.my_order_wait_pay_status)).
                        setTextColor(ContextCompat.getColor(mContext,R.color.order_status_use));
//                holder.setImageResource(R.id.my_order_wait_pay, R.drawable.myorder_pay_ok);
            }

            holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.GONE);
        }else if(order.getStatus() == 3) {
            holder.getView(R.id.my_order_cancel_text).setVisibility(View.GONE);
            holder.setText(R.id.my_order_wait_pay_status, "已过期");
//            holder.setImageResource(R.id.my_order_wait_pay, R.drawable.myorder_pay_alert);
            holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.GONE);
        }else if(order.getStatus() == 4) {
            holder.getView(R.id.my_order_cancel_text).setVisibility(View.GONE);
            holder.setText(R.id.my_order_wait_pay_status, "订单关闭");
//            holder.setImageResource(R.id.my_order_wait_pay, R.drawable.myorder_pay_cancel);
            holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.GONE);
        }else if(order.getStatus() == 5) {
            holder.getView(R.id.my_order_cancel_text).setVisibility(View.GONE);
            holder.setText(R.id.my_order_wait_pay_status, "订单取消");
//            holder.setImageResource(R.id.my_order_wait_pay, R.drawable.my_order_pay_cancel);
            holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.GONE);
        }
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
}
