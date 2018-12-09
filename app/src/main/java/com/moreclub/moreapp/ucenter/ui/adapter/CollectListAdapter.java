package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.List;

/**
 * Created by Captain on 2017/4/10.
 */

public class CollectListAdapter extends RecyclerAdapter<CollectlistInfo> {

    public CollectListAdapter(Context context, int layoutId, List<CollectlistInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, CollectlistInfo collectlistInfo) {
        String photos = collectlistInfo.getThumb();
        float price = collectlistInfo.getPrice();
        String merchantName = collectlistInfo.getAddress();
        int personNum = collectlistInfo.getPersonNum();
        String title = collectlistInfo.getTitle();
        Glide.with(mContext)
                .load(photos)
                .dontAnimate()
                .into((ImageView) holder.getView(R.id.package_image_banner_iv));
        holder.setText(R.id.package_title, title);
        holder.setText(R.id.package_tag, merchantName);
        holder.setText(R.id.package_price, price+"/"+personNum+"人");
        holder.getView(R.id.package_collect_button).setVisibility(View.GONE);
    }
}
