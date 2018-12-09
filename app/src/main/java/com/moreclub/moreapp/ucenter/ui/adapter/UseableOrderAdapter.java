package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.ConsumeCode;
import com.moreclub.moreapp.ucenter.model.Order;
import com.moreclub.moreapp.ucenter.ui.activity.PkgOrderDetailActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class UseableOrderAdapter extends RecyclerAdapter<Order> {

    public UseableOrderAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final Order order) {
        String itemCount = order.getItemNum() == null ? "" : " x "+order.getItemNum() + " 份";
//        String price = "¥ "+order.getActualPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
//        String countPrice = "¥ "+order.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + itemCount;

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

        holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.GONE);
        holder.setText(R.id.my_order_total_text, "");
//        holder.setText(R.id.my_order_price, countPrice);
        holder.getView(R.id.order_operation).setVisibility(View.GONE);
        int unUsedCount = getTypeCount(order.getCodes(), 0);
        if (unUsedCount == order.getCodes().size()) {
            holder.setText(R.id.my_order_wait_pay_status, "未消费");
//            holder.setImageResource(R.id.my_order_wait_pay_status, R.drawable.myorder_pay_ok);
        } else if (unUsedCount < order.getCodes().size() && unUsedCount > 0) {
            holder.setText(R.id.my_order_wait_pay_status, "部分消费");
//            holder.setImageResource(R.id.my_order_wait_pay_status, R.drawable.my_order_wait_pay);
        } else {
            holder.setText(R.id.my_order_wait_pay_status, "已消费");
//            holder.setImageResource(R.id.my_order_wait_pay_status, R.drawable.my_order_wait_pay);
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
