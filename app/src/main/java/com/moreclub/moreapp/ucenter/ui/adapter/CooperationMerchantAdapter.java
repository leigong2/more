package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.GlideCircleTransform;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.List;

/**
 * Created by Captain on 2017/4/11.
 */

public class CooperationMerchantAdapter extends RecyclerAdapter<MerchantItem> {

    Context mContext;
    public CooperationMerchantAdapter(Context context, int layoutId,
                                 List<MerchantItem> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MerchantItem item) {

        Glide.with(mContext)
                .load(item.getThumb())
                .dontAnimate()
                .transform(new GlideCircleTransform(mContext, 2, ContextCompat.getColor(mContext,R.color.merchant_item_distance)))
                .into((ImageView) holder.getView(R.id.header_img));
        holder.setText(R.id.title, item.getName());
        holder.setText(R.id.address, item.getAddress());
        holder.setText(R.id.distance, item.getDistance());
        holder.setText(R.id.des, item.getSellingPoint());
    }
}
