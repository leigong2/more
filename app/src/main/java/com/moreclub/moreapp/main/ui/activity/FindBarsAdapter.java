package com.moreclub.moreapp.main.ui.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.List;

/**
 * Created by Captain on 2017/8/26.
 */

public class FindBarsAdapter extends RecyclerAdapter<MerchantItem> {

    public FindBarsAdapter(Context context, int layoutId, List<MerchantItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, MerchantItem merchantItem) {
        String title = merchantItem.getName();
        String desc = merchantItem.getAddress();
        holder.setText(R.id.merchantName, title);
        holder.setText(R.id.merchant_des, desc);
        if (merchantItem.isSelected()){
            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.VISIBLE);
        } else {
            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.GONE);
        }

        if (merchantItem.getMid() == -1) {
            holder.setVisible(R.id.merchant_des,false);
            TextView view = holder.getView(R.id.merchantName);
            view.setText("不显示所在位置");
            view.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            holder.setVisible(R.id.merchant_des,true);
        }
    }
}
