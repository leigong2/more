package com.moreclub.moreapp.ucenter.ui.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Captain on 2017/4/12.
 */

public class PrivilegeMerchantHeaderAdapter extends RecyclerAdapter<MerchantItem> {

    Context mContext;
    public PrivilegeMerchantHeaderAdapter(Context context, int layoutId,
                                      List<MerchantItem> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MerchantItem item) {

        Glide.with(mContext)
                .load(item.getThumb())
                .dontAnimate()
                .into((CircleImageView) holder.getView(R.id.header_item));
    }
}
