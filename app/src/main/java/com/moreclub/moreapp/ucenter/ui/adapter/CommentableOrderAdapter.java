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
import com.moreclub.moreapp.ucenter.model.Order;
import com.moreclub.moreapp.ucenter.ui.activity.UserEvaluatePackageActivity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class CommentableOrderAdapter extends RecyclerAdapter<Order> {

    public CommentableOrderAdapter(Context context, int layoutId, List<Order> datas) {
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

        holder.setText(R.id.my_order_total_text, "");
        holder.setText(R.id.my_order_wait_pay_status, "已消费");
//        holder.setImageResource(R.id.my_order_wait_pay, R.drawable.my_order_wait_pay);
        holder.getView(R.id.my_order_wait_pay_text).setVisibility(View.VISIBLE);
        holder.setText(R.id.my_order_wait_pay_text, "去评价");
        holder.getView(R.id.order_operation).setVisibility(View.VISIBLE);
        holder.getView(R.id.my_order_wait_pay_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, UserEvaluatePackageActivity.class);
                intent.putExtra("packageName",order.getTitle());
                intent.putExtra("orderId",order.getOrderId());
                intent.putExtra("pid",order.getPid());
                ActivityCompat.startActivity( mContext, intent, compat.toBundle());
            }
        });
    }

}
