package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.Topic;

import java.util.List;

/**
 * Created by Captain on 2017/8/26.
 */

public class SelectTopicAdapter extends RecyclerAdapter<Topic> {

    public SelectTopicAdapter(Context context, int layoutId, List<Topic> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, Topic merchantItem) {
        if (merchantItem.getTid()==-1){
            holder.setText(R.id.merchantName, merchantItem.getName());
        } else {
            String title = "#" + merchantItem.getName() + "#";
            holder.setText(R.id.merchantName, title);
        }

//        if (merchantItem.isSelected()){
//            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.VISIBLE);
//        } else {
//            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.GONE);
//        }
//        String desc = merchantItem.getAddress();
//        holder.setText(R.id.merchantName, title);
//        holder.setText(R.id.merchant_des, desc);
//        if (merchantItem.isSelected()){
//            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.VISIBLE);
//        } else {
//            ((ImageButton) holder.getView(R.id.select)).setVisibility(View.GONE);
//        }
    }
}
