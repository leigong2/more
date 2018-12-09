package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.List;
/**
 * Created by Captain on 2017/4/10.
 */

public class CollectMerchantAdapter extends RecyclerAdapter<CollectlistInfo> {

    public CollectMerchantAdapter(Context context, int layoutId, List<CollectlistInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, CollectlistInfo collectlistInfo) {
            String url = collectlistInfo.getThumb();
            String title = collectlistInfo.getTitle();
            String desc = collectlistInfo.getIntroduction();
            String bizArea = collectlistInfo.getAddress();

            holder.setText(R.id.main_bar_title, title);
            holder.setText(R.id.main_bar_item_desc, desc);
            holder.setText(R.id.main_bar_bizarea, bizArea);
            holder.setText(R.id.main_bar_tags, "");
            holder.setText(R.id.main_bar_item_distance, "");
            ((TextView)holder.getView(R.id.main_bar_line)).setVisibility(View.GONE);

            Glide.with(mContext)
                    .load(url)
                    .dontAnimate()
                    .into((ImageView) holder.getView(R.id.main_bar_item_logo));
    }
}
